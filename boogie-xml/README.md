# Boogie XML

## Module overview

This model should be considered **BETA**

This module provides an APIs for interacting with ARINC 424 XML formats.
This module should be considered BETA functionality.

## Unmarshalling

StreamingUnmarshaller.java
Unmarshaller.java 

They make data, go use them!

```java
File input = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));
StreamingUnmarshaller streamer = new StreamingUnmarshaller(List.of(AeroPublication.class));
ArincRecords streamedData = streamer.apply(Files.newInputStream(input.toPath(), StandardOpenOption.READ)).orElseThrow();

Unmarshaller unmarshaller = new Unmarshaller(List.of(AeroPublication.class));
ArincRecords oneShotAllData = unmarshaller.apply(new FileInputStream(input)).get();
```

## Marshalling

Two marshallers are provided for writing `ArincRecords` back to ARINC 424 XML.

### Marshaller

Simple, all-at-once writing. All data must be available in memory upfront.

```java
ByteArrayOutputStream output = new ByteArrayOutputStream();

try (Marshaller marshaller = new Marshaller(output)) {
  ArincRecords records = ArincRecords.standard()
      .waypoints(Collections.emptySet())
      .airports(Collections.emptySet())
      .ndbNavaids(Collections.emptySet())
      .vhfNavaids(Collections.emptySet())
      .arincAirways(Collections.emptySet())
      .holdingPatterns(Collections.emptySet());

  marshaller.write(records); // One call, writes everything
}
```

### StreamingMarshaller

Incremental writing — sections are written as data becomes available, and airports can be written one at a time. This is useful for large datasets, real-time processing, or streaming from a database.

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

See `RealDifferenceExample.java` for a full runnable comparison.
