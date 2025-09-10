package org.mitre.tdp.boogie.util;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;

/**
 * This class produces flat earth estimates for those not needing accuracy and wanting speed over spherical.
 */
public final class Equirectangular {
  private static final double EARTH_RADIUS_NM = Spherical.EARTH_RADIUS_NM;
  private Equirectangular() {}

  /**
   * Produces the flat earth distance between two points, which for points up to .5 apart NM is pretty good.
   * @param a place one
   * @param b place two
   * @return the distance in NM
   */
  public static double distanceInNm(HasPosition a, HasPosition b) {
    return distanceInNM(a.latitude(), a.longitude(), b.latitude(), b.longitude());
  }

  /**
   * Produces the flat earth distance between two points, which for points up to .5 apart NM is pretty good.
   * @param a place one
   * @param b place two
   * @return the distance in NM
   */
  public static double distanceInNM(LatLong a, LatLong b) {
    return distanceInNM(a.latitude(), a.longitude(), b.latitude(), b.longitude());
  }

  /**
   * The flat earth distance between two points, which for points up to .5 NM apart is pretty good.
   * @param lat1 place one lat
   * @param lon1 place one lon
   * @param lat2 place two lat
   * @param lon2 place two lon
   * @return the distance
   */
  public static double distanceInNM(double lat1, double lon1, double lat2, double lon2) {
    double lat1Rad = FastMath.toRadians(lat1);
    double lon1Rad = FastMath.toRadians(lon1);
    double lat2Rad = FastMath.toRadians(lat2);
    double lon2Rad = FastMath.toRadians(lon2);

    double deltaLon = lon2Rad - lon1Rad;
    double deltaLat = lat2Rad - lat1Rad;

    double x = deltaLon * FastMath.cos((lat1Rad + lat2Rad) / 2);

    double distanceRad = Math.sqrt(x * x + deltaLat * deltaLat);
    return distanceRad * EARTH_RADIUS_NM;
  }
}
