package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column13;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column6;

/**
 * Specification for Terminal/Enroute waypoint records in ARINC V18.
 */
public final class WaypointSpec implements RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(
      column6('E', 'A'), column13('P', 'C'), column13('H', 'C'));

  private final List<RecordField<?>> recordFields;

  public WaypointSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("enrouteSubSectionCode", new SubSectionCode()),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>("terminalSubSectionCode", new SubSectionCode()),
        new RecordField<>("waypointIdentifier", new FixIdentifier()),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("waypointIcaoRegion", new IcaoRegion()),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>("blank2", new BlankSpec(4)),
        new RecordField<>(new WaypointType()),
        new RecordField<>(new WaypointUsage()),
        new RecordField<>("blank3", new BlankSpec(1)),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>("blank4", new BlankSpec(23)),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("waypointElevationXX", new BlankSpec(5)),
        new RecordField<>(new DatumCode()),
        new RecordField<>("blank5", new BlankSpec(8)),
        new RecordField<>(new NameFormat()),
        new RecordField<>(new WaypointNameDescription()),
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
  public List<RecordDiscriminator> recordDiscriminators() {
    return DISCRIMINATORS;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return (arincRecord.regionMatches(4, "EA", 0, 2) || (arincRecord.charAt(4) == 'P' && arincRecord.charAt(12) == 'C') || (arincRecord.charAt(4) == 'H' && arincRecord.charAt(12) == 'C'))
        && PrimaryRecord.INSTANCE.test(arincRecord.charAt(21));
  }
}
