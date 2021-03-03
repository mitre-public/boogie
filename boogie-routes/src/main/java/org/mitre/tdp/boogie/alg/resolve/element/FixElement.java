package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

public final class FixElement extends ResolvedElement<Fix> {

  private final String wildcards;

  public FixElement(Fix ref, String wildcards) {
    super(ElementType.FIX, ref);
    this.wildcards = wildcards;
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    FixTerminationLeg leg = Wildcard.TAILORED.test(wildcards)
        ? FixTerminationLeg.IF(reference())
        : FixTerminationLeg.DF(reference());

    GraphableLeg sleg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }
}
