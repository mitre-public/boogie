package org.mitre.boogie.xml.fixtures;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.Airway;
import org.mitre.boogie.xml.v23_4.generated.AirwayLeg;
import org.mitre.boogie.xml.v23_4.generated.AirwayWaypointDescription;
import org.mitre.boogie.xml.v23_4.generated.Airways;
import org.mitre.boogie.xml.v23_4.generated.Course;
import org.mitre.boogie.xml.v23_4.generated.EnrouteAirwayDirectionalRestriction;
import org.mitre.boogie.xml.v23_4.generated.EnrouteAirwayRouteType;
import org.mitre.boogie.xml.v23_4.generated.Level;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.boogie.xml.v23_4.generated.RouteMaximumAltitude;
import org.mitre.boogie.xml.v23_4.generated.RouteMinimumAltitude;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;
import org.mitre.tdp.boogie.dafif.utils.DafifRnp;

/** Merges DAFIF direction records into the connected paths supported by the v23_4 airway schema. */
final class DafifXmlAirways {

  private DafifXmlAirways() {}

  static void populate(DafifXmlFixture.Records records, AeroPublication publication, DafifXmlReferences refs) {
    populate(records.ats(), publication, refs);
  }

  static void populate(List<DafifAirTrafficSegment> segments, AeroPublication publication, DafifXmlReferences refs) {
    var airways = new Airways();
    publication.setAirways(airways);
    Map<String, Integer> sections = new HashMap<>();
    for (var path : DafifAirwayGraph.paths(segments)) {
      var airway = new Airway();
      airway.setIdentifier(path.identifier());
      int section = sections.merge(path.identifier(), 1, Integer::sum) - 1;
      airway.setReferenceId(DafifXmlReferences.id("airway", path.identifier(), Integer.toString(section)));
      airway.setRecordType(RecordType.STANDARD);
      var types = path.steps().stream().map(step -> routeType(step.edge().properties().routeType())).distinct().toList();
      if (types.stream().noneMatch(Objects::isNull)) {
        // A parent default is required; every actual segment supplies its own overriding route type.
        airway.setAirwayRouteType(types.get(0));
      }
      airways.getAirway().add(airway);
      for (var step : path.steps()) {
        if (airway.getAirwayLeg().isEmpty()) {
          airway.getAirwayLeg().add(endpoint(step.from(), step.edge(), 0, refs));
        }
        var from = airway.getAirwayLeg().get(airway.getAirwayLeg().size() - 1);
        mergeEndpoint(from, step.from(), step.edge());
        var to = endpoint(step.to(), step.edge(), airway.getAirwayLeg().size(), refs);
        segment(step, from, to);
        airway.getAirwayLeg().add(to);
      }
      // Shared endpoints may have descriptions from both directions and adjacent segments.
      for (var leg : airway.getAirwayLeg()) {
        var description = leg.getWaypointDescription();
        if (Boolean.TRUE.equals(description.isIsEssential()) && Boolean.TRUE.equals(description.isIsNonEssential())) {
          // Conflicting source classifications remain in notes instead of asserting both on one XML point.
          description.setIsEssential(null);
          description.setIsNonEssential(null);
        }
        var notes = leg.getNotes().stream().distinct().sorted().toList();
        leg.getNotes().clear();
        leg.getNotes().addAll(notes);
      }
    }
  }

  private static AirwayLeg endpoint(DafifAirwayGraph.Key key, DafifAirwayGraph.Edge edge,
      int sequence, DafifXmlReferences refs) {
    var target = new AirwayLeg();
    target.setSequenceNumber(sequence);
    target.setRecordType(RecordType.STANDARD);
    target.setFixIdent(key.identifier());
    var points = endpoints(key, edge);
    Object fix = null;
    if (points.stream().anyMatch(point -> "V".equals(point.description1()) || "N".equals(point.description1()))) {
      fix = refs.waypointNavaid(key.identifier(), key.country());
    }
    if (fix == null) {
      fix = refs.waypoint(key.identifier(), key.country());
    }
    target.setFixRef(Objects.requireNonNull(fix, () -> "Missing DAFIF ATS endpoint: " + key));
    target.setWaypointDescription(new AirwayWaypointDescription());
    mergeEndpoint(target, key, edge);
    return target;
  }

  private static List<Endpoint> endpoints(DafifAirwayGraph.Key key, DafifAirwayGraph.Edge edge) {
    return Stream.concat(edge.forward().stream(), edge.backward().stream())
        .map(source -> {
          if (key.identifier().equals(source.waypoint1WaypointIdentifierWptIdent())
              && key.country().equals(source.waypoint1CountryCode())) {
            return start(source);
          }
          return end(source);
        }).distinct().toList();
  }

  private static void mergeEndpoint(AirwayLeg target, DafifAirwayGraph.Key key, DafifAirwayGraph.Edge edge) {
    for (var source : endpoints(key, edge)) {
      endpointDescription(target, source);
    }
  }

  private static void endpointDescription(AirwayLeg target, Endpoint source) {
    var description = target.getWaypointDescription();
    if ("E".equals(source.description1())) {
      description.setIsEssential(true);
    } else if ("R".equals(source.description1())) {
      description.setIsNonEssential(true);
    }
    source.compulsory().filter("C"::equals).ifPresent(value -> description.setIsAtcCompulsoryReportingPoint(true));
    source.coastal().filter("C"::equals).ifPresent(value -> description.setIsOceanicGateway(true));
    // Preserve distinctions without an equivalent ARINC field, including the published endpoint coordinates.
    target.getNotes().add("DAFIF endpoint: ICAO=" + source.icao() + ";country=" + source.country()
        + ";navaidType=" + source.navaidType().map(Object::toString).orElse("")
        + ";description=" + source.description1() + source.endOfRoute().orElse(" ")
        + source.compulsory().orElse(" ") + source.coastal().orElse(" ")
        + ";latitude=" + source.latitude().map(Object::toString).orElse("")
        + ";longitude=" + source.longitude().map(Object::toString).orElse("")
        + ";geodeticLatitude=" + source.geodeticLatitude().orElse("")
        + ";geodeticLongitude=" + source.geodeticLongitude().orElse(""));
  }

  private static void segment(DafifAirwayGraph.Step step, AirwayLeg from, AirwayLeg to) {
    var edge = step.edge();
    List<DafifAirTrafficSegment> forward = edge.forward();
    List<DafifAirTrafficSegment> backward = edge.backward();
    boolean forwardPublished = edge.hasForwardSource();
    boolean backwardPublished = edge.hasBackwardSource();
    if (!step.from().equals(edge.first())) {
      forward = edge.backward();
      backward = edge.forward();
      forwardPublished = edge.hasBackwardSource();
      backwardPublished = edge.hasForwardSource();
    }
    Optional<DafifAirTrafficSegment> f = forward.stream().findFirst();
    Optional<DafifAirTrafficSegment> b = backward.stream().findFirst();
    // An explicit opposite-direction profile takes precedence over the general BIDIRECT flag.
    boolean allowForward = f.isPresent() || (!forwardPublished && b.flatMap(DafifAirTrafficSegment::biDirectional).filter("Y"::equals).isPresent());
    boolean allowBackward = b.isPresent() || (!backwardPublished && f.flatMap(DafifAirTrafficSegment::biDirectional).filter("Y"::equals).isPresent());
    if (!allowBackward) {
      from.setLegDirectionRestriction(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD);
    } else if (!allowForward) {
      from.setLegDirectionRestriction(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD);
    }
    var properties = edge.properties();
    from.setAirwayRouteType(routeType(properties.routeType()));
    from.setLevel(level(properties.level()));
    properties.distance().map(BigDecimal::valueOf).ifPresent(from::setRouteDistanceFrom);
    properties.rnp().map(DafifRnp::nauticalMiles).ifPresent(from::setRnp);

    // Orient published bearings to the output path. A reverse inbound is the reciprocal forward outbound.
    f.flatMap(DafifAirTrafficSegment::atsRouteOutboundMagneticCourse).flatMap(DafifXmlAirways::course)
        .or(() -> b.flatMap(DafifAirTrafficSegment::atsRouteInboundMagneticCourse).flatMap(DafifXmlAirways::reciprocalCourse))
        .ifPresent(from::setOutboundCourse);
    f.flatMap(DafifAirTrafficSegment::atsRouteInboundMagneticCourse).flatMap(DafifXmlAirways::course)
        .or(() -> b.flatMap(DafifAirTrafficSegment::atsRouteOutboundMagneticCourse).flatMap(DafifXmlAirways::reciprocalCourse))
        .ifPresent(to::setInboundCourse);

    Optional<DafifAirTrafficSegment> forwardLimits = f;
    Optional<DafifAirTrafficSegment> backwardLimits = b;
    if (f.isEmpty() && allowForward) forwardLimits = b;
    if (b.isEmpty() && allowBackward) backwardLimits = f;
    minimumAltitudes(from, forwardLimits.flatMap(DafifXmlAirways::minimum), backwardLimits.flatMap(DafifXmlAirways::minimum));
    maximumAltitudes(from, forwardLimits.flatMap(DafifXmlAirways::maximum), backwardLimits.flatMap(DafifXmlAirways::maximum));
    Stream.concat(forward.stream(), backward.stream()).forEach(source -> {
      source.cycleDate().map(DafifXmlAirways::cycle).ifPresent(value -> {
        from.setCycleDate(value);
        to.setCycleDate(value);
      });
      sourceNotes(source, from);
      source.atsRouteInboundMagneticCourse().filter(value -> value.endsWith("G"))
          .ifPresent(value -> to.getNotes().add("DAFIF inbound grid course=" + value));
    });
  }

  private static Optional<Altitude> minimum(DafifAirTrafficSegment source) {
    return source.minimumAltitude().or(source::lowerLimit).map(DafifXmlAirways::altitude);
  }

  private static Optional<Altitude> maximum(DafifAirTrafficSegment source) {
    return source.maxAuthorizedAltitude().or(source::upperLimit).map(DafifXmlAirways::altitude);
  }

  private static void sourceNotes(DafifAirTrafficSegment source, AirwayLeg target) {
    target.getNotes().add("DAFIF ATS: sequence=" + source.atsRouteSequenceNumber()
        + ";direction=" + source.atsRouteDirection() + ";type=" + source.atsRouteType()
        + ";ICAO=" + source.icaoCode() + ";bidirectional=" + source.biDirectional().orElse("")
        + ";frequencyClass=" + source.frequencyClass() + ";status=" + source.atsRouteStatus()
        + ";cruiseLevel=" + source.cruiseLevelIndicator().orElse("")
        + ";designator=" + source.atsDesignator().orElse("")
        + ";minimumAltitude=" + source.minimumAltitude().orElse("")
        + ";lowerLimit=" + source.lowerLimit().orElse("")
        + ";maximumAuthorizedAltitude=" + source.maxAuthorizedAltitude().orElse("")
        + ";upperLimit=" + source.upperLimit().orElse("")
        + ";outboundCourse=" + source.atsRouteOutboundMagneticCourse().orElse("")
        + ";inboundCourse=" + source.atsRouteInboundMagneticCourse().orElse(""));
  }

  private static void minimumAltitudes(AirwayLeg target, Optional<Altitude> forward, Optional<Altitude> backward) {
    if (forward.equals(backward)) {
      forward.map(value -> minimumAltitude(value, null)).ifPresent(target.getMinimumAltitudes()::add);
    } else {
      forward.map(value -> minimumAltitude(value, EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD)).ifPresent(target.getMinimumAltitudes()::add);
      backward.map(value -> minimumAltitude(value, EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD)).ifPresent(target.getMinimumAltitudes()::add);
    }
  }

  private static void maximumAltitudes(AirwayLeg target, Optional<Altitude> forward, Optional<Altitude> backward) {
    if (forward.equals(backward)) {
      forward.map(value -> maximumAltitude(value, null)).ifPresent(target.getMaximumAltitudes()::add);
    } else {
      forward.map(value -> maximumAltitude(value, EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD)).ifPresent(target.getMaximumAltitudes()::add);
      backward.map(value -> maximumAltitude(value, EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD)).ifPresent(target.getMaximumAltitudes()::add);
    }
  }

  private static RouteMinimumAltitude minimumAltitude(Altitude value, EnrouteAirwayDirectionalRestriction direction) {
    var target = new RouteMinimumAltitude();
    altitude(target, value);
    target.setAltitudeDirectionRestriction(direction);
    return target;
  }

  private static RouteMaximumAltitude maximumAltitude(Altitude value, EnrouteAirwayDirectionalRestriction direction) {
    var target = new RouteMaximumAltitude();
    if (value.unlimited()) {
      target.setIsUnlimited(true);
    } else {
      altitude(target, value);
    }
    target.setAltitudeDirectionRestriction(direction);
    return target;
  }

  private static Altitude altitude(String value) {
    if ("UNLTD".equals(value)) return new Altitude(null, false, true);
    if (value.startsWith("FL")) {
      return new Altitude(Integer.parseInt(value.substring(2)) * 100, true, false);
    }
    return new Altitude(Integer.parseInt(value), false, false);
  }

  private static void altitude(AirspaceRouteHoldAltitude target, Altitude value) {
    target.setAltitude(value.feet());
    if (value.flightLevel()) {
      target.setIsFlightLevel(true);
    } else {
      target.setIsMsl(true);
    }
  }

  private record Altitude(Integer feet, boolean flightLevel, boolean unlimited) {}

  private static Optional<Course> reciprocalCourse(String value) {
    return course(value).map(target -> {
      target.setCourseValue(target.getCourseValue().add(BigDecimal.valueOf(180))
          .remainder(BigDecimal.valueOf(360)).stripTrailingZeros());
      return target;
    });
  }

  private static Optional<Course> course(String value) {
    // The JAXB Course type has magnetic/true support; a grid bearing must remain explicitly identified in notes.
    if (value.endsWith("G")) {
      return Optional.empty();
    }
    var target = new Course();
    if (value.endsWith("T")) {
      target.setCourseValue(new BigDecimal(value.substring(0, value.length() - 1)));
      target.setIsTrue(true);
    } else {
      target.setCourseValue(new BigDecimal(value));
      target.setIsTrue(false);
    }
    return Optional.of(target);
  }

  private static EnrouteAirwayRouteType routeType(String value) {
    return switch (value) {
      case "R" -> EnrouteAirwayRouteType.RNAV_RNP;
      case "T" -> EnrouteAirwayRouteType.TACAN;
      case "W" -> EnrouteAirwayRouteType.OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER;
      default -> null; // Other DAFIF classifications have no exact equivalent and remain in the ATS notes.
    };
  }

  private static Level level(String value) {
    return switch (value) {
      case "B" -> Level.ALL_ALT;
      case "H" -> Level.HIGH_ALT;
      case "L" -> Level.LOW_ALT;
      default -> throw new IllegalArgumentException("Unsupported DAFIF ATS level: " + value);
    };
  }

  private static String cycle(Integer value) {
    return String.format("%04d", value % 10000);
  }

  private static Endpoint start(DafifAirTrafficSegment source) {
    return new Endpoint(source.waypoint1WaypointIdentifierWptIdent(), source.waypoint1CountryCode(), source.waypoint1IcaoCode(),
        source.waypoint1NavaidType(), source.waypoint1AtsWaypointDescriptionCode1(), source.waypoint1AtsWaypointDescriptionCode2(),
        source.waypoint1AtsWaypointDescriptionCode3(), source.waypoint1AtsWaypointDescriptionCode4(),
        source.waypoint1DegreesLatitude(), source.waypoint1DegreesLongitude(), source.waypoint1GeodeticLatitude(), source.waypoint1GeodeticLongitude());
  }

  private static Endpoint end(DafifAirTrafficSegment source) {
    return new Endpoint(source.waypoint2WaypointIdentifierWptIdent(), source.waypoint2CountryCode(), source.waypoint2IcaoCode(),
        source.waypoint2NavaidType(), source.waypoint2AtsWaypointDescriptionCode1(), source.waypoint2AtsWaypointDescriptionCode2(),
        source.waypoint2AtsWaypointDescriptionCode3(), source.waypoint2AtsWaypointDescriptionCode4(),
        source.waypoint2DegreesLatitude(), source.waypoint2DegreesLongitude(), source.waypoint2GeodeticLatitude(), source.waypoint2GeodeticLongitude());
  }

  private record Endpoint(String identifier, String country, String icao, Optional<Integer> navaidType,
                          String description1, Optional<String> endOfRoute, Optional<String> compulsory, Optional<String> coastal,
                          Optional<Double> latitude, Optional<Double> longitude,
                          Optional<String> geodeticLatitude, Optional<String> geodeticLongitude) {}
}
