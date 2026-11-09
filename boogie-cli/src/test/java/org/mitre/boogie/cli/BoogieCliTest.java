package org.mitre.boogie.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BoogieCliTest {

  @TempDir
  Path tempDirectory;

  private static final String AIRPORT = """
      <airport referenceId="APT-KTST">
        <identifier>KTST</identifier>
        <name>Parser Test Airport</name>
        <location>
          <latitude><deg>38</deg><min>54</min><sec>0</sec><hSec>0</hSec><northSouth>North</northSouth></latitude>
          <longitude><deg>77</deg><min>0</min><sec>0</sec><hSec>0</hSec><eastWest>West</eastWest></longitude>
        </location>
        <elevation>300</elevation>
        <terminalProcedures><sid><identifier>DEPART1</identifier><procedureName>Test Departure</procedureName></sid></terminalProcedures>
        <runway><identifier>RW09</identifier><runwayIdentifier><runwayNumber>9</runwayNumber></runwayIdentifier></runway>
      </airport>
      """;

  @Test
  void summarizesAirportWithoutConfusingNestedIdentifiers() {
    Result result = run(publication(AIRPORT), "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("KTST — Parser Test Airport"), result.out()),
        () -> assertTrue(result.out().contains("Coordinates: 38.900000, -77.000000 | Elevation: 300 ft"), result.out()),
        () -> assertTrue(result.out().contains("Runways: RW09"), result.out()),
        // The nested SID is listed as a procedure, never mistaken for the airport's own identifier.
        () -> assertEquals(1, result.out().lines().filter(line -> line.contains("DEPART1")).count(), result.out()),
        () -> assertTrue(result.out().contains("SIDs (1): DEPART1"), result.out()),
        () -> assertEquals("", result.err())
    );
  }

  @Test
  void readsAnInputFileWhoseNameContainsSpaces() throws Exception {
    Path input = Files.writeString(tempDirectory.resolve("airport sample.xml"), publication(AIRPORT));

    Result result = run("", "--file", input.toString(), "--record", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("KTST — Parser Test Airport"), result.out())
    );
  }

  @Test
  void convertsHundredthsOfSecondsAndSouthernWesternHemispheres() {
    String input = AIRPORT
        .replace(
            "<deg>38</deg><min>54</min><sec>0</sec><hSec>0</hSec><northSouth>North</northSouth>",
            "<deg>33</deg><min>30</min><sec>36</sec><hSec>72</hSec><northSouth>South</northSouth>"
        )
        .replace(
            "<deg>77</deg><min>0</min><sec>0</sec><hSec>0</hSec><eastWest>West</eastWest>",
            "<deg>70</deg><min>40</min><sec>30</sec><hSec>36</hSec><eastWest>West</eastWest>"
        );

    Result result = run(publication(input), "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("-33.510200, -70.675100"), result.out())
    );
  }

  @Test
  void retainsZeroAndNegativeElevations() {
    String first = AIRPORT.replace("<elevation>300</elevation>", "<elevation>0</elevation>");
    String second = AIRPORT.replace("KTST", "KLOW").replace("<elevation>300</elevation>", "<elevation>-120</elevation>");

    Result result = run(publication(first + second), "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("Elevation: 0 ft"), result.out()),
        () -> assertTrue(result.out().contains("Elevation: -120 ft"), result.out())
    );
  }

  @Test
  void displaysMissingFieldsWithoutInventingZeroes() {
    Result result = run(publication("<airport><identifier>KMIS</identifier></airport>"), "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("KMIS"), result.out()),
        () -> assertTrue(result.out().contains("Coordinates: -"), result.out()),
        () -> assertTrue(result.out().contains("Elevation: -"), result.out()),
        () -> assertTrue(result.out().contains("Runways: -"), result.out()),
        () -> assertFalse(result.out().contains("0.000000"), result.out())
    );
  }

  @Test
  void matchesRecordAndIdentifierWithoutCaseSensitivity() {
    String input = publication(AIRPORT + AIRPORT.replace("KTST", "KOTH"));

    Result result = run(input, "--file", "-", "--record", "aIrPoRt", "--id", "ktst");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("KTST"), result.out()),
        () -> assertFalse(result.out().contains("KOTH"), result.out())
    );
  }

  @Test
  void doesNotTreatAnIdentifierFilterAsASubstring() {
    Result result = run(publication(AIRPORT), "-f", "-", "-r", "Airport", "-i", "TST");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertEquals("", result.out()),
        () -> assertFalse(result.err().isBlank(), "No matches should be explained on stderr.")
    );
  }

  @Test
  void readsNamespaceQualifiedRecordsByLocalName() {
    String input = """
        <a:AeroPublication xmlns:a="urn:test:arinc">
          <a:airports><a:airport><a:identifier>KNMS</a:identifier><a:name>Namespaced Airport</a:name></a:airport></a:airports>
        </a:AeroPublication>
        """;

    Result result = run(input, "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("KNMS"), result.out()),
        () -> assertTrue(result.out().contains("Namespaced Airport"), result.out())
    );
  }

  @Test
  void supportsASelectedNestedRecordAndAGenericRecord() {
    Result runway = run(publication(AIRPORT), "-f", "-", "-r", "Runway");
    Result generic = run(
        "<AeroPublication><customRecord><identifier>CUSTOM1</identifier><name>Custom &amp; Test</name><mode>Active</mode></customRecord></AeroPublication>",
        "-f",
        "-",
        "-r",
        "customRecord"
    );

    assertAll(
        () -> assertEquals(0, runway.status()),
        () -> assertTrue(runway.out().contains("RW09"), runway.out()),
        () -> assertFalse(runway.out().contains("Parser Test Airport"), runway.out()),
        () -> assertEquals(0, generic.status()),
        () -> assertTrue(generic.out().contains("CUSTOM1"), generic.out()),
        () -> assertTrue(generic.out().contains("Custom & Test"), generic.out()),
        () -> assertTrue(generic.out().contains("Active"), generic.out())
    );
  }

  @Test
  void limitsMatchingResultsRatherThanEarlierNonmatchingRecords() {
    String input = publication(AIRPORT.replace("KTST", "KOTH") + AIRPORT + AIRPORT.replace("Parser Test Airport", "Second Matching Airport"));

    Result result = run(input, "-f", "-", "-r", "Airport", "-i", "KTST", "-n", "1");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("KTST"), result.out()),
        () -> assertFalse(result.out().contains("KOTH"), result.out()),
        () -> assertFalse(result.out().contains("Second Matching Airport"), result.out())
    );
  }

  @Test
  void failsForMalformedXmlEvenAfterEmittingACompleteRecord() {
    Result result = run("<AeroPublication><airports>" + AIRPORT, "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(1, result.status()),
        () -> assertFalse(result.err().isBlank(), "Malformed input should be explained on stderr.")
    );
  }

  @Test
  void rejectsDtdAndDoesNotExposeAnExternalEntity() throws Exception {
    Path secret = Files.writeString(tempDirectory.resolve("entity.txt"), "EXTERNAL_ENTITY_MUST_NOT_APPEAR");
    String input = "<!DOCTYPE AeroPublication [<!ENTITY external SYSTEM \"" + secret.toUri() + "\">]>"
        + "<AeroPublication><airport><identifier>KTST</identifier><name>&external;</name></airport></AeroPublication>";

    Result result = run(input, "-f", "-", "-r", "Airport");

    assertAll(
        () -> assertEquals(1, result.status()),
        () -> assertFalse(result.out().contains("EXTERNAL_ENTITY_MUST_NOT_APPEAR"), result.out()),
        () -> assertFalse(result.err().contains("EXTERNAL_ENTITY_MUST_NOT_APPEAR"), result.err()),
        () -> assertFalse(result.err().isBlank())
    );
  }

  @Test
  void distinguishesUsageErrorsFromInputErrors() {
    assertAll(
        () -> assertEquals(2, run("", "-f", "-").status()),
        () -> assertEquals(2, run("", "-r", "Airport").status()),
        () -> assertEquals(2, run("", "-f", "-", "-r", "Airport", "--unknown").status()),
        () -> assertEquals(2, run("", "-f", "-", "-r", "Airport", "--limit", "0").status()),
        () -> assertEquals(2, run("", "-f", "-", "-r", "Airport", "--limit", "-1").status()),
        () -> assertEquals(2, run("", "-f", "-", "-r", "Airport", "--limit", "one").status()),
        () -> assertEquals(2, run("", "-f", "-", "-r").status()),
        () -> assertEquals(1, run("", "-f", tempDirectory.resolve("missing.xml").toString(), "-r", "Airport").status())
    );
  }

  @Test
  void printsHelpAndRecordChoicesWithoutAnInputFile() {
    Result help = run("", "--help");
    Result records = run("", "--list-records");

    assertAll(
        () -> assertEquals(0, help.status()),
        () -> assertTrue(help.out().contains("--file"), help.out()),
        () -> assertTrue(help.out().contains("--class"), help.out()),
        () -> assertTrue(help.out().contains("--arinc-version"), help.out()),
        () -> assertEquals("", help.err()),
        () -> assertEquals(0, records.status()),
        () -> assertTrue(records.out().contains("Airport"), records.out()),
        () -> assertTrue(records.out().contains("Runway"), records.out())
    );
  }

  @Test
  void leavesCallerOwnedStandardInputOpen() {
    class TrackedInput extends ByteArrayInputStream {

      boolean closed;

      TrackedInput() {
        super(publication(AIRPORT).getBytes(StandardCharsets.UTF_8));
      }

      @Override
      public void close() {
        closed = true;
      }
    }

    TrackedInput input = new TrackedInput();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();

    int status = BoogieCli.run(
        new String[] {"-f", "-", "-r", "Airport"},
        input,
        new PrintStream(out, true, StandardCharsets.UTF_8),
        new PrintStream(err, true, StandardCharsets.UTF_8)
    );

    assertAll(
        () -> assertEquals(0, status),
        () -> assertFalse(input.closed)
    );
  }

  @Test
  void reportsOutputFailureInsteadOfSuccessfulPartialOutput() {
    PrintStream failedOutput = new PrintStream(new OutputStream() {
      @Override
      public void write(int value) throws IOException {
        throw new IOException("Output unavailable");
      }
    });
    ByteArrayOutputStream err = new ByteArrayOutputStream();

    int status = BoogieCli.run(
        new String[] {"-f", "-", "-r", "Airport"},
        new ByteArrayInputStream(publication(AIRPORT).getBytes(StandardCharsets.UTF_8)),
        failedOutput,
        new PrintStream(err, true, StandardCharsets.UTF_8)
    );

    assertAll(
        () -> assertEquals(1, status),
        () -> assertFalse(err.toString(StandardCharsets.UTF_8).isBlank())
    );
  }

  private static String publication(String airports) {
    return "<AeroPublication><airports>" + airports + "</airports></AeroPublication>";
  }

  private static Result run(String input, String... arguments) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();

    int status = BoogieCli.run(
        arguments,
        new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
        new PrintStream(out, true, StandardCharsets.UTF_8),
        new PrintStream(err, true, StandardCharsets.UTF_8)
    );

    return new Result(status, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
  }

  private record Result(int status, String out, String err) {
  }
}
