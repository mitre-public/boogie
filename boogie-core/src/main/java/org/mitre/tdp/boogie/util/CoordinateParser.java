package org.mitre.tdp.boogie.util;

import com.google.common.base.Preconditions;
import org.mitre.caasd.commons.LatLong;

public class CoordinateParser {

  private CoordinateParser() {
    throw new IllegalStateException("Utility Class");
  }

  public static boolean isNegative(String s) {
    return s.equals("S") || s.endsWith("S") || s.equals("W") || s.endsWith("W");
  }

  public static double sign(String s) {
    return isNegative(s) ? -1.0 : 1.0;
  }

  /**
   * Parser for locations in the format DDMM{N,S}/DDDMM{E,W}.
   */
  public static LatLong parse(String s) {
    Preconditions.checkArgument(s.length() == 12);
    String[] latlon = s.split("/");
    String lat = latlon[0];
    String lon = latlon[1];
    double latDeg = Double.parseDouble(lat.substring(0, 2)) + (Double.parseDouble(lat.substring(2, 4)) / 60.0);
    double lonDeg = Double.parseDouble(lon.substring(0, 3)) + (Double.parseDouble(lon.substring(3, 5)) / 60.0);
    return LatLong.of(sign(lat) * latDeg, sign(lon) * lonDeg);
  }

  /**
   * Convert coordinates of the form "DD-MM-SS.XXXd" or "SS.XXX" where
   *    DD are degrees
   *    MM are minutes
   *    SS.XXX are seconds and decimal seconds
   *    d is a direction (i.e., N, S, E, or W)
   *
   * Conversion will FAIL if input is decimal degrees because it will be interpreted as degree seconds.
   * The use case for this comes from parsing NFDC runway coordinates.
   */
  public static Double convertDegrees(String s) {
    s = s.trim();
    if (s.contains("-")) {
      // degrees-minutes-seconds
      String[] parts = s.substring(0, s.length() - 1).split("-");
      if (parts.length < 3) {
        throw new IllegalArgumentException("Input (" + s + ") does not match expected DD-MM-SS.SSSd format");
      }
      double value = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]) / 60.0 +
          Double.parseDouble(parts[2]) / 3600.0;
      return sign(s) * value;
    } else {
      // decimal degrees
      return sign(s) * Double.parseDouble(s.substring(0, s.length() - 1)) / 3600.0;
    }
  }

  /**
   * Reformat coordinate to work with the CoordinateParser
   * @param s a coordinate of the form ddmmssnn[NS] || dddmmssnn[EW]
   * @return a coordinate of the form dd-mm-ss.nn[NS] || ddd-mm-ss.nn[EW]
   */
  public static String reformatLatCoordinate(String s) {
    Preconditions.checkArgument(!(s.endsWith("E") || s.endsWith("W")));
    return s.substring(0, 2) + "-" + s.substring(2, 4) + "-" + s.substring(4, 6) + "." + s.substring(6);
  }

  public static String reformatLonCoordinate(String s) {
    Preconditions.checkArgument(!(s.endsWith("N") || s.endsWith("S")));
    return s.substring(0, 3) + "-" + s.substring(3, 5) + "-" + s.substring(5, 7) + "." + s.substring(7);
  }
}
