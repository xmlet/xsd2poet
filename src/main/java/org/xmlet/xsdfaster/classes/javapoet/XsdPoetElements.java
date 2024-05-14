package org.xmlet.xsdfaster.classes.javapoet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.xmlet.xsdfaster.classes.Utils.InterfaceInfo;
import org.xmlet.xsdparser.xsdelements.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.firstToUpper;
import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.getElementInterfacesElement;

public class XsdPoetElements {

    private XsdPoetElements() {}


    public static void generateClassFromElement(Map<String, List<XsdAttribute>> createdAttributes, XsdElement element, String apiName) throws IOException {
        System.out.println("teste");
        TypeSpec myType = null;

        createInterfaces(element);


        JavaFile javaFile = JavaFile.builder("", myType).build();
        javaFile.writeTo(new File("src/main/java/org/xmlet/htmlapifaster"));
    }

    private static void createInterfaces(XsdElement element) {
        XsdAbstractElement child = getElementInterfacesElement(element);

        if (child != null){
            List<InterfaceInfo> interfaceInfo = creation(child, element.getName(), 0);
        }

    }

    private static List<InterfaceInfo> creation(XsdAbstractElement element, String className, int interfaceIndex) {
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

        List<InterfaceInfo> interfaceInfoList = new ArrayList<>();

        if (element instanceof XsdGroup){
            XsdChoice choiceElement = choiceElements.size() == 1 ? choiceElements.get(0) : null;
            XsdSequence sequenceElement = sequenceElements.size() == 1 ? sequenceElements.get(0) : null;
            XsdAll allElement = allElements.size() == 1 ? allElements.get(0) : null;

            //interfaceInfoList.add(groupMethod(((XsdGroup) element).getName(), choiceElement, allElement, sequenceElement, className, interfaceIndex, apiName));
        }

        if (element instanceof XsdAll){
            //interfaceInfoList.add(allMethod(directElements, className, interfaceIndex, apiName, groupName));
        }

        if (element instanceof XsdChoice){
            new XsdPoetInterfaces().createAndGetChoiceInterface(groupElements, directElements, className, interfaceIndex, null);
            //interfaceInfoList = choiceMethod(groupElements, directElements, className, interfaceIndex, apiName, groupName);
        }

        if (element instanceof  XsdSequence){
            //sequenceMethod(element.getXsdElements(), className, interfaceIndex, apiName, groupName);
        }

        //interfaceInfoList.forEach(interfaceInfo -> createdInterfaces.put(interfaceInfo.getInterfaceName(), interfaceInfo));

        return interfaceInfoList;
    }
}
