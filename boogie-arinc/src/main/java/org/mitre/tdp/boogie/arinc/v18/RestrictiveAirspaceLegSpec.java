package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.ArcBearing;
import org.mitre.tdp.boogie.arinc.v18.field.ArcDistance;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
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
import org.mitre.tdp.boogie.arinc.v18.field.RestrictiveAirspaceDesignation;
import org.mitre.tdp.boogie.arinc.v18.field.RestrictiveAirspaceName;
import org.mitre.tdp.boogie.arinc.v18.field.RestrictiveType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TimeCode;
import org.mitre.tdp.boogie.arinc.v18.field.UnitIndicator;

public final class RestrictiveAirspaceLegSpec implements RecordSpec {
  private final List<RecordField<?>> recordFields;

  public RestrictiveAirspaceLegSpec() {
    recordFields = List.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IcaoRegion()),
        new RecordField<>(RestrictiveType.SPEC),
        new RecordField<>(new RestrictiveAirspaceDesignation()),
        new RecordField<>(new MultipleCode()),
        new RecordField<>(new SequenceNumber(4)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(Level.SPEC),
        new RecordField<>(new TimeCode()),
        new RecordField<>(new Notam()),
        new RecordField<>("spacing1", new BlankSpec(2)),
        new RecordField<>(BoundaryVia.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>("arcOriginLatitude", new Latitude()),
        new RecordField<>("arcOriginLongitude", new Longitude()),
        new RecordField<>(new ArcDistance()),
        new RecordField<>(new ArcBearing()),
        new RecordField<>("spacing2", new BlankSpec(3)),
        new RecordField<>("lowerLimit", new Limit()),
        new RecordField<>("lowerIndicator", new UnitIndicator()),
        new RecordField<>("upperLimit", new Limit()),
        new RecordField<>("upperIndicator", new UnitIndicator()),
        new RecordField<>(new RestrictiveAirspaceName()),
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
    return arincRecord.charAt(4) == 'U' && arincRecord.charAt(5) == 'R' && PrimaryRecord.INSTANCE.test(arincRecord.substring(24, 25));
  }
}
