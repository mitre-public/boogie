package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Frequency;

class FlatFrequencyConverterTest {
  private final FlatFrequencyConverter converter = FlatFrequencyConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    Optional<FlatFrequency> result = converter.apply(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void convertsValidFrequency() {
    Frequency freq = new Frequency();
    freq.setFrequencyValue(BigDecimal.valueOf(113.1));
    freq.setFreqUnitOfMeasure(org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ);

    FlatFrequency result = converter.apply(freq).orElseThrow();
    assertAll(
        () -> assertEquals(113.1, result.value(), 0.001),
        () -> assertEquals("MEGA_HERTZ", result.unitOfMeasure())
    );
  }

  @Test
  void nullFieldsInsideFrequency() {
    Frequency freq = new Frequency();

    FlatFrequency result = converter.apply(freq).orElseThrow();
    assertAll(
        () -> assertNull(result.value()),
        () -> assertNull(result.unitOfMeasure())
    );
  }
}
