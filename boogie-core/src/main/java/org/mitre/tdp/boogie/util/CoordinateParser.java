package org.mitre.tdp.boogie.util;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.mitre.caasd.commons.LatLong;

import com.google.common.base.Preconditions;

public class CoordinateParser {

  private CoordinateParser() {
    throw new IllegalStateException("Utility Class");
  }

  private static final Predicate<String> validLatLon = Pattern.compile("^[0-9]{4}[NS]/[01]{1}[0-9]{4}[EW]{1}$").asPredicate();

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
   * DD are degrees
   * MM are minutes
   * SS.XXX are seconds and decimal seconds
   * d is a direction (i.e., N, S, E, or W)
   *
   * <p>Conversion will FAIL if input is decimal degrees because it will be interpreted as degree seconds.
   * The use case for this comes from parsing NFDC runway coordinates.
   */
  public static Double convertToDegrees(String s) {
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

  private static final Predicate<String> reformattableLatitudePattern = Pattern.compile("^[0-9]{6,8}[NS]$").asPredicate();

  private static final Predicate<String> reformattableLongitudePattern = Pattern.compile("^[01][0-9]{6,8}[EW]$").asPredicate();

  /**
   * Reformat coordinate to work with the CoordinateParser
   * @param latitude a coordinate of the form ddmmssnn[NS] || dddmmssnn[EW]
   * @return a coordinate of the form dd-mm-ss.nn[NS] || ddd-mm-ss.nn[EW]
   */
  public static Optional<String> reformatLatCoordinate(String latitude) {
    return Optional.ofNullable(latitude)
        .map(String::trim)
        .filter(reformattableLatitudePattern)
        .map(s -> s.substring(0, 2).concat("-").concat(s.substring(2, 4)).concat("-").concat(s.substring(4, 6)).concat(".").concat(s.substring(6)));
  }

  public static Optional<String> reformatLonCoordinate(String longitude) {
    return Optional.ofNullable(longitude)
        .map(String::trim)
        .filter(reformattableLongitudePattern)
        .map(s -> s.substring(0, 3).concat("-").concat(s.substring(3, 5)).concat("-").concat(s.substring(5, 7)).concat(".").concat(s.substring(7)));
  }
}
