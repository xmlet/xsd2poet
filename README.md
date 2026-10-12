# xsd2poet

xsd2poet is a library designed to generate fluent Java and idiomatic Kotlin DSLs
for HTML, based on an HTML specification defined in an XSD file. It leverages
JavaPoet and KotlinPoet for code generation.

## Project Structure

**Since version 1.0.8, xsd2poet has been split into three Maven modules:**

- **xsd2poet-core** – Common infrastructure related to XSD parsing, which is not
  exposed in the public API of the exported modules.
- **xsd2poet-java** – Generates a fluent Java DSL from an XSD file.
- **xsd2poet-kotlin** – Dedicated to generating an idiomatic Kotlin extensions
  API.

### Important: Local Installation Required

**The `xsd2poet-core` module is not published to [Maven
Central](https://repo1.maven.org/maven2/com/github/xmlet/). Consequently,
building `xsd2poet-java` or `xsd2poet-kotlin` requires installing
`xsd2poet-core` into your local Maven repository first.**

From the `xsd2poet-core` module directory, run:

```bash
mvn clean install
```

Once `xsd2poet-core` has been installed locally, the remaining modules can be
built using Maven.