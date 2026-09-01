package org.mitre.tdp.boogie.arinc;

import java.util.List;

/**
 * Interface representing a logical record in the ARINC specification.
 */
public interface RecordSpec {

  /**
   * The expected length of the record.
   */
  int recordLength();

  /**
   * The ordered list of field specs associated with the given record type.
   */
  List<RecordField<?>> recordFields();

  /**
   * Returns the coarse fixed-width addresses at which this record type can occur. Multiple discriminators are alternatives, and
   * together they must cover every input for which {@link #matchesRecord(String)} can return {@code true}. A discriminator may
   * admit records which the matcher later rejects.
   */
  List<RecordDiscriminator> recordDiscriminators();

  /**
   * Returns whether the given record matches the record specification and should be parsed as such.
   */
  boolean matchesRecord(String arincRecord);
}
