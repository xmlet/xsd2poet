package org.xmlet.kotlinPoetGenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.xmlet.javaPoetGenerator.GeneratorConstants.*
import java.io.File

class KClassGenerator {

    companion object{
        fun createKotlinInfrastructureClasses() {
            createKClass(generateElementExtensions(), "ElementExtensions")
            createKClass(generateSuspendConsumer(), "SuspendConsumer")
        }

        private fun generateElementExtensions(): TypeSpec.Builder {
            val any = TypeVariableName("*")

            val t =
                TypeVariableName(
                    "T",
                    ClassName(ELEMENT_PACKAGE, "Element")
                        .parameterizedBy(
                            any,
                            any
                        )
                )

            val builder =
                TypeSpec
                    .interfaceBuilder("ElementExtensions")
                    .addTypeVariable(t)

            builder.addFunction(
                FunSpec
                    .builder("unaryPlus")
                    .addModifiers(KModifier.OPERATOR)
                    .receiver(String::class)
                    .returns(t)
                    .addStatement("return addTextFromkotlin(this)")
                    .build()
            )

            builder.addFunction(
                FunSpec
                    .builder("addTextFromkotlin")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter("txt", String::class)
                    .returns(t)
                    .build()
            )

            return builder
        }

        private fun generateSuspendConsumer(): TypeSpec.Builder {
            val e = TypeVariableName("E")

            val m = TypeVariableName("M")

            val builder =
                TypeSpec
                    .interfaceBuilder("SuspendConsumer")
                    .addTypeVariable(e)
                    .addTypeVariable(m)

            builder.addFunction(
                FunSpec
                    .builder("accept")
                    .receiver(e)
                    .addParameter("model", m)
                    .addModifiers(KModifier.SUSPEND, KModifier.ABSTRACT)
                    .build()
            )

            return builder
        }

        private fun createKClass(builder: TypeSpec.Builder,className: String) {
            val kotlinFile =
                FileSpec
                    .builder(ELEMENT_PACKAGE, className)
                    .addType(builder.build())
                    .build()
            kotlinFile.writeTo(File(KOTLIN_ROOT_PATH))
        }
    }
}