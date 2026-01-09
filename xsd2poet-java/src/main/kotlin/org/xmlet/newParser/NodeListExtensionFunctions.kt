package org.xmlet.newParser

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.util.HashSet

/**
 * Iterates over all the xsd nodes in the code and runs the block of code for each xsd node
 * @param block anonymous function that receives a Node as a parameter and doesn't return anything.
 */
fun NodeList.forEachXsdElement(block: (Node) -> Unit) {
    for (i in 0 until length) {
        val node = this.item(i)
        if (node.nodeName.contains(XsdElementNames.XSD.value))
            block(node)
    }
}

/**
 * Iterates over the NodeList elements until it finds the first element that is in the typeSet
 *
 * This is used because every element node starts with a xsd:complexType as a Child
 * but sometimes the Parser library will have some seemeangly invalid nodes in between, this function wil filter those out
 *
 * @param typeSet Set with xsd node names
 * @return returns the first Node it finds or null if there isn't a xsd node in the NodeList
 */
fun NodeList.getFirstNodeWithType(typeSet: HashSet<String>) : Node? {
    for (i in 0 until length) {
        val node = this.item(i)
        if (typeSet.contains(node.nodeName))
            return node
    }
    return null
}