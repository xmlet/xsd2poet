package org.xmlet.parser

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.lang.StringBuilder
import java.util.HashSet
import java.util.LinkedList
import javax.xml.parsers.DocumentBuilderFactory


fun main() {
    val parser = Parser()
    parser.parser()
    print("teste")
}

class Parser {
    private val simpleTypeList = LinkedList<SimpleType>()

    private val groupList = LinkedList<Group>()

    private val attrGroupList = LinkedList<AttrGroup>()

    private val elementList = LinkedList<ElementXsd>()

    private val choiceList = LinkedList<Choice>()

    private val restrictionNodeSet = hashSetOf("xsd:restriction")

    private val complexTypeSet = hashSetOf("xsd:complexType")

    private val choiceAndAllSet = hashSetOf("xsd:all", "xsd:choice")

    fun parser() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(File("src/main/resources/html_5_2.xsd"))
        doc.documentElement.normalize()

        printInformation(doc.childNodes,0)

    }

    fun printInformation(list: NodeList, importance: Int) {
        val importanceStr = StringBuilder()
        repeat(importance) {
            importanceStr.append("  ")
        }
        repeat(list.length) {x ->
            val schema = list.item(x)
            repeat(schema.childNodes.length) { y ->
                val current = schema.childNodes.item(y)
                if (current.nodeName.contains("xsd:")) {
                    if (current.nodeName.contains("simpleType")) {
                        createSimpleType(current)
                    } else if (current.nodeName.contains("group")) {
                        createGroup(current)
                    } else if (current.nodeName.contains("attributeGroup")) {
                        createAttributeGroup(current)
                    } else if (current.nodeName.contains("element")) {
                        createElement(current)
                    }
                    //print(importanceStr)
                    //println("${current.nodeName} - ${current.attributes.getNamedItem("name")}")
                    //printInformation(current.childNodes, importance + 1)
                }
            }
        }
    }

    private fun createElement(current: Node) {
        val element = ElementXsd(current.attributes.getNamedItem("name").nodeValue)
        current.childNodes.getFirstNodeWithType(complexTypeSet)?.let { complexType ->
            complexType.childNodes.forEachXsdElement { childNode ->
                element.addValue(childNode)
                if (childNode.isChoice()) {
                    createChoice(childNode,element.name)
                }
            }
            elementList.add(element)
        }
    }

    fun Node.isChoice() = nodeName.contains("choice")

    private fun createChoice(current: Node, parentName : String) {
        val choice = Choice("${parentName}Choice")
        current.childNodes.forEachXsdElement { childNode ->
            choice.addValue(childNode)
        }
        choiceList.add(choice)

    }

    private fun createAttributeGroup(current: Node) {
        val attrGroup = AttrGroup(current.attributes.getNamedItem("name").nodeValue)
        current.childNodes.forEachXsdElement {
            attrGroup.addValue(it)
        }
        attrGroupList.add(attrGroup)
    }

    private fun createSimpleType(current : Node) {
        current.childNodes.getFirstNodeWithType(restrictionNodeSet)?.let { restriction ->
            val simpleType = SimpleType(
                    current.attributes.getNamedItem("name").nodeValue,
                    restriction.attributes.getNamedItem("base").nodeValue.substring("xsd:".length)
                )
            restriction.childNodes.forEachXsdElement {
                simpleType.addValue(it.attributes.getNamedItem("value").nodeValue)
            }
            simpleTypeList.add(simpleType)
        }
    }

    private fun createGroup(current: Node) {
        current.childNodes.getFirstNodeWithType(choiceAndAllSet)?.let { root ->
            val group = Group(
                current.attributes.getNamedItem("name").nodeValue,
                root.nodeName
            )
            root.childNodes.forEachXsdElement {
                group.addValue(it)
            }
            groupList.add(group)
        }

    }
}

fun NodeList.forEachXsdElement(block: (Node) -> Unit) {
    for (i in 0 until length) {
        val node = item(i)
        if (node.nodeName.contains("xsd:"))
        block(node)
    }
}

fun NodeList.getFirstNodeWithType(typeSet: HashSet<String>) : Node? {
    for (i in 0 until length) {
        val node = item(i)
        if (typeSet.contains(node.nodeName))
            return node
    }
    return null
}