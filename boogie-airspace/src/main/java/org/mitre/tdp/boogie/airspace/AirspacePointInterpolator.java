package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.fn.TriFunction;

import com.google.common.geometry.S2EdgeUtil;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;

/** Interpolates points along the shortest great-circle leg used by {@link AirspaceQuery}. */
public final class AirspacePointInterpolator implements TriFunction<AirspacePoint, AirspacePoint, Double, AirspacePoint> {
  /** Stateless interpolator shared by callers. */
  public static final AirspacePointInterpolator INSTANCE = new AirspacePointInterpolator();

  private AirspacePointInterpolator() {}

  /**
   * Interpolates time and altitude linearly, with position following the great-circle leg.
   * Exact endpoint fractions return the original observation. Interior altitude is null when
   * either observation lacks altitude. A stationary position still permits time and altitude changes.
   *
   * @param start starting observation
   * @param end ending observation
   * @param fraction progress along the leg, from zero to one inclusive
   * @return the original endpoint or an interpolated interior point
   * @throws IllegalArgumentException if the fraction, positions, or present altitudes are invalid,
   *     or if the endpoints are antipodal
   */
  @Override
  public AirspacePoint apply(AirspacePoint start, AirspacePoint end, Double fraction) {
    requireNonNull(start, "start");
    requireNonNull(end, "end");
    requireNonNull(fraction, "fraction");
    checkArgument(Double.isFinite(fraction) && fraction >= 0.0 && fraction <= 1.0,
        "Expected a finite fraction between zero and one");
    checkArgument((start.altitudeFeet() == null || Double.isFinite(start.altitudeFeet()))
        && (end.altitudeFeet() == null || Double.isFinite(end.altitudeFeet())),
        "Present endpoint altitudes must be finite");
    S2Point a = BoundaryCompiler.point(start.position());
    S2Point b = BoundaryCompiler.point(end.position());
    double length = a.angle(b);
    checkArgument(Math.PI - length > 1e-12, "Antipodal endpoints do not define a unique trajectory leg");
    if (fraction == 0.0) {
      return start;
    }
    if (fraction == 1.0) {
      return end;
    }

    LatLong coordinates = start.position();
    if (length > 0.0) {
      S2LatLng position = new S2LatLng(S2EdgeUtil.interpolate(fraction, a, b));
      coordinates = LatLong.of(position.latDegrees(), position.lngDegrees());
    }
    Instant time = start.time().plusNanos(Math.round(Duration.between(start.time(), end.time()).toNanos() * fraction));
    Double altitude = Optional.ofNullable(start.altitudeFeet())
        .flatMap(startAltitude -> Optional.ofNullable(end.altitudeFeet())
            .map(endAltitude -> (1.0 - fraction) * startAltitude + fraction * endAltitude))
        .orElse(null);
    return new AirspacePoint(time, coordinates, altitude);
  }
}
