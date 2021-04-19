package org.mitre.tdp.boogie.v18.spec.record;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.ComponentElevation;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.GlideSlopeAngle;
import org.mitre.tdp.boogie.v18.spec.field.GlideslopePosition;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.v18.spec.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerAzimuthPositionReference;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerBearing;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerFrequency;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerPosition;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerWidth;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.RunwayIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.StationDeclination;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.v18.spec.field.VorNdbIdentifier;

import static org.mitre.tdp.boogie.ArincField.newField;

public class GlideSlopeSpec implements RecordSpec {

  private List<ArincField<?>> recordFields;

  public GlideSlopeSpec() {
    this.recordFields = Arrays.asList(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField("blank1", new BlankSpec(1)),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField("airportIcaoRegion", new IcaoRegion()),
        newField(new SubSectionCode()),
        newField("localizerIdentifier", new IlsMlsGlsIdentifier()),
        newField("cat", new IlsMlsGlsCategory()),
        newField("blank3", new BlankSpec(3)),
        newField(new ContinuationRecordNumber()),
        newField(new LocalizerFrequency()),
        newField(new RunwayIdentifier()),
        newField("localizerLatitude", new Latitude()),
        newField("localizerLongitude", new Longitude()),
        newField(new LocalizerBearing()),
        newField("glideSlopeLatitude", new Latitude()),
        newField("glideSlopeLongitude", new Longitude()),
        newField(new LocalizerPosition()),
        newField("localizerPositionReference", new LocalizerAzimuthPositionReference()),
        newField(new GlideslopePosition()),
        newField(new LocalizerWidth()),
        newField(new GlideSlopeAngle()),
        newField(new StationDeclination()),
        newField("glideSlopeHeightAtLandingThreshold", new ThresholdCrossingHeight()),
        newField("glideSlopeElevation", new ComponentElevation()),
        newField("supportingFacilityId", new VorNdbIdentifier()),
        newField("supportingFacilityIcaoCode", new IcaoRegion()),
        newField("supportingFacilitySectionCode", SectionCode.SPEC),
        newField("supportingFacilitySubsectionCode", new SubSectionCode()),
        newField("reserved", new BlankSpec(13)),
        newField(new FileRecordNumber()),
        newField(new Cycle())
    );
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
    return arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13)).equals("PI");
  }
}
