package org.mitre.tdp.boogie.dafif;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.dafif.utils.MissingRequiredFieldException;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents the most basic semi-structured view of an DAFIF record. At this point the parser has been "applied" in
 * the sense that it has been used to deconstruct the underlying DAFIF record into it's component fields/substrings.
 * <br>
 * Due to the varying quality of the DAFIF data sources out there this class serves to provide named access to portions of the
 * record ideally to accept/reject records with potentially malformed content in an easier way on the user side.
 * <br>
 * This class also provides the ability to access the values of fields directly by applying the {@link DafifFieldSpec} as outlined by
 * the {@link DafifRecordSpec} inline with querying them for explicit values.
 */
public class DafifRecord implements Serializable {

  /**
   * An map from the {@link DafifRecordField#fieldName()} to a pair of the associated {@link DafifRecordField#fieldSpec()} and to it's
   * extracted substring of associated data from within the source raw DAFIF record string.
   */
  private final Map<String, Pair<DafifFieldSpec<?>, String>> namedData;

  private final DafifRecordType recordType;

  DafifRecord(Map<String, Pair<DafifFieldSpec<?>, String>> namedData, DafifRecordType recordType) {
    this.namedData = namedData;
    this.recordType = recordType;
  }

  public String rawRecord() {
    return namedData.values().stream().map(Pair::second).collect(Collectors.joining("\t"));
  }

  /**
   * Allows users to check if the data class contains a parse-able version of the queried DAFIF field within the record.
   * <br>
   * If the queried field doesn't exist in the record this class will log at the DEBUG level the missing field and offending
   * record. This is generally useful to have baked into the record class so consumers don't have to replicate this logic in
   * multiple places.
   */
  public boolean containsParsedField(String fieldName) {
    return optionalField(fieldName).isPresent();
  }

  public <U, T extends DafifFieldSpec<U>> Optional<T> specForField(String fieldName) {
    return Optional.ofNullable(namedData.get(fieldName)).map(p -> (T) p.first());
  }

  /**
   * Returns the substring of content from the associated raw DAIFF record which is associated with this named field in the spec.
   */
  public String rawField(String fieldName) {
    return Optional.ofNullable(namedData.get(fieldName)).map(Pair::second).orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }

  /**
   * Returns the result of applying {@link DafifFieldSpec#apply(Object)} for the provided field to the raw field value as extracted
   * from the underlying DAFIF record.
   * <br>
   * The {@link DafifFieldSpec}s themselves should be responsible for rejecting bad input values which they don't know how to parse
   * and should return {@link Optional#empty()} in those cases. If those contracts are kept than this method should never throw
   * any hard exceptions due to bad input record content (e.g. NumberFormatException, etc.).
   */
  public <T> Optional<T> optionalField(String fieldName) {
    Optional<Pair<DafifFieldSpec<?>, String>> spec = Optional.ofNullable(namedData.get(fieldName));
    return spec.flatMap(pair -> (Optional<T>) pair.first().apply(pair.second()));
  }

  /**
   * Version of {@link #optionalField(String)} which throws a hard {@link MissingRequiredFieldException} if a valid parsed version
   * of the field cannot be extracted from the underlying raw data.
   */
  public <T> T requiredField(String fieldName) {
    Optional<T> opt = optionalField(fieldName);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }

  public DafifRecordType recordType() {
    return recordType;
  }
}
