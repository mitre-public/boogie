package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Contains the decimal number 0132, i.e., the number of characters in
 * each data record.
 */
public final class RecordLength extends ArincInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "6.2.1e";
  }
}
