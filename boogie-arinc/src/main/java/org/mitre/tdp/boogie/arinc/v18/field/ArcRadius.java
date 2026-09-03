package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The “ARC Radius” field is used to define the radius of a precision turn. In Terminal Procedures, this is the “Constant Radius
 * To A Fix” Path and Termination, for “RF” Leg. In Holding Patterns, this is the turning radius, inbound to outbound leg, for
 * RNP Holding. The ARC Radius field is also used to specify the turn radius of RNP holding patterns included in SID, STAR, and
 * Approach Records as HA, HF, and HM legs.
 */
public final class ArcRadius extends ArincDouble {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.204";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 3;
  }
}
