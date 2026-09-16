package org.mitre.boogie.xml;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;

import org.mitre.boogie.xml.v23_4.generated.A424Base;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airspace;
import org.mitre.boogie.xml.v23_4.generated.AirspaceAltitudeConstraint;
import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.AirspaceSegment;
import org.mitre.boogie.xml.v23_4.generated.Airspaces;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspace;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.FirUir;
import org.mitre.boogie.xml.v23_4.generated.FirUirIndicator;
import org.mitre.boogie.xml.v23_4.generated.FirUirSegment;
import org.mitre.boogie.xml.v23_4.generated.Level;
import org.mitre.boogie.xml.v23_4.generated.LowerLimitConstraint;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.boogie.xml.v23_4.generated.RestrictiveAirspace;
import org.mitre.boogie.xml.v23_4.generated.RestrictiveAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.SupplementalData;
import org.mitre.boogie.xml.v23_4.generated.TimeCode;
import org.mitre.boogie.xml.v23_4.generated.TimesOfOperation;
import org.mitre.boogie.xml.v23_4.generated.UnitIndicator;
import org.mitre.boogie.xml.v23_4.generated.UpperLimitConstraint;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;

/** Maps boundary and special-use tables, retaining source identities and unmatched fields as supplemental data. */
final class DafifXmlAirspaces {

  private DafifXmlAirspaces() {}

  static void populate(DafifXmlFixture.Records records, AeroPublication publication) {
    populate(records.boundaryParents(), records.boundarySegments(), records.suasParents(), records.suasSegments(), publication);
  }

  static void populate(List<DafifBoundaryParent> parents, List<DafifBoundarySegment> segments,
      List<DafifSuasParent> suasParents, List<DafifSuasSegment> suasSegments, AeroPublication publication) {
    var airspaces = new Airspaces();
    publication.setAirspaces(airspaces);
    Map<String, List<DafifBoundarySegment>> boundaries = segments.stream()
        .collect(Collectors.groupingBy(DafifBoundarySegment::boundaryIdentification));
    for (var parent : parents) {
      var geometry = DafifXmlAirspaceGeometry.boundary(boundaries.getOrDefault(parent.boundaryIdentification(), List.of()));
      if (geometry.isEmpty()) continue;
      geometry.forEach(segment -> segment.setLevel(level(parent.level())));
      if (parent.boundaryType() == BoundaryType.FIR || parent.boundaryType() == BoundaryType.UIR) {
        airspaces.getFirUir().add(firUir(parent, geometry));
      } else {
        airspaces.getControlledAirspace().add(controlled(parent, geometry));
      }
    }
    Map<SuasKey, List<DafifSuasSegment>> specialUse = suasSegments.stream()
        .collect(Collectors.groupingBy(segment -> new SuasKey(segment.suasIdentification(), segment.sector().orElse(""))));
    for (var parent : suasParents) {
      var key = new SuasKey(parent.suasIdentification(), parent.sector().orElse(""));
      var geometry = DafifXmlAirspaceGeometry.suas(specialUse.getOrDefault(key, List.of()));
      if (geometry.isEmpty()) continue;
      geometry.forEach(segment -> segment.setLevel(level(parent.level())));
      airspaces.getRestrictiveAirspace().add(restrictive(parent, geometry));
    }
  }

  private static ControlledAirspace controlled(DafifBoundaryParent source, List<AirspaceSegment> geometry) {
    var target = new ControlledAirspace();
    boundaryMetadata(source, target);
    source.name().ifPresent(target::setAirspaceName);
    source.controllingAuthority().ifPresent(target::setControllingAgency);
    source.airspaceClass().ifPresent(target::setAirspaceClassification);
    target.setControlledAirspaceType(controlledType(source));
    // The final DAFIF digit is a negative exponent: 050 = 5 NM, 031 = 0.3 NM, 152 = 0.15 NM.
    source.requiredNavPerformance().map(value -> BigDecimal.valueOf(value / 10).movePointLeft(value % 10))
        .ifPresent(target::setRnp);
    metadata(target, source.cycleDate(), source.lowerAltitude(), source.upperAltitude(), geometry);
    return target;
  }

  private static ControlledAirspaceType controlledType(DafifBoundaryParent source) {
    return switch (source.boundaryType()) {
      case CONTROL_AREA, OCEAN_CONTROL_AREA -> ControlledAirspaceType.CONTROL;
      case CONTROL_ZONE -> ControlledAirspaceType.CLASS_D; // XML includes ICAO CTRs in ClassD.
      case RADAR_AREA -> ControlledAirspaceType.RADAR;
      case TERMINAL_CONTROL_AREA -> switch (source.airspaceClass().orElse("")) {
        case "B" -> ControlledAirspaceType.CLASS_B;
        case "C" -> ControlledAirspaceType.CLASS_C;
        default -> ControlledAirspaceType.TERMINAL_CONTROL;
      };
      // ADIZ, advisory areas, centers, buffer zones, Mode C areas, FABs and OTHER have no exact XML type.
      // Keep the source TYPE in supplementalData, as with other unmapped fields in this test fixture.
      default -> null;
    };
  }

  private static FirUir firUir(DafifBoundaryParent source, List<AirspaceSegment> geometry) {
    var target = new FirUir();
    boundaryMetadata(source, target);
    target.setReferenceId(DafifXmlReferences.id("boundary", source.boundaryIdentification()));
    target.setRecordType(RecordType.STANDARD);
    target.setCycleDate(cycle(source.cycleDate()));
    target.setFirUirIdentifier(source.icaoCode());
    source.name().ifPresent(target::setFirUirName);
    var limits = limits(source.lowerAltitude(), source.upperAltitude());
    // FIR/UIR limits have no AGL unit indicator. Retain these source tokens in supplementalData only.
    if (source.lowerAltitude().endsWith("AGL")) limits.setLowerLimit(null);
    if (source.upperAltitude().endsWith("AGL")) limits.setUpperLimit(null);
    if (source.boundaryType() == BoundaryType.FIR) {
      target.setFirUirIndicator(FirUirIndicator.FIR);
      target.setFirAltitudeLimits(limits);
    } else {
      target.setFirUirIndicator(FirUirIndicator.UIR);
      target.setUirAltitudeLimits(limits);
    }
    for (var segment : geometry) {
      var firSegment = new FirUirSegment();
      firSegment.setSequenceNumber(segment.getSequenceNumber());
      firSegment.setLocation(segment.getLocation());
      firSegment.setBoundaryVia(segment.getBoundaryVia());
      firSegment.setArcOriginLocation(segment.getArcOriginLocation());
      firSegment.setArcDistance(segment.getArcDistance());
      firSegment.setArcBearing(segment.getArcBearing());
      firSegment.setIsEndOfDescription(segment.isIsEndOfDescription());
      firSegment.setLevel(segment.getLevel());
      firSegment.setCycleDate(segment.getCycleDate());
      firSegment.setRecordType(segment.getRecordType());
      firSegment.getNotes().addAll(segment.getNotes());
      target.getFirUirSegment().add(firSegment);
    }
    return target;
  }

  private static RestrictiveAirspace restrictive(DafifSuasParent source, List<AirspaceSegment> geometry) {
    var target = new RestrictiveAirspace();
    sourceField(target, "SUAS_IDENT", source.suasIdentification());
    source.sector().ifPresent(value -> sourceField(target, "SECTOR", value));
    sourceField(target, "TYPE", source.specialUseAirspaceType().code());
    sourceField(target, "ICAO", source.icaoCode());
    sourceField(target, "LOWER_ALT", source.lowerAltitude());
    sourceField(target, "UPPER_ALT", source.upperAltitude());
    sourceField(target, "WGS_DATUM", source.geodeticDatum());
    sourceField(target, "EFF_DATE", source.effectiveDate());
    source.suasWeather().ifPresent(value -> sourceField(target, "WX", value));
    source.communicationName().ifPresent(value -> sourceField(target, "COMM_NAME", value));
    source.communicationsFrequency1().ifPresent(value -> sourceField(target, "FREQ1", value));
    source.communicationsFrequency2().ifPresent(value -> sourceField(target, "FREQ2", value));
    target.setIcaoCode(source.icaoCode().substring(0, 2));
    target.setRestrictiveAirspaceDesignation(source.suasIdentification());
    // DAFIF sectors may have two characters; XML multipleCode only permits one.
    source.sector().filter(value -> value.length() == 1).ifPresent(target::setMultipleCode);
    source.name().ifPresent(target::setAirspaceName);
    source.name().ifPresent(target::setRestrictiveAirspaceName);
    source.controllingAuthority().ifPresent(target::setControllingAgency);
    target.setRestrictiveAirspaceType(switch (source.specialUseAirspaceType()) {
      case ALERT -> RestrictiveAirspaceType.ALERT;
      case DANGER -> RestrictiveAirspaceType.DANGER;
      case MILITARY_OPERATIONS -> RestrictiveAirspaceType.MILITARY_OPS;
      case PROHIBITED -> RestrictiveAirspaceType.PROHIBITED;
      case RESTRICTED -> RestrictiveAirspaceType.RESTRICTED;
      case WARNING -> RestrictiveAirspaceType.WARNING;
      case TEMPORARY_RESERVED -> RestrictiveAirspaceType.UNSPECIFIED; // DAFIF T does not mean ARINC Training.
    });
    source.suasEffectiveTimes().ifPresent(value -> {
      sourceField(target, "EFF_TIMES", value);
      var times = new TimesOfOperation();
      times.setTimeCode("BY NOTAM".equals(value) ? TimeCode.BY_NOTAM : TimeCode.COMPLEX);
      if (times.getTimeCode() == TimeCode.COMPLEX) times.setTimeNarrative(value);
      target.setTimesOfOperation(times);
    });
    metadata(target, source.cycleDate(), source.lowerAltitude(), source.upperAltitude(), geometry);
    return target;
  }

  private static void boundaryMetadata(DafifBoundaryParent source, A424Base target) {
    sourceField(target, "BDRY_IDENT", source.boundaryIdentification());
    sourceField(target, "TYPE", String.format("%02d", source.boundaryType().code()));
    sourceField(target, "ICAO", source.icaoCode());
    sourceField(target, "LOWER_ALT", source.lowerAltitude());
    sourceField(target, "UPPER_ALT", source.upperAltitude());
    sourceField(target, "WGS_DATUM", source.geodeticDatum());
    sourceField(target, "UP_RVSM", source.upperRvsm());
    sourceField(target, "LO_RVSM", source.lowerRvsm());
    source.controllingAuthority().ifPresent(value -> sourceField(target, "CON_AUTH", value));
    source.airspaceClass().ifPresent(value -> sourceField(target, "CLASS", value));
    source.classExceptionFlag().ifPresent(value -> sourceField(target, "CLASS_EXC", value));
    source.classExceptionRemarks().ifPresent(value -> sourceField(target, "CLASS_EX_RMK", value));
    source.communicationName().ifPresent(value -> sourceField(target, "COMM_NAME", value));
    source.communicationsFrequency1().ifPresent(value -> sourceField(target, "COMM_FREQ1", value));
    source.communicationsFrequency2().ifPresent(value -> sourceField(target, "COMM_FREQ2", value));
    source.requiredNavPerformance().ifPresent(value -> sourceField(target, "RNP", String.format("%03d", value)));
  }

  private static void metadata(Airspace target, Integer cycle, String lower, String upper, List<AirspaceSegment> geometry) {
    target.setCycleDate(cycle(cycle));
    target.setAirspaceAltLimits(limits(lower, upper));
    target.setUnitIndicatorLower(unit(lower));
    target.setUnitIndicatorUpper(unit(upper));
    target.getAirspaceSegment().addAll(geometry);
  }

  private static AirspaceAltitudeConstraint limits(String lower, String upper) {
    var target = new AirspaceAltitudeConstraint();
    var floor = new LowerLimitConstraint();
    if ("GND".equals(lower) || "SURFACE".equals(lower)) floor.setIsGround(true);
    else altitude(floor, lower);
    target.setLowerLimit(floor);
    var ceiling = new UpperLimitConstraint();
    if ("UNLTD".equals(upper)) ceiling.setIsUnlimited(true);
    else altitude(ceiling, upper);
    target.setUpperLimit(ceiling);
    return target;
  }

  private static void altitude(AirspaceRouteHoldAltitude target, String value) {
    if (value.startsWith("FL")) {
      target.setAltitude(Math.multiplyExact(Integer.parseInt(value.substring(2)), 100));
      target.setIsFlightLevel(true);
    } else if (value.endsWith("AMSL")) {
      target.setAltitude(Integer.parseInt(value.substring(0, value.length() - 4)));
    } else if (value.endsWith("AGL")) {
      target.setAltitude(Integer.parseInt(value.substring(0, value.length() - 3)));
    } else if ("BY NOTAM".equals(value)) {
      target.setIsNotam(true);
    } else if ("U".equals(value)) {
      target.setIsUnknown(true);
    } else {
      throw new IllegalArgumentException("Unsupported DAFIF airspace altitude: " + value);
    }
  }

  private static UnitIndicator unit(String value) {
    if (value.endsWith("AGL") || "GND".equals(value) || "SURFACE".equals(value)) return UnitIndicator.AGL;
    if (value.endsWith("AMSL") || value.startsWith("FL")) return UnitIndicator.MSL;
    return null;
  }

  private static Level level(String value) {
    return switch (value) {
      case "B" -> Level.ALL_ALT;
      case "H" -> Level.HIGH_ALT;
      case "L" -> Level.LOW_ALT;
      default -> throw new IllegalArgumentException("Unsupported DAFIF airspace level: " + value);
    };
  }

  private static String cycle(Integer value) {
    return value == null ? null : String.format("%04d", value % 10000);
  }

  private static void sourceField(A424Base target, String field, String value) {
    if (value == null || value.isBlank()) return;
    if (target.getSupplementalData() == null) target.setSupplementalData(new SupplementalData());
    target.getSupplementalData().getAny().add(new JAXBElement<>(new QName("urn:boogie:dafif:8.1", field, "dafif"), String.class, value));
  }

  private record SuasKey(String identifier, String sector) {}
}
