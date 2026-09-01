package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

/**
 * Base for field specs whose surrounding fixed-width padding is not part of their value.
 * Padding follows {@link String#trim()} semantics: characters through {@code U+0020} are removed.
 */
public abstract class TrimmableField<T> implements FieldSpec<T> {

  @Override
  public final Optional<T> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (!acceptsSourceLength(endOffset - startOffset)) {
      return Optional.empty();
    }

    while (startOffset < endOffset && source.charAt(startOffset) <= ' ') {
      startOffset++;
    }
    while (startOffset < endOffset && source.charAt(endOffset - 1) <= ' ') {
      endOffset--;
    }

    return startOffset == endOffset ? Optional.empty() : parseTrimmed(source, startOffset, endOffset);
  }

  /**
   * Parses a non-blank range after its surrounding ARINC padding has been removed.
   */
  protected abstract Optional<T> parseTrimmed(String source, int startOffset, int endOffset);

  /**
   * Allows a field family to reject a source width before its padding is removed.
   */
  protected boolean acceptsSourceLength(int length) {
    return true;
  }
}
