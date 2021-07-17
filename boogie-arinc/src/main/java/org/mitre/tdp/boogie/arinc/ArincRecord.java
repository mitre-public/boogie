package org.mitre.tdp.boogie.arinc;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

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

  private static final Logger LOG = LoggerFactory.getLogger(ArincRecord.class);

  /**
   * The raw record string used to generate this parsed data object.
   * <br>
   * Internally we maintain a copy of the raw record data - we could expose this to users if people want access to this value
   * from within the API layer - for now we just carry it through for posterity.
   */
  private final String rawRecord;
  /**
   * An immutable map from the {@link RecordField#fieldName()} to it's {@link RecordField#fieldSpec()} representing the mapping
   * from internal named record field to parser/spec class.
   */
  private final ImmutableMap<String, FieldSpec<?>> namedFields;
  /**
   * An immutable map from the {@link RecordField#fieldName()} to it's extracted substring of associated data from within the
   * source raw ARINC record string.
   */
  private final ImmutableMap<String, String> namedData;

  ArincRecord(String rawRecord, Map<String, FieldSpec<?>> namedFields, Map<String, String> namedData) {
    this.rawRecord = requireNonNull(rawRecord);
    this.namedFields = ImmutableMap.copyOf(namedFields);
    this.namedData = ImmutableMap.copyOf(namedData);
    checkArgument(Sets.symmetricDifference(namedFields.keySet(), namedData.keySet()).isEmpty());
  }

  /**
   * Allows users to check if the data class contains a parse-able version of the queried ARINC field within the record.
   * <br>
   * If the queried field doesn't exist in the record this class will log at the DEBUG level the missing field and offending
   * record. This is generally useful to have baked into the record class so consumers don't have to replicate this logic in
   * multiple places.
   */
  public boolean containsParsedField(String fieldName) {
    Optional<?> value = optionalField(fieldName);
    if (!value.isPresent()) {
      LOG.debug("Unable find requested field '{}' in record '{}'", fieldName, rawRecord);
    }
    return value.isPresent();
  }

  /**
   * Returns the {@link FieldSpec} for the provided (non-null) field name. This is the spec associated with the provided field
   * as declared in the {@link RecordSpec} which was associated with the contained raw ARINC record.
   */
  public <T, A extends FieldSpec<T>> Optional<A> specForField(String fieldName) {
    requireNonNull(fieldName);
    A spec = (A) namedFields.get(fieldName);
    return Optional.ofNullable(spec);
  }

  /**
   * Returns the substring of content from the associated raw ARINC record which is associated with this named field in the spec.
   */
  public String rawField(String fieldName) {
    requireNonNull(fieldName);
    checkArgument(namedData.containsKey(fieldName), "Unable to find field with name ".concat(fieldName));
    return namedData.get(fieldName);
  }

  /**
   * Returns the result of applying {@link FieldSpec#parse(String)} for the provided field to the raw field value as extracted
   * from the underlying ARINC record.
   * <br>
   * The {@link FieldSpec}s themselves should be responsible for rejecting bad input values which they don't know how to parse
   * and should return {@link Optional#empty()} in those cases. If those contracts are kept than this method should never throw
   * any hard exceptions due to bad input record content (e.g. NumberFormatException, etc.).
   */
  public <T> Optional<T> optionalField(String fieldName) {
    Optional<FieldSpec<T>> spec = specForField(fieldName);
    return spec.flatMap(s -> s.parse(rawField(fieldName)));
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
