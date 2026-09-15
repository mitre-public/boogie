package org.mitre.tdp.boogie.dafif.v81.spec;

import java.util.List;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.Bearing;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.Derivation;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.Radius;
import org.mitre.tdp.boogie.dafif.v81.field.Sector;
import org.mitre.tdp.boogie.dafif.v81.field.SegmentNumber;
import org.mitre.tdp.boogie.dafif.v81.field.Shape;
import org.mitre.tdp.boogie.dafif.v81.field.SpecialUseAirspaceType;
import org.mitre.tdp.boogie.dafif.v81.field.SuasIdentification;

/**
 * DAFIF 8.1 SUAS.TXT column layout.
 */
public final class DafifSuasSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields = List.of(
      new DafifRecordField<>("suasIdentification", new SuasIdentification()),
      new DafifRecordField<>("sector", new Sector()),
      new DafifRecordField<>("segmentNumber", new SegmentNumber()),
      new DafifRecordField<>("name", new Name()),
      new DafifRecordField<>("specialUseAirspaceType", new SpecialUseAirspaceType()),
      new DafifRecordField<>("icaoCode", new IcaoCode()),
      new DafifRecordField<>("shape", new Shape()),
      new DafifRecordField<>("derivation", new Derivation()),
      new DafifRecordField<>("geodeticLatitude1", new GeodeticLatitude()),
      new DafifRecordField<>("latitude1", new DegreesLatitude()),
      new DafifRecordField<>("geodeticLongitude1", new GeodeticLongitude()),
      new DafifRecordField<>("longitude1", new DegreesLongitude()),
      new DafifRecordField<>("geodeticLatitude2", new GeodeticLatitude()),
      new DafifRecordField<>("latitude2", new DegreesLatitude()),
      new DafifRecordField<>("geodeticLongitude2", new GeodeticLongitude()),
      new DafifRecordField<>("longitude2", new DegreesLongitude()),
      new DafifRecordField<>("geodeticLatitude0", new GeodeticLatitude()),
      new DafifRecordField<>("latitude0", new DegreesLatitude()),
      new DafifRecordField<>("geodeticLongitude0", new GeodeticLongitude()),
      new DafifRecordField<>("longitude0", new DegreesLongitude()),
      new DafifRecordField<>("radius1", new Radius()),
      new DafifRecordField<>("radius2", new Radius()),
      new DafifRecordField<>("bearing1", new Bearing()),
      new DafifRecordField<>("bearing2", new Bearing()),
      new DafifRecordField<>("navaidIdentifier", new NavaidIdentifier()),
      new DafifRecordField<>("navaidType", new NavaidType()),
      new DafifRecordField<>("navaidCountryCode", new CountryCode()),
      new DafifRecordField<>("navaidKeyCode", new NavaidKeyCode()),
      new DafifRecordField<>("cycleDate", new CycleDate())
  );

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.SUAS;
  }

  @Override
  public int expectedNumFields() {
    return 29;
  }
}
