package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.AttributeGroupsGenerator.generateAttributeGroupsMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ChoiceGenerator.generateChoiceMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ElementGenerator.generateElementCompleteMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ElementGenerator.generateElementMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.EnumGenerator.generateEnumInterface;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.EnumGenerator.generateSimpleTypeMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.InfrastructureGenerator.createElementVisitor;
import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.firstToUpper;

public class ClassGenerator {

    public static void main(String[] args) {
        ClassGenerator generator = new ClassGenerator();
        Parser parser = new Parser();
        parser.parse();
        generator.generateClasses(parser);
    }

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

    private static final String ROOT_PATH = "./src/main/java";

    //public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster.newTest";

    public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String ENUM_INTERFACE_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String RESTRICTION_VALIDATOR_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static ClassName elementClassName = ClassName.get(ELEMENT_PACKAGE, "Element");

    static final TypeVariableName ELEMENT_T_Z =
            TypeVariableName.get("T", ParameterizedTypeName.get(elementClassName, TypeVariableName.get("T"), TypeVariableName.get("Z")));

    public static TypeVariableName zExtendsElement = TypeVariableName.get("Z", elementClassName);

    TypeSpec.Builder elementVisitor;

    public void generateClasses(Parser parser) {
        elementVisitor = createElementVisitor();

        parser.getElementsList().forEach(this::elementGenerator);
        parser.getChoiceList().forEach(this::choiceGenerator);
        parser.getGroupList().forEach(this::groupGenerator);
        generateSimpleType(parser);
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

    private void generateSimpleType(Parser parser) {
        createClass(generateEnumInterface());
        parser.getSimpleTypeList().forEach(this::simpleTypeGenerator);
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

    public static void createClass(TypeSpec.Builder interfaceBuilder) {
        if (interfaceBuilder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(CLASS_PACKAGE, interfaceBuilder.build()).build();
                javaFile.writeTo(new File(ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createClass(TypeSpec.Builder interfaceBuilder, String path) {
        if (interfaceBuilder != null) {
            try {
                JavaFile javaFile = JavaFile.builder(path, interfaceBuilder.build()).build();
                javaFile.writeTo(new File(ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
