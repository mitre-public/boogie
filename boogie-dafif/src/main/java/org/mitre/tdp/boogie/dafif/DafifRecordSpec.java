package org.mitre.tdp.boogie.dafif;

import java.util.List;

/**
 * Interface representing a logical record in the DAFIF specification.
 */
public interface DafifRecordSpec {

  /**
   * The ordered list of field specs associated with the given record type.
   */
  List<DafifRecordField<?>> recordFields();

  /**
   * Returns the type of record this spec is meant to parse.
   */
  DafifRecordType recordType();

  /**
   * Number of expected fields
   */
  int expectedNumFields();
}
