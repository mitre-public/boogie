package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincTransitionTypeClassifier {

  private static final ArincTransitionTypeClassifier classifier = new ArincTransitionTypeClassifier();

  @Test
  void testSidTransitionLabeling() {
    ArincProcedureLeg common = newProcedureLeg("D", "2").build();
    ArincProcedureLeg enroute = newProcedureLeg("D", "3").build();
    ArincProcedureLeg runway = newProcedureLeg("D", "1").build();

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common))),
        () -> assertEquals(TransitionType.ENROUTE, classifier.apply(singletonList(enroute))),
        () -> assertEquals(TransitionType.RUNWAY, classifier.apply(singletonList(runway)))
    );
  }

  @Test
  void testStarTransitionLabeling() {
    ArincProcedureLeg common = newProcedureLeg("E", "2").build();
    ArincProcedureLeg enroute = newProcedureLeg("E", "1").build();
    ArincProcedureLeg runway = newProcedureLeg("E", "3").build();

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common))),
        () -> assertEquals(TransitionType.ENROUTE, classifier.apply(singletonList(enroute))),
        () -> assertEquals(TransitionType.RUNWAY, classifier.apply(singletonList(runway)))
    );
  }

  @Test
  void testApproachTransitionLabeling() {
    ArincProcedureLeg common1 = newProcedureLeg("F", "R").transitionIdentifier(null).build();
    ArincProcedureLeg common2 = newProcedureLeg("F", "R").transitionIdentifier("").build();
    ArincProcedureLeg common3 = newProcedureLeg("F", "R").transitionIdentifier("ALL").build();
    ArincProcedureLeg approach = newProcedureLeg("F", "R").transitionIdentifier("GOROC").build();
    ArincProcedureLeg missed = newProcedureLeg("F", "R").waypointDescription("  M ").build();

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common1))),
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common2))),
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common3))),
        () -> assertEquals(TransitionType.APPROACH, classifier.apply(singletonList(approach))),
        () -> assertEquals(TransitionType.MISSED, classifier.apply(singletonList(missed)))
    );
  }

  private ArincProcedureLeg.Builder newProcedureLeg(String subSection, String routeType) {
    return new ArincProcedureLeg.Builder()
        .sidStarIdentifier("MOCK")
        .airportIdentifier("MOCK")
        .airportIcaoRegion("MOCK")
        .sectionCode(SectionCode.P)
        .subSectionCode(subSection)
        .routeType(routeType);
  }
}
