# Boogie ARINC

# Module overview
<p>This module within the Boogie software project provides a set of configurable and extensible parsers for ARINC424 formatted data.</p>

# Quick start

## Parsing a 424 file

For users who don't really care to know that much about what goes into the 424 specs + versioning and other implementation details - here's to you:
```java
ArincFileParser parser = new ArincFileParser(ArincVersion.V19.parser());

ConvertingArincRecordConsumer consumer = ConvertingArincRecordConsumerFactory.forArincVersion(ArincVersion.V19);
parser.apply(new File(myArinc424File.dat)).forEach(consumer);

// the consumer then contains the collections of various parsed 424 records - the consumer isn't the safest of classes
// but it gets the job done from a convenience perspective and its limitations are well-documented

Collection<ArincAirport> airports = consumer.arincAirports();
Collection<ArincProcedureLeg> procedureLegs = consumer.arincProcedureLegs(); // etc. etc.
```
As defined and implemented in Boogie the ```ArincVersion.V19``` pre-canned set of parsers should work reasonably well on most of the in-house datasets you'll come across whether they're
of an older version or of some of the more recent ```V21``` versions as Boogie is more focused on the <i>structure</i> of the record (with lightweight field-level validation) than on 
the exact allowed content combinations therein.

### Quick notes on what <i>is</i> explicitly checked...

See the various [validator](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/v18/AirportValidator.java?at=main) classes
within the V18 specification package. Most of them are lightweight and about what you would expect - the only heavier weight one is the ```ProcedureLegValidator``` which inspects both the
path terminator (TF, RF, AF, VA, etc.) and the field contents of the record to ensure the required information is present in the record to model how the leg should be flown (e.g. VA legs need
to have a valid specified min altitude and outbound course).

## Indexing in provided database implementations
The nature of most of the ARINC record types is to be referential towards other record types - take for example an ```ArincProcedureLeg``` record:
<br>
<img align="float: left;" height="80" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-arinc/arinc-procedure-leg-v18.png?at=refs%2Fheads%2Fmain"/>
<br>
There are three key groupings of fields we care about in there (as taken from the TDP POJO model), namely:
```java
// Fix Identification Information
private final String fixIdentifier;
private final String fixIcaoRegion;
private final SectionCode fixSectionCode;
private final String fixSubSectionCode;

// Recommended Navaid Identification Information
private final String recommendedNavaidIdentifier;
private final String recommendedNavaidIcaoRegion;
private final SectionCode recommendedNavaidSectionCode;
private final String recommendedNavaidSubSectionCode;

// Center Fix Identification Information
private final String centerFixIdentifier;
private final String centerFixIcaoRegion;
private final SectionCode centerFixSectionCode;
private final String centerFixSubSectionCode;
```
These fields serve as references to other records within the ARINC 424 database file and can be used to uniquely identify them. In general for ```ArincProcedureLeg``` records these are pointers 
to either NDB/VHF Navaids, Enroute/Terminal Waypoints, Airports, or even Runways. As this information is generally nice to have on hand when working with procedure information (among others) Boogie 
provides a collection of easy-to-instantiate and pre-configured (and relatively simple) databases with the appropriate indexing for these records set-up.

```java
FixDatabase fixDatabase = ArincDatabaseFactory.newFixDatabase(ndbNavaids, vhfNavaids, waypoints, airports);

// unique lookups (via) including the ICAO region in the query
Optional<ArincWaypoint> jmack = fixDatabase.waypoint("JMACK", "K6");
Optional<ArincNdbNavaid> dtw = fixDatabase.ndbNavaid("DTW", "K2");

// queries purely by ID (return value only when there is a single match)
Optional<ArincWaypoint> jmack = fixDatabase.waypoint("JMACK");  // etc. for other fix types
 
// queries for all matches
Collection<ArincWaypoint> l254 = fixDatabase.waypoints("JMACK", "DKUN1"); // etc. for other fix types

// the other common database instantiation is:
TerminalAreaDatabase terminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(airports, runways, localizerGlideSlopes, ndbNavaids, vhfNavaids, waypoints, procedureLegs);

// the above is an airport-indexed view of all of the listed argument data and is useful for common queries 
// about records which can be directly related to an airport
Optional<ArincLocalizerGlideSlope> rw13RLocalizerGlideSlope = terminalAreaDatabase.primaryLocalizerGlideSlopeAt("KJFK", "RW13R");

Collection<ArincProcedureLeg> rober2Legs = terminalAreaDatabase.legsForProcedure("KJFK", "ROBER2"); // etc.
```

Most of the database implementations under ```org.mitre.tdp.boogie.database``` provide similar collections of methods for accessing pre-indexed data. The ```ArincDatabaseFactory``` is the de facto 
entry point for creating most of these implementations. See the javadocs on them for further details around usage.

# What is ARINC 424?
<p>
ARINC 424 is a data format primarily used to serialize navigation data and is generally the one used to package data before it is compressed and loaded in the FMS (flight management system) 
on board an aircraft. At a high level ARINC data consists of a variety of field definitions (usable/composable across record types) - e.g. latitude/longitude (fields) that are defined 
once but may be used in airport/navaid/waypoint/etc. records.

The idea is you compose some invariant set of fields together in an ordered sequence to form a full ARINC 424 record string - a la the below:
</p>
<img align="float: left;" height="80" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-arinc/arinc-airway-v18.png?at=refs%2Fheads%2Fmain"/>
<p>
In the above, each of those traced out blocks has an associated number (e.g. 5.12, 5.13) which is a pointer to a specification for how that field should be interpreted within the ARINC ICD 
(typically) a PDF for the appropriate version of ARINC data.

Generally speaking the sequence of fields that make up a record are invariant across versions (with some known exceptions), and the set of available records is invariant in the sense that it
has <i>typically</i> (post V18) only been added to - and no record types have been removed. These record types are organized in a dictionary-like format based on their section/subsection. 
Each section acts as a high-level grouping with subsections within each section for specific kinds of records. As an example the set of high-level sections in ARINC as-of V18 are:

1. <b>A - Grid MORA</b> (Minimum Off-Route Altitude) 
2. <b>D - Navaid</b> (NDB, VHF) 
3. <b>E - Enroute</b> (Airways, Enroute Waypoints, Enroute Communications, etc.)
4. <b>H - Heliport</b> (Heliports, Helo SID/STAR/Approaches, etc.)
5. <b>T - Tables</b> (Cruising Tables, Geographical Reference Tables, etc.)
6. <b>R - Company Routes</b> (would need vendor-specific ARINC 424 data to see - not in general publications of things like LIDO/CIFP)
7. <b>P - Airport</b> (Airports, SID/STAR/Approaches, Terminal Waypoints, Runways, Gates, etc.)
8. <b>U - Airspace</b> (Restricted Airspaces, Controlled Airspaces, FIRs (Flight Information Regions), etc.)
</p>
<p>
These sections help categorize and organize data within an ARINC file - and the section/subsection can be used in conjunction with an identifier and an ICAO region (e.g. K2, etc.) to uniquely 
identify certain record types within the database (e.g. airports, waypoints, navaids).
</p>

# Current capabilities

Given the above, Boogie provides collections of these field-level specs pre-implemented with some tooling on top for composing them together as "records" and then applying those record-level 
specifications to input ARINC 424 strings to produce semi-structured content (things that look like maps). Boogie also provides converters for taking that generated semi-structured content 
and turning it into proper java data models (which should look almost identical to the ARINC 424 record definitions). These converters should work across versions so long as the layout of the 
424 records haven't changed. 

The current set of java POJO conversions are based on the V18 record <i>format</i> and primarily care about the field sequence within the records. On the whole they are generally less particular 
about the specific contents of any field and should therefore be valid targets for data from any of the following versions and record types (i.e. the extracted content of a field may change - but 
since the <i>structure</i> of the records has remained consistent the java POJOs have valid landing grounds for that info).

| Version | Airport (PA) | Runway (PG) | Localizer/GlideSlope (PI) | NDB Navaid (DB, PN) | VHF Navaid (D) | Waypoints (EA, PC) | ProcedureLegs (PD, PE, PF) | Airways (ER) |
|:-------:|:------------:|:-----------:|:-------------------------:|:-------------------:|:--------------:|:------------------:|:--------------------------:|:------------:|
| 18      |y             |y            |y                          |y                    |y               |y                   |y                           |y             |
| 19a     |y             |y            |y                          |y                    |y               |y                   |y                           |y             |
| 21      |y             |y            |y                          |y                    |y               |y                   |y                           |y             |

It should be noted that the above *is not* a complete parsing of all of the available record types within the 424 spec - and is instead a focused (high value) subset for general use in aviation 
research.

We'll go over how all of this is implemented in a later section of this README - but for now it should suffice to say this module supports standard <i>structured</i> parsing for multiple ARINC424 
versions and a subset of the record types within those versions as above.

While the ARINC data models themselves (in the raw data) don't contain version indicators there are key <i>features</i> of the data in those models that can tip consumers off about which
version of the data they're working with downstream of the more robust parsing and lightweight field-level validation Boogie provides. These are:

1. From <b>18 -> 19a</b>: multiple new routeTypeQualifier values were added for approach types (e.g. F, H, I, etc.).
2. From <b>19a -> 21</b>: routeTypeQualifiers became allowed on SID/STAR records as opposed to only Approach records in previous versions. 

<i>Most</i> ARINC data (we have in MITRE) is <i>allegedly</i> V18 - however in actuality a lot of our data providers have taken a degree of liberty with their interpretations of what that 
means over the years (even though there is <i>very</i> good documentation around what should be in that data). The most notable exception is CIFP - who uses the updated qualifiers 
(from 19a) on Approach records (and the updated approach naming conventions - i.e. RNP approaches start with an H) as part of their standard publication even though it claims to be published 
as V18.

# Deeper dives
Hopefully the Quick start was able to get you up and running relatively easily - however if you need to do more with the library this section is here to help.

## Adding supported record types
If you find that the currently supported set of record parsers doesn't meet your needs this section will cover how to extend the API for new record types.

### The RecordSpec
The high level abstraction for defining a record specification in Boogie is the [RecordSpec](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/RecordSpec.java?at=refs%2Fheads%2Fmain).
These specifications define an ordered sequence of (named) fields within a high level ARINC record along with a matcher which is used to decide whether the given specification should be applied
to a given raw text input string (substring of the overall raw text record). 

Parsers a la [ArincFileParser](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/ArincFileParser.java?at=main) are configured with 
a collection of record specifications (which don't need to cover all possible record types within a file). These specs are used to convert the raw record strings to semi-structured [ArincRecord](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/ArincRecord.java?at=main) 
objects. This approach tends to work well as people often don't care about every record type (see TDP) and only having to implement parsing for a subset of records of interest is convenient.

### Field specifications

The above RecordSpecs are composed of sequences of well-defined ARINC field specifications. As a rule of thumb most of the field-level specifications try to be robust to potentially bad input data 
and generally return nothing when the input value doesn't meet the spec's expectations. While there is value in expressing specific errors when field contents break the specification it's generally 
the case that <i>most</i> input data sources take enough liberties with standard specs that your parser is more likely to blow up than not if you're overly particular.

As such Boogie has generally chosen to fast-reject bad inputs fields - returning empty optionals when specific parsers are applied to out-of-spec data - and then layering on more advanced logic to 
accept/reject the more structured/parsed record contents using the more advanced rules based on compositions of fields being present (e.g. using PathTerminator types to push expectations on which leg 
fields must be present for a record to be considered "valid").

In general the requirements for adding a new field specification to the library are in the [FieldSpec](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/FieldSpec.java?at=TDP-5508-boogie-arinc-refactor) 
interface but re-iterated here are:

1. The code of the field's specification in the appropriate ARINC ICD.
2. The length of the field in a standard record (e.g. 4, 10, etc. characters).
3. An implementation for converting the string (containing the subsection of the raw record representing that field) to the appropriate "parsed" output type.

Once you have the above specified you can simply add it to the required/appropriate higher-level record specification.

### Rules of thumb

Ideally most record/field specifications should automatically reject data that aren't to spec. Most of the publicly available 424 data out there *isn't* exactly to spec and so it's important 
parsers converting the raw records -> semi-structured data are robust to potentially bad/non-standard input.