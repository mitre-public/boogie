package org.mitre.boogie.xml.fixtures;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airspace;
import org.mitre.boogie.xml.v23_4.generated.AirspaceAltitudeConstraint;
import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.AirspaceSegment;
import org.mitre.boogie.xml.v23_4.generated.Airspaces;
import org.mitre.boogie.xml.v23_4.generated.AreaCode;
import org.mitre.boogie.xml.v23_4.generated.BoundaryVia;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspace;
import org.mitre.boogie.xml.v23_4.generated.ControlledAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.FirUir;
import org.mitre.boogie.xml.v23_4.generated.FirUirAtcReportingUnitsAltitude;
import org.mitre.boogie.xml.v23_4.generated.FirUirAtcReportingUnitsSpeed;
import org.mitre.boogie.xml.v23_4.generated.FirUirIndicator;
import org.mitre.boogie.xml.v23_4.generated.FirUirSegment;
import org.mitre.boogie.xml.v23_4.generated.Level;
import org.mitre.boogie.xml.v23_4.generated.LowerLimitConstraint;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.boogie.xml.v23_4.generated.RestrictiveAirspace;
import org.mitre.boogie.xml.v23_4.generated.RestrictiveAirspaceType;
import org.mitre.boogie.xml.v23_4.generated.TimeCode;
import org.mitre.boogie.xml.v23_4.generated.TimesOfOperation;
import org.mitre.boogie.xml.v23_4.generated.UnitIndicator;
import org.mitre.boogie.xml.v23_4.generated.UpperLimitConstraint;
import org.mitre.tdp.boogie.arinc.assemble.ControlledAirspaceKey;
import org.mitre.tdp.boogie.arinc.assemble.RestrictiveAirspaceKey;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;

/** Maps the CIFP primary airspace records into the generated XML fixture. */
final class CifpXmlAirspaces {

  private CifpXmlAirspaces() {
  }

  static void populate(ConvertedArincRecords records, AeroPublication publication, CifpXmlReferences refs) {
    Airspaces airspaces = new Airspaces();
    publication.setAirspaces(airspaces);

    Map<ControlledAirspaceKey, List<ArincControlledAirspaceLeg>> controlled = records.arincControlledAirspaceLegs().stream()
        .collect(Collectors.groupingBy(ControlledAirspaceKey::from, LinkedHashMap::new, Collectors.toList()));
    for (List<ArincControlledAirspaceLeg> legs : controlled.values()) {
      legs.sort(Comparator.comparingInt(ArincControlledAirspaceLeg::sequenceNumber));
      airspaces.getControlledAirspace().add(controlled(legs, refs));
    }

    Map<RestrictiveAirspaceKey, List<ArincRestrictiveAirspaceLeg>> restrictive = records.arincRestrictiveAirspaceLegs().stream()
        .collect(Collectors.groupingBy(RestrictiveAirspaceKey::from, LinkedHashMap::new, Collectors.toList()));
    for (List<ArincRestrictiveAirspaceLeg> legs : restrictive.values()) {
      legs.sort(Comparator.comparingInt(ArincRestrictiveAirspaceLeg::sequenceNumber));
      airspaces.getRestrictiveAirspace().add(restrictive(legs, refs));
    }

    Map<List<String>, List<ArincFirUirLeg>> firUirs = records.arincFirUirLegs().stream()
        .collect(Collectors.groupingBy(leg -> List.of(leg.firUirIdentifier(), leg.firUirIndicator().name()),
            LinkedHashMap::new, Collectors.toList()));
    for (List<ArincFirUirLeg> legs : firUirs.values()) {
      legs.sort(Comparator.comparingInt(ArincFirUirLeg::sequenceNumber));
      airspaces.getFirUir().add(firUir(legs, refs));
    }
  }

  private static ControlledAirspace controlled(List<ArincControlledAirspaceLeg> legs, CifpXmlReferences refs) {
    ArincControlledAirspaceLeg first = legs.get(0);
    ControlledAirspace target = new ControlledAirspace();
    first.controlledAirspaceName().ifPresent(target::setAirspaceName);
    first.multipleCode().ifPresent(target::setMultipleCode);
    first.airspaceClassification().ifPresent(target::setAirspaceClassification);
    target.setControlledAirspaceType(switch (first.airspaceType()) {
      case A -> ControlledAirspaceType.CLASS_C;
      case C -> ControlledAirspaceType.CONTROL;
      case M -> ControlledAirspaceType.TERMINAL_CONTROL;
      case R -> ControlledAirspaceType.RADAR;
      case T -> ControlledAirspaceType.CLASS_B;
      case Z -> ControlledAirspaceType.CLASS_D;
      default -> throw new IllegalArgumentException("Unsupported controlled airspace type: " + first.airspaceType());
    });
    first.supplierSectionCode().ifPresent(section -> target.setControlledAirspaceCenterRef(refs.fix(
        section.name(), first.supplierSubSectionCode().orElse(""), first.airspaceCenter(), first.icaoRegion(), "")));
    first.rnp().map(BigDecimal::valueOf).ifPresent(target::setRnp);
    metadata(target, first.cycleDate(), first.timeCode(), first.notam(), first.lowerUnitIndicator(), first.upperUnitIndicator());
    target.setAirspaceAltLimits(limits(first.lowerLimit(), first.upperLimit(),
        refs.rawAirspaceField("UC", first.fileRecordNumber(), 81, 86),
        refs.rawAirspaceField("UC", first.fileRecordNumber(), 87, 92)));
    for (ArincControlledAirspaceLeg leg : legs) {
      AirspaceSegment segment = new AirspaceSegment();
      geometry(segment, leg.sequenceNumber(), leg.boundaryVia(), leg.latitude(), leg.longitude(),
          leg.arcOriginLatitude(), leg.arcOriginLongitude(), leg.arcDistance(), leg.arcBearing());
      leg.level().map(CifpXmlAirspaces::level).ifPresent(segment::setLevel);
      target.getAirspaceSegment().add(segment);
    }
    return target;
  }

  private static RestrictiveAirspace restrictive(List<ArincRestrictiveAirspaceLeg> legs, CifpXmlReferences refs) {
    ArincRestrictiveAirspaceLeg first = legs.get(0);
    RestrictiveAirspace target = new RestrictiveAirspace();
    target.setIcaoCode(first.icaoRegion());
    target.setRestrictiveAirspaceDesignation(first.restrictiveAirspaceDesignation());
    first.restrictiveAirspaceName().ifPresent(target::setRestrictiveAirspaceName);
    first.restrictiveAirspaceName().ifPresent(target::setAirspaceName);
    first.multipleCode().ifPresent(target::setMultipleCode);
    target.setRestrictiveAirspaceType(switch (first.restrictiveType()) {
      case "A" -> RestrictiveAirspaceType.ALERT;
      case "C" -> RestrictiveAirspaceType.CAUTION;
      case "D" -> RestrictiveAirspaceType.DANGER;
      case "L" -> RestrictiveAirspaceType.LONG_TERM_TFR;
      case "M" -> RestrictiveAirspaceType.MILITARY_OPS;
      case "N" -> RestrictiveAirspaceType.NATIONAL_SECURITY;
      case "P" -> RestrictiveAirspaceType.PROHIBITED;
      case "R" -> RestrictiveAirspaceType.RESTRICTED;
      case "T" -> RestrictiveAirspaceType.TRAINING;
      case "W" -> RestrictiveAirspaceType.WARNING;
      case "U" -> RestrictiveAirspaceType.UNSPECIFIED;
      default -> throw new IllegalArgumentException("Unsupported restrictive airspace type: " + first.restrictiveType());
    });
    metadata(target, first.cycleDate(), first.timeCode(), first.notam(), first.lowerUnitIndicator(), first.upperUnitIndicator());
    target.setAirspaceAltLimits(limits(first.lowerLimit(), first.upperLimit(),
        refs.rawAirspaceField("UR", first.fileRecordNumber(), 81, 86),
        refs.rawAirspaceField("UR", first.fileRecordNumber(), 87, 92)));
    for (ArincRestrictiveAirspaceLeg leg : legs) {
      AirspaceSegment segment = new AirspaceSegment();
      geometry(segment, leg.sequenceNumber(), leg.boundaryVia().orElseThrow(), leg.latitude(), leg.longitude(),
          leg.arcOriginLatitude(), leg.arcOriginLongitude(), leg.arcDistance(), leg.arcBearing());
      leg.level().map(CifpXmlAirspaces::level).ifPresent(segment::setLevel);
      target.getAirspaceSegment().add(segment);
    }
    return target;
  }

  private static FirUir firUir(List<ArincFirUirLeg> legs, CifpXmlReferences refs) {
    ArincFirUirLeg first = legs.get(0);
    FirUir target = new FirUir();
    target.setReferenceId(CifpXmlReferences.id("UF", first.firUirIdentifier(), first.firUirIndicator().name()));
    target.setCycleDate(first.cycleDate());
    target.setAreaCode(AreaCode.valueOf(first.customerAreaCode().name()));
    target.setRecordType(switch (first.recordType()) {
      case S -> RecordType.STANDARD;
      case T -> RecordType.TAILORED;
      default -> throw new IllegalArgumentException("Unsupported FIR/UIR record type: " + first.recordType());
    });
    target.setFirUirIdentifier(first.firUirIdentifier());
    first.firUirAddress().ifPresent(target::setFirUirAddress);
    first.firUirName().ifPresent(target::setFirUirName);
    first.entryReport().ifPresent(target::setIsEntryReport);
    target.setFirUirIndicator(switch (first.firUirIndicator()) {
      case F -> FirUirIndicator.FIR;
      case U -> FirUirIndicator.UIR;
      case B -> FirUirIndicator.COMBINED;
      default -> throw new IllegalArgumentException("Unsupported FIR/UIR indicator: " + first.firUirIndicator());
    });
    target.setFirAltitudeLimits(limits(Optional.empty(), first.firUpperLimit(), null,
        refs.rawAirspaceField("UF", first.fileRecordNumber(), 80, 85)));
    target.setUirAltitudeLimits(limits(first.uirLowerLimit(), first.uirUpperLimit(),
        refs.rawAirspaceField("UF", first.fileRecordNumber(), 85, 90),
        refs.rawAirspaceField("UF", first.fileRecordNumber(), 90, 95)));
    first.reportingUnitsAltitude().map(CifpXmlAirspaces::reportingAltitude).ifPresent(target::setFirUirAtcReportingUnitsAltitude);
    first.reportingUnitsSpeed().map(CifpXmlAirspaces::reportingSpeed).ifPresent(target::setFirUirAtcReportingUnitsSpeed);
    for (ArincFirUirLeg leg : legs) {
      FirUirSegment segment = new FirUirSegment();
      geometry(segment, leg.sequenceNumber(), leg.boundaryVia(), leg.firUirLatitude(), leg.firUirLongitude(),
          leg.arcOriginLatitude(), leg.arcOriginLongitude(), leg.arcDistance(), leg.arcBearing());
      leg.adjacentFirIdentifier().ifPresent(segment::setAdjacentFirIdentifier);
      leg.adjacentUirIdentifier().ifPresent(segment::setAdjacentUirIdentifier);
      target.getFirUirSegment().add(segment);
    }
    return target;
  }

  private static void metadata(Airspace target, String cycle, Optional<String> timeCode, Optional<String> notam,
      Optional<String> lowerUnit, Optional<String> upperUnit) {
    target.setCycleDate(cycle);
    lowerUnit.map(CifpXmlAirspaces::unit).ifPresent(target::setUnitIndicatorLower);
    upperUnit.map(CifpXmlAirspaces::unit).ifPresent(target::setUnitIndicatorUpper);
    if (timeCode.isPresent() || notam.isPresent()) {
      TimesOfOperation times = new TimesOfOperation();
      timeCode.map(CifpXmlAirspaces::time).ifPresent(times::setTimeCode);
      if (notam.filter("N"::equals).isPresent()) {
        times.setByNotam(true);
        if (times.getTimeCode() == null) {
          times.setTimeCode(TimeCode.BY_NOTAM);
        }
      }
      target.setTimesOfOperation(times);
    }
  }

  private static void geometry(AirspaceSegment target, int sequence,
      org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia via, Optional<Double> latitude, Optional<Double> longitude,
      Optional<Double> originLatitude, Optional<Double> originLongitude, Optional<Double> distance, Optional<Double> bearing) {
    target.setSequenceNumber(sequence);
    target.setBoundaryVia(switch (via) {
      case C, CE -> BoundaryVia.CIRCLE;
      case G, GE -> BoundaryVia.GREAT_CIRCLE;
      case H, HE -> BoundaryVia.RHUMB_LINE;
      case L, LE -> BoundaryVia.COUNTER_CLOCKWISE_ARC;
      case R, RE -> BoundaryVia.CLOCKWISE_ARC;
      // An end marker alone supplies no path geometry; do not invent a great-circle segment.
      default -> throw new IllegalArgumentException("Airspace boundary has no path geometry: " + via);
    });
    target.setIsEndOfDescription(via.name().endsWith("E"));
    if (latitude.isPresent() && longitude.isPresent()) {
      target.setLocation(CifpXmlPoints.location(latitude.orElseThrow(), longitude.orElseThrow()));
    }
    if (originLatitude.isPresent() && originLongitude.isPresent()) {
      target.setArcOriginLocation(CifpXmlPoints.location(originLatitude.orElseThrow(), originLongitude.orElseThrow()));
    }
    distance.map(BigDecimal::valueOf).ifPresent(target::setArcDistance);
    bearing.map(BigDecimal::valueOf).ifPresent(target::setArcBearing);
  }

  /** Raw tokens retain ground/unlimited/flight-level distinctions erased by the fixed-width numeric model. */
  static AirspaceAltitudeConstraint limits(Optional<Double> lower, Optional<Double> upper, String rawLower, String rawUpper) {
    AirspaceAltitudeConstraint target = new AirspaceAltitudeConstraint();
    if (lower.isPresent() || hasText(rawLower)) {
      LowerLimitConstraint limit = new LowerLimitConstraint();
      if ("GND".equals(trim(rawLower))) {
        limit.setIsGround(true);
      } else {
        altitude(limit, lower, rawLower);
      }
      target.setLowerLimit(limit);
    }
    if (upper.isPresent() || hasText(rawUpper)) {
      UpperLimitConstraint limit = new UpperLimitConstraint();
      if ("UNLTD".equals(trim(rawUpper))) {
        limit.setIsUnlimited(true);
      } else {
        altitude(limit, upper, rawUpper);
      }
      target.setUpperLimit(limit);
    }
    if (target.getLowerLimit() == null && target.getUpperLimit() == null) {
      return null;
    }
    return target;
  }

  private static void altitude(AirspaceRouteHoldAltitude target, Optional<Double> value, String raw) {
    String token = trim(raw);
    if (token.startsWith("FL")) {
      // Types/DataTypes.xsd defines AltitudeValue in feet: FL270 is encoded as 27000.
      target.setAltitude(Math.multiplyExact(Integer.parseInt(token.substring(2)), 100));
      target.setIsFlightLevel(true);
      return;
    }
    switch (token) {
      case "MSL" -> target.setIsMsl(true);
      case "NOTAM" -> target.setIsNotam(true);
      case "NOTSP" -> target.setIsNotSpecified(true);
      case "UNK" -> target.setIsUnknown(true);
      default -> target.setAltitude(BigDecimal.valueOf(value.orElseThrow(
          () -> new IllegalArgumentException("Unsupported airspace altitude: " + raw))).intValueExact());
    }
  }

  private static UnitIndicator unit(String code) {
    return switch (code) {
      case "M" -> UnitIndicator.MSL;
      case "A" -> UnitIndicator.AGL;
      default -> throw new IllegalArgumentException("Unsupported airspace altitude unit: " + code);
    };
  }

  private static Level level(org.mitre.tdp.boogie.arinc.v18.field.Level code) {
    return switch (code) {
      case B -> Level.ALL_ALT;
      case H -> Level.HIGH_ALT;
      case L -> Level.LOW_ALT;
      default -> throw new IllegalArgumentException("Unsupported airspace level: " + code);
    };
  }

  private static TimeCode time(String code) {
    return switch (code) {
      case "C" -> TimeCode.CONTINUOUS_INCLUDING_HOLIDAYS;
      case "H" -> TimeCode.CONTINUOUS_EXCLUDING_HOLIDAYS;
      case "S" -> TimeCode.SPECIFIED_EXCLUDING_HOLIDAYS;
      case "T" -> TimeCode.SPECIFIED_INCLUDING_HOLIDAYS;
      case "P" -> TimeCode.BY_NOTAM;
      case "U" -> TimeCode.UNSPECIFIED;
      default -> throw new IllegalArgumentException("Airspace time code requires unsupported continuation data: " + code);
    };
  }

  private static FirUirAtcReportingUnitsAltitude reportingAltitude(String code) {
    return switch (code) {
      case "0" -> FirUirAtcReportingUnitsAltitude.NOT_SPECIFIED;
      case "1" -> FirUirAtcReportingUnitsAltitude.FLIGHT_LEVEL;
      case "2" -> FirUirAtcReportingUnitsAltitude.METERS;
      case "3" -> FirUirAtcReportingUnitsAltitude.FEET;
      default -> throw new IllegalArgumentException("Unsupported FIR/UIR altitude reporting unit: " + code);
    };
  }

  private static FirUirAtcReportingUnitsSpeed reportingSpeed(String code) {
    return switch (code) {
      case "0" -> FirUirAtcReportingUnitsSpeed.NOT_SPECIFIED;
      case "1" -> FirUirAtcReportingUnitsSpeed.KNOTS;
      case "2" -> FirUirAtcReportingUnitsSpeed.MACH;
      case "3" -> FirUirAtcReportingUnitsSpeed.KILOMETERS_HR;
      default -> throw new IllegalArgumentException("Unsupported FIR/UIR speed reporting unit: " + code);
    };
  }

  private static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  private static String trim(String value) {
    if (value == null) {
      return "";
    }
    return value.trim();
  }
}
