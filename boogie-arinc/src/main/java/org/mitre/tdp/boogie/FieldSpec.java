package org.mitre.tdp.boogie;

/**
 * Interface representing the specification for a logical field type from the ARINC specification.
 *
 * Fields are explicitly tied to their ARINC field code and multiple fields within a record may match the same spec.
 * e.g. latitude is stored the same within a navaid regardless of whether it's the latitude of the navaid itself or
 * its non-collocated DME.
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
   * Returns the parsed value of the raw string associated with the field.
   */
  T parse(String fieldString);
}
