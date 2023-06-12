package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Comparator;

import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public final class ArincAirwayLegComparator implements Comparator<ArincAirwayLeg> {
  @Override
  public int compare(ArincAirwayLeg l, ArincAirwayLeg r) {
    return ComparisonChain.start()
        .compare(l.customerAreaCode().toString(), r.customerAreaCode().toString())
        .compare(l.routeIdentifier(), r.routeIdentifier())
        .compare(l.sequenceNumber(), r.sequenceNumber())
        .compare(l.continuationRecordNumber(), l.continuationRecordNumber(), Ordering.natural().nullsFirst())
        .result();
  }
}
