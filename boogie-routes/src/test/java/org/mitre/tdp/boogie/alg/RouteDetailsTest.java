package org.mitre.tdp.boogie.alg;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.reflect.ModelGenerators;

class RouteDetailsTest {

  @Test
  void testEqualsAndHashCode() {

    ModelGenerators.tdpBusiness().generwriteStdout(RouteDetails.class);
  }
}
