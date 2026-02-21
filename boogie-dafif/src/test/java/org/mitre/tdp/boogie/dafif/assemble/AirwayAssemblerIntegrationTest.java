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
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

@Tag("DAFIF")
@Tag("INTEGRATION")
class AirwayAssemblerIntegrationTest {

  static Collection<DafifAirTrafficSegment> atsSegments;
  static List<Airway> airways;
  static long uniqueAtsIdentifiers;

  @BeforeAll
  static void setUp() {
    EmbeddedDafifFile dafif = EmbeddedDafifFile.instance();

    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(dafif.dafifWaypoints(), dafif.dafifNavaids());
    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        List.of(), List.of(), List.of(), List.of(), List.of());
    FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(tad, fdb);
    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(fdb, fixStrategy);

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
  void testAssembledAirwayCountMatchesUniqueIdentifierDirections() {
    assertEquals(17269, airways.size(),
        "Number of assembled airways should equal the 17269 unique ATS_IDENT+DIRECTION combos in the raw data");
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
