package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn13;

/**
 * Record specification for ARINC airport records V18.
 */
public final class AirportSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn13('P', 'A', 21));

  private final List<RecordField<?>> recordFields;

  public AirportSpec() {
    super(DISCRIMINATORS);
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IataDesignator()),
        new RecordField<>("reserved1", new BlankSpec(2)),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new SpeedLimitAltitude()),
        new RecordField<>(new LongestRunway()),
        new RecordField<>(new IfrCapability()),
        new RecordField<>(LongestRunwaySurfaceCode.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("airportElevation", new AirportHeliportElevation()),
        new RecordField<>(new SpeedLimit()),
        new RecordField<>(new RecommendedNavaid()),
        new RecordField<>("recommendedNavaidIcaoRegion", new IcaoRegion()),
        new RecordField<>("transitionAltitude", new TransitionAltitude()),
        new RecordField<>("transitionLevel", new TransitionAltitude()),
        new RecordField<>(PublicMilitaryIndicator.SPEC),
        new RecordField<>("timezone", new BlankSpec(3)), // 5.178
        new RecordField<>(new DaylightTimeIndicator()),
        new RecordField<>(MagneticTrueIndicator.SPEC),
        new RecordField<>(new DatumCode()),
        new RecordField<>("reserved2", new BlankSpec(4)),
        new RecordField<>("airportFullName", new NameField()),
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

}
