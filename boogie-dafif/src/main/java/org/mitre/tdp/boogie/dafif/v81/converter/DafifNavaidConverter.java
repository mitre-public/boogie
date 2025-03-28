package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifNavaidConverter implements Function<DafifRecord, Optional<DafifNavaid>> {

  @Override
  public Optional<DafifNavaid> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    String navaidIdentifier = dafifRecord.requiredField("navaidIdentifier");
    Integer navaidType = dafifRecord.requiredField("navaidType");
    String countryCode = dafifRecord.requiredField("countryCode");
    Integer navaidKeyCode = dafifRecord.requiredField("navaidKeyCode");
    Optional<Integer> stateProvinceCode = dafifRecord.optionalField("stateProvinceCode");
    Optional<String> name = dafifRecord.optionalField("name");
    String icaoRegion = dafifRecord.requiredField("icaoRegion");
    String wac = dafifRecord.requiredField("wAC");
    Optional<String> navaidFrequencyNav = dafifRecord.optionalField("navaidFrequencyNav");
    String navaidUsageCode = dafifRecord.requiredField("navaidUsageCode");
    Optional<String> navaidChannel = dafifRecord.optionalField("navaidChannel");
    String navaidRadioClassCode = dafifRecord.requiredField("navaidRadioClassCode");
    Optional<String> frequencyProtection = dafifRecord.optionalField("frequencyProtection");
    String navaidPower = dafifRecord.requiredField("navaidPower");
    String navaidRange = dafifRecord.requiredField("navaidRange");
    Optional<String> localHorizontalDatum = dafifRecord.optionalField("localHorizontalDatum");
    String geodeticDatum = dafifRecord.requiredField("geodeticDatum");
    Optional<String> geodeticLatitude = dafifRecord.optionalField("geodeticLatitude");
    Optional<Double> degreesLatitude = dafifRecord.optionalField("degreesLatitude");
    Optional<String> geodeticLongitude = dafifRecord.optionalField("geodeticLongitude");
    Optional<Double> degreesLongitude = dafifRecord.optionalField("degreesLongitude");
    Optional<String> navaidSlavedVariation = dafifRecord.optionalField("navaidSlavedVariation");
    String magneticVariation = dafifRecord.requiredField("magneticVariation");
    String ilsNavaidElevation = dafifRecord.requiredField("ilsNavaidElevation");
    Optional<String> dmeGeodeticLatitude = dafifRecord.optionalField("dmeGeodeticLatitude");
    Optional<Double> dmeDegreesLatitude = dafifRecord.optionalField("dmeDegreesLatitude");
    Optional<String> dmeGeodeticLongitude = dafifRecord.optionalField("dmeGeodeticLongitude");
    Optional<Double> dmeDegreesLongitude = dafifRecord.optionalField("dmeDegreesLongitude");
    String dmeElevation = dafifRecord.requiredField("dmeElevation");
    Optional<String> associatedIcaoFaaHostCtryCode = dafifRecord.optionalField("associatedIcaoFaaHostCtryCode");
    String navaidStatus = dafifRecord.requiredField("navaidStatus");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<Integer> coordinatePrecision = dafifRecord.optionalField("coordinatePrecision");

    DafifNavaid dafifNavaid = new DafifNavaid.Builder()
        .navaidIdentifier(navaidIdentifier)
        .navaidType(navaidType)
        .countryCode(countryCode)
        .navaidKeyCode(navaidKeyCode)
        .stateProvinceCode(stateProvinceCode.orElse(null))
        .name(name.orElse(null))
        .icaoRegion(icaoRegion)
        .wac(wac)
        .navaidFrequencyNav(navaidFrequencyNav.orElse(null))
        .navaidUsageCode(navaidUsageCode)
        .navaidChannel(navaidChannel.orElse(null))
        .navaidRadioClassCode(navaidRadioClassCode)
        .frequencyProtection(frequencyProtection.orElse(null))
        .navaidPower(navaidPower)
        .navaidRange(navaidRange)
        .localHorizontalDatum(localHorizontalDatum.orElse(null))
        .geodeticDatum(geodeticDatum)
        .geodeticLatitude(geodeticLatitude.orElse(null))
        .degreesLatitude(degreesLatitude.orElse(null))
        .geodeticLongitude(geodeticLongitude.orElse(null))
        .degreesLongitude(degreesLongitude.orElse(null))
        .navaidSlavedVariation(navaidSlavedVariation.orElse(null))
        .magneticVariation(magneticVariation)
        .ilsNavaidElevation(ilsNavaidElevation)
        .dmeGeodeticLatitude(dmeGeodeticLatitude.orElse(null))
        .dmeDegreesLatitude(dmeDegreesLatitude.orElse(null))
        .dmeGeodeticLongitude(dmeGeodeticLongitude.orElse(null))
        .dmeDegreesLongitude(dmeDegreesLongitude.orElse(null))
        .dmeElevation(dmeElevation)
        .associatedIcaoFaaHostCtryCode(associatedIcaoFaaHostCtryCode.orElse(null))
        .navaidStatus(navaidStatus)
        .cycleDate(cycleDate)
        .coordinatePrecision(coordinatePrecision.orElse(null))
        .build();

    return Optional.of(dafifNavaid);
  }
}
