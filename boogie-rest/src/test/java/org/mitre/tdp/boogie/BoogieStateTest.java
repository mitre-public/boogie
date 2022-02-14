package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;

/**
 * This class is an amalgamation of tests from the various assembler classes:
 * <ul>
 *   <li>TestFixDatabase</li>
 *   <li>TestAirportAssembler</li>
 *   <li>TestAirwayAssembler</li>
 *   <li>TestProcedureAssembler</li>
 * </ul>
 * re-implemented here as regressions in the REST API.
 */
class BoogieStateTest {

  private static BoogieState terminalState;

  private static BoogieState enrouteState;

  private static File resourceFile(String filename) {
    return new File(System.getProperty("user.dir").concat("/src/test/resources/").concat(filename));
  }

  @BeforeAll
  static void setup() {
    terminalState = BoogieState.forVersion(ArincVersion.V19);
    terminalState.accept(resourceFile("kjfk-and-friends.txt").toPath());

    enrouteState = BoogieState.forVersion(ArincVersion.V19);
    enrouteState.accept(resourceFile("j121-t222-and-friends.txt").toPath());
  }

  @Test
  void testFixes() {
    assertAll(
        "Collection of common database waypoint queries and their expected outcomes (based on real embedded data).",
        () -> assertEquals(Optional.of("AROKE"), terminalState.fixes("AROKE").stream().map(Fix::fixIdentifier).findFirst(), "Waypoint AROKE should be in the database."),
        () -> assertEquals("AROKE,CAXUN", terminalState.fixes("AROKE", "CAXUN").stream().map(Fix::fixIdentifier).collect(Collectors.joining(",")), "Both waypoints exist in the database and so both should be returned.")
    );
  }

  @Test
  void testAirports() {
    Airport airport = terminalState.airports("KJFK").stream().findFirst().orElseThrow();

    Map<String, Runway> runways = airport.runways().stream().collect(Collectors.toMap(Runway::runwayIdentifier, Function.identity()));

    assertAll(
        () -> assertEquals("KJFK", airport.airportIdentifier()),
        () -> assertEquals("K6", airport.airportRegion()),
        () -> assertEquals(-13., airport.publishedVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals(13., airport.elevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(8, airport.runways().size(), "Expected KJFK to have 8 runways."),
        () -> assertTrue(airport.runways().stream().allMatch(runway -> runway.departureRunwayEnd().isPresent()), "All runways should have departure ends."),

        () -> assertEquals("RW13R", runways.get("RW13R").runwayIdentifier()),
        () -> assertEquals(200., runways.get("RW13R").width().orElseThrow(AssertionError::new), "RW13R width"),
        () -> assertEquals(14511., runways.get("RW13R").length().orElseThrow(AssertionError::new), "RW13R length"),
        () -> assertEquals(121., runways.get("RW13R").trueCourse().orElseThrow(AssertionError::new), "RW13R true course"),

        () -> assertEquals("RW31L", runways.get("RW31L").runwayIdentifier()),
        () -> assertEquals(200., runways.get("RW31L").width().orElseThrow(AssertionError::new), "RW31L width"),
        () -> assertEquals(14511., runways.get("RW31L").length().orElseThrow(AssertionError::new), "RW31L length"),
        () -> assertEquals(301., runways.get("RW31L").trueCourse().orElseThrow(AssertionError::new), "RW31L true course")
    );
  }

  @Test
  void testAirways() {
    Airway J121 = enrouteState.airways("J121").iterator().next();

    assertAll(
        () -> assertEquals("J121", J121.airwayIdentifier()),
        () -> assertEquals("CHS|JMACK|BARTL|ISO|WEAVR|ORF|SAWED|KALDA|DUNFE|SWL|RADDS|SIE|AVALO|BRIGS", associatedFixSequence(J121.legs())),
        () -> assertEquals("", recommendedNavaidSequence(J121.legs()))
    );
  }

  @Test
  void testProcedures() {

    Collection<Procedure> procedures = terminalState.procedures("H22LZ").stream()
        .filter(procedure -> "KJFK".equals(procedure.airportIdentifier())).collect(Collectors.toSet());

    assertEquals(1, procedures.size(), "Expected the assembly of only one procedure (H22LZ) from H22LZ legs.");

    Procedure H22LZ = procedures.iterator().next();

    Map<String, Transition> transitions = H22LZ.transitions().stream()
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse("ALL"), Function.identity()));

    assertAll(
        "Assertions about the contents of our H22LZ procedure.",
        () -> assertEquals("H22LZ", H22LZ.procedureIdentifier()),
        () -> assertEquals("KJFK", H22LZ.airportIdentifier()),
        () -> assertEquals("K6", H22LZ.airportRegion()),
        () -> assertEquals(ProcedureType.APPROACH, H22LZ.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.RNP, H22LZ.requiredNavigationEquipage()),
        () -> assertEquals(3, H22LZ.transitions().size(), "Expected total transitions."),

        // check the actual transition contents...
        () -> assertEquals(TransitionType.APPROACH, transitions.get("DPK").transitionType()),
        () -> assertEquals(2, transitions.get("DPK").legs().size(), "Expected legs in DPK."),
        () -> assertEquals("IF|TF", pathTerminatorSequence(transitions.get("DPK").legs())),
        () -> assertEquals("DPK|TUSTE", associatedFixSequence(transitions.get("DPK").legs())),

        () -> assertEquals(TransitionType.COMMON, transitions.get("ALL").transitionType()),
        () -> assertEquals(6, transitions.get("ALL").legs().size(), "Expected legs in ALL."),
        () -> assertEquals("IF|TF|TF|TF|RF|TF", pathTerminatorSequence(transitions.get("ALL").legs())),
        () -> assertEquals("TUSTE|ZAKUS|JIRVA|SOGOE|HESOR|RW22L", associatedFixSequence(transitions.get("ALL").legs())),
        () -> assertEquals(Optional.of("CFBML"), transitions.get("ALL").legs().get(4).centerFix().map(Fix::fixIdentifier)),

        () -> assertEquals(TransitionType.MISSED, transitions.get("MISSED").transitionType()),
        () -> assertEquals(3, transitions.get("MISSED").legs().size(), "Expected legs in MISSED."),
        () -> assertEquals("TF|TF|HM", pathTerminatorSequence(transitions.get("MISSED").legs())),
        () -> assertEquals("WEPLA|CHANT|CHANT", associatedFixSequence(transitions.get("MISSED").legs()))
    );
  }

  private String pathTerminatorSequence(List<? extends Leg> legs) {
    return legs.stream().map(Leg::pathTerminator).map(Enum::name).collect(Collectors.joining("|"));
  }

  private String associatedFixSequence(List<? extends Leg> legs) {
    return legs.stream().map(Leg::associatedFix).filter(Optional::isPresent).map(Optional::get).map(Fix::fixIdentifier).collect(Collectors.joining("|"));
  }

  private String recommendedNavaidSequence(List<? extends Leg> legs) {
    return legs.stream().map(Leg::recommendedNavaid).filter(Optional::isPresent).map(Optional::get).map(Fix::fixIdentifier).collect(Collectors.joining("|"));
  }
}
