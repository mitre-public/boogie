package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

/**
 * Specification for a NDB navaid record from ARINC V18.
 */
public final class NdbNavaidSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(
      primaryColumn6('D', 'B', 21), primaryColumn6('P', 'N', 21));

  private final List<RecordField<?>> recordFields;

  public NdbNavaidSpec() {
    super(DISCRIMINATORS);
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

}
