package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

public class TestLidoControlledAirspaceAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/controlled.txt"));
  private static final ArincFileParser fileParser22 = new ArincFileParser(ArincRecordParser.standard(ArincVersion.V22.specs()));
  private static final ConvertingArincRecordConsumer consumer22 = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
  private static ControlledAirspaceAssembler<Airspace> assembler22;

  @BeforeAll
  public static void setUp() {
    fileParser22.apply(arincTestFile).forEach(consumer22);

    ArincFixDatabase arincFixDatabase22 = ArincDatabaseFactory.newFixDatabase(
        consumer22.arincNdbNavaids(),
        consumer22.arincVhfNavaids(),
        consumer22.arincWaypoints(),
        consumer22.arincAirports(),
        consumer22.arincHoldingPatterns(),
        consumer22.arincHeliports()
    );

    assembler22 = ControlledAirspaceAssembler.standard(arincFixDatabase22);
  }

  @Test
  void testAssemble22() {
    List<Airspace> airspaces = assembler22.assemble(consumer22.arincControlledAirspaceLegs()).toList();
    Airspace circle = airspaces.stream().filter(i -> i.identifier().equals("KBOI-A-ANM ID C BOISE AIR TERMINAL (A")).findFirst().orElseThrow();
    Airspace shape = airspaces.stream().filter(i -> i.identifier().equals("KBOI-A-ANM ID C BOISE AIR TERMINAL (B")).findFirst().orElseThrow();
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
