package org.xmlet.newParser

import org.w3c.dom.Node
import org.xmlet.utils.Utils.firstToUpper
import java.util.*


/**
 * Each instance of this class will represent a Enum class with the type always being string
 * @param baseType the type of the enum values, should be a string with the value "string"
 * */
class SimpleType(name: String, private val baseType: String) : BaseObjectWithAttrs(name){

    //list with the xsd:pattern values, will have a separate class to validate the provided pattern
    private val patternValues : LinkedList<String> = LinkedList()

    // each xsd:simpleType will ALWAYS have the restriction value as being "string", this is just to protect the code
    init {
        if (baseType.lowercase() != "string")
            throw Exception("Enum with a base type that is not xsd:string is currently not supported")
    }

    override val getFinalClassName: String
        get() = className

    private var className = ClassNames.ENUM.value + getUpperCaseName()

    /**
     *
     * */
    override fun addValue(node: Node) {
        val value = node.attributes.getNamedItem(AttributeKeyName.VALUE.value).nodeValue
        when(node.nodeName) {
            XsdElementNames.ENUMERATION.value -> handleEnumeration(value)
            XsdElementNames.PATTERN.value -> {
                patternValues.add(value)
                className = "Attr" + getUpperCaseName().replace("Type", "") + firstToUpper(baseType)
            }
        }
    }

    /**
     * This function has the objective to create the name value pairs for each enum instance
     *
     * If the enumName is empty the string "EMPTY" should be the name
     *      Ex: <xsd:enumeration value="" will be EMPTY("")
     * If the enumName is a digit, add an '_' so it's a valid name
     *      Ex: <xsd:enumeration value="1"/> will be _1("1")
     * if the string is only 1 char long, don't modified it
     *      Ex: <xsd:enumeration value="a"/> will be a("a"),
     *          <xsd:enumeration value="A"/> will be A("A")
     * otherwise return the enumName with full uppercase
     *
     * The enumName, as it is a ENUM element can't have in the name '/','-' and '.'
     *
     * the value will be a string, there are no restrictions for it
     */
    private fun handleEnumeration(value: String) {
        val enumName =
            if (value.isEmpty())
                "EMPTY"
            else if (value.toIntOrNull() != null)
                "_${value}"
            else if (value.length == 1)
                value
            else
                value.uppercase()
        attrValues.add(Pair(
            enumName
                .replace("/", "_")
                .replace("-","_")
                .replace(".","_")
            ,value))
    }

    fun getRestrictionList(): List<String> = patternValues

}