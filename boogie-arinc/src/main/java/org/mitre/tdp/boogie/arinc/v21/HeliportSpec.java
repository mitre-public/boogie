package org.mitre.tdp.boogie.arinc.v21;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v21.field.HeliportType;

/**
 * Record Specification for ARINC heliport records in V21.
 */
public final class HeliportSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public HeliportSpec() {
    recordFields = List.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("heliportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("heliportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IataDesignator()),
        new RecordField<>("blank2", new BlankSpec(5)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new SpeedLimitAltitude()),
        new RecordField<>(new DatumCode()),
        new RecordField<>(new IfrCapability()),
        new RecordField<>(HeliportType.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("heliportElevation", new AirportHeliportElevation()),
        new RecordField<>(new SpeedLimit()),
        new RecordField<>("recommendedVhfNavaid", new RecommendedNavaid()),
        new RecordField<>("recommendedVhfNavaidIcaoRegion", new IcaoRegion()),
        new RecordField<>("transitionAltitude", new TransitionAltitude()),
        new RecordField<>("transitionLevel", new TransitionAltitude()),
        new RecordField<>(PublicMilitaryIndicator.SPEC),
        new RecordField<>("timezone", new BlankSpec(3)), // 5.178
        new RecordField<>(new DaylightTimeIndicator()),
        new RecordField<>("blank3", new BlankSpec(6)),
        new RecordField<>(MagneticTrueIndicator.SPEC),
        new RecordField<>("blank4", new BlankSpec(1)),
        new RecordField<>("heliportFullName", new NameField()),
        new RecordField<>(new FileRecordNumber()),
        new RecordField<>("lastUpdatedCycle", new Cycle())
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
    return arincRecord.charAt(4) == 'H' && arincRecord.charAt(12) == 'A' && PrimaryRecord.INSTANCE.test(arincRecord.substring(21, 22));
  }
}
