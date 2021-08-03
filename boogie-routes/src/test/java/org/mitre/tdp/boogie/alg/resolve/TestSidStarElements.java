package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedure;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.util.Iterators;

class TestSidStarElements {

  @Test
  void testProcedureElementFilteredReturnsNoLegsIfNoTransitionsPass() {
    SidElement sid = new SidElement(new TransitionMaskedProcedure(singleTransitionProcedureGraph(ProcedureType.SID), transition -> false));
    StarElement star = new StarElement(new TransitionMaskedProcedure(singleTransitionProcedureGraph(ProcedureType.STAR), transition -> false));

    assertAll(
        () -> assertEquals(emptyList(), sid.toLinkedLegs()),
        () -> assertEquals(emptyList(), star.toLinkedLegs())
    );
  }

  @Test
  void testStarElementSingleTransitionLegIdentifiersAndWeights() {
    ProcedureGraph pg = singleTransitionProcedureGraph(ProcedureType.STAR);

    ProcedureElement element = new StarElement(pg);
    assertEquals(2, element.toLinkedLegs().size());

    List<LinkedLegs> linked = element.toLinkedLegs();
    assertTrue(allMatch(linked, leg -> leg.linkWeight() == LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));

    LinkedLegs ll1 = linked.get(0);
    LinkedLegs ll2 = linked.get(1);

    assertAll(
        () -> assertEquals(ll1.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZV"),
        () -> assertEquals(ll1.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZY"),
        () -> assertEquals(ll2.source(), ll1.target()),
        () -> assertEquals(ll2.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZZ")
    );
  }

  @Test
  void testSidElementSingleTransitionLegIdentifiersAndWeights() {
    ProcedureGraph pg = singleTransitionProcedureGraph(ProcedureType.SID);

    ProcedureElement element = new SidElement(pg);
    assertEquals(2, element.toLinkedLegs().size());

    List<LinkedLegs> linked = element.toLinkedLegs();
    assertTrue(allMatch(linked, leg -> leg.linkWeight() == LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));

    LinkedLegs ll1 = linked.get(0);
    LinkedLegs ll2 = linked.get(1);

    assertAll(
        () -> assertEquals(ll1.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZV"),
        () -> assertEquals(ll1.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZY"),
        () -> assertEquals(ll2.source(), ll1.target()),
        () -> assertEquals(ll2.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZZ")
    );
  }

  @Test
  void testProcedureElementSubsequentLegLinkReferences() {
    ProcedureGraph pg = singleTransitionProcedureGraph(ProcedureType.STAR);

    ProcedureElement element = new StarElement(pg);

    List<LinkedLegs> linked = element.toLinkedLegs();

    Iterators.pairwise(linked, (ll1, ll2) -> {
      assertEquals(ll1.target(), ll2.source(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.target().hashCode(), ll2.source().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });
  }

  // This is really about checking the linking of the various transitions to each-other in a sane way
  @Test
  void testProcedureElementMultiTransition() {
    ProcedureGraph pg = nominalGraph();

    ProcedureElement element = new StarElement(pg);

    List<LinkedLegs> linked = element.toLinkedLegs();

    assertAll(
        () -> assertEquals(pg.edgeSet().size(), linked.size()),
        () -> assertTrue(legMatches(linked, "AAA", PathTerminator.IF, "BBB", PathTerminator.TF)),
        () -> assertTrue(legMatches(linked, "BBB", PathTerminator.IF, "CCC", PathTerminator.TF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerminator.TF, "CCC", PathTerminator.IF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerminator.IF, "DDD", PathTerminator.TF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerminator.IF, "EEE", PathTerminator.TF))
    );
  }

  private boolean legMatches(List<LinkedLegs> legs, String sourceName, PathTerminator sourceType, String targetName, PathTerminator targetType) {
    return legs.stream()
        .filter(ll -> ll.source().associatedFix().isPresent())
        .filter(ll -> ll.target().associatedFix().isPresent())
        .filter(ll -> sourceName.equals(ll.source().associatedFix().map(Fix::fixIdentifier).orElse(null)))
        .filter(ll -> targetName.equals(ll.target().associatedFix().map(Fix::fixIdentifier).orElse(null)))
        .anyMatch(ll -> ll.source().pathTerminator().equals(sourceType) && ll.target().pathTerminator().equals(targetType));
  }

  private static ProcedureGraph singleTransitionProcedureGraph(ProcedureType procedureType) {
    Leg l1 = IF("ZZV", 0.0, 0.0);
    Leg l2 = TF("ZZY", 0.0, 1.0);
    Leg l3 = TF("ZZZ", 0.0, 2.0);

    Transition transition = mock(Transition.class);
    when(transition.airportIdentifier()).thenReturn("MOCK");
    when(transition.airportRegion()).thenReturn("MOCK");
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);
    when(transition.legs()).thenReturn((List) Arrays.asList(l1, l2, l3));
    when(transition.procedureIdentifier()).thenReturn("GNDLF1");
    when(transition.procedureType()).thenReturn(procedureType);

    return newProcedureGraph(newProcedure(singletonList(transition)));
  }

  private static ProcedureGraph nominalGraph() {

    Leg l1_1 = IF("AAA", 0.0, 0.0);
    Leg l1_2 = TF("BBB", 0.0, 0.1);
    Transition ab = transition("B", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    Leg l2_1 = IF("BBB", 0.0, 0.2);
    Leg l2_2 = TF("CCC", 0.0, 0.4);
    Transition bc = transition("C", "ALPHA1", "APT", TransitionType.COMMON, ProcedureType.STAR, Arrays.asList(l2_1, l2_2));

    Leg l3_1 = IF("CCC", 0.0, 0.4);
    Leg l3_2 = TF("DDD", 0.0, 0.5);
    Transition cd = transition("D", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("CCC", 0.0, 0.4);
    Leg l4_2 = TF("EEE", 0.0, 0.5);
    Transition ce = transition("E", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    return newProcedureGraph(newProcedure(Arrays.asList(ab, bc, cd, ce)));
  }
}
