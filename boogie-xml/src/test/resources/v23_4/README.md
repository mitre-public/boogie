# XML parser fixtures

`parser-sample.xml` is a small synthetic publication for unit tests.

`cifp-2101-no-schema.exi` is a pre-generated CIFP 2101 publication, compressed as schema-less EXI.
It is 7,149,481 bytes (6.8 MiB), tracked with Git LFS, and needs no external XML schemas.
Run `git lfs pull` after cloning if LFS objects have not been downloaded.

The source is `boogie-arinc/src/test/resources/cifp-2101.dat.gz`. The test-only
`ArincExiIntegration.writesCifpXmlAndExi` exporter translates supported V19 primary records to the
v23_4 JAXB publication. This is a converted test fixture, not an original FAA XML publication or
a complete representation of all source records. Legacy heliport pads are grouped by facility;
DME/TACAN components and disconnected airway paths may be separate XML records.
The current parser accepts 14,244 of the export's 14,308 runways; the other 64 lack a structured
`runwayIdentifier`. It retains 14,258 airport procedures and four heliport procedures in the models;
the current assembling parser emits only the airport procedures. The count assertions document these
existing limitations rather than claiming complete source-record preservation.

`OneshotCifpExiIntegrationTest` loads this file into intermediate models and assembled records and
checks fixed record counts. It never regenerates the fixture. Run it with:

```shell
./gradlew :boogie-xml:xml-integration
```

To intentionally regenerate it, follow the CIFP export instructions in the module README and copy
`build/exi/cifp-2101-no-schema.exi` back here. Review any count changes before updating expectations.
The checked-in artifact's SHA-256 is
`471ad3b2cc2d2a5aed99f8cddc19d020e59e7bb0bfe441202676b44405238272`.

`gibberish-sample.xml` is the original large synthetic publication. Its EXI artifact-generation tests
are opt-in; default unit tests load `parser-sample.xml` instead.
