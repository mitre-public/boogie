package org.mitre.tdp.boogie.airspace.fixtures;

import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.BoogieType;
import org.mitre.tdp.boogie.CenterIdentification;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.MagneticVariation;

import com.google.common.collect.Range;

/**
 * Boise Class C's outer shelf, transcribed from {@code boogie-arinc/src/test/resources/controlled.txt}, AIRAC 2210.
 * Lines 2-7 (file records 89077-89082) define component B; line 8 supplies the KBOI airport reference point.
 *
 * <p>The source boundary fields are retained here for comparison with the decimal-degree fixture:
 * <pre>
 * Seq Via Associated fix        Arc origin             Radius NM Bearing deg
 *  10 G   N43331120 W116063326
 *  20 R   N43322935 W115594739    N43335400 W116132200    10.0      98.0
 *  30 G   N43235500 W116141300
 *  40 G   N43271500 W116135900
 *  50 L   N43285276 W116135209    N43335200 W116132200     5.0     184.1
 *  60 GE  N43331120 W116063326
 * </pre>
 * Sequence 60 intentionally repeats sequence 10's fix: this duplicate is in the source, before projection or compilation.
 * The two arc centers also intentionally differ by two seconds of latitude, as published.
 */
public final class BoiseClassCAirspace {

  private BoiseClassCAirspace() {}

  public static Airspace.Standard outerShelf() {
    return Airspace.builder()
        .area("USA")
        .identifier("KBOI-A-K1-ANM ID C BOISE AIR TERMINAL (B-B-C")
        .airspaceType(AirspaceType.CONTROLLED)
        .altitudeLimit(Range.closed(4600.0, 6900.0))
        .centerIdentification(CenterIdentification.builder("KBOI")
            .area("USA").icaoRegion("K1").type(BoogieType.AIRPORT).build())
        .center(Fix.builder().fixIdentifier("KBOI")
            .latLong(LatLong.of(43.56436111111111, -116.22286111111111))
            .magneticVariation(MagneticVariation.ofDegrees(13.0)).build())
        .sequences(List.of(
            AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 10)
                .associatedFix(LatLong.of(43.55311111111111, -116.10923888888888))
                .build(),
            AirspaceSequence.builder(Geometry.CLOCKWISE_ARC, 20)
                .associatedFix(LatLong.of(43.54148611111111, -115.99649722222222))
                .centerFix(LatLong.of(43.565, -116.22277777777778))
                .arcRadius(10.0).arcBearing(98.0)
                .build(),
            AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 30)
                .associatedFix(LatLong.of(43.39861111111111, -116.23694444444445))
                .build(),
            AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 40)
                .associatedFix(LatLong.of(43.45416666666667, -116.23305555555555))
                .build(),
            AirspaceSequence.builder(Geometry.COUNTER_CLOCKWISE_ARC, 50)
                .associatedFix(LatLong.of(43.481322222222225, -116.23113611111111))
                .centerFix(LatLong.of(43.56444444444444, -116.22277777777778))
                .arcRadius(5.0).arcBearing(184.1)
                .build(),
            AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 60)
                .associatedFix(LatLong.of(43.55311111111111, -116.10923888888888))
                .build()))
        .build();
  }
}
