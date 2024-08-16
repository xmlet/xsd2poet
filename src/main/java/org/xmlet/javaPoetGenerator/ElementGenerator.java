package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import kotlin.Pair;
import org.xmlet.newParser.ElementComplete;
import org.xmlet.newParser.ElementXsd;

import javax.lang.model.element.Modifier;

import java.util.HashSet;

import static org.xmlet.javaPoetGenerator.ClassGenerator.*;
import static org.xmlet.javaPoetGenerator.GeneralGenerator.generateSequenceMethod;
import static org.xmlet.javaPoetGenerator.Utils.firstToLower;
import static org.xmlet.javaPoetGenerator.Utils.firstToUpper;

public class ElementGenerator {

    private static HashSet<String> createdFunctions = new HashSet<>();

    static public TypeSpec.Builder generateElementMethods(ElementXsd element, TypeSpec.Builder elementVisitorBuilder) {

        String elementName = element.getNameLowerCase();
        String className = element.getUpperCaseName();

        ClassName elementVisitor = ClassName.get(ELEMENT_PACKAGE, "ElementVisitor");

        TypeSpec.Builder builder = generateCommonElementStructure(elementVisitorBuilder, elementVisitor, className, elementName);

        element.getReferencesList().forEach(reference -> addElementSuperInterface(builder, reference, className));

        element.getAttrValuesList().forEach(pair -> addAttrFunction(builder, pair, className, elementVisitorBuilder));

        if (element.hasSequence()) {
            element.getSequenceElements().forEach(sequenceElement -> {
                generateSequenceMethod(builder, className + firstToUpper(sequenceElement), sequenceElement);

                TypeSpec.Builder sequenceClass = generateCommonElementStructure(elementVisitorBuilder, elementVisitor, className + "Complete" , elementName, ElementType.SIMPLE);

                addElementSuperInterface(sequenceClass, "CustomAttributeGroup", className + "Complete" );

                createClass(sequenceClass);
            });
        }
        return builder;
    }

    static public TypeSpec.Builder generateElementCompleteMethods(
            ElementComplete element,
            TypeSpec.Builder elementVisitorBuilder
    ) {

        ClassName elementVisitor = ClassName.get(ELEMENT_PACKAGE, "ElementVisitor");
        TypeSpec.Builder builder = generateCommonElementStructure(elementVisitorBuilder, elementVisitor, element.getName(), element.getParentName(), ElementType.SIMPLE);

        addElementSuperInterface(builder, "CustomAttributeGroup", element.getName());

        element.getAttrs().forEach(attr -> {
            generateSequenceMethod(builder, element.getName(), attr);
        });

        return builder;
    }

    private static TypeSpec.Builder generateCommonElementStructure(
            TypeSpec.Builder elementVisitorBuilder,
            ClassName elementVisitor,
            String className,
            String elementName,
            ElementType elementType) {

        if (elementType == ElementType.COMPLEX)
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

        TypeSpec.Builder builder = TypeSpec
                .classBuilder( className)
                .addTypeVariable(zExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(elementVisitor, "visitor", Modifier.PROTECTED, Modifier.FINAL)
                .addField(zExtendsElement, "parent", Modifier.PROTECTED, Modifier.FINAL);


        MethodSpec.Builder firstConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(elementVisitor, "visitor")
                .addStatement("this.visitor = visitor")
                .addStatement("this.parent = null");

        if (elementType == ElementType.COMPLEX)
            firstConstructor.addStatement("visitor.visitElement" + className + "(this)");

        MethodSpec.Builder secondConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(zExtendsElement, "parent")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = parent.getVisitor()");

        if (elementType == ElementType.COMPLEX)
            secondConstructor.addStatement("this.visitor.visitElement" + className + "(this)");

        MethodSpec.Builder thirdConstrutor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(zExtendsElement, "parent")
                .addParameter(elementVisitor, "visitor")
                .addParameter(boolean.class, "shouldVisit")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = visitor");

        if (elementType == ElementType.COMPLEX)
            thirdConstrutor
                    .beginControlFlow("if (shouldVisit)")
                    .addStatement("visitor.visitElement" + className + "(this)")
                    .endControlFlow();


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

        builder.addMethod(firstConstructor.build())
                .addMethod(secondConstructor.build())
                .addMethod(thirdConstrutor.build())
                .addMethod(terminator)
                .addMethod(getParent)
                .addMethod(getVisitor)
                .addMethod(getName)
                .addMethod(self);

        return builder;
    }

    static private TypeSpec.Builder generateCommonElementStructure(
            TypeSpec.Builder generateCommonElementStructure,
            ClassName elementVisitor,
            String className,
            String elementName
            ) {
        return generateCommonElementStructure(generateCommonElementStructure, elementVisitor,className,elementName, ElementType.COMPLEX);
    }

    static private void addAttrFunction(
            TypeSpec.Builder builder,
            Pair<String, String> attrData,
            String className,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String[] strs = attrData.component1().split("-");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strs.length; i++) {
            sb.append(firstToUpper(strs[i]));
        }

        String name = sb.toString();

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
            String lowerName = firstToLower(name);
            String varName = invalidStrings.contains(name.toLowerCase()) ? "var1" : lowerName;
            MethodSpec.Builder attrMethod = MethodSpec
                    .methodBuilder(visitAttrFunctionName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, varName);

            String visitString;

            if (type.contains("boolean")){
                visitString = "visitAttributeBoolean";
            } else {
                visitString = "visitAttribute";
            }
            attrMethod.addStatement("this." + visitString + "(\"" + firstToLower(attrData.component1()) + "\", " + varName +")");

            createdFunctions.add(visitAttrFunctionName);

            elementVisitorBuilder.addMethod(attrMethod.build());
        }
    }

    enum ElementType {
        SIMPLE,
        COMPLEX
    }
}
