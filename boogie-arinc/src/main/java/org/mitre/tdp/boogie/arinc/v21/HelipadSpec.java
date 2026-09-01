package org.mitre.tdp.boogie.arinc.v21;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v21.field.*;
import org.mitre.tdp.boogie.arinc.v21.field.PadDimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadIdentifier;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column13;

public final class HelipadSpec implements RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(
      column13('P', 'H'), column13('H', 'H'));

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
  public List<RecordDiscriminator> recordDiscriminators() {
    return DISCRIMINATORS;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    char section = arincRecord.charAt(4);
    char subsection = arincRecord.charAt(12);
    return (section == 'P' || section == 'H') && subsection == 'H';
  }
}
