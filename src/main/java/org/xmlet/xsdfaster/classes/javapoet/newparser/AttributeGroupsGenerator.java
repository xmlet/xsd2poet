package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.AttrGroup;
import org.xmlet.parser.ElementXsd;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class AttributeGroupsGenerator {

    static public TypeSpec.Builder generateAttributeGroupsMethods(
            AttrGroup attrGroup,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String className = firstToUpper(attrGroup.getName());

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(ELEMENT_T_Z)
                .addTypeVariable(zExtendsElement);

        attrGroup.getRefsList().forEach(reference -> addOthersSuperInterface(builder, firstToUpper(reference), className));

        attrGroup.getAttrValuesList().forEach(pair -> {
            String name = firstToUpper(pair.component1().replace("-",""));
            String attrName = "attr" + name;

            MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .returns(TypeVariableName.get("T"));

            String type = pair.component2();

            String getValueFunction;

            if (primitiveAndStringTypes.containsKey(type)) {
                method.addParameter(primitiveAndStringTypes.get(type), attrName);
                if (primitiveAndStringTypes.get(type) == String.class)
                    getValueFunction = "";
                else
                    getValueFunction = ".toString()";
            } else {
                method.addParameter(ClassName.get(CLASS_PACKAGE, "Enum" + firstToUpper(type)), attrName);
                getValueFunction = ".getValue()";
            }
            method.addStatement("this.getVisitor().visitAttribute" + name + "(" + attrName + getValueFunction + ")");
            method.addStatement("return this.self()");
            builder.addMethod(method.build());


            MethodSpec.Builder attrMethod = MethodSpec
                    .methodBuilder("visitAttribute" + name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, attrName);

            String visitString;

            if (type.contains("boolean")){
                visitString = "visitAttributeBoolean";
            } else {
                visitString = "visitAttribute";
            }
            attrMethod.addStatement("this." + visitString + "(\"" + attrName+ "\", " + attrName +")");

            elementVisitorBuilder.addMethod(attrMethod.build());
        });

        return builder;
    }
}
