package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;

public class TestLinearSplitErrorEvaluator {

  @Test
  public void test(){
    System.out.println(Distance.ofNauticalMiles(1).dividedBy(Duration.ofMinutes(6)));
  }
}
