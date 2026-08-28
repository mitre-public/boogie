package org.mitre.tdp.boogie.arinc;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseTrimmedString;

import java.util.Optional;

/**
 * Abstract class representing a string field with trimmable contents - if the string can be trimmed resulting in an empty
 * string then the return of the apply method will be {@link Optional#empty()}.
 */
public abstract class TrimmableString implements FieldSpec<String> {

  @Override
  public final Optional<String> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public final Optional<String> apply(String source, int startOffset, int endOffset) {
    return parseTrimmedString(source, startOffset, endOffset);
  }
}
