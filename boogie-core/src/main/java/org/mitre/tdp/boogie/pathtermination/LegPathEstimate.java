package org.mitre.tdp.boogie.pathtermination;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.util.ArcAngle;
import org.mitre.tdp.boogie.util.FlatEarthMath;
import org.mitre.tdp.boogie.util.Heading;

import com.google.common.collect.Range;

/**
 * <p>This class is used to find the start and end of each leg in a list of legs.
 * <p>This is only nominally for each leg because in real operations e.g., you can hold into an RF and could have left that
 * hold somewhere we don't know where it is. ARINC 424 is filled with stuff like this that makes this just an estimate.
 * <p>The list of legs must start with a leg with an altitude constraint of some kind if xA legs are to be estimated.
 * Otherwise, the altitude terminated legs will just return zero if there is nothing to go on.
 * <p>
 * The other issue is that in theory each leg's start/end in the path and termination object may not perfectly
 * link when we start considering intercepts just being an estimate.
 */
@FunctionalInterface
public interface LegPathEstimate {
  /**
   * ARINC 424 assumes that an aircraft can climb this fast for short distances.
   */
  double FT_PER_NM = 500;

  Map<Leg, PathAndTermination> estimateAll(List<Leg> legs);

  static LegPathEstimate standard() {
    return new Standard();
  }

  final class Standard implements LegPathEstimate {
    private Standard() {
    }

    @Override
    public Map<Leg, PathAndTermination> estimateAll(List<Leg> legs) {
      Map<Leg, PathAndTermination> map = new HashMap<>();
      if (legs.stream().noneMatch(l -> l.associatedFix().isPresent())) {
        return map;
      }
      IntStream.range(0, legs.size()).forEach(i -> map.put(legs.get(i), estimateCurrent(legs, i, map)));
      return map;
    }

    private PathAndTermination estimateCurrent(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      return switch (legs.get(index).pathTerminator()) {
        case IF -> ifLeg(legs, index, map);
        case DF, TF -> dfTf(legs, index, map);
        case CF -> cfLeg(legs, index, map);
        case RF -> rfLeg(legs, index, map);
        case FC, PI -> fcPiLeg(legs, index, map);
        case FA, CA, VA, HA -> faCaVaHaLeg(legs, index, map);
        case VM, FM, HM -> vmFmHmLeg(legs, index, map);
        case FD -> fdLeg(legs, index, map);
        case CD, VD -> cdVdLeg(legs, index, map);
        case CI, VI -> ciViLeg(legs, index, map);
        case CR, VR -> crVrLeg(legs, index, map);
        case AF -> afLeg(legs, index, map);
        case HF -> hfLeg(legs, index, map);
      };
    }

    private Range<Double> currentConstraints(Leg leg, @Nullable Range<Double> previousMins) {
      Range<Double> previous = Optional.ofNullable(previousMins).orElse(Range.all());

      Optional<Double> previousUpper = Optional.of(previous)
          .filter(Range::hasUpperBound)
          .map(Range::upperEndpoint);
      Optional<Double> previousLower = Optional.of(previous)
          .filter(Range::hasLowerBound)
          .map(Range::lowerEndpoint);

      Optional<Double> currentUpper = Optional.of(leg.altitudeConstraint())
          .filter(Range::hasUpperBound)
          .map(Range::upperEndpoint);
      Optional<Double> currentLower = Optional.of(leg.altitudeConstraint())
          .filter(Range::hasLowerBound)
          .map(Range::lowerEndpoint);

      Optional<Double> upper = Stream.of(previousUpper, currentUpper)
          .flatMap(Optional::stream)
          .reduce((f, s) -> s);
      Optional<Double> lower = Stream.of(previousLower, currentLower)
          .flatMap(Optional::stream)
          .reduce((f, s) -> s);

      //sure wish we were in J21
      if (upper.isPresent() && lower.isPresent()) {
        return Range.closed(lower.get(), FastMath.max(upper.get(), lower.get()));
      }
      return upper.map(Range::atMost)
          .or(() -> lower.map(Range::atLeast))
          .orElse(Range.all());
    }

    private PathAndTermination ifLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      LatLong fix = leg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      Optional<PathAndTermination> previous = Optional.of(index).filter(i -> i > 0).map(i -> map.get(legs.get(i - 1)));
      Range<Double> alts = currentConstraints(leg, previous.map(PathAndTermination::endAltitudeFt).orElse(null));
      return PathAndTermination.builder()
          .startOfLeg(fix)
          .endOfLeg(fix)
          .endAltitudeFt(alts)
          .startAltitudeFt(alts)
          .lengthNm(0.0)
          .build();
    }

    private PathAndTermination dfTf(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      if (index == 0) {
        return ifLeg(legs, index, map);
      }
      PathAndTermination previous = map.get(legs.get(index - 1));
      Leg leg = legs.get(index);
      Range<Double> currentConstraints = currentConstraints(leg, previous.endAltitudeFt());
      LatLong end = leg.associatedFix()
          .map(Fix::latLong)
          .orElseThrow(IllegalStateException::new);
      LatLong start = previous.endOfLeg();
      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(end)
          .startAltitudeFt(previous.endAltitudeFt())
          .endAltitudeFt(currentConstraints)
          .lengthNm(start.distanceInNM(end))
          .build();
    }

    private PathAndTermination cfLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      if (index == 0) {
        return ifLeg(legs, index, map);
      }
      Leg leg = legs.get(index);
      MagneticVariation magvar = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      LatLong fix = leg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      double course = leg.outboundMagneticCourse()
          .map(magvar::magneticToTrue)
          .map(Heading::reciprocalHeading)
          .orElseThrow(IllegalStateException::new);
      double dist = leg.routeDistance().orElseThrow(IllegalStateException::new);
      LatLong start = fix.projectOut(course, dist);
      PathAndTermination previous = map.get(legs.get(index - 1));
      Range<Double> alts = currentConstraints(leg, previous.endAltitudeFt());
      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(fix)
          .startAltitudeFt(previous.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(start.distanceInNM(fix))
          .build();
    }

    private PathAndTermination rfLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      if (index == 0) {
        return ifLeg(legs, index, map);
      }
      Leg leg = legs.get(index);
      LatLong fix = leg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      PathAndTermination previousPathAndTermination = map.get(legs.get(index - 1));
      Range<Double> alts = currentConstraints(leg, previousPathAndTermination.endAltitudeFt());
      return PathAndTermination.builder()
          .startOfLeg(previousPathAndTermination.endOfLeg())
          .endOfLeg(fix)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(leg.routeDistance().orElseThrow(IllegalStateException::new))
          .build();
    }

    private PathAndTermination fcPiLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      LatLong fix = leg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      Range<Double> startAlt = Optional.of(index)
          .filter(i -> i > 0)
          .map(i -> map.get(legs.get(index - 1)))
          .map(PathAndTermination::endAltitudeFt)
          .orElse(Range.all());
      Range<Double> alts = currentConstraints(leg, startAlt);
      MagneticVariation magneticVariation = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double pathTrueCourse = leg.outboundMagneticCourse().map(magneticVariation::magneticToTrue).orElseThrow(IllegalStateException::new);
      double pathLengthOrMaxExcursionDist = leg.routeDistance().orElseThrow(IllegalStateException::new);
      LatLong end = fix.projectOut(pathTrueCourse, pathLengthOrMaxExcursionDist);
      return PathAndTermination.builder()
          .startOfLeg(fix)
          .endOfLeg(end)
          .startAltitudeFt(startAlt)
          .endAltitudeFt(alts)
          .lengthNm(leg.routeDistance().orElseThrow(IllegalStateException::new))
          .build();
    }

    private PathAndTermination faCaVaHaLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      if (index == 0) {
        return giveUp(leg, legs);
      }
      PathAndTermination previousPathAndTermination = map.get(legs.get(index - 1));
      LatLong start = leg.associatedFix().map(Fix::latLong).orElse(previousPathAndTermination.endOfLeg());
      MagneticVariation magneticVariation = leg.recommendedNavaid().flatMap(Fix::magneticVariation)
          .or(() -> leg.associatedFix().flatMap(Fix::magneticVariation))
          .or(() -> legs.subList(index, legs.size()).stream()
              .filter(l -> l.recommendedNavaid().isPresent())
              .findFirst()
              .flatMap(Leg::recommendedNavaid)
              .flatMap(Fix::magneticVariation))
          .or(() -> Optional.of(Declinations.approx(start.latitude(), start.longitude())).map(MagneticVariation::ofDegrees))
          .orElseThrow(IllegalStateException::new);
      double pathTrueCourse = leg.outboundMagneticCourse()
          .map(magneticVariation::magneticToTrue)
          .orElseThrow(IllegalStateException::new);
      double deltaHFt = Optional.of(previousPathAndTermination.endAltitudeFt())
          .filter(Range::hasLowerBound)
          .map(p -> leg.altitudeConstraint().lowerEndpoint() - p.lowerEndpoint())
          .orElse(500.0); //no bounds just assume 500 climb
      double lengthOfLeg = deltaHFt / LegPathEstimate.FT_PER_NM;
      LatLong end = start.projectOut(pathTrueCourse, lengthOfLeg);
      Range<Double> alts = currentConstraints(leg, previousPathAndTermination.endAltitudeFt());
      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(end)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(start.distanceInNM(end))
          .build();
    }

    private PathAndTermination vmFmHmLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      if (index == 0) {
        Optional<LatLong> start = leg.associatedFix().map(Fix::latLong);
        Optional<LatLong> next = legs.stream().skip(1)
            .map(Leg::associatedFix)
            .flatMap(Optional::stream)
            .findFirst()
            .map(Fix::latLong);
        double length = start.isPresent() && next.isPresent() ? start.get().distanceInNM(next.get()) : 0.0;
        return PathAndTermination.builder()
            .startOfLeg(start.or(() -> next).orElse(null))
            .endOfLeg(next.orElse(null))
            .startAltitudeFt(Range.all())
            .endAltitudeFt(leg.altitudeConstraint())
            .lengthNm(length)
            .build();
      }
      PathAndTermination previousPathAndTermination = map.get(legs.get(index - 1));
      LatLong startAndEnd = leg.associatedFix().map(Fix::latLong)
          .or(() -> Optional.ofNullable(previousPathAndTermination.endOfLeg()))
          .or(() -> legs.subList(index, legs.size()).stream()
              .filter(l -> l.associatedFix().isPresent())
              .findFirst()
              .flatMap(Leg::associatedFix)
              .map(Fix::latLong))
          .or(() -> legs.stream()
              .filter(l -> l.associatedFix().isPresent())
              .findFirst()
              .flatMap(Leg::associatedFix)
              .map(Fix::latLong))
          .orElseThrow(() -> new IllegalStateException("no points at all"));
      Range<Double> alts = currentConstraints(leg, previousPathAndTermination.endAltitudeFt());
      return PathAndTermination.builder()
          .startOfLeg(startAndEnd)
          .endOfLeg(startAndEnd)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(0.0)
          .build();
    }

    private PathAndTermination fdLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      Range<Double> alts = Optional.of(index)
          .filter(i -> i > 0)
          .map(i -> currentConstraints(leg, map.get(legs.get(i - 1)).endAltitudeFt()))
          .orElse(leg.altitudeConstraint());
      Range<Double> previousAlt = Optional.of(index)
          .filter(i -> i > 0)
          .map(i -> currentConstraints(leg, map.get(legs.get(i - 1)).endAltitudeFt()))
          .orElse(Range.all());
      LatLong start = leg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      LatLong navaid = leg.recommendedNavaid().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      double dme = leg.routeDistance().orElseThrow(IllegalStateException::new);
      MagneticVariation magvar = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double course = leg.outboundMagneticCourse().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);
      Optional<LatLong> end = tryNearThisHeading(course)
          .map(c -> FlatEarthMath.courseInterceptDme(start, c, navaid, dme))
          .flatMap(Optional::stream)
          .findFirst();
      double rho = leg.rho().orElseThrow(IllegalStateException::new);
      //this fallback is not great but at least it will be in the correct direction.
      Supplier<Optional<LatLong>> fallback = () -> Optional.of(start).map(i -> i.projectOut(course, rho));
      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(end.or(fallback).orElse(null))
          .startAltitudeFt(previousAlt)
          .endAltitudeFt(alts)
          .lengthNm(end.map(e -> e.distanceInNM(start)).orElse(rho))
          .build();
    }

    private PathAndTermination cdVdLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      if (index == 0) {
        return giveUp(leg, legs);
      }
      PathAndTermination previousPathAndTermination = map.get(legs.get(index - 1));
      Range<Double> alts = currentConstraints(leg, previousPathAndTermination.endAltitudeFt());
      LatLong start = previousPathAndTermination.endOfLeg();
      LatLong navaid = leg.recommendedNavaid().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      double dme = leg.routeDistance().orElseThrow(IllegalStateException::new);
      MagneticVariation magvar = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double course = leg.outboundMagneticCourse().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);

      Optional<LatLong> end = tryNearThisHeading(course)
          .map(c -> FlatEarthMath.courseInterceptDme(start, c, navaid, dme))
          .flatMap(Optional::stream)
          .findFirst();
      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(end.orElse(start))
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(end.map(e -> e.distanceInNM(start)).orElse(0.0))
          .build();
    }

    private PathAndTermination ciViLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      if (index == 0) {
        return giveUp(leg, legs);
      }

      if (index == legs.size() - 1) {
        return giveUpForwards(leg, legs, index, map);
      }

      Leg nextLeg = legs.get(index + 1);
      PathAndTermination previousPathAndTermination = map.get(legs.get(index - 1));

      if (nextLeg.pathTerminator().equals(PathTerminator.IF) && index == legs.size() - 1) {
        return giveUp(leg, legs);
      }

      return switch (nextLeg.pathTerminator()) {
        case AF -> afIntercept(leg, nextLeg, previousPathAndTermination)
            .orElseGet(() -> giveUpForwards(leg, legs, index, map));
        case CF -> cfIntercept(leg, nextLeg, previousPathAndTermination)
            .orElseGet(() -> giveUpForwards(leg, legs, index, map));
        case FA, FC, FD, FM -> fxIntercept(leg, nextLeg, previousPathAndTermination)
            .orElseGet(() -> giveUpForwards(leg, legs, index, map));
        case IF -> ifTfIntercept(leg, nextLeg, legs.get(index + 2), previousPathAndTermination)
            .orElseGet(() -> giveUpForwards(leg, legs, index, map));
        default -> giveUp(leg, legs); //not suppose to happen anyway per next leg rules
      };
    }

    private Optional<PathAndTermination> cfIntercept(Leg current, Leg next, PathAndTermination previousPathAndTermination) {
      LatLong start = previousPathAndTermination.endOfLeg();
      MagneticVariation nextMagvar = next.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      MagneticVariation currentMagvar = current.recommendedNavaid().flatMap(Fix::magneticVariation).orElse(nextMagvar);

      double currentTrue = current.outboundMagneticCourse().map(currentMagvar::magneticToTrue).orElseThrow(IllegalStateException::new);

      LatLong nextFix = next.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);

      double nextRecip = next.outboundMagneticCourse().map(nextMagvar::magneticToTrue).map(Heading::reciprocalHeading).orElseThrow(IllegalStateException::new);

      //we can't take these intercepts too literally, and we need to go hunting for a place.
      Optional<LatLong> projection = tryNearThisHeading(currentTrue)
          .map(c -> FlatEarthMath.courseInterceptRadial(start, c, nextFix, nextRecip).stream().min(Comparator.comparing(start::distanceInNM)))
          .flatMap(Optional::stream)
          .findFirst();

      Range<Double> alt = currentConstraints(current, previousPathAndTermination.endAltitudeFt());

      return projection.map(p -> PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(p)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alt)
          .lengthNm(p.distanceInNM(start))
          .build());
    }

    private Optional<PathAndTermination> fxIntercept(Leg current, Leg next, PathAndTermination previousPathAndTermination) {
      LatLong start = previousPathAndTermination.endOfLeg();
      MagneticVariation nextMagvar = next.recommendedNavaid()
          .flatMap(Fix::magneticVariation)
          .orElseThrow(IllegalStateException::new);
      MagneticVariation currentMagvar = current.recommendedNavaid()
          .flatMap(Fix::magneticVariation)
          .orElse(nextMagvar);

      double currentTrue = current.outboundMagneticCourse().map(currentMagvar::magneticToTrue).orElseThrow(IllegalStateException::new);

      LatLong nextStart = next.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);

      double nextTrue = next.outboundMagneticCourse().map(nextMagvar::magneticToTrue).orElseThrow(IllegalStateException::new);
      LatLong nextEnd = nextStart.projectOut(nextTrue, 175.0);
      double nextRecip = Heading.reciprocalHeading(nextTrue);

      Optional<LatLong> currentProjection = tryNearThisHeading(currentTrue)
          .map(c -> FlatEarthMath.courseInterceptRadial(start, c, nextEnd, nextRecip).stream().min(Comparator.comparing(start::distanceInNM)))
          .flatMap(Optional::stream)
          .findFirst();

      Range<Double> alt = currentConstraints(current, previousPathAndTermination.endAltitudeFt());

      return currentProjection.map(p -> PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(p)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alt)
          .lengthNm(p.distanceInNM(start))
          .build());
    }

    private Stream<Double> tryNearThisHeading(double currentTrue) {
      return Stream.of(currentTrue, currentTrue - 1, currentTrue + 1, currentTrue + 5, currentTrue - 5);
    }

    /**
     * This just finds where that course intercepts the circle, because the courses often do not intercept the arc.
     *
     * @param current                    the current leg
     * @param af                         the AF leg we are intercepting
     * @param previousPathAndTermination the previous legs data
     * @return the path and termination for this intercept leg.
     */
    private Optional<PathAndTermination> afIntercept(Leg current, Leg af, PathAndTermination previousPathAndTermination) {
      LatLong start = previousPathAndTermination.endOfLeg();
      MagneticVariation magneticVariation = af.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double trueCourse = current.outboundMagneticCourse().map(magneticVariation::magneticToTrue).orElseThrow(IllegalStateException::new);
      double dme = af.rho().orElseThrow(IllegalStateException::new);
      LatLong center = af.recommendedNavaid().map(Fix::latLong).orElseThrow(IllegalStateException::new);

      Range<Double> alts = currentConstraints(current, previousPathAndTermination.endAltitudeFt());

      Optional<LatLong> intercept = tryNearThisHeading(trueCourse)
          .map(c -> FlatEarthMath.courseInterceptDme(start, c, center, dme).stream().min(Comparator.comparing(start::distanceInNM)))
          .flatMap(Optional::stream)
          .findFirst();

      return intercept.map(i -> PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(i)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(start.distanceInNM(i))
          .build());
    }

    private Optional<PathAndTermination> ifTfIntercept(Leg current, Leg ifLeg, Leg tfLeg, PathAndTermination previousPathAndTermination) {
      LatLong start = previousPathAndTermination.endOfLeg();

      LatLong tfFix = tfLeg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      LatLong ifFix = ifLeg.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      double nextPairReverseCourse = tfFix.courseInDegrees(ifFix);

      MagneticVariation currentMagvar = current.recommendedNavaid().flatMap(Fix::magneticVariation)
          .or(() -> ifLeg.associatedFix().flatMap(Fix::magneticVariation))
          .or(() -> Optional.of(ifFix).map(f -> Declinations.approx(f.latitude(), f.longitude())).map(MagneticVariation::ofDegrees))
          .orElseThrow(IllegalStateException::new);
      double currentTrue = current.outboundMagneticCourse()
          .map(currentMagvar::magneticToTrue)
          .orElseThrow(IllegalStateException::new);

      Optional<LatLong> currentProjection = tryNearThisHeading(currentTrue)
          .map(c -> FlatEarthMath.courseInterceptRadial(start, c, tfFix, nextPairReverseCourse).stream().min(Comparator.comparing(start::distanceInNM)))
          .flatMap(Optional::stream)
          .findFirst();

      Range<Double> alt = currentConstraints(current, previousPathAndTermination.endAltitudeFt());

      return currentProjection.map(p -> PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(p)
          .startAltitudeFt(previousPathAndTermination.endAltitudeFt())
          .endAltitudeFt(alt)
          .lengthNm(p.distanceInNM(start))
          .build());
    }

    /**
     * On legs that have nothing to go on we need to just not crash things.
     *
     * @param leg  our leg
     * @param legs all the legs; if there are no fixes in the entire route, things will not go well.
     * @return a give up state.
     */
    private PathAndTermination giveUp(Leg leg, List<Leg> legs) {
      Optional<LatLong> point = legs.stream()
          .map(Leg::associatedFix)
          .flatMap(Optional::stream)
          .findFirst()
          .map(Fix::latLong);
      return PathAndTermination.builder()
          .startOfLeg(point.orElse(null))
          .endOfLeg(point.orElse(null))
          .startAltitudeFt(Range.all())
          .endAltitudeFt(leg.altitudeConstraint())
          .lengthNm(1.0) //assume its a 500' agl ca leg.
          .build();
    }

    /**
     * On legs that have nothing to go on we need to just not crash things.
     *
     * @param leg  our leg
     * @param legs all the legs; if there are no fixes in the entire route, things will not go well.
     * @return a give up state.
     */
    private PathAndTermination giveUpForwards(Leg leg, List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      LatLong point = map.get(legs.get(index - 1)).endOfLeg();
      return PathAndTermination.builder()
          .startOfLeg(point)
          .endOfLeg(point)
          .startAltitudeFt(Range.all())
          .endAltitudeFt(leg.altitudeConstraint())
          .lengthNm(1.0) //assume its a 500' agl ca leg.
          .build();
    }

    private PathAndTermination crVrLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg leg = legs.get(index);
      if (index == 0) {
        return giveUp(leg, legs);
      }

      PathAndTermination previous = map.get(legs.get(index - 1));
      LatLong point = previous.endOfLeg();
      MagneticVariation magvar = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double courseTrue = leg.outboundMagneticCourse().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);

      LatLong navaid = leg.recommendedNavaid().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      double thetaTrue = leg.theta().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);

      LatLong intercept = tryNearThisHeading(courseTrue)
          .map(c -> FlatEarthMath.courseInterceptRadial(point, c, navaid, thetaTrue))
          .flatMap(Optional::stream)
          .findFirst()
          .or(() -> Optional.of(point))
          .orElseThrow(IllegalStateException::new);

      Range<Double> alts = currentConstraints(leg, previous.endAltitudeFt());


      return PathAndTermination.builder()
          .startOfLeg(point)
          .endOfLeg(intercept)
          .startAltitudeFt(previous.endAltitudeFt())
          .endAltitudeFt(alts)
          .lengthNm(point.distanceInNM(intercept))
          .build();
    }

    private PathAndTermination afLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg af = legs.get(index);
      MagneticVariation afMagVar = af.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
      double outboundTrue = af.outboundMagneticCourse().map(afMagVar::magneticToTrue).orElseThrow(IllegalStateException::new);
      double thetaTrue = af.theta().map(afMagVar::magneticToTrue).orElseThrow(IllegalStateException::new);
      double rho = af.rho().orElseThrow(IllegalStateException::new);
      Supplier<Optional<LatLong>> selfDefinedStart = () -> af.recommendedNavaid()
          .map(Fix::latLong)
          .map(i -> i.projectOut(outboundTrue, rho));
      Optional<PathAndTermination> previous = Optional.of(index).filter(i -> i > 0).map(i -> map.get(legs.get(i - 1)));

      LatLong start = previous.map(PathAndTermination::endOfLeg)
          .or(selfDefinedStart)
          .orElseThrow(IllegalStateException::new);
      LatLong end = af.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);

      Range<Double> alts = previous.map(PathAndTermination::startAltitudeFt).orElse(Range.all());

      double arcAngle = ArcAngle.from(outboundTrue, thetaTrue, af.turnDirection().orElseThrow());
      double distance = Spherical.arcLength(rho, arcAngle);


      return PathAndTermination.builder()
          .startOfLeg(start)
          .endOfLeg(end)
          .startAltitudeFt(previous.map(PathAndTermination::startAltitudeFt).orElse(Range.all()))
          .endAltitudeFt(alts)
          .lengthNm(distance)
          .build();
    }

    private PathAndTermination hfLeg(List<Leg> legs, Integer index, Map<Leg, PathAndTermination> map) {
      Leg hf = legs.get(index);
      Optional<PathAndTermination> previous = Optional.of(index).filter(i -> i > 0).map(i -> map.get(legs.get(i - 1)));
      Range<Double> previousAlts = previous.map(PathAndTermination::endAltitudeFt).orElse(Range.all());
      Range<Double> currentAlts = currentConstraints(hf, previousAlts);
      LatLong fix = hf.associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
      Double holdDistance = hf.routeDistance().orElse(4.0); //use a rough number else


      return PathAndTermination.builder()
          .startOfLeg(fix)
          .endOfLeg(fix)
          .startAltitudeFt(previousAlts)
          .endAltitudeFt(currentAlts)
          .lengthNm(holdDistance * 2) //just rough it in and forget the turns .... better than nothing.
          .build();
    }
  }
}
