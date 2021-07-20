package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec2;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The “ARC Radius” field is used to define the radius of a precision turn. In Terminal Procedures, this is the “Constant Radius
 * To A Fix” Path and Termination, for “RF” Leg. In Holding Patterns, this is the turning radius, inbound to outbound leg, for
 * RNP Holding. The ARC Radius field is also used to specify the turn radius of RNP holding patterns included in SID, STAR, and
 * Approach Records as HA, HF, and HM legs.
 */
public final class ArcRadius implements FieldSpec2<Double> {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.204";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(ValidArincNumeric.INSTANCE)
        .map(String::trim)
        .map(ArincStrings::parseDoubleWithThousandths);
  }
}
