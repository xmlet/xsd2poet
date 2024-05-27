package org.xmlet.xsdfaster.classes;

import org.xmlet.xsdfaster.classes.javapoet.XsdPoetElements;
import org.xmlet.xsdparser.xsdelements.XsdAbstractElement;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XsdAsm {

    /**
     * An instance of {@link XsdAsmInterfaces}. It is used to multiple types of interfaces.
     */
    private XsdAsmInterfaces interfaceGenerator = new XsdAsmInterfaces(this);


    private XsdPoetElements xsdPoetElements = new XsdPoetElements();
    /**
     * A {@link Map} object with information about all the attributes that were used in the element generated classes.
     */
    private Map<String, List<XsdAttribute>> createdAttributes = new HashMap<>();

    /**
     * This method is the entry point for the class creation process.
     * It receives all the {@link XsdAbstractElement} objects and creates the necessary infrastructure for the
     * generated fluent interface, the required interfaces, visitor and all the classes based on the elements received.
     * @param elements The elements which will serve as base to the generated classes.
     * @param apiName The resulting fluent interface name.
     */
    public void generateClassFromElements(Stream<XsdElement> elements, String apiName){

        XsdSupportingStructure.createSupportingInfrastructure(apiName);

        List<XsdElement> elementList = elements.collect(Collectors.toList());

        elementList.forEach(this::generateClassFromElement);

        xsdPoetElements.buildElementVisitor();

        // interfaceGenerator.generateInterfaces(createdAttributes, apiName);

        List<XsdAttribute> attributes = new ArrayList<>();

        createdAttributes.keySet().forEach(attribute -> attributes.addAll(createdAttributes.get(attribute)));


        XsdAsmVisitor.generateVisitors(interfaceGenerator.getExtraElementsForVisitor(), attributes, apiName);
    }

    /**
     * Generates an element class based on the received {@link XsdElement} object.
     * @param element The {@link XsdElement} containing information needed for the class creation.
     */
    void generateClassFromElement(XsdElement element){

        try {
            xsdPoetElements.generateClassFromElement(element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
