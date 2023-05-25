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

  /**
   * Provided as a base class for {@link ResolvedTokenVisitor} implementations which may only be collection information about a
   * few of the token types and where the brevity this allows for can make code more readable.
   *
   * <p>The price these classes pay is they will no longer break at compile-time when a new element is added to the object graph
   * which they may (or may not) care about... so use with caution.
   */
  abstract class Noop implements ResolvedTokenVisitor {

    @Override
    public void visit(ResolvedToken.StandardAirport airport) {
    }

    @Override
    public void visit(ResolvedToken.DirectToAirport airport) {
    }

    @Override
    public void visit(ResolvedToken.StandardAirway airway) {
    }

    @Override
    public void visit(ResolvedToken.StandardApproach approach) {
    }

    @Override
    public void visit(ResolvedToken.StandardFix fix) {
    }

    @Override
    public void visit(ResolvedToken.DirectToFix fix) {
    }

    @Override
    public void visit(ResolvedToken.StandardLatLong latLong) {
    }

    @Override
    public void visit(ResolvedToken.DirectToLatLong latLong) {
    }

    @Override
    public void visit(ResolvedToken.SidEnrouteCommon sid) {
    }

    @Override
    public void visit(ResolvedToken.SidRunway sid) {
    }

    @Override
    public void visit(ResolvedToken.StarEnrouteCommon star) {
    }

    @Override
    public void visit(ResolvedToken.StarRunway star) {
    }

    @Override
    public void visit(ResolvedToken.StandardFrd frd) {
    }

    @Override
    public void visit(ResolvedToken.DirectToFrd frd) {
    }
  }
}
