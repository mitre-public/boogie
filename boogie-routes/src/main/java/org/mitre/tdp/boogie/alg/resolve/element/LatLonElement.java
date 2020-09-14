package org.mitre.tdp.boogie.alg.resolve.element;

import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.latLon;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.util.CoordinateParser;

import com.google.common.base.Preconditions;

public class LatLonElement extends ResolvedElement<LocationFix> {

  private LatLonElement(LocationFix ref) {
    super(ElementType.LATLON, ref);
  }

  /**
   * Generates a new LatLonElement from the given string location.
   */
  public static LatLonElement from(String location) {
    Preconditions.checkArgument(location.matches(latLon().pattern()));
    LocationFix floc = new LocationFix(location, CoordinateParser.parse(location));
    return new LatLonElement(floc);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    FixTerminationLeg leg = FixTerminationLeg.DF(reference());
    GraphableLeg sleg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }
}
