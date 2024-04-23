package org.xmlet.xsdasmfaster.classes;

import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdList;
import org.xmlet.xsdparser.xsdelements.XsdRestriction;
import org.xmlet.xsdparser.xsdelements.xsdrestrictions.XsdEnumeration;

import java.util.List;

import static org.xmlet.xsdasmfaster.classes.XsdAsmUtils.getAttributeRestrictions;

/**
 * This class is responsible to generate all the code that is attribute related.
 */
class XsdAsmAttributes {


    private XsdAsmAttributes(){}


    /**
     * Asserts if the received {@link XsdAttribute} object has restrictions. Enumeration restrictions are discarded
     * since they are already validated by the usage of {@link Enum} classses.
     * @param attribute The {@link XsdAttribute} object.
     * @return Whether the received attribute has restrictions other than {@link XsdEnumeration}.
     */
    private static boolean attributeHasRestrictions(XsdAttribute attribute){
        List<XsdRestriction> restrictions = getAttributeRestrictions(attribute);

        //noinspection SimplifiableIfStatement
        if (restrictions.isEmpty()){
            return false;
        }

        return restrictions.stream().anyMatch(XsdAsmAttributes::hasRestrictionsOtherThanEnumeration);
    }

    /**
     * Checks if any type other than Enumeration is present in the {@link XsdRestriction} object.
     * @param restriction The {@link XsdRestriction} object.
     * @return Whether the received attribute has restrictions other than {@link XsdEnumeration}.
     */
    private static boolean hasRestrictionsOtherThanEnumeration(XsdRestriction restriction){
        return restriction.getMinExclusive() != null || restriction.getMinInclusive() != null ||
                restriction.getMaxExclusive() != null || restriction.getMaxInclusive() != null ||
                restriction.getMaxLength() != null || restriction.getMinLength() != null ||
                restriction.getLength() != null || restriction.getPattern() != null ||
                restriction.getTotalDigits() != null || restriction.getFractionDigits() != null;
    }

    /**
     * Returns the {@link XsdList} of the {@link XsdAttribute} object.
     * @param attribute The {@link XsdAttribute} object.
     * @return The contained {@link XsdList} object or null if not present.
     */
    private static XsdList getAttributeList(XsdAttribute attribute) {
        return attribute.getXsdSimpleType() != null ? attribute.getXsdSimpleType().getList() : null;
    }

}
