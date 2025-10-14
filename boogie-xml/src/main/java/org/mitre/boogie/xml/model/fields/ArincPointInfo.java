package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincPointInfo implements Serializable, HasPosition {
  private final String datumCode;
  private final String icaoCode;
  private final String identifier;
  private final LatLong latLong;
  private final MagneticVariation magneticVariation;
  private final String name;
  private final String fir;
  private final String uir;
  private final String referenceId;

  private ArincPointInfo(Builder builder) {
    this.datumCode = builder.datumCode;
    this.icaoCode = builder.icaoCode;
    this.identifier = builder.identifier;
    this.latLong = builder.latLong;
    this.magneticVariation = builder.magneticVariation;
    this.name = builder.name;
    this.uir = builder.uir;
    this.fir = builder.fir;
    this.referenceId = builder.referenceId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String icaoCode() {
    return icaoCode;
  }

  public String identifier() {
    return identifier;
  }

  public String referenceId() {
    return referenceId;
  }

  @Override
  public LatLong latLong() {
    return latLong;
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<MagneticVariation> magneticVariation() {
    return Optional.ofNullable(magneticVariation);
  }

  public Optional<String> name() {
    return Optional.ofNullable(name);
  }

  public Optional<String> fir() {
    return Optional.ofNullable(fir);
  }

  public Optional<String> uir() {
    return Optional.ofNullable(uir);
  }

  public static class Builder {
    private String datumCode;
    private String icaoCode;
    private String identifier;
    private LatLong latLong;
    private MagneticVariation magneticVariation;
    private String name;
    private String uir;
    private String fir;
    private String referenceId;
    private Builder() {}

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder latLong(LatLong latLong) {
      this.latLong = latLong;
      return this;
    }

    public Builder magneticVariation(MagneticVariation magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder uir(String uir) {
      this.uir = uir;
      return this;
    }

    public Builder fir(String fir) {
      this.fir = fir;
      return this;
    }

    public Builder referenceId(String referenceId) {
      this.referenceId = referenceId;
      return this;
    }

    public ArincPointInfo build() {
      return new ArincPointInfo(this);
    }
  }
}
