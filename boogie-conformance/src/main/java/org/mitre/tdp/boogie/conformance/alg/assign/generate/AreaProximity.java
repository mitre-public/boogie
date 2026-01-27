package org.mitre.tdp.boogie.conformance.alg.assign.generate;

import java.util.Objects;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Leg;

/**
 * This is used to define an area of interest and a maximum distance from that area. It's glue between sections.
 * <p>
 * It is in Route Assignment to catch sections of flight that are not covered by {@link Leg} objects.
 * While not a leg, it can be used to construct objects in {@link Leg#make(Object, Function)}
 */
public final class AreaProximity {
  private final LatLong latLong;
  private final double maxDistance;

  public AreaProximity(LatLong latLong, double maxDistance) {
    this.latLong = latLong;
    this.maxDistance = maxDistance;
  }

  public LatLong latLong() {
    return latLong;
  }

  public double maxDistance() {
    return maxDistance;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    AreaProximity that = (AreaProximity) o;
    return Double.compare(maxDistance, that.maxDistance) == 0 && Objects.equals(latLong, that.latLong);
  }

  @Override
  public int hashCode() {
    return Objects.hash(latLong, maxDistance);
  }

  @Override
  public String toString() {
    return "AreaPoximity{" +
        "latLong=" + latLong +
        ", maxDistance=" + maxDistance +
        '}';
  }
}
