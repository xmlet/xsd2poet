package org.xmlet.newParser

import org.w3c.dom.Node
import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.collections.HashSet

/**
 * An ElementComplete will result in a class with the name "<Element><classSuffix>"
 *
 * This class will have a method for each value in the attrList
 * */
class ElementComplete(name : String) : BaseObject(name){

    // Each element will represent a method in the final class
    private val attrList: HashSet<String> = HashSet()

    //suffix used in the end of the class name
    var classSuffix = ""

    fun addAttrs(list: List<String>) = attrList.addAll(list)

    fun getAttrs(): Set<String> = attrList

    //classname will be "<Element><classSuffix>"
    override val getFinalClassName: String
        get() = getUpperCaseName() + classSuffix


    /**
     * add value will always be called on the xsd:group child of the ElementComplete,
     * meaning there is no need for any specific logic to address the add Value
     * */
    override fun addValue(node: Node) {
        addReference(node.attributes.getNamedItem(AttributeKeyName.REF.value).nodeValue)
    }
}