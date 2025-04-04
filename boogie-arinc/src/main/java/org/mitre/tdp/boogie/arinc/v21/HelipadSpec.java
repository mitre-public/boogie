package org.mitre.tdp.boogie.arinc.v21;

import java.util.List;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v21.field.PadDimensions;
import org.mitre.tdp.boogie.arinc.v21.field.*;

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
    return sectionSubSections.contains(sectionSubsection) && PrimaryRecord.INSTANCE.test(arincRecord.substring(21, 22));
  }

  private static final Set<String> sectionSubSections = Set.of("PH", "HH");
}
