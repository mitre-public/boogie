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
  public Optional<HeaderIdent> apply(String s) {
    return Optional.of(s).filter(i -> i.equals("HDR")).map(HeaderIdent::valueOf);
  }
}
