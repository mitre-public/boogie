package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.OverrunDistance;
import org.mitre.tdp.boogie.dafif.v81.field.OverrunSurface;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;

import java.util.List;

public final class DafifAddRuwaySpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifAddRuwaySpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>("highEndRunwayIdentifier", new RunwayIdentifier()),
        new DafifRecordField<>("lowEndRunwayIdentifier", new RunwayIdentifier()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>("highEndDisplacedThresholdGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("highEndDisplacedThresholdDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("highEndDisplacedThresholdGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("highEndDisplacedThresholdDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("highEndOverrunDistance", new OverrunDistance()),
        new DafifRecordField<>("highEndOverrunSurface", new OverrunSurface()),
        new DafifRecordField<>("highEndOverrunGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("highEndOverrunDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("highEndOverrunGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("highEndOverrunDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("lowEndDisplacedThresholdGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("lowEndDisplacedThresholdDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("lowEndDisplacedThresholdGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("lowEndDisplacedThresholdDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>("lowEndOverrunDistance", new OverrunDistance()),
        new DafifRecordField<>("lowEndOverrunSurface", new OverrunSurface()),
        new DafifRecordField<>("lowEndOverrunGeodeticLatitude", new GeodeticLatitude()),
        new DafifRecordField<>("lowEndOverrunDegreesLatitude", new DegreesLatitude()),
        new DafifRecordField<>("lowEndOverrunGeodeticLongitude", new GeodeticLongitude()),
        new DafifRecordField<>("lowEndOverrunDegreesLongitude", new DegreesLongitude()),
        new DafifRecordField<>(new CycleDate())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.ADD_RWY;
  }

  @Override
  public int expectedNumFields() {
    return 25;
  }
}
