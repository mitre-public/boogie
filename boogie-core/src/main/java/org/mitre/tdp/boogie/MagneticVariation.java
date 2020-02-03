package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.tdp.boogie.util.Declinations;

public interface MagneticVariation {

  /**
   * The published magnetic variation from source data. This is based on measurements that may have
   * been taken historically or etc. Typically when available this is what is preferentially used in
   * automated systems such as aircraft FMS's, etc.
   */
  Optional<Float> published();

  /**
   * The modeled value for variation typically based on the WMM (World Magnetic Model) published by
   * NOAA and provided in {@link Declinations}.
   */
  float modeled();

  /**
   * Converts the input magnetic course to a true course using the published local variation if provided
   * otherwise defaulting to the modeled value.
   */
  default Double magneticToTrue(Double course) {
    if (published().isPresent()) {
      return course + published().get();
    }
    return course + modeled();
  }

  /**
   * Converts the input true course to a magnetic course using the published local variation if provided
   * otherwise defaulting to the modeled value.
   */
  default Double trueToMagnetic(Double course) {
    if (published().isPresent()) {
      return course - published().get();
    }
    return course - modeled();
  }
}
