package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;

import java.util.function.Predicate;

public class ArincFirUirLegValidator implements Predicate<ArincRecord> {
  @Override
  public boolean test(ArincRecord arincRecord) {
    return false;
  }
}
