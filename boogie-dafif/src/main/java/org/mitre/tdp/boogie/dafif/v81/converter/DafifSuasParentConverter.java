package org.mitre.tdp.boogie.dafif.v81.converter;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;

public final class DafifSuasParentConverter implements Function<DafifRecord, Optional<DafifSuasParent>> {

  @Override
  public Optional<DafifSuasParent> apply(DafifRecord record) {
    requireNonNull(record, "Cannot convert null DafifRecord.");

    return Optional.of(DafifSuasParent.builder()
        .suasIdentification(record.requiredField("suasIdentification"))
        .sector(record.<String>optionalField("sector").orElse(null))
        .specialUseAirspaceType(SpecialUseAirspaceTypeConverter.INSTANCE.apply(record.requiredField("specialUseAirspaceType")))
        .name(record.<String>optionalField("name").orElse(null))
        .icaoCode(record.requiredField("icaoCode"))
        .controllingAuthority(record.<String>optionalField("controllingAuthority").orElse(null))
        .localHorizontalDatum(record.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(record.requiredField("geodeticDatum"))
        .communicationName(record.<String>optionalField("communicationName").orElse(null))
        .communicationsFrequency1(record.<String>optionalField("communicationsFrequency1").orElse(null))
        .communicationsFrequency2(record.<String>optionalField("communicationsFrequency2").orElse(null))
        .level(record.requiredField("level"))
        .upperAltitude(record.requiredField("upperAltitude"))
        .lowerAltitude(record.requiredField("lowerAltitude"))
        .suasEffectiveTimes(record.<String>optionalField("suasEffectiveTimes").orElse(null))
        .suasWeather(record.<String>optionalField("suasWeather").orElse(null))
        .cycleDate(record.requiredField("cycleDate"))
        .effectiveDate(record.requiredField("effectiveDate"))
        .build());
  }
}
