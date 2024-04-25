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

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    String name = dafifRecord.requiredField("name");
    Optional<Integer> stateProvinceCode = dafifRecord.optionalField("stateProvinceCode");
    String icaoCode = dafifRecord.requiredField("icaoCode");
    Optional<String> faaHostCountryIdentifier = dafifRecord.optionalField("faaHostCountryIdentifier");
    Optional<String> localHorizontalDatum = dafifRecord.optionalField("localHorizontalDatum");
    String geodeticDatum = dafifRecord.requiredField("geodeticDatum");
    Optional<String> geodeticLatitude = dafifRecord.optionalField("geodeticLatitude");
    Optional<Double> degreesLatitude = dafifRecord.optionalField("degreesLatitude");
    Optional<String> geodeticLongitude = dafifRecord.optionalField("geodeticLongitude");
    Optional<Double> degreesLongitude = dafifRecord.optionalField("degreesLongitude");
    Integer elevation = dafifRecord.requiredField("elevation");
    String airportType = dafifRecord.requiredField("airportType");
    String mageneticVariation = dafifRecord.requiredField("magneticVariation");
    String wac = dafifRecord.requiredField("wAC");
    Optional<String> beacon = dafifRecord.optionalField("beacon");
    Optional<String> secondaryAirport = dafifRecord.optionalField("secondaryAirport");
    String primaryOperatingAgency = dafifRecord.requiredField("primaryOperatingAgency");
    Optional<String> secondaryName = dafifRecord.optionalField("secondaryName");
    Optional<String> secondaryIcaoCode = dafifRecord.optionalField("secondaryIcaoCode");
    Optional<String> secondaryFaaHost = dafifRecord.optionalField("secondaryFaaHost");
    Optional<String> secondaryOperatingAgency = dafifRecord.optionalField("secondaryOperatingAgency");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> terrainImpacted = dafifRecord.optionalField("terrainImpacted");
    Optional<String> shoreline = dafifRecord.optionalField("shoreline");
    Optional<Integer> coordinatePrecision = dafifRecord.optionalField("coordinatePrecision");
    Optional<String> magVarOfRecord = dafifRecord.optionalField("magneticVariationOfRecord");

    DafifAirport dafifAirport = new DafifAirport.Builder()
        .airportIdentification(airportIdentification)
        .name(name)
        .stateProvinceCode(stateProvinceCode.orElse(null))
        .icaoCode(icaoCode)
        .faaHostCountryIdentifier(faaHostCountryIdentifier.orElse(null))
        .localHorizontalDatum(localHorizontalDatum.orElse(null))
        .geodeticDatum(geodeticDatum)
        .geodeticLatitude(geodeticLatitude.orElse(null))
        .degreesLatitude(degreesLatitude.orElse(null))
        .geodeticLongitude(geodeticLongitude.orElse(null))
        .degreesLongitude(degreesLongitude.orElse(null))
        .elevation(elevation)
        .airportType(airportType)
        .mageneticVariation(mageneticVariation)
        .wac(wac)
        .beacon(beacon.orElse(null))
        .secondaryAirport(secondaryAirport.orElse(null))
        .primaryOperatingAgency(primaryOperatingAgency)
        .secondaryName(secondaryName.orElse(null))
        .secondaryIcaoCode(secondaryIcaoCode.orElse(null))
        .secondaryFaaHost(secondaryFaaHost.orElse(null))
        .secondaryOperatingAgency(secondaryOperatingAgency.orElse(null))
        .cycleDate(cycleDate)
        .terrainImpacted(terrainImpacted.orElse(null))
        .shoreline(shoreline.orElse(null))
        .coordinatePrecision(coordinatePrecision.orElse(null))
        .magVarOfRecord(magVarOfRecord.orElse(null))
        .build();

    return Optional.of(dafifAirport);
  }
}
