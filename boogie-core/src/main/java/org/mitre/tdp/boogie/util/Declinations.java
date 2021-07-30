package org.mitre.tdp.boogie.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.MagneticVariation;

/**
 * At some point in the future this should probably be swapped out with a standard 3rd part tool better able to support modeling
 * both historically and in the future (e.g. OREKIT - WMM + IGRF).
 */
public final class Declinations {

  private static final Map<GeomagneticCoefficients, Geomagnetics> magnetics = new HashMap<>();

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
   * <br>
   * Elevation may optionally be provided - if it is not the elevation is taken to be sea level (0ft).
   * <br>
   * For reference: Bearing + Declination = True Course, True Course - Declination = Bearing.
   * <br>
   * See {@link MagneticVariation}.
   */
  public static double declination(double lat, double lon, @Nullable Double elev, Instant tau) {
    Geomagnetics magnetics = magnetics(tau);

    // include the decimal year to actually get variation within the 20XX-20XX cycle
    ZonedDateTime zdt = ZonedDateTime.ofInstant(tau, ZoneId.of("UTC"));
    GregorianCalendar greg = GregorianCalendar.from(zdt);
    double year = magnetics.decimalYear(greg);

    // note the Geomagnetics class is expecting km for the vertical datum
    return Optional.ofNullable(elev)
        // ft -> km
        .map(e -> (e * Spherical.METERS_PER_FOOT) / 1000.0d)
        .map(km -> magnetics.getDeclination(lat, lon, year, km))
        .orElse(magnetics.getDeclination(lat, lon, year, 0.0));
  }
}
