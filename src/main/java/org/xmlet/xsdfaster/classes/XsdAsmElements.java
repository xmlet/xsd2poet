package org.xmlet.xsdfaster.classes;

import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdElement;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible to generate all the code that is element related.
 */
class XsdAsmElements {

    private XsdAsmElements(){}

    /**
     * Generates a class based on the information present in a {@link XsdElement} object. It also generated its
     * constructors and required methods.
     * @param interfaceGenerator An instance of {@link XsdAsmInterfaces} which contains interface information.
     * @param createdAttributes Information regarding attribute classes that were already created.
     * @param element The {@link XsdElement} object from which the class will be generated.
     * @param apiName The name of the generated fluent interface.
     */
    static void generateClassFromElement(XsdAsmInterfaces interfaceGenerator, Map<String, List<XsdAttribute>> createdAttributes, XsdElement element, String apiName) {
    }
}
