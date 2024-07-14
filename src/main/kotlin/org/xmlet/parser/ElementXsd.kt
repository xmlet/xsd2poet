package org.xmlet.parser

import org.w3c.dom.Node
import org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper
import java.util.*

class ElementXsd(val nameLowerCase: String) {
    private val attrValues : LinkedList<Pair<String, String>> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    private var textGroupRemoved = false

    private val nameUpperCase = firstToUpper(nameLowerCase)

    init {
        references.add("TextGroup")
    }

    fun getUpperCaseName() : String = nameUpperCase
    fun getAttrValuesList() : List<Pair<String, String>> = attrValues

    fun getReferencesList() : List<String> = references

    private fun removeTextGroup() {
        if (!textGroupRemoved) {
            references.remove("TextGroup")
            textGroupRemoved = true
        }
    }

    fun addValue(node: Node) {
        when (node.nodeName) {
            "xsd:attribute" -> {
                val pair = Pair(
                    firstToUpper(node.attributes.getNamedItem("name").nodeValue),
                    node.attributes.getNamedItem("type").nodeValue
                )
                attrValues.add(pair)
            }
            "xsd:attributeGroup" -> {
                references.add(firstToUpper(node.attributes.getNamedItem("ref").nodeValue))
            }
            "xsd:choice" -> {
                removeTextGroup()
                references.add("${nameUpperCase}Choice")
            }
            "xsd:all" -> {
                removeTextGroup()
                references.add("${nameUpperCase}All")
            }
            "xsd:group" -> {
                removeTextGroup()
                references.add(firstToUpper(node.attributes.getNamedItem("ref").nodeValue))

            }
            "xsd:element" -> {
                removeTextGroup()
                references.add(firstToUpper(node.attributes.getNamedItem("type").nodeValue))
                references.add("GlobalAttributes")
            }
        }
    }

    fun getList() : List<Pair<String,String>> = attrValues
}