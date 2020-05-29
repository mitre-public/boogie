package org.mitre.tdp.boogie.v18;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public interface ArincRunway extends Runway {

  ArincRecord arincRecord();

  default RecordType recordType() {
    return arincRecord().getRequiredField("recordType");
  }

  default CustomerAreaCode customerAreaCode() {
    return arincRecord().getRequiredField("customerAreaCode");
  }

  default SectionCode sectionCode(){
    return arincRecord().getRequiredField("sectionCode");
  }
}
