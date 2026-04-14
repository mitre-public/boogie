package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;

class HeliportAssemblyStrategyTest {

  private static final HeliportAssemblyStrategy<Heliport, Helipad> STRATEGY = HeliportAssemblyStrategy.standard();

  private static final LatLong HELIPORT_POSITION = LatLong.of(40.7484, -73.9967);
  private static final LatLong PAD_POSITION = LatLong.of(40.7490, -73.9970);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-13.0);
  private static final String CYCLE = "2504";

  @Test
  void convertHeliport_basic() {
    ArincHeliport heliport = testHeliport("6N5");
    Helipad pad = Helipad.builder().padIdentifier("H1").origin(PAD_POSITION).build();

    Heliport result = STRATEGY.convertHeliport(heliport, List.of(pad));

    assertAll(
        () -> assertEquals("6N5", result.heliportIdentifier()),
        () -> assertEquals(HELIPORT_POSITION, result.latLong()),
        () -> assertTrue(result.magneticVariation().isPresent()),
        () -> assertEquals(1, result.helipads().size())
    );
  }

  @Test
  void convertHeliport_noHelipads() {
    ArincHeliport heliport = testHeliport("6N5");

    Heliport result = STRATEGY.convertHeliport(heliport, List.of());

    assertAll(
        () -> assertEquals("6N5", result.heliportIdentifier()),
        () -> assertTrue(result.helipads().isEmpty())
    );
  }

  @Test
  void convertHelipad() {
    ArincHelipad pad = testHelipad("H1", PAD_POSITION);

    Helipad result = STRATEGY.convertHelipad(pad);

    assertAll(
        () -> assertEquals("H1", result.padIdentifier()),
        () -> assertEquals(PAD_POSITION, result.origin())
    );
  }

  private static ArincHeliport testHeliport(String identifier) {
    ArincPortInfo portInfo = ArincPortInfo.builder()
        .pointInfo(testPointInfo(identifier, HELIPORT_POSITION))
        .recordInfo(testRecordInfo())
        .build();

    return ArincHeliport.builder()
        .portInfo(portInfo)
        .build();
  }

  private static ArincHelipad testHelipad(String identifier, LatLong position) {
    return ArincHelipad.builder()
        .pointInfo(testPointInfo(identifier, position))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier, LatLong position) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(position)
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
