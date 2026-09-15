package org.mitre.tdp.boogie.dafif.v81.spec;

import java.util.List;

import org.mitre.tdp.boogie.dafif.DafifRecordField;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.field.CommunicationName;
import org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency;
import org.mitre.tdp.boogie.dafif.v81.field.ControllingAuthority;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.EffectiveDate;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.Level;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.LowerAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.Sector;
import org.mitre.tdp.boogie.dafif.v81.field.SpecialUseAirspaceType;
import org.mitre.tdp.boogie.dafif.v81.field.SuasEffectiveTimes;
import org.mitre.tdp.boogie.dafif.v81.field.SuasIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.SuasWeather;
import org.mitre.tdp.boogie.dafif.v81.field.UpperAltitude;

/**
 * DAFIF 8.1 SUAS_PAR.TXT column layout.
 */
public final class DafifSuasParentSpec implements DafifRecordSpec {

  private final List<DafifRecordField<?>> recordFields = List.of(
      new DafifRecordField<>("suasIdentification", new SuasIdentification()),
      new DafifRecordField<>("sector", new Sector()),
      new DafifRecordField<>("specialUseAirspaceType", new SpecialUseAirspaceType()),
      new DafifRecordField<>("name", new Name()),
      new DafifRecordField<>("icaoCode", new IcaoCode()),
      new DafifRecordField<>("controllingAuthority", new ControllingAuthority()),
      new DafifRecordField<>("localHorizontalDatum", new LocalHorizontalDatum()),
      new DafifRecordField<>("geodeticDatum", new GeodeticDatum()),
      new DafifRecordField<>("communicationName", new CommunicationName()),
      new DafifRecordField<>("communicationsFrequency1", new CommunicationsFrequency()),
      new DafifRecordField<>("communicationsFrequency2", new CommunicationsFrequency()),
      new DafifRecordField<>("level", new Level()),
      new DafifRecordField<>("upperAltitude", new UpperAltitude()),
      new DafifRecordField<>("lowerAltitude", new LowerAltitude()),
      new DafifRecordField<>("suasEffectiveTimes", new SuasEffectiveTimes()),
      new DafifRecordField<>("suasWeather", new SuasWeather()),
      new DafifRecordField<>("cycleDate", new CycleDate()),
      new DafifRecordField<>("effectiveDate", new EffectiveDate())
  );

  @Override
  public List<DafifRecordField<?>> recordFields() {
    return recordFields;
  }

  @Override
  public DafifRecordType recordType() {
    return DafifRecordType.SUAS_PAR;
  }

  @Override
  public int expectedNumFields() {
    return 18;
  }
}
