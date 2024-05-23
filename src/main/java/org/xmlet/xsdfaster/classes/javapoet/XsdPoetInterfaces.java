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
        ClassName className = ClassName.get(CLASS_PACKAGE, firstToUpper(element.getName()));
        interfaceBuilder.addMethod(
                MethodSpec
                        .methodBuilder(element.getName())
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



    private void createClass(Builder interfaceBuilder) {
        if (interfaceBuilder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(CLASS_PACKAGE, interfaceBuilder.build()).build();
                javaFile.writeTo(new File(ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
