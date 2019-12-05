package org.mitre.tdp.boogie.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mitre.caasd.commons.Spherical;

public class Declinations {

  private static Map<GeomagneticCoefficients, Geomagnetics> magnetics = new HashMap<>();

  private static Geomagnetics magnetics(Instant tau) {
    GeomagneticCoefficients coeffs = GeomagneticCoefficients.coefficientsAtTime(tau);
    Geomagnetics mags = magnetics.get(coeffs);
    if (null == mags) {
      magnetics.put(coeffs, new Geomagnetics(coeffs));
      return magnetics.get(coeffs);
    }
    return mags;
  }

  /**
   * Returns the declination at a position (degrees), elevation (ft), and time.
   *
   * For reference:
   *
   * Bearing + Declination = True Course
   * True Course - Declination = Bearing
   */
  public static double declination(double lat, double lon, Optional<Double> elev, Instant tau) {
    Geomagnetics magnetics = magnetics(tau);

    // include the decimal year to actually get variation within the 20XX-20XX cycle
    ZonedDateTime zdt = ZonedDateTime.ofInstant(tau, ZoneId.of("UTC"));
    GregorianCalendar greg = GregorianCalendar.from(zdt);
    double year = magnetics.decimalYear(greg);

    // if we get an elevation use it as well, otherwise default to 0.0
    // note the Geomagnetics class is expecting km for the vertical datum
    return elev
        // ft -> km
        .map(e -> (e * Spherical.METERS_PER_FOOT) / 1000.0d)
        .map(km -> magnetics.getDeclination(lat, lon, year, km))
        .orElse(magnetics.getDeclination(lat, lon, year, 0.0));
  }
}
