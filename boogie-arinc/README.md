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
2. <b>ARINC Version 19</b>
   1. Airports
   2. Runways
   3. GlideSlopes
   4. NDB Navaids
   5. VHF Navaids
   6. Waypoints
   7. Procedures (PD/PE/PF - SID/STAR/Approach)
   8. Airways
   
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