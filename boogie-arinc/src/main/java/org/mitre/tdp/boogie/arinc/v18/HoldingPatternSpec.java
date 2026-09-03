package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

public final class HoldingPatternSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn6('E', 'P', 38));

  private final List<RecordField<?>> recordFields;

  public HoldingPatternSpec() {
    super(DISCRIMINATORS);
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
        new RecordField<>("Reserved", new BlankSpec(27)),
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

}
