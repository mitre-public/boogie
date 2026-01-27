package org.mitre.tdp.boogie.conformance.alg.assign.generate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.util.Combinatorics;

public final class AreaProximitySupplier implements Supplier<Optional<FlyableLeg>> {

  private static final Predicate<Pair<Leg, Leg>> FIX_EQUAL_AND_THERE = p -> p.first().associatedFix().equals(p.second().associatedFix())
      && (p.first().associatedFix().isPresent() || p.second().associatedFix().isPresent());

  private final Route left;
  private final Route right;

  public AreaProximitySupplier(Route left, Route right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Optional<FlyableLeg> get() {
    Collection<Pair<Leg, Leg>> pairs = Combinatorics.cartesianProduct(left.legs(), right.legs());
    if (pairs.stream().anyMatch(FIX_EQUAL_AND_THERE)) {
      return Optional.empty();
    }

    Optional<Leg> last = left.legs().stream()
        .filter(i -> i.associatedFix().isPresent())
        .reduce((f,s) -> s);
    Optional<Leg> first = right.legs().stream()
        .filter(i -> i.associatedFix().isPresent())
        .findFirst();

    if (last.isPresent() && first.isPresent()) {
      double distance = last.get().associatedFix().get().latLong().distanceInNM(first.get().associatedFix().get().latLong());
      LatLong mid = LatLong.quickAvgLatLong(last.get().associatedFix().get().latLong(), first.get().associatedFix().get().latLong());
      AreaProximity areaProximity = new AreaProximity(mid, distance);
      Fix fix = Fix.builder()
          .latLong(mid)
          .fixIdentifier("PROXIMITY")
          .magneticVariation(first.get().associatedFix().get().magneticVariation().orElseThrow())
          .build();
      Leg standard = Leg.ifBuilder(fix, 0).build();
      Leg record = Leg.record(areaProximity, standard);
      Route route = Route.newRoute(List.of(record), areaProximity);
      FlyableLeg flyableLeg = new FlyableLeg(null, record, null, route);
      return Optional.of(flyableLeg);
    }

    return Optional.empty();
  }
}
