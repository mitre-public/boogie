package org.mitre.boogie.cli;

import static org.mitre.boogie.cli.Layout.header;
import static org.mitre.boogie.cli.Layout.joined;
import static org.mitre.boogie.cli.Layout.leg;
import static org.mitre.boogie.cli.Layout.line;
import static org.mitre.boogie.cli.Layout.list;
import static org.mitre.boogie.cli.Layout.missing;
import static org.mitre.boogie.cli.Layout.number;
import static org.mitre.boogie.cli.Layout.sequence;
import static org.mitre.boogie.cli.Layout.tag;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Formats compact record summaries for the {@code v23_4} XML field layout, matching the fixed-width text views.
 */
final class RecordSummary {

  static final List<String> PROFILES = List.of(
      "Airport",
      "Runway",
      "Waypoint",
      "Heliport",
      "Ndb",
      "VhfNavaid",
      "Airway",
      "Sid",
      "Star",
      "Approach",
      "Procedure",
      "HoldingPattern"
  );

  /**
   * Classes that cover several XML elements, mirroring the text classes; any other class is used as the element name.
   */
  private static final Map<String, Set<String>> ELEMENTS = Map.of(
      "waypoint", Set.of("waypoint", "terminalWaypoint"),
      "ndb", Set.of("ndb", "terminalNdb"),
      "procedure", Set.of("sid", "star", "approach"),
      "localizer", Set.of("localizerGlideslope"),
      "ils", Set.of("localizerGlideslope")
  );

  private static final Set<String> HEADER_FIELDS = Set.of("identifier", "name", "procedureName", "holdingPatternName");

  private RecordSummary() {
  }

  /**
   * The XML element names selected by a class, e.g. waypoint and terminalWaypoint for Waypoint.
   */
  static Set<String> elementNames(String requested) {
    return ELEMENTS.getOrDefault(requested.toLowerCase(Locale.ROOT), Set.of(requested));
  }

  static String identifier(XmlElement element) {
    return element.value("identifier").orElseGet(() -> {
      if (element.name().equalsIgnoreCase("runway")) {
        return runwayIdentifier(element);
      }
      return element.value("fixIdentifier").orElse("");
    });
  }

  /**
   * The values -i compares against: the record's own identifier, an airport's IATA code, and for a record nested in an
   * airport or heliport, that port's identifier and PORT/IDENTIFIER.
   */
  static List<String> identifiers(XmlElement record, String port) {
    List<String> values = new ArrayList<>();
    String own = identifier(record);
    if (!own.isEmpty()) {
      values.add(own);
    }
    record.value("ataIataDesignator").ifPresent(values::add);
    if (!port.isEmpty()) {
      values.add(port);
      if (!own.isEmpty()) {
        values.add(port + "/" + own);
      }
    }
    return values;
  }

  static String format(XmlElement record, String port) {
    StringBuilder result = new StringBuilder();
    switch (record.name().toLowerCase(Locale.ROOT)) {
      case "airport" -> airport(result, record);
      case "heliport" -> heliport(result, record);
      case "runway" -> runway(result, record, port);
      case "waypoint", "terminalwaypoint" -> waypoint(result, record, port);
      case "ndb", "terminalndb" -> ndb(result, record, port);
      case "vhfnavaid" -> vhf(result, record);
      case "airway" -> airway(result, record);
      case "sid", "star", "approach" -> procedure(result, record, port);
      default -> generic(result, record, port);
    }
    return result.toString();
  }

  private static void airport(StringBuilder result, XmlElement airport) {
    header(result, text(airport, "identifier"), text(airport, "name"));
    line(result, "Coordinates: " + coordinates(airport) + " | Elevation: " + unit(airport, "elevation", "ft"));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(airport, "icaoCode")),
        labelled("IATA", text(airport, "ataIataDesignator")),
        labelled("Magnetic variation", variation(airport, "magneticVariation", "magneticVariationValue",
            "magneticVariationEWT")),
        labelled("Longest runway", number(text(airport, "longestRunway")), "ft"),
        labelled("Transition altitude", number(text(airport, "transitionAltitude")), "ft")
    ));
    line(result, "Runways: " + list(airport.children("runway").stream()
        .map(RecordSummary::runwayIdentifier)
        .map(RecordSummary::clean)
        .toList()));

    List<XmlElement> procedures = airport.child("terminalProcedures").map(XmlElement::children).orElse(List.of());
    procedures(result, "SIDs", procedures, "sid");
    procedures(result, "STARs", procedures, "star");
    procedures(result, "Approaches", procedures, "approach");
  }

  private static void procedures(StringBuilder result, String label, List<XmlElement> procedures, String type) {
    List<String> names = procedures.stream()
        .filter(procedure -> procedure.name().equals(type))
        .map(RecordSummary::identifier)
        .map(RecordSummary::clean)
        .filter(name -> !name.isEmpty())
        .distinct()
        .sorted()
        .toList();
    if (!names.isEmpty()) {
      line(result, label + " (" + names.size() + "): " + String.join(", ", names));
    }
  }

  private static void heliport(StringBuilder result, XmlElement heliport) {
    header(result, text(heliport, "identifier"), text(heliport, "name"));
    line(result, "Coordinates: " + coordinates(heliport) + " | Elevation: " + unit(heliport, "elevation", "ft"));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(heliport, "icaoCode")),
        labelled("IATA", text(heliport, "ataIataDesignator")),
        labelled("Magnetic variation", variation(heliport, "magneticVariation", "magneticVariationValue",
            "magneticVariationEWT"))
    ));
    line(result, "Helipads: " + list(heliport.children("helipad").stream()
        .map(RecordSummary::identifier)
        .map(RecordSummary::clean)
        .toList()));
  }

  private static void runway(StringBuilder result, XmlElement runway, String port) {
    header(result, qualified(port, clean(runwayIdentifier(runway))), text(runway, "runwayDescription"));
    line(result, "Coordinates: " + coordinates(runway)
        + " | Threshold elevation: " + unit(runway, "landingThresholdElevation", "ft"));
    boolean trueBearing = text(runway, "runwayBearing/isTrueBearing").equals("true");
    line(result, joined(
        " | ",
        "Length: " + unit(runway, "runwayLength", "ft"),
        "Width: " + unit(runway, "runwayWidth", "ft"),
        labelled(trueBearing ? "True bearing" : "Magnetic bearing", number(text(runway, "runwayBearing/bearingValue")),
            "°"),
        labelled("True bearing", number(text(runway, "runwayTrueBearing")), "°"),
        labelled("Displaced threshold", text(runway, "displacedThresholdDistance"), "ft"),
        labelled("TCH", text(runway, "thresholdCrossingHeight"), "ft")
    ));
  }

  private static void waypoint(StringBuilder result, XmlElement waypoint, String port) {
    header(result, text(waypoint, "identifier"), text(waypoint, "name"));
    line(result, "Coordinates: " + coordinates(waypoint));
    List<String> usage = new ArrayList<>();
    flag(waypoint, "waypointUsage/isHi", "high", usage);
    flag(waypoint, "waypointUsage/isLo", "low", usage);
    flag(waypoint, "waypointUsage/isTerminal", "terminal", usage);
    flag(waypoint, "waypointType/isRnav", "RNAV", usage);
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(waypoint, "icaoCode")),
        "Area: " + (port.isEmpty() ? "enroute" : "terminal " + port),
        labelled("Usage", String.join(", ", usage)),
        labelled("Magnetic variation", variation(waypoint, "magneticVariation", "magneticVariationValue",
            "magneticVariationEWT"))
    ));
  }

  private static void ndb(StringBuilder result, XmlElement ndb, String port) {
    header(result, text(ndb, "identifier"), text(ndb, "name"));
    line(result, joined(
        " | ",
        "Coordinates: " + coordinates(ndb),
        "Frequency: " + missing(frequency(ndb)),
        labelled("Type", clean(ndb.attributes().getOrDefault("type", "")))
    ));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(ndb, "icaoCode")),
        labelled("Airport", port),
        labelled("Magnetic variation", variation(ndb, "magneticVariation", "magneticVariationValue",
            "magneticVariationEWT"))
    ));
  }

  private static void vhf(StringBuilder result, XmlElement vhf) {
    header(result, text(vhf, "identifier"), text(vhf, "name"));
    line(result, joined(
        " | ",
        "Coordinates: " + coordinates(vhf),
        "Frequency: " + missing(frequency(vhf)),
        // xsi:type distinguishes a VOR from its collocated DME or TACAN, which share an identifier.
        labelled("Type", clean(vhf.attributes().getOrDefault("type", "")))
    ));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(vhf, "icaoCode")),
        labelled("Coverage", text(vhf, "navaidClass/vhfNavaidCoverage")),
        labelled("Elevation", text(vhf, "elevation"), "ft"),
        labelled("Station declination", variation(vhf, "stationDeclination", "stationDeclinationValue",
            "stationDeclinationEWT"))
    ));
  }

  private static void airway(StringBuilder result, XmlElement airway) {
    List<XmlElement> legs = airway.children("airwayLeg");
    header(result, text(airway, "identifier"), legs.size() + (legs.size() == 1 ? " fix" : " fixes"));
    for (XmlElement leg : legs) {
      line(result, leg(
          sequence(text(leg, "sequenceNumber"), 4),
          null,
          text(leg, "fixIdent").isEmpty() ? text(leg, "fixRef") : text(leg, "fixIdent")
      ));
    }
  }

  private static void procedure(StringBuilder result, XmlElement procedure, String port) {
    String type = switch (procedure.name()) {
      case "sid" -> "SID";
      case "star" -> "STAR";
      default -> "Approach";
    };
    List<XmlElement> sections = procedure.children().stream()
        .filter(section -> !section.children("procedureLeg").isEmpty())
        .toList();
    int legs = sections.stream().mapToInt(section -> section.children("procedureLeg").size()).sum();
    header(result, qualified(port, text(procedure, "identifier")),
        type + " (" + legs + (legs == 1 ? " leg)" : " legs)"));
    line(result, joined(
        " | ",
        labelled("Approach type", text(procedure, "approachRouteType")),
        text(procedure, "isRnav").equals("true") ? "RNAV" : ""
    ));

    for (XmlElement section : sections) {
      String intercept = text(section, "glideSlopeInterceptAltitude");
      line(result, joined(" ", sectionName(procedure.name(), section.name()), text(section, "identifier"))
          + (intercept.isEmpty() ? "" : " (glide slope intercept " + intercept + " ft)"));
      for (XmlElement leg : section.children("procedureLeg")) {
        line(result, leg(
            sequence(text(leg, "sequenceNumber"), 3),
            text(leg, "pathAndTermination"),
            text(leg, "fixIdent"),
            tag("turn", text(leg, "turnDirection").isEmpty() ? "" : text(leg, "turnDirection").substring(0, 1), ""),
            course(leg),
            tag("dist", number(text(leg, "distance")), "NM"),
            tag("time", minutes(text(leg, "holdTime")), "min"),
            constraint("alt", leg, "altitudeConstraint"),
            constraint("spd", leg, "speedLimit"),
            tag("vpa", number(text(leg, "verticalAngle")), "°"),
            tag("arc", number(text(leg, "arcRadius")), "NM"),
            tag("center", text(leg, "centerFix"), ""),
            navaid(leg)
        ));
      }
    }
  }

  /**
   * Names a procedure section from its element, e.g. sidRunwayTransition as "Runway transition".
   */
  private static String sectionName(String procedure, String section) {
    String name = section.startsWith(procedure) && section.length() > procedure.length()
        && Character.isUpperCase(section.charAt(procedure.length()))
        ? section.substring(procedure.length()) : section;
    String words = name.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase(Locale.ROOT);
    return Character.toUpperCase(words.charAt(0)) + words.substring(1);
  }

  private static String course(XmlElement leg) {
    String course = number(text(leg, "course/courseValue"));
    return course.isEmpty() ? "" : "crs " + course + "°" + (text(leg, "course/isTrue").equals("true") ? "T" : "");
  }

  /**
   * Formats altitude or speed constraints: {@code @} at, {@code +} at or above and {@code -} at or below, joined with
   * {@code /} when there are several, e.g. {@code alt +3000/-5000}. Altitudes flagged as flight levels print as FL.
   */
  private static String constraint(String tag, XmlElement leg, String field) {
    XmlElement constraint = leg.child(field).orElse(null);
    if (constraint == null) {
      return "";
    }
    List<String> values = new ArrayList<>();
    for (XmlElement bound : constraint.children()) {
      String prefix = switch (bound.name()) {
        case "at" -> "@";
        case "atOrAbove" -> "+";
        case "atOrBelow" -> "-";
        default -> "";
      };
      if (prefix.isEmpty()) {
        continue;
      }
      String value = bound.children().isEmpty() ? number(clean(bound.text())) : altitude(bound);
      if (!value.isEmpty()) {
        values.add(prefix + value);
      }
    }
    if (values.isEmpty()) {
      return "";
    }
    return tag + " " + String.join("/", values) + (tag.equals("spd") ? " kt" : "");
  }

  private static String altitude(XmlElement bound) {
    String altitude = number(text(bound, "altitude"));
    if (!altitude.isEmpty() && text(bound, "isFlightLevel").equals("true")) {
      try {
        return "FL" + String.format(Locale.ROOT, "%03d", Math.round(Double.parseDouble(altitude) / 100));
      } catch (NumberFormatException e) {
        return "FL" + altitude;
      }
    }
    return altitude;
  }

  private static String navaid(XmlElement leg) {
    String navaid = text(leg, "recNavaidIdent");
    if (navaid.isEmpty()) {
      return "";
    }
    String theta = number(text(leg, "theta"));
    String rho = number(text(leg, "rho"));
    return "nav " + navaid + (theta.isEmpty() ? "" : " " + theta + "°") + (rho.isEmpty() ? "" : "/" + rho + " NM");
  }

  /**
   * Converts an xs:duration such as {@code PT1M0.000S} to minutes, keeping an unparseable value visible.
   */
  private static String minutes(String duration) {
    if (duration.isEmpty()) {
      return "";
    }
    try {
      return number(Duration.parse(duration).toMillis() / 60000.0);
    } catch (DateTimeParseException e) {
      return duration;
    }
  }

  private static void generic(StringBuilder result, XmlElement record, String port) {
    String id = identifier(record);
    if (id.isEmpty()) {
      id = record.attributes().getOrDefault("referenceId", record.name());
    }
    header(result, qualified(port, clean(id)),
        clean(first(record, "name", "procedureName", "holdingPatternName", "runwayDescription")));

    List<String> fields = new ArrayList<>();
    Map<String, Integer> groups = new LinkedHashMap<>();
    for (XmlElement child : record.children()) {
      if (!child.children().isEmpty()) {
        groups.merge(child.name(), 1, Integer::sum);
      } else if (!HEADER_FIELDS.contains(child.name()) && !child.text().isBlank()) {
        fields.add(child.name() + "=" + abbreviate(child.text()));
      }
    }
    if (!record.text().isBlank()) {
      fields.add("value=" + abbreviate(record.text()));
    }
    if (!fields.isEmpty()) {
      line(result, truncated(fields, " | "));
    }
    if (!groups.isEmpty()) {
      line(result, "Children: " + truncated(
          groups.entrySet().stream()
              .map(entry -> entry.getKey() + "=" + entry.getValue())
              .toList(),
          ", "
      ));
    }
  }

  private static String coordinates(XmlElement record) {
    return record.child("location")
        .map(location -> coordinate(location, "latitude", "northSouth", "decimalDegreesLatitude", 90)
            + ", " + coordinate(location, "longitude", "eastWest", "decimalDegreesLongitude", 180))
        .orElse("-");
  }

  private static String coordinate(XmlElement location, String axis, String hemisphere, String decimal, int max) {
    XmlElement coordinate = location.child(axis).orElse(null);
    if (coordinate == null) {
      return "-";
    }

    try {
      double degrees;
      if (coordinate.value(decimal).isPresent()) {
        degrees = Double.parseDouble(coordinate.value(decimal).orElseThrow());
      } else {
        if (List.of("deg", "min", "sec", "hSec", hemisphere).stream()
            .anyMatch(field -> coordinate.value(field).isEmpty())) {
          return "-";
        }

        int deg = Integer.parseInt(coordinate.value("deg").orElseThrow());
        int min = Integer.parseInt(coordinate.value("min").orElseThrow());
        int sec = Integer.parseInt(coordinate.value("sec").orElseThrow());
        int hundredths = Integer.parseInt(coordinate.value("hSec").orElseThrow());
        if (deg < 0 || min < 0 || min > 59 || sec < 0 || sec > 59 || hundredths < 0 || hundredths > 99) {
          return "invalid";
        }

        degrees = deg + min / 60.0 + sec / 3600.0 + hundredths / 360000.0;
        String direction = coordinate.value(hemisphere).orElseThrow();
        String positive = axis.equals("latitude") ? "North" : "East";
        String negative = axis.equals("latitude") ? "South" : "West";
        if (direction.equalsIgnoreCase(negative)) {
          degrees = -degrees;
        } else if (!direction.equalsIgnoreCase(positive)) {
          return "invalid";
        }
      }

      if (!Double.isFinite(degrees) || Math.abs(degrees) > max) {
        return "invalid";
      }

      return String.format(Locale.ROOT, "%.6f", degrees);
    } catch (NumberFormatException e) {
      return "invalid";
    }
  }

  private static String runwayIdentifier(XmlElement runway) {
    if (runway.value("identifier").isPresent()) {
      return runway.value("identifier").orElseThrow();
    }

    String number = runway.value("runwayIdentifier/runwayNumber").orElse("");
    if (number.isEmpty()) {
      return "";
    }

    try {
      number = String.format(Locale.ROOT, "%02d", Integer.parseInt(number));
    } catch (NumberFormatException ignored) {
      // Keep the source value visible when inspecting an incomplete or invalid record.
    }

    String side = runway.value("runwayIdentifier/runwayLeftRightCenterType").orElse("");
    side = switch (side) {
      case "Left" -> "L";
      case "Right" -> "R";
      case "Center" -> "C";
      default -> side;
    };

    String suffix = runway.value("runwayIdentifier/runwaySuffix").orElse("");
    suffix = switch (suffix) {
      case "WaterSealaneOrWaterway" -> "W";
      case "GliderRunway" -> "G";
      case "UltralightRunway" -> "U";
      default -> suffix;
    };

    return "RW" + number + side + suffix;
  }

  /**
   * Formats the first frequency present; MHz values use two decimals, as in the text VHF view.
   */
  private static String frequency(XmlElement record) {
    for (String field : List.of("ndbFrequency", "vorFrequency", "dmeFrequency", "frequency")) {
      String value = text(record, field + "/frequencyValue");
      if (!value.isEmpty()) {
        String unit = text(record, field + "/freqUnitOfMeasure");
        if (unit.equals("megaHertz")) {
          try {
            return String.format(Locale.ROOT, "%.2f MHz", Double.parseDouble(value));
          } catch (NumberFormatException e) {
            return value + " MHz";
          }
        }
        return number(value) + " " + (unit.equals("kiloHertz") ? "kHz" : unit);
      }
    }
    return "";
  }

  private static String variation(XmlElement record, String field, String value, String direction) {
    String degrees = text(record, field + "/" + value);
    if (degrees.isEmpty()) {
      return "";
    }
    String hemisphere = switch (text(record, field + "/" + direction)) {
      case "West" -> "W";
      case "East" -> "E";
      case "True" -> "true";
      default -> "";
    };
    try {
      return Layout.variation(Double.parseDouble(degrees), hemisphere);
    } catch (NumberFormatException e) {
      return degrees;
    }
  }

  private static void flag(XmlElement record, String path, String label, List<String> labels) {
    if (text(record, path).equals("true")) {
      labels.add(label);
    }
  }

  /**
   * The cleaned text of a descendant, or an empty string.
   */
  private static String text(XmlElement element, String path) {
    return element.value(path).map(RecordSummary::clean).orElse("");
  }

  private static String qualified(String port, String identifier) {
    return port.isEmpty() ? identifier : clean(port) + " " + identifier;
  }

  private static String first(XmlElement element, String... paths) {
    for (String path : paths) {
      if (element.value(path).isPresent()) {
        return element.value(path).orElseThrow();
      }
    }
    return "";
  }

  private static String unit(XmlElement element, String path, String unit) {
    String value = text(element, path);
    return value.isEmpty() ? "-" : value + " " + unit;
  }

  private static String labelled(String label, String value) {
    return value.isEmpty() ? "" : label + ": " + value;
  }

  private static String labelled(String label, String value, String unit) {
    return value.isEmpty() ? "" : label + ": " + value + (unit.equals("°") ? unit : " " + unit);
  }

  private static String truncated(List<String> values, String separator) {
    if (values.isEmpty()) {
      return "-";
    }

    return values.stream()
        .limit(12)
        .map(RecordSummary::clean)
        .collect(Collectors.joining(separator))
        + (values.size() > 12 ? " … (+" + (values.size() - 12) + ")" : "");
  }

  private static String abbreviate(String value) {
    String cleaned = clean(value);
    return cleaned.length() <= 120 ? cleaned : cleaned.substring(0, 117) + "…";
  }

  static String clean(String value) {
    return value == null ? "" : value.replaceAll("[\\p{Cntrl}\\p{Zl}\\p{Zp}]", " ").replaceAll("\\s+", " ").strip();
  }
}
