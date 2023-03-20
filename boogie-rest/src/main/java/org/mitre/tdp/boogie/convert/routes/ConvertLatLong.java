package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ImmutableLatLong;
import org.mitre.tdp.boogie.contract.routes.LatLong;

public final class ConvertLatLong implements Function<org.mitre.caasd.commons.LatLong, LatLong> {
  public static final ConvertLatLong INSTANCE = new ConvertLatLong();

  private ConvertLatLong() {

  }

  @Override
  public LatLong apply(org.mitre.caasd.commons.LatLong latLong) {
    return ImmutableLatLong.builder()
        .latitude(latLong.latitude())
        .longitude(latLong.longitude())
        .build();
  }
}
