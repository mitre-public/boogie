package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.utils.Iterators;

public class ProcedureElement extends ResolvedElement<Procedure> {

  public ProcedureElement(Procedure ref) {
    super(ElementType.PROCEDURE, ref);
  }

  @Override
  public List<LinkedLegs> legs() {
    List<LinkedLegs> legs = new ArrayList<>();
    Collection<Transition> transitions = reference().transitions();

    // TODO - figure out how to link shared fix merge points between transitions
    transitions.forEach((Transition transition) ->
        Iterators.pairwise(transition.legs(), (Leg l1, Leg l2) -> {
          SectionSplitLeg ssl1 = new SectionSplitLeg(l1);
          SectionSplitLeg ssl2 = new SectionSplitLeg(l2);
          legs.add(new LinkedLegs(ssl1, ssl2));
        }));
    return legs;
  }
}
