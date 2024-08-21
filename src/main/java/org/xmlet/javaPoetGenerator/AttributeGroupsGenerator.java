package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.xmlet.newParser.AttrGroup;
import javax.lang.model.element.Modifier;
import static org.xmlet.javaPoetGenerator.ClassGenerator.*;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.utils.Utils.firstToLower;
import static org.xmlet.utils.Utils.firstToUpper;

public class AttributeGroupsGenerator {

    static public TypeSpec.Builder generateAttributeGroupsMethods(
            AttrGroup attrGroup,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String className = attrGroup.getFinalClassName();

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(ELEMENT_T_Z)
                .addTypeVariable(zExtendsElement);

        attrGroup.getRefsList().forEach(reference -> addOthersSuperInterface(builder, reference));

        attrGroup.getAttrValuesList().forEach(pair -> {
            String[] strs = pair.component1().split("-");
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < strs.length; i++) {
                sb.append(firstToUpper(strs[i]));
            }

            String name = sb.toString();
            String attrName = "attr" + name;

            MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .returns(t);

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


            String lowerName = firstToLower(name);
            String varName = invalidStrings.contains(name.toLowerCase()) ? "var1" : lowerName;
            MethodSpec.Builder attrMethod = MethodSpec
                    .methodBuilder("visitAttribute" + name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, varName);

            String visitString;

            if (type.contains("boolean")){
                visitString = "visitAttributeBoolean";
            } else {
                visitString = "visitAttribute";
            }
            attrMethod.addStatement("this." + visitString + "(\"" + firstToLower(pair.component1()) + "\", " + varName +")");

            elementVisitorBuilder.addMethod(attrMethod.build());
        });

        return builder;
    }
}
