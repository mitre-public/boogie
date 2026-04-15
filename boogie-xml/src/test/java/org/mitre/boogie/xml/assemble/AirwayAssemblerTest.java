package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.database.FixDatabase;
import org.mitre.boogie.xml.database.FixDatabaseFactory;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;

class AirwayAssemblerTest {

  private static final LatLong JMACK_POS = LatLong.of(42.0, -83.0);
  private static final LatLong CHPPR_POS = LatLong.of(42.5, -83.5);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void assemblesAirwayWithNoLegs() {
    ArincRecords records = ArincRecords.standard();
    FixDatabase<Fix> fixDb = FixDatabaseFactory.standard(records);
    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(fixDb);

    ArincAirway airway = ArincAirway.builder().identifier("J60").airwayRouteType("J").legs(List.of()).build();
    List<Airway> airways = assembler.assemble(List.of(airway)).toList();

    assertEquals(1, airways.size());
    assertEquals("J60", airways.get(0).airwayIdentifier());
    assertTrue(airways.get(0).legs().isEmpty());
  }

  @Test
  void assemblesAirwayWithLegsResolvingFixes() {
    ArincRecords records = ArincRecords.standard()
        .waypoints(Set.of(
            testWaypoint("JMACK", JMACK_POS),
            testWaypoint("CHPPR", CHPPR_POS)));

    ArincAirway airway = ArincAirway.builder()
        .identifier("J60")
        .airwayRouteType("J")
        .legs(List.of(
            ArincAirwayLeg.builder().sequenceNumber(10).fixRef("JMACK").build(),
            ArincAirwayLeg.builder().sequenceNumber(20).fixRef("CHPPR").build()))
        .build();

    FixDatabase<Fix> fixDb = FixDatabaseFactory.standard(records);
    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(fixDb);

    Airway awy = assembler.assemble(List.of(airway)).toList().get(0);
    assertAll(
        () -> assertEquals("J60", awy.airwayIdentifier()),
        () -> assertEquals(2, awy.legs().size()),
        () -> assertTrue(awy.legs().get(0).associatedFix().isPresent()),
        () -> assertEquals("JMACK", awy.legs().get(0).associatedFix().get().fixIdentifier()),
        () -> assertTrue(awy.legs().get(1).associatedFix().isPresent()),
        () -> assertEquals("CHPPR", awy.legs().get(1).associatedFix().get().fixIdentifier())
    );
  }

  @Test
  void assemblesAirwayWithUnresolvedFixes() {
    ArincAirway airway = ArincAirway.builder()
        .identifier("V12")
        .airwayRouteType("V")
        .legs(List.of(
            ArincAirwayLeg.builder().sequenceNumber(10).fixRef("UNKNOWN").build()))
        .build();

    FixDatabase<Fix> fixDb = FixDatabaseFactory.standard(ArincRecords.standard());
    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(fixDb);

    Airway awy = assembler.assemble(List.of(airway)).toList().get(0);
    assertTrue(awy.legs().get(0).associatedFix().isEmpty(), "Unresolved fix should be empty");
  }

  @Test
  void assemblesMultipleAirwaysViaFlatMap() {
    FixDatabase<Fix> fixDb = FixDatabaseFactory.standard(ArincRecords.standard());
    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(fixDb);

    List<ArincAirway> arincAirways = List.of(
        ArincAirway.builder().identifier("J60").airwayRouteType("J").legs(List.of()).build(),
        ArincAirway.builder().identifier("V12").airwayRouteType("V").legs(List.of()).build());

    List<Airway> airways = assembler.assemble(arincAirways).toList();

    assertEquals(2, airways.size());
  }

  private static ArincWaypoint testWaypoint(String identifier, LatLong position) {
    return ArincWaypoint.builder()
        .pointInfo(ArincPointInfo.builder()
            .identifier(identifier)
            .icaoCode("K6")
            .latLong(position)
            .magneticVariation(MAG_VAR)
            .referenceId(identifier)
            .build())
        .recordInfo(ArincRecordInfo.builder()
            .cycleDate(CYCLE)
            .recordType(ArincRecordType.STANDARD)
            .build())
        .waypointType(ArincWaypointType.builder().build())
        .waypointUsage(ArincWaypointUsage.of(false, false, false))
        .build();
  }
}
