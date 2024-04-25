package org.mitre.tdp.boogie.dafif.v81.spec;

import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalProcedureType;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.EmergencySafeAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.JulianDate;
import org.mitre.tdp.boogie.dafif.v81.field.AmendmentNumber;
import org.mitre.tdp.boogie.dafif.v81.field.OfficeOfPrimaryResponsibility;
import org.mitre.tdp.boogie.dafif.v81.field.HostCountryAuthority;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.AlternateTakeoffMinimums;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionLevel;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureDesignCriteria;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureAmendmentDate;
import org.mitre.tdp.boogie.dafif.v81.field.ApproachRouteQualifier1;
import org.mitre.tdp.boogie.dafif.v81.field.ApproachRouteQualifier2;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService1;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService2;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService3;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureDesignMagvar;

import java.util.List;

public class DafifTerminalParentSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields;

  public DafifTerminalParentSpec() {
    this.recordFields = ImmutableList.of(
        new DafifRecordField<>(new AirportIdentification()),
        new DafifRecordField<>(new TerminalProcedureType()),
        new DafifRecordField<>(new TerminalIdentifier()),
        new DafifRecordField<>(new IcaoCode()),
        new DafifRecordField<>(new EmergencySafeAltitude()),
        new DafifRecordField<>(new JulianDate()),
        new DafifRecordField<>(new AmendmentNumber()),
        new DafifRecordField<>(new OfficeOfPrimaryResponsibility()),
        new DafifRecordField<>(new HostCountryAuthority()),
        new DafifRecordField<>(new CycleDate()),
        new DafifRecordField<>(new AlternateTakeoffMinimums()),
        new DafifRecordField<>(new TransitionAltitude()),
        new DafifRecordField<>(new TransitionLevel()),
        new DafifRecordField<>(new ProcedureDesignCriteria()),
        new DafifRecordField<>(new ProcedureAmendmentDate()),
        new DafifRecordField<>(new ApproachRouteQualifier1()),
        new DafifRecordField<>(new ApproachRouteQualifier2()),
        new DafifRecordField<>("levelOfService1", new LevelOfService()),
        new DafifRecordField<>("levelOfServiceName1", new LevelOfService1()),
        new DafifRecordField<>("levelOfService2", new LevelOfService()),
        new DafifRecordField<>("levelOfServiceName2", new LevelOfService2()),
        new DafifRecordField<>("levelOfService3", new LevelOfService()),
        new DafifRecordField<>("levelOfServiceName3", new LevelOfService3()),
        new DafifRecordField<>(new ProcedureDesignMagvar())
    );
  }

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.TRM_PAR;
  }

  @Override
  public int expectedNumFields() {
    return 24;
  }
}
