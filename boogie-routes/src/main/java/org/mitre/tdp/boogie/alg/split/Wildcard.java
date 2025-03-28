package org.mitre.tdp.boogie.alg.split;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Enum for known filed route string wildcard modifiers.
 */
public enum Wildcard implements Predicate<String> {
  /**
   * Indicates that the automation system suppressed an AAR, ADR, or ADAR.
   */
  SUPPRESSED("\\*"),
  /**
   * These are associated with military routes, the + indicates to expect multiple flyovers of the given fix.
   *
   * It may also indicate special printing in which case there will be more than one in the route.
   */
  PLUS("\\+"),
  /**
   * Indicates the section between this and the next was omitted a la tailored as the aircraft has already traversed that portion
   * of the route.
   */
  TAILORED("/"),
  /**
   * Indicates the element was filed to direct in the flightplan i.e. if EC is us and EP is the previous element the route was
   * of the format EP..EC
   */
  DIRECT(" "),
  /**
   * Non-specced wildcard characters.
   */
  UNKNOWN("[^a-zA-Z\\d\\s]");

  private final Pattern pattern;

  Wildcard(String match) {
    this.pattern = Pattern.compile(match);
  }

  @Override
  public boolean test(String value) {
    return pattern.asPredicate().test(value);
  }
}
