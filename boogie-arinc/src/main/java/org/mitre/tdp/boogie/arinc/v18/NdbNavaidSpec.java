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
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticVariation;
import org.mitre.tdp.boogie.arinc.v18.field.NameField;
import org.mitre.tdp.boogie.arinc.v18.field.NavaidClass;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbFrequency;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbIdentifier;

import com.google.common.collect.ImmutableList;

/**
 * Specification for a NDB navaid record from ARINC V18.
 */
public final class NdbNavaidSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public NdbNavaidSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("ndbIdentifier", new VorNdbIdentifier()),
        new RecordField<>("blank2", new BlankSpec(2)),
        new RecordField<>("ndbIcaoRegion", new IcaoRegion()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("ndbFrequency", new VorNdbFrequency()),
        new RecordField<>(new NavaidClass()),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>("blank3", new BlankSpec(23)),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("blank4", new BlankSpec(6)),
        new RecordField<>("reserved", new BlankSpec(5)),
        new RecordField<>(new DatumCode()),
        new RecordField<>("ndbNavaidName", new NameField()),
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
    return (arincRecord.regionMatches(4, "DB", 0, 2) || arincRecord.regionMatches(4, "PN", 0, 2)) && PrimaryRecord.INSTANCE.test(arincRecord.substring(21, 22));
  }
}
