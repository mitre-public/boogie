package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.v23_4.generated.Sid;

class ArincProcedureConverterTest {

  private static final ArincProcedureConverter CONVERTER = ArincProcedureConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    Sid sid = new Sid();
    assertEquals(Optional.empty(), CONVERTER.apply(sid));
  }

  @Test
  void convertsSid() {
    Optional<ArincProcedure> result = CONVERTER.apply(TestGeneratedObjects.newValidSid());
    assertTrue(result.isPresent());

    ArincProcedure p = result.get();
    assertAll(
        () -> assertEquals("RNAV1", p.identifier()),
        () -> assertEquals("Sid", p.procedureType()),
        () -> assertEquals(Optional.of("STANDARD"), p.recordType()),
        () -> assertEquals(Optional.of(true), p.isRnav()),
        () -> assertEquals(Optional.of(false), p.isHelicopterOnlyProcedure()),
        () -> assertEquals(Optional.of(false), p.isMilitary()),
        () -> assertEquals(Optional.of(false), p.isSpecial()),
        () -> assertEquals(Optional.of("RNAV ONE DEPARTURE"), p.procedureName()),
        () -> assertEquals(Optional.of("RNAV1.DEP"), p.longIdent()),
        // SidStar fields
        () -> assertEquals(Optional.of(false), p.isVorDmeRnav()),
        () -> assertEquals(Optional.of("RNAV_1"), p.rnavPbnNavSpec()),
        // Sid fields
        () -> assertEquals(Optional.of(false), p.isEngineOut()),
        () -> assertEquals(Optional.of(true), p.isVector()),
        () -> assertEquals(Optional.of(false), p.isPInS()),
        () -> assertEquals(Optional.of(false), p.isPInSProceedVisually()),
        () -> assertEquals(Optional.of(false), p.isPInSProceedVfr()),
        // transitions: 1 runway + 1 enroute = 2
        () -> assertEquals(2, p.transitions().size()),
        () -> assertEquals("RW09L", p.transitions().get(0).identifier().get()),
        () -> assertEquals("GREKI", p.transitions().get(1).identifier().get())
    );
  }

  @Test
  void convertsStar() {
    Optional<ArincProcedure> result = CONVERTER.apply(TestGeneratedObjects.newValidStar());
    assertTrue(result.isPresent());

    ArincProcedure p = result.get();
    assertAll(
        () -> assertEquals("FRDMM3", p.identifier()),
        () -> assertEquals("Star", p.procedureType()),
        () -> assertEquals(Optional.of(true), p.isContinuousDescent()),
        () -> assertEquals(Optional.of(false), p.isVorDmeRnav()),
        () -> assertEquals(Optional.of("RNAV_1"), p.rnavPbnNavSpec()),
        () -> assertEquals(Optional.of("FREEDOM THREE ARRIVAL"), p.procedureName()),
        // transitions: 1 enroute + 1 runway = 2
        () -> assertEquals(2, p.transitions().size()),
        () -> assertEquals("FRDMM", p.transitions().get(0).identifier().get()),
        () -> assertEquals("RW01C", p.transitions().get(1).identifier().get())
    );
  }

  @Test
  void convertsApproach() {
    Optional<ArincProcedure> result = CONVERTER.apply(TestGeneratedObjects.newValidApproach());
    assertTrue(result.isPresent());

    ArincProcedure p = result.get();
    assertAll(
        () -> assertEquals("I01C", p.identifier()),
        () -> assertEquals("Approach", p.procedureType()),
        () -> assertEquals(Optional.of("ILS"), p.approachRouteType()),
        () -> assertEquals(Optional.of("FMS_OVERLAY_AUTHORIZED"), p.gnssFmsIndicator()),
        () -> assertEquals(Optional.of(false), p.isRnavVisual()),
        () -> assertEquals(Optional.of(false), p.isLocalizerBackcourse()),
        () -> assertEquals(Optional.of(true), p.isPreferredProcedure()),
        () -> assertEquals(Optional.of(BigDecimal.valueOf(1.0)), p.categoryARadius()),
        () -> assertEquals(Optional.of(BigDecimal.valueOf(1.5)), p.categoryBRadius()),
        () -> assertEquals(Optional.of(BigDecimal.valueOf(2.0)), p.categoryCRadius()),
        () -> assertEquals(Optional.of(BigDecimal.valueOf(2.5)), p.categoryDRadius()),
        () -> assertEquals(Optional.of("ILS RWY 01C"), p.procedureName()),
        // transitions: 1 approach transition + 1 final approach + 1 missed = 3
        () -> assertEquals(3, p.transitions().size()),
        () -> assertEquals("GNDLF", p.transitions().get(0).identifier().get()),
        () -> assertEquals("I01C", p.transitions().get(1).identifier().get()),
        () -> assertEquals("MA01C", p.transitions().get(2).identifier().get())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Sid sid = TestGeneratedObjects.newValidSid();
    sid.setRecordType(null);
    sid.setRnavPbnNavSpec(null);
    sid.setRnpPbnNavSpec(null);
    sid.setProcedureName(null);
    sid.setLongIdent(null);
    sid.setProcedureDesignMagVar(null);

    Optional<ArincProcedure> result = CONVERTER.apply(sid);
    assertTrue(result.isPresent());

    ArincProcedure p = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), p.recordType()),
        () -> assertEquals(Optional.empty(), p.rnavPbnNavSpec()),
        () -> assertEquals(Optional.empty(), p.rnpPbnNavSpec()),
        () -> assertEquals(Optional.empty(), p.procedureName()),
        () -> assertEquals(Optional.empty(), p.longIdent()),
        () -> assertEquals(Optional.empty(), p.procedureDesignMagVarEwt()),
        () -> assertEquals(Optional.empty(), p.procedureDesignMagVarValue())
    );
  }
}
