package org.mitre.tdp.boogie.arinc.v19;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

public class TestAirwayLegSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new AirwayLegSpec());
  static AirwayLegConverter converter = new AirwayLegConverter();
  static String ER = "SUSAER       A770        0010LEV  K4D 0V C  OB RR                     178405530000 04000     FL450                         339382210";
  @Test
  void parse() {
    ArincRecord record = parser.parse(ER).orElseThrow();
    assertAll(
        () -> assertFalse(record.containsParsedField("verticalScaleFactor")),
        () -> assertFalse(record.containsParsedField("rvsmMinimumLevel")),
        () -> assertFalse(record.containsParsedField("rvsmMaximumLevel"))
    );
  }

  @Test
  void convertEmpty() {
    ArincAirwayLeg leg = parser.parse(ER).flatMap(converter).orElseThrow();
    assertAll("yes testing we dont have the data",
        () -> assertTrue(leg.verticalScaleFactor().isEmpty()),
        () -> assertTrue(leg.rvsmMinLevel().isEmpty()),
        () -> assertTrue(leg.rvsmMaxLevel().isEmpty())
    );
  }
}
