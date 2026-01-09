package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.kotlinpoet.FileSpec;
import org.xmlet.extensionsGenerator.ExtensionsGenerator;
import org.xmlet.newParser.BaseChoiceGroup;

import javax.lang.model.element.Modifier;
import java.util.List;

import static org.xmlet.javaPoetGenerator.ClassGenerator.addOthersSuperInterface;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.utils.Utils.firstToUpper;

/**
 * This class generates Choice Interfaces for the generated library
 */
public class ChoiceGenerator {
    static public TypeSpec.Builder generateChoiceMethods(BaseChoiceGroup baseChoiceGroup) {
        return generateChoiceMethods(
                baseChoiceGroup.getFinalClassName(),
                baseChoiceGroup.getRefsList(),
                baseChoiceGroup.getBaseClassValuesList()
        );
    }

    static public void generateChoiceMethodsForKotlin(BaseChoiceGroup baseChoiceGroup, FileSpec.Builder xsd2PoetExtensions) {
        generateChoiceMethodsForKotlin(
                baseChoiceGroup.getFinalClassName(),
                baseChoiceGroup.getBaseClassValuesList(),
                xsd2PoetExtensions
        );
    }

    /**
     * @param className  the class name of the interface being generated
     * @param refList    list of the super classes this interface will extend
     * @param choiceList list of all the methods that have to be added to the interface
     */
    static public TypeSpec.Builder generateChoiceMethods(
            String className,
            List<String> refList,
            List<String> choiceList) {

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(ELEMENT_T_Z)
                .addTypeVariable(zExtendsElement);

        refList.forEach(reference -> addOthersSuperInterface(builder, reference));

        choiceList.forEach(choiceLowerCaseName -> {
            String choiceUpperCaseName = firstToUpper(choiceLowerCaseName);

            builder.addMethod(
                    MethodSpec
                            .methodBuilder(choiceLowerCaseName)
                            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                            .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, choiceUpperCaseName), ELEMENT_T_Z))
                            .addStatement("return new " + choiceUpperCaseName + "(this.self())")
                            .build()
            );
        });

        return builder;
    }

    /**
     * @param className          the class name of the interface being generated
     * @param choiceList         list of all the methods that have to be added to the interface
     * @param xsd2PoetExtensions
     */
    static public void generateChoiceMethodsForKotlin(
            String className,
            List<String> choiceList,
            FileSpec.Builder xsd2PoetExtensions) {
        choiceList.forEach(choiceLowerCaseName -> {
            ExtensionsGenerator.Companion.addFun(xsd2PoetExtensions, className, choiceLowerCaseName);
        });
    }
}
