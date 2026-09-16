package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.xml.bind.JAXBElement;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.A424Base;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airspaces;
import org.mitre.boogie.xml.v23_4.generated.BoundaryVia;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspace;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.FirUirIndicator;
import org.mitre.boogie.xml.v23_4.generated.Level;
import org.mitre.boogie.xml.v23_4.generated.RestrictiveAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.TimeCode;
import org.mitre.boogie.xml.v23_4.generated.UnitIndicator;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;

class DafifXmlAirspacesTest {

  @Test
  void keepsFirAndUirWithTheSameIcaoIdentifierAndDistinctSourceIdentities() {
    var fir = boundary("FIR-1", BoundaryType.FIR).name("Test FIR").level("L").upperAltitude("FL180").build();
    var uir = boundary("UIR-1", BoundaryType.UIR).name("Test UIR").level("H").lowerAltitude("FL180").build();

    var result = boundaries(fir, uir);

    assertTrue(result.getControlledAirspace().isEmpty());
    assertEquals(2, result.getFirUir().size());
    var targetFir = result.getFirUir().get(0);
    var targetUir = result.getFirUir().get(1);
    assertEquals("KAAA", targetFir.getFirUirIdentifier());
    assertEquals("KAAA", targetUir.getFirUirIdentifier());
    assertNotNull(targetFir.getReferenceId());
    assertNotNull(targetUir.getReferenceId());
    assertNotEquals(targetFir.getReferenceId(), targetUir.getReferenceId());
    assertEquals("FIR-1", supplemental(targetFir).get("BDRY_IDENT"));
    assertEquals("UIR-1", supplemental(targetUir).get("BDRY_IDENT"));
    assertEquals(FirUirIndicator.FIR, targetFir.getFirUirIndicator());
    assertEquals(FirUirIndicator.UIR, targetUir.getFirUirIndicator());
    assertEquals("Test FIR", targetFir.getFirUirName());
    assertEquals("Test UIR", targetUir.getFirUirName());
    assertTrue(targetFir.getFirAltitudeLimits().getLowerLimit().isIsGround());
    assertEquals(18000, targetFir.getFirAltitudeLimits().getUpperLimit().getAltitude());
    assertEquals(18000, targetUir.getUirAltitudeLimits().getLowerLimit().getAltitude());
    assertTrue(targetUir.getUirAltitudeLimits().getUpperLimit().isIsUnlimited());
    assertNull(targetFir.getUirAltitudeLimits());
    assertNull(targetUir.getFirAltitudeLimits());
    assertEquals(Level.LOW_ALT, targetFir.getFirUirSegment().get(0).getLevel());
    assertEquals(Level.HIGH_ALT, targetUir.getFirUirSegment().get(0).getLevel());
    assertEquals(BoundaryVia.CIRCLE, targetFir.getFirUirSegment().get(0).getBoundaryVia());
    assertEquals("2601", targetFir.getCycleDate());
  }

  @Test
  void mapsControlledTypesWhileKeepingThePublishedClass() {
    var types = Map.of(
        BoundaryType.CONTROL_AREA, ControlledAirspaceType.CONTROL,
        BoundaryType.OCEAN_CONTROL_AREA, ControlledAirspaceType.CONTROL,
        BoundaryType.CONTROL_ZONE, ControlledAirspaceType.CLASS_D,
        BoundaryType.RADAR_AREA, ControlledAirspaceType.RADAR);
    types.forEach((source, expected) -> {
      var target = controlled(boundary("TEST", source).airspaceClass("D").build());
      assertEquals(expected, target.getControlledAirspaceType(), source.name());
      assertEquals("D", target.getAirspaceClassification());
    });
    var terminalTypes = Map.of("B", ControlledAirspaceType.CLASS_B, "C", ControlledAirspaceType.CLASS_C,
        "D", ControlledAirspaceType.TERMINAL_CONTROL);
    terminalTypes.forEach((airspaceClass, expected) -> {
      var target = controlled(boundary("TEST", BoundaryType.TERMINAL_CONTROL_AREA).airspaceClass(airspaceClass).build());
      assertEquals(expected, target.getControlledAirspaceType(), airspaceClass);
      assertEquals(airspaceClass, target.getAirspaceClassification());
    });
  }

  @Test
  void decodesRnpMantissaAndExponentAndPreservesTheSourceEncoding() {
    var encodings = Map.of(100, "10", 31, "0.3", 152, "0.15");
    encodings.forEach((encoded, expected) -> {
      var target = controlled(boundary("TEST", BoundaryType.CONTROL_AREA).requiredNavPerformance(encoded)
          .name("Test control area").controllingAuthority("Test Center").build());

      assertEquals(new BigDecimal(expected), target.getRnp());
      assertEquals(String.format("%03d", encoded), supplemental(target).get("RNP"));
      assertEquals("Test control area", target.getAirspaceName());
      assertEquals("Test Center", target.getControllingAgency());
      assertEquals(Level.ALL_ALT, target.getAirspaceSegment().get(0).getLevel());
      assertEquals("2601", target.getCycleDate());
    });
  }

  @Test
  void preservesAglMslAndFlightLevelAltitudeReferences() {
    var feet = controlled(boundary("TEST", BoundaryType.CONTROL_AREA)
        .lowerAltitude("00500AGL").upperAltitude("012000AMSL").build());
    var flightLevels = controlled(boundary("TEST", BoundaryType.CONTROL_AREA)
        .lowerAltitude("FL180").upperAltitude("FL450").build());

    assertEquals(500, feet.getAirspaceAltLimits().getLowerLimit().getAltitude());
    assertEquals(12000, feet.getAirspaceAltLimits().getUpperLimit().getAltitude());
    assertEquals(UnitIndicator.AGL, feet.getUnitIndicatorLower());
    assertEquals(UnitIndicator.MSL, feet.getUnitIndicatorUpper());
    assertNull(feet.getAirspaceAltLimits().getLowerLimit().isIsFlightLevel());
    assertNull(feet.getAirspaceAltLimits().getUpperLimit().isIsFlightLevel());
    assertEquals(18000, flightLevels.getAirspaceAltLimits().getLowerLimit().getAltitude());
    assertEquals(45000, flightLevels.getAirspaceAltLimits().getUpperLimit().getAltitude());
    assertEquals(UnitIndicator.MSL, flightLevels.getUnitIndicatorLower());
    assertEquals(UnitIndicator.MSL, flightLevels.getUnitIndicatorUpper());
    assertTrue(flightLevels.getAirspaceAltLimits().getLowerLimit().isIsFlightLevel());
    assertTrue(flightLevels.getAirspaceAltLimits().getUpperLimit().isIsFlightLevel());
  }

  @Test
  void preservesGroundUnlimitedUnknownAndNotamLimitsWithoutInventingAltitudes() {
    for (String ground : List.of("GND", "SURFACE")) {
      var target = controlled(boundary("TEST", BoundaryType.CONTROL_AREA).lowerAltitude(ground).build());
      assertTrue(target.getAirspaceAltLimits().getLowerLimit().isIsGround());
      assertTrue(target.getAirspaceAltLimits().getUpperLimit().isIsUnlimited());
      assertNull(target.getAirspaceAltLimits().getLowerLimit().getAltitude());
      assertNull(target.getAirspaceAltLimits().getUpperLimit().getAltitude());
      assertEquals(UnitIndicator.AGL, target.getUnitIndicatorLower());
      assertNull(target.getUnitIndicatorUpper());
    }
    for (String token : List.of("U", "BY NOTAM")) {
      var target = controlled(boundary("TEST", BoundaryType.CONTROL_AREA).lowerAltitude(token).upperAltitude(token).build());
      var limits = target.getAirspaceAltLimits();
      if ("U".equals(token)) {
        assertTrue(limits.getLowerLimit().isIsUnknown());
        assertTrue(limits.getUpperLimit().isIsUnknown());
      } else {
        assertTrue(limits.getLowerLimit().isIsNotam());
        assertTrue(limits.getUpperLimit().isIsNotam());
      }
      assertNull(limits.getLowerLimit().getAltitude());
      assertNull(limits.getUpperLimit().getAltitude());
      assertNull(target.getUnitIndicatorLower());
      assertNull(target.getUnitIndicatorUpper());
      assertEquals(token, supplemental(target).get("LOWER_ALT"));
      assertEquals(token, supplemental(target).get("UPPER_ALT"));
    }
  }

  @Test
  void retainsFirAglLimitsAsSourceDataBecauseFirLimitsCannotExpressAgl() {
    var target = boundaries(boundary("TEST", BoundaryType.FIR)
        .lowerAltitude("00500AGL").upperAltitude("012000AGL").build()).getFirUir().get(0);

    assertNull(target.getFirAltitudeLimits().getLowerLimit());
    assertNull(target.getFirAltitudeLimits().getUpperLimit());
    assertEquals("00500AGL", supplemental(target).get("LOWER_ALT"));
    assertEquals("012000AGL", supplemental(target).get("UPPER_ALT"));
  }

  @Test
  void groupsSpecialUseGeometryByIdentifierAndSectorIncludingAbsentAndTwoLetterSectors() {
    var parents = List.of(suas(null).build(), suas("A").build(), suas("AB").build(), suas("C").build());
    var publication = new AeroPublication();
    DafifXmlAirspaces.populate(List.of(), List.of(), parents,
        List.of(suasCircle("AB", 3), suasCircle(null, 1), suasCircle("A", 2)), publication);

    var result = publication.getAirspaces().getRestrictiveAirspace();
    assertEquals(3, result.size());
    for (int index = 0; index < result.size(); index++) {
      var target = result.get(index);
      assertEquals("R-TEST", target.getRestrictiveAirspaceDesignation());
      assertEquals("KA", target.getIcaoCode());
      assertEquals(RestrictiveAirspaceType.RESTRICTED, target.getRestrictiveAirspaceType());
      assertEquals(1, target.getAirspaceSegment().size());
      assertEquals(index + 1.0, target.getAirspaceSegment().get(0).getArcDistance().doubleValue());
    }
    assertNull(result.get(0).getMultipleCode());
    assertEquals("A", result.get(1).getMultipleCode());
    assertNull(result.get(2).getMultipleCode());
    assertEquals("AB", supplemental(result.get(2)).get("SECTOR"));
    assertEquals("KAAA", supplemental(result.get(2)).get("ICAO"));
  }

  @Test
  void keepsTemporaryReservedTypeAndEffectiveTimesWithoutAssumingTrainingOrASchedule() {
    var temporary = suas("A").specialUseAirspaceType(SpecialUseAirspaceType.TEMPORARY_RESERVED)
        .suasEffectiveTimes("MON-FRI 0800-1700").name("Test reserved area").controllingAuthority("Test Center").build();
    var notam = suas("B").suasEffectiveTimes("BY NOTAM").build();
    var publication = new AeroPublication();
    DafifXmlAirspaces.populate(List.of(), List.of(), List.of(temporary, notam),
        List.of(suasCircle("A", 1), suasCircle("B", 2)), publication);

    var target = publication.getAirspaces().getRestrictiveAirspace().get(0);
    assertEquals(RestrictiveAirspaceType.UNSPECIFIED, target.getRestrictiveAirspaceType());
    assertEquals("T", supplemental(target).get("TYPE"));
    assertEquals("Test reserved area", target.getRestrictiveAirspaceName());
    assertEquals("Test Center", target.getControllingAgency());
    assertEquals("MON-FRI 0800-1700", target.getTimesOfOperation().getTimeNarrative());
    assertEquals(TimeCode.COMPLEX, target.getTimesOfOperation().getTimeCode());
    assertEquals("MON-FRI 0800-1700", supplemental(target).get("EFF_TIMES"));
    var targetNotam = publication.getAirspaces().getRestrictiveAirspace().get(1);
    var times = targetNotam.getTimesOfOperation();
    assertEquals(TimeCode.BY_NOTAM, times.getTimeCode());
    assertNull(times.getTimeNarrative());
    assertNull(times.isByNotam());
    assertEquals("BY NOTAM", supplemental(targetNotam).get("EFF_TIMES"));
  }

  @Test
  void retainsUnmappedBoundaryTypesInSupplementalData() {
    for (var type : List.of(BoundaryType.ADVISORY_AREA, BoundaryType.AIR_DEFENSE_IDENTIFICATION_ZONE,
        BoundaryType.AIR_ROUTE_TRAFFIC_CONTROL_CENTER, BoundaryType.AREA_CONTROL_CENTER,
        BoundaryType.BUFFER_ZONE, BoundaryType.MODE_C_DEFINED_AREA, BoundaryType.OTHER, BoundaryType.FUNCTIONAL_AIRSPACE_BLOCK)) {
      var target = controlled(boundary("TEST", type).build());
      assertNull(target.getControlledAirspaceType(), type.name());
      assertEquals(String.format("%02d", type.code()), supplemental(target).get("TYPE"));
    }
  }

  private static ControlledAirspace controlled(DafifBoundaryParent parent) {
    return boundaries(parent).getControlledAirspace().get(0);
  }

  private static Airspaces boundaries(DafifBoundaryParent... parents) {
    var segments = List.of(parents).stream().map(parent -> DafifBoundarySegment.builder()
        .boundaryIdentification(parent.boundaryIdentification()).segmentNumber(10).shape(Shape.CIRCLE)
        .latitude0(35.0).longitude0(-90.0).radius1(5.0).cycleDate(202601).build()).toList();
    var publication = new AeroPublication();
    DafifXmlAirspaces.populate(List.of(parents), segments, List.of(), List.of(), publication);
    return publication.getAirspaces();
  }

  private static DafifBoundaryParent.Builder boundary(String identifier, BoundaryType type) {
    return DafifBoundaryParent.builder().boundaryIdentification(identifier).boundaryType(type).icaoCode("KAAA")
        .level("B").lowerAltitude("GND").upperAltitude("UNLTD").geodeticDatum("WGE").cycleDate(202601);
  }

  private static DafifSuasParent.Builder suas(String sector) {
    return DafifSuasParent.builder().suasIdentification("R-TEST").sector(sector).specialUseAirspaceType(SpecialUseAirspaceType.RESTRICTED)
        .icaoCode("KAAA").level("B").lowerAltitude("GND").upperAltitude("UNLTD").geodeticDatum("WGE").cycleDate(202601);
  }

  private static DafifSuasSegment suasCircle(String sector, double radius) {
    return DafifSuasSegment.builder().suasIdentification("R-TEST").sector(sector).segmentNumber(10).shape(Shape.CIRCLE)
        .latitude0(35.0).longitude0(-90.0).radius1(radius).cycleDate(202601).build();
  }

  private static Map<String, String> supplemental(A424Base target) {
    return target.getSupplementalData().getAny().stream().map(value -> (JAXBElement<?>) value)
        .collect(Collectors.toMap(value -> value.getName().getLocalPart(), value -> (String) value.getValue()));
  }
}
