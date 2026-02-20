# Boogie DAFIF

## Module overview
This module within the Boogie software project provides a set of configurable and extensible parsers for DAFIF (Digital Aeronautical Flight Information File) formatted data.

## Quick start

### Oneshot parsing of a DAFIF zip

For users who want to go straight from a DAFIF zip file to assembled Boogie objects in one call:
```java
OneshotDafifParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure> parser = OneshotDafifParser.standard(DafifVersion.V81);

OneshotDafifParser.ClientRecords<Airport, Fix, Airway, Procedure> records;
try (InputStream is = new FileInputStream("DAFIF8_1.zip")) {
  records = parser.assembleFrom(is);
}

Collection<Airport> airports = records.airports();
Collection<Fix> fixes = records.fixes();
Collection<Airway> airways = records.airways();
Collection<Procedure> procedures = records.procedures();
```

### Parsing a DAFIF zip file (step by step)

If you need more control over the parsing and assembly pipeline:
```java
DafifFileParser parser = new DafifFileParser(DafifVersion.V81);
ConvertingDafifRecordConsumer consumer = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);

// parse individual .TXT files from the DAFIF zip
try (ZipInputStream zis = new ZipInputStream(new FileInputStream("DAFIF8_1.zip"))) {
  ZipEntry entry;
  while ((entry = zis.getNextEntry()) != null) {
    String filename = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
    if (filename.endsWith(".TXT")) {
      parser.apply(zis, filename).forEach(consumer);
    }
    zis.closeEntry();
  }
}

// the consumer then contains the collections of parsed DAFIF records
Collection<DafifAirport> airports = consumer.dafifAirports();
Collection<DafifRunway> runways = consumer.dafifRunways();
Collection<DafifNavaid> navaids = consumer.dafifNavaids();
Collection<DafifWaypoint> waypoints = consumer.dafifWaypoints();
Collection<DafifTerminalParent> terminalParents = consumer.dafifTerminalParents();
Collection<DafifTerminalSegment> terminalSegments = consumer.dafifTerminalSegments();
Collection<DafifAirTrafficSegment> atsSegments = consumer.dafifAts();
Collection<DafifIls> ils = consumer.dafifIls();
Collection<DafifAddRunway> addRunways = consumer.dafifAddRunways();
```

### Indexing in provided database implementations

DAFIF records frequently reference other record types. For example, terminal segments reference waypoints and navaids for their
fixes, and ATS segments reference waypoints that may in turn point to navaids. Boogie provides two pre-configured database
implementations to handle these cross-references:

```java
// fix database - indexes waypoints and navaids for lookup by identifier + country
DafifFixDatabase fixDatabase = DafifDatabaseFactory.newFixDatabase(
    consumer.dafifWaypoints(),
    consumer.dafifNavaids()
);

Optional<DafifWaypoint> waypoint = fixDatabase.waypoint("JMACK", "US");
Optional<DafifNavaid> navaid = fixDatabase.navaid("DCA", "US", 4, 0);
Optional<DafifNavaid> navaidForWpt = fixDatabase.navaidFor(waypoint.get()); // WPT -> NAV resolution

// terminal area database - airport-indexed view of runways, ILS, terminal segments
DafifTerminalAreaDatabase terminalAreaDatabase = DafifDatabaseFactory.newTerminalAreaDatabase(
    consumer.dafifAirports(),
    consumer.dafifRunways(),
    consumer.dafifAddRunways(),
    consumer.dafifIls(),
    consumer.dafifTerminalSegments()
);

// or directly from the consumer
DafifTerminalAreaDatabase terminalAreaDatabase = DafifDatabaseFactory.newTerminalAreaDatabase(consumer);

Collection<DafifRunway> kjfkRunways = terminalAreaDatabase.runwaysAt("KJFK  ");
```

### Assembling Boogie-like records

Boogie-core provides interfaces for common navigational objects (Airways, Procedures, Fixes, etc.). The DAFIF module provides
assemblers that convert parsed DAFIF records into concrete implementations of these interfaces.

```java
DafifFixDatabase fixDatabase;
DafifTerminalAreaDatabase terminalAreaDatabase;

FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(terminalAreaDatabase, fixDatabase);

// Procedure assembly - groups terminal segments by airport and procedure, builds transitions and legs
ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureStrategy = new ProcedureAssemblyStrategy.Standard();
ProcedureAssembler<Procedure> procedureAssembler = ProcedureAssembler.standard(
    terminalAreaDatabase, fixDatabase, procedureStrategy, fixStrategy);

List<Procedure> procedures = procedureAssembler.assemble(consumer.dafifTerminalParents()).toList();

// Airway assembly - groups ATS segments by identifier + direction, produces N+1 leg chains per direction
AirwayAssembler<Airway> airwayAssembler = AirwayAssembler.standard(fixDatabase, fixStrategy);

List<Airway> airways = airwayAssembler.assemble(consumer.dafifAts()).toList();

// Airport assembly - zips airports with their runways and ILS components
AirportAssembler<Airport> airportAssembler = AirportAssembler.standard(terminalAreaDatabase);

List<Airport> airports = consumer.dafifAirports().stream().map(airportAssembler::assemble).toList();

// Fix assembly - converts waypoints and navaids to Fix objects
FixAssembler<Fix> fixAssembler = FixAssembler.standard(terminalAreaDatabase, fixDatabase);

List<Fix> fixes = Stream.concat(consumer.dafifWaypoints().stream(), consumer.dafifNavaids().stream())
    .flatMap(model -> fixAssembler.assemble(model).stream())
    .toList();
```

### Assembling DAFIF into your own models

All of the `*Assembler` classes support custom assembly strategies. This allows clients to inject their own construction logic
for domain-specific model types:

```java
// custom fix strategy
FixAssemblyStrategy<MyCustomFix> customFixStrategy = new MyCustomFixStrategy<>();

// custom procedure strategy
ProcedureAssemblyStrategy<MyProcedure, MyTransition, MyLeg, MyCustomFix> customProcedureStrategy = ...;
ProcedureAssembler<MyProcedure> customAssembler = ProcedureAssembler.withStrategy(
    terminalAreaDatabase, fixDatabase, customProcedureStrategy, customFixStrategy);

// custom airway strategy
AirwayAssemblyStrategy<MyAirway, MyCustomFix, MyLeg> customAirwayStrategy = ...;
AirwayAssembler<MyAirway> customAirwayAssembler = AirwayAssembler.withStrategy(
    fixDatabase, customFixStrategy, customAirwayStrategy);

// or via the oneshot parser builder
OneshotDafifParser<MyAirport, MyRunway, MyFix, MyLeg, MyTransition, MyAirway, MyProcedure> parser =
    OneshotDafifParser.<MyAirport, MyRunway, MyFix, MyLeg, MyTransition, MyAirway, MyProcedure>builder(DafifVersion.V81)
        .airportStrategy(myAirportStrategy)
        .fixStrategy(myFixStrategy)
        .airwayStrategy(myAirwayStrategy)
        .procedureStrategy(myProcedureStrategy)
        .build();
```

## What is DAFIF?

DAFIF (Digital Aeronautical Flight Information File) is a comprehensive database of aeronautical navigation data maintained by
the National Geospatial-Intelligence Agency (NGA). Unlike ARINC 424 (which uses fixed-width records), DAFIF uses a tab-delimited
format distributed as a collection of `.TXT` files within a zip archive.

The DAFIF zip is organized into subdirectories by data type:

| Directory     | File(s)             | Description                                                     |
|:--------------|:--------------------|:----------------------------------------------------------------|
| `DAFIFT/ARPT` | `ARPT.TXT`         | Airports                                                        |
| `DAFIFT/ARPT` | `RWY.TXT`          | Runways                                                         |
| `DAFIFT/ARPT` | `ADD_RWY.TXT`      | Additional runway data                                          |
| `DAFIFT/ARPT` | `ILS.TXT`          | ILS/localizer/glideslope components                             |
| `DAFIFT/NAV`  | `NAV.TXT`          | Navaids (VOR, NDB, TACAN, DME, etc.)                           |
| `DAFIFT/WPT`  | `WPT.TXT`          | Waypoints (enroute and terminal)                                |
| `DAFIFT/TRM`  | `TRM_PAR.TXT`      | Terminal procedure parents (SID/STAR/Approach metadata)         |
| `DAFIFT/TRM`  | `TRM_SEG.TXT`      | Terminal procedure segments (individual legs)                   |
| `DAFIFT/ATS`  | `ATS.TXT`          | Air Traffic Service routes (airways)                            |

Each `.TXT` file has a header row with tab-separated column names followed by data rows. Records are parsed according to the
column definitions in the `DafifRecordSpec` implementations for the appropriate version.

### Key differences from ARINC 424

1. **Format**: Tab-delimited text vs. fixed-width records.
2. **Distribution**: Zip archive with multiple `.TXT` files vs. a single flat file.
3. **Airway representation**: DAFIF represents airways as segments with start/end waypoints per direction. Each unique
   `ATS_IDENT + DIRECTION` combination produces a separate `Airway` object.
4. **Procedure representation**: DAFIF splits procedures into parent records (`TRM_PAR.TXT`) and segment records (`TRM_SEG.TXT`),
   rather than encoding everything in a single leg record.
5. **Fix references**: ATS segments with waypoint description codes `N` (NDB) or `V` (VOR) resolve their navaids through
   the waypoint table — the waypoint acts as a pointer to the navaid via `navaidIdentifier`, `navaidType`, `navaidCountryCode`,
   and `navaidKeyCode` fields.
6. **Course values**: Some ATS outbound magnetic course values carry a `T` suffix indicating True course (e.g. `215.T`). The
   assembler handles this automatically.

## Current capabilities

| Version | Airport | Runway | Add Runway | ILS  | Navaid | Waypoint | Terminal Parent | Terminal Segment | ATS (Airways) |
|:-------:|:-------:|:------:|:----------:|:----:|:------:|:--------:|:---------------:|:----------------:|:-------------:|
| 8.1     | y       | y      | y          | y    | y      | y        | y               | y                | y             |

All 9 DAFIF record types are supported for parsing, validation, and conversion to typed Java model classes. The assembly layer
produces standard Boogie `Airport`, `Fix`, `Airway`, and `Procedure` objects from the parsed data.

Heliport-specific data (`TRMH/`, `SUPPH/` directories) is excluded from parsing.