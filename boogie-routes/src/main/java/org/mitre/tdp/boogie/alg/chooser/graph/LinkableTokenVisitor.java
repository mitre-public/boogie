package org.mitre.tdp.boogie.alg.chooser.graph;

public interface LinkableTokenVisitor {

  void visit(AnyAirport airport);

  void visit(AnyAirway airway);

  void visit(AnyApproach approach);

  void visit(AnyFix fix);

  void visit(AnyLatLong latLong);

  void visit(AnySid sid);

  void visit(AnyStar star);

  void visit(AnyFrd frd);
}
