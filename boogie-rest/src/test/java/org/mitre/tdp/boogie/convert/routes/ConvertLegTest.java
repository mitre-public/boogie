package org.mitre.tdp.boogie.convert.routes;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.contract.routes.Leg;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieLeg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;

public class ConvertLegTest {
  private final static org.mitre.tdp.boogie.Fix fix = new BoogieFix.Builder()
      .fixIdentifier("DAVID")
      .fixRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .publishedVariation(15D)
      .build();

  private static final org.mitre.tdp.boogie.Leg leg = new BoogieLeg.Builder()
      .associatedFix(fix)
      .pathTerminator(PathTerminator.TF)
      .sequenceNumber(10)
      .isFlyOverFix(false)
      .isPublishedHoldingFix(false)
      .altitudeConstraint(Range.all())
      .speedConstraint(Range.all())
      .outboundMagneticCourse(3D)
      .holdTime(Duration.ZERO)
      .verticalAngle(2.2)
      .theta(3d)
      .rho(4d)
      .turnDirection(TurnDirection.either())
      .build();

  private static final ConvertLeg LEG = ConvertLeg.INSTANCE;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testConvert() {
    Leg convertedLeg = LEG.apply(leg);
    assertAll("Checking leg stuff",
        () -> assertEquals("DB", convertedLeg.associatedFix().orElseThrow().fixRegion()),
        () -> assertEquals(PathTerminator.TF, convertedLeg.pathTerminator()),
        () -> assertEquals(10, convertedLeg.sequenceNumber()),
        () -> assertFalse(convertedLeg.isFlyOverFix()),
        () -> assertEquals(Duration.ZERO, convertedLeg.holdTime().orElseThrow()),
        () -> assertEquals(4d, convertedLeg.rho().orElseThrow()),
        () -> assertEquals(3d, convertedLeg.theta().orElseThrow()),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(convertedLeg))
        );
  }

}
