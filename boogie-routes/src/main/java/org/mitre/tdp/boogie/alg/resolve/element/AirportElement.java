package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLeg;

public class AirportElement extends ResolvedElement<Airport> {
  public AirportElement(Airport ref) {
    super(ElementType.AIRPORT, ref);
  }

  @Override
  public List<LinkedLeg> legs() {
    SimpleTFLeg<Airport> leg = SimpleTFLeg.from(reference());
    return Collections.singletonList(new LinkedLeg(leg, leg));
  }
}
