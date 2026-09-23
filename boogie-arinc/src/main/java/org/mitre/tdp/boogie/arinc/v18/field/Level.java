package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;

/**
 * The Level field defines the airway structure of which the record is an element.
 */
public enum Level implements FieldSpec<Level> {
  SPEC,
  /**
   * All altitudes
   */
  B,
  /**
   * High level airways.
   */
  H,
  /**
   * Low level airways
   */
  L;

  static final Set<String> enumValues = Arrays.stream(Level.values()).filter(e -> !SPEC.equals(e)).map(Enum::name).collect(Collectors.toUnmodifiableSet());

  private static final SingleCharacterLookup<Level> PARSED_VALUES = SingleCharacterLookup.of(enumValues, Level::valueOf);

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.19";
  }

  @Override
  public Optional<Level> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    while (startOffset < endOffset && source.charAt(startOffset) <= ' ') {
      startOffset++;
    }
    while (startOffset < endOffset && source.charAt(endOffset - 1) <= ' ') {
      endOffset--;
    }
    return PARSED_VALUES.parse(source, startOffset, endOffset);
  }
}
