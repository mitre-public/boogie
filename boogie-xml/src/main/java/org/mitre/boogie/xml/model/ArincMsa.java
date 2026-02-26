package org.mitre.boogie.xml.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincMsaSector;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;

public final class ArincMsa {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final String centerFix;
  private final String centerFixRef;
  private final String icaoCode;
  /**
   * See {@link MagneticTrueIndicator}
   */
  private final String magneticTrueIndicator;
  private final String multipleCode;
  private final List<ArincMsaSector> sectors;

  private ArincMsa(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.centerFix = builder.centerFix;
    this.centerFixRef = builder.centerFixRef;
    this.icaoCode = builder.icaoCode;
    this.magneticTrueIndicator = builder.magneticTrueIndicator;
    this.multipleCode = builder.multipleCode;
    this.sectors = builder.sectors;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public ArincRecordInfo recordInfo() {
    return recordInfo;
  }

  public Optional<String> centerFix() {
    return Optional.ofNullable(centerFix);
  }

  public Optional<String> centerFixRef() {
    return Optional.ofNullable(centerFixRef);
  }

  public Optional<String> icaoCode() {
    return Optional.ofNullable(icaoCode);
  }

  public Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return Optional.ofNullable(magneticTrueIndicator).filter(MagneticTrueIndicator.VALID::contains).map(MagneticTrueIndicator::valueOf);
  }

  public Optional<String> multipleCode() {
    return Optional.ofNullable(multipleCode);
  }

  public List<ArincMsaSector> sectors() {
    return sectors == null ? List.of() : sectors;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincMsa arincMsa = (ArincMsa) o;
    return Objects.equals(baseInfo, arincMsa.baseInfo) && Objects.equals(recordInfo, arincMsa.recordInfo) && Objects.equals(centerFix, arincMsa.centerFix) && Objects.equals(centerFixRef, arincMsa.centerFixRef) && Objects.equals(icaoCode, arincMsa.icaoCode) && Objects.equals(magneticTrueIndicator, arincMsa.magneticTrueIndicator) && Objects.equals(multipleCode, arincMsa.multipleCode) && Objects.equals(sectors, arincMsa.sectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, centerFix, centerFixRef, icaoCode, magneticTrueIndicator, multipleCode, sectors);
  }

  @Override
  public String toString() {
    return "ArincMsa{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", centerFix='" + centerFix + '\'' +
        ", centerFixRef='" + centerFixRef + '\'' +
        ", icaoCode='" + icaoCode + '\'' +
        ", magneticTrueIndicator='" + magneticTrueIndicator + '\'' +
        ", multipleCode='" + multipleCode + '\'' +
        ", sectors=" + sectors +
        '}';
  }

  public static class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private String centerFix;
    private String centerFixRef;
    private String icaoCode;
    private String magneticTrueIndicator;
    private String multipleCode;
    private List<ArincMsaSector> sectors;

    private Builder() {
    }

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder recordInfo(ArincRecordInfo recordInfo) {
      this.recordInfo = recordInfo;
      return this;
    }

    public Builder centerFix(String centerFix) {
      this.centerFix = centerFix;
      return this;
    }

    public Builder centerFixRef(String centerFixRef) {
      this.centerFixRef = centerFixRef;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder magneticTrueIndicator(String magneticTrueIndicator) {
      this.magneticTrueIndicator = magneticTrueIndicator;
      return this;
    }

    public Builder multipleCode(String multipleCode) {
      this.multipleCode = multipleCode;
      return this;
    }

    public Builder sectors(List<ArincMsaSector> sectors) {
      this.sectors = sectors;
      return this;
    }

    public ArincMsa build() {
      return new ArincMsa(this);
    }
  }
}
