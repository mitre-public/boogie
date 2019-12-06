package org.mitre.tdp.boogie.alg.split;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents the cleaned and tagged portion of a route string between two `.`s.
 *
 * e.g. WYNDE8 in FLT.WYNDE8.KORD.
 */
public class SectionSplit {
  /**
   * The alphanumeric string section value.
   *
   * e.g. CTSL6, J121, etc.
   */
  private final String value;
  /**
   * The extracted ETA (Estimated Time of Arrival) or EET (Estimated Enroute Time)
   * if the route was tagged with one.
   */
  private final String etaEet;
  /**
   * The index of the split in the original route string.
   *
   * Determined by String.split("\\.")
   */
  private final int index;
  /**
   * Concatenated string of any non-alphanumeric values associated with the split
   * string section.
   *
   * e.g. "*+"
   */
  private String wildcards;

  SectionSplit(String val, String tau, int idx, String wildcards) {
    this.value = val;
    this.etaEet = tau;
    this.index = idx;
    this.wildcards = wildcards;
  }

  public String value() {
    return value;
  }

  public String etaEet() {
    return etaEet;
  }

  public Duration etaEetDuration() {
    if (StringUtils.isNumeric(etaEet)) {
      long hh = Long.parseLong(etaEet.substring(0, 2));
      long mm = Long.parseLong(etaEet.substring(2, 4));
      return Duration.ofHours(hh).plus(Duration.ofMinutes(mm));
    }
    return null;
  }

  public int index() {
    return index;
  }

  public String wildcards() {
    return wildcards;
  }

  public SectionSplit setWildcards(String wildcards) {
    this.wildcards = wildcards;
    return this;
  }
}
