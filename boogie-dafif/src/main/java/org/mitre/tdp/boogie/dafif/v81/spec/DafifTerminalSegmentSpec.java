package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.AltitudeDescription;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalProcedureType;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSequenceNumber;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalApproachType;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.TrackDescriptionCode;
import org.mitre.tdp.boogie.dafif.v81.field.TermSegWaypointIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode1Arpt;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode2;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode3;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode4;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSegmentTurnDirection;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12Identifier;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12Type;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12KeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.Fix12Bearing;
import org.mitre.tdp.boogie.dafif.v81.field.Fix12Distance;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.Distance;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSegmentAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointMagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalNavaidSlaveVariation;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeGeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeDegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeGeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeDegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimit;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimitAircraftType;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimitAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.VerticalNavigationVnav;
import org.mitre.tdp.boogie.dafif.v81.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.dafif.v81.field.ArcWaypointIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.ArcWaypointCountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.ArcRadius;

import java.util.List;

public class DafifTerminalSegmentSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifTerminalSegmentSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>(new TerminalProcedureType()),
        new DafifRecordField<>(new TerminalIdentifier()),
        new DafifRecordField<>(new TerminalSequenceNumber()),
        new DafifRecordField<>(new TerminalApproachType()),
        new DafifRecordField<>(new TransitionIdentifier()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>(new TrackDescriptionCode()),
        new DafifRecordField<>(new TermSegWaypointIdentifier()),
        new DafifRecordField<>("waypointCountryCode", new CountryCode()),
        new DafifRecordField<>(new TerminalWaypointDescriptionCode1Arpt()),
        new DafifRecordField<>(new TerminalWaypointDescriptionCode2()),
        new DafifRecordField<>(new TerminalWaypointDescriptionCode3()),
        new DafifRecordField<>(new TerminalWaypointDescriptionCode4()),
        new DafifRecordField<>(new TerminalSegmentTurnDirection()),
        new DafifRecordField<>("navaid1Identifier", new Navaid12Identifier()),
        new DafifRecordField<>("navaid1Type", new Navaid12Type()),
        new DafifRecordField<>("navaid1CountryCode", new CountryCode()),
        new DafifRecordField<>("navaid1KeyCode", new Navaid12KeyCode()),
        new DafifRecordField<>("fix1Bearing", new Fix12Bearing()),
        new DafifRecordField<>("fix1Distance", new Fix12Distance()),
        new DafifRecordField<>("navaid2Identifier", new Navaid12Identifier()),
        new DafifRecordField<>("navaid2Type", new Navaid12Type()),
        new DafifRecordField<>("navaid2CountryCode", new CountryCode()),
        new DafifRecordField<>("navaid2KeyCode", new Navaid12KeyCode()),
        new DafifRecordField<>("fix2Bearing", new Fix12Bearing()),
        new DafifRecordField<>("fix2Distance", new Fix12Distance()),
        new DafifRecordField<>(new TerminalMagneticCourse()),
        new DafifRecordField<>(new Distance()),
        new DafifRecordField<>(new AltitudeDescription()),
        new DafifRecordField<>("altitude1", new TerminalSegmentAltitude()),
        new DafifRecordField<>("altitude2", new TerminalSegmentAltitude()),
        new DafifRecordField<>(new RequiredNavPerformance()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>("waypointGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("waypointDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("waypointGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("waypointDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("waypointMagneticVariation", new TerminalWaypointMagneticVariation()),
        new DafifRecordField<>("navaid1GeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("navaid1DegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("navaid1GeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("navaid1DegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("navaid1MagneticVariation", new TerminalNavaidSlaveVariation()),
        new DafifRecordField<>("navaid1DmeGeodeticLatitude", new Nav12DmeGeodeticLatitude()),
        new DafifRecordField<>("navaid1DmeDegreesLatitude", new Nav12DmeDegreesLatitude()),
        new DafifRecordField<>("navaid1DmeGeodeticLongitude", new Nav12DmeGeodeticLongitude()),
        new DafifRecordField<>("navaid1DmeDegreesLongitude", new Nav12DmeDegreesLongitude()),
        new DafifRecordField<>("navaid2GeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("navaid2DegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("navaid2GeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("navaid2DegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("navaid2MagneticVariation", new TerminalNavaidSlaveVariation()),
        new DafifRecordField<>("navaid2DmeGeodeticLatitude", new Nav12DmeGeodeticLatitude()),
        new DafifRecordField<>("navaid2DmeDegreesLatitude", new Nav12DmeDegreesLatitude()),
        new DafifRecordField<>("navaid2DmeGeodeticLongitude", new Nav12DmeGeodeticLongitude()),
        new DafifRecordField<>("navaid2DmeDegreesLongitude", new Nav12DmeDegreesLongitude()),
        new DafifRecordField<>("speedLimit1", new SpeedLimit()),
        new DafifRecordField<>("speedLimitAircraftType1", new SpeedLimitAircraftType()),
        new DafifRecordField<>("speedLimitAltitude1", new SpeedLimitAltitude()),
        new DafifRecordField<>("speedLimit2", new SpeedLimit()),
        new DafifRecordField<>("speedLimitAircraftType2", new SpeedLimitAircraftType()),
        new DafifRecordField<>("speedLimitAltitude2", new SpeedLimitAltitude()),
        new DafifRecordField<>(new VerticalNavigationVnav()),
        new DafifRecordField<>(new ThresholdCrossingHeight()),
        new DafifRecordField<>(new ArcWaypointIdentifier()),
        new DafifRecordField<>(new ArcWaypointCountryCode()),
        new DafifRecordField<>(new ArcRadius())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.TRM_SEG;
  }

  @Override
  public int expectedNumFields() {
    return 68;
  }
}
