package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*

/**
 * Each instance of this class represents a class in the final library, this class is a HTML element
 * */
class ElementXsd(name: String) : BaseObjectWithAttrs(name){

    //each element of this list will represent a method in the Element class regarding the xsd:sequence
    private val sequenceElements: LinkedList<String> = LinkedList()

    //this flag has the information if the default super class is present or not for this element
    private var isDefaultPresent = true

    //this flag decides if you should create the Complete class (corner case)
    private var hasSequence = false

    // adding the default super class to the references of this element in the instanciation proccess
    init {
        references.add(DefaultSuperClassNames.TEXT_GROUP.value)
    }

    fun hasSequence() = hasSequence

    fun getSequenceElements() : List<String> = sequenceElements

    //when this method is called the objective is to remove the default super class from the references
    private fun removeTextGroup() {
        if (isDefaultPresent) {
            references.remove(DefaultSuperClassNames.TEXT_GROUP.value)
            isDefaultPresent = false
        }
    }

    fun addSequenceElement(sequenceElement : String) {
        sequenceElements.add(sequenceElement)
    }

    override val getFinalClassName: String = getUpperCaseName()

    override fun addValue(node: Node) {
        when (node.nodeName) {
            /**
             * Example:
             * <xsd:element name="html">
             *      <xsd:complexType>
             *          <xsd:attribute name="manifest" type="xsd:anyURI" />         <----- node
             *      </xsd:complexType>
             * </xsd:element>
             *
             * -> node (xsd:attribute)
             *      Means the final Element class will have a method specific for this xsd:attribute.
             *      The method will be called "attr<name>" where <name> is the attribute "name" in the xsd:attribute
             *      The method will also have one parameter of the type in the attribute "type" in the xsd:attribute (In this specific case the xsd:anyURI maps to String.class)
             *      This Method returns the type of the current Element class
             *      Ex: public final Html<Z> attrManifest(String attrManifest) {
             * */
            XsdElementNames.ATTRIBUTE.value -> {
                attrValues.add(
                    Pair(
                        firstToUpper(node.attributes.getNamedItem(AttributeKeyName.NAME.value).nodeValue),
                        node.attributes.getNamedItem(AttributeKeyName.TYPE.value).nodeValue,
                    )
                )
            }
            /**
             * Example:
             * <xsd:element name="html">
             *      <xsd:complexType>
             *          <xsd:attributeGroup ref="globalAttributes" />               <----- node
             *      </xsd:complexType>
             * </xsd:element>
             *
             * -> node (xsd:attributeGroup)
             *      Means the final Element class will implement a class with the name in the attribute "ref",
             *      In this case it will implement GlobalAttributes
             *      Ex: class Html ... implements GlobalAttributes ... {
             * */
            XsdElementNames.ATTRIBUTE_GROUP.value -> {
                addReference(node.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue)
            }
            /**
             * Example:
             * <xsd:element name="html">
             *      <xsd:complexType>
             *          <xsd:choice>                                                <----- node
             *                  <choice children>
             *          </xsd:choice>
             *      </xsd:complexType>
             * </xsd:element>
             *
             * -> node (xsd:choice)
             *      Means the final Element class will implement a class called "<Element>Choice"
             *      This also means there will be an interface called "<Element>Choice" with the values in the
             *      <choice children> as methods. In this case we remove the default super class as there is already a new super class added
             *      Ex: class Html ... implements ... HtmlChoice {
             * */
            XsdElementNames.CHOICE.value -> {
                removeTextGroup()
                addReference(getUpperCaseName() + ClassNames.CHOICE.value)
            }
            /**
             *  <xsd:element name="ol">
             *         <xsd:complexType>
             *             <xsd:all>                                                <---- node
             *                 <xsd:element ref="li" />
             *             </xsd:all>
             *         </xsd:complexType>
             *     </xsd:element>
             *
             * -> node (xsd:all)
             *      Means the final Element class will implement a class called "<Element>All".
             *      This also means there will be an interface called "<Element>All"
             *      with the children xsd:elements representing methods in this interface.
             *      This also means the need to remove the default super class.
             *      Ex:  class Ol ... implements ... OlAll {
             * */
            XsdElementNames.ALL.value -> {
                removeTextGroup()
                addReference(getUpperCaseName() + ClassNames.ALL.value)
            }
            /**
             * <xsd:element name="head">
             *         <xsd:complexType>
             *             <xsd:group ref="metadataContent" />                      <---- node
             *         </xsd:complexType>
             *     </xsd:element>
             *
             * -> node (xsd:group)
             *      Means the final class will implement an Interface with the name in the "ref" attribute
             *      This also means the need to remove the default super class.
             *      Ex: class Head ... implements ... MetadataContent {
             * */
            XsdElementNames.GROUP.value -> {
                removeTextGroup()
                addReference(firstToUpper(node.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue))

            }
            /**
             * <xsd:element name="main" type="simpleFlowContentElement"/>
             *
             *      If the xsd:element doesn't have any children, the node itself will be received by this function.
             *      This will represent in the fina element class implementing a class in the "type" attribute and the
             *      GlobalAttributes class
             *      Ex: class Main ... implements SimpleFlowContentElement, GlobalAttributes {
             * */
            XsdElementNames.ELEMENT.value -> {
                removeTextGroup()
                addReference(node.attributes.getNamedItem(AttributeKeyName.TYPE.value).nodeValue + ClassNames.CHOICE.value)
                addReference(DefaultSuperClassNames.GLOBAL_ATTRIBUTES.value)
            }

            /**
             * <xsd:element name="details" >
             *      <xsd:complexType mixed="true">
             *          <xsd:sequence>
             *              <xsd:element ref="summary"/>
             *              <xsd:group ref="flowContent" />
             *          </xsd:sequence>
             *      </xsd:complexType>
             * </xsd:element>
             *
             * -> node (xsd:sequence)
             *      Means the final class will have a "<element><ref from xsd:element = "summary">" and "<element>Complete" class. (Example1)
             *      The xsd:element from the xsd:sequence will also mean a method in the Element class (Example2)
             *      This also means the need to remove the default super class.
             *      Ex1: class Head ... implements ... MetadataContent {
             *      Ex2: public DetailsSummary summary(String summary) {
             *
             * */
            XsdElementNames.SEQUENCE.value -> hasSequence = true
        }
    }
}