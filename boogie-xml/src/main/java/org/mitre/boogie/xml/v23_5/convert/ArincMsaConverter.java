package org.mitre.boogie.xml.v23_5.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincMsa;
import org.mitre.boogie.xml.model.fields.ArincMsaSector;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;
import org.mitre.boogie.xml.v23_5.generated.Msa;
import org.mitre.boogie.xml.v23_5.generated.Sector;

public final class ArincMsaConverter implements Function<Msa, Optional<ArincMsa>> {
  public static final ArincMsaConverter INSTANCE = new ArincMsaConverter();

  private static final ArincMsaValidator VALIDATOR = ArincMsaValidator.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

  private ArincMsaConverter() {
  }

  @Override
  public Optional<ArincMsa> apply(Msa msa) {
    return Optional.ofNullable(msa)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincMsa convert(Msa msa) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(msa);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(msa);

    List<ArincMsaSector> sectors = msa.getSector().stream()
        .map(ArincMsaConverter::convertSector)
        .toList();

    return ArincMsa.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .centerFix(msa.getCenterFix())
        .centerFixRef(Optional.ofNullable(msa.getCenterFixRef()).map(Object::toString).orElse(null))
        .icaoCode(msa.getIcaoCode())
        .magneticTrueIndicator(Optional.ofNullable(msa.getMagneticTrueIndicator()).map(Enum::name).filter(MagneticTrueIndicator.VALID::contains).orElse(null))
        .multipleCode(msa.getMultipleCode())
        .sectors(sectors)
        .build();
  }

  private static ArincMsaSector convertSector(Sector sector) {
    return new ArincMsaSector(
        sector.getSectorAltitude(),
        sector.getSectorBearingBegin(),
        sector.getSectorBearingEnd(),
        sector.getSectorRadius()
    );
  }
}
