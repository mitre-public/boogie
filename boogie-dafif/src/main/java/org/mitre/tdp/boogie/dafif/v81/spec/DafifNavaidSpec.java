package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.v81.field.AssociatedIcaoFaaHostCtryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeDegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeDegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeElevation;
import org.mitre.tdp.boogie.dafif.v81.field.DmeGeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeGeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.FrequencyProtection;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoRegion;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidElevation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidChannel;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidFrequencyNav;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidPower;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidRadioClassCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidRange;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidSlavedVariation;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidStatus;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidUsageCode;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;

import java.util.List;

public class DafifNavaidSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifNavaidSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new NavaidIdentifier()),
        new DafifRecordField<>(new NavaidType()),
        new DafifRecordField<>(new CountryCode()),
        new DafifRecordField<>(new NavaidKeyCode()),
        new DafifRecordField<>(new StateProvinceCode()),
        new DafifRecordField<>(new Name()),
        new DafifRecordField<>(new IcaoRegion()),
        new DafifRecordField<>(new WAC()),
        new DafifRecordField<>(new NavaidFrequencyNav()),
        new DafifRecordField<>(new NavaidUsageCode()),
        new DafifRecordField<>(new NavaidChannel()),
        new DafifRecordField<>(new NavaidRadioClassCode()),
        new DafifRecordField<>(new FrequencyProtection()),
        new DafifRecordField<>(new NavaidPower()),
        new DafifRecordField<>(new NavaidRange()),
        new DafifRecordField<>(new LocalHorizontalDatum()),
        new DafifRecordField<>(new GeodeticDatum()),
        new DafifRecordField<>(new GeodeticLatitude()),
        new DafifRecordField<>(new DegreesLatitude()),
        new DafifRecordField<>(new GeodeticLongitude()),
        new DafifRecordField<>(new DegreesLongitude()),
        new DafifRecordField<>(new NavaidSlavedVariation()),
        new DafifRecordField<>(new MagneticVariation()),
        new DafifRecordField<>(new IlsNavaidElevation()),
        new DafifRecordField<>(new DmeGeodeticLatitude()),
        new DafifRecordField<>(new DmeDegreesLatitude()),
        new DafifRecordField<>(new DmeGeodeticLongitude()),
        new DafifRecordField<>(new DmeDegreesLongitude()),
        new DafifRecordField<>(new DmeElevation()),
        new DafifRecordField<>(new AssociatedIcaoFaaHostCtryCode()),
        new DafifRecordField<>(new NavaidStatus()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new CoordinatePrecision())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.NAV;
  }

  @Override
  public int expectedNumFields() {
    return 33;
  }
}
