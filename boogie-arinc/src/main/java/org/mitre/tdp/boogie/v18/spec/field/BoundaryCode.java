package org.mitre.tdp.boogie.v18.spec.field;

import java.util.Arrays;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.FieldSpecParseException;

public enum BoundaryCode implements FieldSpec<BoundaryCode> {
  /**
   * Intended to use to parse other boundary codes.
   *
   * e.g. BoundaryCode.USA == BoundaryCode.SPEC.parse("U").
   */
  SPEC(null),
  /**
   * The United States - CONUS
   */
  USA("U"),
  /**
   * Canada and Alaska
   */
  CAN("C"),
  /**
   * Pacific
   */
  PAC("P"),
  /**
   * Latin America
   */
  LAM("L"),
  /**
   * South America
   */
  SAM("S"),
  /**
   * South Pacific
   */
  SPA("1"),
  /**
   * Europe
   */
  EUR("E"),
  /**
   * Eastern Europe
   */
  EEU("2"),
  /**
   * Middle East-South Asia
   */
  MES("M"),
  /**
   * Africa
   */
  AFR("A");

  private final String boundaryCode;

  BoundaryCode(String boundaryCode) {
    this.boundaryCode = boundaryCode;
  }

  public String boundaryCode() {
    return boundaryCode;
  }

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.18";
  }

  @Override
  public BoundaryCode parse(String fieldString) {
    return Arrays.stream(BoundaryCode.values())
        .filter(boundaryCode -> !boundaryCode.equals(SPEC))
        .filter(boundaryCode -> boundaryCode.boundaryCode.equals(fieldString))
        .findFirst()
        .orElseThrow(() -> new FieldSpecParseException(this, fieldString));
  }
}
