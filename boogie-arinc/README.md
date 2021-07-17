# Boogie ARINC
[![Build Status](https://pandafood.mitre.org/plugins/servlet/wittified/build-status/TTFS-VOIC)](https://https://pandafood.mitre.org/browse/CDA-SHIM)
[![Latest Release](https://img.shields.io/badge/version-0.0.91-gre.svg)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)

## Module Overview
<p>This module within the Boogie software project provides a set of configurable and extensible parsers for ARINC424 formatted data.</p>
<p>
Currently this module supports standard parsing for multiple ARINC424 versions and a subset of the record types within those versions:

1. <b>ARINC Version 18</b>
   1. Airports
   2. Runways
   3. GlideSlopes
   4. NDB Navaids
   5. VHF Navaids
   6. Waypoints
   7. Procedures (PD/PE/PF - SID/STAR/Approach)
   8. Airways
2. <b>ARINC Version 19(a)</b>
   1. Airports
   2. Runways
   3. GlideSlopes
   4. NDB Navaids
   5. VHF Navaids
   6. Waypoints
   7. Procedures (PD/PE/PF - SID/STAR/Approach)
   8. Airways
3. <b>ARINC Version 21</b> - TODO
</p>
<p>
While the ARINC data models themselves (in the raw data) don't contain version indicators there are key <i>features</i> of the data in those models that can tip consumers off about which
version of the data they're working with. <i>Most</i> ARINC data is V18 - but there are some key differences between 18, 19a, and 21 namely:

1. From 18 -> 19a: multiple new routeTypeQualifier values were added for approach types (e.g. F, H, I, etc.).
2. From 19a -> 21: routeTypeQualifiers became allowed on SID/STAR records as opposed to only Approach records in previous versions. 

Most of the ARINC data in MITRE is <i>allegedly</i> V18 - however in actuality a lot of our data providers have taken a degree of liberty with their interpretations of what that means 
(even though there is <i>very</i> good documentation around what should be in that data). The most notable exception is CIFP - who uses the updated qualifiers (from 19a) as part of their
standard publication even though it claims to be V18.
</p>

## Using the Library
This portion will document common interaction patterns between users and the API as well as how to extend the API to parse additional record types.

### Setting up an in-memory database:

The easiest entry-point into the system comes through interacting one of the various version-specific ```ArincDatabaseFactory``` classes. These factory classes allow users to instantiate a 
new version of an ```ArincDatabase``` based through a collection of factory methods - the most standard use looks something like:
```
ArincDatabase database = ArincDatabaseFactory.newDatabaseFromFile(databaseFile);
``` 
The ```ArincDatabase``` then allows users access to a variety of query-mechanisms for producing 