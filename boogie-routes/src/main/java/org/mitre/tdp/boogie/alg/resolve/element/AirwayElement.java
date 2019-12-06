package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.List;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLeg;
import org.mitre.tdp.boogie.utils.Iterators;

public class AirwayElement extends ResolvedElement<Airway> {

  public AirwayElement(Airway<?> ref) {
    super(ElementType.AIRWAY, ref);
  }

  @Override
  public List<LinkedLeg> legs() {
    List<LinkedLeg> legs = new ArrayList<>();
    Iterators.pairwise(reference().legs(), (Leg l1, Leg l2) -> {
      legs.add(new LinkedLeg(l1, l2));
      legs.add(new LinkedLeg(l2, l1));
    });
    return legs;
  }
}
