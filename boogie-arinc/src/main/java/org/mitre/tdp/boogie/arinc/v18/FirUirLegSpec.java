package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

public final class FirUirLegSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn6('U', 'F', 19));

  private final List<RecordField<?>> recordFields;

  public FirUirLegSpec() {
    super(DISCRIMINATORS);
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

}
