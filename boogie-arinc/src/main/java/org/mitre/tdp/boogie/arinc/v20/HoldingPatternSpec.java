package org.mitre.tdp.boogie.arinc.v20;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.ArcRadius;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DuplicateIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.HoldingName;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.InboundMagneticCourse;
import org.mitre.tdp.boogie.arinc.v18.field.LegLength;
import org.mitre.tdp.boogie.arinc.v18.field.LegTime;
import org.mitre.tdp.boogie.arinc.v18.field.MaxAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.MinimumAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RegionCode;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMaximumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMinimumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.VerticalScaleFactor;
import org.mitre.tdp.boogie.arinc.v20.field.LegInboundOutboundIndicator;

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
        new RecordField<>(new LegInboundOutboundIndicator()),
        new RecordField<>("Reserved", new BlankSpec(17)),
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
