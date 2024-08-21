package org.xmlet.newParser

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlet.utils.Utils.firstToUpper
import java.io.File
import java.util.HashSet
import java.util.LinkedList
import javax.xml.parsers.DocumentBuilderFactory


/**
 * Class used to Parse the provided xsd file into objects the javaPoetGenerator understands
 * */
class Parser {

    private val simpleTypeList = LinkedList<SimpleType>()

    private val groupList = LinkedList<Group>()

    private val attrGroupList = LinkedList<AttrGroup>()

    private val elementList = LinkedList<ElementXsd>()

    private val choiceList = LinkedList<Choice>()

    private val elementCompleteList = LinkedList<ElementComplete>()

    fun getElementCompleteList() : List<ElementComplete> = elementCompleteList

    fun getAttrGroupsList(): List<AttrGroup> = attrGroupList

    fun getSimpleTypeList(): List<SimpleType> = simpleTypeList

    fun getGroupList(): List<Group> = groupList

    fun getChoiceList(): List<Choice> = choiceList

    fun getElementsList(): List<ElementXsd> = elementList


    /**
     * This method has the objective of parsing the xsd file and populating all the lists representing the
     * @param pathFromSource    this is a String value representing the path of the xsd file,
     *                          representing the path from the src directory (including it) with the xsd file name included
     * Example: "src/main/resources/html_5_2.xsd"
     *
     * */
    fun parse(pathFromSource: String) {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(File(pathFromSource))
        doc.documentElement.normalize()

        parseXsdData(doc.childNodes)

    }

    /**
     * This method will get each child element of the xsd:schema base element and call to corresponding create function
     * <xsd:schema>
     *      <xsd:simpleType/>       <---- idx = 0
     *      <xsd:complexType/>      <---- idx = 1
     *      <xsd:group/>            <---- idx = 2
     *      <xsd:attributeGroup/>   <---- idx = 3
     *      <xsd:element/>          <---- idx = 4
     *              ...
     * </xsd:schema>
     * @param list this is the NodeList object representing the xsd file
     * */
    private fun parseXsdData(list: NodeList) {
        val schema = list.item(0)
        repeat(schema.childNodes.length) { idx ->
            val currentChildNode = schema.childNodes.item(idx)
            when(currentChildNode.nodeName) {
                XsdElementNames.SIMPLE_TYPE.value -> createSimpleType(currentChildNode)
                XsdElementNames.COMPLEX_TYPE.value -> createComplexType(currentChildNode)
                XsdElementNames.GROUP.value -> createGroup(currentChildNode)
                XsdElementNames.ATTRIBUTE_GROUP.value -> createAttributeGroup(currentChildNode)
                XsdElementNames.ELEMENT.value -> createElement(currentChildNode)
            }
        }
    }


    /**
     * This function will be called for each xsd node called xsd:element
     * It will create the elementXsd Object add the values to it and add the object to the elements list
     *
     *
     * Example to explain the iteration used bellow:
     * <xsd:element name="html">
     *      <xsd:complexType>                                        <--------- complexType
     *          <xsd:choice> ... </xsd:choice>                       <---------  childNode1 (createChoice with "Choice" in the className)
     *          <xsd:all> ... </xsd:all>                             <---------  childNode2 (createChoice with "All" in the className)
     *          <xsd:attributeGroup ref="globalAttributes" />        <---------  childNode3
     *          <xsd:attribute name="manifest" type="xsd:anyURI" />  <---------  childNode4
     *          <xsd:sequence/>                                       <---------  childNode5
     *              <xsd:element ref="summary"/>                     <---------      sequenceChildren1
     *              <xsd:group ref="flowContent" />                  <---------      sequenceChildren2
     *          </xsd:sequence>
     *      </xsd:complexType>
     * </xsd:element>
     *
     * @param current It's the xsd:element node being parsed
     * */
    private fun createElement(current: Node) {
        val element = ElementXsd(current.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue)


        current.childNodes.getFirstNodeWithType(complexTypeSet)?.let { complexType ->
            complexType.childNodes.forEachXsdElement { childNode ->
                element.addValue(childNode)
                when(childNode.nodeName) {
                    XsdElementNames.CHOICE.value ->
                        createChoice(childNode, element.getLowerCaseName())
                    XsdElementNames.ALL.value ->
                        createAll(childNode,element.getLowerCaseName())
                    XsdElementNames.SEQUENCE.value ->
                        createSequence(childNode, element)
                }
            }
            elementList.add(element)
        }
        //if this xsd:element doesn't have any children, check the node itself to add to the list
        if (!elementList.contains(element)) {
            element.addValue(current)
            elementList.add(element)
        }
    }

    /**
     *
     * Example:
     * <xsd:element>                                             <--------- elementNode (element is the object representing this node)
     *      <xsd:sequence/>                                      <---------  sequenceNode
     *          <xsd:element ref="summary"/>                     <---------      sequenceChildren1
     *          <xsd:group ref="flowContent" />                  <---------      sequenceChildren2
     *      </xsd:sequence>
     * <xsd:element/>
     * @param sequenceNode it's the xsd:sequence node being parsed
     * @param element It's the Parsers Element class representation with the xsd:sequence node inside it
     *
     *
     * */
    private fun createSequence(sequenceNode: Node, element: ElementXsd) {
        val elementComplete = ElementComplete(element.getLowerCaseName())
        elementCompleteList.add(elementComplete)
        sequenceNode.childNodes.forEachXsdElement { sequenceChildren ->
            when (sequenceChildren.nodeName) {
                XsdElementNames.ELEMENT.value -> {
                    val reference = sequenceChildren.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue
                    element.addSequenceElement(reference)
                    elementComplete.classSuffix = firstToUpper(reference)
                }
                XsdElementNames.GROUP.value -> elementComplete.addValue(sequenceChildren)
            }
        }
    }


    /**
     * This function will be called for each xsd:all or xsd:choice inside a xsd:element node
     * It will create the Choice Object add the values to it and add the object to the choices list
     * Example:
     * <xsd:element name="picture">
     *         <xsd:complexType>
     *             <xsd:all> ... </xsd:all>
     *             <xsd:choice> ... </xsd:choice>
     *         </xsd:complexType>
     *     </xsd:element>
     *
     * For the xsd:element "picture", this function will be called twice,
     * once for the xsd:all and once for the xsd:choice nodes
     *
     * @param current Is the xsd:all or xsd:choice node being parsed
     * */
    private fun createChoice(current: Node, name : String) {
        createChoiceOrAll(current,name, ClassNames.CHOICE.value)
    }

    private fun createAll(current: Node, name : String) {
        createChoiceOrAll(current,name, ClassNames.ALL.value)
    }

    private fun createChoiceOrAll(current: Node, name : String, type: String) {
        val choice = Choice(name, type)
        current.childNodes.forEachXsdElement { childNode ->
            choice.addValue(childNode)
        }
        choiceList.add(choice)
    }

    /**
     * This function will be called for each xsd:group base element
     * Example:
     * <xsd:group name="interactiveContent">
     *      <xsd:all>
     *          <xsd:element ref="a"/>
     *          <xsd:element ref="audio"/>
     *                      ...
     *      </xsd:all>
     *      OR
     *      <xsd:choice>
     *          <xsd:group ref="transparentContentWithoutA"/>
     *          <xsd:element ref="a"/>
     *                      ...
     *      </xsd:choice>
     * </xsd:group>
     *
     * For each xsd:group this function will get the first xsd:all or xsd:choice
     * each xsd:group will only have 1 child, xsd:all or xsd:choice.
     *
     * The rootName(the name of the xsd:group) is important to create the name of the group
     *
     * @param current Is the xsd:group node being parsed
     * */
    private fun createGroup(current: Node) {
        current.childNodes.getFirstNodeWithType(choiceAndAllSet)?.let { root ->
            val group = Group(
                current.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue,
                root.nodeName
            )
            root.childNodes.forEachXsdElement {
                group.addValue(it)
            }
            groupList.add(group)
        }
    }


    /**
     * This function will be called for each xsd:attributeGroup base element
     * Example:
     * <xsd:attributeGroup name="globalAttributes">
     *      <xsd:attributeGroup ref="globalEventAttributes"/>
     *      <xsd:attribute name="accesskey" type="xsd:string" />
     *                              ...
     * </xsd:attributeGroup>
     *
     * @param current Is the xsd:attributeGroup node being parsed
     * */
    private fun createAttributeGroup(current: Node) {
        val attrGroup = AttrGroup(current.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue)
        current.childNodes.forEachXsdElement {
            attrGroup.addValue(it)
        }
        attrGroupList.add(attrGroup)
    }


    /**
     * This function will be called for each xsd:simpleType base element
     * Example:
     * <xsd:simpleType name="TypeSimpleContentType">
     *      <xsd:restriction base="xsd:string">
     *          <xsd:enumeration value="text/asp" />
     *          <xsd:enumeration value="text/asa" />
     *      </xsd:restriction>
     * </xsd:simpleType>
     *
     * It will get the first child element of type xsd:restriction
     *
     * @param current Is the xsd:simpleType node being parsed
     * */
    private fun createSimpleType(current : Node) {
        current.childNodes.getFirstNodeWithType(restrictionNodeSet)?.let { restriction ->
            val simpleType = SimpleType(
                current.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue,
                restriction.attributes.getNamedItem(AttributeKeyName.BASE.value).nodeValue.substring(xsdStrLength),
            )
            restriction.childNodes.forEachXsdElement {
                simpleType.addValue(it)
            }
            simpleTypeList.add(simpleType)
        }
    }


    /**
     * This function will be called for each xsd:complexType base element
     * Example:
     * <xsd:complexType name="simpleMainLessFlowContentElement" mixed="true">
     *      <xsd:group ref="mainLessFlowContent" />
     *                      ...
     * </xsd:complexType>
     *
     * It will get the first child element of type xsd:group
     *
     * @param current Is the xsd:complexType node being parsed
     * */
    private fun createComplexType(current: Node) {
        current.childNodes.getFirstNodeWithType(groupSet)?.let { group ->
            val choice = Choice(current.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue, ClassNames.CHOICE.value)
            choice.addValue(group)
            choiceList.add(choice)
        }
    }

    /**
     * Hashsets used in the getFirstNodeWithType function
     * */
    companion object {
        private val restrictionNodeSet = hashSetOf(XsdElementNames.RESTRICTION.value)

        private val complexTypeSet = hashSetOf(XsdElementNames.COMPLEX_TYPE.value)

        private val groupSet = hashSetOf(XsdElementNames.GROUP.value)

        private val choiceAndAllSet = hashSetOf(XsdElementNames.ALL.value, XsdElementNames.CHOICE.value)

        private val xsdStrLength = XsdElementNames.XSD.value.length
    }
}