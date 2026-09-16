package org.mitre.boogie.xml.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mitre.boogie.xml.v23_4.generated.*;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

/** Converts supported DAFIF terminal tables into ARINC XML procedure records for the export fixture. */
final class DafifXmlProcedures {

  private DafifXmlProcedures() {
  }

  static void populate(DafifXmlFixture.Records records, AeroPublication publication, DafifXmlReferences refs) {
    populate(records.terminalParents(), records.terminalSegments(), refs);
  }

  static void populate(List<DafifTerminalParent> parents, List<DafifTerminalSegment> segments, DafifXmlReferences refs) {
    Map<ProcedureKey, List<DafifTerminalSegment>> byProcedure = new LinkedHashMap<>();
    for (DafifTerminalSegment segment : segments) {
      ProcedureKey key = new ProcedureKey(segment.airportIdentification(), segment.terminalProcedureType(), segment.terminalIdentifier());
      byProcedure.computeIfAbsent(key, ignored -> new ArrayList<>()).add(segment);
    }
    for (DafifTerminalParent source : parents) {
      ProcedureKey key = new ProcedureKey(source.airportIdentification(), source.terminalProcedureType(), source.terminalIdentifier());
      Airport airport = refs.airport(source.airportIdentification());
      if (airport.getTerminalProcedures() == null) {
        airport.setTerminalProcedures(new TerminalProcedures());
      }
      Procedure target = newProcedure(source.terminalProcedureType());
      String name = source.terminalIdentifier();
      target.setIdentifier(name.substring(0, Math.min(6, name.length())).trim());
      target.setLongIdent(name);
      if (name.length() > 7) {
        target.setProcedureName(name.substring(7).trim());
      }
      target.setReferenceId(DafifXmlReferences.id("procedure", source.airportIdentification(),
          source.terminalProcedureType().toString(), name));
      target.setRecordType(RecordType.STANDARD);
      source.hostCountryAuthority().filter("MIL"::equals).ifPresent(ignored -> target.setIsMilitary(true));
      source.procedureDesignMagvar().map(DafifXmlProcedures::magneticVariation).ifPresent(target::setProcedureDesignMagVar);

      List<DafifTerminalSegment> procedureSegments = byProcedure.remove(key);
      if (procedureSegments != null) {
        Map<TransitionKey, List<DafifTerminalSegment>> transitions = new LinkedHashMap<>();
        for (DafifTerminalSegment segment : procedureSegments) {
          TransitionKey transition = new TransitionKey(segment.terminalApproachType(), segment.transitionIdentifier().orElse(""));
          transitions.computeIfAbsent(transition, ignored -> new ArrayList<>()).add(segment);
        }
        for (List<DafifTerminalSegment> transition : transitions.values()) {
          transition.sort(Comparator.comparingInt(DafifTerminalSegment::terminalSequenceNumber));
          addTransition(source, target, transition, refs);
        }
        firstLeg(target).ifPresent(leg -> metadata(source, leg.getNotes()));
      }
      if (target instanceof Sid sid) {
        airport.getTerminalProcedures().getSid().add(sid);
      } else if (target instanceof Star star) {
        airport.getTerminalProcedures().getStar().add(star);
      } else if (target instanceof Approach approach) {
        airport.getTerminalProcedures().getApproach().add(approach);
      }
    }
    if (!byProcedure.isEmpty()) {
      throw new IllegalArgumentException("DAFIF terminal segments have no parent: " + byProcedure.keySet().iterator().next());
    }
  }

  private static Procedure newProcedure(int type) {
    return switch (type) {
      case 1 -> new Star();
      case 2 -> new Sid();
      case 3 -> new Approach();
      default -> throw new IllegalArgumentException("Unknown DAFIF procedure type: " + type);
    };
  }

  private static void addTransition(DafifTerminalParent parent, Procedure procedure,
      List<DafifTerminalSegment> segments, DafifXmlReferences refs) {
    String type = segments.get(0).terminalApproachType();
    if (procedure instanceof Sid sid) {
      ProcedureRoute route;
      switch (type) {
        case "1", "4" -> {
          SidRunwayTransition runway = new SidRunwayTransition();
          sid.getSidRunwayTransition().add(runway);
          route = runway;
        }
        case "2", "5" -> {
          if (sid.getSidCommonRoute() != null) {
            throw new IllegalArgumentException("Multiple DAFIF SID common routes: " + parent.terminalIdentifier());
          }
          SidCommonRoute common = new SidCommonRoute();
          sid.setSidCommonRoute(common);
          route = common;
        }
        case "3", "6" -> {
          SidEnrouteTransition enroute = new SidEnrouteTransition();
          sid.getSidEnrouteTransition().add(enroute);
          route = enroute;
        }
        default -> throw new IllegalArgumentException("Unknown DAFIF SID route type: " + type);
      }
      if (List.of("4", "5", "6").contains(type)) {
        sid.setIsRnav(true);
      }
      fillRoute(parent, route, segments, refs);
    } else if (procedure instanceof Star star) {
      ProcedureRoute route;
      switch (type) {
        case "1", "4", "7" -> {
          StarEnrouteTransition enroute = new StarEnrouteTransition();
          star.getStarEnrouteTransition().add(enroute);
          route = enroute;
        }
        case "2", "5", "8" -> {
          if (star.getStarCommonRoute() != null) {
            throw new IllegalArgumentException("Multiple DAFIF STAR common routes: " + parent.terminalIdentifier());
          }
          StarCommonRoute common = new StarCommonRoute();
          star.setStarCommonRoute(common);
          route = common;
        }
        case "3", "6", "9" -> {
          StarRunwayTransition runway = new StarRunwayTransition();
          star.getStarRunwayTransition().add(runway);
          route = runway;
        }
        default -> throw new IllegalArgumentException("Unknown DAFIF STAR route type: " + type);
      }
      if (List.of("4", "5", "6").contains(type)) {
        star.setIsRnav(true);
      }
      fillRoute(parent, route, segments, refs);
    } else if (procedure instanceof Approach approach) {
      addApproachRoute(parent, approach, segments, refs);
    }
  }

  private static void addApproachRoute(DafifTerminalParent parent, Approach approach,
      List<DafifTerminalSegment> segments, DafifXmlReferences refs) {
    DafifTerminalSegment first = segments.get(0);
    int split = segments.size();
    for (int i = 0; i < segments.size(); i++) {
      if (segments.get(i).terminalWaypointDescriptionCode3().filter("M"::equals).isPresent()) {
        split = i;
        break;
      }
    }
    if (split > 0) {
      List<DafifTerminalSegment> beforeMissed = segments.subList(0, split);
      if ("A".equals(first.terminalApproachType())) {
        ApproachTransition transition = new ApproachTransition();
        fillRoute(parent, transition, beforeMissed, refs);
        approach.getApproachTransition().add(transition);
      } else {
        if (approach.getFinalApproach() != null) {
          throw new IllegalArgumentException("Multiple DAFIF final approach routes: " + parent.terminalIdentifier());
        }
        FinalApproach route = new FinalApproach();
        fillRoute(parent, route, beforeMissed, refs);
        for (DafifTerminalSegment segment : beforeMissed) {
          segment.thresholdCrossingHeight().map(Integer::longValue).ifPresent(route::setProcedureTch);
          if (segment.altitudeDescription().filter(value -> "I".equals(value) || "J".equals(value)).isPresent()
              || (segment.altitudeDescription().isEmpty() && segment.altitude1().isEmpty()
                  && segment.terminalWaypointDescriptionCode4().filter("I"::equals).isPresent())) {
            segment.altitude2().map(DafifXmlProcedures::altitude).map(Constraint::getAltitude)
                .ifPresent(route::setGlideSlopeInterceptAltitude);
          }
        }
        approach.setFinalApproach(route);
        approach.setApproachRouteType(approachType(first.terminalApproachType()));
        if (List.of("G", "R", "O", "J", "K", "1").contains(first.terminalApproachType())) {
          approach.setIsRnav(true);
        }
        if (List.of("B", "3").contains(first.terminalApproachType())) {
          approach.setIsLocalizerBackcourse(true);
        }
        parent.approachRouteQualifier1().filter("B"::equals).ifPresent(ignored -> approach.setIsRnavVisual(true));
      }
    }
    if (split < segments.size()) {
      MissedApproach missed = new MissedApproach();
      fillRoute(parent, missed, segments.subList(split, segments.size()), refs);
      if (parent.approachRouteQualifier2().filter(List.of("A", "B", "E")::contains).isEmpty()) {
        missed.setQualifier2(ApproachQualifier2.PRIMARY_MISSED_APPROACH);
      }
      approach.getMissedApproach().add(missed);
    }
  }

  private static void fillRoute(DafifTerminalParent parent, ProcedureRoute route,
      List<DafifTerminalSegment> segments, DafifXmlReferences refs) {
    DafifTerminalSegment first = segments.get(0);
    first.transitionIdentifier().ifPresent(route::setIdentifier);
    if (parent.terminalProcedureType() == 2) {
      parent.transitionAltitude().map(value -> altitude(value.toString())).ifPresent(route::setTransitionAltitudeOrLevel);
    } else {
      parent.transitionLevel().ifPresent(value -> {
        Constraint level = new Constraint();
        level.setAltitude(value);
        level.setIsFlightLevel(true);
        route.setTransitionAltitudeOrLevel(level);
      });
    }
    if (route instanceof ApproachRoute approach) {
      parent.approachRouteQualifier1().map(DafifXmlProcedures::qualifier1).ifPresent(approach::setQualifier1);
      parent.approachRouteQualifier2().map(DafifXmlProcedures::qualifier2).ifPresent(approach::setQualifier2);
      parent.approachRouteQualifier1().ifPresent(value -> {
        if (value.equals("A")) {
          approach.setApproachPbnNavSpec(ApproachPbnNavSpec.ADV_RNP);
        } else if (value.equals("F")) {
          approach.setApproachPbnNavSpec(ApproachPbnNavSpec.RNP_AR);
        }
      });
    }
    for (DafifTerminalSegment segment : segments) {
      route.getProcedureLeg().add(leg(segment, refs));
    }
  }

  private static void metadata(DafifTerminalParent parent, List<String> notes) {
    // These source-specific attributes have no direct ARINC XML procedure fields.
    parent.emergencySafeAltitude().ifPresent(value -> notes.add("DAFIF emergency safe altitude: " + value + " feet"));
    parent.procedureDesignCriteria().ifPresent(value -> notes.add("DAFIF procedure design criteria: " + value));
    parent.procedureAmendmentDate().ifPresent(value -> notes.add("DAFIF procedure amendment date: " + value));
    parent.amendmentNumber().ifPresent(value -> notes.add("DAFIF amendment number: " + value));
    parent.alternateTakeoffMinimums().ifPresent(value -> notes.add("DAFIF alternate takeoff minimums: " + value));
    parent.procedureDesignMagvar().ifPresent(value -> notes.add("DAFIF design magnetic variation including epoch: " + value));
    if (parent.terminalProcedureType() == 3) {
      sourceLevelOfService(notes, 1, parent.levelOfService1(), parent.levelOfServiceName1());
      sourceLevelOfService(notes, 2, parent.levelOfService2(), parent.levelOfServiceName2());
      sourceLevelOfService(notes, 3, parent.levelOfService3(), parent.levelOfServiceName3());
    }
  }

  private static ProcedureLeg leg(DafifTerminalSegment source, DafifXmlReferences refs) {
    ProcedureLeg target = switch (source.terminalProcedureType()) {
      case 1 -> new StarLeg();
      case 2 -> new SidLeg();
      case 3 -> new ApproachLeg();
      default -> throw new IllegalArgumentException("Unknown DAFIF procedure type: " + source.terminalProcedureType());
    };
    target.setRecordType(RecordType.STANDARD);
    target.setCycleDate(String.format("%04d", source.cycleDate() % 10000));
    target.setSequenceNumber(source.terminalSequenceNumber());
    target.setPathAndTermination(PathAndTermination.valueOf(source.trackDescriptionCode()));
    source.termSegWaypointIdentifier().ifPresent(ident -> {
      target.setFixIdent(ident);
      target.setFixRef(fix(source, refs));
    });
    source.navaid1Identifier().ifPresent(ident -> {
      target.setRecNavaidIdent(ident);
      target.setRecNavaidRef(navaid(source.airportIdentification(), ident, source.navaid1Type(),
          source.navaid1CountryCode(), source.navaid1KeyCode(), refs));
    });
    source.nav1Bearing().map(BigDecimal::valueOf).ifPresent(target::setTheta);
    source.nav1Distance().map(BigDecimal::valueOf).ifPresent(target::setRho);
    source.arcWaypointIdentifier().ifPresent(ident -> {
      target.setCenterFix(ident);
      target.setCenterFixRef(refs.waypoint(ident, source.arcWaypointCountryCode().orElse("")));
    });
    source.arcRadius().map(BigDecimal::valueOf).ifPresent(target::setArcRadius);
    source.requiredNavPerformance().map(BigDecimal::valueOf).ifPresent(target::setRnp);
    source.distance().map(BigDecimal::valueOf).ifPresent(target::setDistance);
    source.terminalMagneticCourse().map(DafifXmlProcedures::course).ifPresent(target::setCourse);
    source.terminalSegmentTurnDirection().map(value -> switch (value) {
      case "L" -> TurnDirection.LEFT;
      case "R" -> TurnDirection.RIGHT;
      default -> throw new IllegalArgumentException("Unknown DAFIF turn direction: " + value);
    }).ifPresent(target::setTurnDirection);
    altitudeConstraint(source, target);
    waypointDescription(source, target);
    speedLimit(source, target);
    if (target instanceof ApproachLeg approach) {
      source.verticalNavigationVnav().map(BigDecimal::valueOf).ifPresent(approach::setVerticalAngle);
      if (source.altitudeDescription().filter(value -> "G".equals(value) || "H".equals(value)).isPresent()) {
        source.altitude2().map(DafifXmlProcedures::altitude).map(Constraint::getAltitude).ifPresent(approach::setGlideSlopeCrossingAltitude);
      }
    } else if (target instanceof StarLeg star) {
      source.verticalNavigationVnav().map(BigDecimal::valueOf).ifPresent(star::setVerticalAngle);
    }
    source.navaid2Identifier().ifPresent(ident -> target.getNotes().add("DAFIF secondary navaid: " + ident
        + "; type=" + source.navaid2Type().orElse("") + "; country=" + source.navaid2CountryCode().orElse("")
        + "; key=" + source.navaid2KeyCode().map(Object::toString).orElse("")
        + "; bearing=" + source.nav2Bearing().map(Object::toString).orElse("")
        + "; distance=" + source.nav2Distance().map(Object::toString).orElse("")));
    return target;
  }

  private static Object fix(DafifTerminalSegment source, DafifXmlReferences refs) {
    String ident = source.termSegWaypointIdentifier().orElseThrow();
    String country = source.waypointCountryCode().orElse("");
    return switch (source.terminalWaypointDescriptionCode1().orElse("")) {
      case "A" -> refs.airportByIdentifier(ident);
      case "G" -> refs.runway(source.airportIdentification(), ident);
      case "E", "P" -> refs.waypoint(ident, country);
      case "N", "V" -> {
        Object navaid = refs.waypointNavaid(ident, country);
        if (navaid == null) {
          navaid = refs.waypoint(ident, country);
        }
        yield navaid;
      }
      default -> null;
    };
  }

  private static Object navaid(String airportId, String ident, Optional<String> type,
      Optional<String> country, Optional<Integer> key, DafifXmlReferences refs) {
    if (type.isEmpty()) {
      return null;
    }
    return switch (type.orElseThrow()) {
      case "D", "P" -> refs.ilsDme(airportId, type.orElseThrow(), ident);
      case "Z" -> refs.localizer(airportId, ident);
      case "1", "2", "3", "4", "5", "7", "9" -> key.map(value -> refs.navaid(ident, country.orElse(""),
          Integer.parseInt(type.orElseThrow()), value)).orElse(null);
      default -> null;
    };
  }

  private static void altitudeConstraint(DafifTerminalSegment source, ProcedureLeg target) {
    Optional<Constraint> first = source.altitude1().map(DafifXmlProcedures::altitude);
    Optional<Constraint> second = source.altitude2().map(DafifXmlProcedures::altitude);
    AltitudeConstraint constraint = new AltitudeConstraint();
    switch (source.altitudeDescription().orElse("")) {
      case "", "G", "I" -> first.ifPresent(constraint::setAt);
      case "+", "H", "J" -> first.ifPresent(constraint::setAtOrAbove);
      case "-" -> first.ifPresent(constraint::setAtOrBelow);
      case "B" -> {
        first.ifPresent(constraint::setAtOrBelow);
        second.ifPresent(constraint::setAtOrAbove);
      }
      case "C" -> second.ifPresent(constraint::setAtOrAbove);
      default -> throw new IllegalArgumentException("Unknown DAFIF altitude description: " + source.altitudeDescription());
    }
    if (constraint.getAt() != null || constraint.getAtOrAbove() != null || constraint.getAtOrBelow() != null) {
      target.setAltitudeConstraint(constraint);
    }
  }

  private static void speedLimit(DafifTerminalSegment source, ProcedureLeg target) {
    if (source.speedLimit1().isPresent()) {
      if (source.speedLimitAltitude1().isEmpty() && source.speedLimitAircraftType1().filter(value -> !"A".equals(value)).isEmpty()) {
        SpeedLimit speed = new SpeedLimit();
        speed.setAtOrBelow(source.speedLimit1().orElseThrow().longValue());
        target.setSpeedLimit(speed);
      } else {
        target.getNotes().add(speedNote(1, source.speedLimit1().orElseThrow(), source.speedLimitAircraftType1(), source.speedLimitAltitude1()));
      }
    }
    source.speedLimit2().ifPresent(value -> target.getNotes().add(speedNote(2, value, source.speedLimitAircraftType2(), source.speedLimitAltitude2())));
  }

  private static String speedNote(int number, Double speed, Optional<String> aircraft, Optional<String> altitude) {
    return "DAFIF speed limit " + number + ": " + speed.intValue() + " knots; aircraft=" + aircraft.orElse("all")
        + "; below altitude=" + altitude.orElse("unspecified");
  }

  private static void waypointDescription(DafifTerminalSegment source, ProcedureLeg target) {
    String first = source.terminalWaypointDescriptionCode1().orElse("");
    String second = source.terminalWaypointDescriptionCode2().orElse("");
    String third = source.terminalWaypointDescriptionCode3().orElse("");
    String fourth = source.terminalWaypointDescriptionCode4().orElse("");
    ProcedureWaypointDescription description = new ProcedureWaypointDescription();
    description.setIsEssential("E".equals(first));
    description.setIsPhantomFix("P".equals(first));
    description.setIsFlyOver("B".equals(second) || "Y".equals(second));
    description.setIsHolding("C".equals(fourth) || "H".equals(fourth));
    target.setWaypointDescriptor(description);
    if (target instanceof ApproachLeg approach) {
      ApproachWaypointDescription detail = new ApproachWaypointDescription();
      detail.setIsInitialApproachFix(List.of("A", "C", "D").contains(fourth));
      detail.setIsIntermediateApproachFix("B".equals(fourth));
      detail.setIsFacf("D".equals(fourth) || "I".equals(fourth));
      detail.setIsFaf("F".equals(fourth));
      detail.setIsMissedApproachPoint("M".equals(fourth));
      switch (third) {
        case "A" -> detail.setStepDownFix(StepDownFix.UNNAMED_IN_FINAL_APPROACH_SEGMENT);
        case "B" -> detail.setStepDownFix(StepDownFix.UNNAMED_IN_INTERMEDIATE_APPROACH_SEGMENT);
        case "S" -> detail.setStepDownFix(StepDownFix.NAMED);
        default -> { }
      }
      approach.setApproachWaypointDescription(detail);
    }
  }

  private static Constraint altitude(String source) {
    Constraint target = new Constraint();
    if (source.startsWith("FL")) {
      target.setAltitude(100 * Integer.parseInt(source.substring(2)));
      target.setIsFlightLevel(true);
    } else {
      target.setAltitude(Integer.parseInt(source));
      target.setIsFlightLevel(false);
    }
    return target;
  }

  private static Course course(String source) {
    Course target = new Course();
    if (source.endsWith("T")) {
      target.setCourseValue(new BigDecimal(source.substring(0, source.length() - 1)));
      target.setIsTrue(true);
    } else {
      target.setCourseValue(new BigDecimal(source));
      target.setIsTrue(false);
    }
    return target;
  }

  private static MagneticVariation magneticVariation(String source) {
    MagneticVariation target = new MagneticVariation();
    target.setMagneticVariationEWT(switch (source.charAt(0)) {
      case 'E' -> MagneticVariationEWT.EAST;
      case 'W' -> MagneticVariationEWT.WEST;
      default -> throw new IllegalArgumentException("Unknown DAFIF magnetic variation: " + source);
    });
    target.setMagneticVariationValue(new BigDecimal(source.substring(1, 5)).movePointLeft(1));
    return target;
  }

  private static ApproachRouteType approachType(String type) {
    return switch (type) {
      case "B", "3" -> ApproachRouteType.LOC_BACKCOURSE;
      case "C", "H", "I" -> ApproachRouteType.ILS;
      case "D" -> ApproachRouteType.VOR_DME;
      case "E", "V" -> ApproachRouteType.VOR;
      case "F", "N", "W" -> ApproachRouteType.NDB;
      case "G", "O" -> ApproachRouteType.RNAV;
      case "J" -> ApproachRouteType.GLS;
      case "K", "1" -> ApproachRouteType.GPS;
      case "L", "2" -> ApproachRouteType.LOC_ONLY;
      case "M", "U", "Z" -> ApproachRouteType.MLS;
      case "Q" -> ApproachRouteType.NDB_DME;
      case "R" -> ApproachRouteType.RNP;
      case "S" -> ApproachRouteType.VOR_USING_VORDME_OR_VORTAC;
      case "T" -> ApproachRouteType.TACAN;
      case "X" -> ApproachRouteType.LDA;
      case "Y" -> ApproachRouteType.SDF;
      default -> throw new IllegalArgumentException("Unsupported DAFIF approach type: " + type);
    };
  }

  private static ApproachQualifier1 qualifier1(String value) {
    return switch (value) {
      case "D" -> ApproachQualifier1.DME_REQUIRED;
      case "J" -> ApproachQualifier1.GPS_REQUIRED_DME_DME_NOT_AUTHORIZED;
      case "N" -> ApproachQualifier1.DME_NOT_REQUIRED;
      case "P" -> ApproachQualifier1.GNSS_REQUIRED;
      case "R" -> ApproachQualifier1.GNSS_OR_DME_DME_REQUIRED;
      case "T" -> ApproachQualifier1.DME_DME_REQUIRED;
      case "U" -> ApproachQualifier1.RNAV_SENSOR_NOT_SPECIFIED;
      case "V" -> ApproachQualifier1.VOR_DME_RNAV;
      case "W" -> ApproachQualifier1.RNAV_REQUIRES_FAS_DATA_BLOCK;
      default -> null;
    };
  }

  private static ApproachQualifier2 qualifier2(String value) {
    return switch (value) {
      case "A" -> ApproachQualifier2.PRIMARY_MISSED_APPROACH;
      case "B" -> ApproachQualifier2.SECONDARY_MISSED_APPROACH;
      case "E" -> ApproachQualifier2.ENGINE_OUT_MISSED_APPROACH;
      case "C" -> ApproachQualifier2.CIRCLE_TO_LAND_MINIMUMS;
      case "H" -> ApproachQualifier2.HELICOPTER_STRAIGHT_IN_MINIMUMS;
      case "I" -> ApproachQualifier2.HELICOPTER_CIRCLE_TO_LAND_MINIMUMS;
      case "L" -> ApproachQualifier2.HELICOPTER_LANDING_MINIMUMS;
      case "S" -> ApproachQualifier2.STRAIGHT_IN_MINIMUMS;
      case "V" -> ApproachQualifier2.VMC_MINIMUMS;
      default -> null;
    };
  }

  private static void sourceLevelOfService(List<String> notes, int number, String authorized, Optional<String> name) {
    if (name.isPresent() || "A".equals(authorized)) {
      notes.add("DAFIF level of service " + number + ": " + name.orElse("unnamed") + "; authorized=" + authorized);
    }
  }

  private static Optional<ProcedureLeg> firstLeg(Procedure procedure) {
    List<ProcedureRoute> routes = new ArrayList<>();
    if (procedure instanceof Sid sid) {
      routes.addAll(sid.getSidRunwayTransition());
      if (sid.getSidCommonRoute() != null) {
        routes.add(sid.getSidCommonRoute());
      }
      routes.addAll(sid.getSidEnrouteTransition());
    } else if (procedure instanceof Star star) {
      routes.addAll(star.getStarEnrouteTransition());
      if (star.getStarCommonRoute() != null) {
        routes.add(star.getStarCommonRoute());
      }
      routes.addAll(star.getStarRunwayTransition());
    } else if (procedure instanceof Approach approach) {
      routes.addAll(approach.getApproachTransition());
      if (approach.getFinalApproach() != null) {
        routes.add(approach.getFinalApproach());
      }
      routes.addAll(approach.getMissedApproach());
    }
    return routes.stream().flatMap(route -> route.getProcedureLeg().stream()).findFirst();
  }

  private record ProcedureKey(String airport, int type, String identifier) {
  }

  private record TransitionKey(String type, String identifier) {
  }
}
