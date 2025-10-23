package org.mitre.tdp.boogie.arinc;

import java.util.function.Predicate;

public final class IsThisAHeader implements Predicate<ArincRecord> {
  @Override
  public boolean test(ArincRecord arincRecord) {
    return arincRecord.containsParsedField("headerIdent");
  }
}
