package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincRequiredEquipageClassifier {

  private static final ArincRequiredEquipageClassifier classifier = new ArincRequiredEquipageClassifier();

  @Test
  void testTryAssignRouteTypeRNP() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "F", null, null);
    ArincProcedureLeg leg1 = mockProcedureLegQual3(SectionCode.P, "D", "1", "G", "E",  "E");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    RequiredNavigationEquipage actual1 = classifier.apply(leg1);
    assertAll(
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual),
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual1, "new way for the same case")
    );
  }

  @Test
  void testTryAssignRouteTypeRNAV() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "4", null, null);
    ArincProcedureLeg leg1 = mockProcedureLegQual3(SectionCode.P, "D", "1", "G", "D",  "X");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    RequiredNavigationEquipage actual1 = classifier.apply(leg1);
    assertAll(
        () -> assertEquals(RequiredNavigationEquipage.RNAV, actual),
        () -> assertEquals(RequiredNavigationEquipage.RNAV, actual1, "new way for the same case")
    );
  }


  @Test
  void testTryAssignRouteTypeCONV() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "1", null, null);
    ArincProcedureLeg leg1 = mockProcedureLegQual3(SectionCode.P, "D", "1", null, "G", null);

    RequiredNavigationEquipage actual = classifier.apply(leg);
    RequiredNavigationEquipage actual1 = classifier.apply(leg1);
    assertAll(
        () -> assertEquals(RequiredNavigationEquipage.CONV, actual),
        () -> assertEquals(RequiredNavigationEquipage.CONV, actual1, "new way for the same case")
    );
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRRS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "R", "S");
    //in this case i had to come up with codes to match the outcome, which is not thrilling
    ArincProcedureLeg leg2 = mockProcedureLegQual3(SectionCode.P, "F", "R", "R", "S", "E");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    RequiredNavigationEquipage actual1 = classifier.apply(leg2);

    assertAll(
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual),
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual1, "not a happy case, this really did not specify what nav spec before")
    );
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRAS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "A", "S");
    ArincProcedureLeg leg1 = mockProcedureLegQual3(SectionCode.P, "F", "R", "J", "S", "A");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    RequiredNavigationEquipage actual1 = classifier.apply(leg1);

    assertAll(
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual),
        () -> assertEquals(RequiredNavigationEquipage.RNP, actual1, "new way for the same case")
    );
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRFS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "F", "S");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNAVForApproachRPS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "P", "S");

    RequiredNavigationEquipage actual = classifier.apply(leg);
    assertEquals(RequiredNavigationEquipage.RNAV, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachH() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "H", null, null);

    RequiredNavigationEquipage actual = classifier.apply(leg);
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  private ArincProcedureLeg mockProcedureLeg(
      SectionCode sectionCode,
      String subSectionCode,
      String routeType,
      String qualifier1,
      String qualifier2
  ) {
    ArincProcedureLeg leg = mock(ArincProcedureLeg.class);
    when(leg.sectionCode()).thenReturn(sectionCode);
    when(leg.subSectionCode()).thenReturn(Optional.ofNullable(subSectionCode));
    when(leg.routeType()).thenReturn(routeType);
    when(leg.routeTypeQualifier1()).thenReturn(Optional.ofNullable(qualifier1));
    when(leg.routeTypeQualifier2()).thenReturn(Optional.ofNullable(qualifier2));
    when(leg.routeTypeQualifier3()).thenReturn(Optional.empty());
    return leg;
  }

  private ArincProcedureLeg mockProcedureLegQual3(
      SectionCode sectionCode,
      String subSectionCode,
      String routeType,
      String qualifier1,
      String qualifier2,
      String qualifier3
  ) {
    ArincProcedureLeg leg = mock(ArincProcedureLeg.class);
    when(leg.sectionCode()).thenReturn(sectionCode);
    when(leg.subSectionCode()).thenReturn(Optional.ofNullable(subSectionCode));
    when(leg.routeType()).thenReturn(routeType);
    when(leg.routeTypeQualifier1()).thenReturn(Optional.ofNullable(qualifier1));
    when(leg.routeTypeQualifier2()).thenReturn(Optional.ofNullable(qualifier2));
    when(leg.routeTypeQualifier3()).thenReturn(Optional.ofNullable(qualifier3));
    return leg;
  }
}
