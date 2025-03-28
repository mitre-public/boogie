package org.mitre.tdp.boogie.arinc.v19.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Definition/Description: Vertical Scale Factor (VSF) is used to set the vertical deviation scale.
 */
public final class VerticalScaleFactor extends ArincInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.293";
  }
}
