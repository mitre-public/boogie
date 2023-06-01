package org.mitre.tdp.boogie.alg.facade;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.util.Streams.pairwise;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.FixTerminationLeg;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;

/**
 * Due to the nature of the {@link RouteTokenResolver} step in the expansion process we get DF->DF leg pairings often when expanding
 * routes which look similar to the following: {@code KDCA..JMACK..HUNTN..TABLO..KORD}
 *
 * <p>These generate leg sequences that look like: {@code KDCA (IF) -> JMACK (DF) -> HUNTN (DF) -> TABLO (DF) -> KORD (IF)}
 *
 * <p>This behavior is an artifact of the resolution process - where these individual "direct-to" waypoint filings on their own
 * don't provide enough context natively to know whether they would be flown as TF or DF leg types by the FMS.
 *
 * <p>e.g. {@code JMACK->HUNTN, HUNTN->TABLO} in the above would be flown as TF's by the FMS as the previous leg terminates in a
 * fix. Tagging them as such requires knowing the sequence of legs produced by the expander and so is done as a post-processing
 * step here.
 *
 * <p>This supports our overall goal of creating a route from the plan which looks as similar as we can make it to what a FMS would
 * generate if it was internally doing the expansion.
 */
final class DirectToConverter implements UnaryOperator<List<ResolvedLeg>> {

  DirectToConverter() {
  }

  @Override
  public List<ResolvedLeg> apply(List<ResolvedLeg> resolvedLegs) {

    Map<ResolvedLeg, ResolvedLeg> embedding = pairwise(resolvedLegs)
        .filter(pair -> isCandidatePair(pair.first(), pair.second()))
        // valid for conversion? -> create mapping from old version to new for embedding
        .map(pair -> Pair.of(pair.second(), makeUpdatedNext(pair.second())))
        .collect(Collectors.toMap(Pair::first, Pair::second));

    return resolvedLegs.stream().map(leg -> embedding.getOrDefault(leg, leg)).collect(toList());
  }

  /**
   * Returns true when the previous leg terminates in a fix and the next leg is indicated as having been filed direct in the route.
   */
  boolean isCandidatePair(ResolvedLeg previous, ResolvedLeg next) {
    return previous.leg().pathTerminator().isFixTerminating()
        && !RouteTokenVisitor.isTailoredBefore(previous.routeToken())
        && IsFiledDirect.test(next.resolvedToken());
  }

  private ResolvedLeg makeUpdatedNext(ResolvedLeg next) {
    Leg tf = FixTerminationLeg.TF(next.leg().associatedFix().orElseThrow());
    return ResolvedLeg.create(next.routeToken(), next.resolvedToken(), tf);
  }

  private static final class IsFiledDirect implements ResolvedTokenVisitor {

    private boolean filedDirect = false;

    private IsFiledDirect() {
    }

    static boolean test(ResolvedToken token) {
      IsFiledDirect visitor = new IsFiledDirect();
      token.accept(visitor);
      return visitor.filedDirect;
    }

    @Override
    public void visit(ResolvedToken.StandardAirport airport) {
    }

    @Override
    public void visit(ResolvedToken.DirectToAirport airport) {
      // in this case we explicitly don't want to modify airport direct-to filings
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
      this.filedDirect = true;
    }

    @Override
    public void visit(ResolvedToken.StandardLatLong latLong) {
    }

    @Override
    public void visit(ResolvedToken.DirectToLatLong latLong) {
      this.filedDirect = true;
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
      this.filedDirect = true;
    }
  }
}
