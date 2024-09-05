package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import com.squareup.kotlinpoet.FileSpec;
import org.xmlet.extensionsGenerator.ExtensionsGenerator;
import org.xmlet.kotlinPoetGenerator.KClassGenerator;
import org.xmlet.newParser.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import static org.xmlet.javaPoetGenerator.AttributeGroupsGenerator.generateAttributeGroupsMethods;
import static org.xmlet.javaPoetGenerator.ChoiceGenerator.generateChoiceMethods;
import static org.xmlet.javaPoetGenerator.ElementGenerator.generateElementCompleteMethods;
import static org.xmlet.javaPoetGenerator.ElementGenerator.generateElementMethods;
import static org.xmlet.javaPoetGenerator.EnumGenerator.generateSimpleTypeMethods;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.javaPoetGenerator.InfrastructureGenerator.*;
import static org.xmlet.utils.Utils.firstToUpper;

public class ClassGenerator {

    public static void generateClasses(Parser parser) {
        createInfrastructureClasses();

        TypeSpec.Builder elementVisitor = createElementVisitor();

        ExtensionsGenerator.Companion.createXsd2PoetExtensions(
                extensionsFile -> {
                    parser.getElementsList().forEach(
                            element ->
                                    elementGenerator(element, elementVisitor, extensionsFile)
                    );
                    return true;
                }
        );

        parser.getChoiceList().forEach(ClassGenerator::choiceGenerator);
        parser.getGroupList().forEach(ClassGenerator::groupGenerator);
        parser.getSimpleTypeList().forEach(ClassGenerator::simpleTypeGenerator);
        parser.getAttrGroupsList().forEach(attrgroup -> attrGroupGenerator(attrgroup, elementVisitor));

        Map<String, Group> groupMap = parser.getGroupList().stream()
                .collect(Collectors.toMap(Group::getUpperCaseName, obj -> obj));

        parser.getElementCompleteList().forEach(elementComplete -> {
            elementComplete.getRefsList().forEach(dependency -> {
                addAttrDependencies(groupMap, dependency, elementComplete);
            });
            createClass(generateElementCompleteMethods(elementComplete, elementVisitor));
        });

        createClass(elementVisitor, ELEMENT_PACKAGE);
    }

    private static void createInfrastructureClasses() {
        createClass(createCustomAttributeGroup());
        createClass(createCustomElement());
        createClass(createBaseElement());
        createClass(createEnumInterface());
        createClass(createTextGroup());
        createClass(createAsyncElement(), ASYNC_PACKAGE);
        createClass(createAwaitConsumer(), ASYNC_PACKAGE);
        createClass(createOnCompletion(), ASYNC_PACKAGE);
        createClass(createText());
        KClassGenerator.Companion.createKotlinInfrastructureClasses();
    }

    private static void addAttrDependencies(Map<String, Group> groupMap, String dependency, ElementComplete elementComplete) {
        Group group = groupMap.get(firstToUpper(dependency));
        if (group != null) {
            elementComplete.addAttrs(group.getBaseClassValuesList());
            group.getRefList().forEach(ref -> addAttrDependencies(groupMap, ref, elementComplete));
        }
    }

    private static void simpleTypeGenerator(SimpleType simpleType) {
        createClass(generateSimpleTypeMethods(simpleType));
    }


    private static void elementGenerator(
            ElementXsd element,
            TypeSpec.Builder elementVisitor,
            FileSpec.Builder extensionsFile) {
        createClass(generateElementMethods(element, elementVisitor, extensionsFile));
    }

    private static void choiceGenerator(Choice choice) {createClass(generateChoiceMethods(choice));}

    private static void groupGenerator(Group group) {createClass(generateChoiceMethods(group));}

    private static void attrGroupGenerator(AttrGroup attrGroup, TypeSpec.Builder elementVisitor) {
        createClass(generateAttributeGroupsMethods(attrGroup, elementVisitor));
    }

    public static void addElementSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName, String className) {
        interfaceBuilder
                .addSuperinterface(
                        ParameterizedTypeName
                                .get(ClassName.get(CLASS_PACKAGE, superInterfaceName),
                                        ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), z), zExtendsElement)
                );
    }

    public static void addOthersSuperInterface(TypeSpec.Builder interfaceBuilder, String superInterfaceName) {
        interfaceBuilder.addSuperinterface(
                ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, superInterfaceName), t, z)
        );
    }

    public static void createClass(TypeSpec.Builder builder) {
        if (builder != null) {
            try {
                JavaFile.builder(CLASS_PACKAGE, builder.build()).build().writeTo(new File(JAVA_ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createClass(TypeSpec.Builder builder, String path) {
        if (builder != null) {
            try {
                JavaFile.builder(path, builder.build()).build().writeTo(new File(JAVA_ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
