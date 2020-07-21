package org.mitre.tdp.boogie.conformance.alg.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

public class TestGraphBasedLegReducer {

  @Test
  public void testReducerPrefersLegsReferencingCurrentAsTarget() {
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

    ConsecutiveLegs cl1 = mock(ConsecutiveLegs.class);
    when(cl1.current()).thenReturn(leg1);
    when(cl1.next()).thenReturn(Optional.of(leg2));

    ConsecutiveLegs cl2 = mock(ConsecutiveLegs.class);
    when(cl2.previous()).thenReturn(Optional.of(leg1));
    when(cl2.current()).thenReturn(leg2);
    when(cl2.next()).thenReturn(Optional.of(leg3));

    GraphBasedLegReducer reducer = GraphBasedLegReducer.newInstance();
    List<ConsecutiveLegs> resultantLegs = reducer.reduce(Arrays.asList(cl1, cl2));

    assertEquals(1, resultantLegs.size());
  }
}
