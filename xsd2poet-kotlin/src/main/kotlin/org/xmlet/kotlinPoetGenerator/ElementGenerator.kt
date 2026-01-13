package org.xmlet.kotlinPoetGenerator

import com.squareup.kotlinpoet.FileSpec
import org.xmlet.extensionsGenerator.ExtensionsGenerator.Companion.addProperty
import org.xmlet.newParser.ElementXsd

/**
 * This class has the objective to create a java file for each HTML Element
 */
object ElementGenerator {
    private val classesWithNoExtensions = mutableSetOf<String?>("Text", "Html")

    fun generateElementMethodsForKotlin(
        element: ElementXsd,
        extensionsFile: FileSpec.Builder,
    ) {
        val className = element.getFinalClassName

        if (!classesWithNoExtensions.contains(className)) {
            addProperty(extensionsFile, element)
        }
    }
}
