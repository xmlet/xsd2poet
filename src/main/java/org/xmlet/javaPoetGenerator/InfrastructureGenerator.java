package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;
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
                .addSuperinterface(ParameterizedTypeName.get(asyncElementClassName, tExtendsElement))
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                elementExtensionsClassName, 
                                elementTExtendsElementZExtendsElement
                        )
                );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("self")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getVisitor")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(elementVisitorClassName)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getName")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(String.class)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("__")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(zExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getParent")
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .returns(zExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("addTextFromkotlin")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ParameterSpec.builder(String.class, "txt").addAnnotation(NotNull.class).build())
                        .addStatement("this.getVisitor().visitRaw(new $T(this.self(), this.getVisitor(), txt))", textClassName)
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("unaryPlus")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addAnnotation(NotNull.class)
                        .addParameter(ParameterSpec.builder(String.class, "$this$unaryPlus").addAnnotation(NotNull.class).build())
                        .addStatement("return addTextFromkotlin($$this$$unaryPlus)")
                        .returns(ParameterizedTypeName.get(elementClassName, t, z))
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("raw")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(r, "text")
                        .addTypeVariable(r)
                        .addStatement("this.getVisitor().visitRaw(new $T(this.self(), this.getVisitor(), text))", textClassName)
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("await")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ParameterizedTypeName.get(awaitConsumerClassName, t, m), "asyncAction")
                        .addTypeVariable(m)
                        .addStatement("final T self = self()")
                        .addStatement("this.getVisitor().visitAwait(self, asyncAction)")
                        .addStatement("return self")
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("dynamic")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(
                                ParameterizedTypeName.get(biConsumerClassName, t, u), 
                                "consumer"
                        ).addTypeVariable(u)
                        .addStatement("T self = this.self()")
                        .addStatement("this.getVisitor().visitDynamic(self, consumer)")
                        .addStatement("return self")
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("of")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(
                                ParameterizedTypeName.get(consumerClassName, t), "consumer")
                        .addStatement("T self = this.self()")
                        .addStatement("consumer.accept(self)")
                        .addStatement("return self")
                        .returns(tExtendsElement)
                        .build()
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

        TypeSpec.Builder builder = TypeSpec
                .classBuilder("ElementVisitor")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build());

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitElement")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(elementClassName, "element")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitAttribute")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "var1")
                        .addParameter(String.class, "var2")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitAttributeBoolean")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "var1")
                        .addParameter(String.class, "var2")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitParent")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(elementClassName, "element")
                        .build()
        );
        ParameterizedTypeName textType = 
                ParameterizedTypeName.get(
                        textClassName, 
                        WildcardTypeName.subtypeOf(elementClassName), 
                        r
                );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitText")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(r)
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitRaw")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(r)
                        .addParameter(textType, "var1")
                        .build());


        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitComment")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(r)
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitDynamic")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addTypeVariable(u)
                        .addParameter(e, "var1")
                        .addParameter(ParameterizedTypeName.get(biConsumerClassName,
                                e, u), "var2")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitAwait")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(m)
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(e, "var1")
                        .addParameter(
                                ParameterizedTypeName.get(awaitConsumerClassName, e, m), 
                                "var2"
                        ).build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitSuspending")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(m)
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(e, "var1")
                        .addParameter(
                                ParameterizedTypeName.get(suspendConsumerClassName, e, m), 
                                "var2"
                        ).build());

        return builder;
    }

    //creates the TextGroup Class
    public static TypeSpec.Builder createTextGroup() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("TextGroup")
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(elementTExtendsElementZExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);


        builder.addMethod(
                MethodSpec
                        .methodBuilder("text")
                        .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                        .addTypeVariable(r)
                        .addParameter(r, "text")
                        .addStatement("this.getVisitor().visitText(new $T(this.self(), this.getVisitor(), text))", textClassName)
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("comment")
                        .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                        .addTypeVariable(r)
                        .addParameter(r, "comment")
                        .addStatement("this.getVisitor().visitComment(new $T(this.self(), this.getVisitor(), comment))", textClassName)
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        return builder;
    }

    //creates the EnumInterface Class
    public static TypeSpec.Builder createEnumInterface() {
        return TypeSpec
                .interfaceBuilder("EnumInterface")
                .addTypeVariable(t)
                .addMethod(
                        MethodSpec
                                .methodBuilder("getValue")
                                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                                .returns(t)
                                .build()
                );
    }

    //creates the CustomAttribute Class
    public static TypeSpec.Builder createCustomAttributeGroup() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("CustomAttributeGroup")
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(elementTExtendsElementZExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        builder.addMethod(
                MethodSpec
                        .methodBuilder("addAttr")
                        .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                        .returns(tExtendsElement)
                        .addParameter(String.class, "name")
                        .addParameter(String.class, "value")
                        .addStatement("this.getVisitor().visitAttribute(name, value)")
                        .addStatement("return this.self()")
                        .build()
        );

        return builder;
    }

    //creates the AsyncElement Class
    public static TypeSpec.Builder createAsyncElement() {
        TypeVariableName eExtendsElement = TypeVariableName.get("E", elementClassName);

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("AsyncElement")
                .addTypeVariable(eExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        builder.addMethod(
                MethodSpec
                        .methodBuilder("await")
                        .addTypeVariable(m)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(
                                ParameterizedTypeName.get(awaitConsumerClassName, eExtendsElement, m),
                                "asyncAction"
                        )
                        .returns(eExtendsElement)
                        .build()
        );

        return builder;
    }

    //creates the AwaitConsumer Class
    public static TypeSpec.Builder createAwaitConsumer() {

        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("AwaitConsumer")
                .addTypeVariable(t)
                .addTypeVariable(m)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        builder.addMethod(
                MethodSpec
                        .methodBuilder("accept")
                        .addParameter(t, "first")
                        .addParameter(m, "model")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(onCompletitionClassName, "third")
                        .build()
        );

        return builder;
    }

    //creates the OnCompletition Class
    public static TypeSpec.Builder createOnCompletion() {

        return TypeSpec
                .interfaceBuilder("OnCompletion")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(
                        MethodSpec
                                .methodBuilder("finish")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(void.class)
                                .build()
                );
    }

    //creates the Text Class
    public static TypeSpec.Builder createText() {
        TypeVariableName z =
                TypeVariableName.get(
                        "Z",
                        ParameterizedTypeName.get(
                                elementClassName,
                                TypeVariableName.get("?"),
                                TypeVariableName.get("?")
                        )
                );

        TypeSpec.Builder builder = TypeSpec
                .classBuilder("Text")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(z)
                .addTypeVariable(r)
                .addField(FieldSpec
                        .builder(String.class, "text")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .build()
                )
                .addField(FieldSpec
                        .builder(z, "parent")
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .build()
                )
                .addField(FieldSpec
                        .builder(elementVisitorClassName, "visitor")
                        .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
                        .build()
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                elementClassName,
                                ParameterizedTypeName.get(textClassName, z, r),
                                z
                        )
                );

        builder.addMethod(
                MethodSpec
                        .constructorBuilder()
                        .addParameter(z, "parent")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(elementVisitorClassName, "visitor")
                        .addParameter(r, "text")
                        .addStatement("this.parent = parent")
                        .addStatement("this.visitor = visitor")
                        .addStatement("this.text = text.toString()")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("self")
                        .returns(ParameterizedTypeName.get(textClassName, z, r))
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return this")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("__")
                        .returns(z)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.visitor.visitText(this)")
                        .addStatement("return this.parent")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getParent")
                        .returns(z)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return this.parent")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getName")
                        .returns(String.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return \"\"")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getVisitor")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(elementVisitorClassName)
                        .addStatement("return this.visitor")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("getValue")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return this.text")
                        .build()
        );
        return builder;
    }
}
