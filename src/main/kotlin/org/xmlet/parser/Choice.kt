package org.xmlet.parser

import org.w3c.dom.Node
import java.util.*

class Choice(val name: String) {
    private val choiceValues : LinkedList<String> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    init {
        references.add("TextGroup")
    }

    fun addValue(node: Node) {
        val value = node.attributes.getNamedItem("ref").nodeValue
        if (node.nodeName == "xsd:element")
            choiceValues.add(value)
        else if (node.nodeName == "xsd:group") {
            references.remove("TextGroup")
            references.add(value)
        }
    }

    fun getList() : List<String> = choiceValues
}