package org.mitre.tdp.boogie.arinc.v18;

import com.google.common.collect.ImmutableList;
import org.mitre.tdp.boogie.arinc.RecordDiscriminator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.List;

import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn13;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;

/**
 * Specification for Terminal/Enroute waypoint records in ARINC V18.
 */
public final class WaypointSpec extends RecordSpec {
  private static final List<RecordDiscriminator> DISCRIMINATORS = List.of(
      primaryColumn6('E', 'A', 21), primaryColumn13('P', 'C', 21), primaryColumn13('H', 'C', 21));

  private final List<RecordField<?>> recordFields;

  public WaypointSpec() {
    super(DISCRIMINATORS);
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

}
