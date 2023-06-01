package org.mitre.tdp.boogie.alg.facade;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;

/**
 * Functional class which is responsible for converting a {@link ResolvedLeg} to an {@link ExpandedRouteLeg} and adding the
 * appropriate categorical features to it's definition.
 */
final class ResolvedLegConverter implements Function<ResolvedLeg, ExpandedRouteLeg> {

  ResolvedLegConverter() {
  }

  @Override
  public ExpandedRouteLeg apply(ResolvedLeg resolvedLeg) {
    return new ExpandedRouteLeg(
        resolvedLeg.routeToken().infrastructureName(),
        fromResolvedElement(resolvedLeg.resolvedToken()),
        RouteTokenVisitor.wildcards(resolvedLeg.routeToken()),
        resolvedLeg.leg()
    );
  }

  public static ElementType fromResolvedElement(ResolvedToken resolvedToken) {
    return ElementTypeVisitor.get(resolvedToken)
        .orElseThrow(() -> new IllegalArgumentException("Unknown how to map input ResolvedElement to ElementType. ResolvedElement class was: ".concat(resolvedToken.getClass().getSimpleName())));
  }

  private static final class ElementTypeVisitor implements ResolvedTokenVisitor {

    private ElementType type;

    private ElementTypeVisitor() {
    }

    private static Optional<ElementType> get(ResolvedToken token) {
      ElementTypeVisitor visitor = new ElementTypeVisitor();
      token.accept(visitor);
      return ofNullable(visitor.type);
    }

    @Override
    public void visit(ResolvedToken.StandardAirport airport) {
      this.type = ElementType.AIRPORT;
    }

    @Override
    public void visit(ResolvedToken.DirectToAirport airport) {
      this.type = ElementType.AIRPORT;
    }

    @Override
    public void visit(ResolvedToken.StandardAirway airway) {
      this.type = ElementType.AIRWAY;
    }

    @Override
    public void visit(ResolvedToken.StandardApproach approach) {
      this.type = ElementType.APPROACH;
    }

    @Override
    public void visit(ResolvedToken.StandardFix fix) {
      this.type = ElementType.FIX;
    }

    @Override
    public void visit(ResolvedToken.DirectToFix fix) {
      this.type = ElementType.FIX;
    }

    @Override
    public void visit(ResolvedToken.StandardLatLong latLong) {
      this.type = ElementType.LATLON;
    }

    @Override
    public void visit(ResolvedToken.DirectToLatLong latLong) {
      this.type = ElementType.LATLON;
    }

    @Override
    public void visit(ResolvedToken.SidEnrouteCommon sid) {
      this.type = ElementType.SID;
    }

    @Override
    public void visit(ResolvedToken.SidRunway sid) {
      this.type = ElementType.SID;
    }

    @Override
    public void visit(ResolvedToken.StarEnrouteCommon star) {
      this.type = ElementType.STAR;
    }

    @Override
    public void visit(ResolvedToken.StarRunway star) {
      this.type = ElementType.STAR;
    }

    @Override
    public void visit(ResolvedToken.StandardFrd frd) {
      this.type = ElementType.TAILORED;
    }

    @Override
    public void visit(ResolvedToken.DirectToFrd frd) {
      this.type = ElementType.TAILORED;
    }
  }
}
