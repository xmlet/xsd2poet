package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;
import org.xmlet.htmlapifaster.SuspendConsumer;
import org.xmlet.htmlapifaster.Text;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

import javax.lang.model.element.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.*;

public class InfrastructureGenerator {

    public static TypeSpec.Builder createBaseElement() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("Element")
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ASYNC_PACKAGE, "AsyncElement"), tExtendsElement))
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(
                                        CLASS_PACKAGE,
                                        "ElementExtensions"
                                ), ParameterizedTypeName.get(
                                        ClassName.get(CLASS_PACKAGE, "Element"),
                                        tExtendsElement,
                                        zExtendsElement
                                )
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
                        .returns(ClassName.get(ELEMENT_PACKAGE, "ElementVisitor"))
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
                        .addStatement("this.getVisitor().visitRaw(new $T(this.self(), this.getVisitor(), txt))", Text.class)
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
                        .returns(ParameterizedTypeName.get(elementClassName, TypeVariableName.get("T"), TypeVariableName.get("Z")))
                        .build()
        );

        TypeVariableName r = TypeVariableName.get("R");

        builder.addMethod(
                MethodSpec
                        .methodBuilder("raw")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(r, "text")
                        .addTypeVariable(r)
                        .addStatement("this.getVisitor().visitRaw(new $T(this.self(), this.getVisitor(), text))", Text.class)
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        TypeVariableName m = TypeVariableName.get("M");

        builder.addMethod(
                MethodSpec
                        .methodBuilder("await")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addParameter(ParameterizedTypeName.get(ClassName.get(ASYNC_PACKAGE, "AwaitConsumer"), TypeVariableName.get("T"), m), "asyncAction")
                        .addTypeVariable(m)
                        .addStatement("final T self = self()")
                        .addStatement("this.getVisitor().visitAwait(self, asyncAction)")
                        .addStatement("return self")
                        .returns(tExtendsElement)
                        .build()
        );

        TypeVariableName u = TypeVariableName.get("U");

        builder.addMethod(
                MethodSpec
                        .methodBuilder("dynamic")
                        .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
                        .addParameter(
                                ParameterizedTypeName.get(
                                        ClassName.get(BiConsumer.class),
                                        TypeVariableName.get("T"),
                                        u
                                ), "consumer")
                        .addTypeVariable(u)
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
                                ParameterizedTypeName.get(
                                        ClassName.get(Consumer.class),
                                        TypeVariableName.get("T")
                                ), "consumer")
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
                                ClassName.get(ELEMENT_PACKAGE, "GlobalAttributes"),
                                ParameterizedTypeName.get(
                                        ClassName.get(ELEMENT_PACKAGE, "CustomElement"),
                                        zExtendsElement
                                ),
                                zExtendsElement
                        )
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(ELEMENT_PACKAGE, "DivChoice"),
                                ParameterizedTypeName.get(
                                        ClassName.get(ELEMENT_PACKAGE, "CustomElement"),
                                        zExtendsElement
                                ),
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
                        .returns(ParameterizedTypeName.get(customElementClassName, zExtendsElement))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addStatement("return this")
                        .build()
        );

        return builder;
    }

    public static TypeSpec.Builder createElementVisitor() {

        //deleteOldElementVisitor();

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

        TypeVariableName rType = TypeVariableName.get("R");
        ParameterizedTypeName textType = ParameterizedTypeName.get(ClassName.get(ELEMENT_PACKAGE, "Text"), WildcardTypeName.subtypeOf(elementClassName), rType);

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitText")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitRaw")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());


        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitComment")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitDynamic")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addTypeVariable(TypeVariableName.get("U"))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("U")), "var2")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitAwait")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("M"))
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(AwaitConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("M")), "var2")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitSuspending")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("M"))
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(SuspendConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("M")), "var2")
                        .build());

        return builder;
    }

    public static TypeSpec.Builder createTextGroup() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("TextGroup")
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(ParameterizedTypeName.get(elementClassName, tExtendsElement, zExtendsElement))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        TypeVariableName r = TypeVariableName.get("R");


        builder.addMethod(
                MethodSpec
                        .methodBuilder("text")
                        .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                        .addTypeVariable(r)
                        .addParameter(r, "text")
                        .addStatement("this.getVisitor().visitText(new $T(this.self(), this.getVisitor(), text))", ClassName.get(ELEMENT_PACKAGE, "Text"))
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
                        .addStatement("this.getVisitor().visitComment(new $T(this.self(), this.getVisitor(), comment))", ClassName.get(ELEMENT_PACKAGE, "Text"))
                        .addStatement("return this.self()")
                        .returns(tExtendsElement)
                        .build()
        );

        return builder;
    }

    public static TypeSpec.Builder createEnumInterface() {
        return TypeSpec
                .interfaceBuilder("EnumInterface")
                .addTypeVariable(TypeVariableName.get("T"))
                .addMethod(
                        MethodSpec
                                .methodBuilder("getValue")
                                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                                .returns(TypeVariableName.get("T"))
                                .build()
                );
    }

    public static TypeSpec.Builder createCustomAttributeGroup() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("CustomAttributeGroup")
                .addTypeVariable(tExtendsElement)
                .addTypeVariable(zExtendsElement)
                .addSuperinterface(ParameterizedTypeName.get(elementClassName, tExtendsElement, zExtendsElement))
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

    public static TypeSpec.Builder createAsyncElement() {
        TypeVariableName eExtendsElement = TypeVariableName.get("E", elementClassName);
        TypeVariableName m = TypeVariableName.get("M");

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
                                ParameterizedTypeName.get(
                                        ClassName.get(ASYNC_PACKAGE, "AwaitConsumer"),
                                        eExtendsElement,
                                        m
                                ),
                                "asyncAction"
                        )
                        .returns(eExtendsElement)
                        .build()
        );

        return builder;
    }

    public static TypeSpec.Builder createAwaitConsumer() {
        TypeVariableName t = TypeVariableName.get("T");

        TypeVariableName m = TypeVariableName.get("M");


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
                        .addParameter(ClassName.get(ASYNC_PACKAGE, "OnCompletion"), "third")
                        .build()
        );

        return builder;
    }

    public static TypeSpec.Builder createOnCompletion() {
        TypeSpec.Builder builder = TypeSpec
                .interfaceBuilder("OnCompletion")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(
                        MethodSpec
                                .methodBuilder("finish")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(void.class)
                                .build()
                );

        return builder;
    }

    /*
    private static void deleteOldElementVisitor() {
        Path filePath = Paths.get("src/main/java/org/xmlet/htmlapifaster/ElementVisitor.java");

        try {
            if (Files.exists(filePath))
                Files.delete(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}
