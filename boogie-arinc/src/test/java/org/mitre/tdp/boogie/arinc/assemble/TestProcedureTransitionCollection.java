package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mitre.caasd.commons.util.Partitioners.splitOnPairwiseChange;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

class TestProcedureTransitionCollection {

  @Test
  void alreadyMissedTransitionRemainsIntactWithRepeatedMarkersAndEqualLegs() {
    ArincProcedureLeg missed = leg(null, 10).waypointDescription("  M ").build();
    ArincProcedureLeg equalMissed = missed.toBuilder().build();
    assertNotSame(missed, equalMissed);
    assertEquals(missed, equalMissed);

    List<ArincProcedureLeg> input = List.of(missed, missed, equalMissed);
    CapturedProcedure result = assemble(input);

    assertEquals(1, result.transitions().size());
    CapturedTransition transition = result.transitions().get(0);
    assertEquals(TransitionType.MISSED, transition.type());
    assertEquals(input, transition.legs());
    assertSame(missed, transition.representative());
    for (int index = 0; index < input.size(); index++) {
      assertSame(input.get(index), transition.legs().get(index));
    }
  }

  @Test
  void alreadyMissedTransitionKeepsLegsDifferingOnlyInFileRecordNumber() {
    ArincProcedureLeg first = leg(null, 10).waypointDescription("  M ").build();
    ArincProcedureLeg second = first.toBuilder().fileRecordNumber(11).build();
    assertNotEquals(first, second);

    CapturedProcedure result = assemble(List.of(first, second));

    assertEquals(List.of(
        new CapturedTransition(first, TransitionType.MISSED, List.of(first, second))
    ), result.transitions());
  }

  @Test
  void equalMultiLegMissedTransitionsRemainOrderedAcrossAnInterveningPartition() {
    ArincProcedureLeg common = leg(null, 0).build();
    ArincProcedureLeg missed = leg(null, 10).waypointDescription("  M ").build();
    ArincProcedureLeg following = leg(null, 10).fileRecordNumber(11).build();
    ArincProcedureLeg alternate = missed.toBuilder().fileRecordNumber(12).build();
    ArincProcedureLeg repeatedMissed = missed.toBuilder().build();
    ArincProcedureLeg repeatedFollowing = following.toBuilder().build();
    List<ArincProcedureLeg> input = List.of(
        common, missed, following, alternate, repeatedMissed, repeatedFollowing);

    CapturedProcedure result = assemble(input);

    assertEquals(List.of(
        new CapturedTransition(common, TransitionType.COMMON, List.of(common)),
        new CapturedTransition(missed, TransitionType.MISSED, List.of(missed, following)),
        new CapturedTransition(alternate, TransitionType.MISSED, List.of(alternate)),
        new CapturedTransition(repeatedMissed, TransitionType.MISSED, List.of(repeatedMissed, repeatedFollowing))
    ), result.transitions());
    assertSame(missed, result.transitions().get(1).legs().get(0));
    assertSame(following, result.transitions().get(1).legs().get(1));
    assertSame(repeatedMissed, result.transitions().get(3).legs().get(0));
    assertSame(repeatedFollowing, result.transitions().get(3).legs().get(1));
  }

  @Test
  void finalRouteIsSortedBeforeSplittingAtTheMissedApproachMarker() {
    for (String identifier : new String[] {null, "", "   ", "ALL"}) {
      ArincProcedureLeg finalFirst = leg(identifier, 10).build();
      ArincProcedureLeg finalLast = leg(identifier, 20).build();
      ArincProcedureLeg missedFirst = leg(identifier, 30).waypointDescription("  M ").build();
      ArincProcedureLeg missedLast = leg(identifier, 40).build();

      CapturedProcedure result = assemble(List.of(missedLast, finalLast, missedFirst, finalFirst));

      assertEquals(List.of(
          new CapturedTransition(finalFirst, TransitionType.COMMON, List.of(finalFirst, finalLast)),
          new CapturedTransition(missedFirst, TransitionType.MISSED, List.of(missedFirst, missedLast))
      ), result.transitions(), "Final transition identifier: " + identifier);
    }
  }

  @Test
  void finalRouteWithoutAMissedApproachMarkerRemainsIntactAndSorted() {
    ArincProcedureLeg first = leg(null, 10).build();
    ArincProcedureLeg middle = leg(null, 20).build();
    ArincProcedureLeg last = leg(null, 30).build();

    CapturedProcedure result = assemble(List.of(last, first, middle));

    assertEquals(List.of(
        new CapturedTransition(first, TransitionType.COMMON, List.of(first, middle, last))
    ), result.transitions());
  }

  @Test
  void sidAndStarTransitionsIgnoreMissedApproachMarkersIncludingCommonRoutes() {
    for (String subsection : List.of("D", "E")) {
      for (String route : List.of("1", "2", "3")) {
        ArincProcedureLeg first = leg(null, 10).subSectionCode(subsection).routeType(route).build();
        ArincProcedureLeg marked = first.toBuilder().sequenceNumber(20).waypointDescription("  M ").build();
        TransitionType expectedType = switch (subsection + route) {
          case "D2", "E2" -> TransitionType.COMMON;
          case "D1", "E3" -> TransitionType.RUNWAY;
          default -> TransitionType.ENROUTE;
        };

        assertEquals(List.of(new CapturedTransition(first, expectedType, List.of(first, marked))),
            assemble(List.of(marked, first)).transitions(), "Subsection/route: " + subsection + route);
      }
    }
  }

  @Test
  void namedApproachTransitionRemainsIntactDespiteLaterMissedApproachMarker() {
    ArincProcedureLeg first = leg("ALPHA", 10).build();
    ArincProcedureLeg marked = leg("ALPHA", 20).waypointDescription("  M ").build();

    assertEquals(List.of(new CapturedTransition(first, TransitionType.APPROACH, List.of(first, marked))),
        assemble(List.of(marked, first)).transitions());
  }

  @Test
  void routeZMissedTransitionRemainsIntactDespiteLaterMissedApproachMarker() {
    ArincProcedureLeg first = leg(null, 10).routeType("Z").build();
    ArincProcedureLeg marked = first.toBuilder().sequenceNumber(20).waypointDescription("  M ").build();

    assertEquals(List.of(new CapturedTransition(first, TransitionType.MISSED, List.of(first, marked))),
        assemble(List.of(marked, first)).transitions());
  }

  @Test
  void duplicateOrdinaryLegsRemainInsideTheirTransition() {
    ArincProcedureLeg first = leg(null, 10).build();
    ArincProcedureLeg equalLeg = first.toBuilder().build();
    List<ArincProcedureLeg> input = List.of(first, first, equalLeg);

    CapturedProcedure result = assemble(input);

    assertEquals(1, result.transitions().size());
    assertEquals(input, result.transitions().get(0).legs());
    assertSame(equalLeg, result.transitions().get(0).legs().get(2));
  }

  @Test
  void sameNameCategoriesAndRouteZVariantsRemainSeparate() {
    List<ArincProcedureLeg> input = List.of(
        leg("SAME", 10).categoryOrType("A").build(),
        leg("SAME", 10).categoryOrType("C").build(),
        leg("SAME", 10).routeType("Z").routeTypeQualifier2("A").build(),
        leg("SAME", 10).routeType("Z").routeTypeQualifier2("B").build()
    );

    CapturedProcedure result = assemble(input);

    assertEquals(4, result.transitions().size());
    assertEquals(Set.of("A", "C"), result.transitions().stream()
        .filter(transition -> transition.type() == TransitionType.APPROACH)
        .map(transition -> transition.representative().categoryOrType().orElseThrow())
        .collect(Collectors.toSet()));
    assertEquals(Set.of("A", "B"), result.transitions().stream()
        .filter(transition -> transition.type() == TransitionType.MISSED)
        .map(transition -> transition.representative().routeTypeQualifier2().orElseThrow())
        .collect(Collectors.toSet()));
  }

  @Test
  void globalEncounterOrderIsRetainedWhenTransitionTypesAreInterleaved() {
    List<ArincProcedureLeg> input = List.of(
        leg(null, 10).categoryOrType("A").build(),
        leg(null, 20).categoryOrType("A").waypointDescription("  M ").build(),
        leg(null, 30).categoryOrType("B").build(),
        leg(null, 40).categoryOrType("B").waypointDescription("  M ").build(),
        leg(null, 50).categoryOrType("C").build(),
        leg(null, 60).categoryOrType("C").waypointDescription("  M ").build()
    );

    List<CapturedTransition> expected = legacyByType(input).entries().stream()
        .map(entry -> new CapturedTransition(entry.getValue().get(0), entry.getKey(), entry.getValue()))
        .toList();

    assertEquals(6, expected.size());
    assertEquals(expected, assemble(input).transitions());
  }

  @Test
  void commonLegDeterminesEquipageInsteadOfTheProcedureRepresentative() {
    ArincProcedureLeg approach = leg("ALPHA", 10).routeType("H").build();
    ArincProcedureLeg common = leg(null, 20).build();
    List<ArincProcedureLeg> input = List.of(approach, common);

    CapturedProcedure result = assemble(input);

    assertSame(approach, result.representative());
    assertEquals(RequiredNavigationEquipage.CONV, result.equipage());
    assertEquals(new ArincRequiredEquipageClassifier().apply(legacyByType(input)), result.equipage());
  }

  @Test
  void procedureRepresentativeUsesEncounterOrderWhileTransitionLegsRemainSorted() {
    ArincProcedureLeg encounteredFirst = leg("ALPHA", 90).routeType("H").build();
    ArincProcedureLeg lowestSequence = leg("ALPHA", 10).routeType("H").build();
    ArincProcedureLeg common = leg(null, 20).build();

    CapturedProcedure result = assemble(List.of(encounteredFirst, lowestSequence, common));
    CapturedTransition approach = result.transitions().stream()
        .filter(transition -> transition.type() == TransitionType.APPROACH)
        .findFirst().orElseThrow();

    assertSame(encounteredFirst, result.representative());
    assertSame(lowestSequence, approach.representative());
    assertEquals(List.of(lowestSequence, encounteredFirst), approach.legs());
    assertEquals(RequiredNavigationEquipage.CONV, result.equipage());
  }

  @Test
  void withoutCommonLegEquipageUsesTheFirstEncounteredTransition() {
    List<ArincProcedureLeg> input = List.of(
        leg("ALPHA", 10).subSectionCode("D").routeType("1").build(),
        leg("BRAVO", 20).subSectionCode("D").routeType("4").build(),
        leg("CHARLIE", 30).subSectionCode("D").routeType("S").build()
    );
    Multimap<TransitionType, List<ArincProcedureLeg>> legacy = legacyByType(input);
    ArincProcedureLeg firstEncountered = legacy.entries().iterator().next().getValue().get(0);
    RequiredNavigationEquipage expected = switch (firstEncountered.routeType()) {
      case "1" -> RequiredNavigationEquipage.CONV;
      case "4" -> RequiredNavigationEquipage.RNAV;
      case "S" -> RequiredNavigationEquipage.RNP;
      default -> throw new AssertionError("Unexpected test route type");
    };

    CapturedProcedure result = assemble(input);

    assertSame(firstEncountered, result.transitions().get(0).representative());
    assertEquals(expected, result.equipage());
  }

  /** Legacy order oracle for duplicate-free inputs with markers only in eligible COMMON approach routes. */
  private static Multimap<TransitionType, List<ArincProcedureLeg>> legacyByType(List<ArincProcedureLeg> input) {
    Multimap<TransitionType, List<ArincProcedureLeg>> result = LinkedHashMultimap.create();
    ArincTransitionTypeClassifier classifier = new ArincTransitionTypeClassifier();
    for (List<ArincProcedureLeg> grouped : ArincTransitionGrouper.INSTANCE.apply(input)) {
      List<List<ArincProcedureLeg>> partitions = splitOnPairwiseChange(
          grouped, (previous, next) -> !IsFirstLegOfMissedApproach.INSTANCE.test(next));
      for (List<ArincProcedureLeg> partition : partitions) {
        result.put(classifier.applySorted(partition), partition);
      }
    }
    return result;
  }

  private static CapturedProcedure assemble(List<ArincProcedureLeg> input) {
    List<CapturedProcedure> result = ProcedureAssembler.withStrategy(
        ArincDatabaseFactory.emptyTerminalAreaDatabase(),
        ArincDatabaseFactory.emptyFixDatabase(),
        FixAssemblyStrategy.standard(),
        CAPTURE
    ).assemble(input).toList();
    assertEquals(1, result.size());
    return result.get(0);
  }

  private static ArincProcedureLeg.Builder leg(String transitionIdentifier, int sequenceNumber) {
    return new ArincProcedureLeg.Builder()
        .sectionCode(SectionCode.P)
        .subSectionCode("F")
        .airportIdentifier("KTEST")
        .airportIcaoRegion("K1")
        .sidStarIdentifier("TEST")
        .routeType("I")
        .transitionIdentifier(transitionIdentifier)
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(sequenceNumber)
        .pathTerm(PathTerminator.IF);
  }

  private record CapturedProcedure(ArincProcedureLeg representative, RequiredNavigationEquipage equipage,
                                   List<CapturedTransition> transitions) {
  }

  private record CapturedTransition(ArincProcedureLeg representative, TransitionType type, List<ArincProcedureLeg> legs) {
  }

  private static final ProcedureAssemblyStrategy<CapturedProcedure, CapturedTransition, ArincProcedureLeg, Fix> CAPTURE =
      new ProcedureAssemblyStrategy<>() {
        @Override
        public CapturedProcedure convertProcedure(ArincProcedureLeg representative, RequiredNavigationEquipage equipage,
                                                  List<CapturedTransition> transitions) {
          return new CapturedProcedure(representative, equipage, transitions);
        }

        @Override
        public CapturedTransition convertTransition(ArincProcedureLeg representative, TransitionType type,
                                                    List<ArincProcedureLeg> legs) {
          return new CapturedTransition(representative, type, legs);
        }

        @Override
        public ArincProcedureLeg convertLeg(ArincProcedureLeg leg, Fix associatedFix, Fix recommendedNavaid, Fix centerFix) {
          return leg;
        }
      };
}
