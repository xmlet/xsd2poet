package org.xmlet.htmlapifaster;

import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.Consumer;

public abstract class ElementVisitorBase {
    public abstract void visitElement(Element var1);

    public abstract void visitAttribute(String var1, String var2);

    /**
     * To distinguish boolean attributes from others.
     *
     * @param name  Attribute name
     * @param value Attribute value
     */
    public abstract void visitAttributeBoolean(String name, String value);

    public abstract void visitParent(Element var1);

    public abstract <R> void visitText(Text<? extends Element, R> var1);

    /**
     * To distinguish from text() that escapes HTML by default.
     * This raw() acts like text() but keeping text as it is.
     */
    public abstract <R> void visitRaw(Text<? extends Element, R> txt);

    public abstract <R> void visitComment(Text<? extends Element, R> var1);

    /*
     * Value slots. Each one carries an accessor we apply to the model of the render, so the
     * preprocessor knows a single value is emitted here and keeps the surrounding HTML static.
     */

    public <M> void visitValueRaw(Function<M, ?> accessor) {
        throw unsupported("rawOf");
    }

    public <M> void visitValueText(Function<M, ?> accessor) {
        throw unsupported("textOf");
    }

    public <M> void visitValueInt(ToIntFunction<M> accessor) {
        throw unsupported("intOf");
    }

    public <M> void visitValueLong(ToLongFunction<M> accessor) {
        throw unsupported("longOf");
    }

    public <M> void visitValueBoolean(Predicate<M> accessor) {
        throw unsupported("boolOf");
    }

    public <M> void visitValueDouble(ToDoubleFunction<M> accessor) {
        throw unsupported("doubleOf");
    }

    public <M> void visitValueAttribute(String name, Function<M, ?> accessor) {
        throw unsupported("attrOf");
    }

    /** As visitValueAttribute, but we do not write the attribute when the value is null. */
    public <M> void visitValueAttributeNullable(
        String name,
        Function<M, ?> accessor
    ) {
        throw unsupported("attrOfNullable");
    }

    /**
     * Emits an item template once for each element of a collection read from the model. The item
     * template is opaque here because this module knows nothing about views; the visitor that
     * implements this method resolves it.
     */
    public <M, E, T extends Element> void visitForEach(
        Function<M, ? extends Iterable<E>> items,
        T element,
        Consumer<T> itemTemplate
    ) {
        throw unsupported("forEachOf");
    }

    /**
     * Preencodes both HTML blocks of a conditional and emits one of them in each render.
     * @param orElse The block for the false case, which may be null to emit nothing
     */
    public <M, T extends Element> void visitWhen(
        Predicate<M> condition,
        T element,
        Consumer<T> body,
        Consumer<T> orElse
    ) {
        throw unsupported("whenOf");
    }

    /**
     * @param element  The HTML element from previous builder.
     * @param consumer The continuation that consumes the element and a model.
     * @param <E>      The type of HTML element.
     * @param <U>      The type of the model.
     */
    public abstract <E extends Element, U> void visitDynamic(E element, BiConsumer<E, U> consumer);

    public abstract <E extends Element> void visitMfe(E element, Consumer<MfeConfiguration> data);

    public abstract <M, E extends Element> void visitAwait(E element, AwaitConsumer<E, M> asyncAction);

    /**
     * Creates the exception for a feature that this visitor does not support. Every method above
     * is a default and not an abstract declaration, so that a new one does not break the visitors
     * of an earlier release. A visitor supports a feature when it overrides its method.
     */
    protected UnsupportedOperationException unsupported(String feature) {
        return new UnsupportedOperationException(
            getClass().getSimpleName() + " does not support " + feature
        );
    }
}
