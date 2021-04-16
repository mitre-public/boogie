package org.mitre.tdp.boogie.arinc.v18.spec;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.RecordSpecValidator;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CruiseTableIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DirectionRestriction;
import org.mitre.tdp.boogie.arinc.v18.field.EuIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FixedRadiusTransitionIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.InboundMagneticCourse;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.MaxAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.MinimumAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.OutboundMagneticCourse;
import org.mitre.tdp.boogie.arinc.v18.field.RecommendedNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.Rho;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.arinc.v18.field.RouteIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.RouteType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SixthCharacter;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.Theta;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

import com.google.common.collect.ImmutableList;

/**
 * The specification for an ARINC enroute airway record V18.
 */
public final class AirwaySpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public AirwaySpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("blank1", new BlankSpec(7)),
        new RecordField<>("routeIdentifier", RouteIdentifier.enroute()),
        new RecordField<>(new SixthCharacter()),
        new RecordField<>("blank2", new BlankSpec(6)),
        new RecordField<>("sequenceNumber", new SequenceNumber(4)),
        new RecordField<>(new FixIdentifier()),
        new RecordField<>("fixIcaoRegion", new IcaoRegion()),
        new RecordField<>("fixSectionCode", SectionCode.SPEC),
        new RecordField<>("fixSubSectionCode", new SubSectionCode()),
        new RecordField<>("continuationRecordNumber", new ContinuationRecordNumber()),
        new RecordField<>(new WaypointDescription()),
        new RecordField<>("boundaryCode", BoundaryCode.SPEC),
        new RecordField<>("routeType", new RouteType()),
        new RecordField<>("level", Level.SPEC),
        new RecordField<>(new DirectionRestriction()),
        new RecordField<>(new CruiseTableIndicator()),
        new RecordField<>(new EuIndicator()),
        new RecordField<>("recommendedNavaid", new RecommendedNavaid()),
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
        new RecordField<>("cycle", new Cycle())
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
    return arincRecord.substring(4, 6).equals("ER");
  }
}
