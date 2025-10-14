package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Optional;

public final class ArincBaseInfo implements Serializable {
  private final SupplementalData supplementalData;

  private ArincBaseInfo(SupplementalData supplementalData) {
    this.supplementalData = supplementalData;
  }

  public static ArincBaseInfo from(SupplementalData supplementalData) {
    return new ArincBaseInfo(supplementalData);
  }

  public Optional<SupplementalData> supplementalData() {
    return Optional.ofNullable(supplementalData);
  }
}
