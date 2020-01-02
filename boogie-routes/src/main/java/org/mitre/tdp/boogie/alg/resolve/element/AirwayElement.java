package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.List;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.utils.Iterators;

public class AirwayElement extends ResolvedElement<Airway> {

  public AirwayElement(Airway ref) {
    super(ElementType.AIRWAY, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    List<LinkedLegs> legs = new ArrayList<>();

    Iterators.pairwise(reference().legs(), (l1, l2) -> {
      SectionSplitLeg ssl1 = new SectionSplitLeg(l1);
      SectionSplitLeg ssl2 = new SectionSplitLeg(l2);
      legs.add(new LinkedLegs(ssl1, ssl2));
      legs.add(new LinkedLegs(ssl2, ssl1));
    });

    return legs;
  }
}
