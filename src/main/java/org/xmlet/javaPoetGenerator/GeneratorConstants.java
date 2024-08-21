package org.xmlet.javaPoetGenerator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeVariableName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GeneratorConstants {
    public static final Map<String,Class<?>> primitiveAndStringTypes;

    static {
        primitiveAndStringTypes = new HashMap<>();
        primitiveAndStringTypes.put("xsd:boolean", Boolean.class);
        primitiveAndStringTypes.put("xsd:byte", Byte.class);
        primitiveAndStringTypes.put("xsd:char", Character.class);
        primitiveAndStringTypes.put("xsd:short", Short.class);
        primitiveAndStringTypes.put("xsd:int", Integer.class);
        primitiveAndStringTypes.put("xsd:positiveInteger", Integer.class);
        primitiveAndStringTypes.put("xsd:long", Long.class);
        primitiveAndStringTypes.put("xsd:float", Float.class);
        primitiveAndStringTypes.put("xsd:double", Double.class);
        primitiveAndStringTypes.put("xsd:string", String.class);
        primitiveAndStringTypes.put("xsd:anyURI", String.class);
        primitiveAndStringTypes.put("sizesType", String.class);
    }

    static final Set<String> invalidStrings = Set.of("class", "default", "for");

    public static final Set<String> specialTypes = Set.of("sizesType");

    public static final String JAVA_ROOT_PATH = "./src/main/java";

    public static final String KOTLIN_ROOT_PATH = "./src/main/kotlin";

    public static final String CLASS_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String ENUM_INTERFACE_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static final String ELEMENT_PACKAGE = "org.xmlet.htmlapifaster";

    public static final String ASYNC_PACKAGE = "org.xmlet.htmlapifaster.async";

    public static final String RESTRICTION_VALIDATOR_PACKAGE = "org.xmlet.xsdasmfaster.classes.infrastructure";

    public static final ClassName elementClassName = ClassName.get(ELEMENT_PACKAGE, "Element");

    public static final ClassName elementVisitorClassName = ClassName.get(ELEMENT_PACKAGE, "ElementVisitor");

    public static final ClassName textClassName = ClassName.get(ELEMENT_PACKAGE, "Text");

    public static final ClassName awaitConsumerClassName = ClassName.get(ASYNC_PACKAGE, "AwaitConsumer");

    public static final ClassName suspendConsumerClassName = ClassName.get(ELEMENT_PACKAGE, "SuspendConsumer");

    public static final ClassName customElementClassName = ClassName.get(ELEMENT_PACKAGE, "CustomElement");

    public static final ClassName enumInterfaceClassName = ClassName.get(ENUM_INTERFACE_PACKAGE, "EnumInterface");

    public static final ClassName restrictionValidatorClassName = ClassName.get(RESTRICTION_VALIDATOR_PACKAGE, "RestrictionValidator");

    public static final ClassName asyncElementClassName = ClassName.get(ASYNC_PACKAGE, "AsyncElement");

    public static final ClassName elementExtensionsClassName = ClassName.get(CLASS_PACKAGE, "ElementExtensions");

    public static final ClassName biConsumerClassName = ClassName.get(BiConsumer.class);

    public static final ClassName consumerClassName = ClassName.get(Consumer.class);

    public static final ClassName divChoiceClassName = ClassName.get(ELEMENT_PACKAGE, "DivChoice");

    public static final ClassName onCompletitionClassName = ClassName.get(ASYNC_PACKAGE, "OnCompletion");

    public static final ClassName globalAttributesClassName = ClassName.get(ELEMENT_PACKAGE, "GlobalAttributes");

    public static final TypeVariableName t = TypeVariableName.get("T");

    public static final TypeVariableName z = TypeVariableName.get("Z");

    public static final TypeVariableName r = TypeVariableName.get("R");

    public static final TypeVariableName m = TypeVariableName.get("M");

    public static final TypeVariableName u = TypeVariableName.get("U");

    public static final TypeVariableName e = TypeVariableName.get("E");

    static final TypeVariableName ELEMENT_T_Z =
            TypeVariableName.get("T", ParameterizedTypeName.get(elementClassName, t, z));

    public static final TypeVariableName zExtendsElement = TypeVariableName.get("Z", elementClassName);

    public static final TypeVariableName tExtendsElement = TypeVariableName.get("T", elementClassName);

    public static final ParameterizedTypeName customElementZExtendsElement =
            ParameterizedTypeName.get(
                    customElementClassName,
                    zExtendsElement
            );

    public static final ParameterizedTypeName elementTExtendsElementZExtendsElement =
            ParameterizedTypeName.get(
                    elementClassName,
                    tExtendsElement,
                    zExtendsElement
            );
}
