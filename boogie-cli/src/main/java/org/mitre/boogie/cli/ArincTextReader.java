package org.mitre.boogie.cli;

import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;

/**
 * Streams fixed-width ARINC 424 lines, parsing only the selected record class with the chosen version's record specs.
 * <p>
 * Airway and procedure legs are grouped while they are contiguous, which is how ARINC 424 files are sorted.
 */
final class ArincTextReader {

  /**
   * Defines how records of a class are grouped for display.
   */
  enum Kind {
    SINGLE,
    AIRPORT,
    AIRWAY,
    PROCEDURE
  }

  /**
   * A requested record class resolved against the {@link RecordSpec}s for one {@link ArincVersion}.
   */
  record Selection(String label, Kind kind, RecordSpec spec, String procedureSubsections) {
  }

  /**
   * Records grouped for display: a single record, an airport with its runways and procedure names by subsection, or the legs
   * of one airway or procedure in file order.
   */
  record TextRecord(Selection selection, ArincRecord head, List<ArincRecord> members, Map<Character, Set<String>> procedures) {
  }

  /**
   * Indicates the requested record class is not defined by the chosen {@link ArincVersion}. Reported as a usage error.
   */
  static final class UnknownClassException extends IllegalArgumentException {

    UnknownClassException(String message) {
      super(message);
    }
  }

  /**
   * Record classes with tailored text views, in the order shown by {@code --list-records}.
   */
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

  private static final Map<String, String> ALIASES = Map.ofEntries(
      entry("ndb", "NdbNavaid"),
      entry("vor", "VhfNavaid"),
      entry("airway", "AirwayLeg"),
      entry("localizer", "LocalizerGlideSlope"),
      entry("ils", "LocalizerGlideSlope"),
      entry("gls", "GnssLandingSystem"),
      entry("header", "Header01"),
      entry("sid", "ProcedureLeg"),
      entry("star", "ProcedureLeg"),
      entry("approach", "ProcedureLeg"),
      entry("procedure", "ProcedureLeg")
  );

  private static final List<String> IDENTIFIER_EXCLUSIONS = List.of(
      "recommended",
      "supporting",
      "center",
      "secondary",
      "dme",
      "ilsmlsgls",
      "adjacent"
  );

  private ArincTextReader() {
  }

  /**
   * Resolves a record class name against the chosen {@link ArincVersion}.
   *
   * @throws UnknownClassException if the class is unavailable, with a message listing the available classes
   */
  static Selection select(ArincVersion version, String requested) {
    String key = requested.toLowerCase(Locale.ROOT);
    String wanted = ALIASES.getOrDefault(key, requested);
    RecordSpec spec = findSpec(version, wanted)
        .orElseThrow(() -> new UnknownClassException(
            "ARINC " + version + " has no " + RecordSummary.clean(requested) + " records; choose from "
                + String.join(", ", classes(version)) + "."
        ));

    String name = specName(spec);
    Kind kind = switch (name) {
      case "Airport" -> Kind.AIRPORT;
      case "AirwayLeg" -> Kind.AIRWAY;
      case "ProcedureLeg" -> Kind.PROCEDURE;
      default -> Kind.SINGLE;
    };
    String subsections = switch (key) {
      case "sid" -> "D";
      case "star" -> "E";
      case "approach" -> "F";
      default -> "DEF";
    };
    return new Selection(name, kind, spec, subsections);
  }

  /**
   * Returns the classes accepted for a version, with tailored views first followed by the remaining spec names.
   */
  static List<String> classes(ArincVersion version) {
    List<String> result = new ArrayList<>();
    for (String profile : PROFILES) {
      if (findSpec(version, ALIASES.getOrDefault(profile.toLowerCase(Locale.ROOT), profile)).isPresent()) {
        result.add(profile);
      }
    }

    version.specs().stream()
        .map(ArincTextReader::displayName)
        .filter(name -> result.stream().noneMatch(name::equalsIgnoreCase))
        .filter(name -> !name.equals("AirwayLeg") && !name.equals("ProcedureLeg") && !name.equals("NdbNavaid"))
        .forEach(result::add);
    return result;
  }

  /**
   * Calls the consumer for each selected group of records in file order. Returning {@code false} from the consumer stops
   * reading. The caller owns the source stream.
   *
   * @return the number of lines of the selected class that were too short or too long to parse
   */
  static int read(InputStream source, ArincVersion version, Selection selection, String identifier,
      Predicate<TextRecord> consumer) throws IOException {
    requireNonNull(consumer, "consumer");

    BufferedReader reader = new BufferedReader(new InputStreamReader(source, StandardCharsets.ISO_8859_1));
    ArincRecordParser parser = ArincRecordParser.standard(selection.spec());
    return switch (selection.kind()) {
      case SINGLE -> readSingle(reader, parser, selection, identifier, consumer);
      case AIRWAY, PROCEDURE -> readGroups(reader, parser, selection, identifier, consumer);
      case AIRPORT -> readAirports(reader, parser, version, selection, identifier, consumer);
    };
  }

  private static int readSingle(BufferedReader reader, ArincRecordParser parser, Selection selection, String identifier,
      Predicate<TextRecord> consumer) throws IOException {
    int malformed = 0;
    String line;
    while ((line = reader.readLine()) != null) {
      if (!selection.spec().matchesRecord(line)) {
        continue;
      }

      Optional<ArincRecord> parsed = parse(parser, line);
      if (parsed.isEmpty()) {
        malformed++;
      } else if (matches(selection, parsed.get(), identifier)
          && !consumer.test(new TextRecord(selection, parsed.get(), List.of(), Map.of()))) {
        break;
      }
    }
    return malformed;
  }

  private static int readGroups(BufferedReader reader, ArincRecordParser parser, Selection selection, String identifier,
      Predicate<TextRecord> consumer) throws IOException {
    int malformed = 0;
    List<ArincRecord> group = new ArrayList<>();
    String groupKey = null;
    String line;
    while ((line = reader.readLine()) != null) {
      if (!selection.spec().matchesRecord(line)
          || (selection.kind() == Kind.PROCEDURE && selection.procedureSubsections().indexOf(line.charAt(12)) < 0)) {
        continue;
      }

      Optional<ArincRecord> parsed = parse(parser, line);
      if (parsed.isEmpty()) {
        malformed++;
        continue;
      }

      String key = groupKey(selection, parsed.get());
      if (!key.equals(groupKey)) {
        if (!emitGroup(selection, group, identifier, consumer)) {
          return malformed;
        }
        group = new ArrayList<>();
        groupKey = key;
      }
      group.add(parsed.get());
    }
    emitGroup(selection, group, identifier, consumer);
    return malformed;
  }

  private static boolean emitGroup(Selection selection, List<ArincRecord> group, String identifier,
      Predicate<TextRecord> consumer) {
    if (group.isEmpty() || !matches(selection, group.get(0), identifier)) {
      return true;
    }
    return consumer.test(new TextRecord(selection, group.get(0), List.copyOf(group), Map.of()));
  }

  /**
   * Buffers airports until the end of input because their runway and procedure records follow them.
   *
   * <p>Only airports matching the identifier are retained, along with runways and procedure names for every airport.
   */
  private static int readAirports(BufferedReader reader, ArincRecordParser parser, ArincVersion version, Selection selection,
      String identifier, Predicate<TextRecord> consumer) throws IOException {
    RecordSpec runwaySpec = findSpec(version, "Runway").orElse(null);
    RecordSpec procedureSpec = findSpec(version, "ProcedureLeg").orElse(null);
    ArincRecordParser runwayParser = runwaySpec == null ? null : ArincRecordParser.standard(runwaySpec);
    ArincRecordParser procedureParser = procedureSpec == null ? null : ArincRecordParser.standard(procedureSpec);

    List<ArincRecord> airports = new ArrayList<>();
    Map<String, List<ArincRecord>> runways = new LinkedHashMap<>();
    Map<String, Map<Character, Set<String>>> procedures = new LinkedHashMap<>();
    int malformed = 0;
    String line;
    while ((line = reader.readLine()) != null) {
      if (selection.spec().matchesRecord(line)) {
        Optional<ArincRecord> parsed = parse(parser, line);
        if (parsed.isEmpty()) {
          malformed++;
        } else if (matches(selection, parsed.get(), identifier)) {
          airports.add(parsed.get());
        }
      } else if (runwaySpec != null && runwaySpec.matchesRecord(line)) {
        parse(runwayParser, line)
            .ifPresent(runway -> runways
                .computeIfAbsent(airportKey(runway), k -> new ArrayList<>())
                .add(runway));
      } else if (procedureSpec != null && line.charAt(4) == 'P' && procedureSpec.matchesRecord(line)) {
        parse(procedureParser, line)
            .ifPresent(leg -> procedures
                .computeIfAbsent(airportKey(leg), k -> new TreeMap<>())
                .computeIfAbsent(raw(leg, "subSectionCode").charAt(0), k -> new TreeSet<>())
                .add(raw(leg, "sidStarIdentifier")));
      }
    }

    for (ArincRecord airport : airports) {
      String key = airportKey(airport);
      if (!consumer.test(new TextRecord(selection, airport, runways.getOrDefault(key, List.of()),
          procedures.getOrDefault(key, Map.of())))) {
        break;
      }
    }
    return malformed;
  }

  private static Optional<ArincRecord> parse(ArincRecordParser parser, String line) {
    try {
      return parser.parse(line);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private static boolean matches(Selection selection, ArincRecord record, String identifier) {
    return identifier == null || identifiers(selection, record).stream().anyMatch(identifier::equalsIgnoreCase);
  }

  /**
   * Returns the values matched by {@code -i}: the record's own identifier, its airport where it has one, and
   * {@code AIRPORT/IDENTIFIER}.
   *
   * <p>Generic views use the first value as their heading.
   */
  static List<String> identifiers(Selection selection, ArincRecord record) {
    List<String> fields = switch (selection.kind()) {
      case AIRPORT -> List.of("airportIdentifier", "iataDesignator");
      case AIRWAY -> List.of("routeIdentifier");
      case PROCEDURE -> List.of("sidStarIdentifier", "airportIdentifier");
      case SINGLE -> selection.spec().recordFields().stream()
          .map(RecordField::fieldName)
          .filter(ArincTextReader::isIdentifierField)
          .toList();
    };
    List<String> values = new ArrayList<>(fields.stream()
        .map(field -> raw(record, field))
        .filter(value -> !value.isEmpty())
        .toList());

    // Qualified forms such as KBOS/I04R or KBOS/RW04R select one record among airports that share an identifier.
    String airport = raw(record, "airportIdentifier");
    if (!airport.isEmpty() && selection.kind() != Kind.AIRPORT) {
      List.copyOf(values).stream()
          .filter(value -> !value.equals(airport))
          .forEach(value -> values.add(airport + "/" + value));
    }
    return values;
  }

  private static boolean isIdentifierField(String name) {
    String lower = name.toLowerCase(Locale.ROOT);
    return (lower.endsWith("identifier") || lower.endsWith("designation") || lower.equals("airspacecenter"))
        && IDENTIFIER_EXCLUSIONS.stream().noneMatch(lower::startsWith);
  }

  private static String groupKey(Selection selection, ArincRecord record) {
    if (selection.kind() == Kind.AIRWAY) {
      return raw(record, "customerAreaCode") + "|" + raw(record, "routeIdentifier");
    }
    return airportKey(record) + "|" + raw(record, "subSectionCode") + "|" + raw(record, "sidStarIdentifier");
  }

  private static String airportKey(ArincRecord record) {
    return raw(record, "airportIdentifier") + "|" + raw(record, "airportIcaoRegion");
  }

  /**
   * Returns the stripped fixed-width text of a field, or an empty string if the spec has no such field.
   */
  static String raw(ArincRecord record, String field) {
    return record.specForField(field).isPresent() ? record.rawField(field).strip() : "";
  }

  private static Optional<RecordSpec> findSpec(ArincVersion version, String name) {
    return version.specs().stream()
        .filter(spec -> specName(spec).equalsIgnoreCase(name) || displayName(spec).equalsIgnoreCase(name))
        .findFirst();
  }

  private static String specName(RecordSpec spec) {
    String simple = spec.getClass().getSimpleName();
    return simple.endsWith("Spec") ? simple.substring(0, simple.length() - 4) : simple;
  }

  /**
   * Returns the spec name without a trailing {@code Leg}, e.g. {@code FirUir} for {@code FirUirLegSpec}.
   */
  private static String displayName(RecordSpec spec) {
    String name = specName(spec);
    return name.endsWith("Leg") && !name.equals("AirwayLeg") && !name.equals("ProcedureLeg")
        ? name.substring(0, name.length() - 3) : name;
  }
}
