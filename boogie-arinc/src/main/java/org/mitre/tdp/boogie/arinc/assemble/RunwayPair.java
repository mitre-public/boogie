package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincRunway;

public record RunwayPair(ArincRunway thisRunway, ArincRunway otherEnd) {
}
