package org.xmlet.xsdfaster.classes;

import org.xmlet.xsdfaster.classes.Utils.AttributeHierarchyItem;
import org.xmlet.xsdfaster.classes.Utils.ElementHierarchyItem;
import org.xmlet.xsdfaster.classes.Utils.InterfaceInfo;
import org.xmlet.xsdparser.xsdelements.XsdAbstractElement;
import org.xmlet.xsdparser.xsdelements.XsdAll;
import org.xmlet.xsdparser.xsdelements.XsdAttribute;
import org.xmlet.xsdparser.xsdelements.XsdAttributeGroup;
import org.xmlet.xsdparser.xsdelements.XsdChoice;
import org.xmlet.xsdparser.xsdelements.XsdComplexType;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdExtension;
import org.xmlet.xsdparser.xsdelements.XsdGroup;
import org.xmlet.xsdparser.xsdelements.XsdMultipleElements;
import org.xmlet.xsdparser.xsdelements.XsdNamedElements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.xmlet.xsdfaster.classes.XsdAsmUtils.*;
import static org.xmlet.xsdfaster.classes.XsdSupportingStructure.*;

/**
 * This class is responsible to generate all the code that is related to interfaces.
 */
class XsdAsmInterfaces {

    /**
     * The value used to differentiate between two {@link XsdAttribute} object with the same name, only differing in
     * the case sensitive aspect.
     */
    private static final String ATTRIBUTE_CASE_SENSITIVE_DIFFERENCE = "Alt";

    /**
     * The suffix applied to all the interfaces that are hierarchy interfaces.
     */
    private static final String HIERARCHY_INTERFACES_SUFFIX = "HierarchyInterface";

    /**
     * The suffix applied to all the interfaces that are generated based on the {@link XsdAll} object.
     */
    private static final String ALL_SUFFIX = "All";

    /**
     * The suffix applied to all the interfaces that are generated based on the {@link XsdChoice} object.
     */
    private static final String CHOICE_SUFFIX = "Choice";


    /**
     * A {@link Map} with information regarding all the interfaces generated.
     */
    private Map<String, InterfaceInfo> createdInterfaces = new HashMap<>();

    /**
     * A {@link Map} with information regarding all the elements generated.
     */
    private Map<String, XsdAbstractElement> createdElements = new HashMap<>();

    /**
     * A {@link Map} with information regarding the hierarchy of attributeGroups.
     */
    private Map<String, AttributeHierarchyItem> attributeGroupInterfaces = new HashMap<>();

    /**
     * A {@link Map} with information regarding the hierarchy interfaces.
     */
    private Map<String, ElementHierarchyItem> hierarchyInterfaces = new HashMap<>();

    /**
     * An {@link XsdAsm} instance. Used to delegate element generation.
     */
    private XsdAsm xsdAsmInstance;


    XsdAsmInterfaces(XsdAsm instance) {
        this.xsdAsmInstance = instance;
    }


    /**
     * Obtains the names of the attribute interfaces that the given {@link XsdElement} will implement.
     * @param element The element that contains the attributes.
     * @return The elements interfaces names.
     */
    private String[] getAttributeGroupInterfaces(XsdElement element){
        List<String> attributeGroups = new ArrayList<>();
        XsdComplexType complexType = element.getXsdComplexType();
        Stream<XsdAttributeGroup> extensionAttributeGroups = Stream.empty();
        XsdExtension extension = getXsdExtension(element);

        if (complexType != null){
            if (extension != null){
                extensionAttributeGroups = extension.getXsdAttributeGroup();
            }

            attributeGroups.addAll(getTypeAttributeGroups(complexType, extensionAttributeGroups));
        }

        return listToArray(attributeGroups, CUSTOM_ATTRIBUTE_GROUP);
    }

    /**
     * Obtains hierarchy interface information from the received {@link XsdElement}.
     * @param element The received {@link XsdElement} object.
     * @param apiName The name of the generated fluent interface.
     * @return The names of the hierarchy interfaces.
     */
    private String[] getHierarchyInterfaces(XsdElement element, String apiName) {
        List<String> interfaceNames = new ArrayList<>();
        XsdElement base = getBaseFromElement(element);
        List<XsdAttribute> elementAttributes = getOwnAttributes(element).collect(Collectors.toList());
        List<ElementHierarchyItem> hierarchyInterfacesList = new ArrayList<>();

        while (base != null) {
            List<String> attributeNames = elementAttributes.stream().map(XsdAttribute::getName).collect(Collectors.toList());
            List<XsdAttribute> moreAttributes = getOwnAttributes(base).filter(attribute -> !attributeNames.contains(attribute.getName())).collect(Collectors.toList());
            elementAttributes.addAll(moreAttributes);

            // hierarchyInterfacesList.add(new ElementHierarchyItem(base.getName() + HIERARCHY_INTERFACES_SUFFIX, moreAttributes, getInterfaces(base, apiName)));

            base = getBaseFromElement(base);
        }

        if (!hierarchyInterfacesList.isEmpty()){
            interfaceNames.add(hierarchyInterfacesList.get(0).getInterfaceName());

            hierarchyInterfacesList.forEach(item -> this.hierarchyInterfaces.put(item.getInterfaceName(), item));
        }

        return listToArray(interfaceNames);
    }

    /**
     * Obtains the attribute groups of a given element that are present in its type attribute.
     * @param complexType The {@link XsdComplexType} object with the type attribute.
     * @param extensionAttributeGroups A {@link Stream} of {@link XsdAttributeGroup} obtained from {@link XsdExtension}.
     * @return The names of the attribute groups present in the type attribute.
     */
    private Collection<String> getTypeAttributeGroups(XsdComplexType complexType, Stream<XsdAttributeGroup> extensionAttributeGroups) {
        Stream<XsdAttributeGroup> attributeGroups = complexType.getXsdAttributes()
                .filter(attribute -> attribute.getParent() instanceof XsdAttributeGroup)
                .map(attribute -> (XsdAttributeGroup) attribute.getParent())
                .distinct();

        attributeGroups = Stream.concat(attributeGroups, extensionAttributeGroups);

        attributeGroups = Stream.concat(attributeGroups, complexType.getXsdAttributeGroup());

        List<XsdAttributeGroup> attributeGroupList = attributeGroups.distinct().collect(Collectors.toList());

        attributeGroupList.forEach(this::addAttributeGroup);

        if (!attributeGroupList.isEmpty()){
            return getBaseAttributeGroupInterface(complexType.getXsdAttributeGroup().collect(Collectors.toList()));
        }

        return Collections.emptyList();
    }

    /**
     * Recursively iterates order to define an hierarchy on the attribute group interfaces.
     * @param attributeGroups The attributeGroups contained in the element.
     * @return A {@link List} of attribute group interface names.
     */
    private List<String> getBaseAttributeGroupInterface(List<XsdAttributeGroup> attributeGroups){
        List<XsdAttributeGroup> parents = new ArrayList<>();

        attributeGroups.forEach(attributeGroup -> {
            XsdAbstractElement parent = attributeGroup.getParent();

            if (parent instanceof XsdAttributeGroup && !parents.contains(parent)){
                parents.add((XsdAttributeGroup) parent);
            }
        });

        if (attributeGroups.size() == 1 || parents.isEmpty()){
            return attributeGroups.stream().map(attributeGroup -> firstToUpper(attributeGroup.getName())).collect(Collectors.toList());
        }

        return getBaseAttributeGroupInterface(parents);
    }

    /**
     * Adds information about the attribute group interface to the attributeGroupInterfaces variable.
     * @param attributeGroup The attributeGroup to add.
     */
    private void addAttributeGroup(XsdAttributeGroup attributeGroup) {
        String interfaceName = firstToUpper(attributeGroup.getName());

        if (!attributeGroupInterfaces.containsKey(interfaceName)){
            List<XsdAttribute> ownElements = attributeGroup.getXsdElements()
                    .filter(attribute -> attribute.getParent().equals(attributeGroup))
                    .map(attribute -> (XsdAttribute) attribute)
                    .collect(Collectors.toList());

            List<String> parentNames = attributeGroup.getAttributeGroups().stream().map(XsdNamedElements::getName).collect(Collectors.toList());
            AttributeHierarchyItem attributeHierarchyItemItem = new AttributeHierarchyItem(parentNames, ownElements);

            attributeGroupInterfaces.put(interfaceName, attributeHierarchyItemItem);

            attributeGroup.getAttributeGroups().forEach(this::addAttributeGroup);
        }
    }

    /** Obtains the signature for the attribute group interfaces based on the implemented interfaces.
     * @param interfaces The implemented interfaces.
     * @return The signature of this interface.
     */
    private StringBuilder getAttributeGroupSignature(String[] interfaces, String apiName) {
        StringBuilder signature = new StringBuilder("<T::L" + elementType + "<TT;TZ;>;Z::" + elementTypeDesc + ">" + JAVA_OBJECT_DESC);

        if (interfaces.length == 0){
            signature.append("L").append(elementType).append("<TT;TZ;>;");
        } else {
            for (String anInterface : interfaces) {
                signature.append("L").append(getFullClassTypeName(anInterface, apiName)).append("<TT;TZ;>;");
            }
        }

        return signature;
    }

    /**
     * Obtains an array with the names of the interfaces implemented by a attribute group interface
     * with the given parents, as in interfaces that will be extended.
     * @param parentsName The list of interfaces that this interface will extend.
     * @return A {@link String} array containing the names of the interfaces that this interface will extend.
     */
    private String[] getAttributeGroupObjectInterfaces(List<String> parentsName) {
        return listToArray(parentsName.stream().map(XsdAsmUtils::firstToUpper).collect(Collectors.toList()), CUSTOM_ATTRIBUTE_GROUP);
    }

    /**
     * Creates a class based on a {@link XsdElement} if it wasn't been already.
     * @param element The element that serves as base to creating the class.
     * @param apiName The name of the generated fluent interface.
     */
    private void createElement(XsdElement element, String apiName) {
        String elementName = element.getName();

        if (!createdElements.containsKey(getCleanName(elementName))){
            createdElements.put(getCleanName(elementName), element);
            xsdAsmInstance.generateClassFromElement(element);
        }
    }

    /**
     * Creates a class based on a {@link XsdElement} if it wasn't been already.
     * @param elementName The name of the element.
     */
    private void addToCreateElements(String elementName) {
        HashMap<String, String> elementAttributes = new HashMap<>();

        elementAttributes.put(XsdAbstractElement.NAME_TAG, elementName);

        if (!createdElements.containsKey(getCleanName(elementName))){
            createdElements.put(getCleanName(elementName), new XsdElement(null, elementAttributes));
        }
    }

    /**
     * Iterates in a given {@link XsdAbstractElement} object in order to obtain all the contained {@link XsdElement} objects.
     * @param element The element to iterate on.
     * @return All the {@link XsdElement} objects contained in the received element.
     */
    private List<XsdElement> getAllElementsRecursively(XsdAbstractElement element) {
        List<XsdElement> allGroupElements = new ArrayList<>();
        List<XsdAbstractElement> directElements = element.getXsdElements().collect(Collectors.toList());

        allGroupElements.addAll(
                directElements.stream()
                        .filter(elem1 -> elem1 instanceof XsdElement)
                        .map(elem1 -> (XsdElement) elem1)
                        .collect(Collectors.toList()));

        for (XsdAbstractElement elem : directElements) {
            if ((elem instanceof XsdMultipleElements || elem instanceof XsdGroup) && elem.getXsdElements() != null) {
                allGroupElements.addAll(getAllElementsRecursively(elem));
            }
        }

        return allGroupElements;
    }

    /**
     * Adds the received {@link XsdElement} to the list of created elements.
     * @param element The received {@link XsdElement}.
     */
    private void addCreatedElement(XsdElement element){
        createdElements.put(getCleanName(element.getName()), element);
    }

    /**
     * Adds a {@link List} of {@link XsdElement} to the list of created elements.
     * @param elementList The {@link List} to add.
     */
    void addCreatedElements(List<XsdElement> elementList) {
        elementList.forEach(this::addCreatedElement);
    }

    /**
     * @return Obtains all the names of the elements that were created in the current execution.
     */
    Set<String> getExtraElementsForVisitor() {
        return createdElements.keySet();
    }

}
