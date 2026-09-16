package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

@Tag("DAFIF")
@Tag("INTEGRATION")
class AirwayAssemblerIntegrationTest {

  static Collection<DafifAirTrafficSegment> atsSegments;
  static List<Airway> airways;
  static long uniqueAtsIdentifiers;
  static AirwayAssembler<Airway> assembler;

  @BeforeAll
  static void setUp() {
    EmbeddedDafifFile dafif = EmbeddedDafifFile.instance();

    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(dafif.dafifWaypoints(), dafif.dafifNavaids());
    assembler = AirwayAssembler.standard(fdb, FixAssemblyStrategy.standard());

    atsSegments = dafif.dafifAts();
    uniqueAtsIdentifiers = atsSegments.stream()
        .map(seg -> seg.atsIdentifier() + "|" + seg.atsRouteDirection())
        .distinct()
        .count();
    airways = assembler.assemble(atsSegments).collect(Collectors.toList());
  }

  @Test
  void testRawSegmentCount() {
    assertEquals(156666, atsSegments.size(), "Parsed ATS segment count should match DAFIFT/ATS/ATS.TXT data row count");
  }

  @Test
  void testUniqueAirwayDirectionCount() {
    assertEquals(17269, uniqueAtsIdentifiers, "Unique ATS_IDENT+DIRECTION combos should match raw DAFIFT/ATS/ATS.TXT");
  }

  @Test
  void testContinuousSectionsPreserveEverySourceSegment() {
    assertAll(
        () -> assertTrue(airways.size() > uniqueAtsIdentifiers, "Disconnected direction groups produce multiple sections"),
        () -> assertEquals(atsSegments.size(), airways.stream().mapToLong(airway -> airway.legs().size() - 1).sum(),
            "Every source segment contributes one edge plus one starting fix per section"),
        () -> assertTrue(airways.stream().allMatch(airway -> airway.legs().get(0).pathTerminator() == PathTerminator.IF))
    );
  }

  @Test
  void testA103SectionsRetainPublishedEndpointsAndDistances() {
    var source = atsSegments.stream().filter(segment -> "A103".equals(segment.atsIdentifier())
        && "E".equals(segment.atsRouteDirection())
        && (segment.atsRouteSequenceNumber() == 60 || segment.atsRouteSequenceNumber() == 210)).toList();
    var sections = assembler.assemble(source).toList();

    assertAll(
        () -> assertEquals(2, source.size()),
        () -> assertEquals(List.of(List.of("OKTAB", "PINAX"), List.of("RORTA", "ASBUG")),
            sections.stream().map(airway -> airway.legs().stream()
                .map(leg -> leg.associatedFix().orElseThrow()).map(Fix::fixIdentifier).toList()).toList()),
        () -> assertEquals(List.of(57.0, 5.9), sections.stream()
            .map(airway -> airway.legs().get(1).routeDistance().orElseThrow()).toList())
    );
  }

  @Test
  void testAllAirwaysHaveLegs() {
    assertAll(airways.stream()
        .<Executable>map(a -> () -> assertFalse(a.legs().isEmpty(),
            "Airway " + a.airwayIdentifier() + " should have at least one leg"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAllAirwaysHaveAtLeastTwoLegs() {
    assertAll(airways.stream()
        .<Executable>map(a -> () -> assertTrue(a.legs().size() >= 2,
            "Airway " + a.airwayIdentifier() + " should have at least 2 legs (start + 1 segment), got " + a.legs().size()))
        .collect(Collectors.toList()));
  }

  @Test
  void testAssembledAirwayIdentifiersCoverAllRawIdentifiers() {
    Set<String> rawIdentifiers = atsSegments.stream()
        .map(DafifAirTrafficSegment::atsIdentifier)
        .collect(Collectors.toSet());
    Set<String> assembledIdentifiers = airways.stream()
        .map(Airway::airwayIdentifier)
        .collect(Collectors.toSet());
    assertEquals(rawIdentifiers, assembledIdentifiers,
        "Assembled airway identifiers should exactly match the set of unique atsIdentifiers in raw data");
  }

  @Test
  void testAllLegsHaveAssociatedFix() {
    long totalLegs = airways.stream().mapToLong(a -> a.legs().size()).sum();
    long legsWithFix = airways.stream()
        .flatMap(a -> a.legs().stream())
        .filter(l -> l.associatedFix().isPresent())
        .count();
    long legsMissingFix = totalLegs - legsWithFix;

    System.out.println("Total legs: " + totalLegs + ", with fix: " + legsWithFix + ", missing fix: " + legsMissingFix);
    assertEquals(totalLegs, legsWithFix, legsMissingFix + " legs are missing an associated fix");
  }
}
