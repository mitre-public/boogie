package org.mitre.tdp.boogie.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.MagneticVariation;

/**
 * At some point in the future this should probably be swapped out with a standard 3rd part tool better able to support modeling
 * both historically and in the future (e.g. OREKIT - WMM + IGRF).
 */
public final class Declinations {

  private Declinations() {
    throw new IllegalStateException("Cannot instantiate static utility class.");
  }

  private static final EnumMap<GeomagneticCoefficients, Geomagnetics> magnetics = new EnumMap<>(GeomagneticCoefficients.class);

  private static Geomagnetics magnetics(Instant tau) {
    GeomagneticCoefficients coeffs = GeomagneticCoefficients.coefficientsAtTime(tau);
    return magnetics.computeIfAbsent(coeffs, Geomagnetics::new);
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
