package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents the most basic semi-structured view of an ARINC record. At this point the parser has associated the
 * underlying fixed-width record with the named fields in its {@link RecordSpec}; field extraction and decoding happen on demand.
 * <br>
 * Due to the varying quality of the ARINC data sources out there this class serves to provide named access to portions of the
 * record ideally to accept/reject records with potentially malformed content in an easier way on the user side.
 * <br>
 * This class also provides the ability to access the values of fields directly by applying the {@link FieldSpec} as outlined by
 * the {@link RecordSpec} inline with querying them for explicit values.
 *
 * <p>Instances are intended to be consumed by a single thread. Different records can be processed independently in parallel,
 * but the same record should not be shared between threads while fields are being decoded.
 */
public final class ArincRecord {

  /**
   * The original fixed-width record. Field values are sliced from this string only when requested.
   */
  private final String rawRecord;

  /**
   * Immutable field metadata shared by every record parsed with the same {@link RecordSpec}.
   */
  private final FieldLayout fieldLayout;

  /**
   * Cache of parsed field values. A null slot is unparsed; every populated slot contains an {@link Optional} so absent values do
   * not need to be decoded repeatedly. Records are decoded by the parser's single-threaded consumption path, so synchronization
   * here would only add per-field overhead.
   */
  private final Object[] parsedFields;

  ArincRecord(String rawRecord, FieldLayout fieldLayout) {
    this.fieldLayout = requireNonNull(fieldLayout);
    this.rawRecord = requireNonNull(rawRecord).substring(0, fieldLayout.recordLength());
    this.parsedFields = new Object[fieldLayout.fieldCount()];
  }

  public String rawRecord() {
    return rawRecord;
  }

  /**
   * Allows users to check if the data class contains a parsable version of the queried ARINC field within the record.
   * <br>
   * If the queried field doesn't exist in the record this class will log at the DEBUG level the missing field and offending
   * record. This is generally useful to have baked into the record class so consumers don't have to replicate this logic in
   * multiple places.
   */
  public boolean containsParsedField(String fieldName) {
    return optionalField(fieldName).isPresent();
  }

  /**
   * Returns the field specification associated with the provided field name.
   * <p>
   * Because fields are selected by string name, callers are responsible for requesting the specification type declared for
   * that field by the underlying {@link RecordSpec}.
   */
  @SuppressWarnings("unchecked")
  public <U, T extends FieldSpec<U>> Optional<T> specForField(String fieldName) {
    FieldLocation field = fieldLayout.field(fieldName);
    return field == null ? Optional.empty() : Optional.of((T) field.spec());
  }

  /**
   * Returns the substring of content from the associated raw ARINC record which is associated with this named field in the spec.
   */
  public String rawField(String fieldName) {
    FieldLocation field = fieldLayout.field(fieldName);
    if (field == null) {
      throw new MissingRequiredFieldException(fieldName);
    }
    return rawRecord.substring(field.startOffset(), field.endOffset());
  }

  /**
   * Parses the provided field directly from its range in the underlying ARINC record.
   * <br>
   * The {@link FieldSpec}s themselves should be responsible for rejecting bad input values which they don't know how to parse
   * and should return {@link Optional#empty()} in those cases. If those contracts are kept then this method should never throw
   * any hard exceptions due to bad input record content (e.g. NumberFormatException, etc.).
   * <p>
   * Because fields are selected by string name, callers are responsible for requesting the value type declared for that field
   * by the underlying {@link RecordSpec}.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> optionalField(String fieldName) {
    FieldLocation field = fieldLayout.field(fieldName);
    if (field == null) {
      return Optional.empty();
    }

    Object parsed = parsedFields[field.index()];
    if (parsed == null) {
      parsed = requireNonNull(field.spec().parse(rawRecord, field.startOffset(), field.endOffset()));
      parsedFields[field.index()] = parsed;
    }

    return (Optional<T>) parsed;
  }

  /**
   * Version of {@link #optionalField(String)} which throws a hard {@link MissingRequiredFieldException} if a valid parsed version
   * of the field cannot be extracted from the underlying raw data.
   */
  public <T> T requiredField(String fieldName) {
    Optional<T> opt = optionalField(fieldName);
    return opt.orElseThrow(() -> new MissingRequiredFieldException(fieldName));
  }

  /**
   * Essentially a compiled {@code FieldSpec<FieldLocation>} which is used to look up the location of a field in the record.
   */
  static final class FieldLayout {

    private final Map<String, FieldLocation> fieldsByName;
    private final int recordLength;
    private final int fieldCount;

    private FieldLayout(Map<String, FieldLocation> fieldsByName, int recordLength, int fieldCount) {
      this.fieldsByName = Collections.unmodifiableMap(fieldsByName);
      this.recordLength = recordLength;
      this.fieldCount = fieldCount;
    }

    static FieldLayout from(List<RecordField<?>> recordFields) {
      Map<String, FieldLocation> fieldsByName = new LinkedHashMap<>(mapCapacity(recordFields.size()));

      int offset = 0;
      for (int index = 0; index < recordFields.size(); index++) {
        RecordField<?> recordField = recordFields.get(index);
        int endOffset = offset + recordField.fieldSpec().fieldLength();
        fieldsByName.put(
            recordField.fieldName(),
            new FieldLocation(index, offset, endOffset, recordField.fieldSpec())
        );
        offset = endOffset;
      }

      return new FieldLayout(fieldsByName, offset, recordFields.size());
    }

    FieldLocation field(String fieldName) {
      return fieldsByName.get(fieldName);
    }

    int fieldCount() {
      return fieldCount;
    }

    int recordLength() {
      return recordLength;
    }
  }

  private record FieldLocation(int index, int startOffset, int endOffset, FieldSpec<?> spec) {
  }

  private static int mapCapacity(int expectedSize) {
    return expectedSize < 3 ? expectedSize + 1 : (int) (expectedSize / 0.75f) + 1;
  }
}
