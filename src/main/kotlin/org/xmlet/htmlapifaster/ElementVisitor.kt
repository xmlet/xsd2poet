package org.xmlet.htmlapifaster

import org.xmlet.htmlapifaster.async.AwaitConsumer
import java.util.function.BiConsumer

abstract class ElementVisitor {
    /**
     * To distinguish boolean attributes from others.
     *
     * @param name Attribute name
     * @param value Attribute value
     */
    abstract fun visitAttributeBoolean(name: String?, value: String?)

    abstract fun visitAttribute(var1: String?, var2: String?)

    abstract fun visitParent(var1: Element<*, *>?)

    abstract fun <R> visitText(var1: Text<out Element<*, *>?, R>?)

    /**
     * To distinguish from text() that escapes HTML by default.
     * This raw() acts like text() but keeping text as it is.
     */
    abstract fun <R> visitRaw(txt: Text<out Element<*, *>?, R>?)

    abstract fun <R> visitComment(var1: Text<out Element<*, *>?, R>?)

    /**
     * @param element The HTML element from previous builder.
     * @param consumer The continuation that consumes the element and a model.
     * @param <E> The type of HTML element.
     * @param <U> The type of the model.
    </U></E> */
    abstract fun <E : Element<*, *>?, U> visitDynamic(element: E, consumer: BiConsumer<E, U>?)

    abstract fun <M, E : Element<*, *>?> visitAwait(element: E, asyncAction: AwaitConsumer<E, M>?)

    abstract fun <M, E : Element<*, *>?> visitSuspending(element: E, suspendAction: SuspendConsumer<E, M>?)

}
