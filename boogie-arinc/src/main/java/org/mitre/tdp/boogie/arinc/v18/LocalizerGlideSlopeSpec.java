package org.mitre.tdp.boogie.arinc.v18;

import java.util.List;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ComponentElevation;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.GlideSlopeAngle;
import org.mitre.tdp.boogie.arinc.v18.field.GlideSlopePosition;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerAzimuthPositionReference;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerBearing;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerFrequency;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerPosition;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerWidth;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.StationDeclination;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbIdentifier;

import com.google.common.collect.ImmutableList;

public final class LocalizerGlideSlopeSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public LocalizerGlideSlopeSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("localizerIdentifier", new IlsMlsGlsIdentifier()),
        new RecordField<>(new IlsMlsGlsCategory()),
        new RecordField<>("blank3", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new LocalizerFrequency()),
        new RecordField<>(new RunwayIdentifier()),
        new RecordField<>("localizerLatitude", new Latitude()),
        new RecordField<>("localizerLongitude", new Longitude()),
        new RecordField<>(new LocalizerBearing()),
        new RecordField<>("glideSlopeLatitude", new Latitude()),
        new RecordField<>("glideSlopeLongitude", new Longitude()),
        new RecordField<>(new LocalizerPosition()),
        new RecordField<>("localizerPositionReference", new LocalizerAzimuthPositionReference()),
        new RecordField<>(new GlideSlopePosition()),
        new RecordField<>(new LocalizerWidth()),
        new RecordField<>(new GlideSlopeAngle()),
        new RecordField<>(new StationDeclination()),
        new RecordField<>("glideSlopeHeightAtLandingThreshold", new ThresholdCrossingHeight()),
        new RecordField<>("glideSlopeElevation", new ComponentElevation()),
        new RecordField<>("supportingFacilityIdentifier", new VorNdbIdentifier()),
        new RecordField<>("supportingFacilityIcaoRegion", new IcaoRegion()),
        new RecordField<>("supportingFacilitySectionCode", SectionCode.SPEC),
        new RecordField<>("supportingFacilitySubSectionCode", new SubSectionCode()),
        new RecordField<>("reserved", new BlankSpec(13)),
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
    return arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'I' && PrimaryRecord.INSTANCE.test(arincRecord.substring(21, 22));
  }
}
