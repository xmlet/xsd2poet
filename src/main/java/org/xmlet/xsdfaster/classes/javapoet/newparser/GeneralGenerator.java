package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.CLASS_PACKAGE;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.zExtendsElement;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class GeneralGenerator {

    public static void generateSequenceMethod(TypeSpec.Builder builder, String className, String sequenceName) {
        String upperSequence = firstToUpper(sequenceName);

        builder.addMethod(
                MethodSpec
                        .methodBuilder(sequenceName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, sequenceName)
                        .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className),zExtendsElement))
                        .addStatement("((" + upperSequence + ")(new " + upperSequence + "(this, this.visitor, true)).text(" + sequenceName + ")).__()")
                        .addStatement("return new " + className + "(this.parent, this.visitor, true)")
                        .build()
        );

    }
}
