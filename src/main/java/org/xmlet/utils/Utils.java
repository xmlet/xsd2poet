package org.xmlet.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import static org.xmlet.javaPoetGenerator.GeneratorConstants.CLASS_PACKAGE;
import static org.xmlet.javaPoetGenerator.GeneratorConstants.primitiveAndStringTypes;

public class Utils {
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

    @NotNull
    public static String generateAttrName(Pair<String, String> pair) {
        String[] strs = pair.component1().split("-");
        StringBuilder sb = new StringBuilder();
        for (String str : strs)
            sb.append(firstToUpper(str));

        return sb.toString();
    }

    public static  String getAttrName(String name) { return "attr" + name;}

    public static String getVisitAttrName(String name) { return "visitAttribute" + name;}

    @NotNull
    public static String getValueFunctionAndAddParameter(String type, MethodSpec.Builder method, String attrName) {
        String getValueFunction;
        if (primitiveAndStringTypes.containsKey(type)) {
            method.addParameter(primitiveAndStringTypes.get(type), attrName);
            if (primitiveAndStringTypes.get(type) == String.class)
                getValueFunction = "";
            else
                getValueFunction = ".toString()";
        } else {
            method.addParameter(ClassName.get(CLASS_PACKAGE, "Enum" + firstToUpper(type)), attrName);
            getValueFunction = ".getValue()";
        }
        return getValueFunction;
    }
}
