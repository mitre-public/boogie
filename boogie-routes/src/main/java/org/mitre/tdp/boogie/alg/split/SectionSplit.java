package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents the cleaned and tagged portion of a route string between two `.`s.
 *
 * <p>e.g. WYNDE8 in FLT.WYNDE8.KORD.
 */
public final class SectionSplit implements Comparable<SectionSplit> {
  /**
   * The alphanumeric string section value.
   *
   * <p>e.g. CTSL6, J121, etc.
   */
  private final String value;
  /**
   * The extracted ETA (Estimated Time of Arrival) or EET (Estimated Enroute Time) if the route was tagged with one.
   */
  private final String etaEet;
  /**
   * On international flight plans a section may have a speed/level change.
   */
  private final String speedLevel;
  /**
   * This field indicates if this point is where flight rules change to this type
   */
  private final String flightRules;
  /**
   * The index of the split in the original route string.
   *
   * <p>Determined by String.split("\\.")
   */
  private final double index;
  /**
   * Concatenated string of any non-alphanumeric values associated with the split string section.
   *
   * <p>e.g. "*+"
   */
  private String wildcards;

  private SectionSplit(Builder bldr) {
    this.value = requireNonNull(bldr.value);
    this.etaEet = bldr.etaEet;
    this.speedLevel = bldr.speedLevel;
    this.flightRules = bldr.flightRules;
    this.index = bldr.index;
    this.wildcards = bldr.wildcards;
  }

  public static SectionSplit.Builder builder() {
    return new SectionSplit.Builder();
  }

  public String value() {
    return value;
  }

  public String etaEet() {
    return etaEet;
  }

  public String speedLevel() {
    return speedLevel;
  }

  public String flightRules() {
    return flightRules;
  }

  public double index() {
    return index;
  }

  public String wildcards() {
    return wildcards;
  }

  public SectionSplit setWildcards(String cards) {
    this.wildcards = cards;
    return this;
  }

  public Builder toBuilder() {
    return new Builder()
        .setValue(value)
        .setEtaEet(etaEet)
        .setSpeedLevel(speedLevel)
        .setFlightRules(flightRules)
        .setIndex(index)
        .setWildcards(wildcards);
  }

  @Override
  public boolean equals(Object that) {
    if (that instanceof SectionSplit) {
      SectionSplit osplit = (SectionSplit) that;
      return Objects.equals(value, osplit.value)
          && Objects.equals(etaEet, osplit.etaEet)
          && Objects.equals(speedLevel, osplit.speedLevel)
          && Objects.equals(flightRules, osplit.flightRules)
          && Objects.equals(index, osplit.index)
          && Objects.equals(wildcards, osplit.wildcards);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, etaEet, speedLevel, flightRules, index, wildcards);
  }

  @Override
  public int compareTo(SectionSplit that) {
    return Comparator.comparingDouble(SectionSplit::index).compare(this, that);
  }

  @Override
  public String toString() {
    return "SectionSplit{" +
        "value='" + value + '\'' +
        ", etaEet='" + etaEet + '\'' +
        ", speedLevel='" + speedLevel + '\'' +
        ", flightRules='" + flightRules + '\'' +
        ", index=" + index +
        ", wildcards='" + wildcards + '\'' +
        '}';
  }

  public static class Builder {
    private String value;
    private String etaEet;
    private String speedLevel;
    private String flightRules;
    private double index;
    private String wildcards;

    public Builder setValue(String val) {
      this.value = val;
      return this;
    }

    public Builder setEtaEet(String etaEet) {
      this.etaEet = etaEet;
      return this;
    }

    public Builder setSpeedLevel(String speedLevel) {
      this.speedLevel = speedLevel;
      return this;
    }

    public Builder setFlightRules(String flightRules) {
      this.flightRules = flightRules;
      return this;
    }

    public Builder setIndex(double index) {
      this.index = index;
      return this;
    }

    public Builder setWildcards(String wildcards) {
      this.wildcards = wildcards;
      return this;
    }

    public SectionSplit build() {
      return new SectionSplit(this);
    }
  }
}
