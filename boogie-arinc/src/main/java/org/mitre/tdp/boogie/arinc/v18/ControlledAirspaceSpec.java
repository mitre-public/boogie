package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceCenter;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceClassification;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.ArcBearing;
import org.mitre.tdp.boogie.arinc.v18.field.ArcDistance;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.ControlledAirspaceName;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.Limit;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MultipleCode;
import org.mitre.tdp.boogie.arinc.v18.field.Notam;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TimeCode;
import org.mitre.tdp.boogie.arinc.v18.field.UnitIndicator;

import java.util.List;

public final class ControlledAirspaceSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public ControlledAirspaceSpec() {
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
        new RecordField<>(new BoundaryVia()),
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
  public boolean matchesRecord(String arincRecord) {
    return arincRecord.charAt(4) == 'U' && arincRecord.charAt(5) == 'C' && PrimaryRecord.INSTANCE.test(arincRecord.substring(24, 25));
  }
}