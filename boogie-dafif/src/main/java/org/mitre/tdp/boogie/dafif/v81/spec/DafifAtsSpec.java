package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.AtsIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteSequenceNumber;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteDirection;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteType;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.BiDirectional;
import org.mitre.tdp.boogie.dafif.v81.field.FrequencyClass;
import org.mitre.tdp.boogie.dafif.v81.field.Level;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteStatus;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointIdentifierWptIdent;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode1;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode2;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode3;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode4;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointIdentifierWptIdent;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode1;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode2;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode3;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode4;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteOutboundMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteDistance;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteInboundMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.MinimumAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.UpperLimit;
import org.mitre.tdp.boogie.dafif.v81.field.LowerLimit;
import org.mitre.tdp.boogie.dafif.v81.field.MaxAuthorizedAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.CruiseLevelIndicator;
import org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.AtsDesignator;

import java.util.List;

public final class DafifAtsSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifAtsSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AtsIdentifier()),
        new DafifRecordField<>(new AtsRouteSequenceNumber()),
        new DafifRecordField<>(new AtsRouteDirection()),
        new DafifRecordField<>(new AtsRouteType()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>(new BiDirectional()),
        new DafifRecordField<>(new FrequencyClass()),
        new DafifRecordField<>(new Level()),
        new DafifRecordField<>(new AtsRouteStatus()),
        new DafifRecordField<>("waypoint1IcaoCode", new IcaoCode()),
        new DafifRecordField<>("waypoint1NavaidType", new NavaidType()),
        new DafifRecordField<>("waypoint1WaypointIdentifierWptIdent", new WaypointIdentifierWptIdent()),
        new DafifRecordField<>("waypoint1CountryCode", new CountryCode()),
        new DafifRecordField<>("waypoint1AtsWaypointDescriptionCode1", new AtsWaypointDescriptionCode1()),
        new DafifRecordField<>("waypoint1AtsWaypointDescriptionCode2", new AtsWaypointDescriptionCode2()),
        new DafifRecordField<>("waypoint1AtsWaypointDescriptionCode3", new AtsWaypointDescriptionCode3()),
        new DafifRecordField<>("waypoint1AtsWaypointDescriptionCode4", new AtsWaypointDescriptionCode4()),
        new DafifRecordField<>("waypoint1GeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("waypoint1DegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("waypoint1GeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("waypoint1DegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("waypoint2IcaoCode", new IcaoCode()),
        new DafifRecordField<>("waypoint2NavaidType", new NavaidType()),
        new DafifRecordField<>("waypoint2WaypointIdentifierWptIdent", new WaypointIdentifierWptIdent()),
        new DafifRecordField<>("waypoint2CountryCode", new CountryCode()),
        new DafifRecordField<>("waypoint2AtsWaypointDescriptionCode1", new AtsWaypointDescriptionCode1()),
        new DafifRecordField<>("waypoint2AtsWaypointDescriptionCode2", new AtsWaypointDescriptionCode2()),
        new DafifRecordField<>("waypoint2AtsWaypointDescriptionCode3", new AtsWaypointDescriptionCode3()),
        new DafifRecordField<>("waypoint2AtsWaypointDescriptionCode4", new AtsWaypointDescriptionCode4()),
        new DafifRecordField<>("waypoint2GeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("waypoint2DegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("waypoint2GeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("waypoint2DegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>(new AtsRouteOutboundMagneticCourse()),
        new DafifRecordField<>(new AtsRouteDistance()),
        new DafifRecordField<>(new AtsRouteInboundMagneticCourse()),
        new DafifRecordField<>(new MinimumAltitude()),
        new DafifRecordField<>(new UpperLimit()),
        new DafifRecordField<>(new LowerLimit()),
        new DafifRecordField<>(new MaxAuthorizedAltitude()),
        new DafifRecordField<>(new CruiseLevelIndicator()),
        new DafifRecordField<>(new RequiredNavPerformance()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new AtsDesignator())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.ATS;
  }

  @Override
  public int expectedNumFields() {
    return 44;
  }
}
