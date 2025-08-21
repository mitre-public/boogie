package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Procedure;

/**
 * Interface for a generic visitor resolved tokens, usable via {@link ResolvedToken#accept(ResolvedTokenVisitor)} for collecting
 * subclass-specific information from them.
 */
public interface ResolvedTokenVisitor {

  /**
   * Used to access an internal {@link Airport} instance of a {@link ResolvedToken} (if it has one). Typically used when looking
   * to infer SID/STARs based on those that are present at the arrival/departure airport.
   *
   * @param token any flavor of resolved token
   */
  static Optional<Airport> airport(ResolvedToken token) {
    GetAirport visitor = new GetAirport();
    token.accept(visitor);
    return ofNullable(visitor.airport);
  }

  /**
   * Used to access an internal SID {@link Procedure} instance of a {@link ResolvedToken} (if it has one). Typically used when
   * inferring runway transitions or simply to see whether a SID has already been resolved.
   *
   * @param token any flavor of resolved token
   */
  static Optional<Procedure> sid(ResolvedToken token) {
    GetSid visitor = new GetSid();
    token.accept(visitor);
    return ofNullable(visitor.sid);
  }

  /**
   * Used to access an internal STAR {@link Procedure} instance of a {@link ResolvedToken} (if it has one). Typically used when
   * inferring runway transitions or simply to see whether a STAR has already been resolved.
   *
   * @param token any flavor of resolved token
   */
  static Optional<Procedure> star(ResolvedToken token) {
    GetStar visitor = new GetStar();
    token.accept(visitor);
    return ofNullable(visitor.star);
  }

  static Optional<Procedure> sidStar(ResolvedToken token) {
    return star(token).or(() -> sid(token));
  }

  /**
   * Used to access an internal Approach {@link Procedure} instance of a {@link ResolvedToken} (if it has one). Typically used
   * when inferring an approach when equipage context has been provided.
   *
   * @param token any flavor of resolved token
   */
  static Optional<Procedure> approach(ResolvedToken token) {
    GetApproach visitor = new GetApproach();
    token.accept(visitor);
    return ofNullable(visitor.approach);
  }

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

  final class GetAirport implements ResolvedTokenVisitor {

    private Airport airport;

    private GetAirport() {
    }

    @Override
    public void visit(ResolvedToken.StandardAirport airport) {
      this.airport = airport.infrastructure();
    }

    @Override
    public void visit(ResolvedToken.DirectToAirport airport) {
      this.airport = airport.infrastructure();
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

  final class GetSid implements ResolvedTokenVisitor {

    private Procedure sid;

    private GetSid() {
    }

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
      this.sid = sid.infrastructure();
    }

    @Override
    public void visit(ResolvedToken.SidRunway sid) {
      this.sid = sid.infrastructure();
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

  final class GetStar implements ResolvedTokenVisitor {

    private Procedure star;

    private GetStar() {
    }

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
      this.star = star.infrastructure();
    }

    @Override
    public void visit(ResolvedToken.StarRunway star) {
      this.star = star.infrastructure();
    }

    @Override
    public void visit(ResolvedToken.StandardFrd frd) {
    }

    @Override
    public void visit(ResolvedToken.DirectToFrd frd) {
    }
  }

  final class GetApproach implements ResolvedTokenVisitor {

    private Procedure approach;

    private GetApproach() {
    }

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
      this.approach = approach.infrastructure();
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
