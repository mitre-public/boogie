package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;

import java.util.Optional;
import java.util.function.Function;

public class ArincFirUirLegConverter implements Function<ArincRecord, Optional<ArincFirUirLeg>> {


  @Override
  public Optional<ArincFirUirLeg> apply(ArincRecord arincRecord) {
    return Optional.empty();
  }
}
