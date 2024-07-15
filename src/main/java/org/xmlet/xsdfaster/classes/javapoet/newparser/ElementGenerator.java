package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import kotlin.Pair;
import org.xmlet.parser.ElementXsd;

import javax.lang.model.element.Modifier;

import java.util.HashSet;
import java.util.Objects;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class ElementGenerator {

    private static HashSet<String> createdFunctions = new HashSet<>();

    static public TypeSpec.Builder generateElementMethods(ElementXsd element, TypeSpec.Builder elementVisitorBuilder) {

        String elementName = element.getNameLowerCase();
        String className = element.getUpperCaseName();

        elementVisitorBuilder.addMethod(
                MethodSpec
                        .methodBuilder("visitElement" + className)
                        .addModifiers(Modifier.PUBLIC)
                        .addTypeVariable(zExtendsElement)
                        .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className),zExtendsElement),elementName)
                        .addStatement("this.visitElement(" + elementName + ")")
                        .build()
        );

        elementVisitorBuilder.addMethod(
                MethodSpec
                        .methodBuilder("visitParent" + className)
                        .addModifiers(Modifier.PUBLIC)
                        .addTypeVariable(zExtendsElement)
                        .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className),zExtendsElement),elementName)
                        .addStatement("this.visitParent(" + elementName + ")")
                        .build()
        );

        ClassName elementVisitor = ClassName.get(ELEMENT_PACKAGE, "ElementVisitor");

        MethodSpec firstConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(elementVisitor, "visitor")
                .addStatement("this.visitor = visitor")
                .addStatement("this.parent = null")
                .addStatement("visitor.visitElement" + className + "(this)")
                .build();

        MethodSpec secondConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(zExtendsElement, "parent")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = parent.getVisitor()")
                .addStatement("this.visitor.visitElement" + className + "(this)")
                .build();

        MethodSpec thirdConstrutor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(zExtendsElement, "parent")
                .addParameter(elementVisitor, "visitor")
                .addParameter(boolean.class, "shouldVisit")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = visitor")
                .beginControlFlow("if (shouldVisit)")
                .addStatement("visitor.visitElement" + className + "(this)")
                .endControlFlow()
                .build();

        MethodSpec terminator = MethodSpec.methodBuilder("__")
                .addModifiers(Modifier.PUBLIC)
                .returns(zExtendsElement)
                .addStatement("this.visitor.visitParent" + className + "(this)")
                .addStatement("return this.parent")
                .build();

        MethodSpec getParent = MethodSpec.methodBuilder("getParent")
                .addModifiers(Modifier.PUBLIC)
                .returns(zExtendsElement)
                .addStatement("return this.parent")
                .build();

        MethodSpec getVisitor = MethodSpec.methodBuilder("getVisitor")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(elementVisitor)
                .addStatement("return this.visitor")
                .build();

        MethodSpec getName = MethodSpec.methodBuilder("getName")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return \"" + elementName + "\"")
                .build();

        MethodSpec self = MethodSpec.methodBuilder("self")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement))
                .addStatement("return this")
                .build();
        TypeSpec.Builder builder = TypeSpec
                .classBuilder(className)
                .addTypeVariable(zExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(elementVisitor, "visitor", Modifier.PROTECTED, Modifier.FINAL)
                .addField(zExtendsElement, "parent", Modifier.PROTECTED, Modifier.FINAL)
                .addMethod(firstConstructor)
                .addMethod(secondConstructor)
                .addMethod(thirdConstrutor)
                .addMethod(terminator)
                .addMethod(getParent)
                .addMethod(getVisitor)
                .addMethod(getName)
                .addMethod(self);

        element.getReferencesList().forEach(reference -> addElementSuperInterface(builder, reference, className));

        element.getAttrValuesList().forEach(pair -> addAttrFunction(builder, pair, className, elementVisitorBuilder));

        return builder;
    }

    static private void addAttrFunction(
            TypeSpec.Builder builder,
            Pair<String, String> attrData,
            String className,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String name = attrData.component1().replace("-","");
        String attrName = "attr" + name;
        String visitAttrFunctionName = "visitAttribute" + name;

        MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement));

        String type = attrData.component2();

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

        if (specialTypes.contains(type)) {
            method.addStatement("$T.validateRestrictions(" + attrName + ")",ClassName.get(CLASS_PACKAGE, "AttrSizesString") );
        }

        method
                .addStatement("this.visitor." + visitAttrFunctionName + "(" + attrName + getValueFunction + ")")
                .addStatement("return this.self()");

        builder.addMethod(method.build());

        //to avoid building the same function several times in ElementVisitor
        if (!createdFunctions.contains(visitAttrFunctionName)) {
            MethodSpec.Builder attrMethod = MethodSpec
                    .methodBuilder(visitAttrFunctionName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, attrName);

            String visitString;

            if (type.contains("boolean")){
                visitString = "visitAttributeBoolean";
            } else {
                visitString = "visitAttribute";
            }
            attrMethod.addStatement("this." + visitString + "(\"" + attrName+ "\", " + attrName +")");

            createdFunctions.add(visitAttrFunctionName);

            elementVisitorBuilder.addMethod(attrMethod.build());
        }
    }
}
