package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.v23_4.generated.AirportGate;

public final class ArincAirportGateConverter implements Function<AirportGate, Optional<ArincAirportGate>> {
  public static final ArincAirportGateConverter INSTANCE = new ArincAirportGateConverter();

  private static final ArincAirportGateValidator VALIDATOR = ArincAirportGateValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

  private ArincAirportGateConverter() {
  }

  @Override
  public Optional<ArincAirportGate> apply(AirportGate gate) {
    return Optional.ofNullable(gate)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincAirportGate convert(AirportGate gate) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(gate);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(gate);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(gate);

    return ArincAirportGate.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .build();
  }
}
