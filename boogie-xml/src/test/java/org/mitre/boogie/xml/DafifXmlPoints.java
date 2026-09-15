package org.mitre.boogie.xml;

import static org.mitre.boogie.xml.CifpXmlPoints.bearing;
import static org.mitre.boogie.xml.CifpXmlPoints.location;
import static org.mitre.boogie.xml.CifpXmlPoints.magneticVariation;
import static org.mitre.boogie.xml.CifpXmlPoints.runwayIdentifier;
import static org.mitre.boogie.xml.DafifXmlReferences.id;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mitre.boogie.xml.v23_4.generated.A424Point;
import org.mitre.boogie.xml.v23_4.generated.A424Record;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.AirportHeliportLocalizerMarker;
import org.mitre.boogie.xml.v23_4.generated.Airports;
import org.mitre.boogie.xml.v23_4.generated.Bearing;
import org.mitre.boogie.xml.v23_4.generated.DatumCode;
import org.mitre.boogie.xml.v23_4.generated.Dme;
import org.mitre.boogie.xml.v23_4.generated.DmeTacan;
import org.mitre.boogie.xml.v23_4.generated.EnrouteNdbs;
import org.mitre.boogie.xml.v23_4.generated.EnrouteWaypoints;
import org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure;
import org.mitre.boogie.xml.v23_4.generated.Frequency;
import org.mitre.boogie.xml.v23_4.generated.LocalizerAzimuthPositionReference;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.mitre.boogie.xml.v23_4.generated.MarkerType;
import org.mitre.boogie.xml.v23_4.generated.Navaid;
import org.mitre.boogie.xml.v23_4.generated.Navaids;
import org.mitre.boogie.xml.v23_4.generated.Ndb;
import org.mitre.boogie.xml.v23_4.generated.PrecisionApproachCategory;
import org.mitre.boogie.xml.v23_4.generated.PublicMilitaryIndicator;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.boogie.xml.v23_4.generated.Runway;
import org.mitre.boogie.xml.v23_4.generated.StationDeclination;
import org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT;
import org.mitre.boogie.xml.v23_4.generated.SurfaceType;
import org.mitre.boogie.xml.v23_4.generated.Tacan;
import org.mitre.boogie.xml.v23_4.generated.TerminalNdb;
import org.mitre.boogie.xml.v23_4.generated.TerminalWaypoint;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.boogie.xml.v23_4.generated.WaypointType;
import org.mitre.boogie.xml.v23_4.generated.WaypointUsage;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.mitre.tdp.boogie.dafif.utils.DafifMagVars;

/** Maps the supported DAFIF point tables to the XML fixture, retaining unmatched source codes in notes. */
final class DafifXmlPoints {
  private DafifXmlPoints() {}

  static void populate(DafifXmlFixture.Records records, AeroPublication publication, DafifXmlReferences refs) {
    publication.setAirports(new Airports());
    publication.setEnrouteWaypoints(new EnrouteWaypoints());
    publication.setVhfNavaids(new Navaids());
    publication.setEnrouteNdbs(new EnrouteNdbs());
    for (DafifAirport source : records.airports()) {
      Airport target = airport(source);
      publication.getAirports().getAirport().add(target);
      refs.addAirport(source.airportIdentification(), target);
      if (source.icaoCode().length() == 4) {
        refs.addAirportAlias(source.icaoCode(), target);
      }
      source.faaHostCountryIdentifier().filter(value -> !value.equals("N"))
          .ifPresent(value -> refs.addAirportAlias(value, target));
    }
    for (DafifRunway source : records.runways()) {
      Airport airport = refs.airport(source.airportIdentification());
      for (boolean high : new boolean[] {true, false}) {
        Runway target = runway(source, high, airport);
        airport.getRunway().add(target);
        refs.addRunway(source.airportIdentification(), target.getIdentifier(), target);
      }
      if (airport.getLongestRunway() == null || source.length() > airport.getLongestRunway()) {
        airport.setLongestRunway(source.length().longValue());
      }
    }
    for (DafifAddRunway source : records.addRunways()) {
      addRunway(source, true, refs);
      addRunway(source, false, refs);
    }
    for (DafifNavaid source : records.navaids()) {
      addNavaid(source, publication, refs);
    }
    for (DafifWaypoint source : records.waypoints()) {
      addWaypoint(source, publication, refs);
    }
    addIls(records, publication, refs);
  }

  private static Airport airport(DafifAirport source) {
    Airport target = new Airport();
    String ident = source.airportIdentification();
    if (source.icaoCode().length() == 4) {
      ident = source.icaoCode();
    } else if (source.faaHostCountryIdentifier().filter(value -> !value.equals("N")).isPresent()) {
      ident = source.faaHostCountryIdentifier().orElseThrow();
    }
    point(target, id("airport", source.airportIdentification()), ident, source.icaoCode(),
        source.degreesLatitude(), source.degreesLongitude(), source.cycleDate());
    target.setName(source.name());
    target.setElevation(source.elevation());
    source.geodeticDatum().ifPresent(value -> datum(target, value));
    target.setMagneticVariation(magneticVariation(DafifMagVars.fromDynamic(source.magneticVariation()).angle().inDegrees()));
    if (source.magVarOfRecord().isPresent()) {
      target.setMagneticVariation(magneticVariation(DafifMagVars.fromRecord(source.magVarOfRecord().orElseThrow()).angle().inDegrees()));
    }
    switch (source.airportType()) {
      case "A" -> target.setPublicMilitaryIndicator(PublicMilitaryIndicator.CIVIL);
      case "B" -> target.setPublicMilitaryIndicator(PublicMilitaryIndicator.JOINT);
      case "C" -> target.setPublicMilitaryIndicator(PublicMilitaryIndicator.MILITARY);
      default -> { /* D describes facilities, not civil/military ownership. */ }
    }
    note(target, "ARPT_IDENT", source.airportIdentification());
    note(target, "TYPE", source.airportType());
    note(target, "OPR_AGY", source.primaryOperatingAgency());
    note(target, "MAG_VAR", source.magneticVariation());
    source.magVarOfRecord().ifPresent(value -> note(target, "MV_RECORD", value));
    source.secondaryName().ifPresent(value -> note(target, "SEC_NAME", value));
    source.secondaryIcaoCode().ifPresent(value -> note(target, "SEC_ICAO", value));
    source.secondaryFaaHost().ifPresent(value -> note(target, "SEC_FAA", value));
    return target;
  }

  private static Runway runway(DafifRunway source, boolean high, Airport airport) {
    String ident = high ? source.highEndIdentifier() : source.lowEndIdentifier();
    Optional<Double> latitude = high ? source.highEndDegreesLatitude() : source.lowEndDegreesLatitude();
    Optional<Double> longitude = high ? source.highEndDegreesLongitude() : source.lowEndDegreesLongitude();
    Runway target = new Runway();
    point(target, id("runway", source.airportIdentification(), ident), ident, airport.getIcaoCode(),
        latitude, longitude, source.cycleDate());
    target.setRunwayEndLocation(target.getLocation());
    target.setRunwayIdentifier(runwayIdentifier(ident));
    target.setRunwayLength(source.length().longValue());
    target.setRunwayWidth(source.width().longValue());
    (high ? source.highEndMagneticHeading() : source.lowEndMagneticHeading()).map(CifpXmlPoints::bearing).ifPresent(target::setRunwayBearing);
    Optional.ofNullable(high ? source.trueHeadingHighEnd() : source.trueHeadingLowEnd())
        .map(BigDecimal::valueOf).ifPresent(target::setRunwayTrueBearing);
    number(high ? source.highEndSlope() : source.lowEndSlope()).ifPresent(target::setRunwayGradient);
    Optional<String> elevation = high ? source.highEndElevation() : source.lowEndElevation();
    elevation.flatMap(DafifXmlPoints::number).map(DafifXmlPoints::feet).ifPresent(target::setRunwayEndElevation);
    elevation.flatMap(DafifXmlPoints::number).map(DafifXmlPoints::feet).ifPresent(target::setLandingThresholdElevation);
    (high ? source.highEndTDZE() : source.lowEndTDZE()).flatMap(DafifXmlPoints::number)
        .map(DafifXmlPoints::feet).ifPresent(target::setTouchDownZoneElevation);
    Optional<Integer> displaced = high ? source.highEndDisplacedThreshold() : source.lowEndDisplacedThreshold();
    displaced.map(Integer::longValue).ifPresent(target::setDisplacedThresholdDistance);
    if (displaced.orElse(0) > 0) {
      // RWY coordinates identify the physical end; ADD_RWY supplies the landing threshold position.
      target.setLocation(null);
      target.setLandingThresholdElevation(null);
      (high ? source.highEndDisplacedThresholdElevation() : source.lowEndDisplacedThresholdElevation())
          .flatMap(DafifXmlPoints::number).map(DafifXmlPoints::feet).ifPresent(target::setLandingThresholdElevation);
    }
    (high ? source.highEndLandingDistance() : source.lowEndLandingDistance()).map(Integer::longValue).ifPresent(target::setLandingDistanceAvailable);
    (high ? source.highEndRunwayDistance() : source.lowEndRunwayDistance()).map(Integer::longValue).ifPresent(target::setTakeOffRunwayAvailable);
    (high ? source.highEndTakeOffDistance() : source.lowEndTakeOffDistance()).map(Integer::longValue).ifPresent(target::setTakeOffDistanceAvailable);
    (high ? source.highEndAccelerateStopDistance() : source.lowEndAccelerateStopDistance()).map(Integer::longValue).ifPresent(target::setAccelerateStopDistanceAvailable);
    source.surface().ifPresent(value -> { target.setSurfaceType(surface(value)); note(target, "SURFACE", value); });
    source.usableRunway().ifPresent(value -> note(target, "CLD_RWY", value));
    source.pavementClassificationNumber().ifPresent(value -> note(target, "PCN", value));
    (high ? source.highEndLightingSystem() : source.lowEndLightingSystem()).ifPresent(value -> note(target, "LIGHTING", value.toString()));
    elevation.ifPresent(value -> note(target, "RWY_END_ELEV_FEET", value));
    return target;
  }

  private static void addRunway(DafifAddRunway source, boolean high, DafifXmlReferences refs) {
    String ident = high ? source.highEndRunwayIdentifier() : source.lowEndRunwayIdentifier();
    Runway target = refs.runway(source.airportIdentification(), ident);
    if (target == null) {
      throw new IllegalArgumentException("ADD_RWY has no runway: " + source.airportIdentification() + "/" + ident);
    }
    Optional<Double> latitude = high ? source.highEndDisplacedThresholdDegreesLatitude() : source.lowEndDisplacedThresholdDegreesLatitude();
    Optional<Double> longitude = high ? source.highEndDisplacedThresholdDegreesLongitude() : source.lowEndDisplacedThresholdDegreesLongitude();
    if (latitude.isPresent() && longitude.isPresent()) {
      target.setLocation(location(latitude.orElseThrow(), longitude.orElseThrow()));
    }
    (high ? source.highEndOverrunDistance() : source.lowEndOverrunDistance()).map(Integer::longValue).ifPresent(target::setStopway);
    note(target, "OVERRUN_SURFACE", high ? source.highEndOverrunSurface() : source.lowEndOverrunSurface());
    (high ? source.highEndOverrunDegreesLatitude() : source.lowEndOverrunDegreesLatitude()).ifPresent(value -> note(target, "OVERRUN_LAT", value.toString()));
    (high ? source.highEndOverrunDegreesLongitude() : source.lowEndOverrunDegreesLongitude()).ifPresent(value -> note(target, "OVERRUN_LONG", value.toString()));
    note(target, "ADD_RWY_CYCLE", source.cycleDate().toString());
  }

  private static void addNavaid(DafifNavaid source, AeroPublication publication, DafifXmlReferences refs) {
    int type = source.navaidType();
    Navaid primary = null;
    Vor vor = null;
    Ndb ndb = null;
    if (type == 1 || type == 2 || type == 4) {
      vor = new Vor();
      navaidPoint(vor, source, "vor", source.degreesLatitude(), source.degreesLongitude(), refs);
      source.navaidFrequencyNav().flatMap(DafifXmlPoints::frequency).ifPresent(vor::setVorFrequency);
      source.navaidSlavedVariation().map(DafifXmlPoints::stationDeclination).ifPresent(vor::setStationDeclination);
      publication.getVhfNavaids().getVhfNavaid().add(vor);
      primary = vor;
    }
    if (type == 5 || type == 7) {
      ndb = new Ndb();
      navaidPoint(ndb, source, "ndb", source.degreesLatitude(), source.degreesLongitude(), refs);
      source.navaidFrequencyNav().flatMap(DafifXmlPoints::frequency).ifPresent(ndb::setNdbFrequency);
      publication.getEnrouteNdbs().getNdb().add(ndb);
      primary = ndb;
    }
    if (type == 2 || type == 3 || type == 4 || type == 7 || type == 9) {
      DmeTacan dme = new Dme();
      if (type == 2 || type == 3) {
        dme = new Tacan();
      }
      Optional<Double> latitude = source.dmeDegreesLatitude();
      Optional<Double> longitude = source.dmeDegreesLongitude();
      if (type == 3 || type == 9) {
        latitude = source.degreesLatitude().or(() -> source.dmeDegreesLatitude());
        longitude = source.degreesLongitude().or(() -> source.dmeDegreesLongitude());
      }
      navaidPoint(dme, source, "dme", latitude, longitude, refs);
      if (type != 3 && type != 9) dme.setElevation(null);
      number(source.dmeElevation()).map(DafifXmlPoints::feet).ifPresent(dme::setElevation);
      if (type != 7) {
        source.navaidFrequencyNav().flatMap(DafifXmlPoints::frequency).ifPresent(dme::setFrequency);
      }
      if (dme instanceof Tacan tacan) {
        source.navaidSlavedVariation().map(DafifXmlPoints::stationDeclination).ifPresent(tacan::setStationDeclination);
      }
      publication.getVhfNavaids().getVhfNavaid().add(dme);
      if (vor != null) vor.setDmeTacanRef(dme);
      if (ndb != null) ndb.setDmeTacanRef(dme);
      if (primary == null) primary = dme;
    }
    if (primary == null) throw new IllegalArgumentException("Unsupported DAFIF NAV type: " + type);
    refs.addNavaid(source.navaidIdentifier(), source.countryCode(), type, source.navaidKeyCode(), primary);
  }

  private static void navaidPoint(Navaid target, DafifNavaid source, String component, Optional<Double> latitude,
      Optional<Double> longitude, DafifXmlReferences refs) {
    point(target, id("nav", source.navaidIdentifier(), source.countryCode(), source.navaidType().toString(),
        source.navaidKeyCode().toString(), component), source.navaidIdentifier(), source.icaoRegion(), latitude, longitude, source.cycleDate());
    source.name().ifPresent(target::setName);
    source.geodeticDatum().ifPresent(value -> datum(target, value));
    number(source.ilsNavaidElevation()).map(DafifXmlPoints::feet).ifPresent(target::setElevation);
    target.setMagneticVariation(magneticVariation(DafifMagVars.fromDynamic(source.magneticVariation()).angle().inDegrees()));
    source.associatedIcaoFaaHostCtryCode().map(refs::airportByIdentifier).ifPresent(target::setPortRef);
    note(target, "NAV_TYPE", source.navaidType().toString());
    note(target, "NAV_COUNTRY", source.countryCode());
    note(target, "NAV_KEY_CD", source.navaidKeyCode().toString());
    note(target, "OS", source.navaidStatus());
    note(target, "USAGE_CD", source.navaidUsageCode());
    note(target, "RCC", source.navaidRadioClassCode());
    source.navaidChannel().ifPresent(value -> note(target, "CHAN", value));
    source.frequencyProtection().ifPresent(value -> note(target, "FREQ_PROT", value));
    note(target, "NAV_RANGE", source.navaidRange());
    note(target, "POWER", source.navaidPower());
  }

  private static void addWaypoint(DafifWaypoint source, AeroPublication publication, DafifXmlReferences refs) {
    Airport airport = null;
    if ("T".equals(source.waypointUsageCode())) airport = refs.airportByIdentifier(source.icaoCode());
    Waypoint target = new Waypoint();
    if (airport == null) {
      publication.getEnrouteWaypoints().getWaypoint().add(target);
    } else {
      TerminalWaypoint terminal = new TerminalWaypoint();
      airport.getTerminalWaypoint().add(terminal);
      target = terminal;
    }
    point(target, id("waypoint", source.waypointIdentifier(), source.countryCode()), source.waypointIdentifier(),
        source.icaoCode(), source.degreesLatitude(), source.degreesLongitude(), source.cycleDate());
    source.waypointDescriptionName().ifPresent(target::setName);
    Waypoint point = target;
    source.geodeticDatum().ifPresent(value -> datum(point, value));
    target.setMagneticVariation(magneticVariation(source.magneticVariation()));
    WaypointType type = new WaypointType();
    type.setIsUnnamed(source.waypointType().contains("I"));
    type.setIsNdb(source.waypointType().contains("N"));
    type.setIsOffRoute(source.waypointType().contains("F"));
    type.setIsRnav(source.waypointType().contains("W"));
    type.setIsVfr(source.waypointType().contains("V"));
    target.setWaypointType(type);
    WaypointUsage usage = new WaypointUsage();
    usage.setIsHi("B".equals(source.waypointUsageCode()) || "H".equals(source.waypointUsageCode()));
    usage.setIsLo("B".equals(source.waypointUsageCode()) || "L".equals(source.waypointUsageCode()));
    usage.setIsTerminal("T".equals(source.waypointUsageCode()));
    target.setWaypointUsage(usage);
    note(target, "WPT_COUNTRY", source.countryCode());
    note(target, "ICAO", source.icaoCode());
    note(target, "TYPE", source.waypointType());
    note(target, "USAGE_CD", source.waypointUsageCode());
    source.waypointBearing().ifPresent(value -> note(point, "BEARING", value.toString()));
    source.distance().ifPresent(value -> note(point, "DISTANCE_NM", value.toString()));
    source.waypointRunwayIdent().ifPresent(value -> note(point, "RWY_ID", value));
    source.waypointRwyIcao().ifPresent(value -> note(point, "RWY_ICAO", value));
    if (source.navaidIdentifier().isPresent() && source.navaidCountryCode().isPresent()
        && source.navaidType().isPresent() && source.navaidKeyCode().isPresent()) {
      String ident = source.navaidIdentifier().orElseThrow();
      String country = source.navaidCountryCode().orElseThrow();
      int navType = source.navaidType().orElseThrow();
      int key = source.navaidKeyCode().orElseThrow();
      note(target, "NAV_REFERENCE", ident + "/" + country + "/" + navType + "/" + key);
      // BEARING/DISTANCE can describe a fix relative to a navaid; only explicit navaid flags alias its identity.
      if (Boolean.TRUE.equals(source.waypointPointNavaidFlag())) {
        refs.addWaypointNavaid(source.waypointIdentifier(), source.countryCode(), refs.navaid(ident, country, navType, key));
      }
    }
    refs.addWaypoint(source.waypointIdentifier(), source.countryCode(), target);
  }

  private static void addIls(DafifXmlFixture.Records records, AeroPublication publication, DafifXmlReferences refs) {
    Map<String, LocalizerGlideslope> localizers = new HashMap<>();
    for (DafifIls source : records.ils()) {
      if (!source.componentType().equals("Z")) continue;
      Airport airport = refs.airport(source.airportIdentification());
      LocalizerGlideslope target = new LocalizerGlideslope();
      ilsPoint(target, source, airport);
      target.setPortRef(airport);
      target.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
      target.setRunwayRef(refs.runway(source.airportIdentification(), source.runwayIdentifier()));
      source.ilsMlsCategory().map(DafifXmlPoints::category).ifPresent(target::setCategory);
      source.navaidFrequency().flatMap(DafifXmlPoints::frequency).ifPresent(target::setLocalizerGlideslopeFrequency);
      source.ilsBearingCourse().flatMap(DafifXmlPoints::course).ifPresent(target::setApproachCourseBearing);
      source.localizerWidth().map(BigDecimal::valueOf).ifPresent(target::setLocalizerWidth);
      source.ilsSlaveVariation().map(DafifXmlPoints::ilsStationDeclination).ifPresent(target::setStationDeclination);
      source.localizerOrGlideSlopeLocation().ifPresent(value -> localizerPosition(target, value));
      number(source.ilsNavaidElevation()).map(DafifXmlPoints::feet).ifPresent(target::setElevation);
      airport.getLocalizerGlideslope().add(target);
      localizers.put(id(source.airportIdentification(), source.runwayIdentifier()), target);
      refs.addLocalizer(source.airportIdentification(), target.getIdentifier(), target);
    }
    for (DafifIls source : records.ils()) {
      if (source.componentType().equals("Z")) continue;
      Airport airport = refs.airport(source.airportIdentification());
      LocalizerGlideslope localizer = localizers.get(id(source.airportIdentification(), source.runwayIdentifier()));
      switch (source.componentType()) {
        case "G" -> {
          if (localizer == null) {
            // An orphan glideslope is retained as a located component, without inventing a localizer position/frequency.
            localizer = new LocalizerGlideslope();
            ilsPoint(localizer, source, airport);
            localizer.setLocation(null);
            localizer.setPortRef(airport);
            localizer.setRunwayRef(refs.runway(source.airportIdentification(), source.runwayIdentifier()));
            localizer.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
            airport.getLocalizerGlideslope().add(localizer);
          }
          if (source.degreesLatitude().isPresent() && source.degreesLongitude().isPresent()) {
            localizer.setGlideslopeLocation(location(source.degreesLatitude().orElseThrow(), source.degreesLongitude().orElseThrow()));
          }
          source.ilsGlideSlopeAngle().map(BigDecimal::valueOf).ifPresent(localizer::setApproachAngle);
          source.thresholdCrossingHeight().map(Integer::longValue).ifPresent(localizer::setGlideslopeHeightAtLandingThreshold);
          number(source.ilsNavaidElevation()).map(DafifXmlPoints::feet).ifPresent(localizer::setElevation);
          LocalizerGlideslope target = localizer;
          source.localizerOrGlideSlopeLocation().ifPresent(value -> note(target, "GS_LOCATION_FEET", value));
          source.navaidFrequency().ifPresent(value -> note(target, "GS_FREQ", value));
          note(localizer, "GS_CYCLE", source.cycleDate().toString());
        }
        case "D", "P" -> {
          Dme dme = new Dme();
          ilsPoint(dme, source, airport);
          dme.setPortRef(airport);
          dme.setIsIlsComponent(true);
          dme.setIsMlsP(source.componentType().equals("P"));
          source.navaidFrequency().flatMap(DafifXmlPoints::frequency).ifPresent(dme::setFrequency);
          source.ilsDmeBias().map(BigDecimal::valueOf).ifPresent(dme::setIlsDmeBias);
          number(source.ilsNavaidElevation()).map(DafifXmlPoints::feet).ifPresent(dme::setElevation);
          publication.getVhfNavaids().getVhfNavaid().add(dme);
          refs.addIlsDme(source.airportIdentification(), source.componentType(), dme.getIdentifier(), dme);
          if (localizer != null) localizer.setSupportingFacilityReference(dme);
        }
        case "B", "I", "L", "M", "O" -> addMarker(source, airport, localizer);
        default -> throw new IllegalArgumentException("Unsupported ILS component in fixture: " + source.componentType());
      }
    }
  }

  private static void ilsPoint(A424Point target, DafifIls source, Airport airport) {
    String ident = source.ilsNavaidIdentifier().orElse(source.runwayIdentifier() + "-" + source.componentType());
    point(target, id("ils", source.airportIdentification(), source.runwayIdentifier(), source.componentType()), ident,
        airport.getIcaoCode(), source.degreesLatitude(), source.degreesLongitude(), source.cycleDate());
    source.name().ifPresent(target::setName);
    datum(target, source.geodeticDatum());
    target.setMagneticVariation(magneticVariation(DafifMagVars.fromDynamic(source.magneticVariation()).angle().inDegrees()));
    note(target, "ILS_COMPONENT", source.componentType());
    note(target, "RWY_IDENT", source.runwayIdentifier());
    source.navaidChannel().ifPresent(value -> note(target, "CHAN", value));
    source.collocation().ifPresent(value -> note(target, "COLCTN", value));
    source.ilsBearingCourse().filter(value -> value.endsWith("G")).ifPresent(value -> note(target, "GRID_BEARING", value));
  }

  private static void addMarker(DafifIls source, Airport airport, LocalizerGlideslope localizer) {
    Optional<BigDecimal> elevation = number(source.ilsNavaidElevation());
    if (source.componentType().equals("L")) {
      TerminalNdb target = new TerminalNdb();
      ilsPoint(target, source, airport);
      target.setPortRef(airport);
      elevation.map(DafifXmlPoints::feet).ifPresent(target::setElevation);
      source.navaidFrequency().flatMap(DafifXmlPoints::frequency).ifPresent(target::setNdbFrequency);
      airport.getTerminalNdb().add(target);
    } else if (elevation.isPresent()) {
      AirportHeliportLocalizerMarker target = new AirportHeliportLocalizerMarker();
      ilsPoint(target, source, airport);
      target.setElevation(feet(elevation.orElseThrow()));
      target.setLocalizerRef(localizer);
      target.setRunwayIdentifier(runwayIdentifier(source.runwayIdentifier()));
      target.setMarkerType(switch (source.componentType()) {
        case "B" -> MarkerType.BM;
        case "I" -> MarkerType.IM;
        case "M" -> MarkerType.MM;
        case "O" -> MarkerType.OM;
        default -> throw new IllegalArgumentException(source.componentType());
      });
      airport.getLocalizerMarker().add(target);
    } else {
      // LocalizerMarker requires a primitive elevation. A waypoint preserves unknown altitude without writing 0 ft.
      TerminalWaypoint target = new TerminalWaypoint();
      ilsPoint(target, source, airport);
      WaypointType type = new WaypointType();
      type.setIsBackMarker(source.componentType().equals("B"));
      type.setIsInnerMarker(source.componentType().equals("I"));
      type.setIsMiddleMarker(source.componentType().equals("M"));
      type.setIsOuterMarker(source.componentType().equals("O"));
      target.setWaypointType(type);
      note(target, "ELEV", source.ilsNavaidElevation());
      airport.getTerminalWaypoint().add(target);
    }
  }

  private static void point(A424Point target, String referenceId, String ident, String icao,
      Optional<Double> latitude, Optional<Double> longitude, Integer cycle) {
    target.setReferenceId(referenceId);
    target.setIdentifier(ident);
    if (icao != null && icao.length() >= 2) target.setIcaoCode(icao.substring(0, 2));
    if (latitude.isPresent() && longitude.isPresent()) target.setLocation(location(latitude.orElseThrow(), longitude.orElseThrow()));
    target.setRecordType(RecordType.STANDARD);
    if (cycle != null) target.setCycleDate(String.format("%04d", cycle % 10000));
  }

  private static void datum(A424Point target, String value) {
    if ("WGE".equals(value) || "WGX".equals(value)) target.setDatumCode(DatumCode.WGE);
    if (!"WGE".equals(value)) note(target, "WGS_DATUM", value);
  }

  private static void note(A424Record target, String field, String value) {
    if (value != null && !value.isBlank()) target.getNotes().add("DAFIF " + field + "=" + value);
  }

  static Optional<Frequency> frequency(String value) {
    if (value == null || !value.matches("[0-9]+[MK]")) return Optional.empty();
    Frequency target = new Frequency();
    target.setFreqUnitOfMeasure(value.endsWith("M") ? FreqUnitOfMeasure.MEGA_HERTZ : FreqUnitOfMeasure.KILO_HERTZ);
    // 112700M = 112.700 MHz and 365000K = 365.000 kHz; the final letter is not part of the numeric scale.
    target.setFrequencyValue(new BigDecimal(value.substring(0, value.length() - 1)).movePointLeft(3));
    return Optional.of(target);
  }

  private static Optional<BigDecimal> number(String value) {
    if (value == null || !value.trim().matches("[+-]?[0-9]+(?:\\.[0-9]+)?")) return Optional.empty();
    return Optional.of(new BigDecimal(value.trim()));
  }

  private static int feet(BigDecimal value) {
    return value.setScale(0, RoundingMode.HALF_UP).intValueExact();
  }

  private static StationDeclination stationDeclination(String value) {
    return stationDeclination(DafifMagVars.fromRecord(value).angle().inDegrees());
  }

  private static StationDeclination ilsStationDeclination(String value) {
    // ILS SLAVE_VAR is E/W + three whole degrees + a space + MMYY; NAV SLAVED_VAR includes tenths.
    double degrees = Double.parseDouble(value.substring(1, 4));
    if (value.charAt(0) == 'W') degrees = -degrees;
    return stationDeclination(degrees);
  }

  private static StationDeclination stationDeclination(double degrees) {
    StationDeclination target = new StationDeclination();
    target.setStationDeclinationValue(BigDecimal.valueOf(Math.abs(degrees)));
    target.setStationDeclinationEWT(StationDeclinationEWT.EAST);
    if (degrees < 0) target.setStationDeclinationEWT(StationDeclinationEWT.WEST);
    return target;
  }

  static Optional<Bearing> course(String value) {
    if (value.endsWith("G")) return Optional.empty();
    boolean isTrue = value.endsWith("T");
    String numeric = value;
    if (isTrue) numeric = value.substring(0, value.length() - 1) + "0";
    Optional<BigDecimal> number = number(numeric);
    if (number.isEmpty()) return Optional.empty();
    Bearing target = bearing(number.orElseThrow().doubleValue());
    target.setIsTrueBearing(isTrue);
    return Optional.of(target);
  }

  private static void localizerPosition(LocalizerGlideslope target, String value) {
    number(value).map(BigDecimal::abs).map(BigDecimal::longValueExact).ifPresent(target::setLocalizerPosition);
    if (value.startsWith("+")) target.setLocalizerPositionReference(LocalizerAzimuthPositionReference.BEYOND_STOP_END);
    else if (value.startsWith("-")) target.setLocalizerPositionReference(LocalizerAzimuthPositionReference.BEFORE_APPROACH_END);
    else if (number(value).isPresent()) target.setLocalizerPositionReference(LocalizerAzimuthPositionReference.OFF_TO_SIDE);
  }

  private static PrecisionApproachCategory category(String value) {
    return switch (value) {
      case "0" -> PrecisionApproachCategory.ILS_LOC_ONLY;
      case "1" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_1;
      case "2" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_2;
      case "3" -> PrecisionApproachCategory.ILS_MLS_GLS_CAT_3;
      case "A" -> PrecisionApproachCategory.LDA_NO_GLIDESLOPE;
      case "F" -> PrecisionApproachCategory.SDF_NO_GLIDE_SLOPE;
      case "I" -> PrecisionApproachCategory.IGS;
      case "L" -> PrecisionApproachCategory.LDA_GLIDESLOPE;
      case "S" -> PrecisionApproachCategory.SDF_GLIDESLOPE;
      default -> throw new IllegalArgumentException("Unknown DAFIF ILS category: " + value);
    };
  }

  private static SurfaceType surface(String value) {
    return switch (value) {
      case "AM2" -> SurfaceType.METAL;
      case "ASP" -> SurfaceType.ASPHALT;
      case "BIT" -> SurfaceType.BITUMINOUS_TAR_OR_ASPHALT;
      case "BRI" -> SurfaceType.BRICK_IS_LAID_OR_MORTARED;
      case "CLA" -> SurfaceType.CLAY;
      case "CON" -> SurfaceType.CONCRETE;
      case "COR" -> SurfaceType.CORAL;
      case "GRE" -> SurfaceType.DIRT;
      case "GRS" -> SurfaceType.GRASS;
      case "GVL" -> SurfaceType.GRAVEL;
      case "ICE" -> SurfaceType.ICE;
      case "LAT" -> SurfaceType.LATERITE;
      case "MAC" -> SurfaceType.MACADAM;
      case "MEM" -> SurfaceType.PROTECTIVE_LAMINATE;
      case "MIX" -> SurfaceType.MIX;
      case "PEM" -> SurfaceType.CONCRETE_AND_ASPHALT;
      case "PER" -> SurfaceType.PAVED;
      case "PSP" -> SurfaceType.PIERCED_STEEL_PLANKING;
      case "SAN" -> SurfaceType.SAND;
      case "SNO" -> SurfaceType.SNOW;
      default -> SurfaceType.UNKNOWN;
    };
  }
}
