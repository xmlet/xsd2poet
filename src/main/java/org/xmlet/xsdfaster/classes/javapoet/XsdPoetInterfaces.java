package org.xmlet.xsdfaster.classes.javapoet;

import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import org.xmlet.xsdparser.xsdelements.*;
import org.xmlet.xsdparser.xsdelements.elementswrapper.ReferenceBase;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.firstToUpper;

public class XsdPoetInterfaces {

    private static final HashSet<String> attributeClassesSet = new HashSet<>();

    private static final HashSet<String> attributeFunctionSet = new HashSet<>();

    private static final HashSet<String> attributesFromSuperClass = new HashSet<>();

    private static final HashSet<String> primitivesSet = new HashSet<>(
            Set.of(
                    "boolean",
                    "byte",
                    "char",
                    "short",
                    "int",
                    "long",
                    "float",
                    "double",
                    "void",
                    "string")
    );

    private static final String primitivesPackage = "java.lang";

    private static final String ROOT_PATH = "./src/main/java";

    private static final String NEW_DIR = "/teste";

    private static final String BICONSUMER_PACKAGE = "java.util.function.BiConsumer";

    private static final String CLASS_PATH = "/org/xmlet/htmlapifaster";

    private static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    private static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster.teste";

    private static final String CHOICE_SUFFIX = "Choice";

    private static final String ALL_SUFFIX = "All";

    TypeVariableName zExtendsElement = TypeVariableName.get("Z", ClassName.get(ELEMENT_PACKAGE, "Element"));

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
        Builder builder = TypeSpec
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

        List<ReferenceBase> attributeGroups = element.getXsdComplexType().getVisitor().getAttributeGroups();

        for (ReferenceBase attributeGroup : attributeGroups) {
            String interfaceName = XsdPoetUtils.firstToUpper(getName(attributeGroup.getElement()));
            builder.addSuperinterface(
                    ParameterizedTypeName
                            .get(ClassName.get(CLASS_PACKAGE, interfaceName), TypeVariableName.get(className+"<Z>"), zExtendsElement)
            );
            if (!attributeClassesSet.contains(interfaceName)) {
                XsdAttributeGroup group = (XsdAttributeGroup) attributeGroup.getElement();
                ClassName classNameElement = ClassName.get(ELEMENT_PACKAGE, "Element");
                TypeVariableName t = TypeVariableName.get("T", ParameterizedTypeName.get(classNameElement, TypeVariableName.get("T"), TypeVariableName.get("Z")));
                Builder interfaceBuilder = TypeSpec
                        .interfaceBuilder(interfaceName)
                        .addTypeVariable(t)
                        .addTypeVariable(TypeVariableName.get("Z", classNameElement))
                        .addModifiers(Modifier.PUBLIC);
                group.getAllAttributes().forEach(attribute -> {
                    createAttrMethodSuperClass(attribute,interfaceBuilder,elementVisitorBuilder, className);
                });

                createClass(interfaceBuilder);
                System.out.println("breakpoint");
                attributeClassesSet.add(interfaceName);
            }
        }

        element.getXsdComplexType().getVisitor().getAttributes().forEach(attribute -> {
            XsdAttribute attr = (XsdAttribute) attribute.getElement();
            String attributeName = attr.getName();
            if (!attributesFromSuperClass.contains(attributeName)) {
                String attributeClassName = firstToUpper(attributeName);
                String attrName = "attr" + attributeClassName;
                String elementVisitorFunctionName = "visitAttribute" + attributeClassName;
                MethodSpec.Builder methodBuilder = createAttrMethodBuilder(attrName, elementVisitorFunctionName, className,"visitor", Modifier.FINAL);
                builder.addMethod(methodBuilder.build());
                if (!attributeFunctionSet.contains(attributeName)) {
                    elementVisitorBuilder.addMethod(
                            MethodSpec.methodBuilder(elementVisitorFunctionName)
                                    .addParameter(String.class, attrName)
                                    .addStatement("this.visitAttribute(\"" + attributeName + "\"," + attrName + ")")
                                    .build()
                    );

                    attributeFunctionSet.add(attributeName);
                }
            }
        });



        MethodSpec parentVisitorMethod = MethodSpec.methodBuilder("visitElement" + className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(zExtendsElement)
                .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement), elementName)
                .addStatement("this.visitElement("+ elementName + ")")
                .build();

        MethodSpec elementVisitorMethod = MethodSpec.methodBuilder("visitParent" + className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(zExtendsElement)
                .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement), elementName)
                .addStatement("this.visitParent("+ elementName + ")")
                .build();

        elementVisitorBuilder.addMethod(parentVisitorMethod);
        elementVisitorBuilder.addMethod(elementVisitorMethod);

        createClass(builder);
    }

    private void createAttrMethodSuperClass(XsdAttribute attribute, Builder interfaceBuilder, Builder elementVisitorBuilder, String className) {
        String attributeName = attribute.getName();
        String attributeClassName = firstToUpper(attributeName);
        String attrName = "attr" + attributeClassName;
        String elementVisitorFunctionName = "visitAttribute" + attributeClassName;
        if (!attributeFunctionSet.contains(attributeName)) {
            MethodSpec.Builder methodBuilder = createAttrMethodBuilder(attrName,elementVisitorFunctionName, className,"getVisitor()", Modifier.DEFAULT);
            String type = attribute.getType().substring("xsd:".length());
            if (primitivesSet.contains(type)) {
                methodBuilder.addParameter(ClassName.get(primitivesPackage, firstToUpper(type)), attrName);
            } else {
                methodBuilder.addParameter(ClassName.get(CLASS_PACKAGE, "Enum" + attributeClassName + "Type"), attrName);
            }
            interfaceBuilder.addMethod(methodBuilder.build());

            elementVisitorBuilder.addMethod(
                    MethodSpec.methodBuilder(elementVisitorFunctionName)
                            .addParameter(String.class, attrName)
                            .addStatement("this.visitAttribute(\"" + attributeName +"\"," + attrName +")")
                            .build()
            );
            attributesFromSuperClass.add(attributeName);
            attributeFunctionSet.add(attributeName);
        }
    }

    private MethodSpec.Builder createAttrMethodBuilder(
            String attrName,
            String elementVisitorFunctionName,
            String className,
            String visitor,
            Modifier modifier
    ) {
        return  MethodSpec.methodBuilder(attrName)
                .addModifiers(Modifier.PUBLIC, modifier)
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement))
                .addParameter(String.class, attrName)
                .addStatement("this." + visitor + "." + elementVisitorFunctionName + "(" + attrName + ")") //unica coisa diferente do codigo acima
                .addStatement("return this.self()");
    }

    public String getName(XsdAbstractElement element) {
        return element.getElementFieldsMap().getOrDefault("name", (String) null);
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
