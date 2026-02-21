package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.mitre.tdp.boogie.dafif.utils.DafifMagVars;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifWaypointConverter implements Function<DafifRecord, Optional<DafifWaypoint>> {

  @Override
  public Optional<DafifWaypoint> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    return Optional.of(new DafifWaypoint.Builder()
        .waypointIdentifier(dafifRecord.requiredField("waypointIdentifierWptIdent"))
        .countryCode(dafifRecord.requiredField("countryCode"))
        .stateProvinceCode(dafifRecord.<Integer>optionalField("stateProvinceCode").orElse(null))
        .waypointPointNavaidFlag(dafifRecord.<String>optionalField("waypointPointNavaidFlag").filter("Y"::equals).isPresent())
        .waypointType(dafifRecord.requiredField("waypointType"))
        .waypointDescriptionName(dafifRecord.<String>optionalField("waypointDescriptionName").orElse(null))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .waypointUsageCode(dafifRecord.requiredField("waypointUsageCode"))
        .waypointBearing(dafifRecord.<Double>optionalField("waypointBearing").orElse(null))
        .distance(dafifRecord.<Double>optionalField("distance").orElse(null))
        .wac(dafifRecord.requiredField("wAC"))
        .localHorizontalDatum(dafifRecord.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(dafifRecord.requiredField("geodeticDatum"))
        .geodeticLatitude(dafifRecord.<String>optionalField("geodeticLatitude").orElse(null))
        .degreesLatitude(dafifRecord.<Double>optionalField("degreesLatitude").orElse(null))
        .geodeticLongitude(dafifRecord.<String>optionalField("geodeticLongitude").orElse(null))
        .degreesLongitude(dafifRecord.<Double>optionalField("degreesLongitude").orElse(null))
        .magneticVariation(DafifMagVars.fromDynamic(dafifRecord.requiredField("magneticVariation")).angle().inDegrees())
        .navaidIdentifier(dafifRecord.<String>optionalField("navaidIdentifier").orElse(null))
        .navaidType(dafifRecord.<Integer>optionalField("navaidType").orElse(null))
        .navaidCountryCode(dafifRecord.<String>optionalField("navaidCountryCode").orElse(null))
        .navaidKeyCode(dafifRecord.<Integer>optionalField("navaidKeyCode").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .waypointRunwayIdent(dafifRecord.<String>optionalField("waypointRunwayIdent").orElse(null))
        .waypointRwyIcao(dafifRecord.<String>optionalField("waypointRwyIcao").orElse(null))
        .coordinatePrecision(dafifRecord.<Integer>optionalField("coordinatePrecision").orElse(null))
        .build());
  }
}
