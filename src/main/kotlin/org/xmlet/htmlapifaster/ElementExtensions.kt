package org.xmlet.htmlapifaster

interface ElementExtensions<T: Element<*,*>, Z: Element<*,*>> : Element<T, Z> {
    operator fun String.unaryPlus(): T {
        return addTextFromkotlin(this)
    }
}