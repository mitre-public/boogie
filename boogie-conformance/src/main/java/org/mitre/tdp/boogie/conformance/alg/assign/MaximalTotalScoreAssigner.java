package org.mitre.tdp.boogie.conformance.alg.assign;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.HasTime;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scored;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.DynamicProgrammer;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.TimeBasedScoreMaximizer;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * The maximal total score assigner uses the {@link DynamicProgrammer} determining the path the aircraft intended to
 * take through a collection of legs to be the one which - on the whole - maximizes the overall conformance score as
 * returned by the {@link LegScorer}s.
 *
 * <p>To use this class for {@link LegAssigner} it is required that the full observed path of the aircraft be provided
 * as well as the full collection of legs. This class internally pre-computes the assignment and then serves it up by
 * point.
 *
 * <p>Typically this should be used when comparing the track against a smaller scoped set of legs (due to the intensity
 * of the {@link DynamicProgrammer} computation).
 */
public interface MaximalTotalScoreAssigner extends LegAssigner {

  @Override
  default ConsecutiveLegs assignmentFor(ConformablePoint conformablePoint) {
    return assignmentFor(conformablePoint.time());
  }

  ConsecutiveLegs assignmentFor(Instant time);

  /**
   * Pre-computes the collection of leg assignments based off of the Dynamic programmer's minimization of the total error
   * in CTD between the points and the collection of available legs.
   */
  static MaximalTotalScoreAssigner computeAssignments(
      Collection<ConformablePoint> points,
      Collection<ConsecutiveLegs> legs) {

    HashMap<Instant, ConsecutiveLegs> assignments = new HashMap<>();

    List<ConformablePoint> orderedPoints = points.stream()
        .sorted(Comparator.comparing(HasTime::time))
        .collect(Collectors.toList());

    List<ConsecutiveLegs> legList = new ArrayList<>(legs);

    TimeBasedScoreMaximizer<ConformablePoint, ConsecutiveLegs> maximizer = TimeBasedScoreMaximizer.to(legList);
    List<Scored<ConformablePoint, ConsecutiveLegs>> scored = maximizer.score(orderedPoints);

    scored.forEach(sc -> Arrays.stream(sc.associatedScores().times())
        .forEach(time -> assignments.put(Instant.ofEpochMilli(time), sc.scorable())));

    return assignments::get;
  }
}
