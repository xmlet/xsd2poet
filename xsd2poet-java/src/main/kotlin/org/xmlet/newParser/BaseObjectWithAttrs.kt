package org.xmlet.newParser

import java.util.*

/**
 * This class will be used by any xsd node with parameters (any values with key, value pairs that need to be saved)
 * @param name lower case name of this xsd node
 * */
abstract class BaseObjectWithAttrs(name: String) : BaseObject(name) {

    //list with all the key, value pairs, it's a list and not a map because it will be mostly used to be iterated on
    protected val attrValues : LinkedList<Pair<String, String>> = LinkedList()

    fun getAttrValuesList() : List<Pair<String,String>> = attrValues
}