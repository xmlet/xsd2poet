package org.xmlet.newParser

fun String.firstToUpper(): String = replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() }
