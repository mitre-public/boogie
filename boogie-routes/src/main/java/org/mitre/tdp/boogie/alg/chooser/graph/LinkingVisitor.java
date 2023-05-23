package org.mitre.tdp.boogie.alg.chooser.graph;

/**
 * Package-private visitor for the built-in standard linking methodology.
 */
interface LinkingVisitor {

  Linker visit(StandardAirport airport);

  Linker visit(DirectToAirport airport);

  Linker visit(StandardAirway airway);

  Linker visit(StandardApproach approach);

  Linker visit(StandardFix fix);

  Linker visit(DirectToFix fix);

  Linker visit(StandardLatLong latLong);

  Linker visit(DirectToLatLong latLong);

  Linker visit(SidEnrouteCommon sid);

  Linker visit(SidRunway sid);

  Linker visit(StarEnrouteCommon star);

  Linker visit(StarRunway star);

  Linker visit(StandardFrd frd);

  Linker visit(DirectToFrd frd);
}
