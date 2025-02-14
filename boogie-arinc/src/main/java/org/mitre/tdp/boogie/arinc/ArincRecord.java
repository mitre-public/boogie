package org.mitre.tdp.boogie.arinc;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents the most basic semi-structured view of an ARINC record. At this point the parser has been "applied" in
 * the sense that it has been used to deconstruct the underlying ARINC record into it's component fields/substrings.
 * <br>
 * Due to the varying quality of the ARINC data sources out there this class serves to provide named access to portions of the
 * record ideally to accept/reject records with potentially malformed content in an easier way on the user side.
 * <br>
 * This class also provides the ability to access the values of fields directly by applying the {@link FieldSpec} as outlined by
 * the {@link RecordSpec} inline with querying them for explicit values.
 */
public final class ArincRecord implements Serializable {
  /**
   * An map from the {@link RecordField#fieldName()} to a pair of the associated {@link RecordField#fieldSpec()} and to it's
   * extracted substring of associated data from within the source raw ARINC record string.
   */
  private final Map<String, ArincField> namedData;

  ArincRecord(Map<String, ArincField> namedData) {
    this.namedData = namedData;
  }

  public String rawRecord() {
    return namedData.values().stream().map(ArincField::data).collect(Collectors.joining(""));
  }

  /**
   * Allows users to check if the data class contains a parse-able version of the queried ARINC field within the record.
   * <br>
   * If the queried field doesn't exist in the record this class will log at the DEBUG level the missing field and offending
   * record. This is generally useful to have baked into the record class so consumers don't have to replicate this logic in
   * multiple places.
   */
  public boolean containsParsedField(String fieldName) {
    return optionalField(fieldName).isPresent();
  }

  public <U, T extends FieldSpec<U>> Optional<T> specForField(String fieldName) {
    return Optional.ofNullable(namedData.get(fieldName)).map(p -> (T) p.spec());
  }

  /**
   * Returns the substring of content from the associated raw ARINC record which is associated with this named field in the spec.
   */
  public String rawField(String fieldName) {
    return Optional.ofNullable(namedData.get(fieldName)).map(ArincField::data).orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }

  /**
   * Returns the result of applying {@link FieldSpec#apply(Object)} for the provided field to the raw field value as extracted
   * from the underlying ARINC record.
   * <br>
   * The {@link FieldSpec}s themselves should be responsible for rejecting bad input values which they don't know how to parse
   * and should return {@link Optional#empty()} in those cases. If those contracts are kept than this method should never throw
   * any hard exceptions due to bad input record content (e.g. NumberFormatException, etc.).
   */
  public <T> Optional<T> optionalField(String fieldName) {
    Optional<ArincField> spec = Optional.ofNullable(namedData.get(fieldName));
    return spec.flatMap(field -> (Optional<T>) field.spec().apply(field.data()));
  }

  /**
   * Version of {@link #optionalField(String)} which throws a hard {@link MissingRequiredFieldException} if a valid parsed version
   * of the field cannot be extracted from the underlying raw data.
   */
  public <T> T requiredField(String fieldName) {
    Optional<T> opt = optionalField(fieldName);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }
}
