# ADR 0001: Add value slots for model-bound HTML views

- Status: Accepted
- Date: 2026-10-22
- Target release: 1.2.0

## Context

The Java HTML builder already offers `dynamic(BiConsumer<T, U>)`.  
It gives a view implementation a builder element and a model, leaving the
callback free to make arbitrary decisions while rendering.

That flexibility is useful, but makes model-dependent output opaque to a
view implementation.  
When the template only needs to emit values, render a collection, or choose
between two known branches, a more descriptive API can expose those operations
as part of the builder instead of requiring all work to happen inside a
general-purpose callback.

Passing values through generic APIs can also box primitive values.  
The view implementation can benefit from seeing the exact kind of value read
from the model, and primitive-specific accessors can avoid boxing at those
boundaries.

## Decision

Add an opt-in value-slots API to the generated Java HTML builder, while
retaining `dynamic(BiConsumer<T, U>)` unchanged.  
Both APIs coexist: `dynamic` remains available for arbitrary model-dependent
logic, and value slots are available when the template can express its
model interaction as typed reads, loops, or conditions.

The value-oriented methods on `ElementBase` register model accessors with the
visitor rather than receiving the model directly in the template:

- `rawOf` and `textOf` emit a model value with or without HTML escaping.
- `intOf`, `longOf`, `doubleOf`, and `boolOf` represent primitive reads with
  primitive-specialized Java functional interfaces.
- `attrOf` and `attrOfNullable` bind an attribute value to a model accessor.
- `forEachOf` and `forEachIndexedOf` bind collection iteration to the model.
- `whenOf` binds a condition and its true/optional false template.

`Slot<M>` is a type-inference aid passed to the nested template.  
It fixes the model or item type for accessor lambdas through `read`,
`readInt`, `readLong`, `readDouble`, and `readBool`; it does not store model
data.  
`forEachIndexedOf` exposes the current item and its zero-based position through
`Indexed<E>`, pairing items lazily as iteration advances.

These operations are surfaced to view implementations through corresponding
`ElementVisitorBase` hooks.  
New hooks have default implementations that report the feature as unsupported,
so existing visitor subclasses are not forced to implement them at compile
time.  
A visitor must override the relevant hooks before templates can use those
operations successfully.

The generated element API also supplies primitive overloads for eligible
attribute setters.  
Together with the primitive-specialized accessor types, this reduces boxing
where primitive values cross the builder API; it does not promise that all
rendering allocations or boxing are eliminated.

## Consequences

### Positive

- View implementations can identify dynamic values, attributes, iteration,
  and conditions instead of treating every model-dependent section as an
  opaque callback.
- A view implementation can use those explicit operations to optimize how it
  binds a model to a template and emits mostly static HTML.
- Primitive-specific accessors and attribute overloads avoid boxing at their
  respective API boundaries.
- Existing templates using `dynamic` retain their behavior and do not need to
  migrate.
- Indexed iteration avoids building a separate wrapper collection solely to
  attach indices to items.

### Trade-offs

- The builder and visitor API surface is larger, and view visitors must
  implement the hooks they intend to support.
- Value slots describe common rendering operations but do not replace
  arbitrary logic; use `dynamic` when the behavior cannot be expressed by
  these operations.
- The performance benefit depends on the visitor implementation and usage.
  The API provides opportunities to reduce boxing and optimize rendering;
  this change alone does not establish a measured speedup.
- Indexed iteration creates an `Indexed` value as each item is visited; it
  avoids an eager wrapper list, not every per-item allocation.

## Alternatives considered

- **Continue using only `dynamic`:** preserves a small API, but leaves simple
  value reads and common template structures opaque to the view
  implementation.
- **Replace `dynamic` with value slots:** rejected because arbitrary callback
  behavior remains useful and existing users should retain a compatible path.
- **Always materialize indexed collections before rendering:** rejected
  because templates should not need an intermediate collection just to access
  a loop index.

## References

- `ElementBase`: builder and value-slot API, including `dynamic`.
- `Slot` and `Indexed`: type inference and indexed iteration support.
- `ElementVisitorBase`: extension points for visitor implementations.
- Feature commits: `e88cb07` and `6d47a4b`, merged in `e39a0ed`.
