package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Path and Termination defines the path geometry for a single record of an ATC terminal procedure.
 */
public final class PathTerm implements FieldSpec<PathTerminator> {

  private static final int LETTER_COUNT = 26;
  private static final List<Optional<PathTerminator>> PARSED_VALUES = parsedValues();

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.21";
  }

  @Override
  public Optional<PathTerminator> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != 2) {
      return Optional.empty();
    }
    int first = source.charAt(startOffset) - 'A';
    int second = source.charAt(startOffset + 1) - 'A';
    return first >= 0 && first < LETTER_COUNT && second >= 0 && second < LETTER_COUNT
        ? PARSED_VALUES.get(first * LETTER_COUNT + second)
        : Optional.empty();
  }

  private static List<Optional<PathTerminator>> parsedValues() {
    List<Optional<PathTerminator>> parsed = new ArrayList<>(Collections.nCopies(LETTER_COUNT * LETTER_COUNT, Optional.empty()));
    for (PathTerminator terminator : PathTerminator.values()) {
      String code = terminator.name();
      parsed.set((code.charAt(0) - 'A') * LETTER_COUNT + code.charAt(1) - 'A', Optional.of(terminator));
    }
    return List.copyOf(parsed);
  }
}
