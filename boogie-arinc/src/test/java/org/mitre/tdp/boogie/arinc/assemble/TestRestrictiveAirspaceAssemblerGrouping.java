package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestRestrictiveAirspaceAssemblerGrouping {

  @Test
  void separatesMultipleCodesAndTheirAltitudeRanges() {
    RestrictiveAirspaceAssembler<Airspace> assembler = RestrictiveAirspaceAssembler.standard();

    List<Airspace> airspaces = assembler.assemble(List.of(
        leg("B", 40, null, null),
        leg("A", 30, null, null),
        leg("B", 20, 18000.0, 23999.0),
        leg("A", 10, 4000.0, 17999.0)
    )).toList();

    Airspace areaA = airspace(airspaces, "BOARDMAN-M-K1-A");
    Airspace areaB = airspace(airspaces, "BOARDMAN-M-K1-B");

    assertAll(
        () -> assertEquals(2, airspaces.size()),
        () -> assertEquals(List.of(10, 30), sequenceNumbers(areaA)),
        () -> assertEquals(List.of(20, 40), sequenceNumbers(areaB)),
        () -> assertEquals(4000.0, areaA.altitudeLimit().lowerEndpoint()),
        () -> assertEquals(17999.0, areaA.altitudeLimit().upperEndpoint()),
        () -> assertEquals(18000.0, areaB.altitudeLimit().lowerEndpoint()),
        () -> assertEquals(23999.0, areaB.altitudeLimit().upperEndpoint())
    );
  }

  private static Airspace airspace(List<Airspace> airspaces, String identifier) {
    return airspaces.stream()
        .filter(airspace -> identifier.equals(airspace.identifier()))
        .findFirst()
        .orElseThrow();
  }

  private static List<Integer> sequenceNumbers(Airspace airspace) {
    return airspace.sequences().stream().map(AirspaceSequence::sequenceNumber).toList();
  }

  private static ArincRestrictiveAirspaceLeg leg(
      String multipleCode,
      int sequenceNumber,
      Double lowerLimit,
      Double upperLimit
  ) {
    return ArincRestrictiveAirspaceLeg.builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.U)
        .subSectionCode("R")
        .icaoCode("K1")
        .restrictiveType("M")
        .restrictiveAirspaceDesignation("BOARDMAN")
        .multipleCode(multipleCode)
        .sequenceNumber(sequenceNumber)
        .continuationRecordNumber("0")
        .boundaryVia(sequenceNumber >= 30 ? BoundaryVia.GE : BoundaryVia.G)
        .lowerLimit(lowerLimit)
        .upperLimit(upperLimit)
        .restrictiveAirspaceName("BOARDMAN MOA")
        .fileRecordNumber(sequenceNumber)
        .cycleDate("2608")
        .build();
  }
}
