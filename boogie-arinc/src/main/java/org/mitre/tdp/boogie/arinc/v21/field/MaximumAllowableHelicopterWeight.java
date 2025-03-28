package org.mitre.tdp.boogie.arinc.v21.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * The Maximum Allowable Helicopter Weight represents the maximum weight, expressed in hundreds of pounds, that a helipad or FATO can support.
 */
public final class MaximumAllowableHelicopterWeight extends ArincInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.309";
  }
}
