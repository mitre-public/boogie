package org.mitre.tdp.boogie;


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
   * Type unsafe method for getting the target parsed field from the spec.
   */
  default <T> T getParsedField(String fieldName) {
    String rawField = getRawField(fieldName);
    ArincField<T> field = fieldForName(fieldName);
    return field.fieldSpec().parse(rawField);
  }

  /**
   * Type safe method for getting the target parsed field from the spec.
   */
  <T> T getParsedField(ArincField<T> spec);

  /**
   * Returns the given record with the contents of the {@link #rawRecord()} parsed against the given spec.
   */
  ArincRecord parseWithSpec(RecordSpec spec);
}
