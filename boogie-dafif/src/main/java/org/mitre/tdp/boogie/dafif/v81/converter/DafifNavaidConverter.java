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

    return Optional.of(new DafifNavaid.Builder()
        .navaidIdentifier(dafifRecord.requiredField("navaidIdentifier"))
        .navaidType(dafifRecord.requiredField("navaidType"))
        .countryCode(dafifRecord.requiredField("countryCode"))
        .navaidKeyCode(dafifRecord.requiredField("navaidKeyCode"))
        .stateProvinceCode(dafifRecord.<Integer>optionalField("stateProvinceCode").orElse(null))
        .name(dafifRecord.<String>optionalField("name").orElse(null))
        .icaoRegion(dafifRecord.requiredField("icaoRegion"))
        .wac(dafifRecord.requiredField("wAC"))
        .navaidFrequencyNav(dafifRecord.<String>optionalField("navaidFrequencyNav").orElse(null))
        .navaidUsageCode(dafifRecord.requiredField("navaidUsageCode"))
        .navaidChannel(dafifRecord.<String>optionalField("navaidChannel").orElse(null))
        .navaidRadioClassCode(dafifRecord.requiredField("navaidRadioClassCode"))
        .frequencyProtection(dafifRecord.<String>optionalField("frequencyProtection").orElse(null))
        .navaidPower(dafifRecord.requiredField("navaidPower"))
        .navaidRange(dafifRecord.requiredField("navaidRange"))
        .localHorizontalDatum(dafifRecord.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(dafifRecord.requiredField("geodeticDatum"))
        .geodeticLatitude(dafifRecord.<String>optionalField("geodeticLatitude").orElse(null))
        .degreesLatitude(dafifRecord.<Double>optionalField("degreesLatitude").orElse(null))
        .geodeticLongitude(dafifRecord.<String>optionalField("geodeticLongitude").orElse(null))
        .degreesLongitude(dafifRecord.<Double>optionalField("degreesLongitude").orElse(null))
        .navaidSlavedVariation(dafifRecord.<String>optionalField("navaidSlavedVariation").orElse(null))
        .magneticVariation(dafifRecord.requiredField("magneticVariation"))
        .ilsNavaidElevation(dafifRecord.requiredField("ilsNavaidElevation"))
        .dmeGeodeticLatitude(dafifRecord.<String>optionalField("dmeGeodeticLatitude").orElse(null))
        .dmeDegreesLatitude(dafifRecord.<Double>optionalField("dmeDegreesLatitude").orElse(null))
        .dmeGeodeticLongitude(dafifRecord.<String>optionalField("dmeGeodeticLongitude").orElse(null))
        .dmeDegreesLongitude(dafifRecord.<Double>optionalField("dmeDegreesLongitude").orElse(null))
        .dmeElevation(dafifRecord.requiredField("dmeElevation"))
        .associatedIcaoFaaHostCtryCode(dafifRecord.<String>optionalField("associatedIcaoFaaHostCtryCode").orElse(null))
        .navaidStatus(dafifRecord.requiredField("navaidStatus"))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .coordinatePrecision(dafifRecord.<Integer>optionalField("coordinatePrecision").orElse(null))
        .build());
  }
}
