package org.mitre.tdp.boogie.models;

import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public class LinkedLegs {
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
}
