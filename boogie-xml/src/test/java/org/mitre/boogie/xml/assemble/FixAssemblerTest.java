package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

class FixAssemblerTest {

  private static final LatLong POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void assemblesWaypoint() {
    FixAssembler<Fix> assembler = FixAssembler.standard();
    Fix fix = assembler.assemble(testWaypoint("JMACK"));

    assertEquals("JMACK", fix.fixIdentifier());
  }

  @Test
  void assemblesNdbNavaid() {
    FixAssembler<Fix> assembler = FixAssembler.standard();
    Fix fix = assembler.assemble(testNdb("DC"));

    assertEquals("DC", fix.fixIdentifier());
  }

  @Test
  void assemblesVhfNavaid() {
    FixAssembler<Fix> assembler = FixAssembler.standard();
    Fix fix = assembler.assemble(testVhf("DTW"));

    assertEquals("DTW", fix.fixIdentifier());
  }

  @Test
  void assemblesGenericFixRecord() {
    FixAssembler<Fix> assembler = FixAssembler.standard();
    ArincAirportGate gate = ArincAirportGate.builder()
        .pointInfo(testPointInfo("A1"))
        .recordInfo(testRecordInfo())
        .build();

    Fix fix = assembler.assemble(gate);

    assertEquals("A1", fix.fixIdentifier());
  }

  @Test
  void assemblerWithStandardStrategy() {
    FixAssembler<Fix> assembler = FixAssembler.withStrategy(FixAssemblyStrategy.standard());

    assertAll(
        () -> assertEquals("JMACK", assembler.assemble(testWaypoint("JMACK")).fixIdentifier()),
        () -> assertEquals("DC", assembler.assemble(testNdb("DC")).fixIdentifier())
    );
  }

  private static ArincWaypoint testWaypoint(String identifier) {
    return ArincWaypoint.builder()
        .pointInfo(testPointInfo(identifier))
        .recordInfo(testRecordInfo())
        .waypointType(ArincWaypointType.builder().build())
        .waypointUsage(ArincWaypointUsage.of(false, false, false))
        .build();
  }

  private static ArincNdbNavaid testNdb(String identifier) {
    return ArincNdbNavaid.builder()
        .pointInfo(testPointInfo(identifier))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincVhfNavaid testVhf(String identifier) {
    return ArincVhfNavaid.builder()
        .pointInfo(testPointInfo(identifier))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(POSITION)
        .magneticVariation(MAG_VAR)
        .referenceId(identifier)
        .build();
  }

  private static ArincRecordInfo testRecordInfo() {
    return ArincRecordInfo.builder()
        .cycleDate(CYCLE)
        .recordType(ArincRecordType.STANDARD)
        .build();
  }
}
