package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifWaypointConverter implements Function<DafifRecord, Optional<DafifWaypoint>> {

  @Override
  public Optional<DafifWaypoint> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    String waypointIdentifierWptIdent = dafifRecord.requiredField("waypointIdentifierWptIdent");
    String countryCode = dafifRecord.requiredField("countryCode");
    Optional<Integer> stateProvinceCode = dafifRecord.optionalField("stateProvinceCode");
    Optional<String> waypointPointNavaidFlag = dafifRecord.optionalField("waypointPointNavaidFlag");
    String waypointType = dafifRecord.requiredField("waypointType");
    Optional<String> waypointDescriptionName = dafifRecord.optionalField("waypointDescriptionName");
    String icaoCode = dafifRecord.requiredField("icaoCode");
    String waypointUsageCode = dafifRecord.requiredField("waypointUsageCode");
    Optional<Double> waypointBearing = dafifRecord.optionalField("waypointBearing");
    Optional<Double> distance = dafifRecord.optionalField("distance");
    String wac = dafifRecord.requiredField("wAC");
    Optional<String> localHorizontalDatum = dafifRecord.optionalField("localHorizontalDatum");
    String geodeticDatum = dafifRecord.requiredField("geodeticDatum");
    Optional<String> geodeticLatitude = dafifRecord.optionalField("geodeticLatitude");
    Optional<Double> degreesLatitude = dafifRecord.optionalField("degreesLatitude");
    Optional<String> geodeticLongitude = dafifRecord.optionalField("geodeticLongitude");
    Optional<Double> degreesLongitude = dafifRecord.optionalField("degreesLongitude");
    String magneticVariation = dafifRecord.requiredField("magneticVariation");
    Optional<String> navaidIdentifier = dafifRecord.optionalField("navaidIdentifier");
    Optional<Integer> navaidType = dafifRecord.optionalField("navaidType");
    Optional<String> navaidCountryCode = dafifRecord.optionalField("navaidCountryCode");
    Optional<Integer> navaidKeyCode = dafifRecord.optionalField("navaidKeyCode");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> waypointRunwayIdent = dafifRecord.optionalField("waypointRunwayIdent");
    Optional<String> waypointRwyIcao = dafifRecord.optionalField("waypointRwyIcao");
    Optional<Integer> coordinatePrecision = dafifRecord.optionalField("coordinatePrecision");

    DafifWaypoint dafifWaypoint = new DafifWaypoint.Builder()
        .waypointIdentifier(waypointIdentifierWptIdent)
        .countryCode(countryCode)
        .stateProvinceCode(stateProvinceCode.orElse(null))
        .waypointPointNavaidFlag(waypointPointNavaidFlag.filter("Y"::equals).isPresent())
        .waypointType(waypointType)
        .waypointDescriptionName(waypointDescriptionName.orElse(null))
        .icaoCode(icaoCode)
        .waypointUsageCode(waypointUsageCode)
        .waypointBearing(waypointBearing.orElse(null))
        .distance(distance.orElse(null))
        .wac(wac)
        .localHorizontalDatum(localHorizontalDatum.orElse(null))
        .geodeticDatum(geodeticDatum)
        .geodeticLatitude(geodeticLatitude.orElse(null))
        .degreesLatitude(degreesLatitude.orElse(null))
        .geodeticLongitude(geodeticLongitude.orElse(null))
        .degreesLongitude(degreesLongitude.orElse(null))
        .magneticVariation(magneticVariation)
        .navaidIdentifier(navaidIdentifier.orElse(null))
        .navaidType(navaidType.orElse(null))
        .navaidCountryCode(navaidCountryCode.orElse(null))
        .navaidKeyCode(navaidKeyCode.orElse(null))
        .cycleDate(cycleDate)
        .waypointRunwayIdent(waypointRunwayIdent.orElse(null))
        .waypointRwyIcao(waypointRwyIcao.orElse(null))
        .coordinatePrecision(coordinatePrecision.orElse(null))
        .build();

    return Optional.of(dafifWaypoint);
  }
}
