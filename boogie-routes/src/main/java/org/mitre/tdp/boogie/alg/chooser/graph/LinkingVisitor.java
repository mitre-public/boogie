package org.mitre.tdp.boogie.alg.chooser.graph;

public interface LinkingVisitor {

  Linker visit(GraphableDirectToAirport airport);

  Linker visit(GraphableAirway airway);
}
