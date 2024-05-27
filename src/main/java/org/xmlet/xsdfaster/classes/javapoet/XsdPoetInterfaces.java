package org.xmlet.xsdfaster.classes.javapoet;

import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import org.xmlet.xsdparser.xsdelements.XsdAbstractElement;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdGroup;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.firstToUpper;

public class XsdPoetInterfaces {

    private static final String ROOT_PATH = "./src/main/java";

    private static final String NEW_DIR = "/teste";

    private static final String BICONSUMER_PACKAGE = "java.util.function.BiConsumer";

    private static final String CLASS_PATH = "/org/xmlet/htmlapifaster";

    private static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    private static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster.teste";

    private static final String CHOICE_SUFFIX = "Choice";

    private static final String ALL_SUFFIX = "All";


    private String getInterfaceName(String className, String suffix) {
        return firstToUpper(className + suffix);
    }

    private Builder createInterfaceAndFunctions(List<XsdAbstractElement> directElements, String interfaceName) {
        Builder interfaceBuilder = createInterfaceBuilder(interfaceName);

        final String[] defaultSuperInterface = {"TextGroup"};
        final int[] numberOfCreatedFunctions = {0};
        directElements.forEach(element -> {
            if (element instanceof XsdElement) {
                addFunctionToInterface(interfaceBuilder, ((XsdElement) element));
                numberOfCreatedFunctions[0] += 1;
            } else if (element instanceof XsdGroup) {
                defaultSuperInterface[0] = firstToUpper(((XsdGroup) element).getName() + "Choice");
            }
        });
        addSuperInterface(interfaceBuilder, defaultSuperInterface[0]);
        return numberOfCreatedFunctions[0] != 0 ? interfaceBuilder : null;
    }

    private Builder createInterfaceBuilder(String interfaceName) {
        ClassName classNameElement = ClassName.get(ELEMENT_PACKAGE, "Element");
        TypeVariableName t = TypeVariableName.get("T", ParameterizedTypeName.get(classNameElement, TypeVariableName.get("T"), TypeVariableName.get("Z")));
        TypeVariableName z = TypeVariableName.get("Z", classNameElement);
        return TypeSpec
                .interfaceBuilder(XsdPoetUtils.firstToUpper(interfaceName))
                .addTypeVariable(t)
                .addTypeVariable(z)
                .addModifiers(Modifier.PUBLIC);
    }

    private void addSuperInterface(Builder interfaceBuilder, String superInterfaceName) {
        ClassName classNameElement = ClassName.get(ELEMENT_PACKAGE, "Element");
        TypeVariableName t = TypeVariableName.get("T", ParameterizedTypeName.get(classNameElement, TypeVariableName.get("T"), TypeVariableName.get("Z")));
        TypeVariableName z = TypeVariableName.get("Z", classNameElement);
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(
                                        ClassName
                                                .get(superInterfaceName.equals("TextGroup") ? ELEMENT_PACKAGE : CLASS_PACKAGE, superInterfaceName), t, z)
                );
    }

    private void addFunctionToInterface(Builder interfaceBuilder, XsdElement element) {
        String elementName = element.getName();
        ClassName className = ClassName.get(CLASS_PACKAGE, firstToUpper(elementName));
        interfaceBuilder.addMethod(
                MethodSpec
                        .methodBuilder(elementName)
                        .addModifiers(Modifier.DEFAULT)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ParameterizedTypeName.get(className, TypeVariableName.get("T")))
                        .addStatement("return new $T(this.self())", className)
                        .build()
        );
    }

    public void createAndGetAllInterface(List<XsdAbstractElement> directElements, String className) {
        checkFileAndCreateInterface(directElements,className, ALL_SUFFIX);
    }
    public void createAndGetChoiceInterface(List<XsdAbstractElement> directElements, String className) {
        checkFileAndCreateInterface(directElements,className, CHOICE_SUFFIX);
    }

    public void checkFileAndCreateInterface(List<XsdAbstractElement> directElements, String className, String suffix) {
        String interfaceName = getInterfaceName(className, suffix);
        String path = ROOT_PATH + CLASS_PATH + NEW_DIR  + "/" + interfaceName + ".java";
        File newFile = new File(path);
        //System.out.println("A ver se o ficheiro no caminho \"" + path + "\" existe ou não");
        if (!newFile.exists()) {
            //System.out.println("Ficheiro ainda não existe classe " + className + " vai ser criada.");
            Builder builder = createInterfaceAndFunctions(directElements, interfaceName);
            createClass(builder);
        } else {
            //System.out.println("Ficheiro já existe classe " + className + " já foi criada.");
        }
    }



    public void createClass(Builder interfaceBuilder) {
        if (interfaceBuilder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(CLASS_PACKAGE, interfaceBuilder.build()).build();
                javaFile.writeTo(new File(ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createElement(XsdElement element, Builder elementVisitorBuilder) {
        String elementName = element.getName();
        String className = firstToUpper(elementName);
        ClassName elementVisitor = ClassName.get(CLASS_PACKAGE, "ElementVisitor");

        TypeVariableName z = TypeVariableName.get("Z", ClassName.get(ELEMENT_PACKAGE, "Element"));

        MethodSpec firstConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(elementVisitor, "visitor")
                .addStatement("this.visitor = visitor")
                .addStatement("this.parent = null")
                .addStatement("visitor.visitElement" + className + "(this)")
                .build();

        MethodSpec secondConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(z, "parent")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = parent.getVisitor()")
                .addStatement("this.visitor.visitElement" + className + "(this)")
                .build();

        MethodSpec thirdConstrutor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(z, "parent")
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
                .returns(z)
                .addStatement("this.visitor.visitParentElement" + className + "(this)")
                .addStatement("return this.parent")
                .build();

        MethodSpec getParent = MethodSpec.methodBuilder("getParent")
                .addModifiers(Modifier.PUBLIC)
                .returns(z)
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
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), z))
                .addStatement("return this")
                .build();
        Builder builder = TypeSpec
                .classBuilder(className)
                .addTypeVariable(z)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(elementVisitor, "visitor", Modifier.PROTECTED, Modifier.FINAL)
                .addField(z, "parent", Modifier.PROTECTED, Modifier.FINAL)
                .addMethod(firstConstructor)
                .addMethod(secondConstructor)
                .addMethod(thirdConstrutor)
                .addMethod(terminator)
                .addMethod(getParent)
                .addMethod(getVisitor)
                .addMethod(getName)
                .addMethod(self);

        MethodSpec elementVisitorMethod = MethodSpec.methodBuilder("visitParent" + className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(z)
                .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), z), elementName)
                .addStatement("this.visitParent("+ elementName + ")")
                .build();

        elementVisitorBuilder.addMethod(elementVisitorMethod);

        createClass(builder);
    }

    public Builder createElementVisitorBuilder() {
        ClassName classNameElement = ClassName.get(ELEMENT_PACKAGE, "Element");
        Builder builder = TypeSpec
                .classBuilder("ElementVisitor")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.constructorBuilder().build());

        MethodSpec visitElement = MethodSpec.methodBuilder("visitElement")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .addParameter(classNameElement, "element")
                .build();

        MethodSpec visitParent = MethodSpec.methodBuilder("visitParent")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .addParameter(classNameElement, "element")
                .build();

        MethodSpec visitAttribute = MethodSpec.methodBuilder("visitAttribute")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .addParameter(String.class, "name")
                .addParameter(String.class, "value")
                .build();

        MethodSpec visitAttributeBoolean = MethodSpec.methodBuilder("visitAttributeBoolean")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .addParameter(String.class, "name")
                .addParameter(String.class, "value")
                .build();

        /*
        TypeVariableName r = TypeVariableName.get("R");
        ParameterizedTypeName textType = ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, "Text"), classNameElement, r);

        MethodSpec visitText = MethodSpec.methodBuilder("visitText")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(textType, "text")
                .build();

        MethodSpec visitRaw = MethodSpec.methodBuilder("visitRaw")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(textType, "text")
                .build();

        MethodSpec visitComment = MethodSpec.methodBuilder("visitComment")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(textType, "text")
                .build();

        TypeVariableName e = TypeVariableName.get("E", classNameElement);

        MethodSpec visitDynamic = MethodSpec.methodBuilder("visitDynamic")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(e)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(e, "element")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BICONSUMER_PACKAGE, "BiConsumer"), e, r), "consumer")
                .build();

        MethodSpec visitAwait = MethodSpec.methodBuilder("visitAwait")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(e)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(e, "element")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BICONSUMER_PACKAGE, "BiConsumer"), e, r), "consumer")
                .build();

        MethodSpec visitSuspending = MethodSpec.methodBuilder("visitSuspending")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(e)
                .addTypeVariable(r)
                .returns(void.class)
                .addParameter(e, "element")
                .addParameter(ParameterizedTypeName.get(ClassName.get(BICONSUMER_PACKAGE, "BiConsumer"), e, r), "consumer")
                .build();
        */
        builder
                .addMethod(visitElement)
                .addMethod(visitParent)
                .addMethod(visitAttribute)
                .addMethod(visitAttributeBoolean);
        return builder;
    }
}
