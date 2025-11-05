package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.field.HeaderIdent.HDR;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.mitre.tdp.boogie.arinc.v18.field.ProductionTestFlag;

public class TestHeader01Spec {
  static String head = "HDR01TEST-22std.dat 001T013212345672113  10-JAN-202011:32:38 IDK BOATSALE    FAKE ARINC424-22                               EECAACEB";
  static ArincRecordParser parser = ArincRecordParser.standard(new Header01Spec());
  static Header01Converter converter = new Header01Converter();
  static ArincRecord record = parser.parse(head).orElseThrow(AssertionError::new);

  @Test
  void headerTest() {
    assertAll(
        () -> assertEquals(HDR, record.requiredField("headerIdent")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("headerNumber")),
        () -> assertEquals("TEST-22std.dat", record.requiredField("fileName")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("versionNumber")),
        () -> assertEquals(ProductionTestFlag.T, record.requiredField("productionTestFlag")),
        () -> assertEquals(Integer.valueOf(132), record.requiredField("recordLength")),
        () -> assertEquals(Integer.valueOf(1234567), record.requiredField("recordCount")),
        () -> assertEquals("2113", record.requiredField("cycle")),
        () -> assertEquals("10-JAN-2020", record.requiredField("creationDate")),
        () -> assertEquals("11:32:38", record.requiredField("creationTime")),
        () -> assertEquals("IDK BOATSALE", record.requiredField("dataSupplierIdent")),
        () -> assertEquals("FAKE ARINC424-22", record.requiredField("targetCustomerIdent")),
        () -> assertFalse(record.containsParsedField("databasePartNumber")),
        () -> assertEquals("EECAACEB", record.requiredField("fileCrc"))
    );
  }

  @Test
  void testConverted() {
    ArincHeaderOne header = converter.apply(record).orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals(1, header.headerNumber()),
        () -> assertEquals("TEST-22std.dat", header.fileName().get()),
        () -> assertEquals(1, header.version().get()),
        () -> assertEquals(ProductionTestFlag.T, header.productionTestFlag().get()),
        () -> assertEquals(132, header.recordLength().get()),
        () -> assertEquals(1234567, header.recordCount().get()),
        () -> assertEquals("2113", header.cycle().get()),
        () -> assertEquals(1578655958000L, header.creationTime().get().toEpochMilli()),
        () -> assertEquals("IDK BOATSALE", header.dataSupplierIdentifier().get()),
        () -> assertEquals("EECAACEB", header.fileCrc().get())
    );
  }
}
