package org.mitre.tdp.boogie.dafif.utils;

import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.MagneticVariation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Converts a DAFIF course to the magnetic reference used by core legs. */
public final class DafifCourses {

  private static final Logger LOG = LoggerFactory.getLogger(DafifCourses.class);

  private DafifCourses() {}

  /**
   * Grid courses have no magnetic equivalent without the grid reference. True courses require a variation.
   * In either case, preserve the original value in the source record and leave the core course absent.
   */
  public static Optional<Double> magnetic(String course, @Nullable MagneticVariation variation) {
    if (course.endsWith("G")) {
      LOG.warn("Cannot convert DAFIF grid course '{}' without a grid reference", course);
      return Optional.empty();
    }
    if (course.endsWith("T")) {
      if (variation == null) {
        LOG.warn("Cannot convert DAFIF true course '{}' without magnetic variation", course);
      }
      double degrees = Double.parseDouble(course.substring(0, course.length() - 1));
      return Optional.ofNullable(variation).map(value -> ((value.trueToMagnetic(degrees) % 360.0) + 360.0) % 360.0);
    }
    return Optional.of(Double.valueOf(course));
  }
}
