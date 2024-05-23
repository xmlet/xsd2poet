package org.xmlet.xsdfaster.classes;


import com.squareup.javapoet.*;
import org.xmlet.xsdfaster.classes.infrastructure.EnumInterface;
import org.xmlet.xsdfaster.classes.infrastructure.RestrictionValidator;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for creating some infrastructure classes that can't be reused.
 */
class XsdSupportingStructure {

    static final String JAVA_OBJECT = "java/lang/Object";
    static final String JAVA_OBJECT_DESC = "Ljava/lang/Object;";
    static final String JAVA_STRING_DESC = "Ljava/lang/String;";
    static final String JAVA_LIST = "java/util/List";
    static final String JAVA_LIST_DESC = "Ljava/util/List;";
    static final String CONSTRUCTOR = "<init>";
    static final String STATIC_CONSTRUCTOR = "<clinit>";
    static final String ELEMENT = "Element";
    static final String CUSTOM_ELEMENT = "CustomElement";
    static final String CUSTOM_ATTRIBUTE_GROUP = "CustomAttributeGroup";
    static final String TEXT_GROUP = "TextGroup";
    private static final String RESTRICTION_VIOLATION_EXCEPTION = "RestrictionViolationException";
    private static final String RESTRICTION_VALIDATOR = "RestrictionValidator";
    static final String ELEMENT_VISITOR = "ElementVisitor";
    static final String ENUM_INTERFACE = "EnumInterface";
    static final String ATTRIBUTE_PREFIX = "Attr";
    private static final String TEXT = "Text";

    /**
     * Type name for the Element interface.
     */
    static String elementType;

    /**
     * Type descriptor for the Element interface.
     */
    static String elementTypeDesc;

    /**
     * Type name for the CustomElement element.
     */
    static String customElementType;

    /**
     * Type descriptor for the CustomElement element.
     */
    static String customElementTypeDesc;

    /**
     * Type name for the {@link RestrictionValidator} class present in the infrastructure package of this solution.
     */
    static String restrictionValidatorType = "org/xmlet/xsdfaster/classes/infrastructure/RestrictionValidator";

    /**
     * Type name for the ElementVisitor abstract class.
     */
    static String elementVisitorType;

    /**
     * Type descriptor for the ElementVisitor abstract class.
     */
    static String elementVisitorTypeDesc;

    /**
     * Type name for the {@link EnumInterface} interface present in the infrastructure package of this solution.
     */
    static String enumInterfaceType = "org/xmlet/xsdfaster/classes/infrastructure/EnumInterface";

    /**
     * Type name for the CustomAttributeGroup interface.
     */
    private static String customAttributeGroupType;

    /**
     * Type name for the TextGroup interface.
     */
    private static String textGroupType;

    /**
     * Type name for the Text class.
     */
    static String textType;

    /**
     * Type descriptor for the Text class.
     */
    static String textTypeDesc;

    /**
     * A {@link Map} object with information regarding types that are present in the infrastructure package of this
     * solution. Their name resolution differs from all the remaining classes since their name is already defined.
     */
    static Map<String, String> infrastructureVars;

    static {
        infrastructureVars = new HashMap<>();

        infrastructureVars.put(RESTRICTION_VALIDATOR, restrictionValidatorType);
        infrastructureVars.put(RESTRICTION_VIOLATION_EXCEPTION, "org/xmlet/xsdfaster/classes/infrastructure/RestrictionViolationException");
        infrastructureVars.put(ENUM_INTERFACE, enumInterfaceType);
    }

    private XsdSupportingStructure(){}

    /**
     * Creates all the classes that belong to the fluent interface infrastructure and can't be defined the same way
     * as the classes present in the infrastructure package of this solution.
     * @param apiName The name of the generated fluent interface.
     */
    static void createSupportingInfrastructure(String apiName){
        elementType = XsdAsmUtils.getFullClassTypeName(ELEMENT, apiName);
        elementTypeDesc = XsdAsmUtils.getFullClassTypeNameDesc(ELEMENT, apiName);
        customElementType = XsdAsmUtils.getFullClassTypeName(CUSTOM_ELEMENT, apiName);
        customElementTypeDesc = XsdAsmUtils.getFullClassTypeNameDesc(CUSTOM_ELEMENT, apiName);
        elementVisitorType = XsdAsmUtils.getFullClassTypeName(ELEMENT_VISITOR, apiName);
        elementVisitorTypeDesc = XsdAsmUtils.getFullClassTypeNameDesc(ELEMENT_VISITOR, apiName);
        textGroupType = XsdAsmUtils.getFullClassTypeName(TEXT_GROUP, apiName);
        customAttributeGroupType = XsdAsmUtils.getFullClassTypeName(CUSTOM_ATTRIBUTE_GROUP, apiName);
        textType = XsdAsmUtils.getFullClassTypeName(TEXT, apiName);
        textTypeDesc = XsdAsmUtils.getFullClassTypeNameDesc(TEXT, apiName);



        /*
        createElement(apiName);
        if (apiName.equals("htmlapifaster")){
            createCustomElement(apiName);
        }
        createTextGroup(apiName);
        createCustomAttributeGroup(apiName);
        createText(apiName);

         */


        infrastructureVars.put(ELEMENT, elementType);
        infrastructureVars.put(ELEMENT_VISITOR, elementVisitorType);
        infrastructureVars.put(TEXT_GROUP, textGroupType);
    }

}
