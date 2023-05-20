package org.mitre.tdp.boogie.alg.resolve;

public interface ResolvedTokenVisitor {

  void visit(AirportElement airportElement);

  void visit(AirwayElement airwayElement);

  void visit(FixElement fixElement);

  void visit(SidElement sidElement);

  void visit(StarElement starElement);

  void visit(ApproachElement approachElement);

  void visit(TailoredElement tailoredElement);

  void visit(LatLonElement latLonElement);

  void visit(DirectToFixElement fixElement);

  void visit(InitialFixElement fixElement);
}
