package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jakarta.xml.bind.JAXBContext;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.A424Point;
import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.AirwayLeg;
import org.mitre.boogie.xml.v23_4.generated.EnrouteAirwayDirectionalRestriction;
import org.mitre.boogie.xml.v23_4.generated.RouteMaximumAltitude;
import org.mitre.boogie.xml.v23_4.generated.RouteMinimumAltitude;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@Tag("DAFIF")
@Tag("INTEGRATION")
class DafifXmlFixtureTest {

  @Test
  void marshalsTheSupportedDafifTablesWithUniqueIdsAndResolvableReferences() throws Exception {
    var fixture = DafifXmlFixture.load();
    var source = fixture.source();
    var handler = new PublicationCounts();
    JAXBContext.newInstance(AeroPublication.class).createMarshaller().marshal(fixture.publication(), handler);

    assertAll(
        () -> assertEquals(10011, source.airports().size()),
        () -> assertEquals(13652, source.runways().size()),
        () -> assertEquals(5358, source.addRunways().size()),
        () -> assertEquals(13262, source.ils().size()),
        () -> assertEquals(8070, source.navaids().size()),
        () -> assertEquals(144997, source.waypoints().size()),
        () -> assertEquals(31932, source.terminalParents().size()),
        () -> assertEquals(349430, source.terminalSegments().size()),
        () -> assertEquals(31037, source.terminalSegments().stream().filter(leg -> leg.speedLimit1().isPresent()).count()),
        () -> assertEquals(156666, source.ats().size()),
        () -> assertEquals(source.airports().size(), handler.count("airport")),
        () -> assertEquals(2 * source.runways().size(), handler.count("runway"), "Both runway ends"),
        () -> assertEquals(4384, handler.count("localizerGlideslope")),
        () -> assertTrue(handler.count("waypoint") + handler.count("terminalWaypoint") >= source.waypoints().size()),
        () -> assertEquals(source.terminalParents().size(), handler.count("sid") + handler.count("star") + handler.count("approach")),
        () -> assertEquals(source.terminalSegments().size(), handler.count("procedureLeg"), "Includes missed approaches"),
        () -> assertEquals(31004, handler.count("speedLimit"), "The other 33 speed limits are aircraft-qualified and remain in notes"),
        () -> assertEquals(14113, handler.count("airway"), "Merges opposite directions and separates continuous paths"),
        () -> assertEquals(103784, handler.count("airwayLeg"), "89671 geometric edges plus one endpoint per path"),
        () -> assertTrue(handler.references.size() > 10000, "Contains cross-record references"),
        () -> assertTrue(handler.ids.containsAll(handler.references), () -> "Missing reference targets: " + handler.missingReferences()),
        () -> assertEquals(2601, fixture.publication().getCycleDate()),
        () -> assertEquals("2026-01-22T00:00:00Z", fixture.publication().getStartOfValidity().toXMLFormat())
    );
    assertAirwayGraphMatchesSource(fixture);
  }

  private static void assertAirwayGraphMatchesSource(DafifXmlFixture.Loaded fixture) {
    Map<DirectedEdge, Limits> expected = new HashMap<>();
    for (DafifAirTrafficSegment source : fixture.source().ats()) {
      Point from = new Point(source.waypoint1WaypointIdentifierWptIdent(), source.waypoint1CountryCode());
      Point to = new Point(source.waypoint2WaypointIdentifierWptIdent(), source.waypoint2CountryCode());
      DirectedEdge edge = new DirectedEdge(source.atsIdentifier(), from, to);
      Limits limits = new Limits(
          source.minimumAltitude().or(source::lowerLimit).map(DafifXmlFixtureTest::sourceAltitude).orElse(null),
          source.maxAuthorizedAltitude().or(source::upperLimit).map(DafifXmlFixtureTest::sourceAltitude).orElse(null));
      assertNull(expected.putIfAbsent(edge, limits), () -> "Duplicate source directed ATS edge: " + edge);
    }

    Map<String, Point> points = sourcePointIds(fixture.source());
    Map<DirectedEdge, Limits> actual = new HashMap<>();
    Set<Geometry> geometries = new HashSet<>();
    Set<String> identifiers = new HashSet<>();
    int bidirectional = 0;
    int oneWay = 0;
    int directionalMinimums = 0;
    int sameFeetDifferentReferences = 0;
    for (var airway : fixture.publication().getAirways().getAirway()) {
      identifiers.add(airway.getIdentifier());
      List<AirwayLeg> legs = airway.getAirwayLeg();
      assertTrue(legs.size() >= 2, () -> "Airway path has no edge: " + airway.getReferenceId());
      for (int i = 0; i + 1 < legs.size(); i++) {
        AirwayLeg leg = legs.get(i);
        Point from = sourcePoint(leg, points);
        Point to = sourcePoint(legs.get(i + 1), points);
        Geometry geometry = geometry(airway.getIdentifier(), from, to);
        assertTrue(geometries.add(geometry), () -> "Geometric edge emitted in more than one path: " + geometry);
        boolean forward = leg.getLegDirectionRestriction() != EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD;
        boolean backward = leg.getLegDirectionRestriction() != EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD;
        Limits forwardLimits = outputLimits(leg, EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD);
        Limits backwardLimits = outputLimits(leg, EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD);
        if (forward) {
          DirectedEdge edge = new DirectedEdge(airway.getIdentifier(), from, to);
          assertNull(actual.putIfAbsent(edge, forwardLimits), () -> "Duplicate XML directed edge: " + edge);
        }
        if (backward) {
          DirectedEdge edge = new DirectedEdge(airway.getIdentifier(), to, from);
          assertNull(actual.putIfAbsent(edge, backwardLimits), () -> "Duplicate XML directed edge: " + edge);
        }
        if (forward && backward) {
          bidirectional++;
          Altitude forwardMinimum = forwardLimits.minimum();
          Altitude backwardMinimum = backwardLimits.minimum();
          if (forwardMinimum != null && backwardMinimum != null && !forwardMinimum.equals(backwardMinimum)) {
            directionalMinimums++;
            if (Objects.equals(forwardMinimum.feet(), backwardMinimum.feet())
                && forwardMinimum.flightLevel() != backwardMinimum.flightLevel()) {
              sameFeetDifferentReferences++;
            }
          }
        } else {
          oneWay++;
        }
      }
    }
    assertEquals(9845, identifiers.size(), "Published airway identifiers");
    assertEquals(89671, geometries.size(), "Every undirected source edge is represented once");
    assertEquals(66995, bidirectional, "Opposite source rows share one bidirectional XML edge");
    assertEquals(22676, oneWay, "Unmatched directional source rows remain one-way");
    assertEquals(9695, directionalMinimums, "Different forward/backward MEAs remain separate");
    assertEquals(7, sameFeetDifferentReferences, "Equal numbers with FL versus MSL references are distinct limits");
    assertEquals(156666, expected.size(), "Source directed edges");
    assertTrue(expected.keySet().equals(actual.keySet()), () -> graphDifference(expected, actual));
    expected.forEach((edge, limits) -> assertEquals(limits, actual.get(edge), () -> "Directional altitude limits for " + edge));
  }

  private static Map<String, Point> sourcePointIds(DafifXmlFixture.Records source) {
    Set<Point> used = new HashSet<>();
    for (var segment : source.ats()) {
      used.add(new Point(segment.waypoint1WaypointIdentifierWptIdent(), segment.waypoint1CountryCode()));
      used.add(new Point(segment.waypoint2WaypointIdentifierWptIdent(), segment.waypoint2CountryCode()));
    }
    Map<String, Point> result = new HashMap<>();
    for (var waypoint : source.waypoints()) {
      Point point = new Point(waypoint.waypointIdentifier(), waypoint.countryCode());
      if (!used.contains(point)) {
        continue;
      }
      addPointId(result, DafifXmlReferences.id("waypoint", point.identifier(), point.country()), point);
      if (Boolean.TRUE.equals(waypoint.waypointPointNavaidFlag())) {
        int type = waypoint.navaidType().orElseThrow();
        String component = switch (type) {
          case 1, 2, 4 -> "vor";
          case 5, 7 -> "ndb";
          case 3, 9 -> "dme";
          default -> throw new AssertionError("Unexpected flagged waypoint navaid type: " + type);
        };
        String id = DafifXmlReferences.id("nav", waypoint.navaidIdentifier().orElseThrow(),
            waypoint.navaidCountryCode().orElseThrow(), Integer.toString(type), waypoint.navaidKeyCode().orElseThrow().toString(), component);
        addPointId(result, id, point);
      }
    }
    return result;
  }

  private static void addPointId(Map<String, Point> points, String id, Point point) {
    Point previous = points.putIfAbsent(id, point);
    if (previous != null) {
      assertEquals(previous, point, () -> "Ambiguous waypoint alias for XML point " + id);
    }
  }

  private static Point sourcePoint(AirwayLeg leg, Map<String, Point> points) {
    A424Point reference = assertInstanceOf(A424Point.class, leg.getFixRef(), "Airway endpoints must reference published points");
    Point point = points.get(reference.getReferenceId());
    if (point == null) {
      throw new AssertionError("Airway endpoint has no source waypoint: " + reference.getReferenceId());
    }
    return point;
  }

  private static Limits outputLimits(AirwayLeg leg, EnrouteAirwayDirectionalRestriction direction) {
    List<RouteMinimumAltitude> minimums = leg.getMinimumAltitudes().stream()
        .filter(value -> value.getAltitudeDirectionRestriction() == null || value.getAltitudeDirectionRestriction() == direction).toList();
    List<RouteMaximumAltitude> maximums = leg.getMaximumAltitudes().stream()
        .filter(value -> value.getAltitudeDirectionRestriction() == null || value.getAltitudeDirectionRestriction() == direction).toList();
    assertTrue(minimums.size() <= 1, "Multiple minimum limits apply to the same edge direction");
    assertTrue(maximums.size() <= 1, "Multiple maximum limits apply to the same edge direction");
    Altitude minimum = null;
    Altitude maximum = null;
    if (!minimums.isEmpty()) {
      minimum = outputAltitude(minimums.get(0), false);
    }
    if (!maximums.isEmpty()) {
      maximum = outputAltitude(maximums.get(0), Boolean.TRUE.equals(maximums.get(0).isIsUnlimited()));
    }
    return new Limits(minimum, maximum);
  }

  private static Altitude outputAltitude(AirspaceRouteHoldAltitude value, boolean unlimited) {
    return new Altitude(value.getAltitude(), Boolean.TRUE.equals(value.isIsFlightLevel()), unlimited);
  }

  private static Altitude sourceAltitude(String value) {
    if (value.equals("UNLTD")) {
      return new Altitude(null, false, true);
    }
    if (value.startsWith("FL")) {
      return new Altitude(Integer.parseInt(value.substring(2)) * 100, true, false);
    }
    return new Altitude(Integer.parseInt(value), false, false);
  }

  private static Geometry geometry(String airway, Point a, Point b) {
    int order = a.country().compareTo(b.country());
    if (order == 0) {
      order = a.identifier().compareTo(b.identifier());
    }
    if (order <= 0) {
      return new Geometry(airway, a, b);
    }
    return new Geometry(airway, b, a);
  }

  private static String graphDifference(Map<DirectedEdge, Limits> expected, Map<DirectedEdge, Limits> actual) {
    return "Directed edges differ: expected=" + expected.size() + "; actual=" + actual.size()
        + "; missing=" + expected.keySet().stream().filter(edge -> !actual.containsKey(edge)).limit(5).toList()
        + "; fabricated=" + actual.keySet().stream().filter(edge -> !expected.containsKey(edge)).limit(5).toList();
  }

  private record Point(String identifier, String country) {}
  private record DirectedEdge(String airway, Point from, Point to) {}
  private record Geometry(String airway, Point first, Point second) {}
  private record Altitude(Integer feet, boolean flightLevel, boolean unlimited) {}
  private record Limits(Altitude minimum, Altitude maximum) {}

  private static final class PublicationCounts extends DefaultHandler {
    private static final Set<String> REFERENCE_ELEMENTS = Set.of(
        "fixRef", "recNavaidRef", "recommendedNavaidRef", "dmeTacanRef", "portRef", "runwayRef",
        "centerFixRef", "localizerRef", "supportingFacilityReference", "controlledAirspaceCenterRef");

    private final Map<String, Integer> counts = new HashMap<>();
    private final Set<String> ids = new HashSet<>();
    private final Set<String> references = new HashSet<>();
    private final StringBuilder reference = new StringBuilder();
    private boolean inReference;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
      counts.merge(localName, 1, Integer::sum);
      String id = attributes.getValue("referenceId");
      if (id != null) {
        assertTrue(ids.add(id), () -> "Duplicate XML ID: " + id);
      }
      inReference = REFERENCE_ELEMENTS.contains(localName);
      reference.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
      if (inReference) {
        reference.append(ch, start, length);
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
      if (inReference) {
        references.add(reference.toString());
      }
      inReference = false;
    }

    int count(String element) {
      return counts.getOrDefault(element, 0);
    }

    Set<String> missingReferences() {
      Set<String> missing = new HashSet<>(references);
      missing.removeAll(ids);
      return missing;
    }
  }
}
