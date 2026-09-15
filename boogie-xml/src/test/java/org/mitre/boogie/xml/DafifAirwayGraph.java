package org.mitre.boogie.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

/** Deterministic paths through the DAFIF airway graph, retaining both directions of each published edge. */
final class DafifAirwayGraph {
  private static final Comparator<Properties> PROPERTIES = Comparator
      .comparing((Properties value) -> value.distance().orElse(null), Comparator.nullsFirst(Double::compareTo))
      .thenComparing(value -> value.rnp().orElse(null), Comparator.nullsFirst(Integer::compareTo))
      .thenComparing(Properties::routeType)
      .thenComparing(Properties::level);
  private static final Comparator<DafifAirTrafficSegment> ROWS = Comparator
      .comparing(DafifAirTrafficSegment::atsRouteDirection)
      .thenComparingInt(DafifAirTrafficSegment::atsRouteSequenceNumber)
      .thenComparing(DafifAirwayGraph::rowKey);

  private DafifAirwayGraph() {}

  static List<Path> paths(List<DafifAirTrafficSegment> records) {
    Map<String, List<DafifAirTrafficSegment>> byIdentifier = new TreeMap<>();
    for (var record : records) {
      byIdentifier.computeIfAbsent(record.atsIdentifier(), key -> new ArrayList<>()).add(record);
    }
    List<Path> result = new ArrayList<>();
    for (var group : byIdentifier.entrySet()) {
      decompose(group.getKey(), edges(group.getKey(), group.getValue()), result);
    }
    return List.copyOf(result);
  }

  private static List<Edge> edges(String identifier, List<DafifAirTrafficSegment> records) {
    Map<Pair, Map<Properties, List<DafifAirTrafficSegment>>> groups = new TreeMap<>();
    for (var record : records) {
      Key from = from(record);
      Key to = to(record);
      Pair pair = pair(from, to);
      Properties properties = new Properties(record.atsRouteDistance(), record.requiredNavPerformance(),
          record.atsRouteType(), record.level());
      groups.computeIfAbsent(pair, key -> new TreeMap<>(PROPERTIES))
          .computeIfAbsent(properties, key -> new ArrayList<>()).add(record);
    }
    List<Edge> result = new ArrayList<>();
    for (var pair : groups.entrySet()) {
      boolean hasForwardSource = false;
      boolean hasBackwardSource = false;
      for (var rows : pair.getValue().values()) {
        for (var row : rows) {
          if (from(row).equals(pair.getKey().first())) {
            hasForwardSource = true;
          } else {
            hasBackwardSource = true;
          }
        }
      }
      for (var group : pair.getValue().entrySet()) {
        List<DafifAirTrafficSegment> forward = new ArrayList<>();
        List<DafifAirTrafficSegment> backward = new ArrayList<>();
        for (var row : group.getValue()) {
          if (from(row).equals(pair.getKey().first())) {
            forward.add(row);
          } else {
            backward.add(row);
          }
        }
        forward.sort(ROWS);
        backward.sort(ROWS);
        validate(identifier, pair.getKey(), "forward", forward);
        validate(identifier, pair.getKey(), "backward", backward);
        result.add(new Edge(pair.getKey().first(), pair.getKey().second(), group.getKey(),
            List.copyOf(forward), List.copyOf(backward), hasForwardSource, hasBackwardSource));
      }
    }
    return result;
  }

  private static void validate(String identifier, Pair pair, String direction, List<DafifAirTrafficSegment> rows) {
    if (rows.size() < 2) {
      return;
    }
    var first = rows.get(0);
    Limits expected = limits(first);
    for (int i = 1; i < rows.size(); i++) {
      var row = rows.get(i);
      if (!expected.equals(limits(row))) {
        throw new IllegalArgumentException("Conflicting DAFIF airway " + identifier + " " + pair.first() + " -> " + pair.second()
            + " " + direction + " altitude limits: " + first.atsRouteDirection() + "/" + first.atsRouteSequenceNumber()
            + " " + expected + " versus " + row.atsRouteDirection() + "/" + row.atsRouteSequenceNumber() + " " + limits(row));
      }
    }
  }

  private static Limits limits(DafifAirTrafficSegment row) {
    return new Limits(row.minimumAltitude().or(row::lowerLimit).map(DafifAirwayGraph::altitude),
        row.maxAuthorizedAltitude().or(row::upperLimit).map(DafifAirwayGraph::altitude));
  }

  private static Altitude altitude(String value) {
    if ("UNLTD".equals(value)) {
      return new Altitude(null, false, true);
    }
    if (value.startsWith("FL")) {
      return new Altitude(Integer.parseInt(value.substring(2)) * 100, true, false);
    }
    return new Altitude(Integer.parseInt(value), false, false);
  }

  private static void decompose(String identifier, List<Edge> edges, List<Path> result) {
    Map<Key, List<Edge>> adjacent = new TreeMap<>();
    for (var edge : edges) {
      adjacent.computeIfAbsent(edge.first(), key -> new ArrayList<>()).add(edge);
      if (!edge.first().equals(edge.second())) {
        adjacent.computeIfAbsent(edge.second(), key -> new ArrayList<>()).add(edge);
      }
    }
    Set<Key> stops = new HashSet<>();
    for (var entry : adjacent.entrySet()) {
      Key node = entry.getKey();
      entry.getValue().sort(Comparator.comparing((Edge edge) -> other(edge, node)).thenComparing(Edge::properties, PROPERTIES));
      int degree = 0;
      Set<Key> neighbors = new HashSet<>();
      boolean parallel = false;
      for (var edge : entry.getValue()) {
        Key neighbor = other(edge, node);
        degree++;
        if (neighbor.equals(node)) {
          degree++; // A loop has two incidences at its one vertex.
        }
        if (!neighbors.add(neighbor)) {
          parallel = true;
        }
      }
      if (degree != 2 || parallel) {
        stops.add(node);
      }
    }

    Set<Edge> visited = new HashSet<>();
    for (var entry : adjacent.entrySet()) {
      if (stops.contains(entry.getKey())) {
        for (var edge : entry.getValue()) {
          if (!visited.contains(edge)) {
            result.add(walk(identifier, entry.getKey(), edge, adjacent, stops, visited));
          }
        }
      }
    }
    // Components without a stop are cycles. Begin at their smallest node/neighbor and keep the closing edge.
    for (var entry : adjacent.entrySet()) {
      for (var edge : entry.getValue()) {
        if (!visited.contains(edge)) {
          result.add(walk(identifier, entry.getKey(), edge, adjacent, stops, visited));
        }
      }
    }
  }

  private static Path walk(String identifier, Key start, Edge first, Map<Key, List<Edge>> adjacent,
                           Set<Key> stops, Set<Edge> visited) {
    List<Step> steps = new ArrayList<>();
    Key from = start;
    Edge edge = first;
    while (edge != null) {
      visited.add(edge);
      Key to = other(edge, from);
      steps.add(new Step(from, to, edge));
      if (stops.contains(to)) {
        break;
      }
      from = to;
      edge = null;
      for (var candidate : adjacent.get(to)) {
        if (!visited.contains(candidate)) {
          edge = candidate;
          break;
        }
      }
    }
    return new Path(identifier, List.copyOf(steps));
  }

  private static Key other(Edge edge, Key node) {
    if (edge.first().equals(node)) {
      return edge.second();
    }
    return edge.first();
  }

  private static Pair pair(Key from, Key to) {
    if (from.compareTo(to) <= 0) {
      return new Pair(from, to);
    }
    return new Pair(to, from);
  }

  private static Key from(DafifAirTrafficSegment row) {
    return new Key(row.waypoint1WaypointIdentifierWptIdent(), row.waypoint1CountryCode());
  }

  private static Key to(DafifAirTrafficSegment row) {
    return new Key(row.waypoint2WaypointIdentifierWptIdent(), row.waypoint2CountryCode());
  }

  private static String rowKey(DafifAirTrafficSegment row) {
    // Complete tie-breaker for duplicate sequence numbers with different source metadata.
    var values = Arrays.asList(row.atsIdentifier(), row.atsRouteSequenceNumber(), row.atsRouteDirection(), row.atsRouteType(),
        row.icaoCode(), row.biDirectional(), row.frequencyClass(), row.level(), row.atsRouteStatus(),
        row.waypoint1IcaoCode(), row.waypoint1NavaidType(), row.waypoint1WaypointIdentifierWptIdent(), row.waypoint1CountryCode(),
        row.waypoint1AtsWaypointDescriptionCode1(), row.waypoint1AtsWaypointDescriptionCode2(),
        row.waypoint1AtsWaypointDescriptionCode3(), row.waypoint1AtsWaypointDescriptionCode4(),
        row.waypoint1GeodeticLatitude(), row.waypoint1DegreesLatitude(), row.waypoint1GeodeticLongitude(), row.waypoint1DegreesLongitude(),
        row.waypoint2IcaoCode(), row.waypoint2NavaidType(), row.waypoint2WaypointIdentifierWptIdent(), row.waypoint2CountryCode(),
        row.waypoint2AtsWaypointDescriptionCode1(), row.waypoint2AtsWaypointDescriptionCode2(),
        row.waypoint2AtsWaypointDescriptionCode3(), row.waypoint2AtsWaypointDescriptionCode4(),
        row.waypoint2GeodeticLatitude(), row.waypoint2DegreesLatitude(), row.waypoint2GeodeticLongitude(), row.waypoint2DegreesLongitude(),
        row.atsRouteOutboundMagneticCourse(), row.atsRouteDistance(), row.atsRouteInboundMagneticCourse(), row.minimumAltitude(),
        row.upperLimit(), row.lowerLimit(), row.maxAuthorizedAltitude(), row.cruiseLevelIndicator(), row.requiredNavPerformance(),
        row.cycleDate(), row.atsDesignator());
    StringBuilder key = new StringBuilder();
    for (Object value : values) {
      String text = Objects.toString(value, "");
      key.append(text.length()).append(':').append(text);
    }
    return key.toString();
  }

  record Key(String identifier, String country) implements Comparable<Key> {
    @Override
    public int compareTo(Key other) {
      int identifierOrder = identifier.compareTo(other.identifier);
      if (identifierOrder != 0) {
        return identifierOrder;
      }
      return country.compareTo(other.country);
    }
  }

  record Path(String identifier, List<Step> steps) {}

  record Step(Key from, Key to, Edge edge) {}

  record Properties(Optional<Double> distance, Optional<Integer> rnp, String routeType, String level) {}

  static final class Edge {
    private final Key first;
    private final Key second;
    private final Properties properties;
    private final List<DafifAirTrafficSegment> forward;
    private final List<DafifAirTrafficSegment> backward;
    private final boolean hasForwardSource;
    private final boolean hasBackwardSource;

    private Edge(Key first, Key second, Properties properties, List<DafifAirTrafficSegment> forward,
                 List<DafifAirTrafficSegment> backward, boolean hasForwardSource, boolean hasBackwardSource) {
      this.first = first;
      this.second = second;
      this.properties = properties;
      this.forward = forward;
      this.backward = backward;
      this.hasForwardSource = hasForwardSource;
      this.hasBackwardSource = hasBackwardSource;
    }

    Key first() { return first; }
    Key second() { return second; }
    Properties properties() { return properties; }
    List<DafifAirTrafficSegment> forward() { return forward; }
    List<DafifAirTrafficSegment> backward() { return backward; }
    boolean hasForwardSource() { return hasForwardSource; }
    boolean hasBackwardSource() { return hasBackwardSource; }
  }

  private record Pair(Key first, Key second) implements Comparable<Pair> {
    @Override
    public int compareTo(Pair other) {
      int firstOrder = first.compareTo(other.first);
      if (firstOrder != 0) {
        return firstOrder;
      }
      return second.compareTo(other.second);
    }
  }

  private record Limits(Optional<Altitude> minimum, Optional<Altitude> maximum) {}

  private record Altitude(Integer feet, boolean flightLevel, boolean unlimited) {}
}
