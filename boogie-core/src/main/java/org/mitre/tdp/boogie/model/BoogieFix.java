package org.mitre.tdp.boogie.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

import static java.util.Objects.requireNonNull;

/**
 * Immutable convenience implementation of the {@link Fix} interface.
 */
public final class BoogieFix implements Serializable, Fix {

  private final String fixIdentifier;
  private final String fixRegion;
  private final LatLong latLong;
  private final Double publishedVariation;
  private final double modeledVariation;
  private final Double elevation;

  private BoogieFix(Builder builder) {
    this.fixIdentifier = builder.fixIdentifier;
    this.fixRegion = builder.fixRegion;
    this.latLong = LatLong.of(builder.latitude, builder.longitude);
    this.publishedVariation = builder.publishedVariation;
    this.modeledVariation = builder.modeledVariation;
    this.elevation = builder.elevation;
  }

  @Override
  public String fixIdentifier() {
    return fixIdentifier;
  }

  public String fixRegion() {
    return fixRegion;
  }

  @Override
  public LatLong latLong() {
    return latLong;
  }

  @Override
  public Optional<MagneticVariation> magneticVariation() {
    return publishedVariation().or(() -> Optional.of(modeledVariation())).map(MagneticVariation::ofDegrees);
  }

  public Optional<Double> publishedVariation() {
    return Optional.ofNullable(publishedVariation);
  }

  public double modeledVariation() {
    return modeledVariation;
  }

  public Optional<Double> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Builder toBuilder() {
    return new Builder()
        .fixIdentifier(fixIdentifier())
        .fixRegion(fixRegion())
        .latitude(latitude())
        .longitude(longitude())
        .publishedVariation(publishedVariation().orElse(null))
        .modeledVariation(modeledVariation())
        .elevation(elevation().orElse(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieFix boogieFix = (BoogieFix) o;
    return Double.compare(boogieFix.modeledVariation, modeledVariation) == 0 &&
        Objects.equals(fixIdentifier, boogieFix.fixIdentifier) &&
        Objects.equals(fixRegion, boogieFix.fixRegion) &&
        Objects.equals(latLong, boogieFix.latLong) &&
        Objects.equals(publishedVariation, boogieFix.publishedVariation) &&
        Objects.equals(elevation, boogieFix.elevation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fixIdentifier, fixRegion, latLong, publishedVariation, modeledVariation, elevation);
  }

  @Override
  public String toString() {
    return "BoogieFix{" +
        "fixIdentifier='" + fixIdentifier + '\'' +
        ", fixRegion='" + fixRegion + '\'' +
        ", latLong=" + latLong +
        ", publishedVariation=" + publishedVariation +
        ", modeledVariation=" + modeledVariation +
        ", elevation=" + elevation +
        '}';
  }

  public static final class Builder {
    private String fixIdentifier;
    private String fixRegion;
    private double latitude;
    private double longitude;
    private Double publishedVariation;
    private double modeledVariation;
    private Double elevation;

    public Builder fixIdentifier(String fixIdentifier) {
      this.fixIdentifier = requireNonNull(fixIdentifier);
      return this;
    }

    public Builder fixRegion(String fixRegion) {
      this.fixRegion = requireNonNull(fixRegion);
      return this;
    }

    public Builder latitude(double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(double longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder publishedVariation(Double publishedVariation) {
      this.publishedVariation = publishedVariation;
      return this;
    }

    public Builder modeledVariation(double modeledVariation) {
      this.modeledVariation = modeledVariation;
      return this;
    }

    public Builder elevation(Double elevation) {
      this.elevation = elevation;
      return this;
    }

    public BoogieFix build() {
      return new BoogieFix(this);
    }
  }
}
