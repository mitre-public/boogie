package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

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
        new RecordField<>(new FirUirIndicator()),  // FIR/UIR Indicator
        new RecordField<>(new SequenceNumber(4)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("adjacentFirIdentifier", new FirUirIdentifier()),  // Adjacent FIR Identifier
        new RecordField<>("adjacentUirIdentifier", new FirUirIdentifier()),  // Adjacent UIR Identifier
        new RecordField<>(new ReportingUnitsSpeed()),
        new RecordField<>(new ReportingUnitsAltitude()),
        new RecordField<>(new EntryReport()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>(BoundaryVia.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
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
    return arincRecord.charAt(4) == 'U' && arincRecord.charAt(5) == 'F' && PrimaryRecord.INSTANCE.test(arincRecord.substring(24, 25));
  }
}
