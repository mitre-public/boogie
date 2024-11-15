package org.mitre.tdp.boogie.arinc.v19;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMaximumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMinimumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.VerticalScaleFactor;

import com.google.common.collect.ImmutableList;

public final class HoldingPatternSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public HoldingPatternSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new RegionCode()),
        new RecordField<>(new IcaoRegion()),
        new RecordField<>("blank", new BlankSpec(15)),
        new RecordField<>(new DuplicateIdentifier()),
        new RecordField<>(new FixIdentifier()),
        new RecordField<>("fixIcaoRegion", new IcaoRegion()),
        new RecordField<>("fixSectionCode", SectionCode.SPEC),
        new RecordField<>("fixSubSectionCode", new SubSectionCode()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("inboundHoldingCourse", new InboundMagneticCourse()),
        new RecordField<>(TurnDirection.SPEC),
        new RecordField<>(new LegLength()),
        new RecordField<>(new LegTime()),
        new RecordField<>(new MinimumAltitude()),
        new RecordField<>(new MaxAltitude()),
        new RecordField<>("holdingSpeed", new SpeedLimit()),
        new RecordField<>(new Rnp()),
        new RecordField<>(new ArcRadius()),
        new RecordField<>(new VerticalScaleFactor()),
        new RecordField<>(new RvsmMinimumLevel()),
        new RecordField<>(new RvsmMaximumLevel()),
        new RecordField<>("Reserved", new BlankSpec(18)),
        new RecordField<>(new HoldingName()),
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
    return arincRecord.regionMatches(4, "EP", 0, 2) && PrimaryRecord.INSTANCE.test(arincRecord.substring(38, 39));
  }
}
