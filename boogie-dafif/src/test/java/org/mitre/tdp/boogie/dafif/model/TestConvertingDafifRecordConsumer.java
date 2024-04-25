package org.mitre.tdp.boogie.dafif.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifFileParser;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifVersion;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConvertingDafifRecordConsumer {

  private static final File dafifTestAirportFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ARPT.TXT"));
  private static final File dafifTestRunwayFile = new File(System.getProperty("user.dir").concat("/src/test/resources/RWY.TXT"));
  private static final File dafifTestIlsFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ILS.TXT"));

  private static final File dafifTestNavaidFile = new File(System.getProperty("user.dir").concat("/src/test/resources/NAV.TXT"));
  private static final File dafifTestTerminalParentFile = new File(System.getProperty("user.dir").concat("/src/test/resources/TRM_PAR.TXT"));
  private static final File dafifTestTerminalSegmentFile = new File(System.getProperty("user.dir").concat("/src/test/resources/TRM_SEG.TXT"));
  private static final File dafifTestWaypointFile = new File(System.getProperty("user.dir").concat("/src/test/resources/WPT.TXT"));
  private static final File dafifTestAtsFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ATS.TXT"));
  private static final File dafifTestAddRunwayFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ADD_RWY.TXT"));

  private static final DafifFileParser fileParser = new DafifFileParser(DafifRecordParser.all());

  private static final ConvertingDafifRecordConsumer consumer = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);

  @BeforeAll
  static void setup() {
    Collection<DafifRecord> airports = fileParser.apply(dafifTestAirportFile);
    Collection<DafifRecord> runways = fileParser.apply(dafifTestRunwayFile);
    Collection<DafifRecord> ils = fileParser.apply(dafifTestIlsFile);
    Collection<DafifRecord> navaids = fileParser.apply(dafifTestNavaidFile);
    Collection<DafifRecord> terminalParents = fileParser.apply(dafifTestTerminalParentFile);
    Collection<DafifRecord> terminalSegments = fileParser.apply(dafifTestTerminalSegmentFile);
    Collection<DafifRecord> waypoints = fileParser.apply(dafifTestWaypointFile);
    Collection<DafifRecord> ats = fileParser.apply(dafifTestAtsFile);
    Collection<DafifRecord> addRunway = fileParser.apply(dafifTestAddRunwayFile);
    airports.forEach(consumer);
    runways.forEach(consumer);
    ils.forEach(consumer);
    navaids.forEach(consumer);
    terminalParents.forEach(consumer);
    terminalSegments.forEach(consumer);
    waypoints.forEach(consumer);
    ats.forEach(consumer);
    addRunway.forEach(consumer);
  }

  @Test
  void testFileParsing() {
    assertAll(
        () -> assertEquals(9851, consumer.dafifAirports().size()),
        () -> assertEquals(6976, consumer.dafifRunways().size()),
        () -> assertEquals(13189, consumer.dafifIls().size()),
        () -> assertEquals(8369, consumer.dafifNavaids().size()),
        () -> assertEquals(5530, consumer.dafifTerminalParents().size()),
        () -> assertEquals(2319, consumer.dafifTerminalSegments().size()),
        () -> assertEquals(10450, consumer.dafifWaypoints().size()),
        () -> assertEquals(15019, consumer.dafifAts().size()),
        () -> assertEquals(5476, consumer.dafifAddRunways().size())
    );
  }
}
