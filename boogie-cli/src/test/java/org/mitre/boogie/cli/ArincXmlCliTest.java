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
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * ARINC 424 XML input, using KBOS and its referenced records converted from the FAA CIFP 2101 cycle. The text fixture holds
 * the same cycle's fixed-width KBOS lines, so shared records must print identically in both formats.
 */
class ArincXmlCliTest {

  private static final byte[] XML = resource("/kbos-cifp-2101.xml");

  private static final byte[] TEXT = resource("/kbos-cifp-2101.dat");

  @Test
  void summarizesAnAirportWithItsRunwaysAndProcedures() {
    Result result = run(XML, "-f", "-", "-c", "Airport", "-i", "bos");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("KBOS — GENERAL EDWARD LAWRENCE LOGAN"), result.out()),
        () -> assertTrue(result.out().contains("ICAO region: K6 | IATA: BOS | Magnetic variation: 15° W"
            + " | Longest runway: 10000 ft | Transition altitude: 18000 ft"), result.out()),
        () -> assertTrue(result.out().contains("Runways: RW04L, RW04R, RW09, RW14,"), result.out()),
        () -> assertTrue(result.out().contains("SIDs (9): BLZZR4, BRUWN5,"), result.out()),
        () -> assertTrue(result.out().contains("STARs (6): GDM4,"), result.out()),
        () -> assertTrue(result.out().contains("Approaches (17): I04R,"), result.out()),
        () -> assertEquals("", result.err())
    );
  }

  @Test
  void qualifiesNestedRecordsWithTheirAirport() {
    Result runways = run(XML, "-f", "-", "-c", "Runway", "-i", "KBOS");
    Result runway = run(XML, "-f", "-", "-c", "Runway", "-i", "kbos/rw04r");

    assertAll(
        () -> assertEquals(12, headings(runways).size(), runways.out()),
        () -> assertEquals(List.of("KBOS RW04R"), headings(runway), runway.out()),
        () -> assertTrue(runway.out().contains("Length: 10006 ft | Width: 150 ft | Magnetic bearing: 35°"
            + " | Displaced threshold: 1155 ft | TCH: 51 ft"), runway.out())
    );
  }

  @Test
  void showsProcedureLegsBySection() {
    Result result = run(XML, "-f", "-", "-c", "Approach", "-i", "KBOS/I04R");

    assertAll(
        () -> assertEquals(0, result.status()),
        () -> assertTrue(result.out().startsWith("KBOS I04R — Approach (8 legs)"), result.out()),
        () -> assertTrue(result.out().contains("Approach type: Ils"), result.out()),
        () -> assertTrue(result.out().lines().anyMatch("  Transition GOSHI"::equals), result.out()),
        () -> assertTrue(result.out().contains("Final approach (glide slope intercept 1700 ft)"), result.out()),
        () -> assertTrue(result.out().contains("030 CF RW04R  crs 35°  dist 5.1 NM  alt @69  vpa -3°"
            + "  nav IBOS 214.7°/1.8 NM"), result.out()),
        () -> assertTrue(result.out().contains("Missed approach"), result.out()),
        () -> assertTrue(result.out().contains("050 HM WAXEN  turn L  crs 210°  time 1 min  alt +3000"), result.out())
    );
  }

  @Test
  void formatsFlightLevelsAndAltitudeWindows() {
    Result result = run(XML, "-f", "-", "-c", "Star", "-i", "KBOS/JFUND2");

    assertAll(
        () -> assertTrue(result.out().contains("030 TF MNSTA  alt +FL240"), result.out()),
        () -> assertTrue(result.out().contains("020 TF AUTUM  alt +11000/-12000  spd @270 kt"), result.out()),
        () -> assertTrue(result.out().lines().anyMatch("  Runway transition RW04B"::equals), result.out())
    );
  }

  @Test
  void selectsProceduresOfEveryKindAndWaypointsOfEveryArea() {
    Result procedures = run(XML, "-f", "-", "-c", "Procedure", "-i", "KBOS");
    Result terminal = run(XML, "-f", "-", "-c", "Waypoint", "-i", "KBOS");
    Result goshi = run(XML, "-f", "-", "-c", "Waypoint", "-i", "GOSHI");
    Result enroute = run(XML, "-f", "-", "-c", "Waypoint", "-i", "AJJAY");

    assertAll(
        () -> assertEquals(9 + 6 + 17, headings(procedures).size(), procedures.out()),
        () -> assertEquals(150, headings(terminal).size(), terminal.out()),
        () -> assertTrue(goshi.out().contains("Area: terminal KBOS | Usage: terminal"), goshi.out()),
        () -> assertEquals(List.of("AJJAY"), headings(enroute), "A name repeating the identifier is not shown."),
        () -> assertTrue(enroute.out().contains("Area: enroute | Usage: high"), enroute.out())
    );
  }

  @Test
  void distinguishesCollocatedNavaidsByType() {
    Result result = run(XML, "-f", "-", "-c", "VhfNavaid", "-i", "ALB");

    assertAll(
        () -> assertEquals(2, headings(result).size(), result.out()),
        () -> assertTrue(result.out().contains("Frequency: 115.30 MHz | Type: Vor"), result.out()),
        () -> assertTrue(result.out().contains("Frequency: 115.30 MHz | Type: Tacan"), result.out())
    );
  }

  @Test
  void printsSharedRecordsIdenticallyInBothFormats() {
    Result xmlRunway = run(XML, "-f", "-", "-c", "Runway", "-i", "KBOS/RW04R");
    Result textRunway = run(TEXT, "-f", "-", "-v", "V18", "-c", "Runway", "-i", "KBOS/RW04R");
    Result xmlApproach = run(XML, "-f", "-", "-c", "Approach", "-i", "KBOS/I04R");
    Result textApproach = run(TEXT, "-f", "-", "-v", "V18", "-c", "Approach", "-i", "KBOS/I04R");

    // The text fixture is ARINC 424-18; parsed as V22 it would lose the runway TCH and the J altitude descriptor.
    // It holds four I04R legs. The XML moves the glide slope intercept from the leg's J altitude
    // descriptor to its final approach section, so that leg is the one expected difference.
    List<String> textLegs = textApproach.out().lines()
        .filter(line -> line.matches(" {4}\\d{3} .*") && !line.contains("alt J"))
        .toList();

    assertAll(
        () -> assertEquals(3, textLegs.size(), textApproach.out()),
        () -> assertTrue(xmlApproach.out().lines().toList().containsAll(textLegs),
            () -> "XML:\n" + xmlApproach.out() + "\nText:\n" + textApproach.out()),
        () -> assertEquals(textRunway.out().lines().limit(3).toList(), xmlRunway.out().lines().toList())
    );
  }

  @Test
  void keepsGenericSummariesForOtherElements() {
    Result result = run(XML, "-f", "-", "-c", "Localizer", "-i", "KBOS/IBOS");

    assertAll(
        () -> assertEquals(List.of("KBOS IBOS"), headings(result), result.out()),
        () -> assertTrue(result.out().contains("category=IlsMlsGlsCat3"), result.out()),
        () -> assertFalse(result.err().contains("no matching"), result.err())
    );
  }

  private static List<String> headings(Result result) {
    return result.out().lines().filter(line -> !line.isEmpty() && !line.startsWith(" ")).toList();
  }

  private static byte[] resource(String name) {
    try (InputStream input = ArincXmlCliTest.class.getResourceAsStream(name)) {
      return input.readAllBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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
