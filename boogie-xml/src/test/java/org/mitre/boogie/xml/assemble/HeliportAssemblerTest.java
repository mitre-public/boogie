package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;

class HeliportAssemblerTest {

  private static final LatLong HELIPORT_POSITION = LatLong.of(40.7484, -73.9967);
  private static final LatLong PAD_POSITION = LatLong.of(40.7490, -73.9970);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-13.0);
  private static final String CYCLE = "2504";

  @Test
  void assemblesEmptyRecords() {
    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard();
    ArincRecords records = ArincRecords.standard();

    Collection<Heliport> heliports = assembler.assemble(records);

    assertTrue(heliports.isEmpty());
  }

  @Test
  void assemblesHeliportWithNoHelipads() {
    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .heliports(Set.of(testHeliport("6N5", List.of())));

    Collection<Heliport> heliports = assembler.assemble(records);

    assertEquals(1, heliports.size());
    Heliport hp = heliports.iterator().next();
    assertAll(
        () -> assertEquals("6N5", hp.heliportIdentifier()),
        () -> assertTrue(hp.helipads().isEmpty())
    );
  }

  @Test
  void assemblesHeliportWithHelipads() {
    ArincHelipad pad1 = testHelipad("H1", PAD_POSITION);
    ArincHelipad pad2 = testHelipad("H2", LatLong.of(40.7492, -73.9972));

    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .heliports(Set.of(testHeliport("6N5", List.of(pad1, pad2))));

    Collection<Heliport> heliports = assembler.assemble(records);

    assertEquals(1, heliports.size());
    Heliport hp = heliports.iterator().next();
    assertEquals(2, hp.helipads().size());
  }

  @Test
  void assemblesMultipleHeliports() {
    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .heliports(Set.of(
            testHeliport("6N5", List.of()),
            testHeliport("JRA", List.of())
        ));

    Collection<Heliport> heliports = assembler.assemble(records);

    assertEquals(2, heliports.size());
  }

  private static ArincHeliport testHeliport(String identifier, List<ArincHelipad> helipads) {
    ArincPortInfo portInfo = ArincPortInfo.builder()
        .pointInfo(testPointInfo(identifier))
        .recordInfo(testRecordInfo())
        .helipads(helipads)
        .build();

    return ArincHeliport.builder()
        .portInfo(portInfo)
        .build();
  }

  private static ArincHelipad testHelipad(String identifier, LatLong position) {
    return ArincHelipad.builder()
        .pointInfo(ArincPointInfo.builder()
            .identifier(identifier)
            .icaoCode("K6")
            .latLong(position)
            .magneticVariation(MAG_VAR)
            .referenceId(identifier)
            .build())
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(HELIPORT_POSITION)
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
