package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalParentConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifTerminalParentValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifTerminalParentSpec {

  private static final DafifRecordParser parser = DafifRecordParser.all();

  private static final DafifTerminalParentValidator validator = new DafifTerminalParentValidator();

  private static final DafifTerminalParentConverter converter = new DafifTerminalParentConverter();

  public static final String RAW_TERMINAL_PARENT = "AA30079\t1\tADRI1C ADRIV 1C (RNAV)\tTNCA\t\t2021336\t2\tUSN\tCIV\t202112\t\t2500\t4000\tPANS-OPS\t02DEC21\t\t\tN\t\tN\t\tN\t\tW011020\n";

  @Test
  void testParseTerminalParent() {
    DafifRecord record = parser.parse(DafifRecordType.TRM_PAR, RAW_TERMINAL_PARENT).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("terminalProcedureType")),
        () -> assertEquals("ADRI1C ADRIV 1C (RNAV)", record.requiredField("terminalIdentifier")),
        () -> assertEquals("TNCA", record.requiredField("icaoCode")),
        () -> assertFalse(record.optionalField("emergencySafeAltitude").isPresent()),
        () -> assertEquals(Integer.valueOf(2021336), record.requiredField("julianDate")),
        () -> assertEquals("2", record.requiredField("amendmentNumber")),
        () -> assertEquals("USN", record.requiredField("officeOfPrimaryResponsibility")),
        () -> assertEquals("CIV", record.requiredField("hostCountryAuthority")),
        () -> assertEquals(Integer.valueOf(202112), record.requiredField("cycleDate")),
        () -> assertFalse(record.optionalField("alternateTakeoffMinimums").isPresent()),
        () -> assertEquals(Integer.valueOf(2500), record.requiredField("transitionAltitude")),
        () -> assertEquals(Integer.valueOf(4000), record.requiredField("transitionLevel")),
        () -> assertEquals("PANS-OPS", record.requiredField("procedureDesignCriteria")),
        () -> assertEquals("02DEC21", record.requiredField("procedureAmendmentDate")),
        () -> assertFalse(record.optionalField("approachRouteQualifier1").isPresent()),
        () -> assertFalse(record.optionalField("approachRouteQualifier2").isPresent()),
        () -> assertEquals("N", record.requiredField("levelOfService1")),
        () -> assertFalse(record.optionalField("levelOfServiceName1").isPresent()),
        () -> assertEquals("N", record.requiredField("levelOfService2")),
        () -> assertFalse(record.optionalField("levelOfServiceName2").isPresent()),
        () -> assertEquals("N", record.requiredField("levelOfService3")),
        () -> assertFalse(record.optionalField("levelOfServiceName3").isPresent()),
        () -> assertEquals("W011020", record.requiredField("procedureDesignMagvar"))
    );

    assertTrue(validator.test(record));

    DafifTerminalParent terminalParent = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", terminalParent.getAirportIdentification()),
        () -> assertEquals(1, terminalParent.getTerminalProcedureType()),
        () -> assertEquals("ADRI1C ADRIV 1C (RNAV)", terminalParent.getTerminalIdentifier()),
        () -> assertEquals("TNCA", terminalParent.getIcaoCode()),
        () -> assertEquals(2021336, terminalParent.getJulianDate()),
        () -> assertEquals("2", terminalParent.getAmendmentNumber()),
        () -> assertEquals("USN", terminalParent.getOfficeOfPrimaryResponsibility()),
        () -> assertEquals("CIV", terminalParent.getHostCountryAuthority()),
        () -> assertEquals(202112, terminalParent.getCycleDate()),
        () -> assertEquals(2500, terminalParent.getTransitionAltitude()),
        () -> assertEquals(4000, terminalParent.getTransitionLevel()),
        () -> assertEquals("PANS-OPS", terminalParent.getProcedureDesignCriteria()),
        () -> assertEquals("02DEC21", terminalParent.getProcedureAmendmentDate()),
        () -> assertEquals("N", terminalParent.getLevelOfService1()),
        () -> assertEquals("N", terminalParent.getLevelOfService2()),
        () -> assertEquals("N", terminalParent.getLevelOfService3()),
        () -> assertEquals("W011020", terminalParent.getProcedureDesignMagvar())
    );

  }
}
