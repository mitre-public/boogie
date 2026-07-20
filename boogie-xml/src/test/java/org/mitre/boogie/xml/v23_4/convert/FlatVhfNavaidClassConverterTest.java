package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.VhfNavaidClass;

class FlatVhfNavaidClassConverterTest {
  private final FlatVhfNavaidClassConverter converter = FlatVhfNavaidClassConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    Optional<FlatVhfNavaidClass> result = converter.apply(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void convertsValidVhfNavaidClass() {
    VhfNavaidClass nc = new VhfNavaidClass();
    nc.setVhfNavaidCoverage(org.mitre.boogie.xml.v23_4.generated.VhfNavaidCoverage.HIGH);
    nc.setVhfNavaidWeatherInfo(org.mitre.boogie.xml.v23_4.generated.NavaidWeatherInfo.AUTOMATED);
    nc.setIsNotCollocated(false);
    nc.setIsBiased(false);
    nc.setIsNoVoice(true);

    FlatVhfNavaidClass result = converter.apply(nc).orElseThrow();
    assertAll(
        () -> assertEquals("HIGH", result.coverage()),
        () -> assertEquals("AUTOMATED", result.weatherInfo()),
        () -> assertEquals(Boolean.FALSE, result.isNotCollocated()),
        () -> assertEquals(Boolean.FALSE, result.isBiased()),
        () -> assertEquals(Boolean.TRUE, result.isNoVoice())
    );
  }

  @Test
  void nullFieldsInsideVhfNavaidClass() {
    VhfNavaidClass nc = new VhfNavaidClass();

    FlatVhfNavaidClass result = converter.apply(nc).orElseThrow();
    assertAll(
        () -> assertNull(result.coverage()),
        () -> assertNull(result.weatherInfo()),
        () -> assertNull(result.isNotCollocated()),
        () -> assertNull(result.isBiased()),
        () -> assertNull(result.isNoVoice())
    );
  }
}
