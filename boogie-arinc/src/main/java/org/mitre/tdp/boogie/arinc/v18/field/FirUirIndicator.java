package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * Definition/Description: The “FIR/UIR Identifier” field may contain the identifier of a FIR, UIR or combined FIR/UIR.
 * This field indicates which one of these records is an element.
 *
 *
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

  private static final Set<String> VALUES = ImmutableSet.of("F", "U", "B");

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.117";
  }

  @Override
  public Optional<FirUirIndicator> apply(String string) {
    return Optional.ofNullable(string).filter(VALUES::contains).map(FirUirIndicator::valueOf);
  }
}
