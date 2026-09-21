package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.checkFromToIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Immutable decoded results for a fixed set of single-character ASCII codes. Looking up a source range allocates neither a
 * substring nor an {@link Optional}. Whitespace is significant; fields which allow padding must trim the range first.
 */
public final class SingleCharacterLookup<T> {

  private static final int ASCII_SIZE = 128;

  private final List<Optional<T>> values;

  private SingleCharacterLookup(Collection<String> codes, Function<String, T> decode) {
    List<Optional<T>> parsed = new ArrayList<>(Collections.nCopies(ASCII_SIZE, Optional.empty()));
    for (String code : codes) {
      if (code.length() != 1 || code.charAt(0) >= ASCII_SIZE) {
        throw new IllegalArgumentException("Expected a single-character ASCII code: " + code);
      }
      parsed.set(code.charAt(0), Optional.of(decode.apply(code)));
    }
    this.values = List.copyOf(parsed);
  }

  /**
   * Decodes the allowed codes once when the lookup is created. Later changes to the supplied collection have no effect.
   */
  public static <T> SingleCharacterLookup<T> of(Collection<String> codes, Function<String, T> decode) {
    return new SingleCharacterLookup<>(codes, decode);
  }

  /**
   * Returns a shared result for an exact one-character range, or empty for unknown codes and other widths.
   */
  public Optional<T> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != 1) {
      return Optional.empty();
    }
    char code = source.charAt(startOffset);
    return code < ASCII_SIZE ? values.get(code) : Optional.empty();
  }
}
