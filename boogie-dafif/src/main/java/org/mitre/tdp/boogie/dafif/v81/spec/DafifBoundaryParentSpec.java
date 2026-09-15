package org.mitre.tdp.boogie.dafif.v81.spec;

import java.util.List;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.AirspaceClass;
import org.mitre.tdp.boogie.dafif.v81.field.BoundaryIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.BoundaryType;
import org.mitre.tdp.boogie.dafif.v81.field.ClassExceptionFlag;
import org.mitre.tdp.boogie.dafif.v81.field.ClassExceptionRemarks;
import org.mitre.tdp.boogie.dafif.v81.field.CommunicationName;
import org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency;
import org.mitre.tdp.boogie.dafif.v81.field.ControllingAuthority;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.Level;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.LowerAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance;
import org.mitre.tdp.boogie.dafif.v81.field.UpperAltitude;

/**
 * DAFIF 8.1 BDRY_PAR.TXT column layout.
 */
public final class DafifBoundaryParentSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields = List.of(
      new DafifRecordField<>("boundaryIdentification", new BoundaryIdentification()),
      new DafifRecordField<>("boundaryType", new BoundaryType()),
      new DafifRecordField<>("name", new Name()),
      new DafifRecordField<>("icaoCode", new IcaoCode()),
      new DafifRecordField<>("controllingAuthority", new ControllingAuthority()),
      new DafifRecordField<>("localHorizontalDatum", new LocalHorizontalDatum()),
      new DafifRecordField<>("geodeticDatum", new GeodeticDatum()),
      new DafifRecordField<>("communicationName", new CommunicationName()),
      new DafifRecordField<>("communicationsFrequency1", new CommunicationsFrequency()),
      new DafifRecordField<>("communicationsFrequency2", new CommunicationsFrequency()),
      new DafifRecordField<>("airspaceClass", new AirspaceClass()),
      new DafifRecordField<>("classExceptionFlag", new ClassExceptionFlag()),
      new DafifRecordField<>("classExceptionRemarks", new ClassExceptionRemarks()),
      new DafifRecordField<>("level", new Level()),
      new DafifRecordField<>("upperAltitude", new UpperAltitude()),
      new DafifRecordField<>("lowerAltitude", new LowerAltitude()),
      new DafifRecordField<>("requiredNavPerformance", new RequiredNavPerformance()),
      new DafifRecordField<>("cycleDate", new CycleDate()),
      new DafifRecordField<>("upperRvsm", new UpperAltitude()),
      new DafifRecordField<>("lowerRvsm", new LowerAltitude())
  );

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.BDRY_PAR;
  }

  @Override
  public int expectedNumFields() {
    return 20;
  }
}
