package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

@Tag("CIFP")
@Tag("INTEGRATION")
public class TestCifpHeliportIntegration {
  private static Multimap<String, Heliport> heliports;
  @BeforeAll
  static void setup() {
    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincRunways(),
        EmbeddedCifpFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincProcedureLegs(),
        EmbeddedCifpFile.instance().arincGnssLandingSystems(),
        EmbeddedCifpFile.instance().arincHelipads(),
        EmbeddedCifpFile.instance().arincHeliports()
    );

    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard(arincTerminalAreaDatabase);
    heliports = Multimaps.index(EmbeddedCifpFile.instance().arincHeliports().stream().map(assembler::assemble).toList(), Heliport::heliportIdentifier);
  }
  @Test
  void testParse() {
    assertAll(
        () -> assertEquals(4, heliports.get("6N5").size(), "34th Street Counts"),
        () -> assertEquals(1, heliports.get("KJRB").size(), "NYC Counts"),
        () -> assertEquals(1, heliports.get("0MN1").size(), "Mayo clinic is an important hospital"),
        () -> assertEquals(1, heliports.get("0MA4").size(), "Boston General")
    );
  }

  @Test
  void testNoPortsWithoutPads() {
    long noPads = heliports.values().stream()
        .map(Heliport::helipads)
        .filter(Collection::isEmpty)
        .count();
    assertEquals(0, noPads);
  }

  @Test
  void testNotDroppingOrGrowingPorts() {
    long ports = EmbeddedCifpFile.instance().arincHeliports().size();
    long heliportsSize = heliports.size();
    long numberWithMoreThanOne = heliports.asMap().values().stream()
        .filter(i -> i.size() > 1).count();

    assertAll(
        () -> assertEquals(ports, heliportsSize),
        () -> assertEquals(280, numberWithMoreThanOne, "This means there was more than one pad there")
    );
  }
}
