package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToAirport;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToFix;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
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

        () -> assertTrue(CONVERTER.isCandidatePair(create(PathTerminator.TF), create(ResolvedToken.directToLatLong(LatLong.of(0., 0.)))),
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

    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(terminator);

    return ResolvedLeg.create(
        RouteToken.standard("fix", 0.),
        new Dummy(),
        leg
    );
  }

  private ResolvedLeg create(ResolvedToken resolvedToken) {

    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(mockFix()));
    when(leg.pathTerminator()).thenReturn(PathTerminator.DF);

    return ResolvedLeg.create(
        RouteToken.standard("fix", 0.),
        resolvedToken,
        leg
    );
  }

  private Airport mockAirport() {
    Airport airport = mock(Airport.class);
    return airport;
  }

  private Fix mockFix() {
    Fix fix = mock(Fix.class);
    return fix;
  }

  private static final class Dummy implements ResolvedToken<Object> {

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
