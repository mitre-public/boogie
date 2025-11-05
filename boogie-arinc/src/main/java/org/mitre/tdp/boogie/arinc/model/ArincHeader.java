package org.mitre.tdp.boogie.arinc.model;

/**
 * A common interface for all header records
 */
public interface ArincHeader {
  /**
   * Headers are numbered where 01 and 02 are defined, additional ones are essentially freeform.
   * @return the header number.
   */
  int headerNumber();
}
