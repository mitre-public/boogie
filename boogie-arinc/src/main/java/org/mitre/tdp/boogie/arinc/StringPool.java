package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Best-effort sharing of short text values. Each slot holds at most one value; a collision replaces that value, so the cache
 * cannot accumulate an unbounded vocabulary. Longer descriptions are not retained here.
 *
 * <p>The cached results are immutable. Concurrent decoding of separate records is safe: a race can only reduce sharing.
 * Cache hits compare the source range directly; only misses create a field string to retain.
 */
final class StringPool {

  private static final int CAPACITY = 1 << 16;
  private static final int MAX_LENGTH = 8;

  private final AtomicReferenceArray<Optional<String>> values = new AtomicReferenceArray<>(CAPACITY);

  Optional<String> canonicalize(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    int length = endOffset - startOffset;
    if (length > MAX_LENGTH) {
      return Optional.of(source.substring(startOffset, endOffset));
    }

    int hash = 0;
    for (int offset = startOffset; offset < endOffset; offset++) {
      hash = 31 * hash + source.charAt(offset);
    }
    int index = (hash ^ (hash >>> 16)) & (CAPACITY - 1);
    Optional<String> cached = values.get(index);
    if (cached != null) {
      String value = cached.get();
      if (value.length() == length && source.regionMatches(startOffset, value, 0, length)) {
        return cached;
      }
    }

    Optional<String> parsed = Optional.of(source.substring(startOffset, endOffset));
    values.set(index, parsed);
    return parsed;
  }
}
