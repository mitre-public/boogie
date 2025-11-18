package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.model.ProcedureGraph;

class ProcedureGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  /**
   * It is possible to mask all the transitions of a procedure when looking and common + enroute vs runway only versions so the
   * grapher must be robust to this.
   */
  @Test
  void test_NoTransitions() {
    Procedure procedure = mock(Procedure.class);

    Collection<LinkedLegs> linked = GRAPHER.graphRepresentationOf(ResolvedToken.sidEnrouteCommon(procedure));
    assertEquals(0, linked.size(), "Should return 0 links and not throw.");
  }

  /**
   * Most of the work being done here is being done by the {@link ProcedureGraph} class.
   */
  @Test
  void test_Standard() {
    Collection<LinkedLegs> linked = GRAPHER.graphRepresentationOf(ResolvedToken.sidEnrouteCommon(CONNR5.INSTANCE));
    assertEquals(3, linked.size(), "Now returns three as the common ends with two ways out because of the two commons and non-common portions are filtered.");
  }
}
