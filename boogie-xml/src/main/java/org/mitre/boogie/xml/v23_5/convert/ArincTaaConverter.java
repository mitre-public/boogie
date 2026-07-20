package org.mitre.boogie.xml.v23_5.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincTaaSectorDetails;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincTaa;
import org.mitre.boogie.xml.v23_5.generated.Taa;
import org.mitre.boogie.xml.v23_5.generated.TaaSectorDetails;

final class ArincTaaConverter implements Function<Taa, Optional<ArincTaa>> {
  static final ArincTaaConverter INSTANCE = new ArincTaaConverter();

  private static final ArincTaaValidator VALIDATOR = ArincTaaValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;

  private ArincTaaConverter() {
  }

  @Override
  public Optional<ArincTaa> apply(Taa taa) {
    return Optional.ofNullable(taa)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincTaa convert(Taa taa) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(taa);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(taa);

    List<ArincTaaSectorDetails> sectors = taa.getSectorTaaDetails().stream()
        .map(this::convertSector)
        .toList();

    return ArincTaa.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .approachTypeIdentifier(new ArrayList<>(taa.getApproachTypeIdentifier()))
        .magneticTrueIndicator(Optional.ofNullable(taa.getMagneticTrueIndicator()).map(Enum::name).orElse(null))
        .taaFixPositionIndicator(Optional.ofNullable(taa.getTaaFixPositionIndicator()).map(Enum::name).orElse(null))
        .taaIafWaypointRef(Optional.ofNullable(taa.getTaaIafWaypointRef()).map(Object::toString).orElse(null))
        .sectorBearingWaypointRef(Optional.ofNullable(taa.getSectorBearingWaypointRef()).map(Object::toString).orElse(null))
        .sectorDetails(sectors)
        .build();
  }

  private ArincTaaSectorDetails convertSector(TaaSectorDetails sd) {
    return new ArincTaaSectorDetails(
        sd.getSectorAltitude(),
        sd.getSectorBearingBegin(),
        sd.getSectorBearingEnd(),
        sd.getSectorRadius(),
        sd.getSectorRadiusStart(),
        sd.getSectorRadiusEnd(),
        sd.isProcedureTurn()
    );
  }
}
