package org.mitre.tdp.boogie.alg.resolve;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mitre.tdp.boogie.Transition;

public class RunwayTransitionFilter implements Predicate<Transition> {

  private static final Pattern RUNWAY_NUMBER = Pattern.compile("(0[1-9]|[1-2]\\d|3[0-6])?");

  private final String runwayId;

  public RunwayTransitionFilter(String runwayId) {
    this.runwayId = runwayId;
  }

  /**
   * Simple regex for common runway ids (00-36)(L,C,R).
   */
  private Optional<String> runwayNumber(String runwayId) {
    Matcher matcher = RUNWAY_NUMBER.matcher(runwayId);
    return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
  }

  @Override
  public boolean test(Transition transition) {
    Optional<String> runwayNumber = runwayNumber(runwayId);
    return transition.identifier().contains(runwayId)
        || runwayId.contains(transition.identifier())
        // SID/STAR transitions serving multiple runways off the same end of the airport typically are tagged with RWY01B as the name for a transition servicing RW01R/L
        || (transition.identifier().endsWith("B") && runwayNumber.filter(transition.identifier()::contains).isPresent());
  }
}
