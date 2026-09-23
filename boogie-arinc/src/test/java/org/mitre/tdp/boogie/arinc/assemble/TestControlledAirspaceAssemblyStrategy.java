package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.BoogieType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

class TestControlledAirspaceAssemblyStrategy {

  @Test
  void referencesMapToCoreModelTypes() {
    assertAll(
        () -> assertType("P", "A", BoogieType.AIRPORT),
        () -> assertType("H", "A", BoogieType.HELIPORT),
        () -> assertType("D", null, BoogieType.FIX),
        () -> assertType("D", "B", BoogieType.FIX),
        () -> assertType("P", "N", BoogieType.FIX),
        () -> assertType("E", "A", BoogieType.FIX),
        () -> assertType("U", "F", BoogieType.AIRSPACE)
    );
  }

  @Test
  void absentAndUnsupportedReferencesRemainUntyped() {
    assertAll(
        () -> assertType(null, null, null),
        () -> assertType(null, "A", null),
        () -> assertType("P", null, null),
        () -> assertType("P", "X", null),
        () -> assertType("U", "R", null),
        () -> assertType("D", " ", null)
    );
  }

  @Test
  void resolvedFixDoesNotDetermineSourceType() {
    Fix fix = Fix.builder().fixIdentifier("CENTER").latLong(LatLong.of(40., -75.)).build();
    Airspace airspace = assemble(null, null, fix);

    assertAll(
        () -> assertEquals(Optional.empty(), airspace.centerIdentification().orElseThrow().type()),
        () -> assertSame(fix, airspace.center().orElseThrow())
    );
  }

  private void assertType(String section, String subsection, BoogieType expected) {
    assertEquals(Optional.ofNullable(expected), assemble(section, subsection, null).centerIdentification().orElseThrow().type());
  }

  private Airspace assemble(String section, String subsection, Fix fix) {
    ArincControlledAirspaceLeg representative = new ArincControlledAirspaceLeg.Builder()
        .customerAreaCode(CustomerAreaCode.USA)
        .icaoCode("K6")
        .airspaceType(AirspaceType.C)
        .airspaceCenter("CENTER")
        .suppliedSectionCode(section)
        .supplierSubSectionCode(subsection)
        .build();
    return ControlledAirspaceAssemblyStrategy.standard().convertControlledAirspace(representative, fix, List.of());
  }
}
