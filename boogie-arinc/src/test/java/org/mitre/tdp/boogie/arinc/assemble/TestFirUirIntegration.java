package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Tag("INTEGRATION")
public class TestFirUirIntegration {
  private static Multimap<String, Airspace> firUirByName;

  @BeforeAll
  static void setup() {
    FirUirAssembler<Airspace> assembler = FirUirAssembler.standard();
    firUirByName = assembler.assemble(EmbeddedLidoFile.instance().arincFirUirLegs())
        .collect(ArrayListMultimap::create, (m, i) -> m.put(i.identifier(), i), Multimap::putAll);
  }

  @Test
  void testUSFirUirCount() {
    assertEquals(49, firUirByName.values().size());
  }


  @Test
  void testCountsByIndicator() {
    assertAll(
        () -> assertEquals(28, firUirByName.values().stream().filter(i -> i.airspaceType().equals(AirspaceType.FIR)).count(), "Fs and Bs"),
        () -> assertEquals(21, firUirByName.values().stream().filter(i -> i.airspaceType().equals(AirspaceType.UIR)).count(), "Us and Bs")
    );
  }
}
