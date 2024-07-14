package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.AttrGroup;
import org.xmlet.parser.ElementXsd;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class AttributeGroupsGenerator {

    static public TypeSpec.Builder generateAttributeGroupsMethods(AttrGroup attrGroup) {
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
                    .returns(TypeVariableName.get("T"))
                    .addStatement("this.getVisitor().visitAttribute" + name + "(" + attrName + ")")
                    .addStatement("return this.self()");

            String type = pair.component2();

            if (primitiveAndStringTypes.containsKey(type)) {
                method.addParameter(primitiveAndStringTypes.get(type), attrName);
            } else {
                method.addParameter(ClassName.get(CLASS_PACKAGE, "Enum" + firstToUpper(type)), attrName);
            }
            builder.addMethod(method.build());
        });

        return builder;
    }
}
