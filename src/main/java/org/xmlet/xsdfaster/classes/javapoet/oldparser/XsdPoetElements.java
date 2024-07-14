package org.xmlet.xsdfaster.classes.javapoet.oldparser;

import com.squareup.javapoet.TypeSpec.Builder;
import org.xmlet.xsdparser.xsdelements.*;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.xmlet.xsdfaster.classes.javapoet.oldparser.XsdPoetUtils.getElementInterfacesElement;

public class XsdPoetElements {

    private static XsdPoetInterfaces xsdPoetInterfaces = new XsdPoetInterfaces();

    private Builder elementVisitorBuilder;

    public XsdPoetElements() {
        elementVisitorBuilder = xsdPoetInterfaces.createElementVisitorBuilder();
    }


    public void generateClassFromElement(XsdElement element) throws IOException {
        System.out.println("teste");

        createInterfaces(element);

        xsdPoetInterfaces.createElement(element, elementVisitorBuilder);
    }

    public void buildElementVisitor() {
        xsdPoetInterfaces.createClass(elementVisitorBuilder);
    }

    private static void createInterfaces(XsdElement element) {
        XsdAbstractElement child = getElementInterfacesElement(element);

        if (child != null){
            creation(child, element.getName());
        }
    }

    private static void creation(XsdAbstractElement element, String className) {
        List<XsdChoice> choiceElements = new ArrayList<>();
        List<XsdGroup> groupElements = new ArrayList<>();
        List<XsdAll> allElements = new ArrayList<>();
        List<XsdSequence> sequenceElements = new ArrayList<>();
        List<XsdElement> directElements = new ArrayList<>();

        Map<Class, List> mapper = new HashMap<>();

        mapper.put(XsdGroup.class, groupElements);
        mapper.put(XsdChoice.class, choiceElements);
        mapper.put(XsdAll.class, allElements);
        mapper.put(XsdSequence.class, sequenceElements);
        mapper.put(XsdElement.class, directElements);
//noinspection unchecked
        element.getXsdElements()
                .forEach(elementChild ->
                        mapper.get(elementChild.getClass()).add(elementChild)
                );
        if (Objects.equals(className, "dt"))
            System.out.println("print");

        if (element instanceof XsdGroup){
            XsdChoice choiceElement = choiceElements.size() == 1 ? choiceElements.get(0) : null;
            XsdAll allElement = allElements.size() == 1 ? allElements.get(0) : null;

            if (choiceElement != null) {
                xsdPoetInterfaces.createAndGetChoiceInterface(choiceElement.getXsdElements().collect(Collectors.toList()), ((XsdGroup) choiceElement.getParent()).getName());
            }

            if (allElement != null)
                xsdPoetInterfaces.createAndGetAllInterface(allElement.getXsdElements().collect(Collectors.toList()), ((XsdGroup) allElement.getParent()).getName());
            //interfaceInfoList.add(groupMethod(((XsdGroup) element).getName(), choiceElement, allElement, sequenceElement, className, interfaceIndex, apiName));
        }

        if (element instanceof XsdAll){
            xsdPoetInterfaces.createAndGetAllInterface(element.getXsdElements().collect(Collectors.toList()), className);
            //interfaceInfoList.add(allMethod(directElements, className, interfaceIndex, apiName, groupName));
        }

        if (element instanceof XsdChoice){
            xsdPoetInterfaces.createAndGetChoiceInterface(element.getXsdElements().collect(Collectors.toList()), className);
            //interfaceInfoList = choiceMethod(groupElements, directElements, className, interfaceIndex, apiName, groupName);
        }

        if (element instanceof  XsdSequence){
            System.out.println("teste");
            //sequenceMethod(element.getXsdElements(), className, interfaceIndex, apiName, groupName);
        }

        //interfaceInfoList.forEach(interfaceInfo -> createdInterfaces.put(interfaceInfo.getInterfaceName(), interfaceInfo));
    }
}
