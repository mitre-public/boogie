package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.reflect.ModelGenerators;

class RunGenerator {

  @Test
  void runGenerator() {
    ModelGenerators.tdpBusiness().generwriteStdout(String.class);
  }
}
