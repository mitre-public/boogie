package org.mitre.tdp.boogie.airspace.fixtures;

import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;

import com.google.common.collect.Range;

/**
 * FIR/UIR boundaries from the LIDO AIRAC 2609 assignment error report (effective September 3, 2026).
 * Coordinates and outgoing geometries are retained from the source: G/GE are great circles, H/HE are rhumb lines,
 * and L is a counterclockwise arc. The final sequence closes to the first; E marks the source boundary's end.
 */
public final class Lido2609FirUirAirspaces {

  private Lido2609FirUirAirspaces() {}

  public static Airspace johannesburgOceanic() {
    return Airspace.builder()
        .area("AFR")
        .identifier("FAJO")
        .airspaceType(AirspaceType.FIR)
        .altitudeLimit(Range.atMost(65_000.0))
        .sequences(List.of(
            sequence(1, Geometry.GREAT_CIRCLE, -27.833333333333332, 35.0),
            sequence(2, Geometry.RHUMB_LINE, -30.0, 40.0),
            sequence(3, Geometry.RHUMB_LINE, -30.0, 57.0),
            sequence(4, Geometry.RHUMB_LINE, -45.0, 57.0),
            sequence(5, Geometry.RHUMB_LINE, -45.0, 75.0),
            sequence(6, Geometry.RHUMB_LINE, -65.0, 75.0),
            sequence(7, Geometry.RHUMB_LINE, -90.0, 75.0),
            sequence(8, Geometry.RHUMB_LINE, -90.0, -10.0),
            sequence(9, Geometry.RHUMB_LINE, -46.0, -10.0),
            sequence(10, Geometry.RHUMB_LINE, -39.0, -10.0),
            sequence(11, Geometry.RHUMB_LINE, -34.0, -10.0),
            sequence(12, Geometry.RHUMB_LINE, -32.93333333333333, -10.0),
            sequence(13, Geometry.GREAT_CIRCLE, -20.0, -10.0),
            sequence(14, Geometry.RHUMB_LINE, -18.0, -5.0),
            sequence(15, Geometry.GREAT_CIRCLE, -18.0, 10.0),
            sequence(16, Geometry.GREAT_CIRCLE, -22.500555555555554, 10.000036111111111),
            sequence(17, Geometry.RHUMB_LINE, -27.5, 10.0),
            sequence(18, Geometry.RHUMB_LINE, -27.5, 15.0),
            sequence(19, Geometry.RHUMB_LINE, -30.5, 15.0),
            sequence(20, Geometry.RHUMB_LINE, -37.0, 15.0),
            sequence(21, Geometry.RHUMB_LINE, -37.0, 22.0),
            sequence(22, Geometry.GREAT_CIRCLE, -37.0, 28.0),
            sequence(23, Geometry.GREAT_CIRCLE, -33.0, 32.0)))
        .build();
  }

  public static Airspace canariesUir() {
    return Airspace.builder()
        .area("AFR")
        .identifier("GCCC")
        .airspaceType(AirspaceType.UIR)
        .altitudeLimit(Range.atLeast(19_500.0))
        .sequences(List.of(
            sequence(1, Geometry.RHUMB_LINE, 30.0, -25.0),
            sequence(2, Geometry.GREAT_CIRCLE, 30.0, -20.0),
            AirspaceSequence.builder(Geometry.COUNTER_CLOCKWISE_ARC, 3)
                .associatedFix(LatLong.of(31.663366666666665, -17.421780555555557))
                .centerFix(LatLong.of(33.06861111111112, -16.358333333333334))
                .arcRadius(100.0).arcBearing(212.8)
                .build(),
            sequence(4, Geometry.GREAT_CIRCLE, 31.480869444444444, -15.748269444444443),
            sequence(5, Geometry.GREAT_CIRCLE, 30.0, -12.5),
            sequence(6, Geometry.RHUMB_LINE, 27.666666666666668, -13.166666666666666),
            sequence(7, Geometry.GREAT_CIRCLE, 27.666666666666668, -11.233333333333333),
            sequence(8, Geometry.GREAT_CIRCLE, 25.99863888888889, -12.003894444444445),
            sequence(9, Geometry.GREAT_CIRCLE, 25.65, -12.15),
            sequence(10, Geometry.GREAT_CIRCLE, 21.341233333333335, -14.017680555555556),
            sequence(11, Geometry.GREAT_CIRCLE, 21.333333333333332, -16.916666666666668),
            sequence(12, Geometry.GREAT_CIRCLE, 20.783333333333335, -17.066666666666666),
            sequence(13, Geometry.GREAT_CIRCLE, 19.0, -19.0),
            sequence(14, Geometry.GREAT_CIRCLE, 19.999025, -19.999936111111115),
            sequence(15, Geometry.RHUMB_LINE, 24.0, -25.0)))
        .build();
  }

  public static Airspace tahiti() {
    return Airspace.builder()
        .area("SPA")
        .identifier("NTTT")
        .airspaceType(AirspaceType.FIR)
        .altitudeLimit(Range.all())
        .sequences(List.of(
            sequence(1, Geometry.RHUMB_LINE, 3.5, -145.0),
            sequence(2, Geometry.RHUMB_LINE, 3.5, -120.0),
            sequence(3, Geometry.RHUMB_LINE, -30.0, -120.0),
            sequence(4, Geometry.RHUMB_LINE, -30.0, -157.0),
            sequence(5, Geometry.RHUMB_LINE, -5.0, -157.0),
            sequence(6, Geometry.GREAT_CIRCLE, -5.0, -155.0)))
        .build();
  }

  private static AirspaceSequence sequence(int number, Geometry geometry, double latitude, double longitude) {
    return AirspaceSequence.builder(geometry, number)
        .associatedFix(LatLong.of(latitude, longitude))
        .build();
  }
}
