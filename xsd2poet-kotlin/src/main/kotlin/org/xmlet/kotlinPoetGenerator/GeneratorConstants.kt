package org.xmlet.kotlinPoetGenerator

object GeneratorConstants {
    val primitiveAndStringTypes: MutableMap<String?, Class<*>?>

    init {
        primitiveAndStringTypes = HashMap<String?, Class<*>?>()
        primitiveAndStringTypes.put("xsd:boolean", Boolean::class.java)
        primitiveAndStringTypes.put("xsd:byte", Byte::class.java)
        primitiveAndStringTypes.put("xsd:char", Char::class.java)
        primitiveAndStringTypes.put("xsd:short", Short::class.java)
        primitiveAndStringTypes.put("xsd:int", Int::class.java)
        primitiveAndStringTypes.put("xsd:positiveInteger", Int::class.java)
        primitiveAndStringTypes.put("xsd:long", Long::class.java)
        primitiveAndStringTypes.put("xsd:float", Float::class.java)
        primitiveAndStringTypes.put("xsd:double", Double::class.java)
        primitiveAndStringTypes.put("xsd:string", String::class.java)
        primitiveAndStringTypes.put("xsd:anyURI", String::class.java)
        primitiveAndStringTypes.put("sizesType", String::class.java)
    }

    const val KOTLIN_ROOT_PATH: String = "./target/generated-sources"

    const val CLASS_PACKAGE: String = "org.xmlet.htmlapifaster"

    const val ELEMENT_PACKAGE: String = "org.xmlet.htmlapifaster"
}
