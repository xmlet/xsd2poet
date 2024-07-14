package org.xmlet.parser

import org.w3c.dom.Node
import java.util.*

class Group(val name: String, type: String) {
    private val groupValues : LinkedList<String> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    init {
        if (type == "xsd:all")
            references.add("TextGroup")
    }

    fun addValue(node: Node) {
        val value = node.attributes.getNamedItem("ref").nodeValue
        if (node.nodeName == "xsd:element")
            groupValues.add(value)
        else
            references.add(value)
    }

    fun getChoiceList() : List<String> = groupValues

    fun getRefList() : List<String> = references
}