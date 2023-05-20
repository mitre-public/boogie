package org.mitre.tdp.boogie.alg.chooser.graph;

public interface LinkingVisitor {

  Linker visit(DirectToAirport airport);

  Linker visit(Airway airway);
}
