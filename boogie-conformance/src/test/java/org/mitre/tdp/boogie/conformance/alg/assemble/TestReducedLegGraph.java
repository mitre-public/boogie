package org.mitre.tdp.boogie.conformance.alg.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

public class TestReducedLegGraph {

  @Test
  public void testReducerPrefersLegsReferencingCurrentAsTarget() {
    Object source1 = new Object();
    Object source2 = new Object();

    Fix pathTerminator1 = mock(Fix.class);
    Fix pathTerminator2 = mock(Fix.class);
    Fix pathTerminator3 = mock(Fix.class);

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

    ReducedLegGraph reducer = ReducedLegGraph.with(Arrays.asList(cl1, cl2));
    List<FlyableLeg> resultantLegs = reducer.allConformableLegs();

    assertEquals(1, resultantLegs.size());
  }
}
