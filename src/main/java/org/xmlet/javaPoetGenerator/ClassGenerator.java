package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import org.xmlet.kotlinPoetGenerator.KClassGenerator;
import org.xmlet.newParser.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.xmlet.javaPoetGenerator.AttributeGroupsGenerator.generateAttributeGroupsMethods;
import static org.xmlet.javaPoetGenerator.ChoiceGenerator.generateChoiceMethods;
import static org.xmlet.javaPoetGenerator.ElementGenerator.generateElementCompleteMethods;
import static org.xmlet.javaPoetGenerator.ElementGenerator.generateElementMethods;
import static org.xmlet.javaPoetGenerator.EnumGenerator.generateSimpleTypeMethods;
import static org.xmlet.javaPoetGenerator.InfrastructureGenerator.*;
import static org.xmlet.utils.Utils.firstToUpper;

public class ClassGenerator {
    public static final Map<String,Class<?>> primitiveAndStringTypes;

    static {
        primitiveAndStringTypes = new HashMap<>();
        primitiveAndStringTypes.put("xsd:boolean", Boolean.class);
        primitiveAndStringTypes.put("xsd:byte", Byte.class);
        primitiveAndStringTypes.put("xsd:char", Character.class);
        primitiveAndStringTypes.put("xsd:short", Short.class);
        primitiveAndStringTypes.put("xsd:int", Integer.class);
        primitiveAndStringTypes.put("xsd:positiveInteger", Integer.class);
        primitiveAndStringTypes.put("xsd:long", Long.class);
        primitiveAndStringTypes.put("xsd:float", Float.class);
        primitiveAndStringTypes.put("xsd:double", Double.class);
        primitiveAndStringTypes.put("xsd:string", String.class);
        primitiveAndStringTypes.put("xsd:anyURI", String.class);
        primitiveAndStringTypes.put("sizesType", String.class);
    }

    private static final Set<String> differentPackageCLasses = Set.of("CustomAttributeGroup", "Element", "Text", "TextGroup");

    static final Set<String> invalidStrings = Set.of("class", "default", "for");

    public static final Set<String> specialTypes = Set.of("sizesType");

    public static final String JAVA_ROOT_PATH = "./src/main/java";

    public static final String KOTLIN_ROOT_PATH = "./src/main/kotlin";

    //public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster.newTest";

    public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String ENUM_INTERFACE_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String ASYNC_PACKAGE = "org.xmlet.htmlapifaster.async";

    public static final String RESTRICTION_VALIDATOR_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static ClassName elementClassName = ClassName.get(ELEMENT_PACKAGE, "Element");

    public static ClassName elementVisitorClassName = ClassName.get(ELEMENT_PACKAGE, "ElementVisitor");

    public static ClassName textClassName = ClassName.get(ELEMENT_PACKAGE, "Text");

    public static ClassName awaitConsumerClassName = ClassName.get(ASYNC_PACKAGE, "AwaitConsumer");

    public static ClassName suspendConsumerClassName = ClassName.get(ELEMENT_PACKAGE, "SuspendConsumer");

    public static ClassName customElementClassName = ClassName.get(ELEMENT_PACKAGE, "CustomElement");

    static final TypeVariableName ELEMENT_T_Z =
            TypeVariableName.get("T", ParameterizedTypeName.get(elementClassName, TypeVariableName.get("T"), TypeVariableName.get("Z")));

    public static TypeVariableName zExtendsElement = TypeVariableName.get("Z", elementClassName);

    public static TypeVariableName tExtendsElement = TypeVariableName.get("T", elementClassName);


    TypeSpec.Builder elementVisitor;

    public void generateClasses(Parser parser) {

        createClass(createCustomAttributeGroup(), CLASS_PACKAGE);
        createClass(createCustomElement(), CLASS_PACKAGE);
        createClass(createBaseElement(), CLASS_PACKAGE);
        elementVisitor = createElementVisitor();
        createClass(createEnumInterface(), CLASS_PACKAGE);
        createClass(createTextGroup(), CLASS_PACKAGE);


        createClass(createAsyncElement(), ASYNC_PACKAGE);
        createClass(createAwaitConsumer(),ASYNC_PACKAGE);
        createClass(createOnCompletion(),ASYNC_PACKAGE);
        createClass(createText(), CLASS_PACKAGE);

        KClassGenerator.Companion.createKotlinInfrastructureClasses();


        parser.getElementsList().forEach(this::elementGenerator);
        parser.getChoiceList().forEach(this::choiceGenerator);
        parser.getGroupList().forEach(this::groupGenerator);
        parser.getSimpleTypeList().forEach(this::simpleTypeGenerator);
        parser.getAttrGroupsList().forEach(this::attrGroupGenerator);

        Map<String, Group> groupMap = parser.getGroupList().stream()
                .collect(Collectors.toMap(Group::getName, obj -> obj));

        parser.getElementCompleteList().forEach(elementComplete -> {
            elementComplete.getDependencyList().forEach(dependency -> {
                addAttrDependencies(groupMap,dependency, elementComplete);
            });
            createClass(generateElementCompleteMethods(elementComplete, elementVisitor));
        });

        createClass(elementVisitor, ELEMENT_PACKAGE);

    }

    private void addAttrDependencies(Map<String, Group> groupMap, String dependency, ElementComplete elementComplete) {
        Group group = groupMap.get(firstToUpper(dependency));
        if (group != null) {
            elementComplete.addAttrs(group.getChoiceList());
            group.getRefList().forEach(ref -> addAttrDependencies(groupMap, ref, elementComplete));
        }
    }

    private void simpleTypeGenerator(SimpleType simpleType) {
        createClass(generateSimpleTypeMethods(simpleType));
    }


    private void elementGenerator(ElementXsd element) {
        createClass(generateElementMethods(element, elementVisitor));
    }

    private void choiceGenerator(Choice choice) {createClass(generateChoiceMethods(choice));}

    private void groupGenerator(Group group) {createClass(generateChoiceMethods(group));}

    private void attrGroupGenerator(AttrGroup attrGroup) { createClass(generateAttributeGroupsMethods(attrGroup, elementVisitor));}

    public static void addElementSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName, String className) {
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(ClassName.get(differentPackageCLasses.contains(superInterfaceName) ? ELEMENT_PACKAGE : CLASS_PACKAGE, superInterfaceName),
                                        ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), TypeVariableName.get("Z")), zExtendsElement)
                );
    }

    public static void addOthersSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName, String className) {
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(ClassName.get(differentPackageCLasses.contains(superInterfaceName) ? ELEMENT_PACKAGE : CLASS_PACKAGE, superInterfaceName),
                                        TypeVariableName.get("T"), TypeVariableName.get("Z"))
                );
    }

    public static void createClass(TypeSpec.Builder builder) {
        if (builder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(CLASS_PACKAGE, builder.build()).build();
                javaFile.writeTo(new File(JAVA_ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createClass(TypeSpec.Builder builder, String path) {
        if (builder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(path, builder.build()).build();
                javaFile.writeTo(new File(JAVA_ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
