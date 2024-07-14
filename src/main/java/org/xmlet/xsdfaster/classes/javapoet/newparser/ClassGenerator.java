package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.parser.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.AttributeGroupsGenerator.generateAttributeGroupsMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ChoiceGenerator.generateChoiceMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ElementGenerator.generateElementMethods;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.EnumGenerator.generateEnumInterface;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.EnumGenerator.generateSimpleTypeMethods;

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
        primitiveAndStringTypes.put("xsd:boolean", boolean.class);
        primitiveAndStringTypes.put("xsd:byte", byte.class);
        primitiveAndStringTypes.put("xsd:char", char.class);
        primitiveAndStringTypes.put("xsd:short", short.class);
        primitiveAndStringTypes.put("xsd:int", int.class);
        primitiveAndStringTypes.put("xsd:long", long.class);
        primitiveAndStringTypes.put("xsd:float", float.class);
        primitiveAndStringTypes.put("xsd:double", double.class);
        primitiveAndStringTypes.put("xsd:string", String.class);
        primitiveAndStringTypes.put("xsd:anyURI", String.class);
        primitiveAndStringTypes.put("sizesType", String.class);
    }

    public static final Set<String> specialTypes = Set.of("sizesType");

    private static final String ROOT_PATH = "./src/main/java";

    public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster.newTest";

    private static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    public static ClassName elementClassName = ClassName.get(ELEMENT_PACKAGE, "Element");

    static final TypeVariableName ELEMENT_T_Z =
            TypeVariableName.get("T", ParameterizedTypeName.get(elementClassName, TypeVariableName.get("T"), TypeVariableName.get("Z")));

    public static TypeVariableName zExtendsElement = TypeVariableName.get("Z", elementClassName);


    public void generateClasses(Parser parser) {
        parser.getElementsList().forEach(this::elementGenerator);
        parser.getChoiceList().forEach(this::choiceGenerator);
        parser.getGroupList().forEach(this::groupGenerator);
        generateSimpleType(parser);
        parser.getAttrGroupsList().forEach(this::attrGroupGenerator);

    }

    private void generateSimpleType(Parser parser) {
        createClass(generateEnumInterface());
        parser.getSimpleTypeList().forEach(this::simpleTypeGenerator);
    }

    private void simpleTypeGenerator(SimpleType simpleType) {
        createClass(generateSimpleTypeMethods(simpleType));
    }


    private void elementGenerator(ElementXsd element) {
        createClass(generateElementMethods(element));
    }

    private void choiceGenerator(Choice choice) {createClass(generateChoiceMethods(choice));}

    private void groupGenerator(Group group) {createClass(generateChoiceMethods(group));}

    private void attrGroupGenerator(AttrGroup attrGroup) { createClass(generateAttributeGroupsMethods(attrGroup));}

    public static void addElementSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName, String className) {
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(ClassName.get(superInterfaceName.equals("TextGroup") ? ELEMENT_PACKAGE : CLASS_PACKAGE, superInterfaceName),
                                        ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), TypeVariableName.get("Z")), zExtendsElement)
                );
    }

    public static void addOthersSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName, String className) {
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(ClassName.get(superInterfaceName.equals("TextGroup") ? ELEMENT_PACKAGE : CLASS_PACKAGE, superInterfaceName),
                                        TypeVariableName.get("T"), TypeVariableName.get("Z"))
                );
    }

    public void createClass(TypeSpec.Builder interfaceBuilder) {
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
