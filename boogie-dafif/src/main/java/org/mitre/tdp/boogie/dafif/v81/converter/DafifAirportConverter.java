package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifAirportConverter implements Function<DafifRecord, Optional<DafifAirport>> {

  @Override
  public Optional<DafifAirport> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    return Optional.of(new DafifAirport.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .name(dafifRecord.requiredField("name"))
        .stateProvinceCode(dafifRecord.<Integer>optionalField("stateProvinceCode").orElse(null))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .faaHostCountryIdentifier(dafifRecord.<String>optionalField("faaHostCountryIdentifier").orElse(null))
        .localHorizontalDatum(dafifRecord.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(dafifRecord.requiredField("geodeticDatum"))
        .geodeticLatitude(dafifRecord.<String>optionalField("geodeticLatitude").orElse(null))
        .degreesLatitude(dafifRecord.<Double>optionalField("degreesLatitude").orElse(null))
        .geodeticLongitude(dafifRecord.<String>optionalField("geodeticLongitude").orElse(null))
        .degreesLongitude(dafifRecord.<Double>optionalField("degreesLongitude").orElse(null))
        .elevation(dafifRecord.requiredField("elevation"))
        .airportType(dafifRecord.requiredField("airportType"))
        .mageneticVariation(dafifRecord.requiredField("magneticVariation"))
        .wac(dafifRecord.requiredField("wAC"))
        .beacon(dafifRecord.<String>optionalField("beacon").orElse(null))
        .secondaryAirport(dafifRecord.<String>optionalField("secondaryAirport").orElse(null))
        .primaryOperatingAgency(dafifRecord.requiredField("primaryOperatingAgency"))
        .secondaryName(dafifRecord.<String>optionalField("secondaryName").orElse(null))
        .secondaryIcaoCode(dafifRecord.<String>optionalField("secondaryIcaoCode").orElse(null))
        .secondaryFaaHost(dafifRecord.<String>optionalField("secondaryFaaHost").orElse(null))
        .secondaryOperatingAgency(dafifRecord.<String>optionalField("secondaryOperatingAgency").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .terrainImpacted(dafifRecord.<String>optionalField("terrainImpacted").orElse(null))
        .shoreline(dafifRecord.<String>optionalField("shoreline").orElse(null))
        .coordinatePrecision(dafifRecord.<Integer>optionalField("coordinatePrecision").orElse(null))
        .magVarOfRecord(dafifRecord.<String>optionalField("magneticVariationOfRecord").orElse(null))
        .build());
  }
}
