package org.mitre.tdp.boogie.alg.resolve;

import java.util.Objects;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public class SectionSplitLeg {
  private SectionSplit sectionSplit;
  private Leg<?> leg;

  public SectionSplitLeg(Leg<?> leg) {
    this.leg = leg;
  }

  public SectionSplit sectionSplit() {
    return sectionSplit;
  }

  public SectionSplitLeg setSectionSplit(SectionSplit split) {
    this.sectionSplit = split;
    return this;
  }

  public Leg<?> leg() {
    return leg;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sectionSplit, leg);
  }

  @Override
  public boolean equals(Object that) {
    if (!SectionSplitLeg.class.isAssignableFrom(that.getClass())) {
      return false;
    }
    SectionSplitLeg ssl = (SectionSplitLeg) that;
    return Objects.equals(sectionSplit, ssl.sectionSplit())
        && Objects.equals(leg, ssl.leg());
  }
}
