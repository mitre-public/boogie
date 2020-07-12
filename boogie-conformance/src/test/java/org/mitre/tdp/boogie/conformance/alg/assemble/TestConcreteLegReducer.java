package org.mitre.tdp.boogie.conformance.alg.assemble;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

public class TestConcreteLegReducer {

  @Test
  public void testIsConcretePairFalseWhenNonConcreteCurrentLeg() {
    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    Leg current = mock(Leg.class);
    when(current.type()).thenReturn(PathTerm.VA);

    Leg previous = mock(Leg.class);
    when(previous.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs = mock(ConsecutiveLegs.class);
    when(consecutiveLegs.current()).thenReturn(current);
    when(consecutiveLegs.previous()).thenReturn(Optional.of(previous));

    assertFalse(reducer.isConcretePair(consecutiveLegs));
  }

  @Test
  public void testIsConcretePairFalseWhenNonConcretePreviousLeg() {
    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    Leg current = mock(Leg.class);
    when(current.type()).thenReturn(PathTerm.TF);

    Leg previous = mock(Leg.class);
    when(previous.type()).thenReturn(PathTerm.VA);

    ConsecutiveLegs consecutiveLegs = mock(ConsecutiveLegs.class);
    when(consecutiveLegs.current()).thenReturn(current);
    when(consecutiveLegs.previous()).thenReturn(Optional.of(previous));

    assertFalse(reducer.isConcretePair(consecutiveLegs));
  }

  @Test
  public void testIsConcretePairTrueWhenBothConcrete() {
    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    Leg current = mock(Leg.class);
    when(current.type()).thenReturn(PathTerm.RF);

    Leg previous = mock(Leg.class);
    when(previous.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs = mock(ConsecutiveLegs.class);
    when(consecutiveLegs.current()).thenReturn(current);
    when(consecutiveLegs.previous()).thenReturn(Optional.of(previous));

    assertTrue(reducer.isConcretePair(consecutiveLegs));
  }

  private Fix fixAt(LatLong location) {
    Fix pathTerm = mock(Fix.class);
    when(pathTerm.latLong()).thenReturn(location);
    when(pathTerm.latitude()).thenCallRealMethod();
    when(pathTerm.longitude()).thenCallRealMethod();
    when(pathTerm.distanceInNmTo(any())).thenCallRealMethod();
    return pathTerm;
  }

  @Test
  public void testReducingCollocatedReducesWhenNearby() {
    Fix pathTerm = fixAt(LatLong.of(1.0, 1.0));

    Leg current = mock(Leg.class);
    when(current.pathTerminator()).thenReturn(pathTerm);

    ConsecutiveLegs cl1 = mock(ConsecutiveLegs.class);
    when(cl1.current()).thenReturn(current);

    ConsecutiveLegs cl2 = mock(ConsecutiveLegs.class);
    when(cl2.current()).thenReturn(current);

    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    List<ConsecutiveLegs> reduced = reducer.reduceCollocated(Arrays.asList(cl1, cl2));
    assertEquals(1, reduced.size());
  }

  @Test
  public void testReducingCollocatedLeavesApartWhenSeparate() {
    Fix pathTerm1 = fixAt(LatLong.of(1.0, 1.0));

    Leg current1 = mock(Leg.class);
    when(current1.pathTerminator()).thenReturn(pathTerm1);

    ConsecutiveLegs cl1 = mock(ConsecutiveLegs.class);
    when(cl1.current()).thenReturn(current1);

    Fix pathTerm2 = fixAt(LatLong.of(50.0, 100.0));

    Leg current2 = mock(Leg.class);
    when(current2.pathTerminator()).thenReturn(pathTerm2);

    ConsecutiveLegs cl2 = mock(ConsecutiveLegs.class);
    when(cl2.current()).thenReturn(current2);

    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    List<ConsecutiveLegs> reduced = reducer.reduceCollocated(Arrays.asList(cl1, cl2));
    assertEquals(2, reduced.size());
  }

  @Test
  public void testReducesEmptyInputsToEmptyOutput() {
    assertEquals(emptyList(), ConcreteLegReducer.newInstance().reduce(emptyList()));
  }

  @Test
  public void testReducesTestLegsCorrectly() {
    List<ConsecutiveLegs> allLegs = testLegs();

    ConcreteLegReducer reducer = ConcreteLegReducer.newInstance();

    List<? extends ConsecutiveLegs> reduced = reducer.reduce(allLegs);
    assertEquals(4, reduced.size());
  }

  /**
   * Collection of testing legs 1-5
   *
   * Legs 1,2 are collocated and have the same identifiers - these should be reduced
   * Leg 3 is collocated with 1,2 but has a different identifier - shouldn't be reduced
   * Leg 4 is not collocated with any other legs but has the same identifier as 1,2 - shouldn't be reduced
   * Leg 5 is not collocated with any others and has a different identifier that all others shouldn't be reduced
   */
  private List<ConsecutiveLegs> testLegs() {
    Fix pathTerm1 = fixAt(LatLong.of(0.0, 0.0));
    when(pathTerm1.identifier()).thenReturn("F1");

    Leg current1 = mock(Leg.class);
    when(current1.pathTerminator()).thenReturn(pathTerm1);
    when(current1.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs1 = mock(ConsecutiveLegs.class);
    when(consecutiveLegs1.current()).thenReturn(current1);
    when(consecutiveLegs1.previous()).thenReturn(Optional.of(current1));

    Fix pathTerm2 = fixAt(LatLong.of(0.0, 0.0));
    when(pathTerm2.identifier()).thenReturn("F1");

    Leg current2 = mock(Leg.class);
    when(current2.pathTerminator()).thenReturn(pathTerm2);
    when(current2.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs2 = mock(ConsecutiveLegs.class);
    when(consecutiveLegs2.current()).thenReturn(current2);
    when(consecutiveLegs2.previous()).thenReturn(Optional.of(current2));

    Fix pathTerm3 = fixAt(LatLong.of(0.0, 0.0));
    when(pathTerm3.identifier()).thenReturn("F3");

    Leg current3 = mock(Leg.class);
    when(current3.pathTerminator()).thenReturn(pathTerm3);
    when(current3.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs3 = mock(ConsecutiveLegs.class);
    when(consecutiveLegs3.current()).thenReturn(current3);
    when(consecutiveLegs3.previous()).thenReturn(Optional.of(current3));

    Fix pathTerm4 = fixAt(LatLong.of(1.0, 1.0));
    when(pathTerm4.identifier()).thenReturn("F1");

    Leg current4 = mock(Leg.class);
    when(current4.pathTerminator()).thenReturn(pathTerm4);
    when(current4.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs4 = mock(ConsecutiveLegs.class);
    when(consecutiveLegs4.current()).thenReturn(current4);
    when(consecutiveLegs4.previous()).thenReturn(Optional.of(current4));

    Fix pathTerm5 = fixAt(LatLong.of(50.0, 50.0));
    when(pathTerm5.identifier()).thenReturn("F5");

    Leg current5 = mock(Leg.class);
    when(current5.pathTerminator()).thenReturn(pathTerm5);
    when(current5.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs consecutiveLegs5 = mock(ConsecutiveLegs.class);
    when(consecutiveLegs5.current()).thenReturn(current5);
    when(consecutiveLegs5.previous()).thenReturn(Optional.of(current5));

    return Arrays.asList(consecutiveLegs1, consecutiveLegs2, consecutiveLegs3, consecutiveLegs4, consecutiveLegs5);
  }
}
