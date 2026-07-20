package org.mitre.boogie.xml.v23_5.convert;

import static java.util.Objects.nonNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.util.MagVar;
import org.mitre.boogie.xml.util.MagneticVariationConverter;
import org.mitre.boogie.xml.v23_5.generated.StationDeclination;
import org.mitre.tdp.boogie.MagneticVariation;

final class FlatStationDeclinationConverter implements Function<StationDeclination, MagneticVariation> {
  static final FlatStationDeclinationConverter INSTANCE = new FlatStationDeclinationConverter();

  private static final MagneticVariationConverter MAGVAR_CONVERTER = MagneticVariationConverter.INSTANCE;

  private FlatStationDeclinationConverter() {
  }

  @Override
  public MagneticVariation apply(StationDeclination sd) {
    return Optional.ofNullable(sd)
        .filter(s -> nonNull(s.getStationDeclinationEWT()) && nonNull(s.getStationDeclinationValue()))
        .map(s -> MagVar.from(
            Optional.ofNullable(s.getStationDeclinationEWT()).map(Enum::name).orElseThrow(IllegalStateException::new),
            Optional.ofNullable(s.getStationDeclinationValue()).map(BigDecimal::doubleValue).orElseThrow(IllegalStateException::new)))
        .flatMap(MAGVAR_CONVERTER)
        .orElse(null);
  }
}
