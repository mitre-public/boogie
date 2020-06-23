package org.mitre.tdp.boogie.v18.spec.record;

import static org.mitre.tdp.boogie.ArincField.newField;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.BoundaryCode;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CruiseTableIndicator;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.DirectionRestriction;
import org.mitre.tdp.boogie.v18.spec.field.EuIndicator;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.FixIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.FixedRadiusTransitionIndicator;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.InboundMagneticCourse;
import org.mitre.tdp.boogie.v18.spec.field.Level;
import org.mitre.tdp.boogie.v18.spec.field.MaxAltitude;
import org.mitre.tdp.boogie.v18.spec.field.MinimumAltitude;
import org.mitre.tdp.boogie.v18.spec.field.OutboundMagneticCourse;
import org.mitre.tdp.boogie.v18.spec.field.RecommendedNavaid;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.Rho;
import org.mitre.tdp.boogie.v18.spec.field.Rnp;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.v18.spec.field.RouteIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.RouteType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.SequenceNumber;
import org.mitre.tdp.boogie.v18.spec.field.SixthCharacter;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.Theta;
import org.mitre.tdp.boogie.v18.spec.field.WaypointDescription;

/**
 * The specification for an ARINC enroute airway record.
 */
public class AirwaySpec implements RecordSpec {

  @Override
  public int recordLength() {
    return 132;
  }

  @Override
  public List<ArincField<?>> recordFields() {
    return Arrays.asList(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField(new SubSectionCode()),
        newField("blank1", new BlankSpec(7)),
        newField("routeIdentifier", RouteIdentifier.enroute()),
        newField(new SixthCharacter()),
        newField("blank2", new BlankSpec(6)),
        newField("sequenceNumber", new SequenceNumber(4)),
        newField(new FixIdentifier()),
        newField("fixIcaoRegion", new IcaoRegion()),
        newField("fixSectionCode", SectionCode.SPEC),
        newField("fixSubSectionCode", new SubSectionCode()),
        newField("continuationRecordNumber", new ContinuationRecordNumber()),
        newField(new WaypointDescription()),
        newField("boundaryCode", BoundaryCode.SPEC),
        newField("routeType", new RouteType()),
        newField("level", Level.SPEC),
        newField(new DirectionRestriction()),
        newField(new CruiseTableIndicator()),
        newField(new EuIndicator()),
        newField("recommendedNavaid", new RecommendedNavaid()),
        newField("recommendedNavaidIcaoRegion", new IcaoRegion()),
        newField("rnp", new Rnp()),
        newField("blank3", new BlankSpec(3)),
        newField("theta", new Theta()),
        newField("rho", new Rho()),
        newField("outboundMagneticCourse", new OutboundMagneticCourse()),
        newField( new RouteHoldDistanceTime()),
        newField("inboundMagneticCourse", new InboundMagneticCourse()),
        newField("blank4", new BlankSpec(1)),
        newField("minAltitude1", new MinimumAltitude()),
        newField("minAltitude2", new MinimumAltitude()),
        newField(new MaxAltitude()),
        newField(new FixedRadiusTransitionIndicator()),
        newField("reserved", new BlankSpec(22)),
        newField("fileRecordNumber", new FileRecordNumber()),
        newField("cycle", new Cycle()));
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return arincRecord.substring(4, 6).equals("ER");
  }
}
