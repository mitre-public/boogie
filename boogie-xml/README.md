# Boogie XML

## Module overview

This module provides an APIs for interacting with ARINC 424 XML formats.
This module should be considered ALPHA functionality.

## Quick start

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
