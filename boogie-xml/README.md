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

To generate a compressed, schema-less EXI file from `gibberish-sample.xml`, run from the repository
root:

```shell
./gradlew :boogie-xml:test --tests 'org.mitre.boogie.xml.OneshotExiParserTest.writesGibberishSampleExi' --rerun-tasks
```

The test writes `boogie-xml/build/exi/gibberish-sample-no-schema.exi`. The file remains after the test until the
module's build directory is cleaned.

To generate larger files from the test resource `cifp-2101.dat.gz`, run:

```shell
./gradlew :boogie-xml:test --tests 'org.mitre.boogie.xml.OneshotExiParserTest.writesCifpXmlAndExi' --rerun-tasks
```

This writes `cifp-2101.xml`, `cifp-2101-no-schema.exi`, and `cifp-2101-schema-informed.exi` under
`boogie-xml/build/exi/`. Both EXI files use compression; the schema-informed file uses the v23_4 XSDs.

The test helpers parse supported V19 primary records into the existing fixed-width ARINC models,
then build a v23_4 JAXB `AeroPublication`. They include airports, heliports, runways, waypoints,
navaids, procedures, airways, and controlled/restrictive airspaces. The writer works directly with
the JAXB publication. This test fixture converter omits unsupported record types, continuation records,
and records rejected by the existing model converters. Some source codes without a corresponding
mapped XML field are retained in record notes.

XML altitude values use feet, including flight levels: FL270 is `27000` with `isFlightLevel=true`.
The flight-level flag identifies the pressure reference; readers keep the numeric value in feet.

To generate the DAFIF fixture from `boogie-xml/src/test/resources/DAFIF8_1_2601.zip`, run:

```shell
./gradlew :boogie-xml:test --tests 'org.mitre.boogie.xml.OneshotExiParserTest.writesDafifXmlAndExi' --rerun-tasks
```

This writes `dafif-2601.xml`, `dafif-2601-no-schema.exi`, and `dafif-2601-schema-informed.exi` under
`boogie-xml/build/exi/`. Both EXI files use compression; the schema-informed file uses the v23_4 XSDs.
The fixture reads the archive directly and converts the nine supported DAFIF 8.1 tables: `ARPT`,
`RWY`, `ADD_RWY`, `ILS`, `NAV`, `WPT`, `TRM_PAR`, `TRM_SEG`, and `ATS`. These populate airports,
runway ends, landing aids, navaids, waypoints, terminal procedures, and airways in a JAXB publication.
Heliport tables (`TRMH`/`SUPPH`) and other unsupported tables are omitted. Source values without a
mapped XML field are retained in notes where applicable. These converters are test helpers.

DAFIF airway directions are merged using a graph keyed by airway identifier and waypoint identifier/country.
Overlapping forward and reverse segments share one XML leg; one-way segments carry forward/backward
restrictions relative to the output sequence. Equal altitude limits are emitted once, while different
directional limits retain separate F/B values, including their flight-level or MSL reference.
The output order is deterministic and preserves cycle-closing edges. Disconnected sections and branches
use separate `Airway` records with the same published identifier because v23_4 represents one continuous
path per record. Direction-specific RNP, level, route type, or distance differences also remain separate
path variants. The fixture does not create connections between unrelated sections. Original DAFIF
directions and end markers remain in notes.

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
