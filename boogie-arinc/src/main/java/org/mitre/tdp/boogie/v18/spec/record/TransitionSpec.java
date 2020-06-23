package org.mitre.tdp.boogie.v18.spec.record;

import static org.mitre.tdp.boogie.ArincField.newField;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.AltitudeDescription;
import org.mitre.tdp.boogie.v18.spec.field.ArcRadius;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.CenterFix;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.FixIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.MinimumAltitude;
import org.mitre.tdp.boogie.v18.spec.field.OutboundMagneticCourse;
import org.mitre.tdp.boogie.v18.spec.field.PathTerm;
import org.mitre.tdp.boogie.v18.spec.field.RecommendedNavaid;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.Rho;
import org.mitre.tdp.boogie.v18.spec.field.Rnp;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.v18.spec.field.RouteType;
import org.mitre.tdp.boogie.v18.spec.field.RouteTypeQualifier;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.SequenceNumber;
import org.mitre.tdp.boogie.v18.spec.field.SidStarIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.SpeedLimit;
import org.mitre.tdp.boogie.v18.spec.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.Theta;
import org.mitre.tdp.boogie.v18.spec.field.TransitionAltitude;
import org.mitre.tdp.boogie.v18.spec.field.TransitionIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirection;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirectionValid;
import org.mitre.tdp.boogie.v18.spec.field.VerticalAngle;
import org.mitre.tdp.boogie.v18.spec.field.WaypointDescription;

/**
 * Record specification for an ARINC SID/STAR/APPROACH.
 */
public class TransitionSpec implements RecordSpec {

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
        newField("blank1", new BlankSpec(1)),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField("airportIcaoRegion", new IcaoRegion()),
        newField(new SubSectionCode()),
        newField(new SidStarIdentifier()),
        newField(new RouteType()),
        newField(new TransitionIdentifier()),
        newField("blank2", new BlankSpec(1)),
        newField(new SequenceNumber(3)),
        newField(new FixIdentifier()),
        newField("fixIcaoRegion", new IcaoRegion()),
        newField("fixSectionCode", SectionCode.SPEC),
        newField("fixSubSectionCode", new SubSectionCode()),
        newField(new ContinuationRecordNumber()),
        newField(new WaypointDescription()),
        newField(TurnDirection.SPEC),
        newField(new Rnp()),
        newField(new PathTerm()),
        newField(new TurnDirectionValid()),
        newField(new RecommendedNavaid()),
        newField("recommendedNavaidIcaoRegion", new IcaoRegion()),
        newField(new ArcRadius()),
        newField(new Theta()),
        newField(new Rho()),
        newField(new OutboundMagneticCourse()),
        newField(new RouteHoldDistanceTime()),
        newField("recommendedNavaidSectionCode", SectionCode.SPEC),
        newField("recommendedNavaidSubSectionCode", new SubSectionCode()),
        newField("blank3", new BlankSpec(2)),
        newField(new AltitudeDescription()),
        newField("atc", new BlankSpec(1)), // 5.81
        newField("minAltitude1", new MinimumAltitude()),
        newField("minAltitude2", new MinimumAltitude()),
        newField(new TransitionAltitude()),
        newField(new SpeedLimit()),
        newField(new VerticalAngle()),
        newField(new CenterFix()),
        newField("multiCd", new BlankSpec(1)), // 5.130 or 5.272
        newField("centerFixIcaoRegion", new IcaoRegion()),
        newField("centerFixSectionCode", SectionCode.SPEC),
        newField("centerFixSubSectionCode", new SubSectionCode()),
        newField("unk", new BlankSpec(1)), // 5.222
        newField(SpeedLimitDescription.SPEC),
        newField("routeQualifier1", new RouteTypeQualifier()),
        newField("routeQualifier2", new RouteTypeQualifier()),
        newField("blank4", new BlankSpec(3)),
        newField(new FileRecordNumber()),
        newField(new Cycle()));
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    String sectionSubsection = arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13));
    return Arrays.asList("PD", "PE", "PF").contains(sectionSubsection);
  }
}
