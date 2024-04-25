package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.v81.field.AccelerateStopDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndDisplacedThreshold;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndRunwayDisplacedThresholdElevation;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndRunwayElevation;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.Identifier;
import org.mitre.tdp.boogie.dafif.v81.field.LandingDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.Length;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndDisplacedThreshold;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndRunwayDisplacedThresholdElevation;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndRunwayElevation;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticHeading;
import org.mitre.tdp.boogie.dafif.v81.field.PavementClassificationNumber;
import org.mitre.tdp.boogie.dafif.v81.field.LightingSystem;
import org.mitre.tdp.boogie.dafif.v81.field.Slope;
import org.mitre.tdp.boogie.dafif.v81.field.Surface;
import org.mitre.tdp.boogie.dafif.v81.field.TDZE;
import org.mitre.tdp.boogie.dafif.v81.field.TakeoffDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayTrueHeading;
import org.mitre.tdp.boogie.dafif.v81.field.TakeoffRunwayAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.UsableRunway;
import org.mitre.tdp.boogie.dafif.v81.field.Width;

import java.util.List;

public class DafifRunwaySpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifRunwaySpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>("highEndIdentifier", new Identifier()),
        new DafifRecordField<>("lowEndIdentifier", new Identifier()),
        new DafifRecordField<>("highEndMagneticHeading", new MagneticHeading()),
        new DafifRecordField<>("lowEndMagneticHeading", new MagneticHeading()),
        new DafifRecordField<>(new Length()),
        new DafifRecordField<>(new Width()),
        new DafifRecordField<>(new Surface()),
        new DafifRecordField<>(new PavementClassificationNumber()),
        new DafifRecordField<>("highEndGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("highEndDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("highEndGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("highEndDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("highEndElevation", new HighEndRunwayElevation()),
        new DafifRecordField<>("highEndSlope", new Slope()),
        new DafifRecordField<>("highEndTDZE", new TDZE()),
        new DafifRecordField<>("highEndDisplacedThreshold", new HighEndDisplacedThreshold()),
        new DafifRecordField<>("highEndDisplacedThresholdElevation", new HighEndRunwayDisplacedThresholdElevation()),
        new DafifRecordField<>("highEndLightingSystem1", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem2", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem3", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem4", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem5", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem6", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem7", new LightingSystem()),
        new DafifRecordField<>("highEndLightingSystem8", new LightingSystem()),
        new DafifRecordField<>("lowEndGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("lowEndDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("lowEndGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("lowEndDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("lowEndElevation", new LowEndRunwayElevation()),
        new DafifRecordField<>("lowEndSlope", new Slope()),
        new DafifRecordField<>("lowEndTDZE", new TDZE()),
        new DafifRecordField<>("lowEndDisplacedThreshold", new LowEndDisplacedThreshold()),
        new DafifRecordField<>("lowEndDisplacedThresholdElevation", new LowEndRunwayDisplacedThresholdElevation()),
        new DafifRecordField<>("lowEndLightingSystem1", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem2", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem3", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem4", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem5", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem6", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem7", new LightingSystem()),
        new DafifRecordField<>("lowEndLightingSystem8", new LightingSystem()),
        new DafifRecordField<>("trueHeadingHighEnd", new RunwayTrueHeading()),
        new DafifRecordField<>("trueHeadingLowEnd", new RunwayTrueHeading()),
        new DafifRecordField<>(new UsableRunway()),
        new DafifRecordField<>("highEndLandingDistance", new LandingDistanceAvailable()),
        new DafifRecordField<>("highEndTakeoffRunwayDistance", new TakeoffRunwayAvailable()),
        new DafifRecordField<>("highEndTakeOffDistance", new TakeoffDistanceAvailable()),
        new DafifRecordField<>("highEndAccelerateStopDistance", new AccelerateStopDistanceAvailable()),
        new DafifRecordField<>("lowEndLandingDistance", new LandingDistanceAvailable()),
        new DafifRecordField<>("lowEndTakeoffRunwayDistance", new TakeoffRunwayAvailable()),
        new DafifRecordField<>("lowEndTakeOffDistance", new TakeoffDistanceAvailable()),
        new DafifRecordField<>("lowEndAccelerateStopDistance", new AccelerateStopDistanceAvailable()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new CoordinatePrecision())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.RWY;
  }

  @Override
  public int expectedNumFields() {
    return 56;
  }
}
