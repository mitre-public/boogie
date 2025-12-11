package org.mitre.tdp.boogie.arinc.v21;

import java.util.List;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.ComponentElevation;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.LandingThresholdElevation;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.HelicopterPerformanceRequirement;
import org.mitre.tdp.boogie.arinc.v21.field.MaximumAllowableHelicopterWeight;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;
import org.mitre.tdp.boogie.arinc.v21.field.PadDimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadIdentifier;
import org.mitre.tdp.boogie.arinc.v21.field.SurfaceType;

import com.google.common.collect.ImmutableList;

public final class HelipadSpec implements RecordSpec {
  private final List<RecordField<?>> recordFields;

  public HelipadSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportOrHeliportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("icaoCode", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>("helipadIdentifier", new PadIdentifier()),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(PadShape.SPEC),
        new RecordField<>(new PadDimensions()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>("latitude", new Latitude()),
        new RecordField<>("longitude", new Longitude()),
        new RecordField<>("helipadSurfaceCode", LongestRunwaySurfaceCode.SPEC),
        new RecordField<>("helipadSurfaceType", SurfaceType.SPEC),
        new RecordField<>(new MaximumAllowableHelicopterWeight()),
        new RecordField<>(HelicopterPerformanceRequirement.SPEC),
        new RecordField<>("blank4", new BlankSpec(5)),
        new RecordField<>(new LandingThresholdElevation()),
        new RecordField<>("blank5", new BlankSpec(53)),
        new RecordField<>(new FileRecordNumber()),
        new RecordField<>(new Cycle())
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
    String sectionSubsection = arincRecord.substring(4, 5).concat(arincRecord.substring(12, 13));
    return sectionSubSections.contains(sectionSubsection);
  }

  private static final Set<String> sectionSubSections = Set.of("PH", "HH");
}
