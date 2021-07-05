package org.mitre.tdp.boogie.v18.spec.record;

import static org.mitre.tdp.boogie.ArincField.newField;

import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.v18.spec.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.LandingThresholdElevation;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.RunwayDescription;
import org.mitre.tdp.boogie.v18.spec.field.RunwayGradient;
import org.mitre.tdp.boogie.v18.spec.field.RunwayIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.RunwayLength;
import org.mitre.tdp.boogie.v18.spec.field.RunwayMagneticBearing;
import org.mitre.tdp.boogie.v18.spec.field.RunwayWidth;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.Stopway;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.v18.spec.field.ThresholdDisplacementDistance;

import com.google.common.collect.ImmutableList;

/**
 * Runway specification from ARINC.
 */
public final class RunwaySpec implements RecordSpec {

  private final List<ArincField<?>> recordFields;

  public RunwaySpec() {
    this.recordFields = ImmutableList.of(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField("blank1", new BlankSpec(1)),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField("airportIcaoRegion", new IcaoRegion()),
        newField(new SubSectionCode()),
        newField(new RunwayIdentifier()),
        newField("blank2", new BlankSpec(3)),
        newField(new ContinuationRecordNumber()),
        newField(new RunwayLength()),
        newField(new RunwayMagneticBearing()),
        newField("blank3", new BlankSpec(1)),
        newField(new Latitude()),
        newField(new Longitude()),
        newField(new RunwayGradient()),
        newField("blank4", new BlankSpec(4)),
        newField("blank5", new BlankSpec(6)), // 5.225
        newField(new LandingThresholdElevation()),
        newField(new ThresholdDisplacementDistance()),
        newField(new ThresholdCrossingHeight()),
        newField(new RunwayWidth()),
        newField("tch", new BlankSpec(1)), // 5.270
        newField(new IlsMlsGlsIdentifier()),
        newField("cat", new IlsMlsGlsCategory()),
        newField(new Stopway()),
        newField("secondaryIlsMlsGlsIdentifier", new IlsMlsGlsIdentifier()),
        newField("secondaryCat", new IlsMlsGlsCategory()),
        newField("reserved", new BlankSpec(6)),
        newField(new RunwayDescription()),
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
    return arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13)).equals("PG");
  }
}
