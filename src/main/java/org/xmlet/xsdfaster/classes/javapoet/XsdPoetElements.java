package org.xmlet.xsdfaster.classes.javapoet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.xmlet.xsdfaster.classes.Utils.InterfaceInfo;
import org.xmlet.xsdparser.xsdelements.*;
import org.xmlet.xsdparser.xsdelements.elementswrapper.ReferenceBase;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.firstToUpper;
import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.getElementInterfacesElement;

public class XsdPoetElements {

    private static XsdPoetInterfaces xsdPoetInterfaces = new XsdPoetInterfaces();

    private XsdPoetElements() {}


    public static void generateClassFromElement(Map<String, List<XsdAttribute>> createdAttributes, XsdElement element, String apiName) throws IOException {
        System.out.println("teste");

        createInterfaces(element);
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

        List<InterfaceInfo> interfaceInfoList = new ArrayList<>();

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
