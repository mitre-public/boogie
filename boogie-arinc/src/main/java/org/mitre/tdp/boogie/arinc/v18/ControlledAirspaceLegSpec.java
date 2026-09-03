package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

public final class ControlledAirspaceLegSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn6('U', 'C', 24));

  private final List<RecordField<?>> recordFields;

  public ControlledAirspaceLegSpec() {
    super(DISCRIMINATORS);
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

}
