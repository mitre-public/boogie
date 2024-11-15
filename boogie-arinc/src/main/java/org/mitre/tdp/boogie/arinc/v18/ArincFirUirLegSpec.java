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
import org.mitre.tdp.boogie.arinc.v18.field.CruiseTableIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirAddress;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirEntryReport;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirName;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirReportingUnitsAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirReportingUnitsSpeed;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Limit;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;

import com.google.common.collect.ImmutableList;

public final class ArincFirUirLegSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public ArincFirUirLegSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new FirUirIdentifier()),  // FIR/UIR Identifier
        new RecordField<>(new FirUirAddress()),  // FIR/UIR Address
        new RecordField<>(FirUirIndicator.SPEC),  // FIR/UIR Indicator
        new RecordField<>(new SequenceNumber(4)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("adjacentFirIdentifier", new FirUirIdentifier()),  // Adjacent FIR Identifier
        new RecordField<>("adjacentUirIdentifier", new FirUirIdentifier()),  // Adjacent UIR Identifier
        new RecordField<>("reportingUnitsSpeed", new FirUirReportingUnitsSpeed()),
        new RecordField<>("reportingUnitsAltitude", new FirUirReportingUnitsAltitude()),
        new RecordField<>(new FirUirEntryReport()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>(BoundaryVia.SPEC),
        new RecordField<>("firUirLatitude", new Latitude()),
        new RecordField<>("firUirLongitude", new Longitude()),
        new RecordField<>("arcOriginLatitude", new Latitude()),
        new RecordField<>("arcOriginLongitude", new Longitude()),
        new RecordField<>(new ArcDistance()),
        new RecordField<>(new ArcBearing()),
        new RecordField<>("firUpperLimit", new Limit()),  // FIR Upper Limit
        new RecordField<>("uirLowerLimit", new Limit()),  // UIR Lower Limit
        new RecordField<>("uirUpperLimit", new Limit()),  // UIR Upper Limit
        new RecordField<>(new CruiseTableIndicator()),
        new RecordField<>("blank2", new BlankSpec(1)),  // Reserved (Expansion)
        new RecordField<>(new FirUirName()),
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
    return arincRecord.charAt(4) == 'U' && arincRecord.charAt(5) == 'F' && PrimaryRecord.INSTANCE.test(arincRecord.substring(19, 20));
  }
}
