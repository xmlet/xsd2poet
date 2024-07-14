package org.xmlet.parser

import org.w3c.dom.Node
import java.util.*

class SimpleType(val upperCaseName: String, val baseType: String) {

    private val enumValues : LinkedList<Pair<String,String>> = LinkedList()

    private val restrictionValues : LinkedList<String> = LinkedList()

    init {

        if (baseType.lowercase() != "string")
            throw Exception("Enum with a base type that is not xsd:string is currently not supported")
    }

    fun addValue(node: Node) {
        val value = node.attributes.getNamedItem("value").nodeValue
        if (node.nodeName.contains("enumeration"))
            handleEnumeration(value)
        else
            restrictionValues.add(value)

    }

    fun handleEnumeration(value: String) {
        val enumName =
            if (value.isEmpty())
                "EMPTY"
            else if (value.toIntOrNull() != null)
                "_${value}"
            else if (value.length > 1)
                value.uppercase()
            else
                value
        enumValues.add(Pair(
            enumName
                .replace("/", "_")
                .replace("-","_")
                .replace(".","_")
            ,value))
    }

    fun getList() : List<Pair<String,String>> = enumValues

    fun getRestrictionList(): List<String> = restrictionValues

}