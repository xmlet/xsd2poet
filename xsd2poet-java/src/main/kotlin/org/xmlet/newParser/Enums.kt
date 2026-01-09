package org.xmlet.newParser


/**
 * Enum with values regaring the xsd element names in the .xsd file
 * */
enum class XsdElementNames(val value: String) {
    XSD("xsd:"),
    RESTRICTION("${XSD.value}restriction"),
    COMPLEX_TYPE("${XSD.value}complexType"),
    SIMPLE_TYPE("${XSD.value}simpleType"),
    GROUP("${XSD.value}group"),
    ALL("${XSD.value}all"),
    CHOICE("${XSD.value}choice"),
    ELEMENT("${XSD.value}element"),
    ATTRIBUTE_GROUP("${XSD.value}attributeGroup"),
    ATTRIBUTE("${XSD.value}attribute"),
    SEQUENCE("${XSD.value}sequence"),
    ENUMERATION("${XSD.value}enumeration"),
    PATTERN("${XSD.value}pattern"),
}

/**
 * Enum with values regaring the attributes needed in the xsd elements
 * Example:
 * <xsd:simpleType name="sandboxType">              <---- "name" attribute
 *      <xsd:restriction base="xsd:string">         <---- "base" attribute
 *      <xsd:enumeration value="allow-forms" />     <---- "value" attribute
 * <xsd:simpleType/>
 * */
enum class AttributeKeyName(val value: String) {
    NAME("name"),
    REF("ref"),
    BASE("base"),
    TYPE("type"),
    VALUE("value"),
}

/**
 * Enum used to create the class names that will be used when generating the classes
 * */
enum class ClassNames(val value: String) {
    SUMMARY("Summary"),
    CHOICE("Choice"),
    ALL("All"),
    ENUM("Enum"),
}

/**
 * Enum used for the Default class names of the super classes of the Elements.
 * Default meaning, if there are no references this classes will be the ones used as super classes
 * */
enum class DefaultSuperClassNames(val value: String) {
    CUSTOM_ATTRIBUTE_GROUP("CustomAttributeGroup"),
    TEXT_GROUP("TextGroup"),
    GLOBAL_ATTRIBUTES("GlobalAttributes"),
}