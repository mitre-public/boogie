package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;

public class AirportElement extends ResolvedElement<Airport> {
  public AirportElement(Airport ref) {
    super(ElementType.AIRPORT, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    SimpleIFLeg leg = SimpleIFLeg.from(reference());
    GraphableLeg splitLeg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(splitLeg, splitLeg));
  }
}
