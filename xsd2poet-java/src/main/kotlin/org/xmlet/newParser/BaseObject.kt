package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*


/**
 * Abstract call that All Parse Objects extend
 * @param lowerCaseName the name of the xsd node in lowerCase
 * */
abstract class BaseObject(private val lowerCaseName: String) {

    // list with all the references, all the Class Names this Element will Extend/Implement
    protected val references: LinkedList<String> = LinkedList()

    // upper case name of this xsd element
    private val upperCaseName: String = firstToUpper(lowerCaseName)

    abstract val getFinalClassName: String

    /**
     * This function will be used by each specific Element class on how to handle the current childNode being processed
     * @param node child node of the xsd node being parsed
     * */
    abstract fun addValue(node: Node)

    /**
     * This function has the objective to get the ClassName of the element
     * @return the ClassName of the element
     * */
    fun getFinalClassName() : String = getFinalClassName

    protected fun addReference(reference: String) { references.add(firstToUpper(reference))}

    fun getRefsList() : List<String> = references

    fun getUpperCaseName() = upperCaseName

    fun getLowerCaseName() = lowerCaseName
}