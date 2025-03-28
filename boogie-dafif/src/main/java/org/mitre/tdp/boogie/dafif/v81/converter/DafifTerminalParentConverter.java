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

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    Integer terminalProcedureType = dafifRecord.requiredField("terminalProcedureType");
    String terminalIdentifier = dafifRecord.requiredField("terminalIdentifier");
    String icaoCode = dafifRecord.requiredField("icaoCode");
    Optional<Integer> emergencySafeAltitude = dafifRecord.optionalField("emergencySafeAltitude");
    Integer julianDate = dafifRecord.requiredField("julianDate");
    Optional<String> amendmentNumber = dafifRecord.optionalField("amendmentNumber");
    String officeOfPrimaryResponsibility = dafifRecord.requiredField("officeOfPrimaryResponsibility");
    Optional<String> hostCountryAuthority = dafifRecord.optionalField("hostCountryAuthority");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> alternateTakeoffMinimums = dafifRecord.optionalField("alternateTakeoffMinimums");
    Optional<Integer> transitionAltitude = dafifRecord.optionalField("transitionAltitude");
    Optional<Integer> transitionLevel = dafifRecord.optionalField("transitionLevel");
    Optional<String> procedureDesignCriteria = dafifRecord.optionalField("procedureDesignCriteria");
    Optional<String> procedureAmendmentDate = dafifRecord.optionalField("procedureAmendmentDate");
    Optional<String> approachRouteQualifier1 = dafifRecord.optionalField("approachRouteQualifier1");
    Optional<String> approachRouteQualifier2 = dafifRecord.optionalField("approachRouteQualifier2");
    String levelOfService1 = dafifRecord.requiredField("levelOfService1");
    Optional<String> levelOfServiceName1 = dafifRecord.optionalField("levelOfServiceName1");
    String levelOfService2 = dafifRecord.requiredField("levelOfService2");
    Optional<String> levelOfServiceName2 = dafifRecord.optionalField("levelOfServiceName2");
    String levelOfService3 = dafifRecord.requiredField("levelOfService3");
    Optional<String> levelOfServiceName3 = dafifRecord.optionalField("levelOfServiceName3");
    Optional<String> procedureDesignMagvar = dafifRecord.optionalField("procedureDesignMagvar");

    DafifTerminalParent dafifTerminalParent = new DafifTerminalParent.Builder()
        .airportIdentification(airportIdentification)
        .terminalProcedureType(terminalProcedureType)
        .terminalIdentifier(terminalIdentifier)
        .icaoCode(icaoCode)
        .emergencySafeAltitude(emergencySafeAltitude.orElse(null))
        .julianDate(julianDate)
        .amendmentNumber(amendmentNumber.orElse(null))
        .officeOfPrimaryResponsibility(officeOfPrimaryResponsibility)
        .hostCountryAuthority(hostCountryAuthority.orElse(null))
        .cycleDate(cycleDate)
        .alternateTakeoffMinimums(alternateTakeoffMinimums.orElse(null))
        .transitionAltitude(transitionAltitude.orElse(null))
        .transitionLevel(transitionLevel.orElse(null))
        .procedureDesignCriteria(procedureDesignCriteria.orElse(null))
        .procedureAmendmentDate(procedureAmendmentDate.orElse(null))
        .approachRouteQualifier1(approachRouteQualifier1.orElse(null))
        .approachRouteQualifier2(approachRouteQualifier2.orElse(null))
        .levelOfService1(levelOfService1)
        .levelOfServiceName1(levelOfServiceName1.orElse(null))
        .levelOfService2(levelOfService2)
        .levelOfServiceName2(levelOfServiceName2.orElse(null))
        .levelOfService3(levelOfService3)
        .levelOfServiceName3(levelOfServiceName3.orElse(null))
        .procedureDesignMagvar(procedureDesignMagvar.orElse(null))
        .build();

    return Optional.of(dafifTerminalParent);
  }
}
