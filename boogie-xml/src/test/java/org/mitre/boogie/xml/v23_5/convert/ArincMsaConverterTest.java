package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincMsa;
import org.mitre.boogie.xml.model.fields.ArincMsaSector;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;
import org.mitre.boogie.xml.v23_5.generated.Msa;

class ArincMsaConverterTest {
  private final ArincMsaConverter converter = ArincMsaConverter.INSTANCE;

  @Test
  void nullMsaReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidMsaReturnsEmpty() {
    Msa msa = new Msa();
    assertEquals(Optional.empty(), converter.apply(msa));
  }

  @Test
  void validMsaConverts() {
    Optional<ArincMsa> result = converter.apply(TestGeneratedObjects.newValidMsa());
    assertTrue(result.isPresent());

    ArincMsa arincMsa = result.get();

    assertAll(
        () -> assertNotNull(arincMsa.recordInfo()),
        () -> assertEquals(Optional.of("DCA"), arincMsa.centerFix()),
        () -> assertEquals(Optional.of("FIX-DCA"), arincMsa.centerFixRef()),
        () -> assertEquals(Optional.of("K6"), arincMsa.icaoCode()),
        () -> assertEquals(Optional.of(MagneticTrueIndicator.MAGNETIC), arincMsa.magneticTrueIndicator()),
        () -> assertEquals(Optional.of("A"), arincMsa.multipleCode()),
        () -> assertEquals(2, arincMsa.sectors().size())
    );

    ArincMsaSector first = arincMsa.sectors().get(0);
    assertAll(
        () -> assertEquals(2500, first.sectorAltitude()),
        () -> assertEquals(0, first.sectorBearingBegin()),
        () -> assertEquals(180, first.sectorBearingEnd()),
        () -> assertEquals(25, first.sectorRadius())
    );

    ArincMsaSector second = arincMsa.sectors().get(1);
    assertAll(
        () -> assertEquals(3000, second.sectorAltitude()),
        () -> assertEquals(180, second.sectorBearingBegin()),
        () -> assertEquals(360, second.sectorBearingEnd()),
        () -> assertEquals(25, second.sectorRadius())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Msa msa = TestGeneratedObjects.newValidMsa();
    msa.setCenterFix(null);
    msa.setIcaoCode(null);
    msa.setMagneticTrueIndicator(null);
    msa.setMultipleCode(null);

    Optional<ArincMsa> result = converter.apply(msa);
    assertTrue(result.isPresent());

    ArincMsa arincMsa = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), arincMsa.centerFix()),
        () -> assertEquals(Optional.empty(), arincMsa.icaoCode()),
        () -> assertEquals(Optional.empty(), arincMsa.magneticTrueIndicator()),
        () -> assertEquals(Optional.empty(), arincMsa.multipleCode()),
        () -> assertEquals(2, arincMsa.sectors().size())
    );
  }
}
