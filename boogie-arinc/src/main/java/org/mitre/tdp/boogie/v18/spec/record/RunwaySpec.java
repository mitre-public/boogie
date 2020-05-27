package org.mitre.tdp.boogie.v18.spec.record;

import java.util.List;

import org.mitre.tdp.boogie.ArincField;
import org.mitre.tdp.boogie.RecordSpec;

public class RunwaySpec implements RecordSpec {
  @Override
  public int recordLength() {
    return 0;
  }

  @Override
  public List<ArincField<?>> recordFields() {
    return null;
  }

  @Override
  public boolean matchesRecord(String arincRecord) {
    return false;
  }
}
