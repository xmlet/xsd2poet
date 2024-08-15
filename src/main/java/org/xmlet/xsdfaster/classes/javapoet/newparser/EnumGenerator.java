package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.SimpleType;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class EnumGenerator {

    static public TypeSpec.Builder generateSimpleTypeMethods(SimpleType simpleType) {
        String className = simpleType.getUpperCaseName();

        TypeSpec.Builder builder;

        if (simpleType.getRestrictionList().isEmpty()) {

            builder = TypeSpec
                    .enumBuilder(className)
                    .addModifiers(Modifier.PUBLIC);

            builder.addMethod(
                    MethodSpec
                            .constructorBuilder()
                            .addParameter(String.class, "value")
                            .addStatement("this.value = value")
                            .build()
            );

            builder.addMethod(
                    MethodSpec
                            .methodBuilder("getValue")
                            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                            .returns(String.class)
                            .addStatement("return value")
                            .build()
            );

            builder.addField(
                    String.class,
                    "value",
                    Modifier.FINAL,
                    Modifier.PRIVATE
            );

            builder.addSuperinterface(
                    ParameterizedTypeName
                            .get(ClassName.get(ENUM_INTERFACE_PACKAGE, "EnumInterface"), TypeName.get(String.class))
            );

            simpleType.getList().forEach(enumValue -> {
                builder
                        .addEnumConstant(enumValue.component1(),
                                TypeSpec
                                        .anonymousClassBuilder("String.valueOf(\"" + enumValue.component2() + "\")")
                                        .build()
                        );
            });
        } else {
            builder = TypeSpec
                    .classBuilder("AttrSizes" + firstToUpper(simpleType.getBaseType()))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
                    .addMethod(MethodSpec
                            .methodBuilder("validateRestrictions")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .addParameter(String.class, "value")
                            .addStatement("$T.validatePattern( \"" + simpleType.getRestrictionList().get(0).replace("\\","\\\\") + "\", value)", ClassName.get(RESTRICTION_VALIDATOR_PACKAGE, "RestrictionValidator"))
                            .build());

        }

        return builder;
    }
}
