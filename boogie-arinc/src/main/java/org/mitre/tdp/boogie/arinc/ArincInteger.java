package org.mitre.tdp.boogie.arinc;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseInteger;

import java.util.Optional;

public abstract class ArincInteger implements FieldSpec<Integer> {

  @Override
  public final Optional<Integer> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public final Optional<Integer> apply(String source, int startOffset, int endOffset) {
    return parseInteger(source, startOffset, endOffset);
  }
}
