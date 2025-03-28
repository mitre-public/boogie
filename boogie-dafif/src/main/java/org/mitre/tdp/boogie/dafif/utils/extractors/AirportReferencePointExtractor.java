package org.mitre.tdp.boogie.dafif.utils.extractors;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;

/**
 * This method takes an airport and
 */
public final class AirportReferencePointExtractor implements Function<DafifAirport, Optional<LatLong>> {
  public static final AirportReferencePointExtractor INSTANCE = new AirportReferencePointExtractor();
  private AirportReferencePointExtractor() {}
  @Override
  public Optional<LatLong> apply(DafifAirport dafifAirport) {
    return Optional.of(dafifAirport)
        .filter(a -> a.degreesLatitude().isPresent() && a.degreesLongitude().isPresent())
        .map(a -> LatLong.of(a.degreesLatitude().get(), a.degreesLongitude().get()));
  }
}
