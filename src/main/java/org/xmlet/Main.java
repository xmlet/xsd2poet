package org.xmlet;

import org.xmlet.javaPoetGenerator.ClassGenerator;
import org.xmlet.newParser.Parser;

public class Main {

    public static void main(String[] args) {
        ClassGenerator generator = new ClassGenerator();
        Parser parser = new Parser();
        parser.parse();
        generator.generateClasses(parser);
    }
}
