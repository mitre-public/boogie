package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Airports can be classified into three categories, airports open to the general public, military airports, and airport closed to the public.
 * This field permits these airports to be categorized by their use.
 */
public enum PublicMilitaryIndicator implements FieldSpec<PublicMilitaryIndicator> {
  SPEC,
  /**
   * Civil
   */
  C,
  /**
   * Military
   */
  M,
  /**
   * Private
   */
  P,
  /**
   * Join operations.
   */
  J;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.177";
  }

  private static final Set<String> allowedPubMil = Arrays.stream(PublicMilitaryIndicator.values())
      .filter(v -> !SPEC.equals(v))
      .map(PublicMilitaryIndicator::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<PublicMilitaryIndicator> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(allowedPubMil::contains).map(PublicMilitaryIndicator::valueOf);
  }
}
