# Boogie DAFIF

## Module overview
This module within the Boogie software project provides a set of configurable and extensible parsers for DAFIF (Digital Aeronautical Flight Information File) formatted data.

## Quick start

### Oneshot parsing of a DAFIF zip

For users who want to go straight from a DAFIF zip file to assembled Boogie objects in one call:
```java
OneshotDafifParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence> parser =
    OneshotDafifParser.standard(DafifVersion.V81);

OneshotDafifParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace> records;
try (InputStream is = new FileInputStream("DAFIF8_1.zip")) {
  records = parser.assembleFrom(is);
}

Collection<Airport> airports = records.airports();
Collection<Fix> fixes = records.fixes();
Collection<Airway> airways = records.airways();
Collection<Procedure> procedures = records.procedures();
Collection<Airspace> boundaries = records.boundaries();
Collection<Airspace> specialUseAirspaces = records.specialUseAirspaces();
Collection<Airspace> allAirspaces = records.airspaces();
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
Collection<DafifBoundaryParent> boundaryParents = consumer.dafifBoundaryParents();
Collection<DafifBoundarySegment> boundarySegments = consumer.dafifBoundarySegments();
Collection<DafifSuasParent> suasParents = consumer.dafifSuasParents();
Collection<DafifSuasSegment> suasSegments = consumer.dafifSuasSegments();
```

### Coded airspace fields

Airspace models expose `BoundaryType`, `SpecialUseAirspaceType`, `Shape`, and `Derivation` from
`org.mitre.tdp.boogie.dafif.model.enums`. Each enum retains its explicit DAFIF `code()`; separate functions in
`org.mitre.tdp.boogie.dafif.v81.converter` handle code lookup.
Boundary type codes are integers (`8` represents `08` in the file); the other enum codes are strings.

```java
BoundaryType boundaryType = boundaryParents.iterator().next().boundaryType();
SpecialUseAirspaceType suasType = suasParents.iterator().next().specialUseAirspaceType();
Shape shape = suasSegments.iterator().next().shape();
Optional<Derivation> derivation = suasSegments.iterator().next().derivation();

boolean restricted = suasType == SpecialUseAirspaceType.RESTRICTED;
String sourceCode = suasType.code(); // "R" for RESTRICTED
SpecialUseAirspaceType converted = SpecialUseAirspaceTypeConverter.INSTANCE.apply("R");
```

Field parsers retain their numeric/string representation in `DafifRecord`. Record converters translate those values into
enums using `BoundaryTypeConverter`, `SpecialUseAirspaceTypeConverter`, `ShapeConverter`, and `DerivationConverter`.
Blank derivation fields remain absent. Unsupported codes raise an explicit conversion error; `BoundaryType.OTHER` is
reserved for the defined code `14`.

### Indexing in provided database implementations

DAFIF records frequently reference other record types. For example, terminal segments reference waypoints and navaids for their
fixes, and ATS segments reference waypoints that may in turn point to navaids. Boogie provides two pre-configured database
implementations to handle these cross-references:

```java
// fix database - indexes waypoints and navaids for lookup by identifier + country
DafifFixDatabase xmlFixDatabase = DafifDatabaseFactory.newFixDatabase(
    consumer.dafifWaypoints(),
    consumer.dafifNavaids()
);

Optional<DafifWaypoint> waypoint = xmlFixDatabase.waypoint("JMACK", "US");
Optional<DafifNavaid> navaid = xmlFixDatabase.navaid("DCA", "US", 4, 0);
Optional<DafifNavaid> navaidForWpt = xmlFixDatabase.navaidFor(waypoint.get()); // WPT -> NAV resolution

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
DafifFixDatabase xmlFixDatabase;
DafifTerminalAreaDatabase terminalAreaDatabase;

FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(terminalAreaDatabase, xmlFixDatabase);

// Procedure assembly - groups terminal segments by airport and procedure, builds transitions and legs
ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureStrategy = new ProcedureAssemblyStrategy.Standard();
ProcedureAssembler<Procedure> procedureAssembler = ProcedureAssembler.standard(
    terminalAreaDatabase, xmlFixDatabase, procedureStrategy, fixStrategy);

List<Procedure> procedures = procedureAssembler.assemble(consumer.dafifTerminalParents()).toList();

// Airway assembly - groups ATS segments by identifier + direction, produces N+1 leg chains per direction
AirwayAssembler<Airway> airwayAssembler = AirwayAssembler.standard(xmlFixDatabase, fixStrategy);

List<Airway> airways = airwayAssembler.assemble(consumer.dafifAts()).toList();

// Airport assembly - zips airports with their runways and ILS components
AirportAssembler<Airport> airportAssembler = AirportAssembler.standard(terminalAreaDatabase);

List<Airport> airports = consumer.dafifAirports().stream().map(airportAssembler::assemble).toList();

// Fix assembly - converts waypoints and navaids to Fix objects
FixAssembler<Fix> fixAssembler = FixAssembler.standard(terminalAreaDatabase, xmlFixDatabase);

List<Fix> fixes = Stream.concat(consumer.dafifWaypoints().stream(), consumer.dafifNavaids().stream())
    .flatMap(model -> fixAssembler.assemble(model).stream())
    .toList();
```

Procedure crossing constraints use `ALT_ONE` for blank/`G`/`I` (at), `+`/`H`/`J` (at or above),
and `-` (at or below). `B` uses `ALT_TWO` as the lower bound and `ALT_ONE` as the upper bound;
`C` uses `ALT_TWO` as the lower bound. Glide-slope values in `ALT_TWO` for `G`/`H`/`I`/`J` remain
in the parsed source model. Flight levels are converted to feet in the core altitude range.

The standard procedure strategy uses speed limits that apply to all aircraft without an altitude
condition. If both speed fields contain such limits, it uses the lower maximum. Aircraft-qualified
or altitude-qualified limits remain in `DafifTerminalSegment` for custom strategies, since the core
`Leg` speed range cannot express those conditions. The procedure assembler propagates restrictions backward
on SIDs and forward on STARs and approaches, keeping holding-pattern restrictions local to the hold.
Connected transitions inherit restrictions common to all contributing branches; conflicting branch-specific
restrictions remain in the source records because one shared leg cannot express different path conditions.
Custom strategies can receive the resolved qualified limits through the contextual `convertLeg` overload.

Core legs retain RF arc radii and published holding-fix flags. DAFIF's positive descent-angle magnitude
becomes a negative core vertical angle. Runway fixes use physical thresholds for SIDs and displaced
landing thresholds for approaches.

### Assembling boundaries and special use airspaces

```java
List<Airspace> boundaries = BoundaryAssembler.standard()
    .assemble(consumer.dafifBoundaryParents(), consumer.dafifBoundarySegments()).toList();
List<Airspace> specialUseAirspaces = SuasAssembler.standard()
    .assemble(consumer.dafifSuasParents(), consumer.dafifSuasSegments()).toList();
```

The assemblers join parent metadata to ordered geometry segments. Boundaries use `BDRY_IDENT`; special use airspaces use
`SUAS_IDENT` and `SECTOR`. Boundary types 08 and 12 produce `FIR` and `UIR`; other boundary types produce `CONTROLLED`.
Special use airspaces produce `RESTRICTIVE`. Identifiers retain the DAFIF identity and sector to keep sectors distinct.

The standard strategies support great circles, rhumb lines, directed arcs, and circles. Generalized boundaries use their
published points as great-circle segments. Source coordinate gaps up to 0.1 NM use explicit great-circle connections that
retain both endpoints. Point definitions, circles with an inner radius, and open or disconnected boundaries cannot be
represented by the core `Airspace` model; standard assembly skips these airspaces with a diagnostic. Their complete source records remain
available from the consumer. `BoundaryAssembler.usingStrategy(...)` and `SuasAssembler.usingStrategy(...)` accept custom
strategies when a client model needs additional geometry or metadata.

Altitude limits expressed as flight levels or AMSL convert to feet MSL. AGL, surface, unknown, unlimited, and by-NOTAM limits
leave the corresponding side of the range unbounded: converting terrain-relative limits to MSL requires terrain data.
The standard oneshot factory returns Boogie `Airspace` objects through `boundaries()`, `specialUseAirspaces()`, and `airspaces()`.
The builder accepts boundary and special use airspace strategies sharing the client-defined `AIR` and `ASEQ` types.
These types do not need to implement Boogie's `Airspace` or `AirspaceSequence` interfaces; all three accessors return `Collection<AIR>`.

### Assembling DAFIF into your own models

All of the `*Assembler` classes support custom assembly strategies. This allows clients to inject their own construction logic
for domain-specific model types:

```java
// custom fix strategy
FixAssemblyStrategy<MyCustomFix> customFixStrategy = new MyCustomFixStrategy<>();

// custom procedure strategy
ProcedureAssemblyStrategy<MyProcedure, MyTransition, MyLeg, MyCustomFix> customProcedureStrategy = ...;
ProcedureAssembler<MyProcedure> customAssembler = ProcedureAssembler.withStrategy(
    terminalAreaDatabase, xmlFixDatabase, customProcedureStrategy, customFixStrategy);

// custom airway strategy
AirwayAssemblyStrategy<MyAirway, MyCustomFix, MyLeg> customAirwayStrategy = ...;
AirwayAssembler<MyAirway> customAirwayAssembler = AirwayAssembler.withStrategy(
    xmlFixDatabase, customFixStrategy, customAirwayStrategy);

// or via the oneshot parser builder
BoundaryAssemblyStrategy<MyAirspace, MyAirspaceSequence> myBoundaryStrategy = ...;
SuasAssemblyStrategy<MyAirspace, MyAirspaceSequence> mySuasStrategy = ...;

OneshotDafifParser<MyAirport, MyRunway, MyFix, MyLeg, MyTransition, MyAirway, MyProcedure, MyAirspace, MyAirspaceSequence> parser =
    OneshotDafifParser.<MyAirport, MyRunway, MyFix, MyLeg, MyTransition, MyAirway, MyProcedure, MyAirspace, MyAirspaceSequence>builder(DafifVersion.V81)
        .airportStrategy(myAirportStrategy)
        .fixStrategy(myFixStrategy)
        .airwayStrategy(myAirwayStrategy)
        .procedureStrategy(myProcedureStrategy)
        .boundaryStrategy(myBoundaryStrategy)
        .suasStrategy(mySuasStrategy)
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
| `DAFIFT/BDRY` | `BDRY_PAR.TXT`, `BDRY.TXT` | Boundary metadata and geometry segments |
| `DAFIFT/SUAS` | `SUAS_PAR.TXT`, `SUAS.TXT` | Special use airspace metadata and geometry segments |

Each `.TXT` file has a header row with tab-separated column names followed by data rows. Records are parsed according to the
column definitions in the `DafifRecordSpec` implementations for the appropriate version.

### Key differences from ARINC 424

1. **Format**: Tab-delimited text vs. fixed-width records.
2. **Distribution**: Zip archive with multiple `.TXT` files vs. a single flat file.
3. **Airway representation**: DAFIF represents airways as segments with start/end waypoints per direction. Each unique
   `ATS_IDENT + DIRECTION` group is split into continuous sections at endpoint gaps or published end markers.
   Each section produces a separate `Airway` object with its own starting fix.
4. **Procedure representation**: DAFIF splits procedures into parent records (`TRM_PAR.TXT`) and segment records (`TRM_SEG.TXT`),
   rather than encoding everything in a single leg record.
5. **Fix references**: ATS segments with waypoint description codes `N` (NDB) or `V` (VOR) resolve their navaids through
   the waypoint table — the waypoint acts as a pointer to the navaid via `navaidIdentifier`, `navaidType`, `navaidCountryCode`,
   and `navaidKeyCode` fields.
6. **Course values**: Some ATS outbound magnetic course values carry a `T` suffix indicating True course (e.g. `215.T`). The
   assembler converts these using the departure fix's magnetic variation. Terminal true courses use the
   available navaid, waypoint, or procedure variation. If the needed variation is missing, the core magnetic
   course remains absent. Grid courses (`G`) also remain absent because the source does not identify a grid
   reference; the original course strings remain available in the DAFIF models.
7. **RNP encoding**: DAFIF encodes a mantissa and exponent (`100` = 10 NM; `031` = 0.3 NM).
   Assembled legs use nautical miles.

## Current capabilities

| Version | Airport | Runway | Add Runway | ILS  | Navaid | Waypoint | Terminal Parent | Terminal Segment | ATS (Airways) |
|:-------:|:-------:|:------:|:----------:|:----:|:------:|:--------:|:---------------:|:----------------:|:-------------:|
| 8.1     | y       | y      | y          | y    | y      | y        | y               | y                | y             |

The nine record types above and the four boundary/SUAS parent and segment types support parsing, validation, and conversion
to typed Java model classes. The assembly layer produces standard Boogie `Airport`, `Fix`, `Airway`, `Procedure`, and
`Airspace` objects from the parsed data. Auxiliary boundary/SUAS country and note tables are not parsed.

Heliport-specific data (`TRMH/`, `SUPPH/` directories) is excluded from parsing.
