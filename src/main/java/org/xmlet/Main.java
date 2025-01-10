package org.xmlet;

import org.xmlet.javaPoetGenerator.ClassGenerator;
import org.xmlet.newParser.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.parse("src/main/resources/html_5_2.xsd");
        ClassGenerator.generateClasses(parser);
    }
}
