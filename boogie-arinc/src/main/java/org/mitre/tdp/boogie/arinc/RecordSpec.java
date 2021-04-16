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
   * Returns whether the given record matches the record specification and should be parsed as such.
   */
  boolean matchesRecord(String arincRecord);
}
