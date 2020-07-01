package org.mitre.tdp.boogie.v18.spec.record;

import static org.mitre.tdp.boogie.ArincField.newField;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.DatumCode;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.FixIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;
import org.mitre.tdp.boogie.v18.spec.field.MagneticVariation;
import org.mitre.tdp.boogie.v18.spec.field.NameFormat;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.WaypointNameDescription;
import org.mitre.tdp.boogie.v18.spec.field.WaypointType;
import org.mitre.tdp.boogie.v18.spec.field.WaypointUsage;

/**
 * Specification for Terminal/Enroute waypoint records in ARINC.
 */
public class WaypointSpec implements RecordSpec {

  private List<ArincField<?>> recordFields;

  public WaypointSpec() {
    this.recordFields = Arrays.asList(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField("enrouteSubSectionCode", new SubSectionCode()),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField("airportIcaoRegion", new IcaoRegion()),
        newField("terminalSubSectionCode", new SubSectionCode()),
        newField(new FixIdentifier()),
        newField("blank1", new BlankSpec(1)),
        newField(new IcaoRegion()),
        newField(new ContinuationRecordNumber()),
        newField("blank2", new BlankSpec(4)),
        newField(new WaypointType()),
        newField(new WaypointUsage()),
        newField("blank3", new BlankSpec(1)),
        newField(new Latitude()),
        newField(new Longitude()),
        newField("blank4", new BlankSpec(23)),
        newField(new MagneticVariation()),
        newField("waypointElevationXX", new BlankSpec(5)),
        newField(new DatumCode()),
        newField("blank5", new BlankSpec(8)),
        newField(new NameFormat()),
        newField(new WaypointNameDescription()),
        newField(new FileRecordNumber()),
        newField(new Cycle()));
  }

  @Override
  public int recordLength() {
    return 132;
  }

  @Override
  public List<ArincField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    // enroute waypoint section subsection are in a different location than the terminal ones
    String enroute = arincRecord.substring(4, 6);
    String terminal = arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13));
    return enroute.equals("EA") || terminal.equals("PC");
  }
}
