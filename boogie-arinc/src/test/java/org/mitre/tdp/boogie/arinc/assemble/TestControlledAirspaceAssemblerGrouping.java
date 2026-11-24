package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.mitre.tdp.boogie.AltitudeLimits;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestControlledAirspaceAssemblerGrouping {

  private static final ControlledAirspaceAssembler<Airspace> ASSEMBLER =
      ControlledAirspaceAssembler.standard(ArincDatabaseFactory.emptyFixDatabase());

  @Test
  void groupsAcrossBlankNamesButSeparatesClassificationLayers() {
    ArincControlledAirspaceLeg classAFirst = leg(10, "TEST AIRSPACE", "A", 1000.0, 2000.0);
    ArincControlledAirspaceLeg classASecond = leg(20, null, "A", null, null);
    ArincControlledAirspaceLeg classCFirst = leg(10, "TEST AIRSPACE", "C", 2000.0, 3000.0);
    ArincControlledAirspaceLeg classCSecond = leg(20, null, "C", null, null);

    List<Airspace> airspaces = ASSEMBLER.assemble(List.of(
        classCSecond,
        classASecond,
        classCFirst,
        classAFirst
    )).toList();

    Airspace classA = airspace(airspaces, "KAAA-M-K1-TEST AIRSPACE-A-A");
    Airspace classC = airspace(airspaces, "KAAA-M-K1-TEST AIRSPACE-A-C");

    assertAll(
        () -> assertEquals(2, airspaces.size()),
        () -> assertEquals(List.of(10, 20), sequenceNumbers(classA)),
        () -> assertEquals(List.of(10, 20), sequenceNumbers(classC)),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.msl(1000.0), AltitudeLimit.msl(2000.0)), classA.altitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.msl(2000.0), AltitudeLimit.msl(3000.0)), classC.altitudeLimits()),
        () -> assertEquals(ControlledAirspaceKey.from(classAFirst), ControlledAirspaceKey.from(classASecond)),
        () -> assertNotEquals(ControlledAirspaceKey.from(classAFirst), ControlledAirspaceKey.from(classCFirst))
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

  private static ArincControlledAirspaceLeg leg(
      int sequenceNumber,
      String name,
      String classification,
      Double lowerLimit,
      Double upperLimit
  ) {
    return new ArincControlledAirspaceLeg.Builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.U)
        .subSectionCode("C")
        .icaoCode("K1")
        .airspaceType(AirspaceType.M)
        .airspaceCenter("KAAA")
        .airspaceClassification(classification)
        .multipleCode("A")
        .sequenceNumber(sequenceNumber)
        .continuationRecordNumber("0")
        .boundaryVia(sequenceNumber == 20 ? BoundaryVia.GE : BoundaryVia.G)
        .lowerLimit(lowerLimit == null ? AltitudeLimit.notSpecified() : AltitudeLimit.msl(lowerLimit))
        .upperLimit(upperLimit == null ? AltitudeLimit.notSpecified() : AltitudeLimit.msl(upperLimit))
        .controlledAirspaceName(name)
        .fileRecordNumber(sequenceNumber)
        .cycleDate("2608")
        .build();
  }
}
