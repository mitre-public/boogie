package org.mitre.boogie.xml;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.mitre.boogie.xml.v23_4.generated.Port;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/** References between the JAXB objects produced by the CIFP test fixture converter. */
final class CifpXmlReferences {

  private static final ArincRecordParser RAW_PARSER = ArincRecordParser.standard(ArincVersion.V19.specs());

  private final Map<PortKey, Port> ports = new HashMap<>();
  private final Map<FixKey, Object> fixes = new HashMap<>();
  private final Map<String, String> airspaceRecords = new HashMap<>();
  private final Map<ProcedureRecordKey, String> procedureRecords = new HashMap<>();
  private final Map<RouteRecordKey, String> routeRecords = new HashMap<>();

  void addPort(String ident, String icao, Port port) {
    String section;
    if (port instanceof Airport) {
      section = "P";
    } else if (port instanceof Heliport) {
      section = "H";
    } else {
      throw new IllegalArgumentException("Unsupported CIFP port type: " + port.getClass().getName());
    }
    Port previous = ports.putIfAbsent(new PortKey(section, clean(ident), clean(icao)), port);
    if (previous != null && previous != port) {
      throw new IllegalArgumentException("Duplicate CIFP port: " + section + "/" + ident + "/" + icao);
    }
  }

  Port port(String section, String ident, String icao) {
    return Objects.requireNonNull(ports.get(new PortKey(clean(section), clean(ident), clean(icao))),
        () -> "Missing CIFP port: " + section + "/" + ident + "/" + icao);
  }

  Port port(String ident, String icao) {
    Port airport = ports.get(new PortKey("P", clean(ident), clean(icao)));
    Port heliport = ports.get(new PortKey("H", clean(ident), clean(icao)));
    if (airport != null && heliport != null) {
      throw new IllegalArgumentException("Ambiguous CIFP port; supply its section: " + ident + "/" + icao);
    }
    if (airport != null) {
      return airport;
    }
    return Objects.requireNonNull(heliport,
        () -> "Missing CIFP port: " + ident + "/" + icao);
  }

  void addFix(String section, String subsection, String ident, String icao, String portIdent, Object target) {
    FixKey key = fixKey(section, subsection, ident, icao, portIdent);
    Object previous = fixes.putIfAbsent(key, target);
    if (previous != null && previous != target) {
      throw new IllegalArgumentException("Duplicate CIFP fix: " + key);
    }
  }

  Object fix(String section, String subsection, String ident, String icao, String portIdent) {
    // An unresolved reference remains absent; the route mapper retains its textual identifier.
    return fixes.get(fixKey(section, subsection, ident, icao, portIdent));
  }

  void addAirspaceRecord(String family, int fileRecordNumber, String rawRecord) {
    String key = family + "/" + fileRecordNumber;
    String previous = airspaceRecords.putIfAbsent(key, rawRecord);
    if (previous != null && !previous.equals(rawRecord)) {
      throw new IllegalArgumentException("Ambiguous CIFP airspace record number: " + key);
    }
  }

  String rawAirspaceField(String family, int fileRecordNumber, int start, int end) {
    String rawRecord = airspaceRecords.get(family + "/" + fileRecordNumber);
    if (rawRecord == null) {
      return null;
    }
    return rawRecord.substring(start, end);
  }

  /** Retains only source strings; parsed-field caches are created transiently when a raw field is requested. */
  void addRouteRecord(String family, ArincRecord record) {
    int fileRecordNumber = record.requiredField("fileRecordNumber");
    switch (family) {
      case "PD", "PE", "PF", "HD", "HE", "HF" -> addRaw(procedureRecords, new ProcedureRecordKey(family,
          clean(record.rawField("airportIdentifier")), clean(record.rawField("airportIcaoRegion")),
          clean(record.rawField("sidStarIdentifier")), fileRecordNumber), record.rawRecord());
      case "ER", "EP" -> addRaw(routeRecords, new RouteRecordKey(family, fileRecordNumber), record.rawRecord());
      default -> throw new IllegalArgumentException("Unsupported raw route family: " + family);
    }
  }

  String procedureField(ArincProcedureLeg source, String fieldName) {
    ProcedureRecordKey key = new ProcedureRecordKey(source.sectionCode().name() + source.subSectionCode().orElseThrow(),
        clean(source.airportIdentifier()), clean(source.airportIcaoRegion()), clean(source.sidStarIdentifier()),
        source.fileRecordNumber());
    return rawField(procedureRecords.get(key), fieldName);
  }

  String airwayField(ArincAirwayLeg source, String fieldName) {
    return rawField(routeRecords.get(new RouteRecordKey("ER", source.fileRecordNumber())), fieldName);
  }

  String holdingField(ArincHoldingPattern source, String fieldName) {
    if (source.fileRecordNumber().isEmpty()) {
      return null;
    }
    return rawField(routeRecords.get(new RouteRecordKey("EP", source.fileRecordNumber().orElseThrow())), fieldName);
  }

  private static <K> void addRaw(Map<K, String> records, K key, String raw) {
    String previous = records.putIfAbsent(key, raw);
    if (previous != null && !previous.equals(raw)) {
      throw new IllegalArgumentException("Ambiguous CIFP route record: " + key);
    }
  }

  private static String rawField(String raw, String fieldName) {
    if (raw == null) {
      return null;
    }
    return RAW_PARSER.parse(raw).orElseThrow(() -> new IllegalArgumentException("Unrecognized stored CIFP route"))
        .rawField(fieldName);
  }

  static String id(String... components) {
    StringBuilder key = new StringBuilder();
    for (String component : components) {
      String value = clean(component);
      key.append(value.length()).append(':').append(value);
    }
    return "cifp-" + UUID.nameUUIDFromBytes(key.toString().getBytes(StandardCharsets.UTF_8));
  }

  private static FixKey fixKey(String section, String subsection, String ident, String icao, String portIdent) {
    String sectionCode = clean(section);
    String subsectionCode = clean(subsection);
    String scope = "";
    if ((sectionCode.equals("P") || sectionCode.equals("H")) && !subsectionCode.equals("A")) {
      scope = clean(portIdent);
    }
    return new FixKey(sectionCode, subsectionCode, clean(ident), clean(icao), scope);
  }

  private static String clean(String value) {
    return Objects.toString(value, "").trim();
  }

  private record PortKey(String section, String ident, String icao) {}

  private record FixKey(String section, String subsection, String ident, String icao, String portIdent) {}

  private record ProcedureRecordKey(String family, String airport, String icao, String procedure, int fileRecordNumber) {}

  private record RouteRecordKey(String family, int fileRecordNumber) {}
}
