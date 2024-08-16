package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*

class ElementXsd(val nameLowerCase: String) {
    private val attrValues : LinkedList<Pair<String, String>> = LinkedList()

    private val references: LinkedList<String> = LinkedList()

    private val sequenceElements: LinkedList<String> = LinkedList()

    private val sequenceGroupRef: LinkedList<String> = LinkedList()

    private var textGroupRemoved = false

    private val nameUpperCase = firstToUpper(nameLowerCase)

    //this flag decides if you should create or not the Complete class
    private var hasSequence = false

    init {
        references.add("TextGroup")
    }

    fun getSequenceGroupRef() = sequenceGroupRef

    fun hasSequence() = hasSequence
    
    fun setHasSequence() { hasSequence = true}

    fun getUpperCaseName() : String = nameUpperCase
    fun getAttrValuesList() : List<Pair<String, String>> = attrValues

    fun getReferencesList() : List<String> = references

    fun getSequenceElements() : List<String> = sequenceElements

    private fun removeTextGroup() {
        if (!textGroupRemoved) {
            references.remove("TextGroup")
            textGroupRemoved = true
        }
    }

    fun addSequenceElement(sequenceElement : String) {
        sequenceElements.add(sequenceElement)
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

    fun addSequenceGroup(nodeName: String) {
        sequenceGroupRef.add(nodeName)
    }
}