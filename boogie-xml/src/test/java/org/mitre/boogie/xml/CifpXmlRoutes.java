package org.mitre.boogie.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.mitre.boogie.xml.v23_4.generated.*;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;

/** Maps the route records in the CIFP fixture to the ARINC 424-23.4 JAXB model. */
final class CifpXmlRoutes {
  private static final DatatypeFactory DATATYPES = datatypeFactory();

  private CifpXmlRoutes() {
  }

  static void populate(ConvertedArincRecords records, AeroPublication publication, CifpXmlReferences refs) {
    Airways airways = new Airways();
    for (List<ArincAirwayLeg> legs : group(records.arincAirwayLegs(), leg -> List.of(
        leg.customerAreaCode().name(), leg.routeIdentifier(), leg.sixthCharacter().orElse(""))).values()) {
      int segment = 0;
      Airway airway = null;
      for (ArincAirwayLeg source : legs) {
        if (airway == null) {
          airway = new Airway();
          airway.setIdentifier(source.routeIdentifier() + source.sixthCharacter().orElse(""));
          airway.setReferenceId(CifpXmlReferences.id("airway", source.customerAreaCode().name(), airway.getIdentifier(),
              Integer.toString(segment++)));
          airway.setRecordType(recordType(source.recordType().name()));
          source.routeType().map(CifpXmlRoutes::airwayRouteType).ifPresent(airway::setAirwayRouteType);
          airways.getAirway().add(airway);
        }
        airway.getAirwayLeg().add(airwayLeg(source, refs));
        // E marks the end of a continuous segment; the next fix must start a separate airway.
        if (source.waypointDescription().filter(value -> value.charAt(1) == 'E').isPresent()) {
          airway = null;
        }
      }
    }
    publication.setAirways(airways);

    for (List<ArincProcedureLeg> legs : group(records.arincProcedureLegs(), leg -> new ProcedureKey(
        leg.sectionCode().name(), leg.airportIdentifier(), leg.airportIcaoRegion(), leg.sidStarIdentifier(),
        leg.subSectionCode().orElseThrow())).values()) {
      ArincProcedureLeg first = legs.get(0);
      Port port = refs.port(first.sectionCode().name(), first.airportIdentifier(), first.airportIcaoRegion());
      if (port == null) {
        throw new IllegalArgumentException("Missing procedure port: " + first.airportIdentifier() + "/" + first.airportIcaoRegion());
      }
      if (port.getTerminalProcedures() == null) {
        port.setTerminalProcedures(new TerminalProcedures());
      }
      Procedure procedure = procedure(first);
      procedure.setIdentifier(first.sidStarIdentifier());
      procedure.setReferenceId(CifpXmlReferences.id("procedure", first.sectionCode().name(), first.airportIdentifier(), first.airportIcaoRegion(),
          first.subSectionCode().orElseThrow(), first.sidStarIdentifier()));
      procedure.setRecordType(recordType(first.recordType().name()));
      for (List<ArincProcedureLeg> transition : group(legs, CifpXmlRoutes::transitionKey).values()) {
        transition.sort(Comparator.comparingInt(ArincProcedureLeg::sequenceNumber)
            .thenComparing(leg -> leg.categoryOrType().orElse("")));
        addTransition(procedure, transition, refs);
      }
      if (procedure instanceof Sid sid) {
        port.getTerminalProcedures().getSid().add(sid);
      } else if (procedure instanceof Star star) {
        port.getTerminalProcedures().getStar().add(star);
      } else if (procedure instanceof Approach approach) {
        port.getTerminalProcedures().getApproach().add(approach);
      }
    }

    if (!records.arincHoldingPatterns().isEmpty()) {
      HoldingPatterns holdingPatterns = new HoldingPatterns();
      records.arincHoldingPatterns().stream().map(hold -> holdingPattern(hold, refs))
          .forEach(holdingPatterns.getHoldingPattern()::add);
      publication.setHoldingPatterns(holdingPatterns);
    }
  }

  private static AirwayLeg airwayLeg(ArincAirwayLeg source, CifpXmlReferences refs) {
    AirwayLeg target = new AirwayLeg();
    record(target, source.recordType().name(), source.customerAreaCode().name(), source.lastUpdateCycle());
    target.setSequenceNumber(source.sequenceNumber());
    target.setFixIdent(source.fixIdentifier());
    target.setFixRef(refs.fix(source.fixSectionCode().name(), source.fixSubSectionCode().orElse(""),
        source.fixIdentifier(), source.fixIcaoRegion(), null));
    source.recommendedNavaidIdentifier().ifPresent(ident -> {
      target.setRecNavaidIdent(ident);
      target.setRecNavaidRef(refs.fix("D", "", ident, source.recommendedNavaidIcaoRegion().orElse(""), null));
    });
    source.routeType().map(CifpXmlRoutes::airwayRouteType).ifPresent(target::setAirwayRouteType);
    source.directionRestriction().map(CifpXmlRoutes::directionRestriction).ifPresent(target::setLegDirectionRestriction);
    source.level().map(Enum::name).map(CifpXmlRoutes::level).ifPresent(target::setLevel);
    source.euIndicator().map(value -> value ? EuIndicator.RESTRICTION : EuIndicator.NO_RESTRICTION).ifPresent(target::setEuIndicator);
    source.routeDistance().map(BigDecimal::valueOf).ifPresent(target::setRouteDistanceFrom);
    course(source.inboundMagneticCourse(), refs.airwayField(source, "inboundMagneticCourse")).ifPresent(target::setInboundCourse);
    course(source.outboundMagneticCourse(), refs.airwayField(source, "outboundMagneticCourse")).ifPresent(target::setOutboundCourse);
    source.theta().map(BigDecimal::valueOf).ifPresent(target::setTheta);
    source.rho().map(BigDecimal::valueOf).ifPresent(target::setRho);
    source.rnp().map(BigDecimal::valueOf).ifPresent(target::setRnp);
    source.fixedRadiusTransitionIndicator().map(BigDecimal::valueOf).ifPresent(target::setFixRadiusTransitionIndicator);
    source.verticalScaleFactor().map(Integer::longValue).ifPresent(target::setVerticalScaleFactor);
    source.waypointDescription().map(CifpXmlRoutes::airwayDescription).ifPresent(target::setWaypointDescription);
    minimumAltitude(source.minAltitude1(), refs.airwayField(source, "minAltitude1")).ifPresent(target.getMinimumAltitudes()::add);
    minimumAltitude(source.minAltitude2(), refs.airwayField(source, "minAltitude2")).ifPresent(target.getMinimumAltitudes()::add);
    maximumAltitude(source.maxAltitude(), refs.airwayField(source, "maxAltitude")).ifPresent(target.getMaximumAltitudes()::add);
    if (source.rvsmMinLevel().isPresent() || source.rvsmMaxLevel().isPresent()) {
      target.setRvsmMinMaxLevels(rvsmLevels(source.rvsmMinLevel(), source.rvsmMaxLevel()));
    }
    return target;
  }

  private static Procedure procedure(ArincProcedureLeg first) {
    return switch (first.subSectionCode().orElseThrow()) {
      case "D" -> new Sid();
      case "E" -> new Star();
      case "F" -> new Approach();
      default -> throw new IllegalArgumentException("Unsupported procedure section: " + first.subSectionCode());
    };
  }

  private static TransitionKey transitionKey(ArincProcedureLeg source) {
    String missedVariant = "";
    if ("Z".equals(source.routeType())) {
      missedVariant = source.routeTypeQualifier2().orElse("");
    }
    return new TransitionKey(source.transitionIdentifier().orElse(""), source.categoryOrType().orElse(""),
        source.routeType(), missedVariant);
  }

  private static void addTransition(Procedure procedure, List<ArincProcedureLeg> legs, CifpXmlReferences refs) {
    ArincProcedureLeg first = legs.get(0);
    String routeType = first.routeType();
    if (procedure instanceof Sid sid) {
      ProcedureRoute route;
      if (List.of("2", "5", "M", "0", "N").contains(routeType)) {
        if (sid.getSidCommonRoute() != null) {
          throw new IllegalArgumentException("Multiple SID common routes: " + sid.getReferenceId());
        }
        SidCommonRoute common = new SidCommonRoute();
        sid.setSidCommonRoute(common);
        route = common;
      } else if (List.of("3", "6", "S", "V", "P").contains(routeType)) {
        SidEnrouteTransition enroute = new SidEnrouteTransition();
        sid.getSidEnrouteTransition().add(enroute);
        route = enroute;
      } else if (List.of("1", "4", "F", "T", "R").contains(routeType)) {
        SidRunwayTransition runway = new SidRunwayTransition();
        sid.getSidRunwayTransition().add(runway);
        route = runway;
      } else {
        throw new IllegalArgumentException("Unknown SID route type: " + routeType);
      }
      sidStarQualifications(route, routeType);
      if (List.of("4", "5", "6", "R", "N", "P", "M", "S", "F").contains(routeType)) {
        sid.setIsRnav(true);
      }
      if (List.of("T", "V").contains(routeType)) {
        sid.setIsVector(true);
      }
      if ("0".equals(routeType)) {
        sid.setIsEngineOut(true);
      }
      fillTransition(route, legs, refs);
    } else if (procedure instanceof Star star) {
      ProcedureRoute route;
      if (List.of("2", "5", "8", "M", "N").contains(routeType)) {
        if (star.getStarCommonRoute() != null) {
          throw new IllegalArgumentException("Multiple STAR common routes: " + star.getReferenceId());
        }
        StarCommonRoute common = new StarCommonRoute();
        star.setStarCommonRoute(common);
        route = common;
      } else if (List.of("1", "4", "7", "F", "R").contains(routeType)) {
        StarEnrouteTransition enroute = new StarEnrouteTransition();
        star.getStarEnrouteTransition().add(enroute);
        route = enroute;
      } else if (List.of("3", "6", "9", "S", "P").contains(routeType)) {
        StarRunwayTransition runway = new StarRunwayTransition();
        star.getStarRunwayTransition().add(runway);
        route = runway;
      } else {
        throw new IllegalArgumentException("Unknown STAR route type: " + routeType);
      }
      sidStarQualifications(route, routeType);
      if (List.of("4", "5", "6", "F", "M", "S", "R", "N", "P").contains(routeType)) {
        star.setIsRnav(true);
      }
      fillTransition(route, legs, refs);
    } else if (procedure instanceof Approach approach) {
      addApproachTransition(approach, legs, refs);
    }
  }

  private static void addApproachTransition(Approach approach, List<ArincProcedureLeg> legs, CifpXmlReferences refs) {
    ArincProcedureLeg first = legs.get(0);
    if ("Z".equals(first.routeType())) {
      MissedApproach missed = new MissedApproach();
      fillTransition(missed, legs, refs);
      approach.getMissedApproach().add(missed);
      return;
    }
    if ("A".equals(first.routeType())) {
      ApproachTransition transition = new ApproachTransition();
      fillTransition(transition, legs, refs);
      approach.getApproachTransition().add(transition);
      return;
    }
    ApproachRouteType type = approachRouteType(first.routeType());
    if (type == null) {
      throw new IllegalArgumentException("Unknown approach route type: " + first.routeType());
    }
    approach.setApproachRouteType(type);
    if (List.of("H", "R", "P", "F").contains(first.routeType())) {
      approach.setIsRnav(true);
    }
    int missedIndex = legs.size();
    for (int i = 0; i < legs.size(); i++) {
      if (legs.get(i).waypointDescription().filter(value -> value.charAt(2) == 'M').isPresent()) {
        missedIndex = i;
        break;
      }
    }
    if (missedIndex > 0) {
      if (approach.getFinalApproach() != null) {
        throw new IllegalArgumentException("Multiple final approaches: " + approach.getReferenceId());
      }
      FinalApproach finalApproach = new FinalApproach();
      List<ArincProcedureLeg> finalLegs = legs.subList(0, missedIndex);
      fillTransition(finalApproach, finalLegs, refs);
      for (ArincProcedureLeg source : finalLegs) {
        if (source.altitudeDescription().filter(value -> "IJ".contains(value)).isPresent()) {
          source.minAltitude2().map(Double::intValue).ifPresent(finalApproach::setGlideSlopeInterceptAltitude);
        }
      }
      approach.setFinalApproach(finalApproach);
    }
    if (missedIndex < legs.size()) {
      MissedApproach missed = new MissedApproach();
      fillTransition(missed, legs.subList(missedIndex, legs.size()), refs);
      missed.setQualifier2(ApproachQualifier2.PRIMARY_MISSED_APPROACH);
      approach.getMissedApproach().add(missed);
    }
  }

  private static void fillTransition(ProcedureRoute target, List<ArincProcedureLeg> legs, CifpXmlReferences refs) {
    ArincProcedureLeg first = legs.get(0);
    first.transitionIdentifier().ifPresent(target::setIdentifier);
    constraint(first.transitionAltitude(), refs.procedureField(first, "transitionAltitude")).ifPresent(target::setTransitionAltitudeOrLevel);
    if (target instanceof ApproachRoute approach) {
      first.routeTypeQualifier1().map(CifpXmlRoutes::approachQualifier1).ifPresent(approach::setQualifier1);
      first.routeTypeQualifier2().map(CifpXmlRoutes::approachQualifier2).ifPresent(approach::setQualifier2);
    }
    for (ArincProcedureLeg source : legs) {
      target.getProcedureLeg().add(procedureLeg(source, refs));
    }
  }

  private static ProcedureLeg procedureLeg(ArincProcedureLeg source, CifpXmlReferences refs) {
    ProcedureLeg target = switch (source.subSectionCode().orElseThrow()) {
      case "D" -> new SidLeg();
      case "E" -> new StarLeg();
      case "F" -> new ApproachLeg();
      default -> throw new IllegalArgumentException("Unknown procedure leg section");
    };
    record(target, source.recordType().name(), source.customerAreaCode().map(Enum::name).orElse(null), source.lastUpdateCycle());
    target.setSequenceNumber(source.sequenceNumber());
    target.setPathAndTermination(PathAndTermination.valueOf(source.pathTerm().name()));
    source.fixIdentifier().ifPresent(ident -> {
      target.setFixIdent(ident);
      target.setFixRef(refs.fix(source.fixSectionCode().map(Enum::name).orElse(""), source.fixSubSectionCode().orElse(""),
          ident, source.fixIcaoRegion().orElse(""), source.airportIdentifier()));
    });
    source.recommendedNavaidIdentifier().ifPresent(ident -> {
      target.setRecNavaidIdent(ident);
      target.setRecNavaidRef(refs.fix(source.recommendedNavaidSectionCode().map(Enum::name).orElse("D"),
          source.recommendedNavaidSubSectionCode().orElse(""), ident,
          source.recommendedNavaidIcaoRegion().orElse(""), source.airportIdentifier()));
    });
    source.centerFixIdentifier().ifPresent(ident -> {
      target.setCenterFix(ident);
      target.setCenterFixRef(refs.fix(source.centerFixSectionCode().map(Enum::name).orElse(""),
          source.centerFixSubSectionCode().orElse(""), ident, source.centerFixIcaoRegion().orElse(""), source.airportIdentifier()));
    });
    source.arcRadius().map(CifpXmlRoutes::procedureDecimal).ifPresent(target::setArcRadius);
    source.theta().map(CifpXmlRoutes::procedureDecimal).ifPresent(target::setTheta);
    source.rho().map(CifpXmlRoutes::procedureDecimal).ifPresent(target::setRho);
    source.rnp().map(CifpXmlRoutes::procedureDecimal).ifPresent(target::setRnp);
    source.routeDistance().map(CifpXmlRoutes::procedureDecimal).ifPresent(target::setDistance);
    source.holdTime().map(duration -> DATATYPES.newDuration(duration.toMillis())).ifPresent(target::setHoldTime);
    course(source.outboundMagneticCourse(), refs.procedureField(source, "outboundMagneticCourse")).ifPresent(target::setCourse);
    source.transitionAltitude().map(Double::intValue).ifPresent(target::setTransitionsAltitudeLevel);
    source.turnDirection().map(Enum::name).map(CifpXmlRoutes::turnDirection).ifPresent(target::setTurnDirection);
    source.turnDirectionValid().ifPresent(target::setIsTurnDirectionValid);
    source.legInboundOutboundIdentifier().map(CifpXmlRoutes::inboundOutbound).ifPresent(target::setLegInboundOutboundIndicator);
    source.speedLimit().ifPresent(value -> target.setSpeedLimit(speedLimit(value, source.speedLimitDescription().orElse("@"))));
    altitudeConstraint(source, target, refs);
    source.waypointDescription().ifPresent(value -> waypointDescription(value, target));
    if (target instanceof ApproachLeg approach) {
      source.verticalAngle().map(CifpXmlRoutes::procedureDecimal).ifPresent(approach::setVerticalAngle);
      if (source.altitudeDescription().filter(value -> "GH".contains(value)).isPresent()) {
        source.minAltitude2().map(Double::intValue).ifPresent(approach::setGlideSlopeCrossingAltitude);
      }
    } else if (target instanceof StarLeg star) {
      source.verticalAngle().map(CifpXmlRoutes::procedureDecimal).ifPresent(star::setVerticalAngle);
    }
    return target;
  }

  private static void altitudeConstraint(ArincProcedureLeg source, ProcedureLeg target, CifpXmlReferences refs) {
    if (source.minAltitude1().isEmpty() && source.minAltitude2().isEmpty()) {
      return;
    }
    AltitudeConstraint altitude = new AltitudeConstraint();
    Optional<Constraint> first = constraint(source.minAltitude1(), refs.procedureField(source, "minAltitude1"));
    Optional<Constraint> second = constraint(source.minAltitude2(), refs.procedureField(source, "minAltitude2"));
    switch (source.altitudeDescription().orElse("@")) {
      case "@", " ", "G", "I", "X" -> first.ifPresent(altitude::setAt);
      case "+", "H", "J", "V" -> first.ifPresent(altitude::setAtOrAbove);
      case "-", "Y" -> first.ifPresent(altitude::setAtOrBelow);
      case "B" -> {
        first.ifPresent(altitude::setAtOrBelow);
        second.ifPresent(altitude::setAtOrAbove);
      }
      case "C" -> second.ifPresent(altitude::setAtOrAbove);
      default -> throw new IllegalArgumentException("Unknown altitude description: " + source.altitudeDescription());
    }
    if (altitude.getAt() != null || altitude.getAtOrAbove() != null || altitude.getAtOrBelow() != null) {
      target.setAltitudeConstraint(altitude);
    }
    if (source.altitudeDescription().filter(value -> "VXY".contains(value)).isPresent() && source.minAltitude2().isPresent()) {
      String raw = refs.procedureField(source, "minAltitude2");
      if (raw == null || raw.isBlank()) {
        raw = source.minAltitude2().orElseThrow().intValue() + " feet";
      }
      target.getNotes().add("CIFP altitude2 (vertical angle reference): " + raw.trim());
    }
  }

  private static void waypointDescription(String value, ProcedureLeg target) {
    ProcedureWaypointDescription description = new ProcedureWaypointDescription();
    description.setIsEssential(value.charAt(0) == 'E');
    description.setIsPhantomFix(value.charAt(0) == 'P');
    description.setIsFlyOver(value.charAt(1) == 'B' || value.charAt(1) == 'Y');
    description.setIsAtcCompulsoryReportingPoint(value.charAt(2) == 'C');
    description.setIsHolding(value.charAt(3) == 'C' || value.charAt(3) == 'H');
    description.setIsSourceProvidedEnrouteWaypoint(value.charAt(3) == 'G' || value.charAt(3) == 'H');
    target.setWaypointDescriptor(description);
    if (target instanceof ApproachLeg approach) {
      ApproachWaypointDescription detail = new ApproachWaypointDescription();
      detail.setIsFixTurningFinalApproach(value.charAt(2) == 'R');
      detail.setIsInitialApproachFix("ACD".indexOf(value.charAt(3)) >= 0);
      detail.setIsIntermediateApproachFix(value.charAt(3) == 'B');
      detail.setIsFacf(value.charAt(3) == 'D' || value.charAt(3) == 'I');
      detail.setIsFaf(value.charAt(3) == 'F');
      detail.setIsFinalEndPoint(value.charAt(3) == 'E');
      detail.setIsMissedApproachPoint(value.charAt(3) == 'M');
      detail.setIsEngineOutDisarmPoint(value.charAt(3) == 'N');
      switch (value.charAt(2)) {
        case 'A' -> detail.setStepDownFix(StepDownFix.UNNAMED_IN_FINAL_APPROACH_SEGMENT);
        case 'B' -> detail.setStepDownFix(StepDownFix.UNNAMED_IN_INTERMEDIATE_APPROACH_SEGMENT);
        case 'S' -> detail.setStepDownFix(StepDownFix.NAMED);
        default -> { }
      }
      approach.setApproachWaypointDescription(detail);
    } else if (target instanceof SidLeg sid) {
      sid.setIsEngineOutDisarmPoint(value.charAt(3) == 'N');
    }
  }

  private static AirwayWaypointDescription airwayDescription(String value) {
    AirwayWaypointDescription target = new AirwayWaypointDescription();
    target.setIsEssential(value.charAt(0) == 'E');
    target.setIsOffAirwayFloating(value.charAt(0) == 'F');
    target.setIsNonEssential(value.charAt(0) == 'R');
    target.setIsTransitionEssential(value.charAt(0) == 'T');
    target.setIsUnchartedIntersection(value.charAt(1) == 'U');
    target.setIsAtcCompulsoryReportingPoint(value.charAt(2) == 'C');
    target.setIsOceanicGateway(value.charAt(2) == 'G');
    target.setIsHolding(value.charAt(3) == 'H');
    target.setIsSourceProvidedEnrouteWaypoint(value.charAt(3) == 'G' || value.charAt(3) == 'H');
    return target;
  }

  private static HoldingPattern holdingPattern(ArincHoldingPattern source, CifpXmlReferences refs) {
    HoldingPattern target = new HoldingPattern();
    record(target, source.recordType().name(), source.customerAreaCode().name(), source.lastUpdatedCycle().orElse(null));
    target.setFixIdentifier(source.fixIdentifier());
    target.setFixRef(refs.fix(source.fixSectionCode().name(), source.fixSubsectionCode(), source.fixIdentifier(),
        source.fixIcaoRegion(), source.regionCode().orElse(null)));
    source.regionCode().ifPresent(ident -> target.setPortRef(refs.port(ident, source.icaoRegion().orElse(""))));
    course(Optional.of(source.inboundHoldingCourse()), refs.holdingField(source, "inboundHoldingCourse"))
        .ifPresent(target::setInboundHoldingCourse);
    switch (source.turnDirection().name()) {
      case "L" -> target.setTurnDirection(Turn.LEFT);
      case "R" -> target.setTurnDirection(Turn.RIGHT);
      default -> { }
    }
    source.holdingName().ifPresent(target::setHoldingPatternName);
    source.holdingSpeed().map(Integer::longValue).ifPresent(target::setHoldingSpeed);
    source.legLength().map(BigDecimal::valueOf).ifPresent(target::setHoldingDistance);
    source.legTime().map(duration -> DATATYPES.newDuration(duration.toMillis())).ifPresent(target::setHoldingTime);
    source.rnp().map(BigDecimal::valueOf).ifPresent(target::setRnp);
    source.arcRadius().map(BigDecimal::valueOf).ifPresent(target::setArcRadius);
    source.verticalScaleFactor().map(Integer::longValue).ifPresent(target::setVerticalScaleFactor);
    source.inboundOutboundIndicator().map(CifpXmlRoutes::inboundOutbound).ifPresent(target::setLegInboundOutboundIndicator);
    if (source.minimumAltitude().isPresent() || source.maxAltitude().isPresent()) {
      HoldRvsmMinimumMaximumAltitudeConstraint limits = new HoldRvsmMinimumMaximumAltitudeConstraint();
      minimumAltitude(source.minimumAltitude(), refs.holdingField(source, "minimumAltitude")).ifPresent(limits::setMinimumAltitude);
      maximumAltitude(source.maxAltitude(), refs.holdingField(source, "maxAltitude")).ifPresent(limits::setMaximumAltitude);
      target.setHoldMinMaxAltitudes(limits);
    }
    if (source.rvsmMin().isPresent() || source.rvsmMax().isPresent()) {
      target.setRvsmMinMaxLevels(rvsmLevels(source.rvsmMin(), source.rvsmMax()));
    }
    return target;
  }

  private static void sidStarQualifications(ProcedureRoute target, String routeType) {
    RouteQualifications qualification = new RouteQualifications();
    if (List.of("1", "2", "3").contains(routeType)) {
      qualification.setIsConventional(true);
    }
    if (List.of("F", "M", "S").contains(routeType)) {
      qualification.setIsFmsReq(true);
    }
    if (target instanceof CommonRoute common) {
      common.setRouteQualifications(qualification);
    } else if (target instanceof EnrouteTransition enroute) {
      enroute.setRouteQualifications(qualification);
    } else if (target instanceof RunwayTransition runway) {
      runway.setRouteQualifications(qualification);
    }
  }

  private static void record(A424Record target, String type, String area, String cycle) {
    target.setRecordType(recordType(type));
    if (area != null) {
      target.setAreaCode(AreaCode.valueOf(area));
    }
    target.setCycleDate(cycle);
  }

  private static RecordType recordType(String value) {
    return switch (value) {
      case "S" -> RecordType.STANDARD;
      case "T" -> RecordType.TAILORED;
      default -> throw new IllegalArgumentException("Unknown record type: " + value);
    };
  }

  private static EnrouteAirwayRouteType airwayRouteType(String value) {
    return switch (value) {
      case "A" -> EnrouteAirwayRouteType.AIRLINE;
      case "C" -> EnrouteAirwayRouteType.CONTROL;
      case "D" -> EnrouteAirwayRouteType.DIRECT;
      case "H" -> EnrouteAirwayRouteType.HELICOPTER;
      case "O" -> EnrouteAirwayRouteType.OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER;
      case "R" -> EnrouteAirwayRouteType.RNAV_RNP;
      case "S" -> EnrouteAirwayRouteType.UNDESIGNATED;
      case "T" -> EnrouteAirwayRouteType.TACAN;
      default -> null;
    };
  }

  private static ApproachRouteType approachRouteType(String value) {
    return switch (value) {
      case "B" -> ApproachRouteType.LOC_BACKCOURSE;
      case "D" -> ApproachRouteType.VOR_DME;
      case "F" -> ApproachRouteType.FMS;
      case "G" -> ApproachRouteType.IGS;
      case "H" -> ApproachRouteType.RNP;
      case "I" -> ApproachRouteType.ILS;
      case "J" -> ApproachRouteType.GLS;
      case "L" -> ApproachRouteType.LOC_ONLY;
      case "M" -> ApproachRouteType.MLS;
      case "N" -> ApproachRouteType.NDB;
      case "P" -> ApproachRouteType.GPS;
      case "Q" -> ApproachRouteType.NDB_DME;
      case "R" -> ApproachRouteType.RNAV;
      case "S" -> ApproachRouteType.VOR_USING_VORDME_OR_VORTAC;
      case "T" -> ApproachRouteType.TACAN;
      case "U" -> ApproachRouteType.SDF;
      case "V" -> ApproachRouteType.VOR;
      case "X" -> ApproachRouteType.LDA;
      default -> null;
    };
  }

  private static ApproachQualifier1 approachQualifier1(String value) {
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

  private static ApproachQualifier2 approachQualifier2(String value) {
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

  private static EnrouteAirwayDirectionalRestriction directionRestriction(String value) {
    return switch (value) {
      case "F" -> EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD;
      case "B" -> EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD;
      default -> null;
    };
  }

  private static Level level(String value) {
    return switch (value) {
      case "B" -> Level.ALL_ALT;
      case "H" -> Level.HIGH_ALT;
      case "L" -> Level.LOW_ALT;
      default -> null;
    };
  }

  private static TurnDirection turnDirection(String value) {
    return switch (value) {
      case "L" -> TurnDirection.LEFT;
      case "R" -> TurnDirection.RIGHT;
      case "E" -> TurnDirection.EITHER;
      default -> null;
    };
  }

  private static LegInboundOutboundIndicator inboundOutbound(String value) {
    return switch (value) {
      case "I" -> LegInboundOutboundIndicator.INBOUND;
      case "O" -> LegInboundOutboundIndicator.OUTBOUND;
      default -> null;
    };
  }

  private static SpeedLimit speedLimit(Integer value, String description) {
    SpeedLimit target = new SpeedLimit();
    switch (description) {
      case "+" -> target.setAtOrAbove(value.longValue());
      case "-" -> target.setAtOrBelow(value.longValue());
      default -> target.setAt(value.longValue());
    }
    return target;
  }

  private static Optional<Course> course(Optional<Double> value, String raw) {
    if (raw != null && !raw.isBlank()) {
      String token = raw.trim();
      Course target = new Course();
      if (token.endsWith("T")) {
        target.setCourseValue(new BigDecimal(token.substring(0, token.length() - 1)));
        target.setIsTrue(true);
        return Optional.of(target);
      }
      if (token.matches("[0-9]{4}")) {
        target.setCourseValue(new BigDecimal(token).movePointLeft(1));
        target.setIsTrue(false);
        return Optional.of(target);
      }
    }
    return value.map(CifpXmlRoutes::magneticCourse);
  }

  private static BigDecimal procedureDecimal(Double value) {
    // The typed procedure model stores these fields as floats; avoid serializing their double widening artifacts.
    return new BigDecimal(Float.toString(value.floatValue()));
  }

  private static Course magneticCourse(Double value) {
    Course target = new Course();
    target.setCourseValue(BigDecimal.valueOf(value));
    target.setIsTrue(false);
    return target;
  }

  private static Optional<Constraint> constraint(Optional<Double> value, String raw) {
    return value.map(altitude -> {
      Constraint target = new Constraint();
      target.setAltitude(altitude.intValue());
      target.setIsFlightLevel(false);
      if (raw != null && raw.trim().startsWith("FL")) {
        // ARINC XML keeps the magnitude in feet even when the flight-level reference flag is true.
        target.setAltitude(100 * Integer.parseInt(raw.trim().substring(2)));
        target.setIsFlightLevel(true);
      }
      return target;
    });
  }

  private static Optional<RouteMinimumAltitude> minimumAltitude(Optional<Double> value, String raw) {
    RouteMinimumAltitude target = new RouteMinimumAltitude();
    if (routeAltitude(target, value, raw)) {
      return Optional.of(target);
    }
    return Optional.empty();
  }

  private static Optional<RouteMaximumAltitude> maximumAltitude(Optional<Double> value, String raw) {
    RouteMaximumAltitude target = new RouteMaximumAltitude();
    if (raw != null && raw.trim().equals("UNLTD")) {
      target.setIsUnlimited(true);
      return Optional.of(target);
    }
    if (routeAltitude(target, value, raw)) {
      return Optional.of(target);
    }
    return Optional.empty();
  }

  private static boolean routeAltitude(AirspaceRouteHoldAltitude target, Optional<Double> value, String raw) {
    if (raw != null) {
      String token = raw.trim();
      if (token.startsWith("FL")) {
        target.setAltitude(100 * Integer.parseInt(token.substring(2)));
        target.setIsFlightLevel(true);
        return true;
      }
      if (token.equals("UNKNN")) {
        target.setIsUnknown(true);
        return true;
      }
      if (token.equals("NESTB")) {
        target.setIsNotSpecified(true);
        return true;
      }
    }
    if (value.isPresent()) {
      target.setAltitude(value.orElseThrow().intValue());
      target.setIsMsl(true);
      return true;
    }
    return false;
  }

  private static HoldRvsmMinimumMaximumAltitudeConstraint rvsmLevels(Optional<Integer> minimum, Optional<Integer> maximum) {
    HoldRvsmMinimumMaximumAltitudeConstraint target = new HoldRvsmMinimumMaximumAltitudeConstraint();
    minimum.ifPresent(value -> {
      RouteMinimumAltitude altitude = new RouteMinimumAltitude();
      altitude.setAltitude(100 * value);
      altitude.setIsFlightLevel(true);
      target.setMinimumAltitude(altitude);
    });
    maximum.ifPresent(value -> {
      RouteMaximumAltitude altitude = new RouteMaximumAltitude();
      altitude.setAltitude(100 * value);
      altitude.setIsFlightLevel(true);
      target.setMaximumAltitude(altitude);
    });
    return target;
  }

  private static <K, V> Map<K, List<V>> group(List<V> records, Function<V, K> key) {
    Map<K, List<V>> groups = new LinkedHashMap<>();
    for (V record : records) {
      groups.computeIfAbsent(key.apply(record), ignored -> new ArrayList<>()).add(record);
    }
    return groups;
  }

  private static DatatypeFactory datatypeFactory() {
    try {
      return DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException("XML duration support unavailable", e);
    }
  }

  private record ProcedureKey(String portSection, String airport, String icao, String identifier, String section) {
  }

  private record TransitionKey(String identifier, String category, String routeType, String missedVariant) {
  }
}
