package org.xmlet.htmlapifaster;

import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.function.BiConsumer;
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

    /**
     * @param element  The HTML element from previous builder.
     * @param consumer The continuation that consumes the element and a model.
     * @param <E>      The type of HTML element.
     * @param <U>      The type of the model.
     */
    public abstract <E extends Element, U> void visitDynamic(E element, BiConsumer<E, U> consumer);

    public abstract <E extends Element> void visitMfe(E element, Consumer<MfeConfigurationBuilder> data);

    public abstract <M, E extends Element> void visitAwait(E element, AwaitConsumer<E, M> asyncAction);
}