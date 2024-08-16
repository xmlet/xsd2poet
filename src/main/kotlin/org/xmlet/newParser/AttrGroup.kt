package org.xmlet.newParser

import org.w3c.dom.Node
import java.util.*

class AttrGroup(val name: String) {
    private val attrValues : LinkedList<Pair<String,String>> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    init {
        references.add("CustomAttributeGroup")
    }

    fun getAttrValuesList() : List<Pair<String,String>> = attrValues

    fun getRefsList() : List<String> = references

    fun addValue(node: Node) {
        if (node.nodeName == "xsd:attribute") {
            val pair = Pair(
                node.attributes.getNamedItem("name").nodeValue,
                node.attributes.getNamedItem("type").nodeValue
            )
            attrValues.add(pair)
        } else {
            references.remove("CustomAttributeGroup")
            references.add(node.attributes.getNamedItem("ref").nodeValue)
        }
    }
}
