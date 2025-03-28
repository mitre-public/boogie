package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The GNSS/FMS Indicator field provides an indication of whether or not the responsible government agency has authorized the overlay of a conventional,
 * ground based approach procedure with the use of a sensor capable of processing GNSS data or if the procedure may be flown with FMS as the primary navigation equipment.
 * The field is also used to indicate when a PBN RNP procedure has been authorized for GNSS-based vertical navigation.
 */
public final class GnssFmsIndicator implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.222";
  }

  private static final Set<String> VALID = Set.of("0", "1", "2", "3", "4", "A", "B", "C", "D", "P", "G", "L", "U");

  @Override
  public Optional<String> apply(String s) {
    return Optional.of(s).filter(VALID::contains);
  }
}
