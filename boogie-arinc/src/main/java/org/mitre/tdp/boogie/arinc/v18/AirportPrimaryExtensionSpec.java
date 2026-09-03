package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.Arrays;
import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.continuationColumn13;

public final class AirportPrimaryExtensionSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(continuationColumn13('P', 'A', 21, 'E'));

  private final List<RecordField<?>> recordFields;

  public AirportPrimaryExtensionSpec() {
    super(DISCRIMINATORS);
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

}
