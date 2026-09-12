package org.xmlet.htmlapifaster;

import org.jetbrains.annotations.NotNull;
import org.xmlet.htmlapifaster.async.AsyncElement;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public interface ElementBase<T extends Element, Z extends Element> extends AsyncElement<T> {
    T self();

    ElementVisitor getVisitor();

    String getName();

    Z __();

    Z getParent();

    default  T addTextFromkotlin(@NotNull String txt) {
        this.getVisitor().visitRaw(new Text(this.self(), this.getVisitor(), txt));
        return this.self();
    }

    /**
     * To distinguish from text() that escapes HTML by default.
     * This raw() acts like text() but keeping text as it is.
     */
    default <R> T raw(R text) {
        this.getVisitor().visitRaw(new Text(this.self(), this.getVisitor(), text));
        return this.self();
    }


    /**
     * Executes an async operation on a certain model
     * @param asyncAction The async action to be executed on a element and model
     * @return The processed element
     * @param <M> Generic type fo the received model
     */
    @Override
    default <M> T await(AwaitConsumer<T,M> asyncAction) {
        final T self = self();
        this.getVisitor().visitAwait(self, asyncAction);
        return self;
    }

    /**
     * @param consumer The continuation that consumes the element and a model.
     * @return The same element that is passed to the consumer, corresponding to this element, i.e. self.
     * @param <U> The type of the model.
     */
    default <U> T dynamic(BiConsumer<T, U> consumer) {
        T self = this.self();
        this.getVisitor().visitDynamic(self, consumer);
        return self;
    }

    default T mfe(Consumer<MfeConfiguration> data) {
        T self = this.self();
        this.getVisitor().visitMfe(self, data);
        return self;
    }

    default T of(Consumer<T> consumer) {
        T self = this.self();
        consumer.accept(self);
        return self;
    }


    /**
     * Emits a single value read from the model, without escaping.
     * @param accessor The function that reads the value from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T rawOf(Function<M, ?> accessor) {
        this.getVisitor().visitValueRaw(accessor);
        return this.self();
    }

    /**
     * Emits a single value read from the model, with HTML escaping.
     * @param accessor The function that reads the value from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T textOf(Function<M, ?> accessor) {
        this.getVisitor().visitValueText(accessor);
        return this.self();
    }

    /**
     * Emits an int read from the model, without boxing.
     * @param accessor The function that reads the int from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T intOf(ToIntFunction<M> accessor) {
        this.getVisitor().visitValueInt(accessor);
        return this.self();
    }

    /**
     * Emits a long read from the model, without boxing.
     * @param accessor The function that reads the long from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T longOf(ToLongFunction<M> accessor) {
        this.getVisitor().visitValueLong(accessor);
        return this.self();
    }

    /**
     * Emits a double read from the model, without boxing.
     * @param accessor The function that reads the double from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T doubleOf(ToDoubleFunction<M> accessor) {
        this.getVisitor().visitValueDouble(accessor);
        return this.self();
    }

    /**
     * Emits a boolean read from the model, as true or false.
     * @param accessor The predicate that reads the boolean from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T boolOf(Predicate<M> accessor) {
        this.getVisitor().visitValueBoolean(accessor);
        return this.self();
    }

    /**
     * Emits an attribute with a fixed name and a value read from the model.
     * @param name The name of the attribute
     * @param accessor The function that reads the value from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T attrOf(String name, Function<M, ?> accessor) {
        this.getVisitor().visitValueAttribute(name, accessor);
        return this.self();
    }

    /**
     * Emits an attribute only when the accessor does not return null.
     * @param name The name of the attribute
     * @param accessor The function that reads the value from the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T attrOfNullable(String name, Function<M, ?> accessor) {
        this.getVisitor().visitValueAttributeNullable(name, accessor);
        return this.self();
    }

    /**
     * Emits the item template once for each element of a collection read from the model.
     * @param items The function that reads the collection from the model
     * @param itemTemplate The template that consumes the element of each item
     * @return The same element, i.e. self
     * @param <M> The type of the model
     * @param <E> The type of the items of the collection
     */
    default <M, E> T forEachOf(
        Function<M, ? extends Iterable<E>> items,
        Consumer<T> itemTemplate
    ) {
        T self = this.self();
        this.getVisitor().visitForEach(items, self, itemTemplate);
        return self;
    }

    /**
     * As forEachOf(Function, Consumer), and it also passes a Slot that fixes the type of the
     * item, so that the lambdas of the template infer it.
     * @param items The function that reads the collection from the model
     * @param itemTemplate The template that consumes the element and the Slot of the item
     * @return The same element, i.e. self
     * @param <M> The type of the model
     * @param <E> The type of the items of the collection
     */
    default <M, E> T forEachOf(
        Function<M, ? extends Iterable<E>> items,
        BiConsumer<T, Slot<E>> itemTemplate
    ) {
        return forEachOf(items, item -> itemTemplate.accept(item, Slot.of()));
    }

    /**
     * Emits the body only when the condition is true for the model. The body has to close every
     * element that it opens. For a conditional attribute we use attrOfNullable instead, because
     * this closes the begin tag of the element before the body runs.
     * @param condition The predicate that we test on the model
     * @param body The template that consumes the element when the condition is true
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T whenOf(
        Predicate<M> condition,
        Consumer<T> body
    ) {
        return whenOf(condition, body, null);
    }

    /**
     * As whenOf(Predicate, Consumer), with a template for the false case.
     * @param condition The predicate that we test on the model
     * @param body The template that consumes the element when the condition is true
     * @param orElse The template for the false case, which may be null to emit nothing
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T whenOf(
        Predicate<M> condition,
        Consumer<T> body,
        Consumer<T> orElse
    ) {
        T self = this.self();
        this.getVisitor().visitWhen(condition, self, body, orElse);
        return self;
    }

    /**
     * As whenOf(Predicate, Consumer), and it also passes a Slot that fixes the type of the
     * model, so that the lambdas of the template infer it.
     * @param condition The predicate that we test on the model
     * @param body The template that consumes the element and the Slot of the model
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T whenOf(
        Predicate<M> condition,
        BiConsumer<T, Slot<M>> body
    ) {
        return whenOf(condition, body, null);
    }

    /**
     * As whenOf(Predicate, Consumer, Consumer), and it also passes a Slot that fixes the type of
     * the model, so that the lambdas of the templates infer it.
     * @param condition The predicate that we test on the model
     * @param body The template that consumes the element and the Slot of the model
     * @param orElse The template for the false case, which may be null to emit nothing
     * @return The same element, i.e. self
     * @param <M> The type of the model
     */
    default <M> T whenOf(
        Predicate<M> condition,
        BiConsumer<T, Slot<M>> body,
        BiConsumer<T, Slot<M>> orElse
    ) {
        return whenOf(
            condition,
            elem -> body.accept(elem, Slot.of()),
            orElse == null ? null : elem -> orElse.accept(elem, Slot.of())
        );
    }
}
