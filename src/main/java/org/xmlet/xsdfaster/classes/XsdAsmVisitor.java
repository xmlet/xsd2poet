package org.xmlet.xsdfaster.classes;

import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdNamedElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class has the responsibility of creating the ElementVisitor class.
 */
class XsdAsmVisitor {

    private static final String VISIT_ELEMENT_NAME = "visitElement";
    private static final String VISIT_PARENT_NAME = "visitParent";
    private static final String VISIT_ATTRIBUTE_NAME = "visitAttribute";

    private XsdAsmVisitor(){}

    /**
     * Generates both the abstract visitor class with methods for each element from the list.
     * @param elementNames The elements names list.
     * @param apiName The name of the generated fluent interface.
     */
    static void generateVisitors(Set<String> elementNames, List<XsdAttribute> attributes, String apiName){
    }


    /**
     * Removes duplicate attribute names.
     * @param attributes The {@link List} of {@link XsdAttribute} objects.
     * @return The distinct {@link List} of {@link XsdAttribute}.
     */
    private static List<XsdAttribute> filterAttributes(List<XsdAttribute> attributes) {
        List<String> attributeNames = attributes.stream()
                .map(XsdNamedElements::getName)
                .distinct()
                .collect(Collectors.toList());

        List<XsdAttribute> filteredAttributes = new ArrayList<>();

        attributeNames.forEach(attributeName -> {
            for (XsdAttribute attribute : attributes) {
                if (attribute.getName().equals(attributeName)){
                    filteredAttributes.add(attribute);
                    break;
                }
            }
        });

        return filteredAttributes;
    }
}
