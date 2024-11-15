package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincTransitionTypeClassifier {

  private static final ArincTransitionTypeClassifier classifier = new ArincTransitionTypeClassifier();

  @Test
  void testSidTransitionLabeling() {
    ArincProcedureLeg common = newProcedureLeg("D", "2");
    ArincProcedureLeg enroute = newProcedureLeg("D", "3");
    ArincProcedureLeg runway = newProcedureLeg("D", "1");
    ArincProcedureLeg engineOut = newProcedureLeg("D", "0");

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common))),
        () -> assertEquals(TransitionType.ENROUTE, classifier.apply(singletonList(enroute))),
        () -> assertEquals(TransitionType.RUNWAY, classifier.apply(singletonList(runway))),
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(engineOut)))
    );
  }

  @Test
  void testStarTransitionLabeling() {
    ArincProcedureLeg common = newProcedureLeg("E", "2");
    ArincProcedureLeg enroute = newProcedureLeg("E", "1");
    ArincProcedureLeg runway = newProcedureLeg("E", "3");

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common))),
        () -> assertEquals(TransitionType.ENROUTE, classifier.apply(singletonList(enroute))),
        () -> assertEquals(TransitionType.RUNWAY, classifier.apply(singletonList(runway)))
    );
  }

  @Test
  void testApproachTransitionLabeling() {
    ArincProcedureLeg common1 = newProcedureLeg("F", "R");
    ArincProcedureLeg common2 = newProcedureLeg("F", "R", "", null);
    ArincProcedureLeg common3 = newProcedureLeg("F", "R", "ALL", null);
    ArincProcedureLeg approach = newProcedureLeg("F", "R", "GOROC", null);
    ArincProcedureLeg missed = newProcedureLeg("F", "R", null, "  M ");

    assertAll(
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common1))),
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common2))),
        () -> assertEquals(TransitionType.COMMON, classifier.apply(singletonList(common3))),
        () -> assertEquals(TransitionType.APPROACH, classifier.apply(singletonList(approach))),
        () -> assertEquals(TransitionType.MISSED, classifier.apply(singletonList(missed)))
    );
  }

  private ArincProcedureLeg newProcedureLeg(String subSection, String routeType) {
    return newProcedureLeg(subSection, routeType, null, null);
  }

  private ArincProcedureLeg newProcedureLeg(String subSection, String routeType, String transitionIdentifier, String waypointDescription) {
    return new ArincProcedureLeg.Builder()
        .sequenceNumber(10)
        .fileRecordNumber(20)
        .sidStarIdentifier("MOCK")
        .airportIdentifier("MOCK")
        .airportIcaoRegion("MOCK")
        .sectionCode(SectionCode.P)
        .subSectionCode(subSection)
        .routeType(routeType)
        .transitionIdentifier(transitionIdentifier)
        .waypointDescription(waypointDescription)
        .build();
  }
}
