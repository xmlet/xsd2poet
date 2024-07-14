package org.xmlet.parser

import org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper
import java.util.*

class SimpleType(val upperCaseName: String, val baseType: String) {

    private val enumValues : LinkedList<Pair<String,String>> = LinkedList()

    private val restrictionValues : LinkedList<String> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    init {
        references.add("EnumInterface")

        if (baseType.lowercase() != "string")
            throw Exception("Enum with a base type that is not xsd:string is currently not supported")
    }

    fun addValue(value: String) {
        val enumName =
            if (value.isEmpty())
                "EMPTY"
            else if (value.toIntOrNull() != null)
                "_${value}"
            else
                value
        enumValues.add(Pair(
            enumName
                .uppercase()
                .replace("/", "_")
                .replace("-","_")
                .replace(".","_")
            ,value))
    }

    fun getList() : List<Pair<String,String>> = enumValues

    fun getRefsList() : List<String> = references

}