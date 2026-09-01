package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

/**
 * Abstract class representing a string field with trimmable contents - if the string can be trimmed resulting in an empty
 * string then parsing returns {@link Optional#empty()}.
 */
public abstract class TrimmableString extends TrimmableField<String> {

  @Override
  protected final Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    return Optional.of(source.substring(startOffset, endOffset).intern());
  }
}
