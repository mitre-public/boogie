package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;

public class FixElement extends ResolvedElement<Fix> {

  public FixElement(Fix ref) {
    super(ElementType.FIX, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    SimpleIFLeg<Fix> leg = SimpleIFLeg.from(reference());
    SectionSplitLeg sleg = new SectionSplitLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }
}
