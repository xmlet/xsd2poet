package org.xmlet.xsdfaster.classes.javapoet.newparser;

import com.squareup.javapoet.*;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.SuspendConsumer;
import org.xmlet.htmlapifaster.Text;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.ELEMENT_PACKAGE;
import static org.xmlet.xsdfaster.classes.javapoet.newparser.ClassGenerator.elementClassName;

public class InfrastructureGenerator {

    public static TypeSpec.Builder createElementVisitor() {

        deleteOldElementVisitor();

        TypeSpec.Builder builder;

        builder = TypeSpec
                .classBuilder("ElementVisitor")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build());

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitElement")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(Element.class, "element")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitAttribute")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "var1")
                        .addParameter(String.class, "var2")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitAttributeBoolean")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String.class, "var1")
                        .addParameter(String.class, "var2")
                        .build()
        );

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitParent")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(Element.class, "element")
                        .build()
        );

        TypeVariableName rType = TypeVariableName.get("R");
        ParameterizedTypeName textType = ParameterizedTypeName.get(ClassName.get(ELEMENT_PACKAGE, "Text"), WildcardTypeName.subtypeOf(elementClassName), rType);

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitText")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitRaw")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());


        builder.addMethod(
                MethodSpec
                        .methodBuilder("visitComment")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("R"))
                        .addParameter(textType, "var1")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitDynamic")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addTypeVariable(TypeVariableName.get("U"))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(BiConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("U")), "var2")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitAwait")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("M"))
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(AwaitConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("M")), "var2")
                        .build());

        builder.addMethod(
                MethodSpec.methodBuilder("visitSuspending")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addTypeVariable(TypeVariableName.get("M"))
                        .addTypeVariable(TypeVariableName.get("E", elementClassName))
                        .addParameter(TypeVariableName.get("E"), "var1")
                        .addParameter(ParameterizedTypeName.get(ClassName.get(SuspendConsumer.class),
                                TypeVariableName.get("E"), TypeVariableName.get("M")), "var2")
                        .build());

        return builder;
    }


    private static void deleteOldElementVisitor() {
        Path filePath = Paths.get("src/main/java/org/xmlet/htmlapifaster/ElementVisitor.java");

        try {
            if (Files.exists(filePath))
                Files.delete(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
