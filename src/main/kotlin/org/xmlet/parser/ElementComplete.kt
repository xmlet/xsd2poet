package org.xmlet.parser

import org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper
import java.util.*
import kotlin.collections.HashSet

class ElementComplete(val name : String, val parentName: String) {
    private val dependencyList: LinkedList<String> = LinkedList()

    private val attrList: HashSet<String> = HashSet()

    fun addAttrs(list: List<String>) = attrList.addAll(list)

    fun getAttrs(): Set<String> = attrList

    fun getDependencyList(): List<String> = dependencyList


    fun addDependency(dependency : String) { dependencyList.add(dependency)}
}