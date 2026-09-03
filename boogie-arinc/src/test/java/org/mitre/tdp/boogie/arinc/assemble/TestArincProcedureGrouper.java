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

class TestArincProcedureGrouper {

  private static final ArincProcedureGrouper grouper = ArincProcedureGrouper.INSTANCE;

  @Test
  void testGroupsByProcedureWithoutChangingEncounterOrder() {
    List<ArincProcedureLeg> input = List.of(
        newProcedureLeg("FIRST", "ALPHA", 30),
        newProcedureLeg("SECOND", "BRAVO", 20),
        newProcedureLeg("FIRST", "ALPHA", 10),
        newProcedureLeg("SECOND", "BRAVO", 40)
    );

    Collection<List<ArincProcedureLeg>> procedures = grouper.apply(input);
    Map<String, List<Integer>> procedureSequences = procedures.stream()
        .collect(Collectors.toMap(
            procedure -> procedure.get(0).sidStarIdentifier(),
            procedure -> procedure.stream().map(ArincProcedureLeg::sequenceNumber).toList()
        ));

    assertAll(
        () -> assertEquals(2, procedures.size()),
        () -> assertEquals(List.of(30, 10), procedureSequences.get("FIRST")),
        () -> assertEquals(List.of(20, 40), procedureSequences.get("SECOND"))
    );
  }

  private static ArincProcedureLeg newProcedureLeg(String procedureIdentifier, String transitionIdentifier, int sequenceNumber) {
    return new ArincProcedureLeg.Builder()
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(sequenceNumber)
        .sidStarIdentifier(procedureIdentifier)
        .airportIdentifier("MOCK")
        .airportIcaoRegion("K1")
        .sectionCode(SectionCode.P)
        .subSectionCode("D")
        .routeType("1")
        .transitionIdentifier(transitionIdentifier)
        .build();
  }
}
