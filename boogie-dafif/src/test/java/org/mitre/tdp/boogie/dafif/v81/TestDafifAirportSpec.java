package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAirportConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAirportSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAirportValidator;

public class TestDafifAirportSpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifAirportSpec());

  private static final DafifAirportValidator validator = new DafifAirportValidator();
  private static final DafifAirportConverter converter = new DafifAirportConverter();

  //First airport record in 2402, ARPT.txt
  public static final String RAW_AIRPORT = "AA30079\tREINA BEATRIX INTL\t\tTNCA\tN\tWGE\tWGE\tN12300529\t12.501469\tW070005618\t-70.015606\t00062\tA\tW011263 0124\t0707\tY\t\tCI\t\t\t\t\t202402\t\t\t0\tW011020\n";

  @Test
  void testParseDafifAirport() {
    DafifRecord record = parser.parse(DafifRecordType.ARPT, RAW_AIRPORT).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals("REINA BEATRIX INTL", record.requiredField("name")),
        () -> assertFalse(record.optionalField("stateProvinceCode").isPresent()),
        () -> assertEquals("TNCA", record.requiredField("icaoCode")),
        () -> assertEquals("N", record.requiredField("faaHostCountryIdentifier")),
        () -> assertEquals("WGE", record.requiredField("localHorizontalDatum")),
        () -> assertEquals("WGE", record.requiredField("geodeticDatum")),
        () -> assertEquals("N12300529", record.requiredField("geodeticLatitude")),
        () -> assertEquals(12.501469, record.requiredField("degreesLatitude")),
        () -> assertEquals("W070005618", record.requiredField("geodeticLongitude")),
        () -> assertEquals(-70.015606, record.requiredField("degreesLongitude")),
        () -> assertEquals(Integer.valueOf(62), record.requiredField("elevation")),
        () -> assertEquals("A", record.requiredField("airportType")),
        () -> assertEquals("W011263 0124", record.requiredField("magneticVariation")),
        () -> assertEquals("0707", record.requiredField("wAC")),
        () -> assertEquals("Y", record.requiredField("beacon")),
        () -> assertFalse(record.optionalField("secondaryAirport").isPresent()),
        () -> assertEquals("CI", record.requiredField("primaryOperatingAgency")),
        () -> assertFalse(record.optionalField("secondaryName").isPresent()),
        () -> assertFalse(record.optionalField("secondaryIcaoCode").isPresent()),
        () -> assertFalse(record.optionalField("secondaryFaaHost").isPresent()),
        () -> assertFalse(record.optionalField("secondaryOperatingAgency").isPresent()),
        () -> assertEquals(Integer.valueOf(202402), record.requiredField("cycleDate")),
        () -> assertFalse(record.optionalField("terrainImpacted").isPresent()),
        () -> assertFalse(record.optionalField("shoreline").isPresent()),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("coordinatePrecision")),
        () -> assertEquals("W011020", record.requiredField("magneticVariationOfRecord"))
    );


    assertTrue(validator.test(record));

    DafifAirport airport = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", airport.getAirportIdentification()),
        () -> assertEquals("REINA BEATRIX INTL", airport.getName()),
        () -> assertNull(airport.getStateProvinceCode()),
        () -> assertEquals("TNCA", airport.getIcaoCode()),
        () -> assertEquals("N", airport.getFaaHostCountryIdentifier()),
        () -> assertEquals("WGE", airport.getLocalHorizontalDatum()),
        () -> assertEquals("WGE", airport.getGeodeticDatum()),
        () -> assertEquals("N12300529", airport.getGeodeticLatitude()),
        () -> assertEquals(12.501469, airport.getDegreesLatitude()),
        () -> assertEquals("W070005618", airport.getGeodeticLongitude()),
        () -> assertEquals(-70.015606, airport.getDegreesLongitude()),
        () -> assertEquals(62, airport.getElevation()),
        () -> assertEquals("A", airport.getAirportType()),
        () -> assertEquals("W011263 0124", airport.getMagneticVariation()),
        () -> assertEquals("0707", airport.getWac()),
        () -> assertEquals("Y", airport.getBeacon()),
        () -> assertNull(airport.getSecondaryAirport()),
        () -> assertEquals("CI", airport.getPrimaryOperatingAgency()),
        () -> assertNull(airport.getSecondaryName()),
        () -> assertNull(airport.getSecondaryIcaoCode()),
        () -> assertNull(airport.getSecondaryFaaHost()),
        () -> assertNull(airport.getSecondaryOperatingAgency()),
        () -> assertEquals(202402, airport.getCycleDate()),
        () -> assertNull(airport.getTerrainImpacted()),
        () -> assertNull(airport.getShoreline()),
        () -> assertEquals(0, airport.getCoordinatePrecision()),
        () -> assertEquals("W011020", airport.getMagVarOfRecord())
    );
  }
}
