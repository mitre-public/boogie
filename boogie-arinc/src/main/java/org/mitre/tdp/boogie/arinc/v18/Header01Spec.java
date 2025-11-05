package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CreationDate;
import org.mitre.tdp.boogie.arinc.v18.field.CreationTime;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DataSupplierIdent;
import org.mitre.tdp.boogie.arinc.v18.field.DatabasePartNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FileCrc;
import org.mitre.tdp.boogie.arinc.v18.field.FileName;
import org.mitre.tdp.boogie.arinc.v18.field.HeaderIdent;
import org.mitre.tdp.boogie.arinc.v18.field.HeaderNumber;
import org.mitre.tdp.boogie.arinc.v18.field.ProductionTestFlag;
import org.mitre.tdp.boogie.arinc.v18.field.RecordCount;
import org.mitre.tdp.boogie.arinc.v18.field.RecordLength;
import org.mitre.tdp.boogie.arinc.v18.field.TargetCustomerIdent;
import org.mitre.tdp.boogie.arinc.v18.field.VersionNumber;

/**
 * The header records contain information to uniquely identify each data file.
 */
public final class Header01Spec implements RecordSpec {
  private final List<RecordField<?>> recordFields;

  public Header01Spec() {
    this.recordFields = List.of(
        new RecordField<>(HeaderIdent.SPEC),
        new RecordField<>(new HeaderNumber()),
        new RecordField<>(new FileName()),
        new RecordField<>(new VersionNumber()),
        new RecordField<>(ProductionTestFlag.SPEC),
        new RecordField<>(new RecordLength()),
        new RecordField<>(new RecordCount()),
        new RecordField<>(new Cycle()),
        new RecordField<>("blank", new BlankSpec(2)),
        new RecordField<>(new CreationDate()),
        new RecordField<>(new CreationTime()),
        new RecordField<>("blank2", new BlankSpec(1)),
        new RecordField<>(new DataSupplierIdent()),
        new RecordField<>(new TargetCustomerIdent()),
        new RecordField<>(new DatabasePartNumber()),
        new RecordField<>("reserved", new BlankSpec(11)),
        new RecordField<>(new FileCrc())
    );
  }

  @Override
  public int recordLength() {
    return 132;
  }

  @Override
  public List<RecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return arincRecord.startsWith("HDR01");
  }
}
