package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
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
                .collect(Collectors.toMap(Group::getUpperCaseName, obj -> obj));

        parser.getElementCompleteList().forEach(elementComplete -> {
            elementComplete.getRefsList().forEach(dependency -> {
                addAttrDependencies(groupMap, dependency, elementComplete);
            });
            createClass(generateElementCompleteMethods(elementComplete, elementVisitor));
        });

        createClass(elementVisitor, ELEMENT_PACKAGE);

    }

    private void addAttrDependencies(Map<String, Group> groupMap, String dependency, ElementComplete elementComplete) {
        Group group = groupMap.get(firstToUpper(dependency));
        if (group != null) {
            elementComplete.addAttrs(group.getBaseClassValuesList());
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

    public void createClass(TypeSpec.Builder builder, String path) {
        if (builder != null) {
            try {
                JavaFile.builder(path, builder.build()).build().writeTo(new File(JAVA_ROOT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
