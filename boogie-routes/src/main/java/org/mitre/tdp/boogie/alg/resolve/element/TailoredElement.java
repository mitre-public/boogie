package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;

public class TailoredElement extends ResolvedElement<Fix> {

  private final String tailored;

  public TailoredElement(String tailored, Fix ref) {
    super(ElementType.TAILORED, ref);
    this.tailored = tailored;
  }

  @Override
  public List<LinkedLegs> legs() {
    Pair<Double, Double> bearingDistance = bearingDistance(tailored);

    double course = convertToTrue(bearingDistance.first());
    double distance = bearingDistance.second();

    LatLong projectedLocation = reference().latLong().projectionInDegrees(course, distance);
    LocationFix asFix = new LocationFix(reference().identifier(), projectedLocation);

    SimpleIFLeg<LocationFix> leg = SimpleIFLeg.from(asFix);
    SectionSplitLeg sleg = new SectionSplitLeg(leg);
    return Collections.singletonList(new LinkedLegs(sleg, sleg));
  }

  /**
   * Catch-all for older input data where the modeled magvar may not have been
   * included and the published may be null.
   */
  private Double convertToTrue(Double bearing) {
    double magvar = reference.magneticVariation().published()
        .orElse(reference.magneticVariation().modeled());
    return bearing + magvar;
  }

  /**
   * Extracts the course and distance from the tailored waypoint definition.
   */
  static Pair<Double, Double> bearingDistance(String tailored) {
    int n = tailored.length();
    Double crs = Double.parseDouble(tailored.substring(n - 6, n - 3));
    Double dist = Double.parseDouble(tailored.substring(n - 3, n));
    return Pair.of(crs, dist);
  }
}
