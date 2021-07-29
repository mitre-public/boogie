package org.mitre.tdp.boogie.alg;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton class for extracting the numeric portion of a runway identifier.
 */
public final class RunwayNumberExtractor {

  public static final RunwayNumberExtractor INSTANCE = new RunwayNumberExtractor();

  private static final Pattern RUNWAY_NUMBER = Pattern.compile("(0[1-9]|[1-2]\\d|3[0-6])");

  private RunwayNumberExtractor() {
  }

  /**
   * Simple regex for common runway ids (00-36).
   */
  public Optional<String> runwayNumber(String runwayId) {
    Matcher matcher = RUNWAY_NUMBER.matcher(runwayId);
    return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
  }
}
