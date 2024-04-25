package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.AirportType;
import org.mitre.tdp.boogie.dafif.v81.field.Beacon;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.AirportHeliportElevation;
import org.mitre.tdp.boogie.dafif.v81.field.FaaHostCountryIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.AirportHeliportName;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariationOfRecord;
import org.mitre.tdp.boogie.dafif.v81.field.PrimaryOperatingAgency;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryAirport;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryFaaHost;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryIcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryName;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryOperatingAgency;
import org.mitre.tdp.boogie.dafif.v81.field.ShorelineHydrography;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.TerrainImpacted;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;

import java.util.List;

public final class DafifAirportSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifAirportSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>("name", new AirportHeliportName()),
        new DafifRecordField<>(new StateProvinceCode()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>(new FaaHostCountryIdentifier()),
        new DafifRecordField<>(new LocalHorizontalDatum()),
        new DafifRecordField<>(new GeodeticDatum()),
        new DafifRecordField<>(new GeodeticLatitude()),
        new DafifRecordField<>(new DegreesLatitude()),
        new DafifRecordField<>(new GeodeticLongitude()),
        new DafifRecordField<>(new DegreesLongitude()),
        new DafifRecordField<>("elevation", new AirportHeliportElevation()),
        new DafifRecordField<>(new AirportType()),
        new DafifRecordField<>(new MagneticVariation()),
        new DafifRecordField<>(new WAC()),
        new DafifRecordField<>(new Beacon()),
        new DafifRecordField<>(new SecondaryAirport()),
        new DafifRecordField<>(new PrimaryOperatingAgency()),
        new DafifRecordField<>(new SecondaryName()),
        new DafifRecordField<>(new SecondaryIcaoCode()),
        new DafifRecordField<>(new SecondaryFaaHost()),
        new DafifRecordField<>(new SecondaryOperatingAgency()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new TerrainImpacted()),
        new DafifRecordField<>(new ShorelineHydrography()),
        new DafifRecordField<>(new CoordinatePrecision()),
        new DafifRecordField<>(new MagneticVariationOfRecord())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.ARPT;
  }

  @Override
  public int expectedNumFields() {
    return 27;
  }
}
