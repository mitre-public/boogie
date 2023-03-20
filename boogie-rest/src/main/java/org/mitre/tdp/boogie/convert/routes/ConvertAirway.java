package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.routes.Airway;
import org.mitre.tdp.boogie.contract.routes.ImmutableAirway;

public final class ConvertAirway implements Function<org.mitre.tdp.boogie.Airway, Airway> {
  public static final ConvertAirway INSTANCE = new ConvertAirway();

  public static final ConvertLeg LEG = ConvertLeg.INSTANCE;

  private ConvertAirway() {

  }

  @Override
  public Airway apply(org.mitre.tdp.boogie.Airway airway) {
    return ImmutableAirway.builder()
        .airwayIdentifier(airway.airwayIdentifier())
        .airwayRegion(airway.airwayRegion())
        .legs(airway.legs().stream().map(LEG).collect(Collectors.toList()))
        .build();
  }
}
