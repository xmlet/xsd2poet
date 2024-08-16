package org.xmlet.newParser

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlet.utils.Utils.firstToUpper
import java.io.File
import java.util.HashSet
import java.util.LinkedList
import javax.xml.parsers.DocumentBuilderFactory


fun main() {
    val parser = Parser()
    parser.parse()
    print("teste")
}

class Parser {
    private val simpleTypeList = LinkedList<SimpleType>()

    private val groupList = LinkedList<Group>()

    private val attrGroupList = LinkedList<AttrGroup>()

    private val elementList = LinkedList<ElementXsd>()

    private val choiceList = LinkedList<Choice>()

    private val elementCompleteList = LinkedList<ElementComplete>()

    private val restrictionNodeSet = hashSetOf("xsd:restriction")

    private val complexTypeSet = hashSetOf("xsd:complexType")

    private val choiceAndAllSet = hashSetOf("xsd:all", "xsd:choice")

    fun getElementCompleteList() : List<ElementComplete> = elementCompleteList

    fun getAttrGroupsList(): List<AttrGroup> = attrGroupList

    fun getSimpleTypeList(): List<SimpleType> = simpleTypeList

    fun getGroupList(): List<Group> = groupList

    fun getChoiceList(): List<Choice> = choiceList

    fun getElementsList(): List<ElementXsd> = elementList

    fun parse() {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(File("src/main/resources/html_5_2.xsd"))
        doc.documentElement.normalize()

        parseXsdData(doc.childNodes)//,0)

    }

    fun parseXsdData(list: NodeList) {
        repeat(list.length) {x ->
            val schema = list.item(x)
            repeat(schema.childNodes.length) { y ->
                val current = schema.childNodes.item(y)
                if (current.nodeName.contains("xsd:")) {
                    if (current.nodeName.contains("simpleType") || current.nodeName.contains("complexType")) {
                        createSimpleType(current)
                    } else if (current.isGroup()) {
                        createGroup(current)
                    } else if (current.nodeName.contains("attributeGroup")) {
                        createAttributeGroup(current)
                    } else if (current.isElement()) {
                        createElement(current)
                    }
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
                    createChoice(childNode,"${element.getUpperCaseName()}Choice")
                } else if (childNode.isAll()) {
                    createChoice(childNode,"${element.getUpperCaseName()}All")
                } else if (childNode.isSequence()) {
                    element.setHasSequence()
                    childNode.childNodes.forEachXsdElement {sequenceChildren ->
                        var elementComplete: ElementComplete? = null
                        if (sequenceChildren.isElement()) {
                            element.addSequenceElement(sequenceChildren.attributes.getNamedItem("ref").nodeValue)
                        } else if (sequenceChildren.isGroup()) {
                            if (elementComplete == null) {
                                elementComplete = ElementComplete(element.getUpperCaseName() + "Summary", element.getUpperCaseName())
                                elementCompleteList.add(elementComplete)
                            }
                            elementComplete.addDependency(sequenceChildren.attributes.getNamedItem("ref").nodeValue)
                        }
                    }

                }
            }
            elementList.add(element)
        }
        if (!elementList.contains(element)) {
            element.addValue(current)
            elementList.add(element)
        }
    }

    fun Node.isGroup() = nodeName.contains("group")

    fun Node.isElement() = nodeName.contains("element")

    fun Node.isChoice() = nodeName.contains("choice")

    fun Node.isAll() = nodeName.contains("all")

    fun Node.isSequence() = nodeName.contains("sequence")

    private fun createChoice(current: Node, name : String) {
        val choice = Choice(name)
        current.childNodes.forEachXsdElement { childNode ->
            choice.addValue(childNode)
        }
        choiceList.add(choice)
    }

    private fun createGroup(current: Node) {
        current.childNodes.getFirstNodeWithType(choiceAndAllSet)?.let { root ->
            val group = Group(
                firstToUpper(current.attributes.getNamedItem("name").nodeValue),
                root.nodeName
            )
            root.childNodes.forEachXsdElement {
                group.addValue(it)
            }
            groupList.add(group)
        }
    }

    private fun createAttributeGroup(current: Node) {
        val attrGroup = AttrGroup(current.attributes.getNamedItem("name").nodeValue)
        current.childNodes.forEachXsdElement {
            attrGroup.addValue(it)
        }
        attrGroupList.add(attrGroup)
    }

    private fun createSimpleType(current : Node) {
        if (current.nodeName.contains("simpleType")) {
            current.childNodes.getFirstNodeWithType(restrictionNodeSet)?.let { restriction ->
                val simpleType = SimpleType(
                    "Enum${firstToUpper(current.attributes.getNamedItem("name").nodeValue)}",
                    restriction.attributes.getNamedItem("base").nodeValue.substring("xsd:".length)
                )
                restriction.childNodes.forEachXsdElement {
                    simpleType.addValue(it);
                }
                if (simpleType.getList().isNotEmpty() || simpleType.getRestrictionList().isNotEmpty())
                    simpleTypeList.add(simpleType)
            }
        } else {
            current.childNodes.getFirstNodeWithType(hashSetOf("xsd:group"))?.let { group ->
                val choice = Choice(firstToUpper(current.attributes.getNamedItem("name").nodeValue))
                choice.addValue(group)
                choiceList.add(choice)
            }
        }
    }
}

fun NodeList.forEachXsdElement(block: (Node) -> Unit) {
    for (i in 0 until length) {
        val node = this.item(i)
        if (node.nodeName.contains("xsd:"))
            block(node)
    }
}

fun NodeList.getFirstNodeWithType(typeSet: HashSet<String>) : Node? {
    for (i in 0 until length) {
        val node = this.item(i)
        if (typeSet.contains(node.nodeName))
            return node
    }
    return null
}