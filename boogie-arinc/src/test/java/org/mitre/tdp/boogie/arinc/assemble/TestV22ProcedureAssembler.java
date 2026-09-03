package org.mitre.tdp.boogie.arinc.assemble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestV22ProcedureAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kbos-supp22.txt"));
  private static final File gqnoFile = new File(System.getProperty("user.dir").concat("/src/test/resources/gqno-and-friends.txt"));
  private static final File arincTestFile2 = new File(System.getProperty("user.dir").concat("/src/test/resources/kjra_9vak5-and-friends"));

  private static ProcedureAssembler<Procedure> assembler;

  private static ConvertedArincRecords records;
  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V22.specs()));

  @BeforeAll
  static void setup() {
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
    fileParser.parseAll(arincTestFile).forEach(consumer);
    fileParser.parseAll(gqnoFile).forEach(consumer);
    fileParser.parseAll(arincTestFile2).forEach(consumer);
    records = consumer.snapshot();

    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        records.arincAirports(),
        records.arincRunways(),
        records.arincLocalizerGlideSlopes(),
        records.arincNdbNavaids(),
        records.arincVhfNavaids(),
        records.arincWaypoints(),
        records.arincProcedureLegs(),
        records.arincGnssLandingSystems(),
        records.arincHelipads(),
        records.arincHeliports()
    );

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        records.arincNdbNavaids(),
        records.arincVhfNavaids(),
        records.arincWaypoints(),
        records.arincAirports(),
        records.arincHoldingPatterns(),
        records.arincHeliports()
    );

    assembler = ProcedureAssembler.standard(arincTerminalAreaDatabase, arincFixDatabase);
  }

  @Test
  void testSidStarApproach() {
    List<Procedure> procedures = assembler.assemble(records.arincProcedureLegs()).toList();
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
        () -> assertEquals(5, r33lx.transitions().stream().filter(i -> i.transitionIdentifier().orElseThrow().equals("ALL")).findFirst().orElseThrow().legs().size()),
        () -> assertEquals(2L, d34y.transitions().stream().filter(i -> i.transitionIdentifier().filter(t -> t.equals("OT")).isPresent()).count(), "yeah there are now 1000's of cases where the transition ident is not unique now")
    );
  }

}
