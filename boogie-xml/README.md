# Boogie XML

## Module overview

This module provides streaming ARINC 424 XML parsing into intermediate model classes or assembled
Boogie domain types. It also reads and writes EXI binary XML, with or without an XML schema, through
stream conversion and StAX readers/writers that work with JAXB.

## Parsing

Two oneshot parsers are provided depending on whether you need intermediate model objects or assembled client types.

### OneshotXmlModelParser — intermediate model classes

Returns an `ArincRecords` container with validated, converted intermediate models. No assembly or fix
resolution is performed. This container retains selected record families, rather than the entire XML document.

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

`standardBuilder(version)` starts with the standard assembly strategies and lets callers replace
individual strategies or choose an input reader. Use `builder()` when supplying all assembly strategies:

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

## EXI binary XML

EXI support uses EXIficient. Existing parser methods continue to read textual XML by default.
The EXI schema choice is independent of `ArincXmlVersion`, which selects the JAXB models and record
converters used by the ARINC parsers.

### Convert XML and EXI streams

```java
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;

var options = ExiOptions.schemaLess().withCompression(true);
var codec = new ExiCodec(options);

try (InputStream xml = Files.newInputStream(Path.of("arinc424.xml"));
     OutputStream exi = Files.newOutputStream(Path.of("arinc424.exi"))) {
  codec.encode(xml, exi);
}

try (InputStream exi = Files.newInputStream(Path.of("arinc424.exi"));
     OutputStream xml = Files.newOutputStream(Path.of("decoded.xml"))) {
  codec.decode(exi, xml);
}
```

Conversion streams XML events without building a complete document in memory. Callers own the input
and output streams and must close them. A codec can be reused for multiple documents.

### Test fixtures and export validation

Run the module's unit tests from the repository root:

```shell
./gradlew :boogie-xml:unit
```

These use checked-in or synthetic fixtures and generate their test schemas locally; the private
integration archives and official ARINC XSDs are not required.

#### Sample EXI artifacts

To generate a compressed, schema-less EXI file from `gibberish-sample.xml`:

```shell
./gradlew :boogie-xml:unit --tests 'org.mitre.boogie.xml.OneshotExiParserTest.writesGibberishSampleExi' --rerun-tasks
```

The test writes `boogie-xml/build/exi/gibberish-sample-no-schema.exi`. The file remains after the test until the
module's build directory is cleaned.

The companion `writesGibberishSampleSchemaInformedExi` test writes `gibberish-sample-schema-informed.exi`.
Its XSDs are generated from the checked-in JAXB classes under `build/exi/gibberish-sample-schemas/`,
using schema ID `urn:boogie:test:arinc424:jaxb:23.4`. This keeps both schema modes covered by unit tests
without external schema files. These generated test schemas are distinct from the official ARINC XSDs.

#### Full CIFP and DAFIF integration tests

The full fixture and export tests use the `XML` tag and run in their own task, defined by the
shared [java-conventions build plugin](../buildSrc/src/main/kotlin/boogie/java-conventions.gradle.kts):

```shell
./gradlew :boogie-xml:xml-integration
```

The [XML CI job](../.github/workflows/xml-integration.yml) checks out `mitre-tdp/boogie-test` with Git LFS
into `boogie-xml/src/test/resources`, using the same SSH key as the other integration jobs.
That checkout supplies `DAFIF8_1_2601.zip`;
the job also copies the tracked `cifp-2101.dat.gz` fixture from `boogie-arinc/src/test/resources`.
Local runs need both archives in `boogie-xml/src/test/resources`. The `unit`, `cifp-integration`,
`dafif-integration`, `lido-integration`, and `assignment-integration` tasks exclude the XML-tagged tests.
The general `test` task still includes them and therefore also needs the integration archives.
The [publish workflow](../.github/workflows/publish.yml) runs XML integration before releasing.

The task runs four tests: CIFP and DAFIF fixture validation, plus one XML/EXI export test for each source.
Its HTML report is `boogie-xml/build/reports/tests/xml-integration/index.html`.

Together, the unit and integration suites check:

- Fixture record counts, unique XML IDs, resolvable references, cycle dates, and validity dates.
  The DAFIF check also reconstructs the directed airway graph and compares every edge's altitude
  limits and decoded RNP with the source, including flight-level versus MSL references.
- Each exported EXI file's `$EXI` cookie and complete decoding through `END_DOCUMENT`, with one
  root and balanced elements. Namespace-aware element and attribute names, counts, and document
  structure are compared with the source XML.
- Parsed model values and assembled record counts for the small sample in `OneshotExiParserTest`.
  Full-publication structural comparisons omit text and attribute values because typed EXI values
  can be normalized; those comparisons do not establish complete value preservation or XSD validity.

Related checks use JUnit `assertAll` so independent failures are reported together. Checks needed
before reading dependent values still run first.

#### CIFP export

To run only the CIFP export:

```shell
./gradlew :boogie-xml:xml-integration --tests 'org.mitre.boogie.xml.fixtures.ArincExiIntegration.writesCifpXmlAndExi' --rerun-tasks
```

This writes `cifp-2101.xml` and `cifp-2101-no-schema.exi` under `boogie-xml/build/exi/`, plus a compressed
schema-informed EXI file. When the official XSDs are available at
`boogie-xml/src/test/resources/v23_4/schemas/Records/AeroPublication.xsd`, that file is named
`cifp-2101-schema-informed.exi` and uses schema ID `urn:boogie:arinc424:23.4`.
Otherwise, the test generates JAXB schemas under `build/exi/cifp-2101-schemas/` and writes
`cifp-2101-jaxb-schema-informed.exi` with schema ID `urn:boogie:test:arinc424:jaxb:23.4`.
Both variants are decoded completely and compared with the source XML's structure.

The test helpers parse supported V19 primary records into the existing fixed-width ARINC models,
then build a v23_4 JAXB `AeroPublication`. They include airports, heliports, runways, waypoints,
navaids, procedures, airways, and controlled/restrictive airspaces. The writer works directly with
the JAXB publication. This test fixture converter omits unsupported record types, continuation records,
and records rejected by the existing model converters. Some source codes without a corresponding
mapped XML field are retained in record notes.

XML altitude values use feet, including flight levels: FL270 is `27000` with `isFlightLevel=true`.
The flight-level flag identifies the pressure reference; readers keep the numeric value in feet.

#### DAFIF export

To run only the DAFIF export:

```shell
./gradlew :boogie-xml:xml-integration --tests 'org.mitre.boogie.xml.fixtures.ArincExiIntegration.writesDafifXmlAndExi' --rerun-tasks
```

This writes `dafif-2601.xml`, `dafif-2601-no-schema.exi`, and either `dafif-2601-schema-informed.exi`
or `dafif-2601-jaxb-schema-informed.exi` under `boogie-xml/build/exi/`, using the same schema selection
as the CIFP export. Generated schemas are retained under `build/exi/dafif-2601-schemas/`.
The fixture reads the archive directly and converts the thirteen supported DAFIF 8.1 tables: `ARPT`,
`RWY`, `ADD_RWY`, `ILS`, `NAV`, `WPT`, `TRM_PAR`, `TRM_SEG`, `ATS`, `BDRY_PAR`, `BDRY`, `SUAS_PAR`, and `SUAS`.
These populate airports, runway ends, landing aids, navaids, waypoints, terminal procedures, airways,
controlled airspaces, FIR/UIRs, and restrictive airspaces in a JAXB publication.
Heliport tables (`TRMH`/`SUPPH`) and other unsupported tables are omitted. Source values without a
mapped XML field are retained in notes where applicable. These converters are test helpers.

For terminal procedures, altitude-description codes determine the constraint: `B` uses altitude 1
as the upper bound and altitude 2 as the lower bound; `C` uses altitude 2 as the lower bound.
Flight levels are converted to feet and keep their flight-level flag.

Terminal speed limits become an XML at-or-below limit in knots only when they have no altitude
qualifier and their aircraft type is absent or `A` (all aircraft). If both limits apply to all aircraft,
the lower maximum is used. Qualified limits remain in notes with their aircraft and altitude qualifiers.
Speed restrictions propagate backward on SIDs and forward on STARs and approaches according to
the DAFIF rules; holding-pattern limits apply to the hold itself. Aircraft and altitude qualifiers
remain attached to inherited restrictions. Connected transitions inherit limits common to all contributing
branches; conflicting branch-specific limits remain in the source records and are not applied universally
to a shared transition.

DAFIF airway directions are merged using a graph keyed by airway identifier and waypoint identifier/country.
Overlapping forward and reverse segments share one XML leg; one-way segments carry forward/backward
restrictions relative to the output sequence. Equal altitude limits are emitted once, while different
directional limits retain separate F/B values, including their flight-level or MSL reference.
The output order is deterministic and preserves cycle-closing edges. Disconnected sections and branches
use separate `Airway` records with the same published identifier because v23_4 represents one continuous
path per record. Direction-specific RNP, level, route type, or distance differences also remain separate
path variants. The fixture does not create connections between unrelated sections. Original DAFIF
directions and end markers remain in notes. Encoded RNP values are converted to nautical miles
(e.g., `100` becomes 10 NM and `031` becomes 0.3 NM). True courses and runway headings retain their
true-bearing flags, including headings from runway identifiers ending in `T`.

DAFIF airspaces retain source start/end coordinates, arc directions and starting bearings, circle centers
and radii, and AGL/MSL/flight-level altitude references. Altitudes `GND`/`SURFACE`, `UNLTD`, `U`, and
`BY NOTAM` become ground, unlimited, unknown, and NOTAM flags, respectively, with no numeric altitude.
Generalized boundaries use great-circle edges between the supplied approximation points.
As in the standard DAFIF assembler, gaps up to 0.1 NM are
bridged explicitly; point, annular, open, or disconnected airspaces are logged and omitted as a whole.
Cycle 2601 emits 17,956 boundaries and 18,111 special-use airspaces, omitting 35 unsupported definitions.
Special-use sectors remain separate. Source identifiers, sectors, original types and other unmatched
metadata are retained in `supplementalData` in the `urn:boogie:dafif:8.1` namespace. Boundary types without
an exact XML equivalent leave `controlledAirspaceType` unset; temporary reserved areas use
`Unspecified`, since DAFIF type T does not mean training. FIR/UIR AGL limits remain in supplemental data
because that XML record has no AGL unit field. This test fixture is not a fully schema-valid translation
of every DAFIF concept.

### Use an XML schema

```java
import org.mitre.boogie.xml.exi.ExiSchema;

var schema = ExiSchema.compile(
    "urn:example:arinc424:23.4:release-1",
    Path.of("schemas/Records/AeroPublication.xsd"));
var options = ExiOptions.schemaInformed(schema).withCompression(true);
var codec = new ExiCodec(options);
```

Compile the schema once; relative imports and includes are resolved from the root XSD. Supply the
schema matching your data. No ARINC XSD distribution is bundled with the published module.

The encoder includes EXI options and a schema ID in the header. The ID identifies the exact schema
set, including its imports; it does not embed the XSD. The decoder must have that schema registered.
The codec automatically registers its encoding schema; additional decoding schemas can be supplied
with `new ExiCodec(options, List.of(otherSchema))`. Unknown schema IDs fail instead of fetching schemas
from locations supplied by the EXI document. Streams omitting EXI options require matching options
to be configured by the caller.

Compression defaults to off and can be enabled in either schema mode. Schema-informed encoding is
non-strict by default; `.withStrict(true)` enables the stricter EXI grammar. Strict EXI is not a
replacement for complete XML Schema validation.

EXI preserves XML data according to its fidelity settings, not the original file bytes. Formatting,
comments, processing instructions, and prefix spellings are not preserved by default. Typed values
may be normalized; use `.withPreserveLexicalValues(true)` when their original spelling matters.
In non-strict mode this also preserves namespace prefixes, which lexical QName values require.
Strict EXI forbids prefix preservation, so strict mode with lexical preservation cannot encode
`xsi:type` through EXIficient. The default settings do not retain otherwise unused namespace bindings
needed by arbitrary QName-valued strings.

### Parse EXI into Boogie records

```java
var options = ExiOptions.schemaLess(); // Or schemaInformed(schema)
var modelParser = OneshotXmlModelParser.builder()
    .version(ArincXmlVersion.V23_4)
    .exiOptions(options)
    .build();
var assemblingParser = OneshotXmlParser.standardBuilder(ArincXmlVersion.V23_4)
    .exiOptions(options)
    .build();

try (InputStream exi = Files.newInputStream(Path.of("arinc424.exi"))) {
  ArincRecords records = modelParser.parseFrom(exi);
}

try (InputStream exi = Files.newInputStream(Path.of("arinc424.exi"))) {
  var records = assemblingParser.assembleFrom(exi);
}
```

Both builders default to XML. `.xml()` selects XML explicitly; `.exiOptions(options)` selects EXI.
For a codec with multiple registered schemas, pass `.readerFactory(codec::createReader)`.
Builders prepare and validate dependencies before constructing parsers.
Malformed or truncated input fails parsing; it is not returned as a successful partial result.

### Write and read JAXB objects directly

`ExiCodec.createWriter` and `createReader` expose standard StAX interfaces. For example, a generated
`AeroPublication` JAXB object can be written directly to EXI:

```java
var context = JAXBContext.newInstance(AeroPublication.class);
try (OutputStream output = Files.newOutputStream(Path.of("arinc424.exi"))) {
  var writer = codec.createWriter(output);
  try {
    context.createMarshaller().marshal(publication, writer);
  } finally {
    writer.close();
  }
}

try (InputStream input = Files.newInputStream(Path.of("arinc424.exi"))) {
  var reader = codec.createReader(input);
  try {
    AeroPublication publication = context.createUnmarshaller()
        .unmarshal(reader, AeroPublication.class).getValue();
  } finally {
    reader.close();
  }
}
```

Unmarshalling the complete publication builds a full JAXB object tree. Use the Boogie parsers above
for record-by-record processing. EXI writing accepts XML events or JAXB objects; converting
`ArincRecords` or assembled Boogie types back into a complete publication requires reverse mapping.

## Marshalling intermediate Boogie models (unfinished)

`StreamingMarshaller` writes ARINC 424 XML incrementally — sections are written as data becomes
available, and airports can be written one at a time.

This writer currently emits placeholders for records. It does not implement model serialization;
use the EXI stream or JAXB APIs above for EXI output.

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
