package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * Definition/Description: The “Section Code” field defines the major section of the navigation system data in which the
 * record resides.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public enum SectionCode implements FieldSpec<SectionCode> {
  SPEC,
  /**
   * Grid MORA.
   */
  A,
  /**
   * Navaid.
   */
  D,
  /**
   * Enroute.
   */
  E,
  /**
   * Heliport.
   */
  H,
  /**
   * Tables.
   */
  T,
  /**
   * Company Routes.
   */
  R,
  /**
   * Airport.
   */
  P,
  /**
   * Airspace.
   */
  U;

  private static final Optional<SectionCode> A_VALUE = Optional.of(A);
  private static final Optional<SectionCode> D_VALUE = Optional.of(D);
  private static final Optional<SectionCode> E_VALUE = Optional.of(E);
  private static final Optional<SectionCode> H_VALUE = Optional.of(H);
  private static final Optional<SectionCode> T_VALUE = Optional.of(T);
  private static final Optional<SectionCode> R_VALUE = Optional.of(R);
  private static final Optional<SectionCode> P_VALUE = Optional.of(P);
  private static final Optional<SectionCode> U_VALUE = Optional.of(U);

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.4";
  }

  @Override
  public Optional<SectionCode> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    return endOffset - startOffset == 1 ? fromCharacter(source.charAt(startOffset)) : Optional.empty();
  }

  private static Optional<SectionCode> fromCharacter(char character) {
    return switch (character) {
      case 'A' -> A_VALUE;
      case 'D' -> D_VALUE;
      case 'E' -> E_VALUE;
      case 'H' -> H_VALUE;
      case 'T' -> T_VALUE;
      case 'R' -> R_VALUE;
      case 'P' -> P_VALUE;
      case 'U' -> U_VALUE;
      default -> Optional.empty();
    };
  }
}
