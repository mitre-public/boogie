package org.mitre.tdp.boogie.arinc.assemble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestV18RestrictiveAirspaceConsumer {

  private static final File restrictiveTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/restrictive.txt"));
  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V18.specs()));
  private static final RestrictiveAirspaceAssembler<Airspace> assembler = RestrictiveAirspaceAssembler.standard();

  private static ConvertedArincRecords records;
  private static List<Airspace> airspaces;

  @BeforeAll
  static void setup() {
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);
    fileParser.parseAll(restrictiveTestFile).forEach(consumer);
    records = consumer.snapshot();
    airspaces = assembler.assemble(records.arincRestrictiveAirspaceLegs()).toList();
  }

  @Test
  void testRestrictiveAirspaceLegCount() {
    assertEquals(75, records.arincRestrictiveAirspaceLegs().size(), "RestrictiveAirspaceLeg count");
  }

  @Test
  void testAssembledAirspaceCount() {
    assertAll(
        () -> assertEquals(1, airspaces.size(), "Should assemble to a single BOARDMAN MOA"),
        () -> assertEquals(75, airspaces.get(0).sequences().size(), "Sequences from all primary legs"),
        () -> assertEquals(4000.0, airspaces.get(0).altitudeLimit().lowerEndpoint()),
        () -> assertEquals(17999.0, airspaces.get(0).altitudeLimit().upperEndpoint()),
        () -> assertEquals("BOARDMAN-M-K1-A", airspaces.get(0).identifier())
    );
  }
}
