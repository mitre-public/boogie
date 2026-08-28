package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestV22ProcedureAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kbos-supp22.txt"));
  private static final File gqnoFile = new File(System.getProperty("user.dir").concat("/src/test/resources/gqno-and-friends.txt"));
  private static final File arincTestFile2 = new File(System.getProperty("user.dir").concat("/src/test/resources/kjra_9vak5-and-friends"));

  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase;
  private static ArincFixDatabase arincFixDatabase;

  private static ProcedureAssembler<Procedure> assembler;

  private static final ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V22.specs()));

  @BeforeAll
  static void setup() {
    fileParser.parseAll(arincTestFile).forEach(consumer);
    fileParser.parseAll(gqnoFile).forEach(consumer);
    fileParser.parseAll(arincTestFile2).forEach(consumer);

    arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        consumer.arincAirports(),
        consumer.arincRunways(),
        consumer.arincLocalizerGlideSlopes(),
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincProcedureLegs(),
        consumer.arincGnssLandingSystems(),
        consumer.arincHelipads(),
        consumer.arincHeliports()
    );

    arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincAirports(),
        consumer.arincHoldingPatterns(),
        consumer.arincHeliports()
    );

    assembler = ProcedureAssembler.standard(arincTerminalAreaDatabase, arincFixDatabase);
  }

  @Test
  void testSidStarApproach() {
    List<Procedure> procedures = assembler.assemble(consumer.arincProcedureLegs()).toList();
    Procedure blzzr6 = procedures.stream().filter(i -> i.procedureIdentifier().equals("BLZZR6")).findFirst().orElseThrow();
    Procedure jfund2 = procedures.stream().filter(i -> i.procedureIdentifier().equals("JFUND2")).findFirst().orElseThrow();
    Procedure r32 = procedures.stream().filter(i -> i.procedureIdentifier().equals("R32")).findFirst().orElseThrow();
    Procedure orw7 = procedures.stream().filter(i -> i.procedureIdentifier().equals("ORW7")).findFirst().orElseThrow();
    Procedure i27 = procedures.stream().filter(i -> i.procedureIdentifier().equals("I27")).findFirst().orElseThrow();
    Procedure r33lx = procedures.stream().filter(i -> i.procedureIdentifier().equals("R33LX")).findFirst().orElseThrow();
    Procedure d34y = procedures.stream().filter(i -> i.procedureIdentifier().equals("D34-Y")).filter(i -> i.airportIdentifier().equals("GQNO")).findFirst().orElseThrow();
    Procedure r210 = procedures.stream().filter(i -> i.procedureIdentifier().equals("R210")).findFirst().orElseThrow();

    assertAll("Make sure there are the right number of legs and that they are the right type",
        () -> assertEquals(93, procedures.size()),
        () -> assertEquals(RequiredNavigationEquipage.CONV, orw7.requiredNavigationEquipage()), //sid
        () -> assertEquals(RequiredNavigationEquipage.RNAV, blzzr6.requiredNavigationEquipage()), //sid
        () -> assertEquals(RequiredNavigationEquipage.RNAV, jfund2.requiredNavigationEquipage()), //star
        () -> assertEquals(RequiredNavigationEquipage.RNP, r32.requiredNavigationEquipage()), //approach
        () -> assertEquals(RequiredNavigationEquipage.CONV, i27.requiredNavigationEquipage()),
        () -> assertEquals(RequiredNavigationEquipage.RNP, r33lx.requiredNavigationEquipage()),
        () -> assertEquals(RequiredNavigationEquipage.RNP, r210.requiredNavigationEquipage()),
        () -> assertEquals(4, r210.transitions().size()),
        () -> assertEquals(6, r33lx.transitions().size()),
        () -> assertEquals(5, r33lx.transitions().stream().filter(i -> i.transitionIdentifier().get().equals("ALL")).findFirst().get().legs().size()),
        () -> assertEquals(2L, d34y.transitions().stream().filter(i -> i.transitionIdentifier().filter(t -> t.equals("OT")).isPresent()).count(), "yeah there are now 1000's of cases where the transition ident is not unique now")
    );
  }

  @Test
  void testSortsOnlyAfterGroupingByTransition() {
    List<ArincProcedureLeg> input = List.of(
        newProcedureLeg("ALPHA", 30),
        newProcedureLeg("BRAVO", 20),
        newProcedureLeg("ALPHA", 10),
        newProcedureLeg("BRAVO", 40)
    );

    Collection<List<ArincProcedureLeg>> procedures = ProcedureAssembler.Standard.groupByProcedure(input);
    List<ArincProcedureLeg> procedure = procedures.iterator().next();
    Map<String, List<Integer>> transitionSequences = ProcedureAssembler.Standard.groupByTransition(procedure).stream()
        .collect(Collectors.toMap(
            transition -> transition.get(0).transitionIdentifier().orElseThrow(),
            transition -> transition.stream().map(ArincProcedureLeg::sequenceNumber).toList()
        ));

    assertAll(
        () -> assertEquals(1, procedures.size()),
        () -> assertEquals(List.of(30, 20, 10, 40), procedure.stream().map(ArincProcedureLeg::sequenceNumber).toList()),
        () -> assertEquals(List.of(10, 30), transitionSequences.get("ALPHA")),
        () -> assertEquals(List.of(20, 40), transitionSequences.get("BRAVO"))
    );
  }

  @Test
  void testAdditionalMissedApproachesAreGroupedByRouteTypeAndVariant() {
    List<ArincProcedureLeg> input = List.of(
        newProcedureLeg("FINAL", "R", "H", 30),
        newProcedureLeg("FINAL", "Z", "A", 20),
        newProcedureLeg("FINAL", "Z", "B", 25),
        newProcedureLeg("FINAL", "Z", "E", 27),
        newProcedureLeg("FINAL", "R", "S", 10),
        newProcedureLeg("FINAL", "Z", "A", 40),
        newProcedureLeg("FINAL", "Z", "B", 45),
        newProcedureLeg("FINAL", "Z", "E", 47)
    );

    Map<String, List<Integer>> transitionSequences = ProcedureAssembler.Standard.groupByTransition(input).stream()
        .collect(Collectors.toMap(
            transition -> "Z".equals(transition.get(0).routeType())
                ? transition.get(0).routeType().concat(transition.get(0).routeTypeQualifier2().orElse(""))
                : transition.get(0).routeType(),
            transition -> transition.stream().map(ArincProcedureLeg::sequenceNumber).toList()
        ));

    assertAll(
        () -> assertEquals(4, transitionSequences.size()),
        () -> assertEquals(List.of(10, 30), transitionSequences.get("R")),
        () -> assertEquals(List.of(20, 40), transitionSequences.get("ZA")),
        () -> assertEquals(List.of(25, 45), transitionSequences.get("ZB")),
        () -> assertEquals(List.of(27, 47), transitionSequences.get("ZE"))
    );
  }

  private static ArincProcedureLeg newProcedureLeg(String transitionIdentifier, int sequenceNumber) {
    return newProcedureLeg(transitionIdentifier, "1", sequenceNumber);
  }

  private static ArincProcedureLeg newProcedureLeg(String transitionIdentifier, String routeType, int sequenceNumber) {
    return newProcedureLeg(transitionIdentifier, routeType, null, sequenceNumber);
  }

  private static ArincProcedureLeg newProcedureLeg(
      String transitionIdentifier,
      String routeType,
      String routeTypeQualifier2,
      int sequenceNumber
  ) {
    return new ArincProcedureLeg.Builder()
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(sequenceNumber)
        .sidStarIdentifier("MOCK")
        .airportIdentifier("MOCK")
        .airportIcaoRegion("K1")
        .sectionCode(SectionCode.P)
        .subSectionCode("D")
        .routeType(routeType)
        .routeTypeQualifier2(routeTypeQualifier2)
        .transitionIdentifier(transitionIdentifier)
        .build();
  }
}
