package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.xmlet.newParser.AttrGroup;
import javax.lang.model.element.Modifier;

import static org.xmlet.javaPoetGenerator.ClassGenerator.*;
import static org.xmlet.javaPoetGenerator.GeneralGenerator.generateAttrFunction;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.utils.Utils.*;

public class AttributeGroupsGenerator {

    static public TypeSpec.Builder generateAttributeGroupsMethods(
            AttrGroup attrGroup,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String className = attrGroup.getFinalClassName();

        //builds the base interface
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(ELEMENT_T_Z)
                .addTypeVariable(zExtendsElement);

        //adds the superInterfaces to the interface
        attrGroup.getRefsList().forEach(reference -> addOthersSuperInterface(builder, reference));

        //adds the attribute function for each attribute to the interface
        attrGroup.getAttrValuesList().forEach(pair -> {
            String name = generateAttrName(pair);
            String attrName = getAttrName(name);
            String visitAttrFunctionName = getVisitAttrName(name);


            MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .returns(t);

            String type = pair.component2();

            String getValueFunction = getValueFunctionAndAddParameter(type, method, attrName);

            method
                    .addStatement("this.getVisitor()." + visitAttrFunctionName + "(" + attrName + getValueFunction + ")")
                    .addStatement("return this.self()");

            builder.addMethod(method.build());

            generateAttrFunction(pair, elementVisitorBuilder, name, visitAttrFunctionName, type);
        });

        return builder;
    }
}
