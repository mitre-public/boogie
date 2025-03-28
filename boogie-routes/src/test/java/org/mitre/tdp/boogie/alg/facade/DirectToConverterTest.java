package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToAirport;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToFix;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class DirectToConverterTest {

  private static final DirectToConverter CONVERTER = new DirectToConverter();

  @Test
  void testIsCandidatePair() {
    assertAll(
        () -> assertFalse(CONVERTER.isCandidatePair(create(PathTerminator.TF), create(directToAirport(mockAirport()))),
            "DirectToAirport should not be registered as direct for the purposes of this converter."),

        () -> assertTrue(CONVERTER.isCandidatePair(create(PathTerminator.TF), create(directToFix(mockFix()))),
            "DirectToFix tokens should be convertable to TF legs when previous leg is fix-terminating."),

        () -> assertFalse(CONVERTER.isCandidatePair(create(PathTerminator.VA), create(directToFix(mockFix()))),
            "DirectToFix tokens should not be convertable to TF legs when previous leg is not fix-terminating"),

        () -> assertTrue(CONVERTER.isCandidatePair(create(PathTerminator.TF), create(ResolvedToken.directToLatLong("0000N/00000W", LatLong.of(0., 0.)))),
            "DirectToLatLong tokens should be convertable to TF legs when previous leg is fix-terminating.")
    );
  }

  @Test
  void testApply() {

    List<ResolvedLeg> initial = List.of(create(PathTerminator.DF), create(directToFix(mockFix())), create(directToAirport(mockAirport())));

    List<ResolvedLeg> modified = CONVERTER.apply(initial);
    assertAll(
        () -> assertSame(initial.get(0), modified.get(0), "Should be same first leg."),

        () -> assertSame(initial.get(1).leg().associatedFix().orElse(null), modified.get(1).leg().associatedFix().orElse(null), "Middle should be same associated fix."),
        () -> assertEquals(PathTerminator.TF, modified.get(1).leg().pathTerminator(), "Middle should have modified PathTerminator."),

        () -> assertSame(initial.get(2), modified.get(2), "Should be same final leg.")
    );
  }

  private ResolvedLeg create(PathTerminator terminator) {

    Leg leg = Leg.builder(terminator, 10).build();

    return ResolvedLeg.create(
        RouteToken.standard("fix", 0.),
        new Dummy(),
        leg
    );
  }

  private ResolvedLeg create(ResolvedToken resolvedToken) {

    Leg leg = Leg.dfBuilder(mockFix(), 10).build();

    return ResolvedLeg.create(
        RouteToken.standard("fix", 0.),
        resolvedToken,
        leg
    );
  }

  private Airport mockAirport() {
    return Airport.builder().airportIdentifier("MOCK").latLong(LatLong.of(0., 0.)).build();
  }

  private Fix mockFix() {
    return Fix.builder().fixIdentifier("MOCK").latLong(LatLong.of(0., 0.)).build();
  }

  private static final class Dummy implements ResolvedToken {

    private final Object object = new Object();

    @Override
    public String identifier() {
      return object.toString();
    }

    @Override
    public Object infrastructure() {
      return object;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      // do nothing
    }
  }
}
