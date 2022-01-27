package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.LandingThresholdElevation;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayDescription;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayGradient;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayLength;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayMagneticBearing;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayWidth;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.Stopway;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.arinc.v18.field.ThresholdDisplacementDistance;

import com.google.common.collect.ImmutableList;

/**
 * Runway specification from ARINC V18.
 */
public final class RunwaySpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public RunwaySpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new RunwayIdentifier()),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new RunwayLength()),
        new RecordField<>(new RunwayMagneticBearing()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new RunwayGradient()),
        new RecordField<>("blank4", new BlankSpec(4)),
        new RecordField<>("blank5", new BlankSpec(6)), // 5.225
        new RecordField<>(new LandingThresholdElevation()),
        new RecordField<>(new ThresholdDisplacementDistance()),
        new RecordField<>(new ThresholdCrossingHeight()),
        new RecordField<>(new RunwayWidth()),
        new RecordField<>("tch", new BlankSpec(1)), // 5.270
        new RecordField<>(new IlsMlsGlsIdentifier()),
        new RecordField<>(new IlsMlsGlsCategory()),
        new RecordField<>(new Stopway()),
        new RecordField<>("secondaryIlsMlsGlsIdentifier", new IlsMlsGlsIdentifier()),
        new RecordField<>("secondaryIlsMlsGlsCategory", new IlsMlsGlsCategory()),
        new RecordField<>("reserved", new BlankSpec(6)),
        new RecordField<>(new RunwayDescription()),
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
    return arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'G';
  }
}
