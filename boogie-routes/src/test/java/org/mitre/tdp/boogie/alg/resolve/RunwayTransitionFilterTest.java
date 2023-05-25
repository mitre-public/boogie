package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.COSTR3;
import org.mitre.tdp.boogie.HOBTT2;
import org.mitre.tdp.boogie.Transition;

class RunwayTransitionFilterTest {

  @ParameterizedTest
  @MethodSource("userProcedureMatch")
  void testMatches(String name, Transition transition) {
    RunwayTransitionFilter filter = new RunwayTransitionFilter(name);
    boolean all = filter.test(transition);
    assertTrue(all);
  }

  static Stream<Arguments> userProcedureMatch() {
    return Stream.of(
        arguments("RW17L", CONNR5.INSTANCE.get("RW17L")),
        arguments("RW26", HOBTT2.INSTANCE.get("RW26B"))
    );
  }

  @ParameterizedTest
  @MethodSource("userProcedureNoMatch")
  void testNoMatch(String name, Transition transition) {
    RunwayTransitionFilter filter = new RunwayTransitionFilter(name);
    boolean all = filter.test(transition);
    assertFalse(all);
  }

  static Stream<Arguments> userProcedureNoMatch() {
    return Stream.of(
        arguments("RW23", COSTR3.INSTANCE.get("ALL")),
        arguments("RW24", CONNR5.INSTANCE.get("RW17L")),
        arguments("RW30", HOBTT2.INSTANCE.get("RW26B"))
    );
  }


}
