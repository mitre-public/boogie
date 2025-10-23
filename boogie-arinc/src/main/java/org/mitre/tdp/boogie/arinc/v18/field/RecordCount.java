package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Contains the decimal count of the number of data records in the file
 */
public final class RecordCount extends ArincInteger {
  @Override
  public int fieldLength() {
    return 7;
  }

  @Override
  public String fieldCode() {
    return "6.2.1f";
  }
}
