package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.ContinuationRecordFilter;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;

class TestProcedureAssembler {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static TerminalAreaDatabase terminalAreaDatabase;
  private static FixDatabase fixDatabase;

  private static ProcedureAssembler assembler;

  @BeforeAll
  static void setup() {
    ContinuationRecordFilter continuationRecordFilter = new ContinuationRecordFilter();
    fileParser.apply(arincTestFile).stream().filter(continuationRecordFilter).forEach(testV18Consumer);

    terminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Consumer.arincAirports(),
        testV18Consumer.arincRunways(),
        testV18Consumer.arincLocalizerGlideSlopes(),
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincProcedureLegs(),
        testV18Consumer.arincGnssLandingSystems()
    );

    fixDatabase = ArincDatabaseFactory.newFixDatabase(
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincAirports()
    );

    assembler = new ProcedureAssembler(terminalAreaDatabase, fixDatabase);
  }

  @Test
  void testAssemblyOfDEEZZ5_SID() {
    Collection<ArincProcedureLeg> DEEZZ5Legs = terminalAreaDatabase.legsForProcedure("KJFK", "DEEZZ5");

    Collection<Procedure> procedures = assembler.apply(DEEZZ5Legs).collect(Collectors.toSet());

    assertEquals(1, procedures.size(), "Expected the assembly of only one procedure (DEEZZ5) from DEEZZ5 legs.");

    Procedure DEEZZ5 = procedures.iterator().next();

    Map<String, Transition> transitions = DEEZZ5.transitions().stream()
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse("ALL"), Function.identity()));

    assertAll(
        "Assertions about the contents of our DEEZ5 procedure.",
        () -> assertEquals("DEEZZ5", DEEZZ5.procedureIdentifier()),
        () -> assertEquals("KJFK", DEEZZ5.airportIdentifier()),
        () -> assertEquals("K6", DEEZZ5.airportRegion()),
        () -> assertEquals(ProcedureType.SID, DEEZZ5.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.RNAV, DEEZZ5.requiredNavigationEquipage()),
        () -> assertEquals(9, DEEZZ5.transitions().size()),

        // check tha actual transition contents...
        () -> assertEquals(TransitionType.COMMON, transitions.get("ALL").transitionType()),
        () -> assertEquals(2, transitions.get("ALL").legs().size()),
        () -> assertEquals("IF|TF", pathTerminatorSequence(transitions.get("ALL").legs())),
        () -> assertEquals("DEEZZ|HEERO", associatedFixSequence(transitions.get("ALL").legs())),

        () -> assertEquals(TransitionType.ENROUTE, transitions.get("CANDR").transitionType()),
        () -> assertEquals(3, transitions.get("CANDR").legs().size()),
        () -> assertEquals("IF|TF|TF", pathTerminatorSequence(transitions.get("CANDR").legs())),
        () -> assertEquals("HEERO|KURNL|CANDR", associatedFixSequence(transitions.get("CANDR").legs())),

        () -> assertEquals(TransitionType.RUNWAY, transitions.get("RW04B").transitionType()),
        () -> assertEquals(3, transitions.get("RW04B").legs().size()),
        () -> assertEquals("VA|VM|DF", pathTerminatorSequence(transitions.get("RW04B").legs())),
        () -> assertEquals("DEEZZ", associatedFixSequence(transitions.get("RW04B").legs()))
    );
  }

  @Test
  void testAssemblyOfH22LZ_APPROACH() {
    Collection<ArincProcedureLeg> H22LZLegs = terminalAreaDatabase.legsForProcedure("KJFK", "H22LZ");

    Collection<Procedure> procedures = assembler.apply(H22LZLegs).collect(Collectors.toSet());

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

  @Test
  void testAssemblyOfI22L_APPROACH(){
    Collection<ArincProcedureLeg> I22LLegs = terminalAreaDatabase.legsForProcedure("KJFK", "I22L");

    Collection<Procedure> procedures = assembler.apply(I22LLegs).collect(Collectors.toSet());

    assertEquals(1, procedures.size(), "Expected the assembly of only one procedure (I22L) from I22L legs.");

    Procedure I22L = procedures.iterator().next();

    Map<String, Transition> transitions = I22L.transitions().stream()
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse("ALL"), Function.identity()));

    assertAll(
        () -> assertEquals("I22L", I22L.procedureIdentifier()),
        () -> assertEquals("KJFK", I22L.airportIdentifier()),
        () -> assertEquals("K6", I22L.airportRegion()),
        () -> assertEquals(ProcedureType.APPROACH, I22L.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.CONV, I22L.requiredNavigationEquipage()),
        () -> assertEquals(5, transitions.size(), "Expected 5 total transitions for I22L."),

        () -> assertEquals(TransitionType.APPROACH, transitions.get("CIMBL").transitionType()),
        () -> assertEquals(8, transitions.get("CIMBL").legs().size(), "Expected legs in CIMBL."),
        () -> assertEquals("IF|TF|TF|TF|TF|TF|TF|TF", pathTerminatorSequence(transitions.get("CIMBL").legs())),
        () -> assertEquals("CIMBL|HAIRR|CEEGL|TAPPR|DETGY|HAUPT|LEFER|ROSLY", associatedFixSequence(transitions.get("CIMBL").legs())),

        () -> assertEquals(TransitionType.APPROACH, transitions.get("DPK").transitionType()),
        () -> assertEquals(2, transitions.get("DPK").legs().size(), "Expected legs in DPK."),
        () -> assertEquals("FC|CF", pathTerminatorSequence(transitions.get("DPK").legs())),
        () -> assertEquals("DPK|ROSLY", associatedFixSequence(transitions.get("DPK").legs())),
        () -> assertEquals("DPK|IIWY", recommendedNavaidSequence(transitions.get("DPK").legs()), "Expected the DPK navaid and the IIWY ILS as recommended navaids."),

        () -> assertEquals(TransitionType.APPROACH, transitions.get("NRTON").transitionType()),
        () -> assertEquals(6, transitions.get("NRTON").legs().size(), "Expected legs in NRTON."),
        () -> assertEquals("IF|TF|TF|TF|TF|TF", pathTerminatorSequence(transitions.get("NRTON").legs())),
        () -> assertEquals("NRTON|SAJUL|DETGY|HAUPT|LEFER|ROSLY", associatedFixSequence(transitions.get("NRTON").legs())),

        () -> assertEquals(TransitionType.COMMON, transitions.get("ALL").transitionType()),
        () -> assertEquals(3, transitions.get("ALL").legs().size(), "Expected 3 legs in the final approach."),
        () -> assertEquals("IF|CF|CF", pathTerminatorSequence(transitions.get("ALL").legs())),
        () -> assertEquals("ROSLY|ZALPO|RW22L", associatedFixSequence(transitions.get("ALL").legs())),
        () -> assertEquals("IIWY|IIWY|IIWY", recommendedNavaidSequence(transitions.get("ALL").legs()), "Expected IIWY ILS coded as recommended navaid for final approach legs."),

        () -> assertEquals(TransitionType.MISSED, transitions.get("MISSED").transitionType()),
        () -> assertEquals(3, transitions.get("MISSED").legs().size(), "Expected 3 legs in the missed approach."),
        () -> assertEquals("CA|CF|HM", pathTerminatorSequence(transitions.get("MISSED").legs())),
        () -> assertEquals("CHANT|CHANT", associatedFixSequence(transitions.get("MISSED").legs())),
        () -> assertEquals("JFK", recommendedNavaidSequence(transitions.get("MISSED").legs()), "Expected JFK as recommended navaid for CF leg of missed approach."),
        () -> assertEquals(Optional.of(Duration.ofMinutes(1)), transitions.get("MISSED").legs().get(2).holdTime(), "Expected coded hold time of 10 minutes in final HM leg.")
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

  /**
   * In implementation this could be done from {@link ArincVersion} - e.g. new ArincFileParser(ArincVersion.V19.parser());
   */
  private static final ArincFileParser fileParser = new ArincFileParser(
      new AirportSpec(),
      new AirwayLegSpec(),
      new LocalizerGlideSlopeSpec(),
      new NdbNavaidSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec()
  );

  /**
   * In implementation this could be done from the factory class {@link ArincRecordConverterFactory}.
   */
  private static final ConvertingArincRecordConsumer testV18Consumer = new ConvertingArincRecordConsumer.Builder()
      .airportDelegator(new AirportValidator())
      .airportConverter(new AirportConverter())
      .airwayLegDelegator(new AirwayLegValidator())
      .airwayLegConverter(new AirwayLegConverter())
      .localizerGlideSlopeDelegator(new LocalizerGlideSlopeValidator())
      .localizerGlideSlopeConverter(new LocalizerGlideSlopeConverter())
      .ndbNavaidDelegator(new NdbNavaidValidator())
      .ndbNavaidConverter(new NdbNavaidConverter())
      .procedureLegDelegator(new ProcedureLegValidator())
      .procedureLegConverter(new ProcedureLegConverter())
      .runwayDelegator(new RunwayValidator())
      .runwayConverter(new RunwayConverter())
      .vhfNavaidDelegator(new VhfNavaidValidator())
      .vhfNavaidConverter(new VhfNavaidConverter())
      .waypointDelegator(new WaypointValidator())
      .waypointConverter(new WaypointConverter())
      .gnssLandingSystemConverter(new GnssLandingSystemConverter())
      .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
      .build();
}
