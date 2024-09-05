package org.xmlet.extensionsGenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.xmlet.javaPoetGenerator.GeneratorConstants.*
import org.xmlet.newParser.ElementXsd
import java.io.File

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
                    .parameterizedBy(any, any)
            )

        private val tExtendsElementTZ = TypeVariableName(
            "T",
            ClassName(ELEMENT_PACKAGE, "Element")
                .parameterizedBy(
                    t,
                    z
                )
        )

        private val listOfTypeVariables = listOf(tExtendsElementTZ, zExtendsElementAnyAny);

        fun createXsd2PoetExtensions(block: (file:  FileSpec.Builder) -> Boolean) {
            val file = FileSpec.builder(EXTENSIONS_PACKAGE, "Xsd2PoetExtensions")
            //file.addImport(Element::class,"")

            block(file)

            file.build().writeTo(File(KOTLIN_ROOT_PATH))
        }

        fun addExtensions(file: FileSpec.Builder, element: ElementXsd) {
            addProperty(file, element)
            addFun(file, element)
        }

        private fun addProperty(
            file: FileSpec.Builder,
            element: ElementXsd
        ){
            val className = element.getFinalClassName()

            file.addProperty(
                PropertySpec.builder(element.getLowerCaseName(),ClassName(ELEMENT_PACKAGE, className).parameterizedBy(t))
                    .receiver(t)
                    .addTypeVariables(listOfTypeVariables)
                    .getter(
                        FunSpec.getterBuilder()
                            .addModifiers(KModifier.INLINE)
                            .addStatement("return $className(this.self())")
                            .build()
                    )
                    .build()
            )
        }

        private fun addFun(
            file: FileSpec.Builder,
            element: ElementXsd,
        ) {
            val className = element.getFinalClassName()
            file.addFunction(
                FunSpec.builder(element.getLowerCaseName())
                    .returns(t)
                    .receiver(t)
                    .addTypeVariables(listOfTypeVariables)
                    .addParameter("block", LambdaTypeName.get(
                        receiver = ClassName(ELEMENT_PACKAGE, className).parameterizedBy(t),
                        returnType = kotlinUnit,
                    ))
                    .addStatement("val elem =  $className(this)")
                    .addStatement("elem.block()")
                    .addStatement("return elem.`__`()")
                    .build()
            )
        }
    }
}