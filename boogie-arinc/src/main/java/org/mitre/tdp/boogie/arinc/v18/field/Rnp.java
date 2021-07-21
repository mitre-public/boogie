package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * Required Navigation Performance (RNP) is a statement of the Navigation Performance necessary for operation within a defined
 * airspace in accordance with ICAO Annex 15 and/or State published rules.
 */
public final class Rnp implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.211";
  }

  /**
   * The required navigational precision in nm.
   */
  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(ValidArincNumeric.INSTANCE)
        .filter(s -> s.length() > 2)
        .map(s -> {
          int value = Integer.parseInt(fieldValue.substring(0, 2));
          int exp = -Integer.parseInt(fieldValue.substring(2));
          return value * Math.pow(10., exp);
        });
  }
}
