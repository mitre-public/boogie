package org.mitre.boogie.cli;

import static org.mitre.boogie.cli.ArincTextReader.raw;
import static org.mitre.boogie.cli.Layout.header;
import static org.mitre.boogie.cli.Layout.joined;
import static org.mitre.boogie.cli.Layout.leg;
import static org.mitre.boogie.cli.Layout.line;
import static org.mitre.boogie.cli.Layout.list;
import static org.mitre.boogie.cli.Layout.missing;
import static org.mitre.boogie.cli.Layout.number;
import static org.mitre.boogie.cli.Layout.pad;
import static org.mitre.boogie.cli.Layout.sequence;
import static org.mitre.boogie.cli.Layout.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mitre.boogie.cli.ArincTextReader.TextRecord;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.RecordField;

/**
 * Formats compact summaries of fixed-width ARINC 424 records.
 */
final class ArincTextSummary {

  private static final int WIDTH = 100;

  private static final Set<String> BOILERPLATE = Set.of(
      "recordType",
      "customerAreaCode",
      "sectionCode",
      "subSectionCode",
      "enrouteSubSectionCode",
      "terminalSubSectionCode",
      "continuationRecordNumber",
      "fileRecordNumber",
      "lastUpdateCycle",
      "lastUpdatedCycle",
      "cycle"
  );

  private ArincTextSummary() {
  }

  static String format(TextRecord display) {
    ArincRecord record = display.head();
    StringBuilder result = new StringBuilder();

    switch (display.selection().label()) {
      case "Airport" -> airport(result, display);
      case "Runway" -> runway(result, record);
      case "Waypoint" -> waypoint(result, record);
      case "NdbNavaid" -> ndb(result, record);
      case "VhfNavaid" -> vhf(result, record);
      case "Heliport" -> heliport(result, record);
      case "HoldingPattern" -> holding(result, record);
      case "AirwayLeg" -> airway(result, display.members());
      case "ProcedureLeg" -> procedure(result, display.members());
      default -> generic(result, display);
    }

    return result.toString();
  }

  private static void airport(StringBuilder result, TextRecord display) {
    ArincRecord airport = display.head();
    header(result, text(airport, "airportIdentifier"), text(airport, "airportFullName"));
    line(result, "Coordinates: " + coordinates(airport, "latitude", "longitude")
        + " | Elevation: " + unit(airport, "airportElevation", "ft"));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(airport, "airportIcaoRegion")),
        labelled("IATA", text(airport, "iataDesignator")),
        labelled("Magnetic variation", variation(airport, "magneticVariation")),
        labelled("Longest runway", text(airport, "longestRunway"), "ft"),
        labelled("Transition altitude", text(airport, "transitionAltitude"), "ft")
    ));
    line(result, "Runways: " + list(display.members().stream()
        .map(runway -> text(runway, "runwayIdentifier"))
        .toList()));

    Map<Character, Set<String>> procedures = display.procedures();
    procedures(result, "SIDs", procedures.get('D'));
    procedures(result, "STARs", procedures.get('E'));
    procedures(result, "Approaches", procedures.get('F'));
  }

  private static void procedures(StringBuilder result, String label, Set<String> names) {
    if (names != null && !names.isEmpty()) {
      line(result, label + " (" + names.size() + "): " + String.join(", ", names));
    }
  }

  private static void runway(StringBuilder result, ArincRecord runway) {
    header(
        result,
        text(runway, "airportIdentifier") + " " + text(runway, "runwayIdentifier"),
        text(runway, "runwayDescription")
    );
    line(result, "Coordinates: " + coordinates(runway, "latitude", "longitude")
        + " | Threshold elevation: " + unit(runway, "landingThresholdElevation", "ft"));
    line(result, joined(
        " | ",
        "Length: " + unit(runway, "runwayLength", "ft"),
        "Width: " + unit(runway, "runwayWidth", "ft"),
        labelled("Magnetic bearing", text(runway, "runwayMagneticBearing"), "°"),
        labelled("Displaced threshold", text(runway, "thresholdDisplacementDistance"), "ft"),
        labelled("TCH", text(runway, "thresholdCrossingHeight"), "ft")
    ));

    String ils = text(runway, "ilsMlsGlsIdentifier");
    if (!ils.isEmpty()) {
      String category = text(runway, "ilsMlsGlsCategory");
      line(result, "Landing system: " + ils + (category.isEmpty() ? "" : " (category " + category + ")"));
    }
  }

  private static void waypoint(StringBuilder result, ArincRecord waypoint) {
    header(result, text(waypoint, "waypointIdentifier"), text(waypoint, "waypointNameDescription"));
    line(result, "Coordinates: " + coordinates(waypoint, "latitude", "longitude"));

    String area = text(waypoint, "airportIdentifier");
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(waypoint, "waypointIcaoRegion")),
        "Area: " + (area.isEmpty() || area.equals("ENRT") ? "enroute" : "terminal " + area),
        labelled("Type", text(waypoint, "waypointType")),
        labelled("Usage", text(waypoint, "waypointUsage")),
        labelled("Magnetic variation", variation(waypoint, "magneticVariation"))
    ));
  }

  private static void ndb(StringBuilder result, ArincRecord ndb) {
    header(result, text(ndb, "ndbIdentifier"), text(ndb, "ndbNavaidName"));
    line(result, joined(
        " | ",
        "Coordinates: " + coordinates(ndb, "latitude", "longitude"),
        "Frequency: " + unit(ndb, "ndbFrequency", "kHz"),
        labelled("Class", text(ndb, "navaidClass"))
    ));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(ndb, "ndbIcaoRegion")),
        labelled("Airport", text(ndb, "airportIdentifier")),
        labelled("Magnetic variation", variation(ndb, "magneticVariation"))
    ));
  }

  private static void vhf(StringBuilder result, ArincRecord vhf) {
    header(result, text(vhf, "vhfIdentifier"), text(vhf, "vhfNavaidName"));

    // The V18 field spec stores VHF frequencies in hundreds of kHz; e.g. 1127.0 is 112.70 MHz.
    String frequency = value(vhf, "vhfFrequency")
        .filter(Double.class::isInstance)
        .map(Double.class::cast)
        .map(mhz -> String.format(Locale.ROOT, "%.2f MHz", mhz / 10))
        .orElse("-");
    line(result, joined(
        " | ",
        "Coordinates: " + coordinates(vhf, "latitude", "longitude"),
        "Frequency: " + frequency,
        labelled("Class", text(vhf, "navaidClass"))
    ));

    String dme = text(vhf, "dmeIdentifier");
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(vhf, "vhfIcaoRegion")),
        labelled("Airport", text(vhf, "airportIdentifier")),
        dme.isEmpty() ? "" : "DME: " + dme + " at " + coordinates(vhf, "dmeLatitude", "dmeLongitude")
            + (text(vhf, "dmeElevation").isEmpty() ? "" : ", elevation " + text(vhf, "dmeElevation") + " ft"),
        labelled("Station declination", variation(vhf, "stationDeclination"))
    ));
  }

  private static void heliport(StringBuilder result, ArincRecord heliport) {
    header(result, text(heliport, "heliportIdentifier"), text(heliport, "heliportFullName"));
    line(result, "Coordinates: " + coordinates(heliport, "latitude", "longitude")
        + " | Elevation: " + unit(heliport, "heliportElevation", "ft"));
    line(result, joined(
        " | ",
        "ICAO region: " + missing(text(heliport, "heliportIcaoRegion")),
        labelled("IATA", text(heliport, "iataDesignator")),
        labelled("Helipad", text(heliport, "helipadIdentifier")),
        labelled("Magnetic variation", variation(heliport, "magneticVariation"))
    ));
  }

  private static void holding(StringBuilder result, ArincRecord hold) {
    header(result, text(hold, "fixIdentifier"), text(hold, "holdingName"));
    String area = text(hold, "regionCode");
    line(result, joined(
        " | ",
        "Fix region: " + missing(text(hold, "fixIcaoRegion")),
        "Area: " + (area.isEmpty() || area.equals("ENRT") ? "enroute" : "terminal " + area),
        labelled("Duplicate", text(hold, "duplicateIdentifier"))
    ));
    line(result, joined(
        " | ",
        "Inbound course: " + unit(hold, "inboundHoldingCourse", "°"),
        labelled("Turn", text(hold, "turnDirection")),
        labelled("Leg length", text(hold, "legLength"), "NM"),
        // Leg time is recorded in tenths of a minute.
        labelled("Leg time", value(hold, "legTime")
            .filter(Integer.class::isInstance)
            .map(tenths -> number((Integer) tenths / 10.0))
            .orElse(""), "min"),
        labelled("Minimum altitude", text(hold, "minimumAltitude"), "ft"),
        labelled("Maximum altitude", text(hold, "maxAltitude"), "ft"),
        labelled("Speed", text(hold, "holdingSpeed"), "kt")
    ));
  }

  private static void airway(StringBuilder result, List<ArincRecord> legs) {
    ArincRecord first = legs.get(0);
    header(
        result,
        text(first, "routeIdentifier"),
        legs.size() + (legs.size() == 1 ? " fix" : " fixes")
            + (text(first, "customerAreaCode").isEmpty() ? "" : " in " + text(first, "customerAreaCode"))
    );
    line(result, joined(
        " | ",
        labelled("Route type", text(first, "routeType")),
        labelled("Level", text(first, "level"))
    ));

    for (ArincRecord leg : legs) {
      line(result, leg(
          sequence(raw(leg, "sequenceNumber"), 4),
          null,
          pad(text(leg, "fixIdentifier"), 5) + " " + pad(text(leg, "fixIcaoRegion"), 2),
          tag("crs", text(leg, "outboundMagneticCourse"), "°"),
          distance(leg),
          tag("min", text(leg, "minAltitude1"), "ft"),
          tag("max", text(leg, "maxAltitude"), "ft"),
          tag("dir", text(leg, "directionRestriction"), "")
      ));
    }
  }

  private static void procedure(StringBuilder result, List<ArincRecord> legs) {
    ArincRecord first = legs.get(0);
    String type = switch (raw(first, "subSectionCode")) {
      case "D" -> "SID";
      case "E" -> "STAR";
      case "F" -> "Approach";
      default -> "Procedure";
    };
    header(
        result,
        text(first, "airportIdentifier") + " " + text(first, "sidStarIdentifier"),
        type + " (" + legs.size() + (legs.size() == 1 ? " leg)" : " legs)")
    );

    String transition = null;
    for (ArincRecord leg : legs) {
      String current = text(leg, "routeType") + "|" + text(leg, "transitionIdentifier");
      if (!current.equals(transition)) {
        transition = current;
        String name = text(leg, "transitionIdentifier");
        line(result, (name.isEmpty() ? "Common route" : "Transition " + name)
            + " (route type " + missing(text(leg, "routeType")) + ")");
      }
      line(result, leg(
          sequence(raw(leg, "sequenceNumber"), 3),
          text(leg, "pathTerm"),
          text(leg, "fixIdentifier"),
          tag("turn", text(leg, "turnDirection"), ""),
          tag("crs", text(leg, "outboundMagneticCourse"), "°"),
          distance(leg),
          altitude(leg),
          speed(leg),
          tag("vpa", text(leg, "verticalAngle"), "°"),
          tag("arc", text(leg, "arcRadius"), "NM"),
          tag("center", text(leg, "centerFixIdentifier"), ""),
          navaid(leg)
      ));
    }
  }

  private static String altitude(ArincRecord leg) {
    String first = text(leg, "minAltitude1");
    if (first.isEmpty()) {
      return "";
    }
    String second = text(leg, "minAltitude2");
    return "alt " + text(leg, "altitudeDescription") + first + (second.isEmpty() ? "" : "/" + second);
  }

  private static String speed(ArincRecord leg) {
    String speed = text(leg, "speedLimit");
    return speed.isEmpty() ? "" : "spd " + text(leg, "speedLimitDescription") + speed + " kt";
  }

  private static String navaid(ArincRecord leg) {
    String navaid = text(leg, "recommendedNavaidIdentifier");
    if (navaid.isEmpty()) {
      return "";
    }
    String theta = text(leg, "theta");
    String rho = text(leg, "rho");
    return "nav " + navaid + (theta.isEmpty() ? "" : " " + theta + "°") + (rho.isEmpty() ? "" : "/" + rho + " NM");
  }

  /**
   * Formats a route or hold distance stored in tenths of a nautical mile, or tenths of a minute when prefixed with {@code T}.
   */
  private static String distance(ArincRecord leg) {
    String value = raw(leg, "routeHoldDistanceTime");
    if (value.isEmpty()) {
      return "";
    }
    boolean time = value.startsWith("T");
    String digits = time ? value.substring(1) : value;
    if (!digits.chars().allMatch(Character::isDigit) || digits.isEmpty()) {
      return "dist " + value;
    }
    return (time ? "time " : "dist ") + number(Integer.parseInt(digits) / 10.0) + (time ? " min" : " NM");
  }

  private static void generic(StringBuilder result, TextRecord display) {
    ArincRecord record = display.head();
    List<String> identifiers = ArincTextReader.identifiers(display.selection(), record);
    String name = display.selection().spec().recordFields().stream()
        .map(RecordField::fieldName)
        .filter(field -> field.endsWith("Name") || field.endsWith("NameDescription"))
        .map(field -> text(record, field))
        .filter(value -> !value.isEmpty())
        .findFirst()
        .orElse("");
    String heading = String.join(" ", identifiers.stream().filter(value -> !value.contains("/")).distinct().toList());
    header(result, heading.isEmpty() ? display.selection().label() : heading, name);

    List<String> fields = new ArrayList<>();
    for (RecordField<?> field : display.selection().spec().recordFields()) {
      String fieldName = field.fieldName();
      String lower = fieldName.toLowerCase(Locale.ROOT);
      if (BOILERPLATE.contains(fieldName) || lower.startsWith("blank") || lower.startsWith("reserved")
          || lower.startsWith("spacing")) {
        continue;
      }

      String value = text(record, fieldName);
      String rawValue = raw(record, fieldName);
      if (value.isEmpty() && !rawValue.isEmpty()) {
        value = RecordSummary.clean(rawValue) + " (unparsed)";
      }
      if (!value.isEmpty()) {
        fields.add(fieldName + "=" + value);
      }
    }

    StringBuilder current = new StringBuilder();
    for (String field : fields) {
      if (current.length() > 0 && current.length() + field.length() + 3 > WIDTH) {
        line(result, current.toString());
        current.setLength(0);
      }
      current.append(current.length() == 0 ? "" : " | ").append(field);
    }
    if (current.length() > 0) {
      line(result, current.toString());
    }
  }

  private static Optional<Object> value(ArincRecord record, String field) {
    try {
      return record.optionalField(field);
    } catch (RuntimeException e) {
      // Blank and reserved field specs cannot be parsed; a viewer treats them, and any other undecodable field, as absent.
      return Optional.empty();
    }
  }

  /**
   * Returns the parsed field for display, or an empty string when the field is absent, blank or cannot be parsed.
   */
  private static String text(ArincRecord record, String field) {
    return value(record, field)
        .map(value -> value instanceof Double number ? number(number) : value.toString())
        .map(RecordSummary::clean)
        .orElse("");
  }

  private static String coordinates(ArincRecord record, String latitude, String longitude) {
    Optional<Object> lat = value(record, latitude);
    Optional<Object> lon = value(record, longitude);
    if (lat.isEmpty() || lon.isEmpty() || !(lat.get() instanceof Double y) || !(lon.get() instanceof Double x)) {
      return "-";
    }
    return String.format(Locale.ROOT, "%.6f, %.6f", y, x);
  }

  private static String variation(ArincRecord record, String field) {
    return value(record, field)
        .filter(Double.class::isInstance)
        .map(Double.class::cast)
        .map(degrees -> Layout.variation(degrees, degrees < 0 ? "W" : "E"))
        .orElse("");
  }

  private static String unit(ArincRecord record, String field, String unit) {
    String value = text(record, field);
    return value.isEmpty() ? "-" : value + (unit.equals("°") ? unit : " " + unit);
  }

  private static String labelled(String label, String value) {
    return value.isEmpty() ? "" : label + ": " + value;
  }

  private static String labelled(String label, String value, String unit) {
    if (value.isEmpty()) {
      return "";
    }
    return label + ": " + value + (unit.equals("°") ? unit : " " + unit);
  }
}
