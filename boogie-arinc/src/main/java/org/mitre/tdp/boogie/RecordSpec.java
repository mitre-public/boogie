package org.mitre.tdp.boogie;

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
  List<ArincField<?>> recordFields();

  /**
   * Returns whether the given record matches the record specification and should be parsed as such.
   */
  boolean matchesRecord(String arincRecord);

  /**
   * Creates a new parsed record from the supplied raw record and the calling spec.
   */
  default ArincRecord createParsedRecord(String rawRecord) {
    return new ArincData(rawRecord).parseWithSpec(this);
  }
}
