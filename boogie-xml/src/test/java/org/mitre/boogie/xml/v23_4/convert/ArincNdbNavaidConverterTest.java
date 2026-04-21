package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.NavaidNdbEmissionType;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidCoverage;
import org.mitre.boogie.xml.model.fields.NdbNavaidIfMarkerInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidType;
import org.mitre.boogie.xml.v23_4.generated.Ndb;

class ArincNdbNavaidConverterTest {
  private final ArincNdbNavaidConverter converter = ArincNdbNavaidConverter.INSTANCE;

  @Test
  void nullNdbReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidNdbReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(new Ndb()));
  }

  @Test
  void validNdbConverts() {
    Optional<ArincNdbNavaid> result = converter.apply(TestGeneratedObjects.newValidNdb());
    assertTrue(result.isPresent());

    ArincNdbNavaid nav = result.get();

    assertAll(
        () -> assertNotNull(nav.recordInfo()),
        () -> assertNotNull(nav.pointInfo()),
        () -> assertEquals("K6", nav.pointInfo().icaoCode()),
        () -> assertEquals("DC", nav.pointInfo().identifier()),

        () -> assertEquals(382.0, nav.frequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.KILO_HERTZ), nav.freqUnitOfMeasure()),

        () -> assertEquals(Optional.of(400L), nav.typeOfEmission()),
        () -> assertEquals(Optional.of(1020L), nav.repetitionRate()),
        () -> assertEquals(Optional.of(NavaidNdbEmissionType.CARRIER_KEYED), nav.navaidNdbEmissionType()),
        () -> assertEquals(Optional.of(NavaidVoice.NO_VOICE_IDENT), nav.ndbVoice()),

        () -> assertEquals(Optional.of(false), nav.isBfoRequired()),
        () -> assertEquals(Optional.of(NdbNavaidCoverage.HIGH_POWER_NDB), nav.ndbNavaidCoverage()),
        () -> assertEquals(Optional.of(NdbNavaidIfMarkerInfo.OUTER_MARKER), nav.ndbNavaidIfMarker()),
        () -> assertEquals(Optional.of(NdbNavaidType.NDB), nav.ndbNavaidType()),
        () -> assertEquals(Optional.of(NavaidWeatherInfo.AUTOMATED), nav.ndbNavaidWeatherInfo()),
        () -> assertEquals(Optional.of(true), nav.isNoVoice()),

        () -> assertEquals(Optional.of("DME-DC"), nav.dmeTacanRef()),
        () -> assertEquals(Optional.of(250), nav.elevation()),
        () -> assertEquals(Optional.of(true), nav.isVFRCheckpoint())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setTypeOfEmission(null);
    ndb.setRepetitionRate(null);
    ndb.setNavaidNdbEmissionType(null);
    ndb.setNdbVoice(null);

    Optional<ArincNdbNavaid> result = converter.apply(ndb);
    assertTrue(result.isPresent());

    ArincNdbNavaid nav = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), nav.typeOfEmission()),
        () -> assertEquals(Optional.empty(), nav.repetitionRate()),
        () -> assertEquals(Optional.empty(), nav.navaidNdbEmissionType()),
        () -> assertEquals(Optional.empty(), nav.ndbVoice())
    );
  }
}
