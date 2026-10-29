package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test utility which carves a single procedure (plus everything it references) out of a CIFP file and writes it in the layout of
 * the FAA "specials" .ari files, so we can exercise specials handling with public data. See the {@code specials/} and
 * {@code dups/} test resources for its output.
 * <p>
 * The specials layout differs from a standard 424 file in a handful of ways, all of which are replicated here:
 * <ul>
 *   <li>a custom {@code HDR } line (city/state/airport/chart name/amendment/date/version) in place of {@code HDR01}</li>
 *   <li>columns 124-132 ({@code fileRecordNumber} and {@code lastUpdateCycle}) are blank on every record, so every record fails
 *   the standard validators</li>
 *   <li>every referenced fix (enroute or terminal) is emitted as an {@code EA}/{@code ENRT} waypoint, with leg references
 *   recoded {@code PC -> EA}</li>
 *   <li>waypoint type {@code W?F} where {@code ?} is the fix's role on the procedure, usage {@code R} (+{@code B} for non-CNF),
 *   and names formatted as {@code CNF(XXXXX)}</li>
 *   <li>datum {@code WGE}</li>
 *   <li>record order: navaids, waypoints, airport, procedure legs, runway, MSA</li>
 * </ul>
 * The one notable thing not replicated: specials model the runway threshold as a waypoint (e.g. {@code GUC24}), the CIFP data
 * keeps the {@code RWxx} runway fix.
 */
public final class SpecialsFileGenerator {

  private static final ArincRecordParser PARSER = ArincVersion.V18.parser();

  private static final DateTimeFormatter CIFP_HEADER_DATE = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .appendPattern("dd-MMM-yyyy")
      .toFormatter(Locale.US);

  private static final Pattern RUNWAY_FROM_IDENT = Pattern.compile("^[A-Z](\\d{2}[LRC]?)");

  private SpecialsFileGenerator() {
  }

  /**
   * Usage: {@code SpecialsFileGenerator <cifp file|embedded> <airport> <procedure> <output file> [key=value ...]}
   * <p>
   * Optional keys: {@code ident} (rename the procedure), {@code city}, {@code state}, {@code type}, {@code chart},
   * {@code amendment}, {@code date} (yyyyMMdd) and {@code version} - all of which only land in the header except {@code ident}.
   */
  public static void main(String[] args) throws IOException {
    Map<String, String> options = new HashMap<>();
    for (int i = 4; i < args.length; i++) {
      String[] kv = args[i].split("=", 2);
      options.put(kv[0], kv[1]);
    }

    List<String> cifp = "embedded".equals(args[0]) ? embeddedCifp() : Files.readAllLines(Path.of(args[0]));
    Files.write(Path.of(args[3]), generate(cifp, args[1], args[2], options));
  }

  public static List<String> embeddedCifp() throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(EmbeddedCifpFile.getInputStream()))) {
      return reader.lines().toList();
    }
  }

  /**
   * Returns the lines of a specials-style file for the given airport/procedure from the provided CIFP lines.
   */
  public static List<String> generate(List<String> cifp, String airport, String procedure, Map<String, String> options) {
    requireNonNull(airport);
    requireNonNull(procedure);

    String airportLine = cifp.stream()
        .filter(l -> l.startsWith("SUSAP " + airport) && l.charAt(12) == 'A' && l.charAt(21) == '0')
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No airport " + airport));
    String airportPrefix = airportLine.substring(0, 12);

    String procedureKey = String.format("%-6s", procedure);
    List<String> legs = cifp.stream()
        .filter(l -> l.startsWith(airportPrefix) && "DEF".indexOf(l.charAt(12)) >= 0 && l.substring(13, 19).equals(procedureKey))
        .toList();
    if (legs.isEmpty()) {
      throw new IllegalArgumentException("No procedure " + procedure + " at " + airport);
    }
    char subSection = legs.get(0).charAt(12);

    // fix key (ident|region|section+subsection) -> waypoint type role character
    Map<String, Character> fixRoles = new LinkedHashMap<>();
    Set<String> runways = new TreeSet<>();
    for (String leg : legs) {
      // continuation records aren't parsed but also don't introduce new references
      PARSER.parse(leg).ifPresent(arincRecord -> {
        String description = arincRecord.<String>optionalField("waypointDescription").orElse("    ");
        addReference(arincRecord, "fix", role(description), fixRoles, runways);
        addReference(arincRecord, "recommendedNavaid", ' ', fixRoles, runways);
        addReference(arincRecord, "centerFix", ' ', fixRoles, runways);
      });
    }
    if (subSection == 'F') {
      Matcher matcher = RUNWAY_FROM_IDENT.matcher(procedure);
      if (matcher.find()) {
        runways.add("RW" + matcher.group(1));
      }
    }

    List<String> navaids = new ArrayList<>();
    List<String> waypoints = new ArrayList<>();
    Set<String> resolved = new HashSet<>();
    for (String line : cifp) {
      if (line.length() < 132 || line.charAt(21) != '0') {
        continue;
      }
      boolean terminal = line.startsWith(airportPrefix);
      if (line.startsWith("SUSAD") || (terminal && line.charAt(12) == 'N')) {
        String key = line.substring(13, 17).trim() + "|" + line.substring(19, 21) + "|" + (terminal ? "PN" : line.substring(4, 6));
        if (fixRoles.containsKey(key)) {
          navaids.add(blankTail(replaceField(line, "datumCode", "WGE")));
          resolved.add(key);
        }
      } else if (line.startsWith("SUSAEA") || (terminal && line.charAt(12) == 'C')) {
        String key = line.substring(13, 18).trim() + "|" + line.substring(19, 21) + "|" + (terminal ? "PC" : "EA");
        if (fixRoles.containsKey(key)) {
          waypoints.add(toEnrouteWaypoint(line, fixRoles.get(key)));
          resolved.add(key);
        }
      }
    }

    Set<String> unresolved = new TreeSet<>(fixRoles.keySet());
    unresolved.removeAll(resolved);
    unresolved.removeIf(key -> key.endsWith("|PG"));
    if (!unresolved.isEmpty()) {
      throw new IllegalStateException("Unable to resolve fix references: " + unresolved);
    }
    navaids.sort(Comparator.comparing(line -> line.substring(13, 17)));
    waypoints.sort(Comparator.comparing(line -> line.substring(13, 18)));

    List<String> runwayLines = new ArrayList<>();
    List<String> msaLines = new ArrayList<>();
    List<String> fallbackMsaLines = new ArrayList<>();
    for (String line : cifp) {
      if (!line.startsWith(airportPrefix) || line.length() < 132) {
        continue;
      }
      String ident = line.substring(13, 18).trim();
      if (line.charAt(12) == 'G' && line.charAt(21) == '0' && runways.contains(ident)) {
        runwayLines.add(blankTail(line));
      }
      if (line.charAt(12) == 'S') {
        String section = line.substring(20, 22);
        String msa = blankTail(section.equals("PC") ? line.substring(0, 20) + "EA" + line.substring(22) : line);
        if (section.equals("PG") && runways.contains(ident)) {
          msaLines.add(msa);
        } else if (fixRoles.containsKey(ident + "|" + line.substring(18, 20) + "|" + section)) {
          fallbackMsaLines.add(msa);
        }
      }
    }

    String ident = options.getOrDefault("ident", procedure);
    char routeType = legs.stream().map(l -> l.charAt(19)).filter(c -> c != 'A').findFirst().orElse('A');

    List<String> lines = new ArrayList<>();
    lines.add(header(cifp, airportLine, airport, procedure, subSection, routeType, options));
    lines.addAll(navaids);
    lines.addAll(waypoints);
    lines.add(blankTail(replaceField(airportLine, "datumCode", "WGE")));
    legs.forEach(leg -> lines.add(blankTail(recodeLeg(leg, ident))));
    lines.addAll(runwayLines);
    lines.addAll(msaLines.isEmpty() ? fallbackMsaLines : msaLines);
    return lines;
  }

  /**
   * Maps the leg's waypoint description (col 43) to the specials waypoint type (col 28): IAF -> I, FAF -> A, IF -> C, MAP -> M.
   */
  private static char role(String waypointDescription) {
    return switch (waypointDescription.charAt(3)) {
      case 'A' -> 'I';
      case 'F' -> 'A';
      case 'I' -> 'C';
      case 'M' -> 'M';
      default -> ' ';
    };
  }

  private static void addReference(
      ArincRecord arincRecord,
      String prefix,
      char role,
      Map<String, Character> fixRoles,
      Set<String> runways
  ) {
    Optional<String> identifier = arincRecord.optionalField(prefix + "Identifier");
    if (identifier.isEmpty()) {
      return;
    }
    String section = arincRecord.rawField(prefix + "SectionCode") + arincRecord.rawField(prefix + "SubSectionCode");
    if (section.equals("PG")) {
      runways.add(identifier.get());
    }
    String key = identifier.get() + "|" + arincRecord.rawField(prefix + "IcaoRegion") + "|" + section;
    // when a fix plays multiple roles prefer the most "final" one
    fixRoles.merge(key, role, (a, b) -> " ICAM".indexOf(b) > " ICAM".indexOf(a) ? b : a);
  }

  private static String toEnrouteWaypoint(String line, char role) {
    StringBuilder builder = new StringBuilder(replaceField(line, "datumCode", "WGE"));
    String name = line.substring(98, 123).trim();
    boolean cnf = name.contains("(CNF)");
    if (cnf) {
      name = "CNF(" + name.replace("(CNF)", "").trim() + ")";
    }
    builder.replace(4, 13, "EAENRT   ");
    builder.replace(26, 31, "W" + role + "FR" + (cnf ? " " : "B"));
    builder.replace(98, 123, String.format("%-25s", name));
    return blankTail(builder.toString());
  }

  private static String recodeLeg(String line, String ident) {
    StringBuilder builder = new StringBuilder(line);
    builder.replace(13, 19, String.format("%-6s", ident));
    // the fix section lives in the same columns on primary and continuation records
    if (builder.substring(36, 38).equals("PC")) {
      builder.replace(36, 38, "EA");
    }
    if (PARSER.parse(line).isPresent()) {
      for (String prefix : List.of("recommendedNavaid", "centerFix")) {
        int section = fieldOffset(line, prefix + "SectionCode");
        int subSection = fieldOffset(line, prefix + "SubSectionCode");
        if (builder.charAt(section) == 'P' && builder.charAt(subSection) == 'C') {
          builder.setCharAt(section, 'E');
          builder.setCharAt(subSection, 'A');
        }
      }
    }
    return builder.toString();
  }

  private static String replaceField(String line, String fieldName, String value) {
    int start = fieldOffset(line, fieldName);
    int length = fieldLength(line, fieldName);
    return line.substring(0, start) + String.format("%-" + length + "s", value) + line.substring(start + length);
  }

  private static int fieldOffset(String line, String fieldName) {
    int offset = 0;
    for (RecordField<?> field : specFor(line).recordFields()) {
      if (field.fieldName().equals(fieldName)) {
        return offset;
      }
      offset += field.fieldSpec().fieldLength();
    }
    throw new IllegalArgumentException("No field " + fieldName + " for record " + line);
  }

  private static int fieldLength(String line, String fieldName) {
    return specFor(line).recordFields().stream()
        .filter(field -> field.fieldName().equals(fieldName))
        .findFirst()
        .map(field -> field.fieldSpec().fieldLength())
        .orElseThrow(() -> new IllegalArgumentException("No field " + fieldName + " for record " + line));
  }

  private static RecordSpec specFor(String line) {
    String record = line.substring(0, 132);
    return ArincVersion.V18.specs().stream()
        .filter(spec -> spec.matchesRecord(record))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No V18 spec for record " + line));
  }

  /**
   * Blanks columns 124-132 (file record number + cycle date), the "missing" required data in the specials files.
   */
  private static String blankTail(String line) {
    return String.format("%-132s", line).substring(0, 123) + " ".repeat(9);
  }

  private static String header(
      List<String> cifp,
      String airportLine,
      String airport,
      String procedure,
      char subSection,
      char routeType,
      Map<String, String> options
  ) {
    String airportName = airportLine.substring(93, 123).trim();
    String runway = procedure.replaceAll("^[A-Z](\\d{2}[LRC]?).*$", "$1");

    String type;
    String chart;
    if (subSection != 'F') {
      type = "RNAV";
      chart = procedure;
    } else {
      switch (routeType) {
        case 'H' -> { type = "RNAV"; chart = "RNAV (RNP) RWY " + runway; }
        case 'R', 'P' -> { type = "RNAV"; chart = "RNAV (GPS) RWY " + runway; }
        case 'I' -> { type = "ILS"; chart = "ILS RWY " + runway; }
        case 'L' -> { type = "LOC"; chart = "LOC RWY " + runway; }
        case 'V', 'S' -> { type = "VOR"; chart = "VOR RWY " + runway; }
        default -> { type = "OTHER"; chart = procedure; }
      }
    }

    String date = cifp.stream()
        .filter(l -> l.startsWith("HDR01"))
        .findFirst()
        .map(l -> Pattern.compile("\\d{2}-[A-Z]{3}-\\d{4}").matcher(l))
        .filter(Matcher::find)
        .map(m -> LocalDate.parse(m.group(), CIFP_HEADER_DATE).format(DateTimeFormatter.BASIC_ISO_DATE))
        .orElse("");

    StringBuilder header = new StringBuilder(" ".repeat(132));
    put(header, 1, "HDR");
    put(header, 5, pad(options.getOrDefault("city", airportName.split("[- ]")[0]).toUpperCase(), 24));
    put(header, 30, pad(options.getOrDefault("state", ""), 2));
    put(header, 32, "US");
    put(header, 34, pad(airport, 4));
    put(header, 38, pad(options.getOrDefault("type", type), 6));
    put(header, 44, subSection == 'F' ? "SIAP" : subSection == 'D' ? "DP  " : "STAR");
    put(header, 48, pad(options.getOrDefault("chart", chart), 50));
    put(header, 98, pad(options.getOrDefault("amendment", "ORIG"), 19));
    put(header, 117, pad(options.getOrDefault("date", date), 8));
    put(header, 125, pad(options.getOrDefault("version", "V18A"), 4));
    return header.toString();
  }

  private static String pad(String value, int length) {
    return String.format("%-" + length + "." + length + "s", value);
  }

  private static void put(StringBuilder builder, int column, String value) {
    builder.replace(column - 1, column - 1 + value.length(), value);
  }
}
