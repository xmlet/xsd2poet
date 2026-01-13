package org.xmlet.kotlinPoetGenerator

import com.squareup.kotlinpoet.FileSpec
import org.xmlet.extensionsGenerator.ExtensionsGenerator
import org.xmlet.newParser.BaseChoiceGroup
import java.util.function.Consumer

/**
 * This class generates Choice Interfaces for the generated library
 */
object ChoiceGenerator {
    fun generateChoiceMethodsForKotlin(
        baseChoiceGroup: BaseChoiceGroup,
        xsd2PoetExtensions: FileSpec.Builder,
    ) {
        ChoiceGenerator.generateChoiceMethodsForKotlin(
            baseChoiceGroup.getFinalClassName,
            baseChoiceGroup.getBaseClassValuesList(),
            xsd2PoetExtensions,
        )
    }

    /**
     * @param className          the class name of the interface being generated
     * @param choiceList         list of all the methods that have to be added to the interface
     * @param xsd2PoetExtensions
     */
    fun generateChoiceMethodsForKotlin(
        className: String,
        choiceList: List<String?>,
        xsd2PoetExtensions: FileSpec.Builder,
    ) {
        choiceList.forEach(
            Consumer { choiceLowerCaseName: String? ->
                ExtensionsGenerator.Companion.addFun(xsd2PoetExtensions, className, choiceLowerCaseName!!)
            },
        )
    }
}
