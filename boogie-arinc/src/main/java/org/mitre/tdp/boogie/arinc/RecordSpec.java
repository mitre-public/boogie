package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.util.List;

/**
 * A logical record in the ARINC specification. Its immutable discriminators are the sole source of truth for matching and
 * parser dispatch.
 */
public abstract class RecordSpec {

  private final List<RecordDiscriminator> recordDiscriminators;

  /**
   * Creates a record specification identified exclusively by the supplied discriminator alternatives.
   *
   * @param recordDiscriminators the non-empty, non-null discriminator alternatives for this specification
   */
  protected RecordSpec(List<RecordDiscriminator> recordDiscriminators) {
    List<RecordDiscriminator> discriminators =
        List.copyOf(requireNonNull(recordDiscriminators, "Record discriminators cannot be null."));
    if (discriminators.isEmpty()) {
      throw new IllegalArgumentException("Record specifications require at least one discriminator.");
    }
    this.recordDiscriminators = discriminators;
  }

  /**
   * The expected length of the record.
   *
   * @return the fixed record length
   */
  public abstract int recordLength();

  /**
   * The ordered list of field specs associated with the given record type.
   *
   * @return the ordered fields spanning the record
   */
  public abstract List<RecordField<?>> recordFields();

  /**
   * Returns the immutable fixed-width matchers identifying this record type. Multiple discriminators are alternatives.
   *
   * @return the discriminator alternatives
   */
  public final List<RecordDiscriminator> recordDiscriminators() {
    return recordDiscriminators;
  }

  /**
   * Returns whether any discriminator identifies the given record as this specification.
   *
   * @param arincRecord the raw record to test
   * @return {@code true} when at least one discriminator matches
   */
  public final boolean matchesRecord(String arincRecord) {
    requireNonNull(arincRecord);
    for (int index = 0; index < recordDiscriminators.size(); index++) {
      if (recordDiscriminators.get(index).matches(arincRecord)) {
        return true;
      }
    }
    return false;
  }
}
