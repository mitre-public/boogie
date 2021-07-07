package org.mitre.tdp.boogie.arinc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.v18.ArincWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ReflectiveDataClassGenerator} expects to be able to use reflection to introspect a partial dataclass on the current
 * TDP classpath - the generator inspects the internal fields of the class and generates:
 * <br>
 * 1. A set of getter methods in the TDP naming convention
 * 2. A null constructor (all fields set to this.field1 = null;)
 * 3. A builder class with the following attributes: private non-final internal fields, java-syntax getters, java-syntax setters
 * 4. A constructor taking the builder class as an argument
 * 5. A .toBuilder method converting the class back into a builder
 * <br>
 * This class returns the generated "implementation" as a text string which can be printed and copied into the target data class
 * (and necessarily reformatted).
 * <br>
 * This class could be extended to do a collection of other convenience functions such as:
 * <br>
 * 1. Automagically doing the "serialize string return enum" paradigm as long as we enforce the field name is the snake_case enum name
 * 2. Automagically doing the "serialize long return instant" paradigm on "_time" fields so long as that field name convention is met
 * 3. Automagically doing the "serialize float return double" paradigm on non-"_latitude,_longitude" fields as long as that convention is met
 * <br>
 * For now the general way to use this class is to:
 * <br>
 * 1. Template your entire data class with non-final fields (so it can be compiled)
 * 2. Run this class pointed at your data class to dump the contents to stdout
 * 3. Copy the contents into the data class below the fields (and hit the format button)
 * 4. Find replace your non-final fields with final (e.g. find "public" replace "public final"
 * <br>
 * Other notable potential improvements:
 * <br>
 * 1. Biggest standout is parameterized types get the fully qualified class name in the template parameter (e.g. List{org.mitre.cda.ForeignKey})
 * instead of just ForeignKey - this could be fixed relatively generically with a .split("\\.").findFirstCapitalized().returnRest().
 */
public final class ReflectiveDataClassGenerator implements Function<Class, String> {

  private static final Logger LOG = LoggerFactory.getLogger(ReflectiveDataClassGenerator.class);

  /**
   * Entry point to run the {@link ReflectiveDataClassGenerator} on a class on the classpath.
   */
  public static void main(String[] args) {
    ReflectiveDataClassGenerator generator = new ReflectiveDataClassGenerator();
    String generated = generator.apply(ArincWaypoint.class);
    System.out.println(generated);
    System.out.println("Successfully generated string class representation - check the console.");
  }

  @Override
  public String apply(Class clz) {
    String classText = generateNullConstructor(clz).concat("\n")
        .concat(generateFromBuilderConstructor(clz).concat("\n"))
        .concat(generateAllMethods(clz)).concat("\n")
        .concat(generateToBuilder(clz)).concat("\n")
        .concat(generateBuilder(clz)).concat("\n");
    LOG.info("Generated text for class {} - content was \n{}", clz.getSimpleName(), classText);
    return classText;
  }

  /**
   * Generates a null constructor based on the fields provided in the given class definition.
   */
  String generateNullConstructor(Class clz) {
    String constructorStart = "private ".concat(clz.getSimpleName()).concat("(){\n");
    String fieldInitializations = gettableFields(clz).stream()
        .map(Field::getName)
        .map(field -> "this.".concat(field).concat(" = null;"))
        .collect(Collectors.joining("\n"));
    return constructorStart.concat(fieldInitializations).concat("\n}\n");
  }

  /**
   * Generates a constructor from the to-be-generated builder assuming some simple conventions.
   */
  String generateFromBuilderConstructor(Class clz) {
    String constructorStart = "private ".concat(clz.getSimpleName()).concat("(Builder builder){\n");
    String fieldInitializations = gettableFields(clz).stream()
        .map(field -> "this.".concat(field.getName()).concat(" = ").concat("builder.").concat(tdpFieldToJavaConvention(field.getName())).concat(";"))
        .collect(Collectors.joining("\n"));
    return constructorStart.concat(fieldInitializations).concat("\n}\n");
  }

  /**
   * Generates string representations of methods for all declared fields in the provided TDP-formatted class.
   */
  String generateAllMethods(Class clz) {
    return gettableFields(clz).stream().map(this::tdpFieldToGetter).collect(Collectors.joining("\n"));
  }

  /**
   * Generates a builder for the class definition.
   */
  String generateBuilder(Class clz) {
    String builderBase = "public static final class Builder {\n";
    String builderFields = gettableFields(clz).stream()
        .map(this::tdpFieldToJavaField)
        .collect(Collectors.joining("\n"));
    String builderSetters = gettableFields(clz).stream()
        .map(this::tdpFieldToSetter)
        .collect(Collectors.joining("\n"));
    String buildMethod = "public ".concat(clz.getSimpleName()).concat(" build(){\n")
        .concat("return new ").concat(clz.getSimpleName()).concat("(this);\n")
        .concat("}\n");
    return builderBase.concat(builderFields).concat(builderSetters).concat("\n").concat(buildMethod).concat("}\n");
  }

  /**
   * Generates a .toBuilder() method for the class making some assumptions about the format of the internal methods.
   */
  String generateToBuilder(Class clz) {
    String builderSets = gettableFields(clz).stream()
        .map(field -> {
          String javaField = tdpFieldToJavaConvention(field.getName());
          return ".".concat(javaField).concat("(").concat(javaField).concat("())");
        })
        .collect(Collectors.joining("\n"));
    return "public Builder toBuilder(){\n"
        .concat("return new Builder()\n")
        .concat(builderSets)
        .concat(";\n}\n");
  }

  /**
   * Converts a TDP field to a getter method of he appropriate standardized form.
   */
  String tdpFieldToGetter(Field field) {
    String javaName = tdpFieldToJavaConvention(field.getName());
    String fieldType = fieldTypeName(field);
    return "public ".concat(fieldType).concat(" ").concat(javaName).concat("(){\n")
        .concat("return ".concat(field.getName())).concat(";").concat("\n")
        .concat("}\n");
  }

  String tdpFieldToSetter(Field field) {
    String javaName = tdpFieldToJavaConvention(field.getName());
    String fieldType = fieldTypeName(field);
    return "public Builder ".concat(javaName).concat("(").concat(fieldType).concat(" ").concat(javaName).concat("){\n")
        .concat("this.").concat(javaName).concat(" = ").concat(javaName).concat(";\n")
        .concat("return this;\n")
        .concat("}\n");
  }

  String fieldTypeName(Field field) {
    String base = field.getType().getSimpleName();
    if (field.getGenericType() instanceof ParameterizedType) {
      ParameterizedType type = (ParameterizedType) field.getGenericType();
      String parameterTypes = Stream.of(type.getActualTypeArguments())
          .map(Type::getTypeName)
          .collect(Collectors.joining(","));
      base = base.concat("<").concat(parameterTypes).concat(">");
    }
    return base;
  }

  /**
   * Converts a provided TDP-formatted field to Java convention.
   */
  String tdpFieldToJavaField(Field field) {
    return "private ".concat(fieldTypeName(field)).concat(" ").concat(tdpFieldToJavaConvention(field.getName())).concat(";");
  }

  /**
   * Converts a TDP field in the snake-case format to standard Java camelCase convention (thank you Guava).
   */
  String tdpFieldToJavaConvention(String tdpField) {
    return tdpField;
//    return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tdpField);
  }

  /**
   * Returns the list of "gettable" fields in a TDP class.
   */
  List<Field> gettableFields(Class clz) {
    return Stream.of(clz.getDeclaredFields())
        .filter(field -> !Modifier.isTransient(field.getModifiers()))
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        .collect(Collectors.toList());
  }
}
