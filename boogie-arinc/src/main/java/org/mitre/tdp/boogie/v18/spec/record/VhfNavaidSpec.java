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
import org.mitre.tdp.boogie.v18.spec.field.DmeElevation;
import org.mitre.tdp.boogie.v18.spec.field.DmeIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.FigureOfMerit;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.FrequencyProtectionDistance;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.IlsDmeBias;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;
import org.mitre.tdp.boogie.v18.spec.field.NameField;
import org.mitre.tdp.boogie.v18.spec.field.NavaidClass;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.StationDeclination;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.VorNdbFrequency;
import org.mitre.tdp.boogie.v18.spec.field.VorNdbIdentifier;

/**
 * Specification for a VHF navaid.
 */
public class VhfNavaidSpec implements RecordSpec {

  @Override
  public int recordLength() {
    return 132;
  }

  @Override
  public List<ArincField<?>> recordFields() {
    return Arrays.asList(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField(new SubSectionCode()),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField("airportIcaoRegion", new IcaoRegion()),
        newField("blank1", new BlankSpec(1)),
        newField(new VorNdbIdentifier()),
        newField("blank2", new BlankSpec(2)),
        newField(new IcaoRegion()),
        newField(new ContinuationRecordNumber()),
        newField(new VorNdbFrequency()),
        newField(new NavaidClass()),
        newField(new Latitude()),
        newField(new Longitude()),
        newField(new DmeIdentifier()),
        newField("dmeLatitude", new Latitude()),
        newField("dmeLongitude", new Longitude()),
        newField(new StationDeclination()),
        newField(new DmeElevation()),
        newField(new FigureOfMerit()),
        newField(new IlsDmeBias()),
        newField(new FrequencyProtectionDistance()),
        newField(new DatumCode()),
        newField("vhfNavaidName", new NameField()),
        newField(new FileRecordNumber()),
        newField(new Cycle()));
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return arincRecord.substring(4, 5).equals("D");
  }
}
