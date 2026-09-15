package org.mitre.tdp.boogie.dafif.v81.converter;

import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.model.enums.Derivation;

/**
 * Converts a parsed DAFIF 8.1 derivation code into its model value.
 */
public final class DerivationConverter implements Function<String, Derivation> {

  public static final DerivationConverter INSTANCE = new DerivationConverter();

  private DerivationConverter() {
  }

  /**
   * @throws IllegalArgumentException if the code is null or unsupported
   */
  @Override
  public Derivation apply(String code) {
    if (code == null) {
      throw new IllegalArgumentException("DAFIF derivation code cannot be null.");
    }
    return switch (code) {
      case "B" -> Derivation.DISTANCE_AND_BEARING;
      case "E" -> Derivation.END_COORDINATES;
      case "R" -> Derivation.PLOTTED_COORDINATES;
      default -> throw new IllegalArgumentException("Unsupported DAFIF derivation code: " + code);
    };
  }
}
