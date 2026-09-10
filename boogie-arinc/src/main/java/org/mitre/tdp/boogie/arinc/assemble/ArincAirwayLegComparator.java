package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Comparator;

import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

public final class ArincAirwayLegComparator implements Comparator<ArincAirwayLeg> {

  private static final Comparator<ArincAirwayLeg> COMPARATOR = Comparator
      .comparing((ArincAirwayLeg leg) -> leg.customerAreaCode().toString())
      .thenComparing(ArincAirwayLeg::routeIdentifier)
      .thenComparingInt(ArincAirwayLeg::sequenceNumber)
      .thenComparing(leg -> leg.continuationRecordNumber().orElse(null), Comparator.nullsFirst(Comparator.naturalOrder()));

  @Override
  public int compare(ArincAirwayLeg l, ArincAirwayLeg r) {
    return COMPARATOR.compare(l, r);
  }
}
