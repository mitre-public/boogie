package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincPortInfo;

public final class ArincHeliport {
  private final ArincPortInfo portInfo;
  private final String heliportType;

  private ArincHeliport(Builder builder) {
    this.portInfo = builder.portInfo;
    this.heliportType = builder.heliportType;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .portInfo(portInfo)
        .heliportType(heliportType);
  }

  public ArincPortInfo portInfo() {
    return portInfo;
  }

  public Optional<String> heliportType() {
    return Optional.ofNullable(heliportType);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincHeliport that = (ArincHeliport) o;
    return Objects.equals(portInfo, that.portInfo) && Objects.equals(heliportType, that.heliportType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portInfo, heliportType);
  }

  @Override
  public String toString() {
    return "ArincHeliport{" +
        "portInfo=" + portInfo +
        ", heliportType='" + heliportType + '\'' +
        '}';
  }

  public static class Builder {
    private ArincPortInfo portInfo;
    private String heliportType;

    private Builder() {
    }

    public Builder portInfo(ArincPortInfo portInfo) {
      this.portInfo = portInfo;
      return this;
    }

    public Builder heliportType(String heliportType) {
      this.heliportType = heliportType;
      return this;
    }

    public ArincHeliport build() {
      return new ArincHeliport(this);
    }
  }
}
