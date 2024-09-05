package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.*;
import com.squareup.kotlinpoet.FileSpec;
import kotlin.Pair;
import org.xmlet.extensionsGenerator.ExtensionsGenerator;
import org.xmlet.newParser.ElementComplete;
import org.xmlet.newParser.ElementXsd;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

import static org.xmlet.javaPoetGenerator.ClassGenerator.*;
import static org.xmlet.javaPoetGenerator.GeneralGenerator.generateAttrFunction;
import static org.xmlet.javaPoetGenerator.GeneralGenerator.generateSequenceMethod;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.*;
import static org.xmlet.utils.Utils.*;
import static org.xmlet.utils.Utils.getVisitAttrName;


/**
 * This class has the objective to create a java file for each HTML Element
 * */
public class ElementGenerator {

    private static final Set<String> classesWithNoExtensions = Set.of("Text", "Html");

    //Set used to avoid creating duplicated function in elementVisitor
    private static HashSet<String> createdFunctions = new HashSet<>();

    static public TypeSpec.Builder generateElementMethods(
            ElementXsd element,
            TypeSpec.Builder elementVisitorBuilder,
            FileSpec.Builder extensionsFile) {

        String lowerCaseName = element.getLowerCaseName();
        String className = element.getFinalClassName();

        //generates the common element structure of all the elements, it's complex because it's a "normal" html element
        TypeSpec.Builder builder = generateComplexCommonElementStructure(elementVisitorBuilder, className, lowerCaseName);

        //generates the imports of this element
        element.getRefsList().forEach(reference -> addElementSuperInterface(builder, reference, className));

        //generates the attributes of this element
        element.getAttrValuesList().forEach(pair -> addAttrFunction(builder, pair, className, elementVisitorBuilder));

        //generates the sequence logic, if the element had a sequence in the xsd file
        handleSequence(element, builder, elementVisitorBuilder);

        if (!classesWithNoExtensions.contains(className)) {
            ExtensionsGenerator.Companion.addExtensions(extensionsFile, element);
        }

        return builder;
    }

    /**
     * logic to handle elements that had a xsd:sequence in the xsd file
     * */
    static private void handleSequence(
            ElementXsd element,
            TypeSpec.Builder builder,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        if (element.hasSequence()) {
            element.getSequenceElements().forEach(sequenceElement -> {
                /**
                 * generate the sequence method in the Element class for each xsd:element
                 * <xsd:sequence>
                 *      <xsd:element ref="summary"/>  <--- for each of xsd:element inside a xsd:sequence
                 *              ...
                 * </xsd:sequence>
                 * */
                generateSequenceMethod(builder, element.getFinalClassName() + firstToUpper(sequenceElement), sequenceElement);

                //this logic is to create the <ElementName>Complete class
                String completeClassName = element.getFinalClassName() + "Complete";
                //created the simple structure of a element
                TypeSpec.Builder sequenceClass =
                        generateSimpleCommonElementStructure(
                                elementVisitorBuilder,
                                completeClassName,
                                element.getLowerCaseName()
                        );

                //It always has the CustomAttributeGroup has a super interface
                addElementSuperInterface(sequenceClass, "CustomAttributeGroup", completeClassName );

                createClass(sequenceClass);
            });
        }
    }

    /**
     * This method is to generate the "<Element Name>Complete" methods
     * */
    static public TypeSpec.Builder generateElementCompleteMethods(
            ElementComplete element,
            TypeSpec.Builder elementVisitorBuilder
    ) {

        // generates the simple structure
        TypeSpec.Builder builder = generateSimpleCommonElementStructure(
                elementVisitorBuilder,
                element.getFinalClassName(),
                element.getLowerCaseName()
        );

        //generates the superInterace
        addElementSuperInterface(builder, "CustomAttributeGroup", element.getFinalClassName());

        //generates a method for each attribute
        element.getAttrs().forEach(attr -> {
            generateSequenceMethod(builder, element.getUpperCaseName(), attr);
        });

        return builder;
    }

    /**
     * Generate the common element structure.
     * All elements will have the same structure
     * With some exceptions that some have some more methods than others (corner cases Ex:DetailsComplete or DetailsSummary)
     * */
    private static TypeSpec.Builder generateCommonElementStructure(
            TypeSpec.Builder elementVisitorBuilder,
            String className,
            String elementName,
            ElementType elementType) {

        if (elementType == ElementType.COMPLEX)
            elementVisitorBuilder.addMethod(
                    MethodSpec
                            .methodBuilder("visitElement" + className)
                            .addModifiers(Modifier.PUBLIC)
                            .addTypeVariable(zExtendsElement)
                            .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className),zExtendsElement),elementName)
                            .addStatement("this.visitElement(" + elementName + ")")
                            .build()
            );

        elementVisitorBuilder.addMethod(
                MethodSpec
                        .methodBuilder("visitParent" + className)
                        .addModifiers(Modifier.PUBLIC)
                        .addTypeVariable(zExtendsElement)
                        .addParameter(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className),zExtendsElement),elementName)
                        .addStatement("this.visitParent(" + elementName + ")")
                        .build()
        );

        TypeSpec.Builder builder = TypeSpec
                .classBuilder( className)
                .addTypeVariable(zExtendsElement)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(elementVisitorClassName, "visitor", Modifier.PROTECTED, Modifier.FINAL)
                .addField(zExtendsElement, "parent", Modifier.PROTECTED, Modifier.FINAL);


        MethodSpec.Builder firstConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(elementVisitorClassName, "visitor")
                .addStatement("this.visitor = visitor")
                .addStatement("this.parent = null");

        if (elementType == ElementType.COMPLEX)
            firstConstructor.addStatement("visitor.visitElement" + className + "(this)");

        MethodSpec.Builder secondConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(zExtendsElement, "parent")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = parent.getVisitor()");

        if (elementType == ElementType.COMPLEX)
            secondConstructor.addStatement("this.visitor.visitElement" + className + "(this)");

        MethodSpec.Builder thirdConstrutor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(zExtendsElement, "parent")
                .addParameter(elementVisitorClassName, "visitor")
                .addParameter(boolean.class, "shouldVisit")
                .addStatement("this.parent = parent")
                .addStatement("this.visitor = visitor");

        if (elementType == ElementType.COMPLEX)
            thirdConstrutor
                    .beginControlFlow("if (shouldVisit)")
                    .addStatement("visitor.visitElement" + className + "(this)")
                    .endControlFlow();


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
                .returns(elementVisitorClassName)
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

        builder.addMethod(firstConstructor.build())
                .addMethod(secondConstructor.build())
                .addMethod(thirdConstrutor.build())
                .addMethod(terminator)
                .addMethod(getParent)
                .addMethod(getVisitor)
                .addMethod(getName)
                .addMethod(self);

        return builder;
    }

    static private TypeSpec.Builder generateComplexCommonElementStructure(
            TypeSpec.Builder generateCommonElementStructure,
            String className,
            String elementName
            ) {
        return generateCommonElementStructure(generateCommonElementStructure, className,elementName, ElementType.COMPLEX);
    }

    static private TypeSpec.Builder generateSimpleCommonElementStructure(
            TypeSpec.Builder generateCommonElementStructure,
            String className,
            String elementName
    ) {
        return generateCommonElementStructure(generateCommonElementStructure, className,elementName, ElementType.SIMPLE);
    }



    // This function has the objective to add the attribute functions to each HTML element that requires it
    static private void addAttrFunction(
            TypeSpec.Builder builder,
            Pair<String, String> pair,
            String className,
            TypeSpec.Builder elementVisitorBuilder
    ) {
        String name = generateAttrName(pair);
        String attrName = getAttrName(name);
        String visitAttrFunctionName = getVisitAttrName(name);

        MethodSpec.Builder method = MethodSpec.methodBuilder(attrName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(ParameterizedTypeName.get(ClassName.get(CLASS_PACKAGE, className), zExtendsElement));

        String type = pair.component2();

        String getValueFunction = getValueFunctionAndAddParameter(type, method, attrName);

        if (specialTypes.contains(type)) {
            method.addStatement("$T.validateRestrictions(" + attrName + ")",ClassName.get(CLASS_PACKAGE, "AttrSizesString") );
        }

        method
                .addStatement("this.visitor." + visitAttrFunctionName + "(" + attrName + getValueFunction + ")")
                .addStatement("return this.self()");
        builder.addMethod(method.build());



        //to avoid building the same function several times in ElementVisitor, it will only be created if not present in the Set.
        // Several element can have the attrSomething() function in their definition
        // In this case we avoid building this attrSomething() function several times in ElementVisitor
        // the usage of the set is for the O(1) search
        if (!createdFunctions.contains(visitAttrFunctionName)) {
            generateAttrFunction(pair, elementVisitorBuilder, name, visitAttrFunctionName, type);
            createdFunctions.add(visitAttrFunctionName);
        }
    }

    private enum ElementType {
        SIMPLE,
        COMPLEX
    }
}
