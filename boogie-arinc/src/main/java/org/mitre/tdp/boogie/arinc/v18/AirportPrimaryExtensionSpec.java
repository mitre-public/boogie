package org.mitre.tdp.boogie.arinc.v18;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;

public final class AirportPrimaryExtensionSpec implements RecordSpec {
  private final List<RecordField<?>> recordFields;

  public AirportPrimaryExtensionSpec() {
    this.recordFields = Arrays.asList(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>(new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IataDesignator()),
        new RecordField<>("reserved1", new BlankSpec(2)),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(ApplicationType.SPEC),
        new RecordField<>(new Notes(100)),
        new RecordField<>(new FileRecordNumber()),
        new RecordField<>(new Cycle())
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
    return arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'A' && PrimaryRecord.INSTANCE.negate().test(arincRecord.substring(21, 22)) && arincRecord.charAt(22) == 'E';
  }
}
