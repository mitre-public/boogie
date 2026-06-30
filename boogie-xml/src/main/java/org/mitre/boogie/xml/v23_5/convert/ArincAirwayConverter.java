package org.mitre.boogie.xml.v23_5.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.v23_5.generated.Airway;

public final class ArincAirwayConverter implements Function<Airway, Optional<ArincAirway>> {
  public static final ArincAirwayConverter INSTANCE = new ArincAirwayConverter();

  private static final ArincAirwayValidator VALIDATOR = ArincAirwayValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincAirwayLegConverter LEG_CONVERTER = ArincAirwayLegConverter.INSTANCE;

  private ArincAirwayConverter() {
  }

  @Override
  public Optional<ArincAirway> apply(Airway airway) {
    return Optional.ofNullable(airway)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincAirway convert(Airway airway) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(airway);

    List<ArincAirwayLeg> legs = airway.getAirwayLeg().stream()
        .map(LEG_CONVERTER)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());

    return ArincAirway.builder()
        .baseInfo(baseInfo)
        .identifier(airway.getIdentifier())
        .recordType(Optional.ofNullable(airway.getRecordType()).map(Enum::name).orElse(null))
        .customerCode(airway.getCustomerCode())
        .airwayRouteType(airway.getAirwayRouteType().name())
        .qualifier1(Optional.ofNullable(airway.getQualifier1()).map(Enum::name).orElse(null))
        .qualifier2(Optional.ofNullable(airway.getQualifier2()).map(Enum::name).orElse(null))
        .rnavPbnNavSpec(Optional.ofNullable(airway.getRnavPbnNavSpec()).map(Enum::name).orElse(null))
        .rnpPbnNavSpec(Optional.ofNullable(airway.getRnpPbnNavSpec()).map(Enum::name).orElse(null))
        .legs(legs)
        .build();
  }
}
