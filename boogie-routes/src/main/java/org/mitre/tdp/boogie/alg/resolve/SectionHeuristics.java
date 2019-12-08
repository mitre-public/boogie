package org.mitre.tdp.boogie.alg.resolve;

import java.util.regex.Pattern;

/**
 * A set of static heuristic rules for identifying section types by name.
 *
 * Each of these patterns matches only identifiers of that particular infrastructure element type
 * and is guaranteed not to overlap with another. However note that not all of these heuristics
 * are complete in terms of coverage.
 *
 * The two that do cover all cases are the latLon and the tailored heuristics.
 */
public class SectionHeuristics {

  /**
   * Matches 4 letter icao airport codes, will fail of 3 letter FAA codes and alphanumeric identifiers.
   */
  public static Pattern airport() {
    return Pattern.compile("[A-Z]{4}$");
  }

  /**
   * Matches only domestic airways.
   */
  public static Pattern airway() {
    return Pattern.compile("[VJTQ]{1}[0-9]{1,3}$");
  }

  /**
   * Matches any 5 letter waypoint name, will not capture navaids as most are 2-3 letters.
   */
  public static Pattern waypoint() {
    return Pattern.compile("[A-Z]{5}$");
  }

  /**
   * Matches the formatting of any raw latLon coordinates filed in a flight plan.
   */
  public static Pattern latLon() {
    return Pattern.compile("[0-9]{4}[NS]{1}/[01]{1}[0-9]{4}[EW]{1}$");
  }

  /**
   * Matches the tailored waypoint/navaid format in the route string. Note you can file infrastructure
   * elements or even raw latLons post a tailoring indicator without adding the bearing/distance on the
   * end.
   */
  public static Pattern tailored() {
    return Pattern.compile("[A-Z]{2,5}[0-9]{6}$");
  }
}
