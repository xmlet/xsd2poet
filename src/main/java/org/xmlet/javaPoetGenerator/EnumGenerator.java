package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;
import org.xmlet.newParser.SimpleType;
import javax.lang.model.element.Modifier;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.enumInterfaceClassName;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.restrictionValidatorClassName;

/**
 * This class is in charge of creating all the Enum classes in the final library based on each SimpleType received from the parser
 *
 * If the SimpleType has a restrictionList then we create a class to enforce the restriction, there is no need for the Enum
 * */
public class EnumGenerator {

    /**
     * There are two types of Classes being generated
     *
     * Either a enum if there are no restrictions
     * or a class to enforce the restriction
     *
     * @param simpleType current simpleType being used to generate a Enum class or the Restriction class
     * */
    static public TypeSpec.Builder generateSimpleTypeMethods(SimpleType simpleType) {
        return simpleType.getRestrictionList().isEmpty()
                ? buildEnumTypeNoRestriction(simpleType)
                : buildEnumWithRestriction(simpleType);
    }


    /**
     * Method to generate the Enum Class
     *
     * Creates a constructor tha receives a value (always a String)
     * Creates a field and it's getter
     * Adds the super interface, it's always the EnumInterface
     * For Each attribute Value add an enum constant with a name a String.valueOf("<value>")
     * */
    private static TypeSpec.Builder buildEnumTypeNoRestriction(SimpleType simpleType) {
        TypeSpec.Builder builder;
        builder = TypeSpec
                .enumBuilder(simpleType.getGetFinalClassName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec
                        .constructorBuilder()
                        .addParameter(String.class, "value")
                        .addStatement("this.value = value")
                        .build())
                .addMethod(MethodSpec
                        .methodBuilder("getValue")
                        .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return value")
                        .build())
                .addField(String.class, "value", Modifier.FINAL, Modifier.PRIVATE)
                .addSuperinterface(ParameterizedTypeName.get(enumInterfaceClassName, TypeName.get(String.class)));


        simpleType.getAttrValuesList().forEach(enumValue -> {
            builder.addEnumConstant(enumValue.component1(), TypeSpec
                    .anonymousClassBuilder("String.valueOf(\"" + enumValue.component2() + "\")").build());
        });

        return builder;
    }


    /**
     * Method to generate the Restriction Class
     *
     * Creates a class
     * adds an empty constructor
     * adds a method to validate the restriction
     * */
    @NotNull
    private static TypeSpec.Builder buildEnumWithRestriction(SimpleType simpleType) {
        TypeSpec.Builder builder;
        String patternStr = simpleType.getRestrictionList().get(0).replace("\\","\\\\");
        builder = TypeSpec
                .classBuilder(simpleType.getGetFinalClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
                .addMethod(MethodSpec
                        .methodBuilder("validateRestrictions")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addParameter(String.class, "value")
                        .addStatement(
                                "$T.validatePattern( \"" + patternStr + "\", value)",
                                restrictionValidatorClassName
                        )
                        .build());
        return builder;
    }
}
