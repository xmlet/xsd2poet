package org.xmlet.parser

import org.w3c.dom.Node
import java.util.*

class ElementXsd(val name: String) {
    private val attrValues : LinkedList<Pair<String, String>> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    fun addValue(node: Node) {
        when (node.nodeName) {
            "xsd:attribute" -> {
                val pair = Pair(
                    node.attributes.getNamedItem("name").nodeValue,
                    node.attributes.getNamedItem("type").nodeValue
                )
                attrValues.add(pair)
            }
            "xsd:attributeGroup" -> {
                references.add(node.attributes.getNamedItem("ref").nodeValue)
            }
            "choice" -> {
                references.add("${name}Choice")
            }
        }
    }

    fun getList() : List<Pair<String,String>> = attrValues
}