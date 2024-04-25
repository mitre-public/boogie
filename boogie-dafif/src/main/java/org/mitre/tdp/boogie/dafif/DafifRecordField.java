package org.mitre.tdp.boogie.dafif;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Convenience container class for a <i>uniquely</i> named field within a top-level {@link DafifRecordSpec} - used primarily in spec
 * construction.
 * <br>
 * Most record types will use the same logical 'DAFIF field type' multiple times within a record to contain different relevant
 * information - this class allows us to associated named subsections of an DAFIF record with a parsing spec.
 * <br>
 * e.g. The most common examples are things like Latitude/Longitude - a single DAFIF record type may contain the latitude and
 * longitude of a variety of different things of interest in it's definition - the spec for what a "latitude" is should be an
 * invariant across records with latitudes and should be re-usable for multiple named subsections of the record.
 */
public final class DafifRecordField<T> {

  /**
   * The string name of the field within the containing {@link DafifRecordSpec}.
   */
  private final String fieldName;
  /**
   * The {@link DafifFieldSpec} which should be applied to parse the string contents of the field from the raw record.
   */
  private final DafifFieldSpec<T> fieldSpec;

  /**
   * This is provided for brevity in the cases where a single {@link DafifFieldSpec} doesn't appear multiple times in a record.
   */
  public DafifRecordField(DafifFieldSpec<T> fieldSpec) {
    this(fieldSpec.fieldName(), fieldSpec);
  }

  public DafifRecordField(String fieldName, DafifFieldSpec<T> fieldSpec) {
    this.fieldName = requireNonNull(fieldName).intern();
    this.fieldSpec = requireNonNull(fieldSpec);
    checkArgument(isValidEnumSpec(), "Enum-based FieldSpecs must use reserved SPEC value for spec parsing.");
  }

  public String fieldName() {
    return fieldName;
  }

  public DafifFieldSpec<T> fieldSpec() {
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
