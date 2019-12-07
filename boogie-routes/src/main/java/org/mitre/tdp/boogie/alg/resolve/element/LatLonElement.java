package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.SectionHeuristics;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.util.CoordinateParser;

public class LatLonElement extends ResolvedElement<LocationFix> {

  private LatLonElement(LocationFix ref) {
    super(ElementType.LATLON, ref);
  }

  @Override
  public List<LinkedLegs> legs() {
    SimpleTFLeg<LocationFix> leg = SimpleTFLeg.from(reference());
    SectionSplitLeg sleg = new SectionSplitLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }

  public static LatLonElement from(String location) {
    Preconditions.checkArgument(SectionHeuristics.matches(location, SectionHeuristics.latLon()));
    LocationFix floc = new LocationFix(location, CoordinateParser.parse(location));
    return new LatLonElement(floc);
  }
}
