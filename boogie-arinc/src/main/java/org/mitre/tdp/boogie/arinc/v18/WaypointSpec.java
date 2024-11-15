package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DatumCode;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticVariation;
import org.mitre.tdp.boogie.arinc.v18.field.NameFormat;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointNameDescription;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointType;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointUsage;

import com.google.common.collect.ImmutableList;

/**
 * Specification for Terminal/Enroute waypoint records in ARINC V18.
 */
public final class WaypointSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public WaypointSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("enrouteSubSectionCode", new SubSectionCode()),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>("terminalSubSectionCode", new SubSectionCode()),
        new RecordField<>("waypointIdentifier", new FixIdentifier()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("waypointIcaoRegion", new IcaoRegion()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("blank2", new BlankSpec(4)),
        new RecordField<>(new WaypointType()),
        new RecordField<>(new WaypointUsage()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>("blank4", new BlankSpec(23)),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("waypointElevationXX", new BlankSpec(5)),
        new RecordField<>(new DatumCode()),
        new RecordField<>("blank5", new BlankSpec(8)),
        new RecordField<>(new NameFormat()),
        new RecordField<>(new WaypointNameDescription()),
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
    return (arincRecord.regionMatches(4, "EA", 0, 2) || (arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'C'))
        && PrimaryRecord.INSTANCE.test(arincRecord.substring(21, 22));
  }
}
