package org.mitre.tdp.boogie.arinc;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Convenience container class for a <i>uniquely</i> named field within a top-level {@link RecordSpec} - used primarily in spec
 * construction.
 * <br>
 * Most record types will use the same logical 'ARINC field type' multiple times within a record to contain different relevant
 * information - this class allows us to associated named subsections of an ARINC record with a parsing spec.
 * <br>
 * e.g. The most common examples are things like Latitude/Longitude - a single ARINC record type may contain the latitude and
 * longitude of a variety of different things of interest in it's definition - the spec for what a "latitude" is should be an
 * invariant across records with latitudes and should be re-usable for multiple named subsections of the record.
 */
public final class RecordField<T> {

  /**
   * The string name of the field within the containing {@link RecordSpec}.
   */
  private final String fieldName;
  /**
   * The {@link FieldSpec} which should be applied to parse the string contents of the field from the raw record.
   */
  private final FieldSpec<T> fieldSpec;

  /**
   * This is provided for brevity in the cases where a single {@link FieldSpec} doesn't appear multiple times in a record.
   */
  public RecordField(FieldSpec<T> fieldSpec) {
    this(fieldSpec.fieldName(), fieldSpec);
  }

  public RecordField(String fieldName, FieldSpec<T> fieldSpec) {
    this.fieldName = requireNonNull(fieldName).intern();
    this.fieldSpec = requireNonNull(fieldSpec);
    checkArgument(isValidEnumSpec(), "Enum-based FieldSpecs must use reserved SPEC value for spec parsing.");
  }

  public String fieldName() {
    return fieldName;
  }

  public FieldSpec<T> fieldSpec() {
    return fieldSpec;
  }

  boolean isValidEnumSpec() {
    return !(fieldSpec instanceof Enum) || ((Enum) fieldSpec).name().equals("SPEC");
  }

  @Override
  public String toString() {
    return "RecordField{fieldName=".concat(fieldName).concat(",fieldSpec=").concat(fieldSpec.getClass().getSimpleName()).concat("}");
  }
}
