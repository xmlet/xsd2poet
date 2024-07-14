package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import kotlin.Pair;
import org.xmlet.parser.ElementXsd;

import javax.lang.model.element.Modifier;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class ElementGenerator {

    static public TypeSpec.Builder generateElementMethods(ElementXsd element) {
        String elementName = element.getNameLowerCase();
        String className = element.getUpperCaseName();

        ClassName elementVisitor = ClassName.get(CLASS_PACKAGE, "ElementVisitor");

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

        element.getAttrValuesList().forEach(pair -> addAttrFunction(builder, pair, className));

        return builder;
    }

    static private void addAttrFunction(TypeSpec.Builder builder, Pair<String, String> attrData, String className) {
        String name = attrData.component1().replace("-","");
        String attrName = "attr" + name;

        MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement));

        String type = attrData.component2();

        if (primitiveAndStringTypes.containsKey(type)) {
            method.addParameter(primitiveAndStringTypes.get(type), attrName);
        } else {
            method.addParameter(ClassName.get(CLASS_PACKAGE, "Enum" + firstToUpper(type)), attrName);
        }

        if (specialTypes.contains(type)) {
            method.addStatement("$T.validateRestrictions(" + attrName + ")",ClassName.get(CLASS_PACKAGE, "AttrSizesString") );
        }

        method
                .addStatement("this.visitor.visitAttribute" + name + "(" + attrName + ")")
                .addStatement("return this.self()");

        builder.addMethod(method.build());
    }
}
