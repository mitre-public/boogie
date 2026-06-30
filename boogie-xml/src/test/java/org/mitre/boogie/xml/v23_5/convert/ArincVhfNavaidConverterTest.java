package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.fields.FigureOfMerit;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.NavaidSynchronization;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.VhfNavaidCoverage;
import org.mitre.boogie.xml.model.fields.VorRangePower;
import org.mitre.boogie.xml.v23_5.generated.Dme;
import org.mitre.boogie.xml.v23_5.generated.LocalizerGlideslope;
import org.mitre.boogie.xml.v23_5.generated.MilitaryTacan;
import org.mitre.boogie.xml.v23_5.generated.Tacan;
import org.mitre.boogie.xml.v23_5.generated.Vor;
import org.mitre.tdp.boogie.MagneticVariation;

class ArincVhfNavaidConverterTest {
  private final ArincVhfNavaidConverter converter = ArincVhfNavaidConverter.INSTANCE;

  @Test
  void nullVorReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidVorReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(new Vor()));
  }

  @Test
  void validVorConverts() {
    Optional<ArincVhfNavaid> result = converter.apply(TestGeneratedObjects.newValidVor());
    assertTrue(result.isPresent());

    ArincVhfNavaid nav = result.get();

    assertAll(
        () -> assertNotNull(nav.recordInfo()),
        () -> assertNotNull(nav.pointInfo()),
        () -> assertEquals("K6", nav.pointInfo().icaoCode()),
        () -> assertEquals("DCA", nav.pointInfo().identifier()),

        () -> assertEquals(113.1, nav.frequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.MEGA_HERTZ), nav.freqUnitOfMeasure()),

        () -> assertEquals(Optional.of(MagneticVariation.ofDegrees(3.5)), nav.stationDeclination()),

        () -> assertEquals(Optional.of(FigureOfMerit.HIGH_ALT), nav.figureOfMerit()),
        () -> assertEquals(Optional.of(200L), nav.frequencyProtection()),
        () -> assertEquals(Optional.of(NavaidSynchronization.SYNCHRONOUS), nav.navaidSynchronization()),
        () -> assertEquals(Optional.of(NavaidVoice.VOICE_IDENT), nav.vorVoice()),
        () -> assertEquals(Optional.of(VorRangePower.NM_130_FEET_60000_LEGACY), nav.vorRangePower()),

        () -> assertEquals(Optional.of(VhfNavaidCoverage.HIGH), nav.vhfNavaidCoverage()),
        () -> assertEquals(Optional.of(NavaidWeatherInfo.AUTOMATED), nav.vhfNavaidWeatherInfo()),
        () -> assertEquals(Optional.of(false), nav.isNotCollocated()),
        () -> assertEquals(Optional.of(false), nav.isBiased()),
        () -> assertEquals(Optional.of(false), nav.isNoVoice()),

        () -> assertEquals(Optional.of("PORT-DCA"), nav.portRef()),
        () -> assertEquals(Optional.of("DME-DCA"), nav.dmeTacanRef()),
        () -> assertEquals(Optional.of(300), nav.elevation()),
        () -> assertEquals(Optional.of(false), nav.isVFRCheckpoint())
    );
  }

  @Test
  void validTacanConverts() {
    Tacan tacan = TestGeneratedObjects.newValidTacan();
    Optional<ArincVhfNavaid> result = converter.apply(tacan);
    assertTrue(result.isPresent());

    ArincVhfNavaid nav = result.get();

    assertAll(
        () -> assertEquals(Optional.of("TACAN"), nav.navaidSubType()),
        () -> assertEquals(Optional.of(MagneticVariation.ofDegrees(-2.0)), nav.stationDeclination()),
        () -> assertEquals(Optional.of(true), nav.isPaired()),
        () -> assertEquals(Optional.of(IlsDmeLocation.NOT_COLLOCATED), nav.ilsDmeLocation()),
        () -> assertEquals(Optional.of(0.5), nav.ilsDmeBias())
    );
  }

  @Test
  void validMilitaryTacanConverts() {
    MilitaryTacan tacan = TestGeneratedObjects.newValidMilitaryTacan();
    Optional<ArincVhfNavaid> result = converter.apply(tacan);
    assertTrue(result.isPresent());

    ArincVhfNavaid nav = result.get();

    assertAll(
        () -> assertEquals(Optional.of("MILITARY_TACAN"), nav.navaidSubType()),
        () -> assertEquals(Optional.of(MagneticVariation.ofDegrees(4.0)), nav.stationDeclination()),
        () -> assertEquals(Optional.of(IlsDmeLocation.COLLOCATED_GLIDE_SLOPE), nav.ilsDmeLocation()),
        () -> assertEquals(Optional.of(0.75), nav.ilsDmeBias())
    );
  }

  @Test
  void localizerGlideslopeReturnsEmpty() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    assertEquals(Optional.empty(), converter.apply(lg));
  }

  @Test
  void validDmeConverts() {
    Dme dme = TestGeneratedObjects.newValidDme();
    Optional<ArincVhfNavaid> result = converter.apply(dme);
    assertTrue(result.isPresent());

    ArincVhfNavaid nav = result.get();

    assertAll(
        () -> assertNotNull(nav.recordInfo()),
        () -> assertNotNull(nav.pointInfo()),
        () -> assertEquals("K6", nav.pointInfo().icaoCode()),
        () -> assertEquals("DME1", nav.pointInfo().identifier()),

        () -> assertEquals(Optional.of("DME"), nav.navaidSubType()),

        () -> assertEquals(110.3, nav.frequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.MEGA_HERTZ), nav.freqUnitOfMeasure()),

        () -> assertEquals(Optional.of(IlsDmeLocation.COLLOCATED_LOCALIZER), nav.ilsDmeLocation()),
        () -> assertEquals(Optional.of(0.25), nav.ilsDmeBias()),
        () -> assertEquals(Optional.of(true), nav.isIlsComponent()),

        () -> assertEquals(Optional.of("NM_130_FEET_18000"), nav.dmeExpandedServiceVolume()),
        () -> assertEquals(Optional.of("T"), nav.dmeOperationalServiceVolume()),
        () -> assertEquals(Optional.of(false), nav.isRouteInappropriateDme()),
        () -> assertEquals(Optional.of(false), nav.isPaired()),
        () -> assertEquals(Optional.of(false), nav.isMlsP()),

        () -> assertEquals(Optional.of(VhfNavaidCoverage.TERMINAL), nav.vhfNavaidCoverage()),
        () -> assertEquals(Optional.of(NavaidWeatherInfo.AUTOMATED), nav.vhfNavaidWeatherInfo()),

        () -> assertEquals(Optional.of("PORT-DME1"), nav.portRef()),
        () -> assertEquals(Optional.of(300), nav.elevation()),
        () -> assertEquals(Optional.of(false), nav.isVFRCheckpoint())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setStationDeclination(null);
    vor.setFigureOfMerit(null);
    vor.setNavaidSynchronization(null);
    vor.setVorVoice(null);
    vor.setVorRangePower(null);
    vor.setFrequencyProtection(null);

    Optional<ArincVhfNavaid> result = converter.apply(vor);
    assertTrue(result.isPresent());

    ArincVhfNavaid nav = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), nav.stationDeclination()),
        () -> assertEquals(Optional.empty(), nav.figureOfMerit()),
        () -> assertEquals(Optional.empty(), nav.navaidSynchronization()),
        () -> assertEquals(Optional.empty(), nav.vorVoice()),
        () -> assertEquals(Optional.empty(), nav.vorRangePower()),
        () -> assertEquals(Optional.empty(), nav.frequencyProtection())
    );
  }
}
