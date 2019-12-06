package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLeg;

public class FixElement extends ResolvedElement<Fix> {

  public FixElement(Fix ref) {
    super(ElementType.FIX, ref);
  }

  @Override
  public List<LinkedLeg> legs() {
    SimpleTFLeg<Fix> leg = SimpleTFLeg.from(reference());
    return Collections.singletonList(new LinkedLeg(leg, leg));
  }
}
