package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DatumCode;
import org.mitre.tdp.boogie.arinc.v18.field.DmeElevation;
import org.mitre.tdp.boogie.arinc.v18.field.DmeIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FigureOfMerit;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FrequencyProtectionDistance;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.IlsDmeBias;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.NameField;
import org.mitre.tdp.boogie.arinc.v18.field.NavaidClass;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.StationDeclination;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbFrequency;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbIdentifier;

import com.google.common.collect.ImmutableList;

/**
 * Specification for a VHF navaid V18.
 */
public final class VhfNavaidSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public VhfNavaidSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("vhfIdentifier", new VorNdbIdentifier()),
        new RecordField<>("blank2", new BlankSpec(2)),
        new RecordField<>("vhfIcaoRegion", new IcaoRegion()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("vhfFrequency", new VorNdbFrequency()),
        new RecordField<>(new NavaidClass()),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new DmeIdentifier()),
        new RecordField<>("dmeLatitude", new Latitude()),
        new RecordField<>("dmeLongitude", new Longitude()),
        new RecordField<>(new StationDeclination()),
        new RecordField<>(new DmeElevation()),
        new RecordField<>(new FigureOfMerit()),
        new RecordField<>(new IlsDmeBias()),
        new RecordField<>(new FrequencyProtectionDistance()),
        new RecordField<>(new DatumCode()),
        new RecordField<>("vhfNavaidName", new NameField()),
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
    return "D".equals(arincRecord.substring(4, 5));
  }
}
