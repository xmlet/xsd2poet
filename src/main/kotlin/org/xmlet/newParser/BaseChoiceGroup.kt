package org.xmlet.newParser

import java.util.*

abstract class BaseChoiceGroup(name: String) : BaseObject(name) {
    //list with the values used to create each method representing a xsd:element
    protected val baseClassValues : LinkedList<String> = LinkedList()

    fun getBaseClassValuesList() : List<String> = baseClassValues
}