package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

/**
 * Abstract class representing a string field with trimmable contents - if the string can be trimmed resulting in an empty
 * string then parsing returns {@link Optional#empty()}.
 *
 * <p>Short values are shared through one bounded, thread-safe cache across all instances of this field family. The cache
 * survives individual imports, retains at most 65,536 values of up to eight characters, and also serves standalone field
 * parsing. Values must be compared by equality; eviction and concurrent parsing mean reference identity is not guaranteed.
 * Cache hits reuse the parsed result without creating a substring.
 */
public abstract class TrimmableString extends TrimmableField<String> {

  private static final StringPool STRINGS = new StringPool();

  @Override
  protected final Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    return STRINGS.canonicalize(source, startOffset, endOffset);
  }
}
