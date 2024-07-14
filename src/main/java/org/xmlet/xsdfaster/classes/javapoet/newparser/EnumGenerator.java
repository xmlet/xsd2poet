package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.SimpleType;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;

public class EnumGenerator {

    static public TypeSpec.Builder generateSimpleTypeMethods(SimpleType simpleType) {

        String className = simpleType.getUpperCaseName();

        TypeSpec.Builder builder = TypeSpec
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
                        .get(ClassName.get(CLASS_PACKAGE, "EnumInterface"), TypeName.get(String.class))
        );

        simpleType.getList().forEach(enumValue -> {
            builder
                    .addEnumConstant(enumValue.component1(),
                            TypeSpec
                                    .anonymousClassBuilder("String.valueOf(\"" + enumValue.component2() + "\")")
                                    .build()
                    );
        });

        return builder;
    }


    static public TypeSpec.Builder generateEnumInterface() {
        return TypeSpec
                .interfaceBuilder("EnumInterface")
                .addTypeVariable(TypeVariableName.get("T"))
                .addMethod(
                        MethodSpec
                                .methodBuilder("getValue")
                                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                                .returns(TypeVariableName.get("T"))
                                .build()
                );
    }
}
