package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*

/**
 * This class will represent a interface which will be implemented by others with
 * default methods (defined by the list in the groupValues, each entyr will be a method)
 *
 * @type will be either All or Choice
 * */
class Group(name: String, type: String) : BaseChoiceGroup(name) {

    override val getFinalClassName: String = getUpperCaseName()

    //when the xsd node creating this group is a xsd:all it will ALWAYS implement TextGroup
    init {
        if (type == XsdElementNames.ALL.value)
            references.add(DefaultSuperClassNames.TEXT_GROUP.value)
    }

    /**
     * <xsd:group name="transparentContent">
     *      <xsd:choice>
     *          <xsd:group ref="transparentContentWithoutA"/>       <---- node1
     *          <xsd:element ref="a"/>                              <---- node2
     *      </xsd:choice>
     * </xsd:group>
     * Each xsd:group children (node1) represents a super class the created Interface will extend
     * Ex: interface TransparentContentChoice ... extends TransparentContentWithoutA {
     * Each xsd:element children (node2) represents a method in the created Interface
     * Ex: default A<T> a() { ... }
     *
     * */
    override fun addValue(node: Node) {
        val value = node.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue
        when(node.nodeName) {
            XsdElementNames.ELEMENT.value -> baseClassValues.add(value)
            XsdElementNames.GROUP.value -> addReference(value)
        }
    }

    fun getRefList() : List<String> = references
}