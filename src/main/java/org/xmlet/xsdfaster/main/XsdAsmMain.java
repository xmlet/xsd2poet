package org.xmlet.xsdfaster.main;

import org.xmlet.xsdparser.core.XsdParser;
import org.xmlet.xsdparser.xsdelements.XsdElement;

import java.util.List;
import java.util.stream.Collectors;

public class XsdAsmMain {

    public static void main(String[] args){
//        if (args.length == 2){
//            new XsdAsm().generateClassFromElements(new XsdParser(args[0]).getResultXsdElements(), args[1]);
//        }
        String xsdPath = "./src/main/resources/html_5_2.xsd";
        //String xsdPath = "./src/main/resources/teste.xsd";
        String out = "htmlapifaster";
        List<XsdElement> xsdElements = new XsdParser(xsdPath).getResultXsdElements().collect(Collectors.toList());
        //new XsdAsm().generateClassFromElements(xsdElements.stream(), out);
    }
}
