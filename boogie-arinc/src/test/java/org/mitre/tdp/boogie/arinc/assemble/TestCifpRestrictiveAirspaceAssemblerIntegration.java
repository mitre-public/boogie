package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;

@Tag("CIFP")
@Tag("INTEGRATION")
public class TestCifpRestrictiveAirspaceAssemblerIntegration {

  private static List<Airspace> airspaces;

  @BeforeAll
  static void setup() {
    RestrictiveAirspaceAssembler<Airspace> assembler = RestrictiveAirspaceAssembler.standard();
    airspaces = assembler.assemble(EmbeddedCifpFile.instance().restrictiveAirspaces()).toList();
  }

  @Test
  void testAssembledCount() {
    assertEquals(1203, airspaces.size(), "Should assemble restrictive airspaces from CIFP data");
  }

  @Test
  void testAllRestrictiveType() {
    assertTrue(
        airspaces.stream().allMatch(a -> a.airspaceType().equals(AirspaceType.RESTRICTIVE)),
        "All assembled airspaces should be RESTRICTIVE type"
    );
  }

  @Test
  void testEachAirspaceHasSequences() {
    assertTrue(
        airspaces.stream().allMatch(a -> !a.sequences().isEmpty()),
        "Each assembled airspace should have at least one sequence"
    );
  }
}
