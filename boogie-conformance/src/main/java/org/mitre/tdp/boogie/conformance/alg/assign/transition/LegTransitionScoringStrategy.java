package org.mitre.tdp.boogie.conformance.alg.assign.transition;


import java.util.Optional;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions;

import com.google.common.annotations.Beta;

/**
 * This class contains strategies for scoring different combinations of {@link FlyableLeg}
 */
@Beta
public interface LegTransitionScoringStrategy {
  static LegTransitionScoringStrategy sameLeg()  {
    return new SameLeg();
  }
  static LegTransitionScoringStrategy legsInSequence()  {
    return new LegsInSequence();
  }
  static LegTransitionScoringStrategy sameFixDifferentRoute() {
    return new SameFixDifferentRoute();
  }
  static LegTransitionScoringStrategy linearDistanceBetweenFix()  {
    return new LinearDistanceBetweenFix();
  }

  Optional<Double> score(FlyableLeg currentLeg, FlyableLeg nextLeg);

  /**
   * For all the points from the start of the leg to the end -> they get mapped to the same flyable leg and that's great.
   */
  final class SameLeg implements LegTransitionScoringStrategy {
    private SameLeg() {}

    @Override
    public Optional<Double> score(FlyableLeg currentLeg, FlyableLeg nextLeg) {
      return Optional.of(currentLeg)
          .filter(l -> l.equals(nextLeg))
          .map(p -> .99);
    }
  }

  /**
   * When nearing a new leg, it's great to transition to the next leg as well.
   */
  final class LegsInSequence implements LegTransitionScoringStrategy {
    private LegsInSequence() {}
    @Override
    public Optional<Double> score(FlyableLeg currentLeg, FlyableLeg nextLeg) {
      return nextLeg.previous()
          .filter(p -> p.equals(currentLeg.current()))
          .map(p -> .99);
    }
  }

  /**
   * If the current's next has the same fix as the next's current BUT the routes are different the edge is less likely.
   */
  final class SameFixDifferentRoute implements LegTransitionScoringStrategy {
    private SameFixDifferentRoute() {}
    @Override
    public Optional<Double> score(FlyableLeg currentLeg, FlyableLeg nextLeg) {

      if(currentLeg.next().flatMap(Leg::associatedFix).equals(nextLeg.current().associatedFix())
          && !currentLeg.routes().equals(nextLeg.routes())
      ) {
        return Optional.of(.50); //its still possible but we want to penalize this as its possible to be route jitter.
      }

      return Optional.empty();
    }
  }

  /**
   * If the legs are near each other, then they are more likely.
   */
  final class LinearDistanceBetweenFix implements LegTransitionScoringStrategy {
    private LinearDistanceBetweenFix() {}
    @Override
    public Optional<Double> score(FlyableLeg currentLeg, FlyableLeg nextLeg) {
      if (currentLeg.current().associatedFix().isPresent() &&  nextLeg.current().associatedFix().isPresent() && !currentLeg.routes().equals(nextLeg.routes())) {
        LatLong a = currentLeg.current().associatedFix().get().latLong();
        LatLong b = nextLeg.current().associatedFix().get().latLong();
        Double distance = a.distanceInNM(b);
        Double weight = WeightFunctions.linearBetween(0., .9, 150., .05).apply(distance);
        Double prob = FastMath.max(weight, 0.5);
        return Optional.of(prob);
      }
      return Optional.empty();
    }
  }
}
