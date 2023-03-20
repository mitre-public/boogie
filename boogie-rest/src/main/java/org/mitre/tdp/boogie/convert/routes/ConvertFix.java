package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.Fix;
import org.mitre.tdp.boogie.contract.routes.ImmutableFix;

public final class ConvertFix implements Function<org.mitre.tdp.boogie.Fix, Fix> {
  public static final ConvertFix INSTANCE = new ConvertFix();

  private static final ConvertLatLong LAT_LONG = ConvertLatLong.INSTANCE;

  private ConvertFix() {

  }

  @Override
  public Fix apply(org.mitre.tdp.boogie.Fix fix) {
    return ImmutableFix.builder()
        .fixIdentifier(fix.fixIdentifier())
        .fixRegion(fix.fixRegion())
        .publishedVariation(fix.publishedVariation())
        .modeledVariation(fix.modeledVariation())
        .latLong(LAT_LONG.apply(fix.latLong()))
        .elevation(fix.elevation())
        .build();
  }

}
