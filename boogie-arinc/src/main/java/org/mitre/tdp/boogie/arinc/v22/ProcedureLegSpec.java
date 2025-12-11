package org.mitre.tdp.boogie.arinc.v22;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v20.field.LegInboundOutboundIndicator;
import org.mitre.tdp.boogie.arinc.v21.field.GnssFmsIndicator;
import org.mitre.tdp.boogie.arinc.v21.field.PreferredMultipleApproachIndicator;
import org.mitre.tdp.boogie.arinc.v22.field.AltitudeDescription;
import org.mitre.tdp.boogie.arinc.v22.field.ProcedureDesignAircraftCategoryOrType;
import org.mitre.tdp.boogie.arinc.v22.field.RouteTypeQualifier;

import com.google.common.collect.ImmutableList;

public final class ProcedureLegSpec implements RecordSpec {

  private static final Predicate<String> airportRecords = arincRecord -> (Stream.of("PD", "PE", "PF").anyMatch(ss -> arincRecord.regionMatches(4, ss, 0, 1) && arincRecord.regionMatches(12, ss, 1, 1)));
  private static final Predicate<String> heliportRecords = arincRecord -> (Stream.of("HD", "HE", "HF").anyMatch(ss -> arincRecord.regionMatches(4, ss, 0, 1) && arincRecord.regionMatches(12, ss, 1, 1)));

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
        new RecordField<>(ProcedureDesignAircraftCategoryOrType.SPEC),
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
        new RecordField<>(new LegInboundOutboundIndicator()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>(new AltitudeDescription()),
        new RecordField<>("atc", new BlankSpec(1)), // 5.81 just nope-ing out of this one
        new RecordField<>("minAltitude1", new MinimumAltitude()),
        new RecordField<>("minAltitude2", new MinimumAltitude()),
        new RecordField<>(new TransitionAltitude()),
        new RecordField<>(new SpeedLimit()),
        new RecordField<>(new VerticalAngle()),
        new RecordField<>("centerFixIdentifier", new CenterFix()),
        new RecordField<>("multiCd", new BlankSpec(1)), // 5.130 or 5.272 just nope-ing out of this one
        new RecordField<>("centerFixIcaoRegion", new IcaoRegion()),
        new RecordField<>("centerFixSectionCode", SectionCode.SPEC),
        new RecordField<>("centerFixSubSectionCode", new SubSectionCode()),
        new RecordField<>(new GnssFmsIndicator()),
        new RecordField<>(new SpeedLimitDescription()),
        new RecordField<>("routeTypeQualifier1", new RouteTypeQualifier()),
        new RecordField<>("routeTypeQualifier2", new RouteTypeQualifier()),
        new RecordField<>("routeTypeQualifier3", new RouteTypeQualifier()),
        new RecordField<>(new PreferredMultipleApproachIndicator()),
        new RecordField<>("blank4", new BlankSpec(1)),
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
    return airportRecords.or(heliportRecords).test(arincRecord) && PrimaryRecord.INSTANCE.test(arincRecord.substring(38, 39));
  }
}
