package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.arinc.v18.TestAirportSpec.CYYH;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

class TestTrueReferenceAssembly {

  // Complete set of N15 primary legs from the CYYH Lido 2605 fixture.
  private static final List<String> N15 = List.of(
      "SCANP CYYHCYFN15   AYYH  F010YYH  CYDB0N   L   PI YCB CY      07871999350T0100D   + 01700     18000                 U  S   113952605",
      "SCANP CYYHCYFN15   AYYH  F020FF15 CYPC0EE      CF YCB CY      07781999125T0017D   + 01700                           U  S   113962605",
      "SCANP CYYHCYFN15   N     F010FF15 CYPC0E  F    IF YYH CY                      DB  + 01700     18000       YYH  ACYDBU  S   113972605",
      "SCANP CYYHCYFN15   N     F020EP15 CYPC0E  E    TF YYH CY                      DB    00140             -300          U  S   113982605",
      "SCANP CYYHCYFN15   N     F030YYH  CYDB0N  M    TF YYH CY                      DB  + 00490                           U  S   113992605",
      "SCANP CYYHCYFN15   Z     F040YYH  CYDB0N M     FA YCB CY      07871999125T    D   + 02000                           U  A   114002605",
      "SCANP CYYHCYFN15   Z     F050YYH  CYDB0N   R   DF                                   02000                           U  A   114012605",
      "SCANP CYYHCYFN15   Z     F060YYH  CYDB0NE  L   HM                     125TT010  I                  230              U- A   114022605"
  );

  @Test
  void assemblesEveryCyyhN15LegIncludingTrueCourses() {
    ArincAirport airport = airport();
    List<ArincProcedureLeg> source = N15.stream().map(TestTrueReferenceAssembly::parse).toList();
    List<Leg> legs = assemble(source, List.of(airport), List.of());

    assertAll(
        () -> assertEquals(8, legs.size()),
        () -> assertEquals(4, legs.stream().filter(leg -> leg.outboundCourse().isPresent()).count()),
        () -> assertEquals(1, legs.stream().filter(leg -> leg.outboundCourse().equals(Optional.of(ReferencedCourse.trueCourse(350.0)))).count()),
        () -> assertEquals(3, legs.stream().filter(leg -> leg.outboundCourse().equals(Optional.of(ReferencedCourse.trueCourse(125.0)))).count()),
        () -> assertTrue(legs.stream().allMatch(leg -> leg.outboundMagneticCourse().isEmpty())),
        () -> assertTrue(legs.stream().anyMatch(leg -> leg.pathTerminator() == PathTerminator.PI)),
        () -> assertTrue(legs.stream().anyMatch(leg -> leg.pathTerminator() == PathTerminator.HM))
    );
  }

  @ParameterizedTest
  @CsvSource({"T,3500,TRUE", "M,3500,MAGNETIC", "M,350T,TRUE", "MIXED,3500,MAGNETIC", "MIXED,350T,TRUE"})
  void appliesAirportReferenceWithoutMutatingSource(String indicator, String course, CourseReference expected) {
    ArincAirport airport = airport().toBuilder()
        .magneticTrueIndicator(indicator.equals("MIXED") ? null : MagneticTrueIndicator.valueOf(indicator))
        .magneticVariation(12.0)
        .build();
    String raw = N15.get(0);
    ArincProcedureLeg source = parse(raw.substring(0, 70) + course + raw.substring(74));
    ReferencedCourse original = source.outboundCourse().orElseThrow();
    Leg result = assemble(List.of(source), List.of(airport), List.of()).get(0);

    assertAll(
        () -> assertEquals(expected, result.outboundCourse().orElseThrow().reference()),
        () -> assertEquals(350.0, result.outboundCourse().orElseThrow().degrees()),
        () -> assertEquals(original, source.outboundCourse().orElseThrow()),
        () -> assertEquals(source.theta(), result.theta(), "Theta retains the recommended navaid's reference")
    );
  }

  @Test
  void missingAirportDoesNotInventTrueReference() {
    ArincProcedureLeg source = parse(N15.get(0)).toBuilder().outboundCourse(ReferencedCourse.magnetic(125.0)).build();
    assertEquals(source.outboundCourse(), assemble(List.of(source), List.of(), List.of()).get(0).outboundCourse());
  }

  @Test
  void parentReferenceDoesNotLeakAcrossRegions() {
    ArincAirport trueAirport = airport();
    ArincAirport magneticAirport = trueAirport.toBuilder().airportIcaoRegion("ZZ")
        .magneticTrueIndicator(MagneticTrueIndicator.M).build();
    ArincProcedureLeg trueLeg = parse(N15.get(0)).toBuilder().outboundCourse(ReferencedCourse.magnetic(125.0)).build();
    ArincProcedureLeg magneticLeg = trueLeg.toBuilder().airportIcaoRegion("ZZ")
        .outboundCourse(ReferencedCourse.magnetic(126.0)).build();
    List<ReferencedCourse> courses = assemble(List.of(trueLeg, magneticLeg), List.of(trueAirport, magneticAirport), List.of())
        .stream().map(leg -> leg.outboundCourse().orElseThrow()).toList();
    assertAll(
        () -> assertEquals(2, courses.size()),
        () -> assertTrue(courses.containsAll(List.of(ReferencedCourse.trueCourse(125.0), ReferencedCourse.magnetic(126.0))))
    );
  }

  @Test
  void heliportReferenceAppliesOnlyToHeliportProcedures() {
    ArincHeliport heliport = ArincHeliport.builder()
        .recordType(RecordType.S).customerAreaCode(CustomerAreaCode.CAN).sectionCode(SectionCode.H)
        .heliportIdentifier("CYYH").heliportIcaoRegion("CY")
        .latitude(69.55).longitude(-93.58).magneticTrueIndicator(MagneticTrueIndicator.T)
        .fileRecordNumber(1).cycleDate("2605").build();
    ArincProcedureLeg source = parse(N15.get(0)).toBuilder().outboundCourse(ReferencedCourse.magnetic(125.0)).build();
    ArincProcedureLeg helicopterLeg = source.toBuilder().sectionCode(SectionCode.H).build();
    assertAll(
        () -> assertEquals(CourseReference.MAGNETIC, assemble(List.of(source), List.of(), List.of(heliport)).get(0).outboundCourse().orElseThrow().reference()),
        () -> assertEquals(CourseReference.TRUE, assemble(List.of(helicopterLeg), List.of(), List.of(heliport)).get(0).outboundCourse().orElseThrow().reference())
    );

    // P and H records may share a procedure group; either section may be encountered first.
    for (List<ArincProcedureLeg> order : List.of(List.of(source, helicopterLeg), List.of(helicopterLeg, source))) {
      List<ReferencedCourse> courses = assemble(order, List.of(), List.of(heliport)).stream()
          .map(leg -> leg.outboundCourse().orElseThrow()).toList();
      assertAll(
          () -> assertEquals(2, courses.size()),
          () -> assertTrue(courses.containsAll(List.of(ReferencedCourse.magnetic(125.0), ReferencedCourse.trueCourse(125.0))))
      );
    }
  }

  private static List<Leg> assemble(List<ArincProcedureLeg> legs, List<ArincAirport> airports, List<ArincHeliport> heliports) {
    var terminal = ArincDatabaseFactory.newTerminalAreaDatabase(airports, List.of(), List.of(), List.of(), List.of(), List.of(), legs, List.of(), List.of(), heliports);
    var fixes = ArincDatabaseFactory.newFixDatabase(List.of(), List.of(), List.of(), airports, List.of(), heliports);
    return ProcedureAssembler.standard(terminal, fixes).assemble(legs)
        .flatMap(proc -> proc.transitions().stream()).flatMap(transition -> transition.legs().stream()).map(leg -> (Leg) leg).toList();
  }

  private static ArincAirport airport() {
    return ArincRecordParser.standard(new AirportSpec()).parse(CYYH).flatMap(new AirportConverter()).orElseThrow();
  }

  private static ArincProcedureLeg parse(String raw) {
    return ArincRecordParser.standard(new ProcedureLegSpec()).parse(raw).flatMap(new ProcedureLegConverter()).orElseThrow();
  }
}
