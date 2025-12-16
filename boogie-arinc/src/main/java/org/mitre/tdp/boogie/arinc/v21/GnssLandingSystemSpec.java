package org.mitre.tdp.boogie.arinc.v21;

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
import org.mitre.tdp.boogie.arinc.v18.field.DatumCode;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.GlideSlopeAngle;
import org.mitre.tdp.boogie.arinc.v18.field.GlsChannel;
import org.mitre.tdp.boogie.arinc.v18.field.GlsStationIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerBearing;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticVariation;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.ServiceVolumeRadius;
import org.mitre.tdp.boogie.arinc.v18.field.StationElevationWgs84;
import org.mitre.tdp.boogie.arinc.v18.field.StationType;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TdmaSlots;
import org.mitre.tdp.boogie.arinc.v20.field.ThresholdCrossingHeight;

import com.google.common.collect.ImmutableList;

public final class GnssLandingSystemSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public GnssLandingSystemSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("glsRefPathIdentifier", new IlsMlsGlsIdentifier()),
        new RecordField<>("glsCategory", new IlsMlsGlsCategory()),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new GlsChannel()),
        new RecordField<>(new RunwayIdentifier()),
        new RecordField<>("blank3", new BlankSpec(19)),
        new RecordField<>("glsApproachBearing", new LocalizerBearing()),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new GlsStationIdentifier()),
        new RecordField<>("blank4", new BlankSpec(5)),
        new RecordField<>(new ServiceVolumeRadius()),
        new RecordField<>(new TdmaSlots()),
        new RecordField<>("glsApproachSlope", new GlideSlopeAngle()),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("reserved", new BlankSpec(2)),
        new RecordField<>("stationElevation", new ComponentElevation()),
        new RecordField<>(new DatumCode()),
        new RecordField<>(new StationType()),
        new RecordField<>("blank5", new BlankSpec(2)),
        new RecordField<>(new StationElevationWgs84()),
        new RecordField<>("glidePathTch", new ThresholdCrossingHeight()),
        new RecordField<>("blank6", new BlankSpec(5)),
        new RecordField<>(new FileRecordNumber()),
        new RecordField<>("lastUpdatedCycle", new Cycle())
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
    return (arincRecord.charAt(4) == 'P' || arincRecord.charAt(4) == 'H')
        && arincRecord.charAt(12) == 'T'
        && PrimaryRecord.INSTANCE.test(arincRecord.substring(21,22));
  }
}
