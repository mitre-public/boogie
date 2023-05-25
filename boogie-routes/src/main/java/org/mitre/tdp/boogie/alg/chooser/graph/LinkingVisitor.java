package org.mitre.tdp.boogie.alg.chooser.graph;

/**
 * Package-private visitor for the built-in standard linking methodology.
 */
interface LinkingVisitor {

  Linker visit(AnyAirport airport);

  Linker visit(AnyAirway airway);

  Linker visit(AnyApproach approach);

  Linker visit(AnyFix fix);

  Linker visit(AnyLatLong latLong);

  Linker visit(AnySid sid);

  Linker visit(AnyStar star);

  Linker visit(AnyFrd frd);
}
