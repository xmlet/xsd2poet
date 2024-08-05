package org.xmlet.xsdfaster.classes.javapoet.oldparser;

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

    public static String firstToUpper(String name){
        if (name.length() == 1){
            return name.toUpperCase();
        }

        String firstLetter = name.substring(0, 1).toUpperCase();
        return firstLetter + name.substring(1);
    }

    public static String firstToLower(String name){
        if (name.length() == 1){
            return name.toLowerCase();
        }

        String firstLetter = name.substring(0, 1).toLowerCase();
        return firstLetter + name.substring(1);
    }
}
