package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.SectionHeuristics;
import org.mitre.tdp.boogie.models.LinkedLeg;
import org.mitre.tdp.boogie.util.CoordinateParser;

public class LatLonElement extends ResolvedElement<LocationFix> {

  private LatLonElement(LocationFix ref) {
    super(ElementType.LATLON, ref);
  }

  @Override
  public List<LinkedLeg> legs() {
    SimpleTFLeg<LocationFix> leg = SimpleTFLeg.from(reference());
    return Collections.singletonList(new LinkedLeg(leg, leg));
  }

  public static LatLonElement from(String location) {
    Preconditions.checkArgument(SectionHeuristics.matches(location, SectionHeuristics.latLon()));
    LocationFix floc = new LocationFix(location, CoordinateParser.parse(location));
    return new LatLonElement(floc);
  }
}
