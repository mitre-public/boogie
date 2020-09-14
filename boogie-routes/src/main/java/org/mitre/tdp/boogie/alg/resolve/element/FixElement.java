package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;

public class FixElement extends ResolvedElement<Fix> {

  public FixElement(Fix ref) {
    super(ElementType.FIX, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    FixTerminationLeg leg = FixTerminationLeg.DF(reference());
    GraphableLeg sleg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }
}
