# Boogie CLI

Print compact views of ARINC 424 records from the command line, from either ARINC 424 XML
or legacy fixed-width (132-column) text:

```shell
boogie -f file.xml -c Airport -i KBOS
boogie -f cifp-2101.dat -v V18 -c Airport -i KBOS
```

```text
KBOS — GENERAL EDWARD LAWRENCE LOGAN
  Coordinates: 42.362944, -71.006389 | Elevation: 19 ft
  ICAO region: K6 | IATA: BOS | Magnetic variation: 15° W | Longest runway: 10000 ft | Transition altitude: 18000 ft
  Runways: RW04L, RW04R, RW09, RW14, RW15L, RW15R, RW22L, RW22R, RW27, RW32, RW33L, RW33R
  SIDs (9): BLZZR4, BRUWN5, CELTK5, HYLND5, LBSTA6, PATSS5, REVSS4, SSOXS5, WYLYY3
  STARs (6): GDM4, JFUND2, OOSHN5, ORW7, ROBUC3, WOONS2
  Approaches (17): I04R, I15R, I22L, I27, I33L, L04R, L15R, L22L, L27, L33L, R04R, R15R, R22L, R27, R32, R33L, VDM-A
```

Procedures and airways show one line per leg:

```text
$ boogie -f cifp-2101.dat -v V18 -c Approach -i KBOS/I04R
KBOS I04R — Approach (8 legs)
  Transition GOSHI (route type A)
    010 IF GOSHI  alt @6000  spd -210 kt
    020 TF WINNI  alt +4000
  Common route (route type I)
    010 IF WINNI  alt J4000/1700  nav IBOS 214.7°/16.9 NM
    011 CF NABBO  crs 35°  dist 5 NM  alt +3000  nav IBOS 214.7°/11.9 NM
    ...
```

Coordinates are latitude, longitude in decimal degrees, rounded to six decimal places.
Missing values appear as `-`.

## Build and run

Java 17 or later is required. From the repository root:

```shell
./gradlew :boogie-cli:installDist
export PATH="$PWD/boogie-cli/build/install/boogie/bin:$PATH"
boogie --help
```

To install elsewhere, copy the entire `boogie-cli/build/install/boogie` directory;
the launcher needs the adjacent `lib` directory. Create distributable archives with
`./gradlew :boogie-cli:distZip :boogie-cli:distTar`.

## Options

| Option | Meaning |
| --- | --- |
| `-f`, `--file` | Required input file; use `-` for standard input. XML or text is detected from the content, and gzip input is decompressed. |
| `-c`, `--class` | Required record class, case-insensitive. `-r`/`--record` is accepted as an alias. |
| `-i`, `--id` | Exact, case-insensitive identifier match (see below). |
| `-v`, `--arinc-version` | ARINC 424 version used to parse text: `V18`–`V22`, the `_NAV`/`_AIRSPACE` variants, or just `18`. Defaults to `V22`; see [Reading behavior](#reading-behavior). Ignored for XML. |
| `-n`, `--limit` | Positive maximum number of matches to print. |
| `--list-records` | List the XML classes and the text classes for the `-v` version. |
| `-h`, `--help` | Show usage. |

```shell
boogie -f cifp.dat -v 18 -c Runway -i KBOS          # all KBOS runways
boogie -f cifp.dat -v 18 -c Runway -i KBOS/RW04R    # one runway
boogie -f cifp.dat -v 18 -c Sid -i KBOS             # every KBOS SID, leg by leg
boogie -f lido.dat.gz -c Airway -i J75
boogie -f lido.dat.gz -c Airport -i LHR             # IATA codes work for airports
boogie -f lido.dat.gz -c FirUir -n 3 | less
```

## Classes

Both formats have tailored views for `Airport`, `Runway`, `Waypoint`, `Heliport`, `Ndb`,
`VhfNavaid`, `Airway`, `Sid`, `Star`, `Approach`, `Procedure` (all three), and
`HoldingPattern` (text only). The views print the same record the same way in either format, so
XML and text from the same cycle can be compared line by line. Remaining differences come from
the data: XML splits an approach's final and missed segments, puts the glide slope intercept on
the final approach rather than in a `J` altitude descriptor, and keeps a collocated VOR and DME
as separate `vhfNavaid` records, distinguished by `Type`.

In XML, `Waypoint` covers `waypoint` and `terminalWaypoint`, `Ndb` covers `ndb` and
`terminalNdb`, and `Localizer`/`Ils` selects `localizerGlideslope`. Any other element name gets
a generic summary of its direct fields.

In text, every other record type in the chosen version is available by its Boogie spec name,
e.g. `LocalizerGlideSlope` (or `Localizer`/`Ils`), `GnssLandingSystem` (`Gls`), `Helipad`,
`FirUir`, `ControlledAirspace`, `RestrictiveAirspace`, `AirportPrimaryExtension` and `Header01`
(`Header`). These print every populated field; values the version's field spec cannot decode
are shown raw and marked `(unparsed)`.

`-i` matches a record's own identifier, its airport when it has one (in XML, the enclosing
`airport` or `heliport`), or `AIRPORT/IDENTIFIER`. Airports also match their IATA code. For
example, `-c Waypoint -i KBOS` lists KBOS terminal waypoints, and `-c Approach -i I04R` lists
I04R at every airport.

## Reading behavior

Text is streamed and only primary records of the selected class are parsed, using the
`RecordSpec`s that `boogie-arinc` defines for the chosen version. The ARINC version cannot
be read from the file itself, so choose it with `-v`. `V22` is the default. Records still
parse under a newer version, but fields whose layout changed are silently lost: read the FAA
CIFP (ARINC 424-18) as V22 and runway threshold crossing heights and some procedure altitude
descriptors disappear, so pass `-v V18` for it. Airway and procedure legs are grouped while they are contiguous, as they are
in sorted ARINC 424 files. An airport view waits until the end of the input, because its
runway and procedure records follow it. Selected-class lines that are not 132 characters
long are skipped and counted on standard error.

XML is read with the JDK's StAX parser rather than `boogie-xml`, without XSD validation or
reference resolution, buffering one selected record subtree at a time, so incomplete or invalid
records are still shown. The views follow the repository's v23_4 model. EXI and ZIP input are
not supported.

Record output goes to standard output; diagnostics go to standard error. Exit codes are `0`
for success (including no matches), `1` for input/output or XML errors, and `2` for invalid
arguments, including a class the chosen ARINC version does not define.

Run the CLI tests with `./gradlew :boogie-cli:test`; they are also part of the repository-wide
`./gradlew unit` task. The module uses the shared `java-conventions` build, but it is not published
to Maven Central or any other Maven repository. Distribute it with `installDist`, `distZip` or `distTar`.

`src/test/resources/kbos-cifp-2101.xml` contains KBOS and its related data from `boogie-xml/src/test/resources/cifp-2101.dat.gz`,
converted with `CifpXmlFixture` to the v23_4 JAXB model. It includes all supported KBOS terminal
records, airport-associated VHF navaids and controlled airspaces, and the transitive references
to enroute waypoints and navaid components. It contains 12 runways, 9 SIDs, 6 STARs, 17 approaches
(734 procedure legs), 150 terminal and 34 enroute waypoints, 25 VHF navaid components, 5 localizers,
and 4 controlled airspaces (10 boundary segments). All 838 references resolve to its 259 unique IDs.
JAXB and `OneshotXmlModelParser` round trips were checked when generating it. Like the full CIFP
export, this fixture omits unsupported record types and continuation records. Its validity dates
are January 28 through February 25, 2021.
