package org.mitre.tdp.boogie.alg.resolve;

/**
 * Interface for a generic visitor resolved tokens, usable via {@link ResolvedToken#accept(ResolvedTokenVisitor)} for collecting
 * subclass-specific information from them.
 */
public interface ResolvedTokenVisitor {

  void visit(ResolvedToken.StandardAirport airport);

  void visit(ResolvedToken.DirectToAirport airport);

  void visit(ResolvedToken.StandardAirway airway);

  void visit(ResolvedToken.StandardApproach approach);

  void visit(ResolvedToken.StandardFix fix);

  void visit(ResolvedToken.DirectToFix fix);

  void visit(ResolvedToken.StandardLatLong latLong);

  void visit(ResolvedToken.DirectToLatLong latLong);

  void visit(ResolvedToken.SidEnrouteCommon sid);

  void visit(ResolvedToken.SidRunway sid);

  void visit(ResolvedToken.StarEnrouteCommon star);

  void visit(ResolvedToken.StarRunway star);

  void visit(ResolvedToken.StandardFrd frd);

  void visit(ResolvedToken.DirectToFrd frd);
}
