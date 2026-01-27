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

/**
 * This class supplies an {@link AreaProximity} "leg" when the left and right don't share any fixes.
 * This will hopefully create an option for when a route is arriving -> fly around in a circle -> approach.
 */
public final class AreaProximitySupplier implements Supplier<Optional<FlyableLeg>> {

  private static final Predicate<Pair<Leg, Leg>> FIX_EQUAL_AND_THERE = p -> p.first().associatedFix().equals(p.second().associatedFix())
      && (p.first().associatedFix().isPresent() || p.second().associatedFix().isPresent()); //optionals are equal when both empty we don't want that.

  private final Route left;
  private final Route right;

  public AreaProximitySupplier(Route left, Route right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Optional<FlyableLeg> get() {
    Collection<Pair<Leg, Leg>> pairs = Combinatorics.cartesianProduct(left.legs(), right.legs());
    //going to link up so ok.
    if (pairs.stream().anyMatch(FIX_EQUAL_AND_THERE)) {
      return Optional.empty();
    }

    Optional<? extends Fix> last = left.legs().stream()
        .map(Leg::associatedFix)
        .flatMap(Optional::stream)
        .reduce((f,s) -> s);
    Optional<? extends Fix> first = right.legs().stream()
        .map(Leg::associatedFix)
        .flatMap(Optional::stream)
        .findFirst();

    //nobody has fixes so ... just leave the natural phase linking in the linker to do the job.
    if (last.isEmpty() || first.isEmpty()) {
      return Optional.empty();
    }

    double distance = last.get().latLong().distanceInNM(first.get().latLong());
    LatLong mid = LatLong.quickAvgLatLong(last.get().latLong(), first.get().latLong());
    AreaProximity areaProximity = new AreaProximity(mid, distance);
    Fix fix = Fix.builder()
        .latLong(mid)
        .fixIdentifier("PROXIMITY")
        .magneticVariation(first.get().magneticVariation().orElseThrow())
        .build();
    Leg standard = Leg.ifBuilder(fix, 0).build();
    Leg record = Leg.record(areaProximity, standard);
    Route route = Route.newRoute(List.of(record), areaProximity);
    FlyableLeg flyableLeg = new FlyableLeg(null, record, null, route);
    return Optional.of(flyableLeg);
  }
}
