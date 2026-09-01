package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Definition/Description: The “FIR/UIR Identifier” field may contain the identifier of a FIR, UIR or combined FIR/UIR.
 * This field indicates which one of these records is an element.
 */
public enum FirUirIndicator implements FieldSpec<FirUirIndicator> {
  SPEC,
  /**
   * FIR
   */
  F,
  /**
   * UIR
   */
  U,
  /**
   * Both
   */
  B;

  private static FirUirIndicator parse(String source) {
    return switch (source) {
      case "F" -> F;
      case "U" -> U;
      case "B" -> B;
      default -> null;
    };
  }

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.117";
  }

  @Override
  public Optional<FirUirIndicator> parse(String source, int startOffset, int endOffset) {
    return Optional.ofNullable(parse(source.substring(startOffset, endOffset)));
  }
}
