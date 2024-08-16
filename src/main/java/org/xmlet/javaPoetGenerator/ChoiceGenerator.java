package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.xmlet.newParser.Choice;
import org.xmlet.newParser.Group;

import javax.lang.model.element.Modifier;

import java.util.List;

import static org.xmlet.javaPoetGenerator.ClassGenerator.*;
import static org.xmlet.javaPoetGenerator.Utils.firstToUpper;

public class ChoiceGenerator {
    static public TypeSpec.Builder generateChoiceMethods(Choice choice) {
        return generateChoiceMethods(choice.getName(), choice.getRefList(), choice.getChoiceList());
    }

    static public TypeSpec.Builder generateChoiceMethods(Group group) {
        return generateChoiceMethods(group.getName(), group.getRefList(), group.getChoiceList());
    }
    static public TypeSpec.Builder generateChoiceMethods(
            String elementName,
            List<String> refList,
            List<String> choiceList
    ) {
        String className = firstToUpper(elementName);

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder(elementName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(ELEMENT_T_Z)
                .addTypeVariable(zExtendsElement);

        refList.forEach(reference -> addOthersSuperInterface(builder, firstToUpper(reference), className));


        choiceList.forEach(currentChoice -> {
            String name = firstToUpper(currentChoice);
            builder.addMethod(
                    MethodSpec
                            .methodBuilder(currentChoice)
                            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                            .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, name), ELEMENT_T_Z))
                            .addStatement("return new " + name + "(this.self())")
                            .build()
            );
        });

        return builder;
    }
}
