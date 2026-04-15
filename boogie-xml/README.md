# Boogie XML

## Module overview

This module provides APIs for reading and writing ARINC 424 XML formats. It supports streaming
unmarshalling into either raw JAXB model classes or fully assembled Boogie domain types, and
marshalling back to XML.

## Parsing

Two oneshot parsers are provided depending on whether you need raw model objects or assembled client types.

### OneshotXmlModelParser — raw model classes

Returns the unmarshalled `ArincRecords` container with raw JAXB model objects. No assembly or fix
resolution is performed.

```java
try (InputStream is = new FileInputStream("arinc424.xml")) {
  ArincRecords records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(is);

  records.airports();        // Set<ArincAirport>
  records.waypoints();       // Set<ArincWaypoint>
  records.ndbNavaids();      // Set<ArincNdbNavaid>
  records.vhfNavaids();      // Set<ArincVhfNavaid>
  records.arincAirways();    // Set<ArincAirway>
  records.heliports();       // Set<ArincHeliport>
  records.holdingPatterns(); // Set<ArincHoldingPattern>
}
```

### OneshotXmlParser — assembled client types

Streams the XML, assembles all record types on-the-fly, and returns typed client collections plus
lookup databases. All assembly (fixes, airports, airways, procedures, heliports) happens inline
during streaming — no buffering of intermediate model objects.

```java
try (InputStream is = new FileInputStream("arinc424.xml")) {
  var records = OneshotXmlParser.standard(ArincXmlVersion.V23_4).assembleFrom(is);

  records.airports();    // Collection<Airport>
  records.fixes();       // Collection<Fix>
  records.airways();     // Collection<Airway>
  records.procedures();  // Collection<Procedure>
  records.heliports();   // Collection<Heliport>
}
```

Custom assembly strategies can be provided via the builder:

```java
OneshotXmlParser.<MyAirport, MyRunway, MyFix, MyLeg, MyTransition, MyAirway, MyProcedure, MyHelipad, MyHeliport>builder()
    .version(ArincXmlVersion.V23_4)
    .fixStrategy(myFixStrategy)
    .airportStrategy(myAirportStrategy)
    .airwayStrategy(myAirwayStrategy)
    .procedureStrategy(myProcedureStrategy)
    .heliportStrategy(myHeliportStrategy)
    .build();
```

### Databases

The assembling parser also builds two lookup databases, accessible from `ClientRecords`:

- **`XmlFixDatabase`** — global fix lookups by XML reference ID or by typed (identifier, ICAO code) queries
  (`waypoint()`, `ndbNavaid()`, `vhfNavaid()`, `airport()`, `heliport()`)
- **`XmlTerminalAreaDatabase`** — airport/heliport-scoped `PortPage`s with terminal fix lookups
  (runways, gates, terminal waypoints, NDB navaids, helipads, localizer/glideslopes, markers, GNSS landing systems)

```java
var records = OneshotXmlParser.standard(ArincXmlVersion.V23_4).assembleFrom(is);

// Global fix lookup by reference ID
records.fixDatabase().fix("some-ref-id");

// Typed lookup by identifier + ICAO code
records.fixDatabase().waypoint("JMACK", "K6");
records.fixDatabase().vhfNavaid("DXO", "K6");

// Terminal area — airport-scoped lookups
records.terminalAreaDatabase().airportPage("KATL", "K6")
    .ifPresent(page -> {
        page.runway("RW09L");
        page.localizerGlideSlope("ILS09L");
        page.terminalWaypoint("TWPT1");
    });
```

## Marshalling

`StreamingMarshaller` writes ARINC 424 XML incrementally — sections are written as data becomes
available, and airports can be written one at a time.

THIS FUNCTIONALITY IS WIP AND NOT COMPLETE

```java
ByteArrayOutputStream output = new ByteArrayOutputStream();

try (StreamingMarshaller marshaller = new StreamingMarshaller(output)) {
  marshaller.writeHeader();

  marshaller.writeWaypoints(Collections.emptySet());

  // Airports support incremental writing
  marshaller.startAirports();
  // Write airports one at a time as they become available
  // for (ArincAirport airport : airportSource) { marshaller.writeAirport(airport); }
  marshaller.endAirports();

  marshaller.writeNavaids(Collections.emptySet(), Collections.emptySet());
  marshaller.writeAirways(Collections.emptySet());
  marshaller.writeHoldingPatterns(Collections.emptySet());

  marshaller.writeFooter();
}
```

The `StreamingMarshaller` enforces proper XML structure:
- `writeHeader()` must be called first
- Other sections cannot be written while the airports section is open
- Auto-closes the footer when using try-with-resources
