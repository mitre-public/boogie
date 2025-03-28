package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Singleton class for extracting the numeric portion of a runway identifier.
 */
final class RunwayIdExtractor {

  private static final Pattern RUNWAY_NUMBER = Pattern.compile("(0[1-9]|[1-2]\\d|3[0-6])");

  private static final Pattern PARALLEL_DESIGNATOR = Pattern.compile("[LRC]");

  private RunwayIdExtractor() {
  }

  /**
   * Simple regex for common runway ids (00-36).
   */
  public static Optional<String> runwayNumber(String runwayId) {
    return RUNWAY_NUMBER.matcher(runwayId).results().findFirst().map(MatchResult::group);
  }

  public static Optional<String> parallelDesignator(String runwayId) {
    return PARALLEL_DESIGNATOR.matcher(String.valueOf(runwayId.charAt(runwayId.length() - 1))).results().findFirst().map(MatchResult::group);
  }
}
