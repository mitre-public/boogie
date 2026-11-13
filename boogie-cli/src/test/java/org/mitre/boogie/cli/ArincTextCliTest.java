package org.mitre.boogie.cli;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

import org.junit.jupiter.api.Test;

/**
 * Fixed-width ARINC 424 input, using KBOS lines from the FAA CIFP 2101 cycle (ARINC 424-18).
 */
class ArincTextCliTest {

  private static final byte[] CIFP = resource("/kbos-cifp-2101.dat");

  @Test
  void summarizesAnAirportWithItsRunwaysAndProcedures() {
    Result result = run(CIFP, "-f", "-", "-v", "V18", "-c", "Airport", "-i", "KBOS");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("KBOS — GENERAL EDWARD LAWRENCE LOGAN"), result.out()),
        () -> assertTrue(result.out().contains("Coordinates: 42.362944, -71.006389 | Elevation: 19 ft"), result.out()),
        () -> assertTrue(result.out().contains("IATA: BOS | Magnetic variation: 15° W"), result.out()),
        () -> assertTrue(result.out().contains("Runways: RW04R"), result.out()),
        () -> assertTrue(result.out().contains("Approaches (1): I04R"), result.out()),
        () -> assertEquals("", result.err())
    );
  }

  @Test
  void matchesAnAirportByIataCodeWithoutCaseSensitivity() {
    Result result = run(CIFP, "-f", "-", "-v", "18", "-c", "airport", "-i", "bos");

    assertTrue(result.out().startsWith("KBOS — "), result.out());
  }

  @Test
  void groupsProcedureLegsByTransition() {
    Result result = run(CIFP, "-f", "-", "-v", "V18", "-c", "Approach", "-i", "KBOS/I04R");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("KBOS I04R — Approach (4 legs)"), result.out()),
        () -> assertTrue(result.out().contains("Transition GOSHI (route type A)"), result.out()),
        () -> assertTrue(result.out().contains("010 IF GOSHI  alt @6000  spd -210 kt"), result.out()),
        () -> assertTrue(result.out().contains("Common route (route type I)"), result.out()),
        () -> assertTrue(
            result.out().contains("011 CF NABBO  crs 35°  dist 5 NM  alt +3000  nav IBOS 214.7°/11.9 NM"),
            result.out()
        )
    );
  }

  @Test
  void selectsRunwaysByAirportAndNavaidsByIdentifier() {
    Result runway = run(CIFP, "-f", "-", "-c", "Runway", "-i", "KBOS");
    Result vhf = run(CIFP, "-f", "-", "-c", "VhfNavaid", "-i", "BOS");

    assertAll(
        () -> assertTrue(runway.out().contains("KBOS RW04R"), runway.out()),
        () -> assertTrue(runway.out().contains("Length: 10006 ft | Width: 150 ft"), runway.out()),
        () -> assertTrue(runway.out().contains("Landing system: IBOS (category 3)"), runway.out()),
        () -> assertTrue(vhf.out().startsWith("BOS — BOSTON"), vhf.out()),
        () -> assertTrue(vhf.out().contains("Frequency: 112.70 MHz | Class: VDH"), vhf.out())
    );
  }

  @Test
  void readsGzipInputAndListsEveryFieldForOtherClasses() {
    Result result = run(gzip(CIFP), "-f", "-", "-c", "Header01");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("Header01 — FAACIFP18"), result.out()),
        () -> assertTrue(result.out().contains("dataSupplierIdent=U.S.A. DOT FAA"), result.out())
    );
  }

  @Test
  void stopsAfterTheLimit() {
    Result result = run(CIFP, "-f", "-", "-c", "Procedure", "-n", "1");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertEquals(1, result.out().lines().filter(line -> !line.startsWith(" ")).count(), result.out())
    );
  }

  @Test
  void reportsTruncatedLinesOfTheSelectedClass() {
    String text = new String(CIFP, StandardCharsets.ISO_8859_1);
    String truncated = text.lines()
        .filter(line -> line.startsWith("SUSAD"))
        .findFirst()
        .orElseThrow()
        .substring(0, 100);

    Result result = run((text + truncated + "\n").getBytes(StandardCharsets.ISO_8859_1), "-f", "-", "-c", "VhfNavaid");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("BOS — BOSTON"), result.out()),
        () -> assertTrue(result.err().contains("skipped 1 VhfNavaid line(s)"), result.err())
    );
  }

  @Test
  void reportsUnknownClassesAndVersionsAsUsageErrors() {
    Result unknownClass = run(CIFP, "-f", "-", "-c", "Bogus");
    Result unknownVersion = run(CIFP, "-f", "-", "-c", "Airport", "-v", "V99");
    Result missing = run(CIFP, "-f", "-", "-c", "Airport", "-i", "KXXX");

    assertAll(
        () -> assertEquals(2, unknownClass.status()),
        () -> assertTrue(unknownClass.err().contains("choose from Airport, Runway"), unknownClass.err()),
        () -> assertEquals(2, unknownVersion.status()),
        () -> assertTrue(unknownVersion.err().contains("V18, V18_NAV"), unknownVersion.err()),
        () -> assertEquals(0, missing.status()),
        () -> assertEquals("", missing.out()),
        () -> assertTrue(
            missing.err().contains("no matching Airport records for identifier KXXX in ARINC V22 text"),
            missing.err()
        )
    );
  }

  @Test
  void listsTextClassesForTheChosenVersion() {
    Result result = run(new byte[0], "--list-records", "-v", "V18");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().contains("Text (V18): Airport, Runway"), result.out()),
        () -> assertTrue(result.out().contains("LocalizerGlideSlope"), result.out())
    );
  }

  private static byte[] resource(String name) {
    try (InputStream input = ArincTextCliTest.class.getResourceAsStream(name)) {
      return input.readAllBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static byte[] gzip(byte[] bytes) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
      gzip.write(bytes);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return out.toByteArray();
  }

  private static Result run(byte[] input, String... arguments) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();

    int status = BoogieCli.run(
        arguments,
        new ByteArrayInputStream(input),
        new PrintStream(out, true, StandardCharsets.UTF_8),
        new PrintStream(err, true, StandardCharsets.UTF_8)
    );

    return new Result(status, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
  }

  private record Result(int status, String out, String err) {
  }
}
