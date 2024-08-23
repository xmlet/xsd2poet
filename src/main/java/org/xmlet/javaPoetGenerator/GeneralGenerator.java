package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import kotlin.Pair;

import javax.lang.model.element.Modifier;

import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.utils.Utils.firstToLower;
import static org.xmlet.utils.Utils.firstToUpper;

public class GeneralGenerator {

    // Class to generate a sequence method
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

    /**
     * this function will create the attr function in the ElementVisitor
     * */
    static void generateAttrFunction(
            Pair<String, String> pair,
            TypeSpec.Builder elementVisitorBuilder,
            String name,
            String visitAttrFunctionName,
            String type
    ) {
        String lowerName = firstToLower(name);
        //safeguard to not use reserved words as variables names Ex: "class"
        String varName = invalidStrings.contains(name.toLowerCase()) ? "var1" : lowerName;
        MethodSpec.Builder attrMethod = MethodSpec
                .methodBuilder(visitAttrFunctionName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, varName);

        String visitString= type.contains("boolean") ? "visitAttributeBoolean" : "visitAttribute";

        attrMethod.addStatement("this." + visitString + "(\"" + firstToLower(pair.component1()) + "\", " + varName +")");

        elementVisitorBuilder.addMethod(attrMethod.build());
    }
}
