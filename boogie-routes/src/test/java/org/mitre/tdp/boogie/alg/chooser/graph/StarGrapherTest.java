package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedure;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.model.BoogieTransition;
import org.mitre.tdp.boogie.model.ProcedureGraph;

class StarGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_CommonOrEnroute() {

    ResolvedToken.StarEnrouteCommon token = ResolvedToken.starEnrouteCommon(nominalGraph(ProcedureType.STAR));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(3, representation.size(), "Should have three links.");
  }

  @Test
  void test_Runway() {

    ResolvedToken.StarRunway token = ResolvedToken.starRunway(nominalGraph(ProcedureType.STAR));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(2, representation.size(), "Should have two links.");
  }

  private boolean containsMatchingLink(
      List<LinkedLegs> legs,
      String sourceName,
      PathTerminator sourceType,
      String targetName,
      PathTerminator targetType
  ) {
    return legs.stream()
        .filter(ll -> ll.source().associatedFix().isPresent())
        .filter(ll -> ll.target().associatedFix().isPresent())
        .filter(ll -> sourceName.equals(ll.source().associatedFix().map(Fix::fixIdentifier).orElse(null)))
        .filter(ll -> targetName.equals(ll.target().associatedFix().map(Fix::fixIdentifier).orElse(null)))
        .anyMatch(ll -> ll.source().pathTerminator().equals(sourceType) && ll.target().pathTerminator().equals(targetType));
  }

  private static ProcedureGraph nominalGraph(ProcedureType type) {

    Leg l1_1 = IF("AAA", 0.0, 0.0);
    Leg l1_2 = TF("BBB", 0.0, 0.1);
    BoogieTransition ab = transition("B", "ALPHA1", "APT", TransitionType.ENROUTE, type, Arrays.asList(l1_1, l1_2));

    Leg l2_1 = IF("BBB", 0.0, 0.2);
    Leg l2_2 = TF("CCC", 0.0, 0.4);
    BoogieTransition bc = transition("C", "ALPHA1", "APT", TransitionType.COMMON, type, Arrays.asList(l2_1, l2_2));

    Leg l3_1 = IF("CCC", 0.0, 0.4);
    Leg l3_2 = TF("DDD", 0.0, 0.5);
    BoogieTransition cd = transition("D", "ALPHA1", "APT", TransitionType.RUNWAY, type, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("CCC", 0.0, 0.4);
    Leg l4_2 = TF("EEE", 0.0, 0.5);
    BoogieTransition ce = transition("E", "ALPHA1", "APT", TransitionType.RUNWAY, type, Arrays.asList(l4_1, l4_2));

    return newProcedureGraph(newProcedure(Arrays.asList(ab, bc, cd, ce)));
  }
}
