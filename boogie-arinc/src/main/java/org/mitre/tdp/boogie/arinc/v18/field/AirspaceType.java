package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Controlled Airspace Type field is used to indicate the type of controlled airspace, using codes from the table below.
 *
 * The airspace type should be derived from official government publications. The table below shows the indicators
 * used for the various types. For the USA, the previous applied designations such as TCA are supplied for ease of
 * reference, they are longer officially published.
 *
 * A - Class C Airspace (was ARSA within the USA)
 * C - Control Area, ICAO Designation (CTA)
 * M - Terminal Control Area, ICAO Designation (TMA or TCA)
 * R - Radar Zone or Radar Area (was TRSA within the USA)
 * T - Class B Airspace (Was TCA with the USA)
 * Z - Class D Airspace within the USA, Control Zone, ICAO Designation (CTR)
 */

public enum AirspaceType implements FieldSpec<AirspaceType> {
  /**
   * Intended to use to parse other Airspace Types.
   * e.g. AirspaceType.A == AirspaceType.SPEC.parse("A").
   */
  SPEC,
  /**
   * Class C Airspace
   */
  A,
  /**
   * Control Area
   */
  C,
  /**
   * Terminal Control Area
   */
  M,
  /**
   * Radar Zone or Radar Area
   */
  R,
  /**
   * Class B Airspace
   */
  T,
  /**
   * Class D Airspace within the USA, Control Zone, ICAO Designation
   */
  Z;

  private static final Set<String> validNames = Arrays.stream(AirspaceType.values())
      .filter(d -> !SPEC.equals(d))
      .map(AirspaceType::name)
      .collect(Collectors.toSet());
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.213";
  }

  @Override
  public Optional<AirspaceType> apply(String string) {
    return Optional.of(string).filter(validNames::contains).map(AirspaceType::valueOf);
  }
}