package org.mitre.boogie.xml.fixtures;

import static org.mitre.boogie.xml.fixtures.CifpXmlReferences.id;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.v23_4.generated.A424Point;
import org.mitre.boogie.xml.v23_4.generated.A424Record;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.Airports;
import org.mitre.boogie.xml.v23_4.generated.AreaCode;
import org.mitre.boogie.xml.v23_4.generated.Bearing;
import org.mitre.boogie.xml.v23_4.generated.Constraint;
import org.mitre.boogie.xml.v23_4.generated.DatumCode;
import org.mitre.boogie.xml.v23_4.generated.Dme;
import org.mitre.boogie.xml.v23_4.generated.DmeTacan;
import org.mitre.boogie.xml.v23_4.generated.EastWest;
import org.mitre.boogie.xml.v23_4.generated.EnrouteNdbs;
import org.mitre.boogie.xml.v23_4.generated.EnrouteWaypoints;
import org.mitre.boogie.xml.v23_4.generated.FigureOfMerit;
import org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure;
import org.mitre.boogie.xml.v23_4.generated.Frequency;
import org.mitre.boogie.xml.v23_4.generated.Gls;
import org.mitre.boogie.xml.v23_4.generated.Helipad;
import org.mitre.boogie.xml.v23_4.generated.HelipadDimensions;
import org.mitre.boogie.xml.v23_4.generated.HelipadShape;
import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.mitre.boogie.xml.v23_4.generated.HeliportType;
import org.mitre.boogie.xml.v23_4.generated.Heliports;
import org.mitre.boogie.xml.v23_4.generated.Latitude;
import org.mitre.boogie.xml.v23_4.generated.LocalizerAzimuthPositionReference;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.mitre.boogie.xml.v23_4.generated.Location;
import org.mitre.boogie.xml.v23_4.generated.Longitude;
import org.mitre.boogie.xml.v23_4.generated.MagneticTrueIndicator;
import org.mitre.boogie.xml.v23_4.generated.MagneticVariation;
import org.mitre.boogie.xml.v23_4.generated.MagneticVariationEWT;
import org.mitre.boogie.xml.v23_4.generated.MilitaryTacan;
import org.mitre.boogie.xml.v23_4.generated.NameFormatIndicator;
import org.mitre.boogie.xml.v23_4.generated.Navaid;
import org.mitre.boogie.xml.v23_4.generated.NavaidWeatherInfo;
import org.mitre.boogie.xml.v23_4.generated.Navaids;
import org.mitre.boogie.xml.v23_4.generated.Ndb;
import org.mitre.boogie.xml.v23_4.generated.NdbNavaidClass;
import org.mitre.boogie.xml.v23_4.generated.NdbNavaidCoverage;
import org.mitre.boogie.xml.v23_4.generated.NdbNavaidIfMarkerInfo;
import org.mitre.boogie.xml.v23_4.generated.NdbNavaidType;
import org.mitre.boogie.xml.v23_4.generated.NorthSouth;
import org.mitre.boogie.xml.v23_4.generated.Port;
import org.mitre.boogie.xml.v23_4.generated.PrecisionApproachCategory;
import org.mitre.boogie.xml.v23_4.generated.PublicMilitaryIndicator;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.boogie.xml.v23_4.generated.Runway;
import org.mitre.boogie.xml.v23_4.generated.RunwayIdentifier;
import org.mitre.boogie.xml.v23_4.generated.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.v23_4.generated.RunwaySuffix;
import org.mitre.boogie.xml.v23_4.generated.RunwaySurfaceCode;
import org.mitre.boogie.xml.v23_4.generated.SpeedLimit;
import org.mitre.boogie.xml.v23_4.generated.StationDeclination;
import org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT;
import org.mitre.boogie.xml.v23_4.generated.Tacan;
import org.mitre.boogie.xml.v23_4.generated.TerminalNdb;
import org.mitre.boogie.xml.v23_4.generated.TerminalWaypoint;
import org.mitre.boogie.xml.v23_4.generated.VhfNavaidClass;
import org.mitre.boogie.xml.v23_4.generated.VhfNavaidCoverage;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.boogie.xml.v23_4.generated.WaypointType;
import org.mitre.boogie.xml.v23_4.generated.WaypointUsage;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;

/** Maps the CIFP fixture's point records into the v23.4 JAXB graph used by the file-writing test. */
final class CifpXmlPoints {

  private CifpXmlPoints() {
  }

  static void populate(ConvertedArincRecords records, AeroPublication publication, CifpXmlReferences refs) {
    publication.setAirports(new Airports());
    publication.setHeliports(new Heliports());
    publication.setEnrouteWaypoints(new EnrouteWaypoints());
    publication.setVhfNavaids(new Navaids());
    publication.setEnrouteNdbs(new EnrouteNdbs());
    for (ArincAirport source : records.arincAirports()) {
      Airport target = airport(source);
      publication.getAirports().getAirport().add(target);
      refs.addPort(source.airportIdentifier(), source.airportIcaoRegion(), target);
      refs.addFix("P", "A", source.airportIdentifier(), source.airportIcaoRegion(), source.airportIdentifier(), target);
    }
    Map<String, ArincHeliport> heliportSources = new LinkedHashMap<>();
    for (ArincHeliport source : records.arincHeliports()) {
      String key = id("heliport", source.heliportIdentifier(), source.heliportIcaoRegion());
      ArincHeliport first = heliportSources.putIfAbsent(key, source);
      Heliport target;
      if (first == null) {
        target = heliport(source);
        publication.getHeliports().getHeliport().add(target);
        refs.addPort(source.heliportIdentifier(), source.heliportIcaoRegion(), target);
        refs.addFix("H", "A", source.heliportIdentifier(), source.heliportIcaoRegion(), source.heliportIdentifier(), target);
      } else {
        target = (Heliport) refs.port("H", source.heliportIdentifier(), source.heliportIcaoRegion());
        reportHeliportDifferences(first, source, target);
      }
      if (source.padIdentifier().isPresent()) {
        Helipad pad = helipad(source);
        target.getHelipad().add(pad);
        refs.addFix("H", "H", source.padIdentifier().orElseThrow(), source.heliportIcaoRegion(), source.heliportIdentifier(), pad);
      }
    }
    for (ArincRunway source : records.arincRunways()) {
      Airport port = (Airport) refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion());
      Runway target = runway(source);
      port.getRunway().add(target);
      refs.addFix("P", "G", source.runwayIdentifier(), source.airportIcaoRegion(), source.airportIdentifier(), target);
    }
    for (ArincHelipad source : records.arincHelipads()) {
      Helipad target = helipad(source);
      refs.port(source.sectionCode().name(), source.airportHeliportIdentifier(), source.icaoCode()).getHelipad().add(target);
      refs.addFix(source.sectionCode().name(), source.subSectionCode().orElse("H"), source.helipadIdentifier(),
          source.icaoCode(), source.airportHeliportIdentifier(), target);
    }
    for (ArincWaypoint source : records.arincWaypoints()) {
      addWaypoint(source, publication, refs);
    }
    for (ArincVhfNavaid source : records.arincVhfNavaids()) {
      addVhf(source, publication, refs);
    }
    for (ArincNdbNavaid source : records.arincNdbNavaids()) {
      addNdb(source, publication, refs);
    }
    for (ArincLocalizerGlideSlope source : records.arincLocalizerGlideSlopes()) {
      LocalizerGlideslope target = localizer(source, refs);
      refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion()).getLocalizerGlideslope().add(target);
      refs.addFix(source.sectionCode().name(), source.subSectionCode().orElse("I"), source.localizerIdentifier(),
          source.airportIcaoRegion(), source.airportIdentifier(), target);
    }
    for (ArincGnssLandingSystem source : records.arincGnssLandingSystems()) {
      Gls target = gls(source, refs);
      refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion()).getGls().add(target);
      refs.addFix(source.sectionCode().name(), source.subSectionCode().orElse("T"), source.glsRefPathIdentifier(),
          source.airportIcaoRegion(), source.airportIdentifier(), target);
    }
    for (ArincAirport source : records.arincAirports()) {
      source.recommendedNavaid().ifPresent(ident -> refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion())
          .setRecommendedNavaidRef(refs.fix("D", "", ident, source.recommendedNavaidIcaoRegion().orElse(""), "")));
    }
    for (ArincHeliport source : heliportSources.values()) {
      source.recommendedVhfNavaid().ifPresent(ident -> refs.port("H", source.heliportIdentifier(), source.heliportIcaoRegion())
          .setRecommendedNavaidRef(refs.fix("D", "", ident, source.navaidIcaoRegion().orElse(""), "")));
    }
  }

  private static Airport airport(ArincAirport source) {
    Airport target = new Airport();
    point(target, "airport", source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion(), "", source.latitude(), source.longitude());
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    source.airportFullName().ifPresent(target::setName);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.magneticVariation().map(CifpXmlPoints::magneticVariation).ifPresent(target::setMagneticVariation);
    target.setElevation(source.airportElevation().orElseThrow(
        () -> new IllegalArgumentException("CIFP airport has no elevation: " + source.airportIdentifier())).intValue());
    source.iataDesignator().ifPresent(target::setAtaIataDesignator);
    source.daylightTimeIndicator().ifPresent(target::setDaylightIndicator);
    source.ifrCapability().ifPresent(target::setIsIfrCapable);
    source.magneticTrueIndicator().map(Enum::name).map(CifpXmlPoints::magneticTrue).ifPresent(target::setMagneticTrueIndicator);
    source.publicMilitaryIndicator().map(Enum::name).map(CifpXmlPoints::publicMilitary).ifPresent(target::setPublicMilitaryIndicator);
    source.transitionAltitude().map(Double::intValue).ifPresent(target::setTransitionAltitude);
    source.transitionLevel().map(Double::intValue).ifPresent(target::setTransitionLevel);
    source.speedLimit().map(CifpXmlPoints::speedLimit).ifPresent(target::setSpeedLimit);
    source.speedLimitAltitude().map(CifpXmlPoints::constraint).ifPresent(target::setSpeedLimitAltitude);
    source.longestRunway().map(Integer::longValue).ifPresent(target::setLongestRunway);
    source.longestRunwaySurfaceCode().map(Enum::name).map(CifpXmlPoints::surface).ifPresent(target::setLongestRunwaySurfaceCode);
    return target;
  }

  private static Heliport heliport(ArincHeliport source) {
    Heliport target = new Heliport();
    point(target, "heliport", source.sectionCode().name(), source.heliportIdentifier(), source.heliportIcaoRegion(), "", source.latitude(), source.longitude());
    record(target, source.recordType().name(), Optional.of(source.customerAreaCode().name()), source.cycleDate().orElse(null));
    source.heliportName().ifPresent(target::setName);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.magneticVariation().map(CifpXmlPoints::magneticVariation).ifPresent(target::setMagneticVariation);
    target.setElevation(source.heliportElevation().orElseThrow(
        () -> new IllegalArgumentException("CIFP heliport has no elevation: " + source.heliportIdentifier())).intValue());
    source.iataDesignator().ifPresent(target::setAtaIataDesignator);
    source.daylightTimeIndicator().ifPresent(target::setDaylightIndicator);
    source.ifrCapability().ifPresent(target::setIsIfrCapable);
    source.magneticTrueIndicator().map(Enum::name).map(CifpXmlPoints::magneticTrue).ifPresent(target::setMagneticTrueIndicator);
    source.publicMilitaryIndicator().map(Enum::name).map(CifpXmlPoints::publicMilitary).ifPresent(target::setPublicMilitaryIndicator);
    source.transitionAltitude().map(Double::intValue).ifPresent(target::setTransitionAltitude);
    source.transitionLevel().map(Double::intValue).ifPresent(target::setTransitionLevel);
    source.speedLimit().map(CifpXmlPoints::speedLimit).ifPresent(target::setSpeedLimit);
    source.speedLimitAltitude().map(CifpXmlPoints::constraint).ifPresent(target::setSpeedLimitAltitude);
    Optional.ofNullable(source.heliportType()).map(Enum::name).map(value -> switch (value) {
      case "H" -> HeliportType.HOSPITAL;
      case "O" -> HeliportType.OIL_RIG;
      case "U" -> HeliportType.OTHER;
      default -> HeliportType.NOT_PROVIDED;
    }).ifPresent(target::setHeliportType);
    return target;
  }

  private static Runway runway(ArincRunway source) {
    Runway target = new Runway();
    point(target, "runway", source.sectionCode().name(), source.runwayIdentifier(), source.airportIcaoRegion(), source.airportIdentifier(), source.latitude(), source.longitude());
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    target.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
    source.runwayLength().map(Integer::longValue).ifPresent(target::setRunwayLength);
    source.runwayWidth().map(Integer::longValue).ifPresent(target::setRunwayWidth);
    source.runwayMagneticBearing().map(CifpXmlPoints::bearing).ifPresent(target::setRunwayBearing);
    source.runwayGradient().map(BigDecimal::valueOf).ifPresent(target::setRunwayGradient);
    source.landingThresholdElevation().ifPresent(target::setLandingThresholdElevation);
    source.thresholdDisplacementDistance().map(Integer::longValue).ifPresent(target::setDisplacedThresholdDistance);
    source.thresholdCrossingHeight().map(Integer::longValue).ifPresent(target::setThresholdCrossingHeight);
    source.stopway().map(Integer::longValue).ifPresent(target::setStopway);
    source.runwayDescription().ifPresent(target::setRunwayDescription);
    return target;
  }

  private static Helipad helipad(ArincHeliport source) {
    Helipad target = new Helipad();
    point(target, "helipad", "H", source.padIdentifier().orElseThrow(), source.heliportIcaoRegion(),
        source.heliportIdentifier(), source.latitude(), source.longitude());
    record(target, source.recordType().name(), Optional.of(source.customerAreaCode().name()), source.cycleDate().orElse(null));
    source.heliportElevation().map(Double::intValue).ifPresent(target::setElevation);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.magneticVariation().map(CifpXmlPoints::magneticVariation).ifPresent(target::setMagneticVariation);
    source.padShape().map(Enum::name).map(CifpXmlPoints::helipadShape).ifPresent(target::setHelipadShape);
    target.setHelipadTlofDimensions(helipadDimensions(source.padXDimension(), source.padYDimension(), source.padDiameter()));
    return target;
  }

  private static void reportHeliportDifferences(ArincHeliport first, ArincHeliport source, Heliport target) {
    Map<String, Object> original = heliportMetadata(first);
    heliportMetadata(source).entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
      String field = entry.getKey();
      Object value = entry.getValue();
      if (!Objects.equals(original.get(field), value)) {
        target.getNotes().add("CIFP shared heliport field " + field + " differs for pad "
            + source.padIdentifier().orElse("(unspecified)") + ": first=" + original.get(field) + ", pad=" + value);
      }
    });
  }

  private static Map<String, Object> heliportMetadata(ArincHeliport source) {
    // Position, elevation, geometry and update cycle belong to each source pad and may legitimately differ.
    return Map.ofEntries(
        Map.entry("recordType", source.recordType()),
        Map.entry("customerAreaCode", source.customerAreaCode()),
        Map.entry("iataDesignator", source.iataDesignator()),
        Map.entry("speedLimitAltitude", source.speedLimitAltitude()),
        Map.entry("datumCode", source.datumCode()),
        Map.entry("heliportType", Optional.ofNullable(source.heliportType())),
        Map.entry("ifrCapability", source.ifrCapability()),
        Map.entry("magneticVariation", source.magneticVariation()),
        Map.entry("speedLimit", source.speedLimit()),
        Map.entry("recommendedVhfNavaid", source.recommendedVhfNavaid()),
        Map.entry("navaidIcaoRegion", source.navaidIcaoRegion()),
        Map.entry("transitionAltitude", source.transitionAltitude()),
        Map.entry("transitionLevel", source.transitionLevel()),
        Map.entry("publicMilitaryIndicator", source.publicMilitaryIndicator()),
        Map.entry("daylightTimeIndicator", source.daylightTimeIndicator()),
        Map.entry("magneticTrueIndicator", source.magneticTrueIndicator()),
        Map.entry("heliportName", source.heliportName()));
  }

  private static Helipad helipad(ArincHelipad source) {
    Helipad target = new Helipad();
    point(target, "helipad", source.sectionCode().name(), source.helipadIdentifier(), source.icaoCode(), source.airportHeliportIdentifier(), source.latitude(), source.longitude());
    record(target, source.recordType().name(), Optional.of(source.customerAreaCode().name()), source.cycle());
    source.padElevation().map(Double::intValue).ifPresent(target::setElevation);
    Optional.ofNullable(source.padShape()).map(CifpXmlPoints::helipadShape).ifPresent(target::setHelipadShape);
    target.setHelipadTlofDimensions(helipadDimensions(source.padXDimension(), source.padYDimension(), source.padDiameter()));
    source.helipadSurfaceCode().map(CifpXmlPoints::surface).ifPresent(target::setSurfaceCode);
    return target;
  }

  private static HelipadShape helipadShape(String value) {
    return switch (value) {
      case "C" -> HelipadShape.CIRCLE;
      case "S" -> HelipadShape.SQUARE_OR_RECTANGLE;
      case "R" -> HelipadShape.RUNWAY;
      case "U" -> HelipadShape.UNDEFINED;
      default -> throw new IllegalArgumentException("Unsupported helipad shape: " + value);
    };
  }

  private static HelipadDimensions helipadDimensions(Optional<Double> x, Optional<Double> y, Optional<Double> diameter) {
    HelipadDimensions target = new HelipadDimensions();
    if (x.isPresent() && y.isPresent()) {
      target.setPadLengthLongSide((int) Math.max(x.orElseThrow(), y.orElseThrow()));
      target.setPadLengthShortSide((int) Math.min(x.orElseThrow(), y.orElseThrow()));
    }
    diameter.map(Double::intValue).ifPresent(target::setPadDiameter);
    return target;
  }

  private static void addWaypoint(ArincWaypoint source, AeroPublication publication, CifpXmlReferences refs) {
    String portIdent = source.airportIdentifier().orElse("");
    Waypoint target;
    if (source.sectionCode().name().equals("E")) {
      target = new Waypoint();
      publication.getEnrouteWaypoints().getWaypoint().add(target);
    } else {
      TerminalWaypoint terminal = new TerminalWaypoint();
      refs.port(source.sectionCode().name(), portIdent, source.airportIcaoRegion().orElseThrow()).getTerminalWaypoint().add(terminal);
      target = terminal;
    }
    point(target, "waypoint", source.sectionCode().name(), source.waypointIdentifier(), source.waypointIcaoRegion(), portIdent, source.latitude(), source.longitude());
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    source.waypointNameDescription().ifPresent(target::setName);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.magneticVariation().map(CifpXmlPoints::magneticVariation).ifPresent(target::setMagneticVariation);
    source.nameFormat().map(CifpXmlPoints::nameFormat).ifPresent(target::setNameFormatIndicator);
    source.waypointType().ifPresent(value -> target.getNotes().add("CIFP waypointType=" + value));
    source.waypointUsage().ifPresent(value -> {
      WaypointUsage usage = new WaypointUsage();
      char level = value.charAt(1);
      usage.setIsHi(level == 'H' || level == 'B');
      usage.setIsLo(level == 'L' || level == 'B');
      usage.setIsTerminal(level == ' ');
      target.setWaypointUsage(usage);
      WaypointType type = new WaypointType();
      type.setIsRnav(value.charAt(0) == 'R');
      target.setWaypointType(type);
    });
    refs.addFix(source.sectionCode().name(), source.subSectionCode().orElse(""), source.waypointIdentifier(),
        source.waypointIcaoRegion(), portIdent, target);
  }

  private static void addVhf(ArincVhfNavaid source, AeroPublication publication, CifpXmlReferences refs) {
    String navaidClass = source.navaidClass().orElse("     ");
    Vor vor = null;
    if (navaidClass.charAt(0) == 'V') {
      vor = new Vor();
      vhfPoint(vor, source, "vor", source.vhfIdentifier(), source.latitude(), source.longitude(), refs);
      // The fixed-width parser's shared VOR/NDB frequency field scales by 10, not the VHF-specific 100.
      source.vhfFrequency().map(value -> frequency(value / 10.0, FreqUnitOfMeasure.MEGA_HERTZ)).ifPresent(vor::setVorFrequency);
      vor.setNavaidClass(vhfClass(navaidClass));
      source.stationDeclination().map(CifpXmlPoints::stationDeclination).ifPresent(vor::setStationDeclination);
      source.figureOfMerit().map(CifpXmlPoints::figureOfMerit).ifPresent(vor::setFigureOfMerit);
      source.frequencyProtectionDistance().map(Double::longValue).ifPresent(vor::setFrequencyProtection);
      publication.getVhfNavaids().getVhfNavaid().add(vor);
    }
    DmeTacan dme = null;
    if (source.dmeIdentifier().isPresent() || navaidClass.charAt(1) != ' ') {
      dme = switch (navaidClass.charAt(1)) {
        case 'T' -> new Tacan();
        case 'M' -> new MilitaryTacan();
        default -> new Dme();
      };
      String ident = source.dmeIdentifier().orElse(source.vhfIdentifier());
      vhfPoint(dme, source, "dme", ident, source.dmeLatitude().orElse(source.latitude()), source.dmeLongitude().orElse(source.longitude()), refs);
      source.vhfFrequency().map(value -> frequency(value / 10.0, FreqUnitOfMeasure.MEGA_HERTZ)).ifPresent(dme::setFrequency);
      source.dmeElevation().map(Double::intValue).ifPresent(dme::setElevation);
      source.ilsDmeBias().map(BigDecimal::valueOf).ifPresent(dme::setIlsDmeBias);
      source.figureOfMerit().map(CifpXmlPoints::figureOfMerit).ifPresent(dme::setFigureOfMerit);
      source.frequencyProtectionDistance().map(Double::longValue).ifPresent(dme::setFrequencyProtection);
      dme.setNavaidClass(vhfClass(navaidClass));
      dme.setIsIlsComponent(navaidClass.charAt(1) == 'I');
      dme.setIsMlsP(navaidClass.charAt(1) == 'P');
      if (dme instanceof Tacan tacan) {
        source.stationDeclination().map(CifpXmlPoints::stationDeclination).ifPresent(tacan::setStationDeclination);
      } else if (dme instanceof MilitaryTacan tacan) {
        source.stationDeclination().map(CifpXmlPoints::stationDeclination).ifPresent(tacan::setStationDeclination);
      }
      publication.getVhfNavaids().getVhfNavaid().add(dme);
    }
    Navaid primary = dme;
    if (vor != null) {
      vor.setDmeTacanRef(dme);
      primary = vor;
    }
    if (primary == null) {
      throw new IllegalArgumentException("CIFP navaid has no supported VHF component: " + source.vhfIdentifier());
    }
    refs.addFix("D", "", source.vhfIdentifier(), source.vhfIcaoRegion(), "", primary);
  }

  private static void vhfPoint(Navaid target, ArincVhfNavaid source, String kind, String ident, double latitude, double longitude, CifpXmlReferences refs) {
    point(target, kind, source.sectionCode().name(), ident, source.vhfIcaoRegion(), source.airportIdentifier().orElse(""), latitude, longitude);
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    source.vhfNavaidName().ifPresent(target::setName);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.airportIdentifier().ifPresent(port -> target.setPortRef(refs.port(port, source.airportIcaoRegion().orElse(""))));
  }

  private static void addNdb(ArincNdbNavaid source, AeroPublication publication, CifpXmlReferences refs) {
    String portIdent = source.airportIdentifier().orElse("");
    Ndb target;
    if (source.sectionCode().name().equals("D")) {
      target = new Ndb();
      publication.getEnrouteNdbs().getNdb().add(target);
    } else {
      TerminalNdb terminal = new TerminalNdb();
      Port port = refs.port(source.sectionCode().name(), portIdent, source.airportIcaoRegion().orElseThrow());
      port.getTerminalNdb().add(terminal);
      terminal.setPortRef(port);
      target = terminal;
    }
    point(target, "ndb", source.sectionCode().name(), source.ndbIdentifier(), source.ndbIcaoRegion(), portIdent, source.latitude(), source.longitude());
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    source.ndbNavaidName().ifPresent(target::setName);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.magneticVariation().map(CifpXmlPoints::magneticVariation).ifPresent(target::setMagneticVariation);
    source.ndbFrequency().map(value -> frequency(value, FreqUnitOfMeasure.KILO_HERTZ)).ifPresent(target::setNdbFrequency);
    source.navaidClass().map(CifpXmlPoints::ndbClass).ifPresent(target::setNdbClass);
    if (source.sectionCode().name().equals("D")) {
      source.airportIdentifier().ifPresent(port -> target.setPortRef(refs.port(port, source.airportIcaoRegion().orElse(""))));
    }
    refs.addFix(source.sectionCode().name(), source.subSectionCode().orElse(""), source.ndbIdentifier(), source.ndbIcaoRegion(), portIdent, target);
  }

  private static LocalizerGlideslope localizer(ArincLocalizerGlideSlope source, CifpXmlReferences refs) {
    LocalizerGlideslope target = new LocalizerGlideslope();
    target.setReferenceId(id("localizer", source.sectionCode().name(), source.localizerIdentifier(), source.airportIcaoRegion(), source.airportIdentifier()));
    target.setIdentifier(source.localizerIdentifier());
    target.setIcaoCode(source.airportIcaoRegion());
    if (source.localizerLatitude().isPresent() && source.localizerLongitude().isPresent()) {
      target.setLocation(location(source.localizerLatitude().orElseThrow(), source.localizerLongitude().orElseThrow()));
    }
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name), source.lastUpdateCycle());
    target.setPortRef(refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion()));
    target.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
    target.setRunwayRef(refs.fix("P", "G", source.runwayIdentifier(), source.airportIcaoRegion(), source.airportIdentifier()));
    source.ilsMlsGlsCategory().map(CifpXmlPoints::category).ifPresent(target::setCategory);
    source.localizerFrequency().map(value -> frequency(value, FreqUnitOfMeasure.MEGA_HERTZ)).ifPresent(target::setLocalizerGlideslopeFrequency);
    source.localizerBearing().map(CifpXmlPoints::bearing).ifPresent(target::setApproachCourseBearing);
    source.glideSlopeAngle().map(BigDecimal::valueOf).ifPresent(target::setApproachAngle);
    if (source.glideSlopeLatitude().isPresent() && source.glideSlopeLongitude().isPresent()) {
      target.setGlideslopeLocation(location(source.glideSlopeLatitude().orElseThrow(), source.glideSlopeLongitude().orElseThrow()));
    }
    source.localizerPosition().map(Integer::longValue).ifPresent(target::setLocalizerPosition);
    source.glideSlopePosition().map(Integer::longValue).ifPresent(target::setGlideslopePosition);
    source.localizerWidth().map(BigDecimal::valueOf).ifPresent(target::setLocalizerWidth);
    source.stationDeclination().map(CifpXmlPoints::stationDeclination).ifPresent(target::setStationDeclination);
    source.glideSlopeHeightAtLandingThreshold().map(Integer::longValue).ifPresent(target::setGlideslopeHeightAtLandingThreshold);
    source.glideSlopeElevation().map(Double::intValue).ifPresent(target::setElevation);
    source.localizerPositionReference().map(value -> switch (value) {
      case " " -> LocalizerAzimuthPositionReference.BEYOND_STOP_END;
      case "+" -> LocalizerAzimuthPositionReference.BEFORE_APPROACH_END;
      case "-" -> LocalizerAzimuthPositionReference.OFF_TO_SIDE;
      default -> null;
    }).ifPresent(target::setLocalizerPositionReference);
    source.supportingFacilityIdentifier().ifPresent(ident -> target.setSupportingFacilityReference(refs.fix(
        source.supportingFacilitySectionCode().map(Enum::name).orElse("D"), source.supportingFacilitySubSectionCode().orElse(""),
        ident, source.supportingFacilityIcaoCode().orElse(""), source.airportIdentifier())));
    return target;
  }

  private static Gls gls(ArincGnssLandingSystem source, CifpXmlReferences refs) {
    Gls target = new Gls();
    point(target, "gls", source.sectionCode().name(), source.glsRefPathIdentifier(), source.airportIcaoRegion(), source.airportIdentifier(), source.stationLatitude(), source.stationLongitude());
    record(target, source.recordType().name(), Optional.of(source.customerAreaCode().name()), source.lastUpdatedCycle());
    target.setPortRef(refs.port(source.sectionCode().name(), source.airportIdentifier(), source.airportIcaoRegion()));
    target.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
    target.setRunwayRef(refs.fix("P", "G", source.runwayIdentifier(), source.airportIcaoRegion(), source.airportIdentifier()));
    target.setGlsChannel(source.glsChannel());
    target.setMagneticVariation(magneticVariation(source.magneticVariation()));
    source.glsCategory().map(CifpXmlPoints::category).ifPresent(target::setCategory);
    source.glsApproachBearing().map(CifpXmlPoints::bearing).ifPresent(target::setApproachCourseBearing);
    source.glsApproachSlope().map(BigDecimal::valueOf).ifPresent(target::setApproachAngle);
    source.serviceVolumeRadius().map(Integer::longValue).ifPresent(target::setServiceVolumeRadius);
    source.stationElevation().map(Double::intValue).ifPresent(target::setElevation);
    source.stationElevationWgs84().map(Double::intValue).ifPresent(target::setStationElevationWgs84);
    source.datumCode().map(DatumCode::fromValue).ifPresent(target::setDatumCode);
    source.stationType().ifPresent(target::setStationType);
    source.tdmaSlots().ifPresent(target::setTdmaSlots);
    source.glidePathTCH().map(Integer::longValue).ifPresent(target::setThresholdCrossingHeight);
    source.glsStationIdent().ifPresent(value -> target.getNotes().add("CIFP glsStationIdent=" + value));
    return target;
  }

  private static void point(A424Point target, String kind, String section, String ident, String icao, String port, double latitude, double longitude) {
    target.setReferenceId(id(kind, section, ident, icao, port));
    target.setIdentifier(ident);
    target.setIcaoCode(icao);
    target.setLocation(location(latitude, longitude));
  }

  private static void record(A424Record target, String type, Optional<String> area, String cycle) {
    target.setRecordType(switch (type) {
      case "S" -> RecordType.STANDARD;
      case "T" -> RecordType.TAILORED;
      default -> throw new IllegalArgumentException("Unsupported record type: " + type);
    });
    area.map(AreaCode::fromValue).ifPresent(target::setAreaCode);
    target.setCycleDate(cycle);
  }

  static Location location(double latitude, double longitude) {
    // Convert the parsed coordinate back to its native hundredth-of-arcsecond precision, with carrying at 60 seconds.
    long latHundredths = Math.round(Math.abs(latitude) * 360_000);
    Latitude lat = new Latitude();
    lat.setDeg((int) (latHundredths / 360_000));
    lat.setMin((int) (latHundredths / 6_000 % 60));
    lat.setSec((int) (latHundredths / 100 % 60));
    lat.setHSec((int) (latHundredths % 100));
    lat.setNorthSouth(NorthSouth.NORTH);
    if (latitude < 0) {
      lat.setNorthSouth(NorthSouth.SOUTH);
    }
    long lonHundredths = Math.round(Math.abs(longitude) * 360_000);
    Longitude lon = new Longitude();
    lon.setDeg((int) (lonHundredths / 360_000));
    lon.setMin((int) (lonHundredths / 6_000 % 60));
    lon.setSec((int) (lonHundredths / 100 % 60));
    lon.setHSec((int) (lonHundredths % 100));
    lon.setEastWest(EastWest.EAST);
    if (longitude < 0) {
      lon.setEastWest(EastWest.WEST);
    }
    Location target = new Location();
    target.setLatitude(lat);
    target.setLongitude(lon);
    return target;
  }

  static Bearing bearing(double value) {
    Bearing target = new Bearing();
    target.setBearingValue(BigDecimal.valueOf(value));
    target.setIsTrueBearing(false);
    return target;
  }

  static MagneticVariation magneticVariation(double value) {
    MagneticVariation target = new MagneticVariation();
    target.setMagneticVariationValue(BigDecimal.valueOf(Math.abs(value)));
    target.setMagneticVariationEWT(MagneticVariationEWT.EAST);
    if (value < 0) {
      target.setMagneticVariationEWT(MagneticVariationEWT.WEST);
    }
    return target;
  }

  private static StationDeclination stationDeclination(double value) {
    StationDeclination target = new StationDeclination();
    target.setStationDeclinationValue(BigDecimal.valueOf(Math.abs(value)));
    target.setStationDeclinationEWT(StationDeclinationEWT.EAST);
    if (value < 0) {
      target.setStationDeclinationEWT(StationDeclinationEWT.WEST);
    }
    return target;
  }

  static RunwayIdentifier runwayIdentifier(String value) {
    String runway = value;
    if (runway.startsWith("RW")) {
      runway = runway.substring(2);
    }
    // CIFP also contains compass-named water runways (N, NE, ...). Their identifier is retained on A424Point.
    if (runway.length() < 2 || !Character.isDigit(runway.charAt(0)) || !Character.isDigit(runway.charAt(1))) {
      return null;
    }
    RunwayIdentifier target = new RunwayIdentifier();
    target.setRunwayNumber(Long.parseLong(runway.substring(0, 2)));
    if (runway.length() > 2) {
      switch (runway.charAt(2)) {
        case 'L' -> target.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.LEFT);
        case 'R' -> target.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.RIGHT);
        case 'C' -> target.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.CENTER);
        case 'W' -> target.setRunwaySuffix(RunwaySuffix.WATER_SEALANE_OR_WATERWAY);
        case 'G' -> target.setRunwaySuffix(RunwaySuffix.GLIDER_RUNWAY);
        case 'U' -> target.setRunwaySuffix(RunwaySuffix.ULTRALIGHT_RUNWAY);
        // Retain unrepresentable CIFP suffixes in the point's complete textual identifier.
        default -> { return null; }
      }
    }
    return target;
  }

  private static Frequency frequency(double value, FreqUnitOfMeasure unit) {
    Frequency target = new Frequency();
    target.setFrequencyValue(BigDecimal.valueOf(value));
    target.setFreqUnitOfMeasure(unit);
    return target;
  }

  private static Constraint constraint(double value) {
    Constraint target = new Constraint();
    target.setAltitude((int) value);
    target.setIsFlightLevel(false);
    return target;
  }

  private static SpeedLimit speedLimit(int value) {
    SpeedLimit target = new SpeedLimit();
    target.setAtOrBelow((long) value);
    return target;
  }

  private static MagneticTrueIndicator magneticTrue(String value) {
    return switch (value) {
      case "M" -> MagneticTrueIndicator.MAGNETIC;
      case "T" -> MagneticTrueIndicator.TRUE;
      default -> throw new IllegalArgumentException("Unsupported magnetic/true indicator: " + value);
    };
  }

  private static PublicMilitaryIndicator publicMilitary(String value) {
    return switch (value) {
      case "C" -> PublicMilitaryIndicator.CIVIL;
      case "M" -> PublicMilitaryIndicator.MILITARY;
      case "P" -> PublicMilitaryIndicator.PRIVATE;
      case "J" -> PublicMilitaryIndicator.JOINT;
      default -> throw new IllegalArgumentException("Unsupported public/military indicator: " + value);
    };
  }

  private static RunwaySurfaceCode surface(String value) {
    return switch (value) {
      case "H" -> RunwaySurfaceCode.HARD;
      case "S" -> RunwaySurfaceCode.SOFT;
      case "W" -> RunwaySurfaceCode.WATER;
      case "U" -> RunwaySurfaceCode.UNDEFINED;
      default -> throw new IllegalArgumentException("Unsupported runway surface: " + value);
    };
  }

  private static NameFormatIndicator nameFormat(String value) {
    return switch (value.charAt(0)) {
      case 'A' -> NameFormatIndicator.ABEAM;
      case 'B' -> NameFormatIndicator.BEARING_DISTANCE;
      case 'D' -> NameFormatIndicator.AIRPORT_NAME;
      case 'F' -> NameFormatIndicator.FIR;
      case 'H' -> NameFormatIndicator.PHONETIC_LETTER_NAME;
      case 'I' -> NameFormatIndicator.AIRPORT_IDENT;
      case 'L' -> NameFormatIndicator.LAT_LONG;
      case 'M' -> NameFormatIndicator.MULTIPLE_WORD;
      case 'N' -> NameFormatIndicator.NAVAID;
      case 'P' -> NameFormatIndicator.PUBLISHED_FIVE_LETTER_NAME;
      case 'Q' -> NameFormatIndicator.PUBLISHED_LESS_THAN_FIVE_LETTER_NAME;
      case 'R' -> NameFormatIndicator.PUBLISHED_MORE_THAN_FIVE_NAME;
      case 'T' -> NameFormatIndicator.APT_RWY_RELATED;
      case 'U' -> NameFormatIndicator.UIR;
      default -> switch (value.charAt(1)) {
        case 'O' -> NameFormatIndicator.OFFICIAL_FIVE_LETTER;
        case 'M' -> NameFormatIndicator.NO_PUBLISHED_FIVE_LETTER;
        default -> null;
      };
    };
  }

  private static FigureOfMerit figureOfMerit(int value) {
    return switch (value) {
      case 0 -> FigureOfMerit.TERMINAL_USE;
      case 1 -> FigureOfMerit.LOW_ALT;
      case 2 -> FigureOfMerit.HIGH_ALT;
      case 3 -> FigureOfMerit.EXTENDED_HIGH_ALT;
      case 7 -> FigureOfMerit.NOT_NOTA_MD;
      case 9 -> FigureOfMerit.OUT_OF_SERVICE;
      default -> throw new IllegalArgumentException("Unsupported figure of merit: " + value);
    };
  }

  private static VhfNavaidClass vhfClass(String value) {
    VhfNavaidClass target = new VhfNavaidClass();
    target.setVhfNavaidCoverage(switch (value.charAt(2)) {
      case 'T' -> VhfNavaidCoverage.TERMINAL;
      case 'L' -> VhfNavaidCoverage.LOW;
      case 'H' -> VhfNavaidCoverage.HIGH;
      case 'U' -> VhfNavaidCoverage.UNDEFINED;
      case 'C' -> VhfNavaidCoverage.ILS_TACAN;
      default -> null;
    });
    target.setVhfNavaidWeatherInfo(weather(value.charAt(3)));
    target.setIsBiased(value.charAt(3) == 'D');
    target.setIsNoVoice(value.charAt(3) == 'W');
    target.setIsNotCollocated(value.charAt(4) == 'N');
    return target;
  }

  private static NdbNavaidClass ndbClass(String value) {
    NdbNavaidClass target = new NdbNavaidClass();
    target.setNdbNavaidType(switch (value.charAt(0)) {
      case 'H' -> NdbNavaidType.NDB;
      case 'S' -> NdbNavaidType.SABH;
      case 'M' -> NdbNavaidType.MARINE_BEACON;
      default -> null;
    });
    target.setNdbNavaidIfMarker(switch (value.charAt(1)) {
      case 'I' -> NdbNavaidIfMarkerInfo.INNER_MARKER;
      case 'M' -> NdbNavaidIfMarkerInfo.MIDDLE_MARKER;
      case 'O' -> NdbNavaidIfMarkerInfo.OUTER_MARKER;
      case 'C' -> NdbNavaidIfMarkerInfo.BACK_MARKER;
      default -> null;
    });
    target.setNdbNavaidCoverage(switch (value.charAt(2)) {
      case 'H' -> NdbNavaidCoverage.HIGH_POWER_NDB;
      case 'M' -> NdbNavaidCoverage.LOW_POWER_NDB;
      case 'L' -> NdbNavaidCoverage.LOCATOR;
      case ' ' -> NdbNavaidCoverage.NDB;
      default -> null;
    });
    target.setNdbNavaidWeatherInfo(weather(value.charAt(3)));
    target.setIsNoVoice(value.charAt(3) == 'W');
    target.setIsBfoRequired(value.charAt(4) == 'B');
    return target;
  }

  private static NavaidWeatherInfo weather(char value) {
    return switch (value) {
      case 'A' -> NavaidWeatherInfo.AUTOMATED;
      case 'B' -> NavaidWeatherInfo.SCHEDULED;
      default -> null;
    };
  }

  private static PrecisionApproachCategory category(String value) {
    return switch (value) {
      case "0" -> PrecisionApproachCategory.ILS_LOC_ONLY;
      case "1" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_1;
      case "2" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_2;
      case "3" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_3;
      case "I" -> PrecisionApproachCategory.IGS;
      case "L" -> PrecisionApproachCategory.LDA_GLIDESLOPE;
      case "A" -> PrecisionApproachCategory.LDA_NO_GLIDESLOPE;
      case "S" -> PrecisionApproachCategory.SDF_GLIDESLOPE;
      case "F" -> PrecisionApproachCategory.SDF_NO_GLIDE_SLOPE;
      default -> throw new IllegalArgumentException("Unsupported precision approach category: " + value);
    };
  }
}
