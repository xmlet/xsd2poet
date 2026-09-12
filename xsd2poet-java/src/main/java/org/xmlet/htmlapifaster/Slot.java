package org.xmlet.htmlapifaster;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Fixes the type of the model that a block of slots reads from. The slot methods take an
 * accessor over an unbounded M, so a lambda passed straight to one of them has to spell out
 * that type. A Slot fixes it once, so the lambdas of the block infer it instead. Every method
 * returns the accessor it receives unchanged; it exists only for the type it pins.
 *
 * @param <M> The type of the model that the block reads from
 * @author Bernardo Pereira
 */
public final class Slot<M> {

    private static final Slot<?> INSTANCE = new Slot<>();

    private Slot() {}

    @SuppressWarnings("unchecked")
    static <M> Slot<M> of() {
        return (Slot<M>) INSTANCE;
    }

    /**
     * Fixes the type of an accessor for rawOf, textOf, attrOf or attrOfNullable. It keeps the
     * result type too, so it also serves the items of a nested forEachOf.
     * @param accessor The function that reads the value from the model
     * @return The same accessor
     * @param <R> The type of the value that the accessor reads
     */
    public <R> Function<M, R> read(Function<M, R> accessor) {
        return accessor;
    }

    /**
     * Fixes the type of an accessor for intOf.
     * @param accessor The function that reads the int from the model
     * @return The same accessor
     */
    public ToIntFunction<M> readInt(ToIntFunction<M> accessor) {
        return accessor;
    }

    /**
     * Fixes the type of an accessor for longOf.
     * @param accessor The function that reads the long from the model
     * @return The same accessor
     */
    public ToLongFunction<M> readLong(ToLongFunction<M> accessor) {
        return accessor;
    }

    /**
     * Fixes the type of an accessor for doubleOf.
     * @param accessor The function that reads the double from the model
     * @return The same accessor
     */
    public ToDoubleFunction<M> readDouble(ToDoubleFunction<M> accessor) {
        return accessor;
    }

    /**
     * Fixes the type of an accessor for boolOf, or of a condition of whenOf.
     * @param accessor The predicate that reads the boolean from the model
     * @return The same accessor
     */
    public Predicate<M> readBool(Predicate<M> accessor) {
        return accessor;
    }
}
