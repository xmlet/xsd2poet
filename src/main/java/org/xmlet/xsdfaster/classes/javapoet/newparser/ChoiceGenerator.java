package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.Choice;
import org.xmlet.parser.Group;

import javax.lang.model.element.Modifier;

import java.util.List;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

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
