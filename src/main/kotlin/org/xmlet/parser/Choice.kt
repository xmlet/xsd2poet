package org.xmlet.parser

import org.w3c.dom.Node
import java.util.*

class Choice(val name: String) {
    private val choiceValues : LinkedList<String> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    private var textGroupRemoved = false

    init {
        references.add("TextGroup")
    }

    private fun removeTextGroup() {
        if (!textGroupRemoved) {
            references.remove("TextGroup")
            textGroupRemoved = true
        }
    }

    fun addValue(node: Node) {
        val value = node.attributes.getNamedItem("ref").nodeValue
        if (node.nodeName == "xsd:element")
            choiceValues.add(value)
        else if (node.nodeName == "xsd:group") {
            removeTextGroup()
            references.add(value)
        }
    }

    fun getChoiceList() : List<String> = choiceValues

    fun getRefList() : List<String> = references
}