package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestLidoHeliportAssemblerIntegration {
  private static Multimap<String, Heliport> heliports;
  @BeforeAll
  static void setup() {
    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincRunways(),
        EmbeddedLidoFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincProcedureLegs(),
        EmbeddedLidoFile.instance().arincGnssLandingSystems(),
        EmbeddedLidoFile.instance().arincHelipads(),
        EmbeddedLidoFile.instance().arincHeliports()
    );

    HeliportAssembler<Heliport> assembler = HeliportAssembler.standard(arincTerminalAreaDatabase);
    heliports = Multimaps.index(EmbeddedLidoFile.instance().arincHeliports().stream().map(assembler::assemble).toList(), Heliport::heliportIdentifier);
  }
  @Test
  void testParse() {
    assertAll(
        () -> assertEquals(1, heliports.get("6N5").size(), "34th Street Counts"),
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
    assertEquals(16, noPads, "there are a few without pads that make it through parsing.");
  }

  @Test
  void testNotDroppingOrGrowingPorts() {
    long ports = EmbeddedLidoFile.instance().arincHeliports().size();
    long heliportsSize = heliports.size();
    long numberWithMoreThanOne = heliports.asMap().values().stream().filter(i -> i.size() > 1).count();

    long pads = EmbeddedLidoFile.instance().arincHelipads().stream().filter(i -> i.sectionCode().equals(SectionCode.H)).count();
    long padCounts = heliports.values().stream().map(Heliport::helipads).mapToLong(Collection::size).sum();
    assertAll(
        () -> assertEquals(ports, heliportsSize, "Each record should parse"),
        () -> assertEquals(0, numberWithMoreThanOne, "With pads there should not be duplicates."),
        () -> assertEquals(pads, padCounts)
    );
  }
}
