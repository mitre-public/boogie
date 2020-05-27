package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.List;

public enum ArincVersion implements ArincSpec {
  /**
   * Implementation of an {@link ArincSpec} for use parsing data of the ARINC V18 specification.
   */
  V18();

  private final List<RecordSpec> specs;

  ArincVersion(RecordSpec... specs) {
    this(Arrays.asList(specs));
  }

  ArincVersion(List<RecordSpec> specs) {
    this.specs = specs;
  }

  @Override
  public List<RecordSpec> recordSpecs() {
    return specs;
  }
}
