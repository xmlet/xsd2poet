package org.xmlet.xsdfaster.classes;

import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;

import java.util.List;

/**
 * This class is responsible to generate all the code that is {@link Enum} related.
 */
class XsdAsmEnum {

    private XsdAsmEnum(){}

    /**
     * Creates an {@link Enum} class based on the information received.
     * @param attribute The {@link XsdAttribute} that has the restrictions that are used to create the {@link Enum} class.
     * @param enumerations The {@link List} of {@link XsdEnumeration} that contains all the values that will be used in
     *                     the generated {@link Enum} class.
     * @param apiName The name of the generated fluent interface.
     */
    static void createEnum(XsdAttribute attribute, List<XsdEnumeration> enumerations, String apiName){
    }
}
