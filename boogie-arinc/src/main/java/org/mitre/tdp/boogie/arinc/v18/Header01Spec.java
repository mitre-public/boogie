package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.prefix;

/**
 * The header records contain information to uniquely identify each data file.
 */
public final class Header01Spec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(prefix("HDR01"));

  private final List<RecordField<?>> recordFields;

  public Header01Spec() {
    super(DISCRIMINATORS);
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

}
