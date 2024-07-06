package org.xmlet.parser

import java.util.*

class SimpleType(val name: String, val baseType: String) {
    private val enumValues : LinkedList<String> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    init {
        references.add("EnumInterface")
    }

    fun addValue(value: String) {
        enumValues.add(value)
    }

    fun getList() : List<String> = enumValues

}