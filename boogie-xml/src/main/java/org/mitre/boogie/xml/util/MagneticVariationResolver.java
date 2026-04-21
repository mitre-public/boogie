package org.mitre.boogie.xml.util;

import java.time.Instant;
import java.util.function.BiFunction;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;

/**
 * Resolves the {@link MagneticVariation} for a given {@link ArincPointInfo} and cycle date string.
 *
 * <p>If the point has a published magnetic variation it will be used directly. Otherwise a fallback is computed from
 * the point's position and the provided AIRAC cycle date.
 */
public final class MagneticVariationResolver implements BiFunction<ArincPointInfo, String, MagneticVariation> {

  public static final MagneticVariationResolver INSTANCE = new MagneticVariationResolver();

  private MagneticVariationResolver() {
  }

  @Override
  public MagneticVariation apply(ArincPointInfo point, String cycleDate) {
    return point.magneticVariation()
        .orElseGet(() -> compute(point.latLong(), cycleDate));
  }

  private static MagneticVariation compute(LatLong latLong, String cycleDate) {
    Instant cycleStart = AiracCycle.startDate(cycleDate);
    return MagneticVariation.from(latLong.latitude(), latLong.longitude(), cycleStart);
  }
}
