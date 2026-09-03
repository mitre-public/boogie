package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum HeaderIdent implements FieldSpec<HeaderIdent> {
  SPEC,
  HDR;

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "6.2.1a";
  }

  @Override
  public Optional<HeaderIdent> parse(String source, int startOffset, int endOffset) {
    return Optional.of(source)
        .map(s -> s.substring(startOffset, endOffset))
        .filter(i -> i.equals("HDR"))
        .map(HeaderIdent::valueOf);
  }
}
