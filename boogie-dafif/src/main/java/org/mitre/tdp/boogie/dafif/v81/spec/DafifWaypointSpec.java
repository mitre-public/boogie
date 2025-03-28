package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointIdentifierWptIdent;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointPointNavaidFlag;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointDescriptionName;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointUsageCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointBearing;
import org.mitre.tdp.boogie.dafif.v81.field.Distance;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointRunwayIdent;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointRwyIcao;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;

import java.util.List;

public class DafifWaypointSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifWaypointSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new WaypointIdentifierWptIdent()),
        new DafifRecordField<>(new CountryCode()),
        new DafifRecordField<>(new StateProvinceCode()),
        new DafifRecordField<>(new WaypointPointNavaidFlag()),
        new DafifRecordField<>(new WaypointType()),
        new DafifRecordField<>(new WaypointDescriptionName()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>(new WaypointUsageCode()),
        new DafifRecordField<>(new WaypointBearing()),
        new DafifRecordField<>(new Distance()),
        new DafifRecordField<>(new WAC()),
        new DafifRecordField<>(new LocalHorizontalDatum()),
        new DafifRecordField<>(new GeodeticDatum()),
        new DafifRecordField<>(new GeodeticLatitude()),
        new DafifRecordField<>(new DegreesLatitude()),
        new DafifRecordField<>(new GeodeticLongitude()),
        new DafifRecordField<>(new DegreesLongitude()),
        new DafifRecordField<>(new MagneticVariation()),
        new DafifRecordField<>(new NavaidIdentifier()),
        new DafifRecordField<>(new NavaidType()),
        new DafifRecordField<>("navaidCountryCode", new CountryCode()),
        new DafifRecordField<>(new NavaidKeyCode()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new WaypointRunwayIdent()),
        new DafifRecordField<>(new WaypointRwyIcao()),
        new DafifRecordField<>(new CoordinatePrecision())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.WPT;
  }

  @Override
  public int expectedNumFields() {
    return 26;
  }
}
