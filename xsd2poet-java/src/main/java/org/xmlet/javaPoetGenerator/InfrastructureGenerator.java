package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;

// this class has the objective to build the Insfrastructure of HtmlFlow library
public class InfrastructureGenerator {

    //creates the Element Class
    public static TypeSpec.Builder createBaseElement() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("Element")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(ELEMENT_PACKAGE, "ElementBase"),
                        tExtendsElement,
                        zExtendsElement)
                );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("custom")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(String.class, "name")
                        .addStatement("return new $T(this.self(), name)", customElementClassName)
                        .returns(ParameterizedTypeName.get(customElementClassName,tExtendsElement))
                        .build()
        );

        return builder;
    }

    //creates the CustomElement Class
    public static TypeSpec.Builder createCustomElement() {
        TypeSpec.Builder builder = TypeSpec
                .classBuilder("CustomElement")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(zExtendsElement)
                .addField(FieldSpec
                        .builder(zExtendsElement, "parent")
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .build()
                ).addField(FieldSpec
                        .builder(elementVisitorClassName,"visitor")
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .build()
                ).addField(FieldSpec
                        .builder(String.class,"name")
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .build()
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                globalAttributesClassName,
                                customElementZExtendsElement,
                                zExtendsElement
                        )
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                divChoiceClassName,
                                customElementZExtendsElement,
                                zExtendsElement
                        )
                )
                .addMethod(
                        MethodSpec
                                .constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(zExtendsElement, "var1")
                                .addParameter(String.class, "var2")
                                .addStatement("this.parent = var1")
                                .addStatement("this.visitor = var1.getVisitor()")
                                .addStatement("this.name = var2")
                                .addStatement("this.visitor.visitElement(this)")
                                .build()
                );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("__")
                        .returns(zExtendsElement)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.visitor.visitParent(this)")
                        .addStatement("return this.parent")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getParent")
                        .returns(zExtendsElement)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return this.parent")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getVisitor")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .returns(elementVisitorClassName)
                        .addStatement("return this.visitor")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getName")
                        .returns(String.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return this.name")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("self")
                        .returns(customElementZExtendsElement)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addStatement("return this")
                        .build()
        );

        return builder;
    }

    //creates the ElementVisitor Class
    public static TypeSpec.Builder createElementVisitor() {
        return TypeSpec
                .classBuilder("ElementVisitor")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ClassName.get(ELEMENT_PACKAGE, "ElementVisitorBase"))
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build());
    }

}
