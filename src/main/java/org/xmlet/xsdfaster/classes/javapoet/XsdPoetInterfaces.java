package org.xmlet.xsdfaster.classes.javapoet;

import com.squareup.javapoet.*;
import org.xmlet.xsdfaster.classes.Utils.InterfaceInfo;
import org.xmlet.xsdparser.xsdelements.XsdElement;
import org.xmlet.xsdparser.xsdelements.XsdGroup;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.xmlet.xsdfaster.classes.javapoet.XsdPoetUtils.firstToUpper;

public class XsdPoetInterfaces {

    private static final String CLASS_PATH = "src/main/java/org/xmlet/htmlapifaster";

    private static final String CHOICE_SUFFIX = "Choice";

    private Map<String, InterfaceInfo> createdInterfaces = new HashMap<>();


    private String getInterfaceName(String className, int interfaceIndex, String groupName) {
        if (groupName != null)
            return firstToUpper(groupName + CHOICE_SUFFIX);
        return className + CHOICE_SUFFIX + interfaceIndex;

    }
    private boolean isInterfaceAlreadyCreated(String interfaceName) {
        return createdInterfaces.containsKey(interfaceName);
    }
    private InterfaceInfo createChoiceInterface(List<XsdGroup> groupElements, List<XsdElement> directElements, String interfaceName) {
        try {

            System.out.println("teste");
            TypeSpec.Builder interfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceName)
                    .addTypeVariable( TypeVariableName.get("T : Element<T,Z>, Z : Element<*,*>"))
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(
                            ClassName.get(CLASS_PATH, "TextGroup")));

            directElements.forEach(element -> {
                String nameStartingWithUpper = firstToUpper(element.getName());
                interfaceBuilder.addMethod(
                        MethodSpec
                                .methodBuilder(element.getName())
                                .returns(TypeVariableName.get(nameStartingWithUpper + "<T>"))
                                .addStatement("return new " + nameStartingWithUpper + "(this.self());")
                                .build()
                );
            });

            JavaFile javaFile = JavaFile.builder("", interfaceBuilder.build()).build();
            javaFile.writeTo(new File(CLASS_PATH));
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public InterfaceInfo createAndGetChoiceInterface(List<XsdGroup> groupElements, List<XsdElement> directElements, String className, int interfaceIndex, String groupName) {
        String interfaceName = getInterfaceName(className, interfaceIndex, groupName);
        if (isInterfaceAlreadyCreated(interfaceName)) {
            return createdInterfaces.get(interfaceName);
        }

        return createChoiceInterface(groupElements, directElements, interfaceName);
    }


}
