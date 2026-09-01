package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * Definition/Description: The “Record Type” field content indicates whether the record data are “standard,” i.e., suitable for
 * universal application, or “tailored,” i.e. included  on  the  master  file  for  a  single  user’s  specific  purpose (Section
 * 1.2 of this Specification refers).
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public enum RecordType implements FieldSpec<RecordType> {
  SPEC,
  /**
   * Standard record types used across all aircraft and airlines.
   */
  S,
  /**
   * Tailored records generally for use by a particular airline (company routes, etc.).
   */
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.2";
  }

  private static final Optional<RecordType> STANDARD = Optional.of(S);
  private static final Optional<RecordType> TAILORED = Optional.of(T);

  @Override
  public Optional<RecordType> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    return endOffset - startOffset == 1 ? fromCharacter(source.charAt(startOffset)) : Optional.empty();
  }

  private static Optional<RecordType> fromCharacter(char character) {
    return switch (character) {
      case 'S' -> STANDARD;
      case 'T' -> TAILORED;
      default -> Optional.empty();
    };
  }
}
