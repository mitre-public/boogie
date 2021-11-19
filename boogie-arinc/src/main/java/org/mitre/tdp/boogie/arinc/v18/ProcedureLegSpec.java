package org.mitre.tdp.boogie.arinc.v18;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.AltitudeDescription;
import org.mitre.tdp.boogie.arinc.v18.field.ArcRadius;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CenterFix;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.MinimumAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.OutboundMagneticCourse;
import org.mitre.tdp.boogie.arinc.v18.field.PathTerm;
import org.mitre.tdp.boogie.arinc.v18.field.RecommendedNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.Rho;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.arinc.v18.field.RouteType;
import org.mitre.tdp.boogie.arinc.v18.field.RouteTypeQualifier;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SidStarIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.Theta;
import org.mitre.tdp.boogie.arinc.v18.field.TransitionAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.TransitionIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirectionValid;
import org.mitre.tdp.boogie.arinc.v18.field.VerticalAngle;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

import com.google.common.collect.ImmutableList;

/**
 * Record specification for an ARINC SID/STAR/APPROACH V18.
 */
public final class ProcedureLegSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public ProcedureLegSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new SidStarIdentifier()),
        new RecordField<>(new RouteType()),
        new RecordField<>(new TransitionIdentifier()),
        new RecordField<>("blank2", new BlankSpec(1)),
        new RecordField<>(new SequenceNumber(3)),
        new RecordField<>(new FixIdentifier()),
        new RecordField<>("fixIcaoRegion", new IcaoRegion()),
        new RecordField<>("fixSectionCode", SectionCode.SPEC),
        new RecordField<>("fixSubSectionCode", new SubSectionCode()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new WaypointDescription()),
        new RecordField<>(TurnDirection.SPEC),
        new RecordField<>(new Rnp()),
        new RecordField<>(new PathTerm()),
        new RecordField<>(new TurnDirectionValid()),
        new RecordField<>("recommendedNavaidIdentifier", new RecommendedNavaid()),
        new RecordField<>("recommendedNavaidIcaoRegion", new IcaoRegion()),
        new RecordField<>(new ArcRadius()),
        new RecordField<>(new Theta()),
        new RecordField<>(new Rho()),
        new RecordField<>(new OutboundMagneticCourse()),
        new RecordField<>(new RouteHoldDistanceTime()),
        new RecordField<>("recommendedNavaidSectionCode", SectionCode.SPEC),
        new RecordField<>("recommendedNavaidSubSectionCode", new SubSectionCode()),
        new RecordField<>("blank3", new BlankSpec(2)),
        new RecordField<>(new AltitudeDescription()),
        new RecordField<>("atc", new BlankSpec(1)), // 5.81
        new RecordField<>("minAltitude1", new MinimumAltitude()),
        new RecordField<>("minAltitude2", new MinimumAltitude()),
        new RecordField<>(new TransitionAltitude()),
        new RecordField<>(new SpeedLimit()),
        new RecordField<>(new VerticalAngle()),
        new RecordField<>("centerFixIdentifier", new CenterFix()),
        new RecordField<>("multiCd", new BlankSpec(1)), // 5.130 or 5.272
        new RecordField<>("centerFixIcaoRegion", new IcaoRegion()),
        new RecordField<>("centerFixSectionCode", SectionCode.SPEC),
        new RecordField<>("centerFixSubSectionCode", new SubSectionCode()),
        new RecordField<>("unk", new BlankSpec(1)), // 5.222
        new RecordField<>(new SpeedLimitDescription()),
        new RecordField<>("routeTypeQualifier1", new RouteTypeQualifier()),
        new RecordField<>("routeTypeQualifier2", new RouteTypeQualifier()),
        new RecordField<>("blank4", new BlankSpec(3)),
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
  public boolean matchesRecord(String arincRecord) {
    String sectionSubsection = arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13));
    return Arrays.asList("PD", "PE", "PF").contains(sectionSubsection);
  }
}
