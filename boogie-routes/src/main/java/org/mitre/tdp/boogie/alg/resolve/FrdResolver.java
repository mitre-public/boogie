package org.mitre.tdp.boogie.alg.resolve;

import static java.lang.Double.parseDouble;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToFrd;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.standardFrd;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.TAILORED;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isTailoredBefore;

import java.util.List;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.FixRadialDistance;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class FrdResolver implements SingleTokenResolver {

  private final LookupService<Fix> lookupService;

  FrdResolver(LookupService<Fix> lookupService) {
    this.lookupService = requireNonNull(lookupService);
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken token) {
    return Stream.of(token)
        .filter(t -> t.infrastructureName().matches(TAILORED.pattern()))
        .flatMap(t -> lookupService.apply(fixIdentifier(t.infrastructureName())).stream()
            .map(f -> makeResolved(t, f)))
        .collect(toList());
  }

  private ResolvedToken makeResolved(RouteToken token, Fix fix) {

    Pair<Course, Distance> pair = bearingDistance(token.infrastructureName());

    FixRadialDistance frd = FixRadialDistance.create(fix, pair.first(), pair.second());
    return isTailoredBefore(token) ? standardFrd(frd) : directToFrd(frd);
  }

  static String fixIdentifier(String frd) {
    return frd.substring(0, frd.length() - 6);
  }

  static Pair<Course, Distance> bearingDistance(String frd) {
    int n = frd.length();

    double crs = parseDouble(frd.substring(n - 6, n - 3));
    double dist = parseDouble(frd.substring(n - 3, n));

    return Pair.of(Course.ofDegrees(crs), Distance.ofNauticalMiles(dist));
  }
}
