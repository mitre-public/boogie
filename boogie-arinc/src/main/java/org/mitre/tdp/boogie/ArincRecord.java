package org.mitre.tdp.boogie;


import java.util.Optional;

/**
 * An {@link ArincRecord} is an extension of a record spec which contains extracted field information
 * from a raw arinc record served via a map-like interface.
 */
public interface ArincRecord {

  /**
   * The raw record string.
   */
  String rawRecord();

  /**
   * The {@link RecordSpec} used to parse this particular {@link ArincRecord} or null if it hasn't been
   * parsed yet - in which case users may call {@link #parseWithSpec(RecordSpec)}.
   */
  RecordSpec parseSpec();

  /**
   * Returns whether the parsing process has been run on the given {@link ArincRecord} and consumers can
   * expect the getter methods to return appropriate fields, etc.
   */
  boolean isParsed();

  /**
   * Adds the given fieldName, fieldValue pair to the ARINC record.
   */
  void put(ArincField<?> field, String value);

  /**
   * Returns the field for the given string name.
   */
  <T, A extends ArincField<T>> A fieldForName(String fieldName);

  /**
   * Returns the associated spec and raw field from the record which match the provided fieldName.
   */
  String getRawField(String fieldName);

  /**
   * Type safe method for getting the target parsed field from the spec.
   */
  <T> Optional<T> getOptionalField(ArincField<T> spec);

  default <T> T getRequiredField(ArincField<T> field) {
    Optional<T> opt = getOptionalField(field);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(field.fieldName()));
  }

  /**
   * Type unsafe method for getting the target parsed field from the spec.
   */
  default <T> Optional<T> getOptionalField(String fieldName) {
    ArincField<T> field = fieldForName(fieldName);
    return getOptionalField(field);
  }

  default <T> T getRequiredField(String fieldName) {
    Optional<T> opt = getOptionalField(fieldName);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }

  /**
   * Returns the given record with the contents of the {@link #rawRecord()} parsed against the given spec.
   */
  ArincRecord parseWithSpec(RecordSpec spec);
}
