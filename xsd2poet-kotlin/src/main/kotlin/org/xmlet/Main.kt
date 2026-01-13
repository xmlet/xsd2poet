package org.xmlet

import org.xmlet.kotlinPoetGenerator.ClassGenerator.generateExtensionsForKotlin
import org.xmlet.newParser.Parser

fun main() {
    val parser = Parser()
    val xsdIn = Parser::class.java.classLoader.getResourceAsStream("html_5_2.xsd")
    checkNotNull(xsdIn)
    parser.parse(xsdIn)
    generateExtensionsForKotlin(parser)

}
