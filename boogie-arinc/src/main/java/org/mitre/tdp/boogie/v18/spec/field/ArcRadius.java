package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

public class ArcRadius implements NumericDouble {
  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.204";
  }
}
