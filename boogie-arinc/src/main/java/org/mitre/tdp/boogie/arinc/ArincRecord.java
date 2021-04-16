package org.mitre.tdp.boogie.arinc;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

/**
 * This class represents the most basic semi-structured view of an ARINC record. At this point the parser has been "applied" in
 * the sense that it has been used to deconstruct the underlying ARINC record into it's component fields.
 * <br>
 * Due to the varying quality of the ARINC data sources out there this class serves to provide named access to portions of the
 * record ideally to accept/reject records with potentially malformed content in an easier way on the user side.
 * <br>
 * This class also provides the ability to access the values of fields directly by applying the {@link FieldSpec} as outlined by
 * the {@link RecordSpec} inline with querying them for explicit values.
 * <br>
 * Also note that if you <i>really</i> want to this class has been made
 */
public final class ArincRecord implements Serializable {

  /**
   * The raw record string used to generate this parsed data object.
   * <br>
   * Internally we maintain a copy of the raw record data - we could expose this to users if people want access to this value
   * from within the API layer - for now we just carry it through for posterity.
   */
  private final String rawRecord;
  /**
   * An immutable map from the {@link RecordField#fieldName()} to it's {@link RecordField#fieldSpec()} representing the mapping
   * from internal record field to parser/spec class.
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
   * Returns the {@link FieldSpec} for the provided (non-null) field name. This is the spec associated with the provided field
   * as declared in the {@link RecordSpec} which was associated with the contained raw ARINC record.
   */
  public <T, A extends FieldSpec<T>> A specForField(String fieldName) {
    requireNonNull(fieldName);
    A spec = (A) namedFields.get(fieldName);
    return requireNonNull(spec, "Unable to lookup spec for field of name ".concat(fieldName));
  }

  /**
   * Returns the substring of content from the associated raw ARINC record which is associated with this named field in the spec.
   */
  public String rawField(String fieldName) {
    requireNonNull(fieldName);
    checkArgument(namedData.containsKey(fieldName), "Unable to find field with name ".concat(fieldName));
    return namedData.get(fieldName);
  }

  public <T> Optional<T> optionalField(String fieldName) {
    FieldSpec<T> spec = specForField(fieldName);
    return spec.parse(rawField(fieldName));
  }

  public <T> T requiredField(String fieldName) {
    Optional<T> opt = optionalField(fieldName);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }
}
