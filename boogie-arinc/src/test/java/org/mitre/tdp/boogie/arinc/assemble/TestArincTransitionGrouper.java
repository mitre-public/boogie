package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincTransitionGrouper {

  private static final ArincTransitionGrouper grouper = ArincTransitionGrouper.INSTANCE;

  @Test
  void testSortsOnlyAfterGroupingByTransition() {
    List<ArincProcedureLeg> input = List.of(
        newProcedureLeg("ALPHA", 30),
        newProcedureLeg("BRAVO", 20),
        newProcedureLeg("ALPHA", 10),
        newProcedureLeg("BRAVO", 40)
    );

    Map<String, List<Integer>> transitionSequences = grouper.apply(input).stream()
        .collect(Collectors.toMap(
            transition -> transition.get(0).transitionIdentifier().orElseThrow(),
            transition -> transition.stream().map(ArincProcedureLeg::sequenceNumber).toList()
        ));

    assertAll(
        () -> assertEquals(List.of(10, 30), transitionSequences.get("ALPHA")),
        () -> assertEquals(List.of(20, 40), transitionSequences.get("BRAVO"))
    );
  }

  @Test
  void testSeparatesFinalCodingAndMissedApproachVariants() {
    List<ArincProcedureLeg> input = List.of(
        newProcedureLeg("FINAL", "R", "H", 30),
        newProcedureLeg("FINAL", "Z", "A", 20),
        newProcedureLeg("FINAL", "Z", "B", 25),
        newProcedureLeg("FINAL", "Z", "E", 27),
        newProcedureLeg("FINAL", "R", "S", 10),
        newProcedureLeg("FINAL", "Z", "A", 40),
        newProcedureLeg("FINAL", "Z", "B", 45),
        newProcedureLeg("FINAL", "Z", "E", 47)
    );

    Collection<List<ArincProcedureLeg>> transitions = grouper.apply(input);
    Map<String, List<Integer>> transitionSequences = transitions.stream()
        .collect(Collectors.toMap(
            transition -> "Z".equals(transition.get(0).routeType())
                ? transition.get(0).routeType().concat(transition.get(0).routeTypeQualifier2().orElse(""))
                : transition.get(0).routeType(),
            transition -> transition.stream().map(ArincProcedureLeg::sequenceNumber).toList()
        ));

    assertAll(
        () -> assertEquals(4, transitions.size()),
        () -> assertEquals(List.of(10, 30), transitionSequences.get("R")),
        () -> assertEquals(List.of(20, 40), transitionSequences.get("ZA")),
        () -> assertEquals(List.of(25, 45), transitionSequences.get("ZB")),
        () -> assertEquals(List.of(27, 47), transitionSequences.get("ZE"))
    );
  }

  private static ArincProcedureLeg newProcedureLeg(String transitionIdentifier, int sequenceNumber) {
    return newProcedureLeg(transitionIdentifier, "1", null, sequenceNumber);
  }

  private static ArincProcedureLeg newProcedureLeg(
      String transitionIdentifier,
      String routeType,
      String routeTypeQualifier2,
      int sequenceNumber
  ) {
    return new ArincProcedureLeg.Builder()
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(sequenceNumber)
        .sidStarIdentifier("MOCK")
        .airportIdentifier("MOCK")
        .airportIcaoRegion("K1")
        .sectionCode(SectionCode.P)
        .subSectionCode("D")
        .routeType(routeType)
        .routeTypeQualifier2(routeTypeQualifier2)
        .transitionIdentifier(transitionIdentifier)
        .build();
  }
}
