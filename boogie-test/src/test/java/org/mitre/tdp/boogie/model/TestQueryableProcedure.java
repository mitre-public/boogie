package org.mitre.tdp.boogie.model;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.test.CONNR5;
import org.mitre.tdp.boogie.test.HOBTT2;

class TestQueryableProcedure {

  @Test
  void testQueryableSIDProcedure() {
    QueryableProcedure procedure = new QueryableProcedure(CONNR5.INSTANCE);

    Set<PathTerminator> initialLegTerminators = procedure.initialProcedureLegs().stream().map(Leg::pathTerminator).collect(Collectors.toSet());
    Set<String> finalLegIdentifiers = procedure.finalProcedureLegs().stream().map(leg -> leg.associatedFix().map(Fix::fixIdentifier).orElse(null)).collect(Collectors.toSet());

    assertAll(
        () -> assertEquals(Optional.of(TransitionType.COMMON), procedure.transitionWithName(null).map(Transition::transitionType)),
        () -> assertEquals(Optional.of(TransitionType.RUNWAY), procedure.transitionWithName("RW17L").map(Transition::transitionType)),
        () -> assertEquals(Optional.empty(), procedure.transitionWithName("ENROUTE").map(Transition::transitionType)),
        () -> assertFalse(procedure.transitionsOfType(TransitionType.COMMON).isEmpty()),
        () -> assertFalse(procedure.transitionsOfType(TransitionType.RUNWAY).isEmpty()),
        () -> assertEquals(emptySet(), procedure.transitionsOfType(TransitionType.ENROUTE)),
        () -> assertEquals(emptySet(), procedure.transitionsOfType(TransitionType.APPROACH)),
        () -> assertTrue(procedure.containsFixWithIdentifier("GOROC")),
        () -> assertFalse(procedure.containsFixWithIdentifier("HELLO")),
        () -> assertEquals(Optional.of(PathTerminator.VA), procedure.firstLegOfTransition("RW08").map(Leg::pathTerminator)),
        () -> assertEquals(Optional.of("CONNR"), procedure.lastLegOfTransition("RW08").flatMap(Leg::associatedFix).map(Fix::fixIdentifier)),
        () -> assertEquals(newHashSet(PathTerminator.VI, PathTerminator.VA), initialLegTerminators, "Should grab the initial leg of the runway transitions."),
        () -> assertEquals(newHashSet("DBL"), finalLegIdentifiers, "Should grab the last fix of the common portion (as CONNR5 has no enroute transitions)."),
        () -> assertEquals(Optional.of("GOROC"), procedure.fixWithIdentifier("GOROC").map(Fix::fixIdentifier)),
        () -> assertEquals(9, procedure.fixesWithIdentifier("CONNR").size()),
        () -> assertEquals(4, procedure.associatedLegsOf("GOROC").size()),
        () -> assertEquals(newHashSet(PathTerminator.CF), procedure.associatedLegsOf("GOROC").stream().map(Leg::pathTerminator).collect(Collectors.toSet())),
        () -> assertEquals(9, procedure.associatedLegsOf("CONNR").size()),
        () -> assertEquals(newHashSet(PathTerminator.TF, PathTerminator.IF, PathTerminator.DF), procedure.associatedLegsOf("CONNR").stream().map(Leg::pathTerminator).collect(Collectors.toSet()))
    );
  }

  @Test
  void testQueryableSTARProcedure(){
    QueryableProcedure procedure = new QueryableProcedure(HOBTT2.INSTANCE);

    Set<String> initialLegIdentifiers = procedure.initialProcedureLegs().stream().map(leg -> leg.associatedFix().map(Fix::fixIdentifier).orElse(null)).collect(Collectors.toSet());
    Set<String> finalLegIdentifiers = procedure.finalProcedureLegs().stream().map(leg -> leg.associatedFix().map(Fix::fixIdentifier).orElse(null)).collect(Collectors.toSet());

    assertAll(
        () -> assertEquals(newHashSet("DRSDN", "KHMYA", "COOUP", "BEORN", "FRDDO", "ENNTT", "ORRKK", "GOLLM", "STRDR", "SHYRE"), initialLegIdentifiers, "Should grab the first legs of the enroute transitions."),
        () -> assertEquals(newHashSet("YURII", "KEAVY"), finalLegIdentifiers, "Should grab the last fixes of the runway transitions.")
    );
  }
}
