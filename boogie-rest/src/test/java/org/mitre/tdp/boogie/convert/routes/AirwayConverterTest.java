package org.mitre.tdp.boogie.convert.routes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.contract.routes.Airway;
import org.mitre.tdp.boogie.model.BoogieAirport;
import org.mitre.tdp.boogie.model.BoogieAirway;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieLeg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;

public class AirwayConverterTest {
  private final static BoogieFix fix = new BoogieFix.Builder()
      .fixIdentifier("DAVID")
      .fixRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .publishedVariation(15D)
      .build();

  private static final BoogieLeg leg = new BoogieLeg.Builder()
      .associatedFix(fix)
      .pathTerminator(PathTerminator.TF)
      .sequenceNumber(10)
      .isFlyOverFix(false)
      .isPublishedHoldingFix(false)
      .altitudeConstraint(Range.all())
      .speedConstraint(Range.all())
      .build();

  private final static BoogieAirway airway = new BoogieAirway.Builder()
      .airwayIdentifier("J1")
      .airwayRegion("K1")
      .legs(List.of(leg))
      .build();

  private static final ConvertAirway airwayConverter = ConvertAirway.INSTANCE;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testAirwayConvert() throws JsonProcessingException {
    Airway convertedAirway = airwayConverter.apply(airway);
    assertAll("Checking the airway",
        () -> assertNotNull(convertedAirway),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(convertedAirway)),
        () -> assertEquals("J1", convertedAirway.airwayIdentifier()),
        () -> assertEquals("K1", convertedAirway.airwayRegion()),
        () -> assertFalse(convertedAirway.legs().isEmpty())
    );
  }
}
