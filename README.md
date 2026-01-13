# xsd2poet

xsd2poet is a library designed to generate fluent Java and 
idiomatic Kotlin DSLs for HTML, based on an HTML specification
defined in an XSD file. It leverages JavaPoet and KotlinPoet
for code generation.

This Maven multi-module project is organized as follows:
* **xsd2poet-core** – Common infrastructure related to XSD parsing, which is not
  exposed in the public API of the exported modules.
* **xsd2poet-java** – Generates a fluent Java DSL from an XSD file.
* **xsd2poet-kotlin** – Dedicated to generating an idiomatic Kotlin extensions
  API.
