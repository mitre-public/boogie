package org.mitre.tdp.boogie.conformance.alg;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPairImpl;
import org.mitre.tdp.boogie.conformance.alg.assign.LegAssigner;
import org.mitre.tdp.boogie.conformance.alg.evaluate.ConformanceEvaluator;
import org.mitre.tdp.boogie.fn.Partitioner;

/**
 * Splits a collection of {@link ConformablePoint}s into navigable subsets base on:
 *
 * <p>1) The {@link LegAssigner} which assigns each point to the best possible candidate leg.
 * 2) The {@link ConformanceEvaluator} which evaluates the conformance of the aircraft to its' assigned legs.
 */
public interface ConformanceSplitter {

  /**
   * Returns the leg assigner to use for building leg assignments for the splitter.
   */
  LegAssigner legAssigner();

  /**
   * Returns the conformance evaluator to use for splitting the points.
   */
  ConformanceEvaluator conformanceEvaluator();

  /**
   * Generates a list of lists splitting conforming/non-conforming/unknown sections of points.
   */
  default List<List<ConformablePoint>> splits(List<? extends ConformablePoint> points) {
    return points.stream().collect(Partitioner.listByPredicate((p1, p2) -> {
      Optional<Boolean> b1 = conforming(p1);
      Optional<Boolean> b2 = conforming(p1);
      return (!b1.isPresent() && !b2.isPresent()) || (b1.isPresent() && b2.isPresent() && (b1.get().equals(b2.get())));
    }));
  }

  /**
   * Returns a boolean representing whether the point should be considered:
   *
   * 1) If no value is present - unknown - a determination was not able to be made given the leg and point data.
   */
  default Optional<Boolean> conforming(ConformablePoint point) {
    FlyableLeg consecutiveLegs = legAssigner().assignmentFor(point);
    return consecutiveLegs.previous()
        .map(previous -> new LegPairImpl(previous, consecutiveLegs.current())
            .setSourceObject(consecutiveLegs.getSourceObject()))
        .flatMap(legPair -> conformanceEvaluator().conforming(point, legPair));
  }

  /**
   * Creates a new conformance splitter based on the given leg assigner and conformance evaluator.
   */
  static ConformanceSplitter with(LegAssigner assigner, ConformanceEvaluator evaluator) {
    return new ConformanceSplitter() {
      @Override
      public LegAssigner legAssigner() {
        return assigner;
      }

      @Override
      public ConformanceEvaluator conformanceEvaluator() {
        return evaluator;
      }
    };
  }
}
