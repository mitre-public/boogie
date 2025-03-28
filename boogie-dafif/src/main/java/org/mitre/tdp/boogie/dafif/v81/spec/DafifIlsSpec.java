package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.Collocation;
import org.mitre.tdp.boogie.dafif.v81.field.ComponentType;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IlsBearingCourse;
import org.mitre.tdp.boogie.dafif.v81.field.IlsDmeBias;
import org.mitre.tdp.boogie.dafif.v81.field.IlsGlideSlopeAngle;
import org.mitre.tdp.boogie.dafif.v81.field.IlsMlsCategory;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidElevation;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IlsSlaveVariation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.LocalizerOrGlideSlopeLocation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalizerWidth;
import org.mitre.tdp.boogie.dafif.v81.field.LocatorOrMarkerLocation;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.MlsDmePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidChannel;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidFrequency;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.ThresholdCrossingHeight;

import java.util.List;

public final class DafifIlsSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifIlsSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>(new RunwayIdentifier()),
        new DafifRecordField<>(new ComponentType()),
        new DafifRecordField<>(new Collocation()),
        new DafifRecordField<>(new Name()),
        new DafifRecordField<>(new NavaidFrequency()),
        new DafifRecordField<>(new NavaidChannel()),
        new DafifRecordField<>(new IlsGlideSlopeAngle()),
        new DafifRecordField<>(new LocalizerOrGlideSlopeLocation()),
        new DafifRecordField<>(new LocatorOrMarkerLocation()),
        new DafifRecordField<>(new IlsNavaidElevation()),
        new DafifRecordField<>(new LocalHorizontalDatum()),
        new DafifRecordField<>(new GeodeticDatum()),
        new DafifRecordField<>(new IlsMlsCategory()),
        new DafifRecordField<>(new GeodeticLatitude()),
        new DafifRecordField<>(new DegreesLatitude()),
        new DafifRecordField<>(new GeodeticLongitude()),
        new DafifRecordField<>(new DegreesLongitude()),
        new DafifRecordField<>(new IlsNavaidIdentifier()),
        new DafifRecordField<>(new NavaidType()),
        new DafifRecordField<>(new CountryCode()),
        new DafifRecordField<>(new NavaidKeyCode()),
        new DafifRecordField<>(new MagneticVariation()),
        new DafifRecordField<>(new IlsSlaveVariation()),
        new DafifRecordField<>(new IlsBearingCourse()),
        new DafifRecordField<>(new LocalizerWidth()),
        new DafifRecordField<>(new ThresholdCrossingHeight()),
        new DafifRecordField<>(new IlsDmeBias()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new MlsDmePrecision()),
        new DafifRecordField<>(new CoordinatePrecision())
    );
  }


  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.ILS;
  }

  @Override
  public int expectedNumFields() {
    return 31;
  }
}
