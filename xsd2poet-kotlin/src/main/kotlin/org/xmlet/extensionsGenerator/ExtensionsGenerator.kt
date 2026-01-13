package org.xmlet.extensionsGenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import org.xmlet.kotlinPoetGenerator.GeneratorConstants.ELEMENT_PACKAGE
import org.xmlet.newParser.ElementXsd

class ExtensionsGenerator {
    companion object {
        private val t = TypeVariableName("T")

        private val z = TypeVariableName("Z")

        private val kotlinUnit = ClassName("kotlin", "Unit")

        val any = TypeVariableName("*")

        private val zExtendsElementAnyAny =
            TypeVariableName(
                "Z",
                ClassName(ELEMENT_PACKAGE, "Element")
                    .parameterizedBy(any, any),
            )

        private val tExtendsElementTZ =
            TypeVariableName(
                "T",
                ClassName(ELEMENT_PACKAGE, "Element")
                    .parameterizedBy(
                        t,
                        z,
                    ),
            )

        private val listOfTypeVariables = listOf(tExtendsElementTZ, zExtendsElementAnyAny)

        fun addProperty(
            file: FileSpec.Builder,
            element: ElementXsd,
        ) {
            val className = element.getFinalClassName()

            file.addProperty(
                PropertySpec
                    .builder(
                        element.getLowerCaseName(),
                        ClassName(ELEMENT_PACKAGE, className).parameterizedBy(t),
                    ).receiver(t)
                    .addTypeVariables(listOfTypeVariables)
                    .getter(
                        FunSpec
                            .getterBuilder()
                            .addModifiers(KModifier.INLINE)
                            .addStatement("return $className(this.self())")
                            .build(),
                    ).build(),
            )
        }

        /**
         * For example, given a FlowContent instance e.g. fc, we can do:
         *    val sameFc = fc.h3 { it: H3 -> ... } // returns the same FlowContent instance
         * The extension function h3 should be like:
         *    inline fun <T : Element<T, Z>, Z : Element<*, *>> FlowContent<T, Z>.h3(crossinline
         *     block: H3<FlowContent<T, Z>>.() -> Unit): FlowContent<T, Z> {
         * Type parameters T and Z will capture the implementing class of FlowContent (i.e. T) and its parent (i.e. Z).
         */
        fun addFun(
            file: FileSpec.Builder,
            receiverName: String, // e.g. "FlowContent"
            functionName: String, // e.g. "h3" inside given FlowContent
        ) {
            val receiverClass = ClassName(ELEMENT_PACKAGE, receiverName).parameterizedBy(t, z) // i.e. FlowContent<T, Z>
            val childName = functionName.replaceFirstChar { it.uppercase() } // i.e. H3 inside FlowContent
            val childClass =
                ClassName(ELEMENT_PACKAGE, childName).parameterizedBy( // i.e. H3<FlowContent<T, Z>>
                    receiverClass,
                )

            file.addFunction(
                FunSpec
                    .builder(functionName) // i.e. h3()
                    .addModifiers(KModifier.INLINE)
                    .returns(receiverClass) // i.e. FlowContent<T, Z>
                    .receiver(receiverClass) // i.e. FlowContent<T, Z>
                    .addTypeVariables(listOfTypeVariables) // i.e. <T : Element<T, Z>, Z : Element<*, *>>
                    .addParameter(
                        "block",
                        LambdaTypeName.get(
                            receiver = childClass, // i.e. H3<FlowContent<T, Z>>
                            returnType = kotlinUnit,
                        ),
                        KModifier.CROSSINLINE,
                    ).addStatement("val elem =  $childName(this)")
                    .addStatement("elem.block()")
                    .addStatement("return elem.`__`()")
                    .build(),
            )
        }
    }
}
