package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.FastMath.abs;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.util.Declinations;

/**
 * Interface for object which functionally provide information about the magnetic variation at a location.
 */
public final class MagneticVariation implements Serializable {

  /**
   * The published magnetic variation from source data. This is based on measurements that may have been taken historically or etc.
   * Typically when available this is what is preferentially used in automated systems such as aircraft FMS's, etc.
   */
  private final Double published;
  /**
   * The modeled value of the variation - the idea is this is always present even in the absence of one being published and so can
   * be used as a fallback for magnetic->true course conversions.
   * <br>
   * Typically based on the WMM (World Magnetic Model) published by NOAA and provided in {@link Declinations}.
   */
  private final double modeled;

  /**
   * Serialization...
   */
  private MagneticVariation() {
    this.published = null;
    this.modeled = 0.0;
  }

  public MagneticVariation(@Nullable Double published, double modeled) {
    checkArgument(published == null || abs(published) < 180);
    checkArgument(abs(modeled) < 180);
    this.published = published;
    this.modeled = modeled;
  }

  public Optional<Double> published() {
    return Optional.ofNullable(published);
  }

  public double modeled() {
    return modeled;
  }

  /**
   * Converts the input magnetic course to a true course using the published local variation if provided otherwise defaulting to
   * the modeled value.
   */
  public Double magneticToTrue(Double course) {
    return published().map(published -> course + published).orElse(course + modeled());
  }

  /**
   * Converts the input true course to a magnetic course using the published local variation if provided otherwise defaulting to
   * the modeled value.
   */
  public Double trueToMagnetic(Double course) {
    return published().map(published -> course - published).orElse(course - modeled());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MagneticVariation that = (MagneticVariation) o;
    return Double.compare(that.modeled, modeled) == 0 &&
        Objects.equals(published, that.published);
  }

  @Override
  public int hashCode() {
    return Objects.hash(published, modeled);
  }

  @Override
  public String toString() {
    return "MagneticVariation{" +
        "published=" + published +
        ", modeled=" + modeled +
        '}';
  }
}
