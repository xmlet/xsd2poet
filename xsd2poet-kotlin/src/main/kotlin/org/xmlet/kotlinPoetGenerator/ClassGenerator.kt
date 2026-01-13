package org.xmlet.kotlinPoetGenerator

import com.squareup.kotlinpoet.FileSpec
import org.xmlet.kotlinPoetGenerator.ElementGenerator.generateElementMethodsForKotlin
import org.xmlet.newParser.Choice
import org.xmlet.newParser.ElementXsd
import org.xmlet.newParser.Group
import org.xmlet.newParser.Parser
import java.io.File
import java.io.IOException
import java.util.function.Consumer

object ClassGenerator {
    @Throws(IOException::class)
    fun generateExtensionsForKotlin(parser: Parser) {
        val xsd2PoetExtensions = FileSpec.builder(GeneratorConstants.CLASS_PACKAGE, "Xsd2PoetExtensions")

        parser.getElementsList().forEach(
            Consumer { element: ElementXsd -> elementGeneratorForKotlin(element, xsd2PoetExtensions) },
        )

        parser.getChoiceList().forEach(
            Consumer { choice: Choice ->
                ClassGenerator.choiceGeneratorForKotlin(
                    choice,
                    xsd2PoetExtensions,
                )
            },
        )
        parser
            .getGroupList()
            .forEach(Consumer { group: Group? -> ClassGenerator.groupGeneratorForKotlin(group!!, xsd2PoetExtensions) })
        xsd2PoetExtensions.build().writeTo(File(GeneratorConstants.KOTLIN_ROOT_PATH))
    }

    private fun elementGeneratorForKotlin(
        element: ElementXsd,
        extensionsFile: FileSpec.Builder,
    ) {
        generateElementMethodsForKotlin(element, extensionsFile)
    }

    private fun choiceGeneratorForKotlin(
        choice: Choice,
        xsd2PoetExtensions: FileSpec.Builder,
    ) {
        ChoiceGenerator.generateChoiceMethodsForKotlin(choice, xsd2PoetExtensions)
    }

    private fun groupGeneratorForKotlin(
        group: Group,
        xsd2PoetExtensions: FileSpec.Builder,
    ) {
        ChoiceGenerator.generateChoiceMethodsForKotlin(group, xsd2PoetExtensions)
    }
}
