package org.mitre.tdp.boogie.validate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ReferencedCourse;
import org.mitre.tdp.boogie.TurnDirection;

import com.google.common.collect.Range;

class TestPathTerminatorBasedLegValidator {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Test
  void testLegContentValidatorCoversAllPathTerminatorTypes() {
    assertTrue(Arrays.stream(PathTerminator.values()).noneMatch(pathTerminator -> {
      Leg leg = mockLeg(pathTerminator);
      return validator.test(leg);
    }), "The validator should have a method for each provided PathTerminator type - and with no fields set none should pass validation.");
  }

  @Test
  void testTrueCourseSatisfiesCourseToAltitudeRequirements() {
    Leg leg = Leg.builder(PathTerminator.CA, 1)
        .outboundTrueCourse(94.0)
        .altitudeConstraint(Range.atLeast(1000.0))
        .build();

    assertTrue(validator.test(leg));
  }

  @Test
  void testReferencedCourseFactorySatisfiesArcToFixRequirements() {
    Fix fix = Fix.builder()
        .fixIdentifier("FIX")
        .latLong(LatLong.of(0.0, 0.0))
        .magneticVariation(MagneticVariation.ZERO)
        .build();

    Leg leg = Leg.afBuilder(fix, fix, 1, ReferencedCourse.trueCourse(94.0))
        .theta(90.0)
        .rho(5.0)
        .turnDirection(TurnDirection.right())
        .build();

    assertTrue(validator.test(leg));
  }

  private Leg mockLeg(PathTerminator pathTerminator) {
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    return leg;
  }
}
