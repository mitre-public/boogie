package org.mitre.tdp.boogie.conformance.alg.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

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
    List<FlyableLeg> resultantLegs = reducer.flyableLegs();

    assertEquals(3, resultantLegs.size());

    assertEquals(Sets.newHashSet(leg1, leg2), resultantLegs.stream().map(leg -> leg.previous().orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));
    assertEquals(Sets.newHashSet(leg2, leg3), resultantLegs.stream().map(leg -> leg.next().orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet()));

    assertEquals(source1, resultantLegs.get(0).getSourceObject().get());
    assertEquals(source1, resultantLegs.get(1).getSourceObject().get());
    assertEquals(source2, resultantLegs.get(2).getSourceObject().get());
  }
}
