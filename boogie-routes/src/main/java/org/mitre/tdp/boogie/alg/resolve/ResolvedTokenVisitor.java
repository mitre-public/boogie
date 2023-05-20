package org.mitre.tdp.boogie.alg.resolve;

public interface ResolvedTokenVisitor {

  void visit(AirportToken airportElement);

  void visit(AirwayToken airwayElement);

  void visit(FixToken fixElement);

  void visit(SidToken sidElement);

  void visit(StarToken starElement);

  void visit(ApproachToken approachElement);

  void visit(TailoredToken tailoredElement);

  void visit(LatLonToken latLonElement);

  void visit(DirectToFixToken fixElement);

  void visit(InitialFixElement fixElement);
}
