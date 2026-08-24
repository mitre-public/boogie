package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestLidoRestrictiveAirspaceAssemblerIntegration {

  private static List<Airspace> airspaces;

  @BeforeAll
  static void setup() {
    RestrictiveAirspaceAssembler<Airspace> assembler = RestrictiveAirspaceAssembler.standard();
    airspaces = assembler.assemble(EmbeddedLidoFile.instance().arincRestrictiveAirspaceLegs()).toList();
  }

  @Test
  void testAssembledCount() {
    assertEquals(20503, airspaces.size(), "Should assemble restrictive airspaces from LIDO data");
  }

  @Test
  void testAllRestrictiveType() {
    assertTrue(
        airspaces.stream().allMatch(a -> a.airspaceType().equals(AirspaceType.RESTRICTIVE)), "All assembled airspaces should be RESTRICTIVE type"
    );
  }

  @Test
  void testEachAirspaceHasSequences() {
    assertTrue(
        airspaces.stream().allMatch(a -> !a.sequences().isEmpty()), "Each assembled airspace should have at least one sequence"
    );
  }
}
