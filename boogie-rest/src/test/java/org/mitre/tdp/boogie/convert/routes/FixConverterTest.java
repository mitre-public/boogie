package org.mitre.tdp.boogie.convert.routes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.contract.routes.Airport;
import org.mitre.tdp.boogie.contract.routes.Fix;
import org.mitre.tdp.boogie.contract.routes.Runway;
import org.mitre.tdp.boogie.model.BoogieAirport;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieRunway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FixConverterTest {
  private static final org.mitre.tdp.boogie.Fix fix = new BoogieFix.Builder()
      .fixIdentifier("DAVID")
      .fixRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .elevation(10D)
      .publishedVariation(15D)
      .modeledVariation(23D)
      .build();

  private static final org.mitre.tdp.boogie.Runway runway = new BoogieRunway.Builder()
      .airportIdentifier("DAVID")
      .airportRegion("DB")
      .departureRunwayEnd(LatLong.of(50D, 90D))
      .arrivalRunwayEnd(LatLong.of(50D, 91D))
      .length(5000D)
      .width(150D)
      .runwayIdentifier("RW26")
      .build();

  private static final org.mitre.tdp.boogie.Airport airport = new BoogieAirport.Builder()
      .airportIdentifier("DAVID")
      .airportRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .publishedVariation(15D)
      .elevation(100D)
      .runways(List.of(runway))
      .build();

  private static final ConvertFix fixConverter = ConvertFix.INSTANCE;
  private static final ConvertRunway runwayConverter = ConvertRunway.INSTANCE;
  private static final ConvertAirport airportConverter = ConvertAirport.INSTANCE;

  private static final ObjectMapper mapper = new ObjectMapper();

  Fix convertedFix = fixConverter.apply(fix);
  Runway convertedRunway = runwayConverter.apply(runway);
  Airport convertedAirport = airportConverter.apply(airport);

  @Test
  void testConvertAndAsJSON() throws JsonProcessingException {
    String string = mapper.writeValueAsString(convertedFix);
    assertAll("Checking basic object creation and ability to write to json string",
        () -> assertNotNull(convertedFix),
        () -> assertNotNull(convertedRunway),
        () -> assertNotNull(convertedAirport),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(convertedFix)),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(convertedRunway)),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(convertedAirport)),
        () -> assertDoesNotThrow(() -> convertedFix.latLong().toCommons()),
        () -> assertEquals(50D, convertedFix.latLong().toCommons().longitude())
    );
  }

  @Test
  void testFix() {
    assertAll("Checking values in fix",
        () -> assertEquals("DAVID", convertedFix.fixIdentifier()),
        () -> assertEquals(50D, convertedFix.latitude()),
        () -> assertEquals(50D, convertedFix.longitude()),
        () -> assertEquals(10D, convertedFix.elevation().orElseThrow()),
        () -> assertEquals("DB", convertedFix.fixRegion()),
        () -> assertEquals(15D, convertedFix.magneticVariation().published().orElseThrow()),
        () -> assertEquals(23D, convertedFix.magneticVariation().modeled())
    );
  }

  @Test
  void testRunway() {
    assertAll("Checking values in runway",
        () -> assertEquals("RW26", convertedRunway.runwayIdentifier()),
        () -> assertEquals("DAVID", convertedRunway.airportIdentifier()),
        () -> assertEquals("DB", convertedRunway.airportRegion()),
        () -> assertEquals(5000D, convertedRunway.length().orElseThrow()),
        () -> assertEquals(150D, convertedRunway.width().orElseThrow())
    );
  }

  @Test
  void testAirport() {
    assertAll("Checking values in airport",
        () -> assertEquals("DAVID", convertedAirport.airportIdentifier()),
        () -> assertEquals("DAVID", convertedAirport.fix().fixIdentifier()),
        () -> assertEquals("DB", convertedAirport.airportRegion()),
        () -> assertEquals("DB", convertedAirport.fix().fixRegion()),
        () -> assertFalse(convertedAirport.runways().isEmpty())
        );
  }
}
