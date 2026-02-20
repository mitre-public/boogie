package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifTerminalParentConverter implements Function<DafifRecord, Optional<DafifTerminalParent>> {

  @Override
  public Optional<DafifTerminalParent> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    return Optional.of(new DafifTerminalParent.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .terminalProcedureType(dafifRecord.requiredField("terminalProcedureType"))
        .terminalIdentifier(dafifRecord.requiredField("terminalIdentifier"))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .emergencySafeAltitude(dafifRecord.<Integer>optionalField("emergencySafeAltitude").orElse(null))
        .julianDate(dafifRecord.requiredField("julianDate"))
        .amendmentNumber(dafifRecord.<String>optionalField("amendmentNumber").orElse(null))
        .officeOfPrimaryResponsibility(dafifRecord.requiredField("officeOfPrimaryResponsibility"))
        .hostCountryAuthority(dafifRecord.<String>optionalField("hostCountryAuthority").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .alternateTakeoffMinimums(dafifRecord.<String>optionalField("alternateTakeoffMinimums").orElse(null))
        .transitionAltitude(dafifRecord.<Integer>optionalField("transitionAltitude").orElse(null))
        .transitionLevel(dafifRecord.<Integer>optionalField("transitionLevel").orElse(null))
        .procedureDesignCriteria(dafifRecord.<String>optionalField("procedureDesignCriteria").orElse(null))
        .procedureAmendmentDate(dafifRecord.<String>optionalField("procedureAmendmentDate").orElse(null))
        .approachRouteQualifier1(dafifRecord.<String>optionalField("approachRouteQualifier1").orElse(null))
        .approachRouteQualifier2(dafifRecord.<String>optionalField("approachRouteQualifier2").orElse(null))
        .levelOfService1(dafifRecord.requiredField("levelOfService1"))
        .levelOfServiceName1(dafifRecord.<String>optionalField("levelOfServiceName1").orElse(null))
        .levelOfService2(dafifRecord.requiredField("levelOfService2"))
        .levelOfServiceName2(dafifRecord.<String>optionalField("levelOfServiceName2").orElse(null))
        .levelOfService3(dafifRecord.requiredField("levelOfService3"))
        .levelOfServiceName3(dafifRecord.<String>optionalField("levelOfServiceName3").orElse(null))
        .procedureDesignMagvar(dafifRecord.<String>optionalField("procedureDesignMagvar").orElse(null))
        .build());
  }
}
