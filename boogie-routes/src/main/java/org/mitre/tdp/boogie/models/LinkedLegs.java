package org.mitre.tdp.boogie.models;

import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

import static org.mitre.tdp.boogie.utils.Nulls.nonNullEquals;

public class LinkedLegs {

  /**
   * This is the default weight we use when a linked pair of legs come from the same reference section.
   *
   * A non-zero weight is used to keep things deterministic.
   */
  static final double MATCH_WEIGHT = 0.00001;

  private final SectionSplitLeg source;
  private final SectionSplitLeg target;

  /**
   * The identifier of the section if this leg skipped one.
   */
  private SectionSplit skipped;

  public LinkedLegs(SectionSplitLeg s, SectionSplitLeg t) {
    this.source = s;
    this.target = t;
  }

  public SectionSplitLeg source() {
    return source;
  }

  public SectionSplitLeg target() {
    return target;
  }

  public Double linkWeight() {
    return nonNullEquals(source.sectionSplit(), target.sectionSplit())
        || source.leg().pathTerminator() == null
        || target.leg().pathTerminator() == null
        ? MATCH_WEIGHT
        : source.leg().pathTerminator().latLong().distanceInNM(target.leg().pathTerminator().latLong());
  }
}
