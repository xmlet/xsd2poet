package org.xmlet.xsdfaster.classes.javapoet;

import org.xmlet.xsdparser.xsdelements.*;

public class XsdPoetUtils {

    static XsdAbstractElement getElementInterfacesElement(XsdElement element){
        XsdAbstractElement child = null;

        if (element != null){
            XsdComplexType complexType = element.getXsdComplexType();

            if (complexType != null){
                child = complexType.getXsdChildElement();

                if (child == null){
                    XsdComplexContent complexContent = complexType.getComplexContent();

                    if (complexContent != null){
                        XsdExtension extension = complexContent.getXsdExtension();

                        if (extension != null){
                            child = extension.getXsdChildElement();
                        }
                    }
                }
            }
        }

        return child;
    }

    static String firstToUpper(String name){
        if (name.length() == 1){
            return name.toUpperCase();
        }

        String firstLetter = name.substring(0, 1).toUpperCase();
        return firstLetter + name.substring(1);
    }
}
