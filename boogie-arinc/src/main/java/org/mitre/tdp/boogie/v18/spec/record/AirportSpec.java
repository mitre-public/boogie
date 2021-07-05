package org.mitre.tdp.boogie.v18.spec.record;

import static org.mitre.tdp.boogie.ArincField.newField;

import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportElevation;
import org.mitre.tdp.boogie.v18.spec.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;
import org.mitre.tdp.boogie.v18.spec.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Cycle;
import org.mitre.tdp.boogie.v18.spec.field.DatumCode;
import org.mitre.tdp.boogie.v18.spec.field.DaylightTimeIndicator;
import org.mitre.tdp.boogie.v18.spec.field.FileRecordNumber;
import org.mitre.tdp.boogie.v18.spec.field.IataDesignator;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;
import org.mitre.tdp.boogie.v18.spec.field.IfrCapability;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;
import org.mitre.tdp.boogie.v18.spec.field.LongestRunway;
import org.mitre.tdp.boogie.v18.spec.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;
import org.mitre.tdp.boogie.v18.spec.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.v18.spec.field.MagneticVariation;
import org.mitre.tdp.boogie.v18.spec.field.NameField;
import org.mitre.tdp.boogie.v18.spec.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.v18.spec.field.RecommendedNavaid;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.SpeedLimit;
import org.mitre.tdp.boogie.v18.spec.field.SpeedLimitAltitude;
import org.mitre.tdp.boogie.v18.spec.field.SubSectionCode;
import org.mitre.tdp.boogie.v18.spec.field.TransitionAltitude;

import com.google.common.collect.ImmutableList;

/**
 * Record specification for ARINC airport records.
 */
public final class AirportSpec implements RecordSpec {

  private final List<ArincField<?>> recordFields;

  public AirportSpec() {
    this.recordFields = ImmutableList.of(
        newField(RecordType.SPEC),
        newField(CustomerAreaCode.SPEC),
        newField(SectionCode.SPEC),
        newField("blank1", new BlankSpec(1)),
        newField("airportIdentifier", new AirportHeliportIdentifier()),
        newField(new IcaoRegion()),
        newField(new SubSectionCode()),
        newField(new IataDesignator()),
        newField("reserved1", new BlankSpec(2)),
        newField("blank2", new BlankSpec(3)),
        newField(new ContinuationRecordNumber()),
        newField(new SpeedLimitAltitude()),
        newField(new LongestRunway()),
        newField(new IfrCapability()),
        newField(LongestRunwaySurfaceCode.SPEC),
        newField(new Latitude()),
        newField(new Longitude()),
        newField(new MagneticVariation()),
        newField("airportElevation", new AirportHeliportElevation()),
        newField(new SpeedLimit()),
        newField(new RecommendedNavaid()),
        newField("recommendedNavaidIcaoRegion", new IcaoRegion()),
        newField("transitionAltitude", new TransitionAltitude()),
        newField("transitionLevel", new TransitionAltitude()),
        newField(PublicMilitaryIndicator.SPEC),
        newField("timezone", new BlankSpec(3)), // 5.178
        newField(new DaylightTimeIndicator()),
        newField(MagneticTrueIndicator.SPEC),
        newField(new DatumCode()),
        newField("reserved2", new BlankSpec(4)),
        newField("airportFullName", new NameField()),
        newField(new FileRecordNumber()),
        newField(new Cycle()));
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
    String subsection = arincRecord.substring(12, 13);
    return arincRecord.substring(4, 5).concat(subsection).equals("PA");
  }
}
