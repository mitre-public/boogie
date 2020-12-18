package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

import com.google.common.collect.Sets;

class TestHashCombinationStrategy {

  @Test
  public void testCombineByHash() {
    Route r1 = Collections::emptyList;
    Route r2 = Collections::emptyList;

    Leg leg = mock(Leg.class);
    Leg alt = mock(Leg.class);
    FlyableLeg f1 = new FlyableLeg(alt, leg, null, r1);
    FlyableLeg f2 = new FlyableLeg(null, leg, alt, r2);

    HashCombinationStrategy combinationStrategy = new HashCombinationStrategy(l -> l.current().hashCode());
    Map<FlyableLeg, FlyableLeg> mapping = combinationStrategy.combineSimilar(Arrays.asList(f1, f2));

    assertAll(
        () -> assertEquals(mapping.get(f1), mapping.get(f2)),
        () -> assertTrue(mapping.get(f1).routes().containsAll(Sets.newHashSet(r1, r2)))
    );
  }
}
