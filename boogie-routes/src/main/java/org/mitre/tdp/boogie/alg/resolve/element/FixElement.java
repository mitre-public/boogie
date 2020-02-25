package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;

public class FixElement extends ResolvedElement<Fix> {

  public FixElement(Fix ref) {
    super(ElementType.FIX, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    SimpleIFLeg leg = SimpleIFLeg.from(reference());
    GraphableLeg sleg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }
}
