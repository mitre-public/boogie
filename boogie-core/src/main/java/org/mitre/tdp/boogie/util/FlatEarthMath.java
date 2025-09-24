package org.mitre.tdp.boogie.util;

import java.util.List;
import java.util.Optional;

import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.apache.commons.geometry.euclidean.threed.line.Line3D;
import org.apache.commons.geometry.euclidean.threed.line.Lines3D;
import org.apache.commons.geometry.euclidean.threed.shape.Sphere;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.numbers.core.Precision;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;

/**
 * This class projects coordinates onto flat earths and then provides closed form solutions to a few geometry problems
 * that are useful in finding the start/end of ARINC 424 legs.
 * <p>
 * This library provides estimates, better than 2D projections, but please expect these numbers to be off by ~500-1000 feet
 * for points that are ~100NM apart.
 */
public final class FlatEarthMath {
  private static final double EARTH_RADIUS_NM = Spherical.EARTH_RADIUS_NM;
  private static final Precision.DoubleEquivalence PRECISION = Precision.doubleEquivalenceOfEpsilon(1e-3);

  private FlatEarthMath() {
  }

  /**
   * Produces the flat earth distance between two points, which for points up to .5 apart NM is pretty good.
   *
   * @param a place one
   * @param b place two
   * @return the distance in NM
   */
  public static double distanceInNm(HasPosition a, HasPosition b) {
    return distanceInNM(a.latitude(), a.longitude(), b.latitude(), b.longitude());
  }

  /**
   * Produces the flat earth distance between two points, which for points up to .5 apart NM is pretty good.
   *
   * @param a place one
   * @param b place two
   * @return the distance in NM
   */
  public static double distanceInNM(LatLong a, LatLong b) {
    return distanceInNM(a.latitude(), a.longitude(), b.latitude(), b.longitude());
  }

  /**
   * The flat earth distance between two points, which for points up to .5 NM apart is pretty good.
   *
   * @param lat1 place one lat
   * @param lon1 place one lon
   * @param lat2 place two lat
   * @param lon2 place two lon
   * @return the distance in NM
   */
  public static double distanceInNM(double lat1, double lon1, double lat2, double lon2) {
    double lat1Rad = FastMath.toRadians(lat1);
    double lon1Rad = FastMath.toRadians(lon1);
    double lat2Rad = FastMath.toRadians(lat2);
    double lon2Rad = FastMath.toRadians(lon2);

    double deltaLon = lon2Rad - lon1Rad;
    double deltaLat = lat2Rad - lat1Rad;

    double x = deltaLon * FastMath.cos((lat1Rad + lat2Rad) / 2);

    double distanceRad = FastMath.sqrt(x * x + deltaLat * deltaLat);
    return distanceRad * EARTH_RADIUS_NM;
  }

  /**
   * This method projects the points into 2d space and the evaluates the intercepts between a line and a sphere.
   *
   * @param start          the start of the path
   * @param pathTrueCourse the true course from that start in degrees
   * @param navaid         the center of the circle
   * @param dme            the radius of the circle in degrees
   * @return The first intercept found while traveling from the start input.
   */
  public static Optional<LatLong> courseInterceptDme(LatLong start, Double pathTrueCourse, LatLong navaid, Double dme) {
    LatLong projection = start.projectOut(pathTrueCourse, 10.0);
    Vector3D lineStart = toCartesian(start.latitude(), start.longitude());
    Vector3D lineEnd = toCartesian(projection.latitude(), projection.longitude());
    Vector3D center = toCartesian(navaid.latitude(), navaid.longitude());

    Sphere sphere = Sphere.from(center, dme, PRECISION);
    Line3D line = Lines3D.fromPoints(lineStart, lineEnd, PRECISION);

    List<Vector3D> intercepts = sphere.intersections(line);

    if (intercepts.isEmpty()) {
      return Optional.empty();
    }

    if (intercepts.size() == 1) {
      return intercepts.stream().findFirst().map(FlatEarthMath::toGeographic);
    }

    if (start.distanceInNM(navaid) <= dme) {
      return intercepts.stream().skip(1).findFirst().map(FlatEarthMath::toGeographic);
    }

    return intercepts.stream().findFirst().map(FlatEarthMath::toGeographic);
  }

  /**
   * This uses flat earth projections to calculate the intercepts between two lines.
   *
   * @param start            the starting point for the intercept.
   * @param pathTrueCourse   the true course from that point in degrees
   * @param navaid           the navaid starting the radial.
   * @param radialTrueCourse the true course from that radia in degrees
   * @return the 0-2 intercepts.
   */
  public static Optional<LatLong> courseInterceptRadial(LatLong start, double pathTrueCourse, LatLong navaid, double radialTrueCourse) {
    Vector3D l1 = toCartesian(start.latitude(), start.longitude());
    LatLong projection = start.projectOut(pathTrueCourse, 200.0);
    Vector3D l1a = toCartesian(projection.latitude(), projection.longitude());

    Vector3D radialStart = toCartesian(navaid.latitude(), navaid.longitude());
    LatLong radialProjection = navaid.projectOut(radialTrueCourse, 200.0);
    Vector3D radialEnd = toCartesian(radialProjection.latitude(), radialProjection.longitude());

    Line3D line1 = Lines3D.fromPoints(l1, l1a, PRECISION);
    Line3D line2 = Lines3D.fromPoints(radialStart, radialEnd, PRECISION);

    return Optional.ofNullable(line1.intersection(line2))
        .map(FlatEarthMath::toGeographic);
  }

  /**
   * Converts geographic coordinates (latitude, longitude) to a 3D Cartesian unit vector.
   * The Earth is assumed to be a unit sphere centered at the origin (0,0,0).
   *
   * @param latitudeDegrees  The latitude in degrees.
   * @param longitudeDegrees The longitude in degrees.
   * @return a Vector3D representing the point on the unit sphere
   */
  public static Vector3D toCartesian(double latitudeDegrees, double longitudeDegrees) {
    double lat = FastMath.toRadians(latitudeDegrees);
    double lon = FastMath.toRadians(longitudeDegrees);
    double x = Spherical.EARTH_RADIUS_NM * FastMath.cos(lat) * FastMath.cos(lon);
    double y = Spherical.EARTH_RADIUS_NM * FastMath.cos(lat) * FastMath.sin(lon);
    double z = Spherical.EARTH_RADIUS_NM * FastMath.sin(lat);
    return Vector3D.of(x, y, z);
  }

  /**
   * Converts a 3D Cartesian unit vector back to geographic coordinates (latitude, longitude).
   *
   * @param vector The Vector3D representing the point on the unit sphere.
   * @return an array of two doubles: [latitude, longitude] in degrees.
   */
  public static LatLong toGeographic(Vector3D vector) {
    double x = vector.getX();
    double y = vector.getY();
    double z = vector.getZ();

    double lonRad = FastMath.atan2(y, x);
    double latRad = FastMath.asin(z / Spherical.EARTH_RADIUS_NM);

    double latitudeDegrees = FastMath.toDegrees(latRad);
    double longitudeDegrees = FastMath.toDegrees(lonRad);

    return LatLong.of(latitudeDegrees, longitudeDegrees);
  }
}
