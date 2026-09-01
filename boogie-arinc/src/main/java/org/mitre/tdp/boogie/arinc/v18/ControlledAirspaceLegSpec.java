package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column6;

public final class ControlledAirspaceLegSpec implements RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(column6('U', 'C'));

  private final List<RecordField<?>> recordFields;

  public ControlledAirspaceLegSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IcaoRegion()),
        new RecordField<>(AirspaceType.SPEC),
        new RecordField<>(new AirspaceCenter()),
        new RecordField<>("sectionCode2", SectionCode.SPEC),
        new RecordField<>("subSectionCode2", new SubSectionCode()),
        new RecordField<>(new AirspaceClassification()),
        new RecordField<>("blank1", new BlankSpec(2)),
        new RecordField<>(new MultipleCode()),
        new RecordField<>(new SequenceNumber(4)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(Level.SPEC),
        new RecordField<>(new TimeCode()),
        new RecordField<>(new Notam()),
        new RecordField<>("blank2", new BlankSpec(2)),
        new RecordField<>(BoundaryVia.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>("arcOriginLatitude", new Latitude()),
        new RecordField<>("arcOriginLongitude", new Longitude()),
        new RecordField<>(new ArcDistance()),
        new RecordField<>(new ArcBearing()),
        new RecordField<>(new Rnp()),
        new RecordField<>("lowerLimit", new Limit()),
        new RecordField<>("lowerIndicator", new UnitIndicator()),
        new RecordField<>("upperLimit", new Limit()),
        new RecordField<>("upperIndicator", new UnitIndicator()),
        new RecordField<>(new ControlledAirspaceName()),
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
  public List<RecordDiscriminator> recordDiscriminators() {
    return DISCRIMINATORS;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return arincRecord.charAt(4) == 'U' && arincRecord.charAt(5) == 'C' && PrimaryRecord.INSTANCE.test(arincRecord.charAt(24));
  }
}
