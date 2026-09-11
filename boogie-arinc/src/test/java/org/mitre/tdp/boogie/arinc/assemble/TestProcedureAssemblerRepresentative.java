package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestProcedureAssemblerRepresentative {

  @Test
  void usesFirstCommonLegEvenWhenOtherTransitionsHaveLowerSequenceNumbers() {
    ArincProcedureLeg commonFirst = leg("ALL", "5", 30);
    List<ArincProcedureLeg> legs = List.of(
        leg("ENROUTE", "3", 10),
        leg("ALL", "5", 50),
        leg("RW01", "1", 20),
        commonFirst
    );

    SelectedRepresentative result = assembleRepresentative(legs);
    assertAll(
        () -> assertSame(commonFirst, result.leg()),
        () -> assertEquals(RequiredNavigationEquipage.RNAV, result.equipage())
    );
  }

  @Test
  void usesFirstRunwayLegWhenThereIsNoCommonTransition() {
    ArincProcedureLeg runwayFirst = leg("RW01", "4", 30);
    List<ArincProcedureLeg> legs = List.of(
        leg("ENROUTE", "3", 10),
        leg("RW01", "4", 50),
        runwayFirst
    );

    SelectedRepresentative result = assembleRepresentative(legs);
    assertAll(
        () -> assertSame(runwayFirst, result.leg()),
        () -> assertEquals(RequiredNavigationEquipage.RNAV, result.equipage())
    );
  }

  @Test
  void usesFirstAvailableTransitionLegWhenThereIsNoCommonOrRunwayTransition() {
    ArincProcedureLeg enrouteFirst = leg("ENROUTE", "3", 30);
    List<ArincProcedureLeg> legs = List.of(leg("ENROUTE", "3", 50), enrouteFirst);

    SelectedRepresentative result = assembleRepresentative(legs);
    assertAll(
        () -> assertSame(enrouteFirst, result.leg()),
        () -> assertEquals(RequiredNavigationEquipage.CONV, result.equipage())
    );
  }

  private static SelectedRepresentative assembleRepresentative(List<ArincProcedureLeg> legs) {
    var terminal = ArincDatabaseFactory.newTerminalAreaDatabase(
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), legs, List.of(), List.of(), List.of());
    var fixes = ArincDatabaseFactory.newFixDatabase(List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    return ProcedureAssembler.withStrategy(terminal, fixes, FixAssemblyStrategy.standard(), new RepresentativeStrategy())
        .assemble(legs).findFirst().orElseThrow();
  }

  private static ArincProcedureLeg leg(String transitionIdentifier, String routeType, int sequenceNumber) {
    return new ArincProcedureLeg.Builder()
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(sequenceNumber)
        .sidStarIdentifier("MOCK")
        .airportIdentifier("MOCK")
        .airportIcaoRegion("K1")
        .sectionCode(SectionCode.P)
        .subSectionCode("D")
        .routeType(routeType)
        .transitionIdentifier(transitionIdentifier)
        .build();
  }

  private record SelectedRepresentative(ArincProcedureLeg leg, RequiredNavigationEquipage equipage) {
  }

  private static final class RepresentativeStrategy
      implements ProcedureAssemblyStrategy<SelectedRepresentative, ArincProcedureLeg, ArincProcedureLeg, Fix> {

    @Override
    public SelectedRepresentative convertProcedure(
        ArincProcedureLeg representative, RequiredNavigationEquipage equipage, List<ArincProcedureLeg> transitions) {
      return new SelectedRepresentative(representative, equipage);
    }

    @Override
    public ArincProcedureLeg convertTransition(
        ArincProcedureLeg representative, TransitionType transitionType, List<ArincProcedureLeg> legs) {
      return representative;
    }

    @Override
    public ArincProcedureLeg convertLeg(ArincProcedureLeg leg, Fix associatedFix, Fix recommendedNavaid, Fix centerFix) {
      return leg;
    }
  }
}
