package org.mitre.tdp.boogie.arinc.v18.spec;

import java.util.List;

import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.IataDesignator;
import org.mitre.tdp.boogie.arinc.RecordField;
import org.mitre.tdp.boogie.arinc.RecordSpec;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportElevation;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DatumCode;
import org.mitre.tdp.boogie.arinc.v18.field.DaylightTimeIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.IfrCapability;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunway;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticVariation;
import org.mitre.tdp.boogie.arinc.v18.field.NameField;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecommendedNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TransitionAltitude;

import com.google.common.collect.ImmutableList;

/**
 * Record specification for ARINC airport records V18.
 */
public final class AirportSpec implements RecordSpec {

  private final List<RecordField<?>> recordFields;

  public AirportSpec() {
    this.recordFields = ImmutableList.of(
        new RecordField<>(RecordType.SPEC),
        new RecordField<>(CustomerAreaCode.SPEC),
        new RecordField<>(SectionCode.SPEC),
        new RecordField<>("blank1", new BlankSpec(1)),
        new RecordField<>("airportIdentifier", new AirportHeliportIdentifier()),
        new RecordField<>("airportIcaoRegion", new IcaoRegion()),
        new RecordField<>(new SubSectionCode()),
        new RecordField<>(new IataDesignator()),
        new RecordField<>("reserved1", new BlankSpec(2)),
        new RecordField<>("blank2", new BlankSpec(3)),
        new RecordField<>(new ContinuationRecordNumber()),
        new RecordField<>(new SpeedLimitAltitude()),
        new RecordField<>(new LongestRunway()),
        new RecordField<>(new IfrCapability()),
        new RecordField<>(LongestRunwaySurfaceCode.SPEC),
        new RecordField<>(new Latitude()),
        new RecordField<>(new Longitude()),
        new RecordField<>(new MagneticVariation()),
        new RecordField<>("airportElevation", new AirportHeliportElevation()),
        new RecordField<>(new SpeedLimit()),
        new RecordField<>(new RecommendedNavaid()),
        new RecordField<>("recommendedNavaidIcaoRegion", new IcaoRegion()),
        new RecordField<>("transitionAltitude", new TransitionAltitude()),
        new RecordField<>("transitionLevel", new TransitionAltitude()),
        new RecordField<>(PublicMilitaryIndicator.SPEC),
        new RecordField<>("timezone", new BlankSpec(3)), // 5.178
        new RecordField<>(new DaylightTimeIndicator()),
        new RecordField<>(MagneticTrueIndicator.SPEC),
        new RecordField<>(new DatumCode()),
        new RecordField<>("reserved2", new BlankSpec(4)),
        new RecordField<>("airportFullName", new NameField()),
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
    String subsection = arincRecord.substring(12, 13);
    return arincRecord.substring(4, 5).concat(subsection).equals("PA");
  }
}
