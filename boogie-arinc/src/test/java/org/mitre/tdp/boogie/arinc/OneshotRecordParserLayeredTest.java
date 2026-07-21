package org.mitre.tdp.boogie.arinc;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;

/**
 * {@link OneshotRecordParser#assembleFrom(List)}: several prioritized 424 sources assembled as one model — earlier sources win
 * per record identity, records unique to any source are retained, and cross-source references resolve because the merge happens
 * before assembly.
 */
class OneshotRecordParserLayeredTest {

  private static final Path ARINC_TEST_FILE =
      Path.of(System.getProperty("user.dir"), "src/test/resources/kjfk-and-friends.txt");

  /** The KJFK ILS 04L final rides AROKE -> KRSTL -> RW04L; AROKE is a KJFK terminal (PC) waypoint at N40. */
  private static final String AROKE_PREFIX = "SUSAP KJFKK6CAROKE";
  private static final String I04L_PREFIX = "SUSAP KJFKK6FI04L";

  private static List<String> base;
  private static String aroke;

  @BeforeAll
  static void load() throws IOException {
    base = Files.readAllLines(ARINC_TEST_FILE);
    aroke = base.stream().filter(line -> line.startsWith(AROKE_PREFIX)).findFirst().orElseThrow();
  }

  private static InputStream streamOf(List<String> lines) {
    return new ByteArrayInputStream(String.join("\n", lines).getBytes(StandardCharsets.UTF_8));
  }

  private static OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> assemble(
      List<InputStream> orderedSources) {
    return OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(orderedSources);
  }

  @Test
  void earlierSourceWinsWhenRecordIdentityCollides() {
    // the patch redefines the same waypoint identity (same airport/region/section/ident) a degree north
    String patched = aroke.replace("N40282056", "N41282056");

    List<Fix> arokes = assemble(List.of(streamOf(List.of(patched)), streamOf(base)))
        .fixes().stream().filter(fix -> "AROKE".equals(fix.fixIdentifier())).toList();

    assertAll(
        () -> assertEquals(1, arokes.size(), "one AROKE survives the merge, not both versions"),
        () -> assertEquals(41, (int) arokes.get(0).latitude(), "the earlier (patch) source's content wins")
    );
  }

  @Test
  void laterSourceIsShadowedNotMangled() {
    // same two sources, base first: base's AROKE wins and the patch's version is dropped entirely
    String patched = aroke.replace("N40282056", "N41282056");

    List<Fix> arokes = assemble(List.of(streamOf(base), streamOf(List.of(patched))))
        .fixes().stream().filter(fix -> "AROKE".equals(fix.fixIdentifier())).toList();

    assertAll(
        () -> assertEquals(1, arokes.size()),
        () -> assertEquals(40, (int) arokes.get(0).latitude(), "base wins when it is the earlier source")
    );
  }

  @Test
  void recordsUniqueToAnySourceAreAllRetained() {
    // an additive overlay: a waypoint identity the base does not define (the LBG-SPA.dat use case —
    // a decommissioned navaid still filed in routes, re-added on top of the current cycle)
    String added = aroke.replace("AROKE", "AROKX");

    var merged = assemble(List.of(streamOf(List.of(added)), streamOf(base)));
    Set<String> idents = merged.fixes().stream().map(Fix::fixIdentifier).collect(toSet());

    assertAll(
        () -> assertTrue(idents.contains("AROKX"), "overlay-only record present"),
        () -> assertTrue(idents.contains("AROKE"), "base records all present")
    );
  }

  @Test
  void crossSourceReferencesResolveBecauseMergeHappensBeforeAssembly() {
    // split the ILS 04L legs into their own source: its legs reference fixes (AROKE, KRSTL), the
    // localizer, and the runway (RW04L) that only the base source defines — assembled per-source the
    // procedure would come out with dangling references; merged-then-assembled it is complete
    List<String> approachOnly = base.stream().filter(line -> line.startsWith(I04L_PREFIX)).toList();
    List<String> baseWithoutApproach = base.stream().filter(line -> !line.startsWith(I04L_PREFIX)).toList();
    assertTrue(!approachOnly.isEmpty(), "fixture still carries the I04L approach");

    var merged = assemble(List.of(streamOf(approachOnly), streamOf(baseWithoutApproach)));

    Procedure i04l = merged.procedures().stream()
        .filter(procedure -> "I04L".equals(procedure.procedureIdentifier())
            && "KJFK".equals(procedure.airportIdentifier()))
        .findFirst().orElseThrow();

    Set<String> boundFixes = i04l.transitions().stream()
        .map(Transition::legs).flatMap(List::stream)
        .map(Leg::associatedFix).flatMap(Optional::stream)
        .map(Fix::fixIdentifier)
        .collect(toSet());

    assertTrue(boundFixes.containsAll(Set.of("AROKE", "KRSTL", "RW04L")),
        "approach legs bind fixes and the runway defined only in the base source, got: " + boundFixes);
  }

  @Test
  void singleSourceListMatchesSingleStreamAssembly() throws IOException {
    var single = OneshotRecordParser.standard(ArincVersion.V19)
        .assembleFrom(Files.newInputStream(ARINC_TEST_FILE));
    var listOfOne = assemble(List.of(streamOf(base)));

    assertAll(
        () -> assertEquals(single.airports().size(), listOfOne.airports().size(), "Airports"),
        () -> assertEquals(single.fixes().size(), listOfOne.fixes().size(), "Fixes"),
        () -> assertEquals(single.airways().size(), listOfOne.airways().size(), "Airways"),
        () -> assertEquals(single.procedures().size(), listOfOne.procedures().size(), "Procedures"),
        () -> assertEquals(single.firUirs().size(), listOfOne.firUirs().size(), "FIR-UIRs"),
        () -> assertEquals(single.heliports().size(), listOfOne.heliports().size(), "Heliports")
    );
  }

  @Test
  void emptySourceListYieldsEmptyClientRecords() {
    var merged = assemble(List.of());

    assertAll(
        () -> assertEquals(0, merged.airports().size()),
        () -> assertEquals(0, merged.fixes().size()),
        () -> assertEquals(0, merged.procedures().size())
    );
  }
}
