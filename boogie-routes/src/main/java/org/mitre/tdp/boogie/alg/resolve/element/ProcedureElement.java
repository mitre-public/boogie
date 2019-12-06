package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLeg;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.utils.Iterators;

public class ProcedureElement extends ResolvedElement<Procedure> {

  public ProcedureElement(Procedure ref) {
    super(ElementType.PROCEDURE, ref);
  }

  @Override
  public List<LinkedLeg> legs() {
    List<LinkedLeg> legs = new ArrayList<>();
    Collection<Transition> transitions = reference().transitions();

    // TODO - figure out how to link shared fix merge points between transitions
    transitions.forEach((Transition transition) ->
        Iterators.pairwise(transition.legs(), (Leg l1, Leg l2) -> legs.add(new LinkedLeg(l1, l2))));
    return legs;
  }
}
