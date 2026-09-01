package org.mitre.tdp.boogie.arinc.v20;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v20.field.ThresholdCrossingHeight;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column13;

public final class RunwaySpec implements RecordSpec {

  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(column13('P', 'G'));

  private final List<RecordField<?>> recordFields;

  public RunwaySpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new RunwayIdentifier()),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new RunwayLength()),
        new RecordField<>(new RunwayMagneticBearing()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new RunwayGradient()),
        new RecordField<>("blank4", new BlankSpec(4)),
        new RecordField<>("blank5", new BlankSpec(6)), // 5.225
        new RecordField<>(new LandingThresholdElevation()),
        new RecordField<>(new ThresholdDisplacementDistance()),
        new RecordField<>("blank6", new BlankSpec(2)),
        new RecordField<>(new RunwayWidth()),
        new RecordField<>("tch", new BlankSpec(1)), // 5.270
        new RecordField<>(new IlsMlsGlsIdentifier()),
        new RecordField<>(new IlsMlsGlsCategory()),
        new RecordField<>(new Stopway()),
        new RecordField<>("secondaryIlsMlsGlsIdentifier", new IlsMlsGlsIdentifier()),
        new RecordField<>("secondaryIlsMlsGlsCategory", new IlsMlsGlsCategory()),
        new RecordField<>(new ThresholdCrossingHeight()),
        new RecordField<>("reserved", new BlankSpec(3)),
        new RecordField<>(new RunwayDescription()),
        new RecordField<>(new FileRecordNumber()),
        new RecordField<>("lastUpdateCycle", new Cycle())
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
    return arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'G' && PrimaryRecord.INSTANCE.test(arincRecord.charAt(22));
  }
}
