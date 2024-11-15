package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

class TestArincRequiredEquipageClassifier {

  private static final ArincRequiredEquipageClassifier classifier = new ArincRequiredEquipageClassifier();

  @Test
  void testTryAssignRouteTypeRNP() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "F", null, null);

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignRouteTypeRNAV() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "4", null, null);

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNAV, actual);
  }


  @Test
  void testTryAssignRouteTypeCONV() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "D", "1", null, null);

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.CONV, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRRS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "R", "S");

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRAS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "A", "S");

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRFS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "F", "S");

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNAVForApproachRPS() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "R", "P", "S");

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNAV, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachH() {
    ArincProcedureLeg leg = mockProcedureLeg(SectionCode.P, "F", "H", null, null);

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
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
    return leg;
  }

  private Multimap<TransitionType, List<ArincProcedureLeg>> asMultimap(TransitionType transitionType, ArincProcedureLeg arincProcedureLeg) {
    LinkedHashMultimap<TransitionType, List<ArincProcedureLeg>> multimap = LinkedHashMultimap.create();
    multimap.put(transitionType, singletonList(arincProcedureLeg));
    return multimap;
  }
}
