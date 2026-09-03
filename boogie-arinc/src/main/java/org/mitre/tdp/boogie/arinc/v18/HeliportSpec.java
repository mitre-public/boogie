package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn13;

/**
 * Record Specification for ARINC heliport records in V18.
 */
public final class HeliportSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn13('H', 'A', 21));

  private final List<RecordField<?>> recordFields;

  public HeliportSpec() {
    super(DISCRIMINATORS);
    recordFields = List.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("heliportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("heliportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IataDesignator()),
        new RecordField<>("helipadIdentifier", new PadIdentifier()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new SpeedLimitAltitude()),
        new RecordField<>(new DatumCode()),
        new RecordField<>(new IfrCapability()),
        new RecordField<>("blank2", new BlankSpec(1)),
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
        new RecordField<>(new PadDimensions()),
        new RecordField<>(MagneticTrueIndicator.SPEC),
        new RecordField<>("reserved", new BlankSpec(1)),
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

}
