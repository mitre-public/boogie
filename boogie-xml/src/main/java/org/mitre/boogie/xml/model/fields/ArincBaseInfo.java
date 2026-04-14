package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincBaseInfo that = (ArincBaseInfo) o;
    return Objects.equals(supplementalData, that.supplementalData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supplementalData);
  }

  @Override
  public String toString() {
    return "ArincBaseInfo{" +
        "supplementalData=" + supplementalData +
        '}';
  }
}
