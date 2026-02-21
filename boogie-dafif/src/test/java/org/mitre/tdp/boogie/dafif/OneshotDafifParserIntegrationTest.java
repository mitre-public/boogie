package org.mitre.tdp.boogie.dafif;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;

import com.google.common.io.Resources;

@Tag("DAFIF")
@Tag("INTEGRATION")
class OneshotDafifParserIntegrationTest {

  static OneshotDafifParser.ClientRecords<Airport, Fix, Airway, Procedure> records;

  @BeforeAll
  static void setUp() throws IOException {
    OneshotDafifParser<Airport, ?, Fix, ?, ?, Airway, Procedure> parser = OneshotDafifParser.standard(DafifVersion.V81);
    try (InputStream is = Resources.getResource("DAFIF8_1_2601.zip").openStream()) {
      records = parser.assembleFrom(is);
    }
  }

  @Test
  void testAirportCount() {
    assertEquals(10011, records.airports().size(), "Assembled airports should match ARPT.TXT row count");
  }

  @Test
  void testFixCount() {
    assertFalse(records.fixes().isEmpty(), "Should assemble at least one fix");
  }

  @Test
  void testAirwayCount() {
    assertEquals(17269, records.airways().size(),
        "Assembled airways should match unique ATS_IDENT+DIRECTION combos in ATS.TXT");
  }

  @Test
  void testProcedureCount() {
    assertEquals(31932, records.procedures().size(),
        "Assembled procedures should match unique TRM_PAR row count");
  }

  @Test
  void testAllCollectionsNonEmpty() {
    assertAll(
        () -> assertFalse(records.airports().isEmpty(), "Airports should not be empty"),
        () -> assertFalse(records.fixes().isEmpty(), "Fixes should not be empty"),
        () -> assertFalse(records.airways().isEmpty(), "Airways should not be empty"),
        () -> assertFalse(records.procedures().isEmpty(), "Procedures should not be empty")
    );
  }
}
