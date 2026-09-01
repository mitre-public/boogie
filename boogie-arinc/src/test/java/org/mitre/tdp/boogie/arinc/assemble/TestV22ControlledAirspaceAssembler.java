package org.mitre.tdp.boogie.arinc.assemble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestV22ControlledAirspaceAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/controlled.txt"));
  private static final TestArincFileParser fileParser22 = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V22.specs()));
  private static ConvertedArincRecords records22;
  private static ControlledAirspaceAssembler<Airspace> assembler22;

  @BeforeAll
  public static void setUp() {
    ConvertingArincRecordConsumer consumer22 = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
    fileParser22.parseAll(arincTestFile).forEach(consumer22);
    records22 = consumer22.snapshot();

    ArincFixDatabase arincFixDatabase22 = ArincDatabaseFactory.newFixDatabase(
        records22.arincNdbNavaids(),
        records22.arincVhfNavaids(),
        records22.arincWaypoints(),
        records22.arincAirports(),
        records22.arincHoldingPatterns(),
        records22.arincHeliports()
    );

    assembler22 = ControlledAirspaceAssembler.standard(arincFixDatabase22);
  }

  @Test
  void testAssemble22() {
    List<Airspace> airspaces = assembler22.assemble(records22.arincControlledAirspaceLegs()).toList();
    Airspace circle = airspaces.stream().filter(i -> i.identifier().equals("KBOI-A-K1-ANM ID C BOISE AIR TERMINAL (A-A-C")).findFirst().orElseThrow();
    Airspace shape = airspaces.stream().filter(i -> i.identifier().equals("KBOI-A-K1-ANM ID C BOISE AIR TERMINAL (B-B-C")).findFirst().orElseThrow();
    assertAll(
        () -> assertEquals(2, airspaces.size() , "Should only be two of them"),
        () -> assertEquals(1, circle.sequences().size(), "Just one leg"),
        () -> assertEquals(Geometry.CIRCLE, circle.sequences().get(0).geometry(), "its a circle"),
        () -> assertTrue(circle.sequences().get(0).centerFix().isPresent()),
        () -> assertEquals(6, shape.sequences().size()),
        () -> assertTrue(shape.sequences().stream().noneMatch(i -> i.geometry().equals(Geometry.CIRCLE)))
    );
  }
}
