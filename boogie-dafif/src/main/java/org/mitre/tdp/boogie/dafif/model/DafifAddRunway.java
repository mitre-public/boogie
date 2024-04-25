package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.OverrunDistance;
import org.mitre.tdp.boogie.dafif.v81.field.OverrunSurface;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;

import java.util.Objects;

public final class DafifAddRunway {
  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;
  /**
   * {@link RunwayIdentifier}
   */
  private final String highEndRunwayIdentifier;
  /**
   * {@link RunwayIdentifier}
   */
  private final String lowEndRunwayIdentifier;
  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;
  /**
   * {@link GeodeticLatitude}
   */
  private final String highEndDisplacedThresholdGeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double highEndDisplacedThresholdDegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String highEndDisplacedThresholdGeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double highEndDisplacedThresholdDegreesLongitude;
  /**
   * {@link OverrunDistance}
   */
  private final Integer highEndOverrunDistance;
  /**
   * {@link OverrunSurface}
   */
  private final String highEndOverrunSurface;
  /**
   * {@link GeodeticLatitude}
   */
  private final String highEndOverrunGeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double highEndOverrunDegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String highEndOverrunGeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double highEndOverrunDegreesLongitude;
  /**
   * {@link GeodeticLatitude}
   */
  private final String lowEndDisplacedThresholdGeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double lowEndDisplacedThresholdDegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String lowEndDisplacedThresholdGeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double lowEndDisplacedThresholdDegreesLongitude;
  /**
   * {@link OverrunDistance}
   */
  private final Integer lowEndOverrunDistance;
  /**
   * {@link OverrunSurface}
   */
  private final String lowEndOverrunSurface;
  /**
   * {@link GeodeticLatitude}
   */
  private final String lowEndOverrunGeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double lowEndOverrunDegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String lowEndOverrunGeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double lowEndOverrunDegreesLongitude;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;

  private DafifAddRunway(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.highEndRunwayIdentifier = builder.highEndRunwayIdentifier;
    this.lowEndRunwayIdentifier = builder.lowEndRunwayIdentifier;
    this.icaoCode = builder.icaoCode;
    this.highEndDisplacedThresholdGeodeticLatitude = builder.highEndDisplacedThresholdGeodeticLatitude;
    this.highEndDisplacedThresholdDegreesLatitude = builder.highEndDisplacedThresholdDegreesLatitude;
    this.highEndDisplacedThresholdGeodeticLongitude = builder.highEndDisplacedThresholdGeodeticLongitude;
    this.highEndDisplacedThresholdDegreesLongitude = builder.highEndDisplacedThresholdDegreesLongitude;
    this.highEndOverrunDistance = builder.highEndOverrunDistance;
    this.highEndOverrunSurface = builder.highEndOverrunSurface;
    this.highEndOverrunGeodeticLatitude = builder.highEndOverrunGeodeticLatitude;
    this.highEndOverrunDegreesLatitude = builder.highEndOverrunDegreesLatitude;
    this.highEndOverrunGeodeticLongitude = builder.highEndOverrunGeodeticLongitude;
    this.highEndOverrunDegreesLongitude = builder.highEndOverrunDegreesLongitude;
    this.lowEndDisplacedThresholdGeodeticLatitude = builder.lowEndDisplacedThresholdGeodeticLatitude;
    this.lowEndDisplacedThresholdDegreesLatitude = builder.lowEndDisplacedThresholdDegreesLatitude;
    this.lowEndDisplacedThresholdGeodeticLongitude = builder.lowEndDisplacedThresholdGeodeticLongitude;
    this.lowEndDisplacedThresholdDegreesLongitude = builder.lowEndDisplacedThresholdDegreesLongitude;
    this.lowEndOverrunDistance = builder.lowEndOverrunDistance;
    this.lowEndOverrunSurface = builder.lowEndOverrunSurface;
    this.lowEndOverrunGeodeticLatitude = builder.lowEndOverrunGeodeticLatitude;
    this.lowEndOverrunDegreesLatitude = builder.lowEndOverrunDegreesLatitude;
    this.lowEndOverrunGeodeticLongitude = builder.lowEndOverrunGeodeticLongitude;
    this.lowEndOverrunDegreesLongitude = builder.lowEndOverrunDegreesLongitude;
    this.cycleDate = builder.cycleDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifAddRunway that = (DafifAddRunway) o;
    return Objects.equals(airportIdentification, that.airportIdentification) && Objects.equals(highEndRunwayIdentifier, that.highEndRunwayIdentifier) && Objects.equals(lowEndRunwayIdentifier, that.lowEndRunwayIdentifier) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(highEndDisplacedThresholdGeodeticLatitude, that.highEndDisplacedThresholdGeodeticLatitude) && Objects.equals(highEndDisplacedThresholdDegreesLatitude, that.highEndDisplacedThresholdDegreesLatitude) && Objects.equals(highEndDisplacedThresholdGeodeticLongitude, that.highEndDisplacedThresholdGeodeticLongitude) && Objects.equals(highEndDisplacedThresholdDegreesLongitude, that.highEndDisplacedThresholdDegreesLongitude) && Objects.equals(highEndOverrunDistance, that.highEndOverrunDistance) && Objects.equals(highEndOverrunSurface, that.highEndOverrunSurface) && Objects.equals(highEndOverrunGeodeticLatitude, that.highEndOverrunGeodeticLatitude) && Objects.equals(highEndOverrunDegreesLatitude, that.highEndOverrunDegreesLatitude) && Objects.equals(highEndOverrunGeodeticLongitude, that.highEndOverrunGeodeticLongitude) && Objects.equals(highEndOverrunDegreesLongitude, that.highEndOverrunDegreesLongitude) && Objects.equals(lowEndDisplacedThresholdGeodeticLatitude, that.lowEndDisplacedThresholdGeodeticLatitude) && Objects.equals(lowEndDisplacedThresholdDegreesLatitude, that.lowEndDisplacedThresholdDegreesLatitude) && Objects.equals(lowEndDisplacedThresholdGeodeticLongitude, that.lowEndDisplacedThresholdGeodeticLongitude) && Objects.equals(lowEndDisplacedThresholdDegreesLongitude, that.lowEndDisplacedThresholdDegreesLongitude) && Objects.equals(lowEndOverrunDistance, that.lowEndOverrunDistance) && Objects.equals(lowEndOverrunSurface, that.lowEndOverrunSurface) && Objects.equals(lowEndOverrunGeodeticLatitude, that.lowEndOverrunGeodeticLatitude) && Objects.equals(lowEndOverrunDegreesLatitude, that.lowEndOverrunDegreesLatitude) && Objects.equals(lowEndOverrunGeodeticLongitude, that.lowEndOverrunGeodeticLongitude) && Objects.equals(lowEndOverrunDegreesLongitude, that.lowEndOverrunDegreesLongitude) && Objects.equals(cycleDate, that.cycleDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, highEndRunwayIdentifier, lowEndRunwayIdentifier, icaoCode, highEndDisplacedThresholdGeodeticLatitude, highEndDisplacedThresholdDegreesLatitude, highEndDisplacedThresholdGeodeticLongitude, highEndDisplacedThresholdDegreesLongitude, highEndOverrunDistance, highEndOverrunSurface, highEndOverrunGeodeticLatitude, highEndOverrunDegreesLatitude, highEndOverrunGeodeticLongitude, highEndOverrunDegreesLongitude, lowEndDisplacedThresholdGeodeticLatitude, lowEndDisplacedThresholdDegreesLatitude, lowEndDisplacedThresholdGeodeticLongitude, lowEndDisplacedThresholdDegreesLongitude, lowEndOverrunDistance, lowEndOverrunSurface, lowEndOverrunGeodeticLatitude, lowEndOverrunDegreesLatitude, lowEndOverrunGeodeticLongitude, lowEndOverrunDegreesLongitude, cycleDate);
  }

  public String getAirportIdentification() {
    return airportIdentification;
  }

  public String getHighEndRunwayIdentifier() {
    return highEndRunwayIdentifier;
  }

  public String getLowEndRunwayIdentifier() {
    return lowEndRunwayIdentifier;
  }

  public String getIcaoCode() {
    return icaoCode;
  }

  public String getHighEndDisplacedThresholdGeodeticLatitude() {
    return highEndDisplacedThresholdGeodeticLatitude;
  }

  public Double getHighEndDisplacedThresholdDegreesLatitude() {
    return highEndDisplacedThresholdDegreesLatitude;
  }

  public String getHighEndDisplacedThresholdGeodeticLongitude() {
    return highEndDisplacedThresholdGeodeticLongitude;
  }

  public Double getHighEndDisplacedThresholdDegreesLongitude() {
    return highEndDisplacedThresholdDegreesLongitude;
  }

  public Integer getHighEndOverrunDistance() {
    return highEndOverrunDistance;
  }

  public String getHighEndOverrunSurface() {
    return highEndOverrunSurface;
  }

  public String getHighEndOverrunGeodeticLatitude() {
    return highEndOverrunGeodeticLatitude;
  }

  public Double getHighEndOverrunDegreesLatitude() {
    return highEndOverrunDegreesLatitude;
  }

  public String getHighEndOverrunGeodeticLongitude() {
    return highEndOverrunGeodeticLongitude;
  }

  public Double getHighEndOverrunDegreesLongitude() {
    return highEndOverrunDegreesLongitude;
  }

  public String getLowEndDisplacedThresholdGeodeticLatitude() {
    return lowEndDisplacedThresholdGeodeticLatitude;
  }

  public Double getLowEndDisplacedThresholdDegreesLatitude() {
    return lowEndDisplacedThresholdDegreesLatitude;
  }

  public String getLowEndDisplacedThresholdGeodeticLongitude() {
    return lowEndDisplacedThresholdGeodeticLongitude;
  }

  public Double getLowEndDisplacedThresholdDegreesLongitude() {
    return lowEndDisplacedThresholdDegreesLongitude;
  }

  public Integer getLowEndOverrunDistance() {
    return lowEndOverrunDistance;
  }

  public String getLowEndOverrunSurface() {
    return lowEndOverrunSurface;
  }

  public String getLowEndOverrunGeodeticLatitude() {
    return lowEndOverrunGeodeticLatitude;
  }

  public Double getLowEndOverrunDegreesLatitude() {
    return lowEndOverrunDegreesLatitude;
  }

  public String getLowEndOverrunGeodeticLongitude() {
    return lowEndOverrunGeodeticLongitude;
  }

  public Double getLowEndOverrunDegreesLongitude() {
    return lowEndOverrunDegreesLongitude;
  }

  public Integer getCycleDate() {
    return cycleDate;
  }

  public static class Builder {
    private String airportIdentification;
    private String highEndRunwayIdentifier;
    private String lowEndRunwayIdentifier;
    private String icaoCode;
    private String highEndDisplacedThresholdGeodeticLatitude;
    private Double highEndDisplacedThresholdDegreesLatitude;
    private String highEndDisplacedThresholdGeodeticLongitude;
    private Double highEndDisplacedThresholdDegreesLongitude;
    private Integer highEndOverrunDistance;
    private String highEndOverrunSurface;
    private String highEndOverrunGeodeticLatitude;
    private Double highEndOverrunDegreesLatitude;
    private String highEndOverrunGeodeticLongitude;
    private Double highEndOverrunDegreesLongitude;
    private String lowEndDisplacedThresholdGeodeticLatitude;
    private Double lowEndDisplacedThresholdDegreesLatitude;
    private String lowEndDisplacedThresholdGeodeticLongitude;
    private Double lowEndDisplacedThresholdDegreesLongitude;
    private Integer lowEndOverrunDistance;
    private String lowEndOverrunSurface;
    private String lowEndOverrunGeodeticLatitude;
    private Double lowEndOverrunDegreesLatitude;
    private String lowEndOverrunGeodeticLongitude;
    private Double lowEndOverrunDegreesLongitude;
    private Integer cycleDate;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder highEndRunwayIdentifier(String highEndRunwayIdentifier) {
      this.highEndRunwayIdentifier = highEndRunwayIdentifier;
      return this;
    }

    public Builder lowEndRunwayIdentifier(String lowEndRunwayIdentifier) {
      this.lowEndRunwayIdentifier = lowEndRunwayIdentifier;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder highEndDisplacedThresholdGeodeticLatitude(String highEndDisplacedThresholdGeodeticLatitude) {
      this.highEndDisplacedThresholdGeodeticLatitude = highEndDisplacedThresholdGeodeticLatitude;
      return this;
    }

    public Builder highEndDisplacedThresholdDegreesLatitude(Double highEndDisplacedThresholdDegreesLatitude) {
      this.highEndDisplacedThresholdDegreesLatitude = highEndDisplacedThresholdDegreesLatitude;
      return this;
    }

    public Builder highEndDisplacedThresholdGeodeticLongitude(String highEndDisplacedThresholdGeodeticLongitude) {
      this.highEndDisplacedThresholdGeodeticLongitude = highEndDisplacedThresholdGeodeticLongitude;
      return this;
    }

    public Builder highEndDisplacedThresholdDegreesLongitude(Double highEndDisplacedThresholdDegreesLongitude) {
      this.highEndDisplacedThresholdDegreesLongitude = highEndDisplacedThresholdDegreesLongitude;
      return this;
    }

    public Builder highEndOverrunDistance(Integer highEndOverrunDistance) {
      this.highEndOverrunDistance = highEndOverrunDistance;
      return this;
    }

    public Builder highEndOverrunSurface(String highEndOverrunSurface) {
      this.highEndOverrunSurface = highEndOverrunSurface;
      return this;
    }

    public Builder highEndOverrunGeodeticLatitude(String highEndOverrunGeodeticLatitude) {
      this.highEndOverrunGeodeticLatitude = highEndOverrunGeodeticLatitude;
      return this;
    }

    public Builder highEndOverrunDegreesLatitude(Double highEndOverrunDegreesLatitude) {
      this.highEndOverrunDegreesLatitude = highEndOverrunDegreesLatitude;
      return this;
    }

    public Builder highEndOverrunGeodeticLongitude(String highEndOverrunGeodeticLongitude) {
      this.highEndOverrunGeodeticLongitude = highEndOverrunGeodeticLongitude;
      return this;
    }

    public Builder highEndOverrunDegreesLongitude(Double highEndOverrunDegreesLongitude) {
      this.highEndOverrunDegreesLongitude = highEndOverrunDegreesLongitude;
      return this;
    }

    public Builder lowEndDisplacedThresholdGeodeticLatitude(String lowEndDisplacedThresholdGeodeticLatitude) {
      this.lowEndDisplacedThresholdGeodeticLatitude = lowEndDisplacedThresholdGeodeticLatitude;
      return this;
    }

    public Builder lowEndDisplacedThresholdDegreesLatitude(Double lowEndDisplacedThresholdDegreesLatitude) {
      this.lowEndDisplacedThresholdDegreesLatitude = lowEndDisplacedThresholdDegreesLatitude;
      return this;
    }

    public Builder lowEndDisplacedThresholdGeodeticLongitude(String lowEndDisplacedThresholdGeodeticLongitude) {
      this.lowEndDisplacedThresholdGeodeticLongitude = lowEndDisplacedThresholdGeodeticLongitude;
      return this;
    }

    public Builder lowEndDisplacedThresholdDegreesLongitude(Double lowEndDisplacedThresholdDegreesLongitude) {
      this.lowEndDisplacedThresholdDegreesLongitude = lowEndDisplacedThresholdDegreesLongitude;
      return this;
    }

    public Builder lowEndOverrunDistance(Integer lowEndOverrunDistance) {
      this.lowEndOverrunDistance = lowEndOverrunDistance;
      return this;
    }

    public Builder lowEndOverrunSurface(String lowEndOverrunSurface) {
      this.lowEndOverrunSurface = lowEndOverrunSurface;
      return this;
    }

    public Builder lowEndOverrunGeodeticLatitude(String lowEndOverrunGeodeticLatitude) {
      this.lowEndOverrunGeodeticLatitude = lowEndOverrunGeodeticLatitude;
      return this;
    }

    public Builder lowEndOverrunDegreesLatitude(Double lowEndOverrunDegreesLatitude) {
      this.lowEndOverrunDegreesLatitude = lowEndOverrunDegreesLatitude;
      return this;
    }

    public Builder lowEndOverrunGeodeticLongitude(String lowEndOverrunGeodeticLongitude) {
      this.lowEndOverrunGeodeticLongitude = lowEndOverrunGeodeticLongitude;
      return this;
    }

    public Builder lowEndOverrunDegreesLongitude(Double lowEndOverrunDegreesLongitude) {
      this.lowEndOverrunDegreesLongitude = lowEndOverrunDegreesLongitude;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public DafifAddRunway build() {
      return new DafifAddRunway(this);
    }
  }
}
