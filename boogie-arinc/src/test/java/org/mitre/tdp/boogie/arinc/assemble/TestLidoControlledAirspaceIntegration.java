package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestLidoControlledAirspaceIntegration {
  static Map<String, Airspace> map;
  @BeforeAll
  static void setup() {
    ArincFixDatabase fixes = ArincDatabaseFactory.newFixDatabase(
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincHoldingPatterns(),
        EmbeddedLidoFile.instance().arincHeliports()

    );
    ControlledAirspaceAssembler<Airspace> assembler = ControlledAirspaceAssembler.standard(fixes);
    map = assembler.assemble(EmbeddedLidoFile.instance().arincControlledAirspaceLegs())
        .collect(Collectors.toMap(Airspace::identifier, Function.identity()));
  }

  @Test
  void test() {
    assertEquals(11761, map.size());
  }
}
