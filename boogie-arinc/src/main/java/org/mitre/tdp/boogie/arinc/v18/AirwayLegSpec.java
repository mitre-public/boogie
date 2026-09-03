package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

/**
 * The specification for an ARINC enroute airway record V18.
 */
public final class AirwayLegSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(primaryColumn6('E', 'R', 38));

  private final List<RecordField<?>> recordFields;

  public AirwayLegSpec() {
    super(DISCRIMINATORS);
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("blank1", new BlankSpec(7)),
        new RecordField<>("routeIdentifier", new EnrouteRouteIdentifier()),
        new RecordField<>(new SixthCharacter()),
        new RecordField<>("blank2", new BlankSpec(6)),
        new RecordField<>("sequenceNumber", new SequenceNumber(4)),
        new RecordField<>(new FixIdentifier()),
        new RecordField<>("fixIcaoRegion", new IcaoRegion()),
        new RecordField<>("fixSectionCode", SectionCode.SPEC),
        new RecordField<>("fixSubSectionCode", new SubSectionCode()),
        new RecordField<>("continuationRecordNumber", new ContinuationRecordNumber()),
        new RecordField<>(new WaypointDescription()),
        new RecordField<>(new BoundaryCode()),
        new RecordField<>(new RouteType()),
        new RecordField<>(Level.SPEC),
        new RecordField<>(new DirectionRestriction()),
        new RecordField<>(new CruiseTableIndicator()),
        new RecordField<>(new EuIndicator()),
        new RecordField<>("recommendedNavaidIdentifier", new RecommendedNavaid()),
        new RecordField<>("recommendedNavaidIcaoRegion", new IcaoRegion()),
        new RecordField<>("rnp", new Rnp()),
        new RecordField<>("blank3", new BlankSpec(3)),
        new RecordField<>("theta", new Theta()),
        new RecordField<>("rho", new Rho()),
        new RecordField<>("outboundMagneticCourse", new OutboundMagneticCourse()),
        new RecordField<>(new RouteHoldDistanceTime()),
        new RecordField<>("inboundMagneticCourse", new InboundMagneticCourse()),
        new RecordField<>("blank4", new BlankSpec(1)),
        new RecordField<>("minAltitude1", new MinimumAltitude()),
        new RecordField<>("minAltitude2", new MinimumAltitude()),
        new RecordField<>(new MaxAltitude()),
        new RecordField<>(new FixedRadiusTransitionIndicator()),
        new RecordField<>("reserved", new BlankSpec(22)),
        new RecordField<>("fileRecordNumber", new FileRecordNumber()),
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
