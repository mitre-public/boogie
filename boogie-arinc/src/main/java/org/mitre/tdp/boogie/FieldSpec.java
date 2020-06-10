package org.mitre.tdp.boogie;

import java.util.Optional;

/**
 * Interface representing the specification for a logical field type from the ARINC specification.
 *
 * Fields are explicitly tied to their ARINC field code and multiple fields within a record may match the same spec.
 * e.g. latitude is stored the same within a navaid regardless of whether it's the latitude of the navaid itself or
 * its non-collocated DME.
 *
 * Note that since for a variety of fields blank values can mean things
 */
public interface FieldSpec<T> {

  /**
   * The length of the field in characters.
   */
  int fieldLength();

  /**
   * The string code name for the field in the ARINC spec.
   */
  String fieldCode();

  /**
   * Parses the value associated with the field. This method only receives input string field values after the specs
   * {@link #filterInput(String)} method has been called on them.
   */
  T parseValue(String fieldValue);

  /**
   * Returns the parsed value of the raw string associated with the field if there was a value present to be parsed.
   *
   * This method will typically throw {@link FieldSpecParseException}s when a value is present in the raw but cannot be
   * handled by the parse method. Users should look to the concrete field specification when this occurs for further
   * details about why the exception was encountered.
   */
  default Optional<T> parse(String fieldValue) {
    return Optional.of(fieldValue).filter(v -> !filterInput(v)).map(this::parseValue);
  }

  /**
   * Returns whether the input field string should be pre-filtered before being passed to the {@link #parseValue(String)}
   * method of the implementing class.
   *
   * This method is defaulted to false.
   */
  default boolean filterInput(String fieldString) {
    return false;
  }

  /**
   * The default field name for the spec field in a {@link ArincRecord}.
   *
   * The field name to use in accordance with {@link ArincField#newField(FieldSpec)} when no explicit field name is
   * otherwise provided in the {@link RecordSpec}.
   *
   * Default value for this is camelCase version of the implementing class's simple name.
   */
  default String fieldName() {
    String simpleName = this.getClass().getSimpleName();
    return simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1));
  }
}
