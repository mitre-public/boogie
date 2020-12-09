package org.mitre.tdp.boogie.conformance.alg.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Sets;

public class TestGraphicalLegReducer {

  @Test
  public void testResultantFlyableLegs() {
    Object source1 = new Object();
    Object source2 = new Object();

    Fix pathTerminator1 = mock(Fix.class);
    when(pathTerminator1.identifier()).thenReturn("1");

    Fix pathTerminator2 = mock(Fix.class);
    when(pathTerminator2.identifier()).thenReturn("2");

    Fix pathTerminator3 = mock(Fix.class);
    when(pathTerminator3.identifier()).thenReturn("3");

    Leg leg1 = mock(Leg.class);
    when(leg1.type()).thenReturn(PathTerm.TF);
    when(leg1.pathTerminator()).thenReturn(pathTerminator1);

    Leg leg2 = mock(Leg.class);
    when(leg2.type()).thenReturn(PathTerm.TF);
    when(leg2.pathTerminator()).thenReturn(pathTerminator2);

    Leg leg3 = mock(Leg.class);
    when(leg3.type()).thenReturn(PathTerm.TF);
    when(leg3.pathTerminator()).thenReturn(pathTerminator3);

    LegPair cl1 = new LegPairImpl(leg1, leg2).setSourceObject(source1);
    LegPair cl2 = new LegPairImpl(leg2, leg3).setSourceObject(source2);

    GraphicalLegReducer reducer = new GraphicalLegReducer(
        LegHashers.byIdentifier()
            .andThenBy(LegHashers.byLocation())
            .andThenBy(LegHashers.byType())
            .orElseBy(LegHashers.byHashCode())
    );

    Arrays.asList(cl1, cl2).forEach(reducer::addLegPair);
    List<FlyableLeg> resultantLegs = reducer.flyableLegList();

    assertEquals(3, resultantLegs.size());

    assertEquals(Sets.newHashSet(leg1, leg2), resultantLegs.stream().map(leg -> leg.previous().orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));
    assertEquals(Sets.newHashSet(leg2, leg3), resultantLegs.stream().map(leg -> leg.next().orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));

    assertEquals(source1, resultantLegs.get(0).getSourceObject().get());
    assertEquals(source1, resultantLegs.get(1).getSourceObject().get());
    assertEquals(source2, resultantLegs.get(2).getSourceObject().get());
  }

  @Test
  void testArcCenterHasher() {
    Fix pathTerminator = mock(Fix.class, "F");
    when(pathTerminator.identifier()).thenReturn("F");

    Fix rfCenter1 = mock(Fix.class, "C1");
    when(rfCenter1.identifier()).thenReturn("C1");
    when(rfCenter1.latLong()).thenReturn(LatLong.of(0., 1.));

    Fix rfCenter2 = mock(Fix.class, "C2");
    when(rfCenter2.identifier()).thenReturn("C2");
    when(rfCenter2.latLong()).thenReturn(LatLong.of(0., 2.));

    Leg leg1 = mock(Leg.class, "leg1");
    when(leg1.type()).thenReturn(PathTerm.RF);
    when(leg1.pathTerminator()).thenReturn(pathTerminator);
    when(leg1.centerFix()).then((Answer<Optional<Fix>>) (x -> Optional.of(rfCenter1)));

    Leg leg1b = mock(Leg.class, "leg1b");
    when(leg1b.type()).thenReturn(PathTerm.RF);
    when(leg1b.pathTerminator()).thenReturn(pathTerminator);
    when(leg1b.centerFix()).then((Answer<Optional<Fix>>) (x -> Optional.of(rfCenter1)));

    Leg leg2 = mock(Leg.class, "leg2");
    when(leg2.type()).thenReturn(PathTerm.RF);
    when(leg2.pathTerminator()).thenReturn(pathTerminator);
    when(leg2.centerFix()).then((Answer<Optional<Fix>>) (x -> Optional.of(rfCenter2)));

    LegHasher hasher = LegHashers.byIdentifier()
        .andThenBy(LegHashers.byArcCenter())
        .orElseBy(LegHashers.byHashCode());

    assertEquals(hasher.hash(leg1), hasher.hash(leg1b),
        "Arc legs with matching center fixes should have matching hashes");
    assertNotEquals(hasher.hash(leg1), hasher.hash(leg2),
        "Arc legs with different center fixes should have different hashes");
  }
}
