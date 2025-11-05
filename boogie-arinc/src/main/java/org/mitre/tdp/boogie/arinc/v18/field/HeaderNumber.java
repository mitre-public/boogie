package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Decimal 01 indicates the first header record.
 */
public final class HeaderNumber extends ArincInteger {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "6.2.1b";
  }
}
