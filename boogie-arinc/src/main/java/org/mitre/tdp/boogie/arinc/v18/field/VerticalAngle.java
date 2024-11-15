package org.mitre.tdp.boogie.arinc.v18.field;

import static java.lang.Math.abs;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The “Vertical Angle” field defines the vertical navigation path prescribed for the procedure. The vertical angle should cause
 * the aircraft to fly at the last coded altitude and then descend on the angle, projected back from the fix and altitude code
 * for that fix at which the angle is coded. Vertical Angle information is provided only for descending vertical navigation. The
 * angle is preceded by a “–” (minus sign) to indicate the descending flight.
 */
public final class VerticalAngle implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.70";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim)
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithHundredths).filter(d -> abs(d) < 10.);
  }
}
