package org.mitre.tdp.boogie.alg.split;

import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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

  private SectionSplit(Builder bldr) {
    this.value = bldr.value;
    this.etaEet = bldr.etaEet;
    this.index = bldr.index;
    this.wildcards = bldr.wildcards;
  }

  public String value() {
    return value;
  }

  public String etaEet() {
    return etaEet;
  }

  public int index() {
    return index;
  }

  public String wildcards() {
    return wildcards;
  }

  public void setWildcards(String cards) {
    this.wildcards = cards;
  }

  public Builder builder() {
    return new Builder()
        .setValue(value)
        .setEtaEet(etaEet)
        .setIndex(index)
        .setWildcards(wildcards);
  }

  @Override
  public boolean equals(Object that) {
    if (that instanceof SectionSplit) {
      SectionSplit osplit = (SectionSplit) that;
      return Objects.equals(value, osplit.value)
          && Objects.equals(etaEet, osplit.etaEet)
          && Objects.equals(index, osplit.index)
          && Objects.equals(wildcards, osplit.wildcards);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, etaEet, index, wildcards);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  public static class Builder {
    private String value;
    private String etaEet;
    private int index;
    private String wildcards;

    public Builder setValue(String val) {
      this.value = val;
      return this;
    }

    public Builder setEtaEet(String etaEet) {
      this.etaEet = etaEet;
      return this;
    }

    public Builder setIndex(int index) {
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
