package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("D").routeType("F").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignRouteTypeRNAV() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("D").routeType("4").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNAV, actual);
  }


  @Test
  void testTryAssignRouteTypeCONV() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("D").routeType("1").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.CONV, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRRS() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("F")
        .routeType("R").routeTypeQualifier1("R").routeTypeQualifier2("S").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRAS() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("F")
        .routeType("R").routeTypeQualifier1("A").routeTypeQualifier2("S").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachRFS() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("F")
        .routeType("R").routeTypeQualifier1("F").routeTypeQualifier2("S").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNAVForApproachRPS() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("F")
        .routeType("R").routeTypeQualifier1("P").routeTypeQualifier2("S").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNAV, actual);
  }

  @Test
  void testTryAssignedRouteTypeRNPForApproachH() {
    ArincProcedureLeg leg = new ArincProcedureLeg.Builder().sectionCode(SectionCode.P).subSectionCode("F").routeType("H").build();

    RequiredNavigationEquipage actual = classifier.apply(asMultimap(TransitionType.COMMON, leg));
    assertEquals(RequiredNavigationEquipage.RNP, actual);
  }

  private Multimap<TransitionType, List<ArincProcedureLeg>> asMultimap(TransitionType transitionType, ArincProcedureLeg arincProcedureLeg) {
    LinkedHashMultimap<TransitionType, List<ArincProcedureLeg>> multimap = LinkedHashMultimap.create();
    multimap.put(transitionType, singletonList(arincProcedureLeg));
    return multimap;
  }
}
