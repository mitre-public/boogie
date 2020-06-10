package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The speed limit will be derived from official government source documentation and shown in Knots. When used on an Airport or
 * Heliport Record, the field is an indication of the maximum allowed speed and applies to all flight segments departing or arriving
 * that airport’s or heliport’s terminal area, at and below the specified Speed Limit Altitude (5.73). When used on Airport and
 * Heliport SID/STAR/Approach Records, the field is an indication of a speed for a fix in the procedure description, used in
 * conjunction with Speed Limit Description (5.261).
 */
public class SpeedLimit implements NumericInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.72";
  }
}
