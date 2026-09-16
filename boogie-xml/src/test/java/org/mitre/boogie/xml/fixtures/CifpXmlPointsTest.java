package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.EastWest;
import org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure;
import org.mitre.boogie.xml.v23_4.generated.Location;
import org.mitre.boogie.xml.v23_4.generated.NorthSouth;
import org.mitre.boogie.xml.v23_4.generated.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.v23_4.generated.Tacan;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.HeliportConverter;
import org.mitre.tdp.boogie.arinc.v18.HeliportSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class CifpXmlPointsTest {

  @Test
  void groupsHeliportPadsWithoutLosingTheirGeometryOrConflictingMetadata() {
    String line = "SUSAH 20CTK6A   H1   0     NARN N41585008W072232928W013500880         1800018000P    040050M JOHNSON MEML HOSPITAL         730792403";
    var first = ArincRecordParser.standard(new HeliportSpec()).parse(line)
        .flatMap(new HeliportConverter()).orElseThrow();
    var second = first.toBuilder().padIdentifier("H2").latitude(42.0).heliportElevation(881.0)
        .heliportName("SECOND SOURCE NAME").build();
    ConvertedArincRecords records = new ConvertedArincRecords(
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of(first, second));
    var publication = new AeroPublication();
    var refs = new CifpXmlReferences();

    CifpXmlPoints.populate(records, publication, refs);

    assertEquals(1, publication.getHeliports().getHeliport().size());
    var heliport = publication.getHeliports().getHeliport().get(0);
    assertEquals(2, heliport.getHelipad().size());
    var firstPad = heliport.getHelipad().get(0);
    var secondPad = heliport.getHelipad().get(1);
    assertAll(
        () -> assertEquals("JOHNSON MEML HOSPITAL", heliport.getName()),
        () -> assertTrue(heliport.getNotes().stream().anyMatch(note -> note.contains("heliportName") && note.contains("SECOND SOURCE NAME"))),
        () -> assertEquals(880, firstPad.getElevation()),
        () -> assertEquals(881, secondPad.getElevation()),
        () -> assertEquals(41, firstPad.getLocation().getLatitude().getDeg()),
        () -> assertEquals(42, secondPad.getLocation().getLatitude().getDeg()),
        () -> assertEquals(50, firstPad.getHelipadTlofDimensions().getPadLengthLongSide()),
        () -> assertEquals(40, firstPad.getHelipadTlofDimensions().getPadLengthShortSide()),
        () -> assertNotEquals(firstPad.getReferenceId(), secondPad.getReferenceId()),
        () -> assertSame(firstPad, refs.fix("H", "H", "H1", "K6", "20CT")),
        () -> assertSame(secondPad, refs.fix("H", "H", "H2", "K6", "20CT")));
  }

  @Test
  void distinguishesTerminalPointsWhenAnAirportAndHeliportShareTheirIdentifier() {
    var airport = new ArincAirport.Builder().recordType(RecordType.S).sectionCode(SectionCode.P)
        .airportIdentifier("1AK5").airportIcaoRegion("PA").latitude(40.0).longitude(-70.0)
        .airportElevation(10.0).fileRecordNumber(1).lastUpdateCycle("2101").build();
    var heliport = ArincHeliport.builder().recordType(RecordType.S).customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H).subSectionCode("A").heliportIdentifier("1AK5").heliportIcaoRegion("PA")
        .latitude(40.0).longitude(-70.0).heliportElevation(20.0).build();
    var airportFix = new ArincWaypoint.Builder().recordType(RecordType.S).sectionCode(SectionCode.P)
        .terminalSubSectionCode("C").airportIdentifier("1AK5").airportIcaoRegion("PA")
        .waypointIdentifier("SHARE").waypointIcaoRegion("PA").latitude(41.0).longitude(-71.0)
        .fileRecordNumber(2).lastUpdateCycle("2101").build();
    var heliportFix = airportFix.toBuilder().sectionCode(SectionCode.H).build();
    ConvertedArincRecords records = new ConvertedArincRecords(
        List.of(airport), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(airportFix, heliportFix), List.of(), List.of(),
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of(heliport));
    AeroPublication publication = new AeroPublication();
    var refs = new CifpXmlReferences();

    CifpXmlPoints.populate(records, publication, refs);

    var airportWaypoint = publication.getAirports().getAirport().get(0).getTerminalWaypoint().get(0);
    var heliportWaypoint = publication.getHeliports().getHeliport().get(0).getTerminalWaypoint().get(0);
    assertAll(
        () -> assertNotEquals(airportWaypoint.getReferenceId(), heliportWaypoint.getReferenceId()),
        () -> assertSame(airportWaypoint, refs.fix("P", "C", "SHARE", "PA", "1AK5")),
        () -> assertSame(heliportWaypoint, refs.fix("H", "C", "SHARE", "PA", "1AK5")));
  }

  @Test
  void convertsOlderHeliportsWithoutInventingTheLaterHeliportType() {
    String line = "SUSAH 20CTK6A   H1   0     NARN N41585008W072232928W013500880         1800018000P    040050M JOHNSON MEML HOSPITAL         730792403";
    var source = ArincRecordParser.standard(new HeliportSpec()).parse(line)
        .flatMap(new HeliportConverter()).orElseThrow();
    ConvertedArincRecords records = new ConvertedArincRecords(
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of(source));
    AeroPublication publication = new AeroPublication();

    CifpXmlPoints.populate(records, publication, new CifpXmlReferences());

    var heliport = publication.getHeliports().getHeliport().get(0);
    assertAll(
        () -> assertNull(heliport.getHeliportType()),
        () -> assertEquals("20CT", heliport.getIdentifier()),
        () -> assertEquals(880, heliport.getElevation()),
        () -> assertEquals(41, heliport.getLocation().getLatitude().getDeg()),
        () -> assertEquals(EastWest.WEST, heliport.getLocation().getLongitude().getEastWest()));
  }

  @Test
  void keepsVorAndTacanComponentsAndConvertsFrequencyToMegahertz() {
    String line = "SUSAD        ABQ   K2111320VTHW N35023766W106485872ABQ N35023766W106485872E0130057492  423NARALBUQUERQUE                   057582003";
    ArincVhfNavaid source = ArincRecordParser.standard(new VhfNavaidSpec()).parse(line)
        .flatMap(new VhfNavaidConverter()).orElseThrow();
    ConvertedArincRecords records = new ConvertedArincRecords(
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(source), List.of(), List.of(), List.of(),
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of());
    AeroPublication publication = new AeroPublication();
    CifpXmlReferences refs = new CifpXmlReferences();

    CifpXmlPoints.populate(records, publication, refs);

    assertEquals(2, publication.getVhfNavaids().getVhfNavaid().size());
    Vor vor = assertInstanceOf(Vor.class, publication.getVhfNavaids().getVhfNavaid().get(0));
    Tacan tacan = assertInstanceOf(Tacan.class, publication.getVhfNavaids().getVhfNavaid().get(1));
    assertAll(
        () -> assertEquals(113.2, vor.getVorFrequency().getFrequencyValue().doubleValue()),
        () -> assertEquals(FreqUnitOfMeasure.MEGA_HERTZ, vor.getVorFrequency().getFreqUnitOfMeasure()),
        () -> assertEquals(113.2, tacan.getFrequency().getFrequencyValue().doubleValue()),
        () -> assertEquals(5749, tacan.getElevation()),
        () -> assertSame(tacan, vor.getDmeTacanRef()),
        () -> assertNotEquals(vor.getReferenceId(), tacan.getReferenceId()),
        () -> assertSame(vor, refs.fix("D", "", "ABQ", "K2", "")),
        () -> assertEquals(35, vor.getLocation().getLatitude().getDeg()),
        () -> assertEquals(37, vor.getLocation().getLatitude().getSec()),
        () -> assertEquals(66, vor.getLocation().getLatitude().getHSec()),
        () -> assertEquals(EastWest.WEST, vor.getLocation().getLongitude().getEastWest()));
  }

  @Test
  void convertsCoordinatesWithoutSixtySecondOverflow() {
    Location location = CifpXmlPoints.location(-12.999999999, 179.999999999);
    assertAll(
        () -> assertEquals(13, location.getLatitude().getDeg()),
        () -> assertEquals(0, location.getLatitude().getMin()),
        () -> assertEquals(0, location.getLatitude().getSec()),
        () -> assertEquals(0, location.getLatitude().getHSec()),
        () -> assertEquals(NorthSouth.SOUTH, location.getLatitude().getNorthSouth()),
        () -> assertEquals(180, location.getLongitude().getDeg()),
        () -> assertEquals(0, location.getLongitude().getSec()),
        () -> assertEquals(EastWest.EAST, location.getLongitude().getEastWest()));
  }

  @Test
  void preservesRunwayNumberAndSide() {
    var runway = CifpXmlPoints.runwayIdentifier("RW04L");
    assertAll(
        () -> assertEquals(4, runway.getRunwayNumber()),
        () -> assertEquals(RunwayLeftRightCenterType.LEFT, runway.getRunwayLeftRightCenterType())
    );
  }
}
