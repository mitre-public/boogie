package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn13;

public final class LocalizerGlideSlopeSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(
      primaryColumn13('P', 'I', 21), primaryColumn13('H', 'I', 21));

  private final List<RecordField<?>> recordFields;

  public LocalizerGlideSlopeSpec() {
    super(DISCRIMINATORS);
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

}
