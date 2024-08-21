package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*

/**
 * Each of this elements will represent an Interface in the final library
 *
 * the interfaces will have default methods for each element
 *
 * the interfaces will either extend TextGroup or any other Class if xsd:group is present
 *
 * @param suffix will be added after the element name to generate the class name: "<ElementName><Suffix>"
 * */
class Choice(name: String, suffix: String) : BaseChoiceGroup(name){

    private var isDefaultPresent = true


    /**
     * If there is no xsd:group then the default super class is TextGroup
     * */
    init {
        references.add(DefaultSuperClassNames.TEXT_GROUP.value)
    }

    /**
     * Method to check if the default value is present, if it is remove it
     * */
    private fun removeTextGroup() {
        if (isDefaultPresent) {
            references.remove(DefaultSuperClassNames.TEXT_GROUP.value)
            isDefaultPresent = false
        }
    }

    override val getFinalClassName: String = getUpperCaseName() + suffix


    /**
     * This element might have xsd:element or xsd:group both can be present more than once.
     * The Choice will either appear as a base element (xsd:complexType):
     * Example 1:
     *      <xsd:group name="transparentContent">
     *         <xsd:choice>
     *             <xsd:group ref="transparentContentWithoutA"/>
     *                              ...
     *         </xsd:choice>
     *     </xsd:group>
     * OR as a chield of a xsd:element (xsd:choice):
     * Example 2:
     *      <xsd:element name="html">
     *         <xsd:complexType>
     *             <xsd:choice>
     *                 <xsd:element ref="body"/>
     *                 <xsd:element ref="head" minOccurs="0"/>
     *             </xsd:choice>
     *                      ...
     *         </xsd:complexType>
     *     </xsd:element>
     *
     * The xsd:group will represent the superClass (the default is textGroup)
     * from Example 1: public interface TransparentContentChoice ... extends TransparentContentWithoutA
     *
     * The xsd:element will represent a method in the interface
     * from Example 2: default Body<T> body() { ... }
     *
     * */
    override fun addValue(node: Node) {
        val value = node.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue
        when(node.nodeName) {
            XsdElementNames.ELEMENT.value -> baseClassValues.add(value)
            XsdElementNames.GROUP.value -> {
                removeTextGroup()
                addReference(value)
            }
        }
    }
}