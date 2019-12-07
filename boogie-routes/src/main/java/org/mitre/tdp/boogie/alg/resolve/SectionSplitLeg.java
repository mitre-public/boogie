package org.mitre.tdp.boogie.alg.resolve;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public class SectionSplitLeg {
  private SectionSplit sectionSplit;
  private Leg leg;

  public SectionSplitLeg(Leg leg) {
    this.leg = leg;
  }

  public SectionSplit sectionSplit() {
    return sectionSplit;
  }

  public SectionSplitLeg setSectionSplit(SectionSplit split) {
    this.sectionSplit = split;
    return this;
  }

  public Leg leg() {
    return leg;
  }
}
