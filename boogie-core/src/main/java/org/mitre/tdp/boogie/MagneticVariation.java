package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.tdp.boogie.util.Declinations;

/**
 * Interface for object which functionally provide information about the magnetic variation at a location.
 */
public interface MagneticVariation {

  /**
   * The published magnetic variation from source data. This is based on measurements that may have been taken historically or etc. Typically
   * when available this is what is preferentially used in automated systems such as aircraft FMS's, etc.
   */
  Optional<Double> published();

  /**
   * The modeled value for variation typically based on the WMM (World Magnetic Model) published by NOAA and provided in {@link Declinations}.
   */
  double modeled();

  /**
   * Converts the input magnetic course to a true course using the published local variation if provided otherwise defaulting to the modeled
   * value.
   */
  default Double magneticToTrue(Double course) {
    return published().map(published -> course + published).orElse(course + modeled());
  }

  /**
   * Converts the input true course to a magnetic course using the published local variation if provided otherwise defaulting to the modeled
   * value.
   */
  default Double trueToMagnetic(Double course) {
    return published().map(published -> course - published).orElse(course - modeled());
  }
}
