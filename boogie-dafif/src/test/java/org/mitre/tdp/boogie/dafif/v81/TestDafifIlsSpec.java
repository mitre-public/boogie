package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifIlsConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifIlsSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifIlsValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifIlsSpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifIlsSpec());

  private static final DafifIlsValidator validator = new DafifIlsValidator();
  private static final DafifIlsConverter converter = new DafifIlsConverter();

  public static final String RAW_ILS = "AA30079\t11\tD\t\t\tU\t024X\t\t\t\t00011\tWGE\tWGE\t\tN12300951\t12.502642\tW070013023\t-70.025064\tIBE\t\t\t\tW011264 0124\t\t\t\t\t\t202402\t\t0\n";

  @Test
  void testParseDafifIls() {
    DafifRecord record = parser.parse(DafifRecordType.ILS, RAW_ILS).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals("11", record.requiredField("runwayIdentifier")),
        () -> assertEquals("D", record.requiredField("componentType")),
        () -> assertFalse(record.optionalField("collocation").isPresent()),
        () -> assertFalse(record.optionalField("name").isPresent()),
        () -> assertEquals("U", record.requiredField("navaidFrequency")),
        () -> assertEquals("024X", record.requiredField("navaidChannel")),
        () -> assertFalse(record.optionalField("ilsGlideSlopeAngle").isPresent()),
        () -> assertFalse(record.optionalField("localizerOrGlideSlopeLocation").isPresent()),
        () -> assertFalse(record.optionalField("locatorOrMarkerLocation").isPresent()),
        () -> assertEquals("00011", record.requiredField("ilsNavaidElevation")),
        () -> assertEquals("WGE", record.requiredField("localHorizontalDatum")),
        () -> assertEquals("WGE", record.requiredField("geodeticDatum")),
        () -> assertFalse(record.optionalField("ilsMlsCategory").isPresent()),
        () -> assertEquals("N12300951", record.requiredField("geodeticLatitude")),
        () -> assertEquals(12.502642, record.requiredField("degreesLatitude")),
        () -> assertEquals("W070013023", record.requiredField("geodeticLongitude")),
        () -> assertEquals(-70.025064, record.requiredField("degreesLongitude")),
        () -> assertEquals("IBE", record.requiredField("ilsNavaidIdentifier")),
        () -> assertFalse(record.optionalField("navaidType").isPresent()),
        () -> assertFalse(record.optionalField("countryCode").isPresent()),
        () -> assertFalse(record.optionalField("navaidKeyCode").isPresent()),
        () -> assertEquals("W011264 0124", record.requiredField("magneticVariation")),
        () -> assertFalse(record.optionalField("ilsSlaveVariation").isPresent()),
        () -> assertFalse(record.optionalField("ilsBearingCourse").isPresent()),
        () -> assertFalse(record.optionalField("localizerWidth").isPresent()),
        () -> assertFalse(record.optionalField("thresholdCrossingHeight").isPresent()),
        () -> assertFalse(record.optionalField("ilsDmeBias").isPresent()),
        () -> assertEquals(Integer.valueOf(202402), record.requiredField("cycleDate")),
        () -> assertFalse(record.optionalField("mlsDmePrecision").isPresent()),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("coordinatePrecision"))
    );


    assertTrue(validator.test(record));

    DafifIls ils = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", ils.getAirportIdentification()),
        () -> assertEquals("11", ils.getRunwayIdentifier()),
        () -> assertEquals("D", ils.getComponentType()),
        () -> assertEquals("U", ils.getNavaidFrequency()),
        () -> assertEquals("024X", ils.getNavaidChannel()),
        () -> assertEquals("00011", ils.getIlsNavaidElevation()),
        () -> assertEquals("WGE", ils.getLocalHorizontalDatum()),
        () -> assertEquals("WGE", ils.getGeodeticDatum()),
        () -> assertEquals("N12300951", ils.getGeodeticLatitude()),
        () -> assertEquals(12.502642, ils.getDegreesLatitude()),
        () -> assertEquals("W070013023", ils.getGeodeticLongitude()),
        () -> assertEquals(-70.025064, ils.getDegreesLongitude()),
        () -> assertEquals("IBE", ils.getIlsNavaidIdentifier()),
        () -> assertEquals("W011264 0124", ils.getMagneticVariation()),
        () -> assertEquals(202402, ils.getCycleDate()),
        () -> assertEquals(0, ils.getCoordinatePrecision())
    );

  }
}
