package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.v23_4.generated.Airway;

class ArincAirwayConverterTest {

  private static final ArincAirwayConverter CONVERTER = ArincAirwayConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    Airway airway = new Airway();
    assertEquals(Optional.empty(), CONVERTER.apply(airway));
  }

  @Test
  void validAirwayConverts() {
    Optional<ArincAirway> result = CONVERTER.apply(TestGeneratedObjects.newValidAirway());
    assertTrue(result.isPresent());

    ArincAirway aw = result.get();
    assertAll(
        () -> assertEquals("J60", aw.identifier()),
        () -> assertEquals(Optional.of("STANDARD"), aw.recordType()),
        () -> assertEquals(Optional.of("USA"), aw.customerCode()),
        () -> assertEquals("OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER", aw.airwayRouteType()),
        () -> assertEquals(Optional.of("GNSS_REQUIRED"), aw.qualifier1()),
        () -> assertEquals(Optional.empty(), aw.qualifier2()),
        () -> assertEquals(2, aw.legs().size()),
        () -> assertEquals(10, aw.legs().get(0).sequenceNumber()),
        () -> assertEquals(Optional.of("WYPT1"), aw.legs().get(0).fixIdent()),
        () -> assertEquals(20, aw.legs().get(1).sequenceNumber()),
        () -> assertEquals(Optional.of("WYPT2"), aw.legs().get(1).fixIdent())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Airway airway = TestGeneratedObjects.newValidAirway();
    airway.setRecordType(null);
    airway.setCustomerCode(null);
    airway.setQualifier1(null);
    airway.setQualifier2(null);
    airway.setRnavPbnNavSpec(null);
    airway.setRnpPbnNavSpec(null);

    Optional<ArincAirway> result = CONVERTER.apply(airway);
    assertTrue(result.isPresent());

    ArincAirway aw = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), aw.recordType()),
        () -> assertEquals(Optional.empty(), aw.customerCode()),
        () -> assertEquals(Optional.empty(), aw.qualifier1()),
        () -> assertEquals(Optional.empty(), aw.qualifier2()),
        () -> assertEquals(Optional.empty(), aw.rnavPbnNavSpec()),
        () -> assertEquals(Optional.empty(), aw.rnpPbnNavSpec()),
        () -> assertEquals(2, aw.legs().size())
    );
  }
}
