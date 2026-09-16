package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

class AirwayAssemblyStrategyTest {

  @ParameterizedTest
  @CsvSource({"100,10", "050,5", "040,4", "020,2", "010,1", "031,0.3", "152,0.15"})
  void decodesSourceRnpToNauticalMiles(String encoded, double nauticalMiles) {
    var segment = segment().requiredNavPerformance(Integer.parseInt(encoded)).build();
    var leg = AirwayAssemblyStrategy.standard().convertLeg(segment, null);
    assertEquals(nauticalMiles, leg.rnp().orElseThrow(), 1e-12);
  }

  @ParameterizedTest
  @CsvSource({"329.T,15,314", "329.T,-15,344", "5.T,15,350", "350.T,-15,5", "329.0,15,329"})
  void convertsTrueCoursesAndPreservesMagneticCourses(String course, double variation, double expected) {
    var fix = Fix.builder().fixIdentifier("START").latLong(LatLong.of(40.0, -70.0))
        .magneticVariation(MagneticVariation.ofDegrees(variation)).build();
    var destination = Fix.builder().fixIdentifier("END").latLong(LatLong.of(41.0, -71.0))
        .magneticVariation(MagneticVariation.ofDegrees(25.0)).build();
    var leg = AirwayAssemblyStrategy.standard().convertLeg(segment().atsRouteOutboundMagneticCourse(course).build(), fix, destination);
    assertAll(
        () -> assertEquals(expected, leg.outboundMagneticCourse().orElseThrow(), 1e-12),
        () -> assertEquals(destination, leg.associatedFix().orElseThrow())
    );
  }

  @Test
  void leavesUnknownReferenceCoursesAbsentWithoutLosingSourceValues() {
    var trueSegment = segment().atsRouteOutboundMagneticCourse("329.T").build();
    var gridSegment = segment().atsRouteOutboundMagneticCourse("329.G").build();
    var strategy = AirwayAssemblyStrategy.standard();
    assertAll(
        () -> assertTrue(strategy.convertLeg(trueSegment, null).outboundMagneticCourse().isEmpty()),
        () -> assertTrue(strategy.convertLeg(gridSegment, null).outboundMagneticCourse().isEmpty()),
        () -> assertEquals("329.T", trueSegment.atsRouteOutboundMagneticCourse().orElseThrow()),
        () -> assertEquals("329.G", gridSegment.atsRouteOutboundMagneticCourse().orElseThrow()),
        () -> assertTrue(strategy.convertLeg(segment().build(), null).rnp().isEmpty())
    );
  }

  private static DafifAirTrafficSegment.Builder segment() {
    return new DafifAirTrafficSegment.Builder().atsRouteSequenceNumber(10);
  }
}
