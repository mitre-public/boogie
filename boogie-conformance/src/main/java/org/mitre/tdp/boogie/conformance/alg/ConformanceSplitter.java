package org.mitre.tdp.boogie.conformance.alg;

import java.util.List;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.LegAssigner;
import org.mitre.tdp.boogie.conformance.alg.evaluate.ConformanceEvaluator;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.fn.Partitioner;

/**
 * Splits a collection of {@link ConformablePoint}s into navigable subsets base on:
 *
 * 1) The {@link LegAssigner} which assigns each point to the best possible candidate leg
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
   *
   */
  default List<List<ConformablePoint>> splits(List<? extends ConformablePoint> points) {
    return points.stream().collect(Partitioner.listByPredicate(this::conforming));
  }

  default boolean conforming(ConformablePoint point) {
    ConsecutiveLegs consecutiveLegs = legAssigner().assignmentFor(point);
    return conformanceEvaluator().conforming(point, consecutiveLegs);
  }

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
