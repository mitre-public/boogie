package org.mitre.tdp.boogie.dafif.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.dafif.v81.field.AccelerateStopDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndDisplacedThreshold;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndRunwayDisplacedThresholdElevation;
import org.mitre.tdp.boogie.dafif.v81.field.HighEndRunwayElevation;
import org.mitre.tdp.boogie.dafif.v81.field.LandingDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.Length;
import org.mitre.tdp.boogie.dafif.v81.field.LightingSystem;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndDisplacedThreshold;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndRunwayDisplacedThresholdElevation;
import org.mitre.tdp.boogie.dafif.v81.field.LowEndRunwayElevation;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticHeading;
import org.mitre.tdp.boogie.dafif.v81.field.PavementClassificationNumber;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayTrueHeading;
import org.mitre.tdp.boogie.dafif.v81.field.Slope;
import org.mitre.tdp.boogie.dafif.v81.field.Surface;
import org.mitre.tdp.boogie.dafif.v81.field.TDZE;
import org.mitre.tdp.boogie.dafif.v81.field.TakeoffDistanceAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.TakeoffRunwayAvailable;
import org.mitre.tdp.boogie.dafif.v81.field.UsableRunway;
import org.mitre.tdp.boogie.dafif.v81.field.Width;

public final class DafifRunway {

  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;

  /**
   * {@link RunwayIdentifier}
   */
  private final String highEndIdentifier;

  /**
   * {@link RunwayIdentifier}
   */
  private final String lowEndIdentifier;

  /**
   * {@link MagneticHeading}
   */
  private final Double highEndMagneticHeading;

  /**
   * {@link MagneticHeading}
   */
  private final Double lowEndMagneticHeading;

  /**
   * {@link Length}
   */
  private final Integer length;

  /**
   * {@link Width}
   */
  private final Integer width;

  /**
   * {@link Surface}
   */
  private final String surface;

  /**
   * {@link PavementClassificationNumber}
   */
  private final String pavementClassificationNumber;

  /**
   * {@link GeodeticLatitude}
   */
  private final String highEndGeodeticLatitude;

  /**
   * {@link DegreesLatitude}
   */
  private final Double highEndDegreesLatitude;

  /**
   * {@link GeodeticLongitude}
   */
  private final String highEndGeodeticLongitude;

  /**
   * {@link DegreesLongitude}
   */
  private final Double highEndDegreesLongitude;

  /**
   * {@link HighEndRunwayElevation}
   */
  private final String highEndElevation;

  /**
   * {@link Slope}
   */
  private final String highEndSlope;

  /**
   * {@link TDZE}
   */
  private final String highEndTDZE;

  /**
   * {@link HighEndDisplacedThreshold}
   */
  private final Integer highEndDisplacedThreshold;

  /**
   * {@link HighEndRunwayDisplacedThresholdElevation}
   */
  private final String highEndDisplacedThresholdElevation;

  /**
   * {@link LightingSystem}
   */
  private final List<Integer> highEndLightingSystem;

  /**
   * {@link GeodeticLatitude}
   */
  private final String lowEndGeodeticLatitude;

  /**
   * {@link DegreesLatitude}
   */
  private final Double lowEndDegreesLatitude;

  /**
   * {@link GeodeticLongitude}
   */
  private final String lowEndGeodeticLongitude;

  /**
   * {@link DegreesLongitude}
   */
  private final Double lowEndDegreesLongitude;

  /**
   * {@link LowEndRunwayElevation}
   */
  private final String lowEndElevation;

  /**
   * {@link Slope}
   */
  private final String lowEndSlope;

  /**
   * {@link TDZE}
   */
  private final String lowEndTDZE;

  /**
   * {@link LowEndDisplacedThreshold}
   */
  private final Integer lowEndDisplacedThreshold;

  /**
   * {@link LowEndRunwayDisplacedThresholdElevation}
   */
  private final String lowEndDisplacedThresholdElevation;

  /**
   * {@link LightingSystem}
   */
  private final List<Integer> lowEndLightingSystem;

  /**
   * {@link RunwayTrueHeading}
   */
  private final Double trueHeadingHighEnd;

  /**
   * {@link RunwayTrueHeading}
   */
  private final Double trueHeadingLowEnd;

  /**
   * {@link UsableRunway}
   */
  private final String usableRunway;

  /**
   * {@link LandingDistanceAvailable}
   */
  private final Integer highEndLandingDistance;

  /**
   * {@link TakeoffRunwayAvailable}
   */
  private final Integer highEndRunwayDistance;

  /**
   * {@link TakeoffDistanceAvailable}
   */
  private final Integer highEndTakeOffDistance;

  /**
   * {@link AccelerateStopDistanceAvailable}
   */
  private final Integer highEndAccelerateStopDistance;

  /**
   * {@link LandingDistanceAvailable}
   */
  private final Integer lowEndLandingDistance;

  /**
   * {@link TakeoffRunwayAvailable}
   */
  private final Integer lowEndRunwayDistance;

  /**
   * {@link TakeoffDistanceAvailable}
   */
  private final Integer lowEndTakeOffDistance;

  /**
   * {@link AccelerateStopDistanceAvailable}
   */
  private final Integer lowEndAccelerateStopDistance;

  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;

  /**
   * {@link CoordinatePrecision}
   */
  private final Integer coordinatePrecision;

  private DafifRunway(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.highEndIdentifier = builder.highEndIdentifier;
    this.lowEndIdentifier = builder.lowEndIdentifier;
    this.highEndMagneticHeading = builder.highEndMagneticHeading;
    this.lowEndMagneticHeading = builder.lowEndMagneticHeading;
    this.length = builder.length;
    this.width = builder.width;
    this.surface = builder.surface;
    this.pavementClassificationNumber = builder.pavementClassificationNumber;
    this.highEndGeodeticLatitude = builder.highEndGeodeticLatitude;
    this.highEndDegreesLatitude = builder.highEndDegreesLatitude;
    this.highEndGeodeticLongitude = builder.highEndGeodeticLongitude;
    this.highEndDegreesLongitude = builder.highEndDegreesLongitude;
    this.highEndElevation = builder.highEndElevation;
    this.highEndSlope = builder.highEndSlope;
    this.highEndTDZE = builder.highEndTDZE;
    this.highEndDisplacedThreshold = builder.highEndDisplacedThreshold;
    this.highEndDisplacedThresholdElevation = builder.highEndDisplacedThresholdElevation;
    this.highEndLightingSystem = builder.highEndLightingSystem;
    this.lowEndGeodeticLatitude = builder.lowEndGeodeticLatitude;
    this.lowEndDegreesLatitude = builder.lowEndDegreesLatitude;
    this.lowEndGeodeticLongitude = builder.lowEndGeodeticLongitude;
    this.lowEndDegreesLongitude = builder.lowEndDegreesLongitude;
    this.lowEndElevation = builder.lowEndElevation;
    this.lowEndSlope = builder.lowEndSlope;
    this.lowEndTDZE = builder.lowEndTDZE;
    this.lowEndDisplacedThreshold = builder.lowEndDisplacedThreshold;
    this.lowEndDisplacedThresholdElevation = builder.lowEndDisplacedThresholdElevation;
    this.lowEndLightingSystem = builder.lowEndLightingSystem;
    this.trueHeadingHighEnd = builder.trueHeadingHighEnd;
    this.trueHeadingLowEnd = builder.trueHeadingLowEnd;
    this.usableRunway = builder.usableRunway;
    this.highEndLandingDistance = builder.highEndLandingDistance;
    this.highEndRunwayDistance = builder.highEndRunwayDistance;
    this.highEndTakeOffDistance = builder.highEndTakeOffDistance;
    this.highEndAccelerateStopDistance = builder.highEndAccelerateStopDistance;
    this.lowEndLandingDistance = builder.lowEndLandingDistance;
    this.lowEndRunwayDistance = builder.lowEndRunwayDistance;
    this.lowEndTakeOffDistance = builder.lowEndTakeOffDistance;
    this.lowEndAccelerateStopDistance = builder.lowEndAccelerateStopDistance;
    this.cycleDate = builder.cycleDate;
    this.coordinatePrecision = builder.coordinatePrecision;
  }

  public String airportIdentification() {
    return airportIdentification;
  }

  public String highEndIdentifier() {
    return highEndIdentifier;
  }

  public String lowEndIdentifier() {
    return lowEndIdentifier;
  }

  public Integer width() {
    return width;
  }

  public Integer length() {
    return length;
  }

  public String highEndSlope() {
    return highEndSlope;
  }

  public String lowEndSlope() {
    return lowEndSlope;
  }

  public Double trueHeadingHighEnd() {
    return trueHeadingHighEnd;
  }

  public Double trueHeadingLowEnd() {
    return trueHeadingLowEnd;
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public Optional<Double> highEndMagneticHeading() {
    return Optional.ofNullable(highEndMagneticHeading);
  }

  public Optional<Double> lowEndMagneticHeading() {
    return Optional.ofNullable(lowEndMagneticHeading);
  }

  public Optional<String> surface() {
    return Optional.ofNullable(surface);
  }

  public Optional<String> pavementClassificationNumber() {
    return Optional.ofNullable(pavementClassificationNumber);
  }

  public Optional<String> highEndGeodeticLatitude() {
    return Optional.ofNullable(highEndGeodeticLatitude);
  }

  public Optional<Double> highEndDegreesLatitude() {
    return Optional.ofNullable(highEndDegreesLatitude);
  }

  public Optional<String> highEndGeodeticLongitude() {
    return Optional.ofNullable(highEndGeodeticLongitude);
  }

  public Optional<Double> highEndDegreesLongitude() {
    return Optional.ofNullable(highEndDegreesLongitude);
  }

  public Optional<String> highEndElevation() {
    return Optional.ofNullable(highEndElevation);
  }

  public Optional<String> highEndTDZE() {
    return Optional.ofNullable(highEndTDZE);
  }

  public Optional<Integer> highEndDisplacedThreshold() {
    return Optional.ofNullable(highEndDisplacedThreshold);
  }

  public Optional<String> highEndDisplacedThresholdElevation() {
    return Optional.ofNullable(highEndDisplacedThresholdElevation);
  }

  public Optional<List<Integer>> highEndLightingSystem() {
    return Optional.ofNullable(highEndLightingSystem);
  }

  public Optional<String> lowEndGeodeticLatitude() {
    return Optional.ofNullable(lowEndGeodeticLatitude);
  }

  public Optional<Double> lowEndDegreesLatitude() {
    return Optional.ofNullable(lowEndDegreesLatitude);
  }

  public Optional<String> lowEndGeodeticLongitude() {
    return Optional.ofNullable(lowEndGeodeticLongitude);
  }

  public Optional<Double> lowEndDegreesLongitude() {
    return Optional.ofNullable(lowEndDegreesLongitude);
  }

  public Optional<String> lowEndElevation() {
    return Optional.ofNullable(lowEndElevation);
  }

  public Optional<String> lowEndTDZE() {
    return Optional.ofNullable(lowEndTDZE);
  }

  public Optional<Integer> lowEndDisplacedThreshold() {
    return Optional.ofNullable(lowEndDisplacedThreshold);
  }

  public Optional<String> lowEndDisplacedThresholdElevation() {
    return Optional.ofNullable(lowEndDisplacedThresholdElevation);
  }

  public Optional<List<Integer>> lowEndLightingSystem() {
    return Optional.ofNullable(lowEndLightingSystem);
  }

  public Optional<String> usableRunway() {
    return Optional.ofNullable(usableRunway);
  }

  public Optional<Integer> highEndLandingDistance() {
    return Optional.ofNullable(highEndLandingDistance);
  }

  public Optional<Integer> highEndRunwayDistance() {
    return Optional.ofNullable(highEndRunwayDistance);
  }

  public Optional<Integer> highEndTakeOffDistance() {
    return Optional.ofNullable(highEndTakeOffDistance);
  }

  public Optional<Integer> highEndAccelerateStopDistance() {
    return Optional.ofNullable(highEndAccelerateStopDistance);
  }

  public Optional<Integer> lowEndLandingDistance() {
    return Optional.ofNullable(lowEndLandingDistance);
  }

  public Optional<Integer> lowEndRunwayDistance() {
    return Optional.ofNullable(lowEndRunwayDistance);
  }

  public Optional<Integer> lowEndTakeOffDistance() {
    return Optional.ofNullable(lowEndTakeOffDistance);
  }

  public Optional<Integer> lowEndAccelerateStopDistance() {
    return Optional.ofNullable(lowEndAccelerateStopDistance);
  }

  public Optional<Integer> coordinatePrecision() {
    return Optional.ofNullable(coordinatePrecision);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifRunway that = (DafifRunway) o;
    return Objects.equals(airportIdentification, that.airportIdentification) && Objects.equals(highEndIdentifier, that.highEndIdentifier) && Objects.equals(lowEndIdentifier, that.lowEndIdentifier) && Objects.equals(highEndMagneticHeading, that.highEndMagneticHeading) && Objects.equals(lowEndMagneticHeading, that.lowEndMagneticHeading) && Objects.equals(length, that.length) && Objects.equals(width, that.width) && Objects.equals(surface, that.surface) && Objects.equals(pavementClassificationNumber, that.pavementClassificationNumber) && Objects.equals(highEndGeodeticLatitude, that.highEndGeodeticLatitude) && Objects.equals(highEndDegreesLatitude, that.highEndDegreesLatitude) && Objects.equals(highEndGeodeticLongitude, that.highEndGeodeticLongitude) && Objects.equals(highEndDegreesLongitude, that.highEndDegreesLongitude) && Objects.equals(highEndElevation, that.highEndElevation) && Objects.equals(highEndSlope, that.highEndSlope) && Objects.equals(highEndTDZE, that.highEndTDZE) && Objects.equals(highEndDisplacedThreshold, that.highEndDisplacedThreshold) && Objects.equals(highEndDisplacedThresholdElevation, that.highEndDisplacedThresholdElevation) && Objects.equals(highEndLightingSystem, that.highEndLightingSystem) && Objects.equals(lowEndGeodeticLatitude, that.lowEndGeodeticLatitude) && Objects.equals(lowEndDegreesLatitude, that.lowEndDegreesLatitude) && Objects.equals(lowEndGeodeticLongitude, that.lowEndGeodeticLongitude) && Objects.equals(lowEndDegreesLongitude, that.lowEndDegreesLongitude) && Objects.equals(lowEndElevation, that.lowEndElevation) && Objects.equals(lowEndSlope, that.lowEndSlope) && Objects.equals(lowEndTDZE, that.lowEndTDZE) && Objects.equals(lowEndDisplacedThreshold, that.lowEndDisplacedThreshold) && Objects.equals(lowEndDisplacedThresholdElevation, that.lowEndDisplacedThresholdElevation) && Objects.equals(lowEndLightingSystem, that.lowEndLightingSystem) && Objects.equals(trueHeadingHighEnd, that.trueHeadingHighEnd) && Objects.equals(trueHeadingLowEnd, that.trueHeadingLowEnd) && Objects.equals(usableRunway, that.usableRunway) && Objects.equals(highEndLandingDistance, that.highEndLandingDistance) && Objects.equals(highEndRunwayDistance, that.highEndRunwayDistance) && Objects.equals(highEndTakeOffDistance, that.highEndTakeOffDistance) && Objects.equals(highEndAccelerateStopDistance, that.highEndAccelerateStopDistance) && Objects.equals(lowEndLandingDistance, that.lowEndLandingDistance) && Objects.equals(lowEndRunwayDistance, that.lowEndRunwayDistance) &&Objects.equals(lowEndTakeOffDistance, that.lowEndTakeOffDistance) && Objects.equals(lowEndAccelerateStopDistance, that.lowEndAccelerateStopDistance) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(coordinatePrecision, that.coordinatePrecision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, highEndIdentifier, lowEndIdentifier, highEndMagneticHeading, lowEndMagneticHeading, length, width, surface, pavementClassificationNumber, highEndGeodeticLatitude, highEndDegreesLatitude, highEndGeodeticLongitude, highEndDegreesLongitude, highEndElevation, highEndSlope, highEndTDZE, highEndDisplacedThreshold, highEndDisplacedThresholdElevation, highEndLightingSystem, lowEndGeodeticLatitude, lowEndDegreesLatitude, lowEndGeodeticLongitude, lowEndDegreesLongitude, lowEndElevation, lowEndSlope, lowEndTDZE, lowEndDisplacedThreshold, lowEndDisplacedThresholdElevation, lowEndLightingSystem, trueHeadingHighEnd, trueHeadingLowEnd, usableRunway, highEndLandingDistance, highEndRunwayDistance, highEndTakeOffDistance, highEndAccelerateStopDistance, lowEndLandingDistance, lowEndRunwayDistance, lowEndTakeOffDistance, lowEndAccelerateStopDistance, cycleDate, coordinatePrecision);
  }

  public static final class Builder {
    private String airportIdentification;
    private String highEndIdentifier;
    private String lowEndIdentifier;
    private Double highEndMagneticHeading;
    private Double lowEndMagneticHeading;
    private Integer length;
    private Integer width;
    private String surface;
    private String pavementClassificationNumber;
    private String highEndGeodeticLatitude;
    private Double highEndDegreesLatitude;
    private String highEndGeodeticLongitude;
    private Double highEndDegreesLongitude;
    private String highEndElevation;
    private String highEndSlope;
    private String highEndTDZE;
    private Integer highEndDisplacedThreshold;
    private String highEndDisplacedThresholdElevation;
    private List<Integer> highEndLightingSystem;
    private String lowEndGeodeticLatitude;
    private Double lowEndDegreesLatitude;
    private String lowEndGeodeticLongitude;
    private Double lowEndDegreesLongitude;
    private String lowEndElevation;
    private String lowEndSlope;
    private String lowEndTDZE;
    private Integer lowEndDisplacedThreshold;
    private String lowEndDisplacedThresholdElevation;
    private List<Integer> lowEndLightingSystem;
    private Double trueHeadingHighEnd;
    private Double trueHeadingLowEnd;
    private String usableRunway;
    private Integer highEndLandingDistance;
    private Integer highEndRunwayDistance;
    private Integer highEndTakeOffDistance;
    private Integer highEndAccelerateStopDistance;
    private Integer lowEndLandingDistance;
    private Integer lowEndRunwayDistance;
    private Integer lowEndTakeOffDistance;
    private Integer lowEndAccelerateStopDistance;
    private Integer cycleDate;
    private Integer coordinatePrecision;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder highEndIdentifier(String highEndIdentifier) {
      this.highEndIdentifier = highEndIdentifier;
      return this;
    }

    public Builder lowEndIdentifier(String lowEndIdentifier) {
      this.lowEndIdentifier = lowEndIdentifier;
      return this;
    }

    public Builder highEndMagneticHeading(Double highEndMagneticHeading) {
      this.highEndMagneticHeading = highEndMagneticHeading;
      return this;
    }

    public Builder lowEndMagneticHeading(Double lowEndMagneticHeading) {
      this.lowEndMagneticHeading = lowEndMagneticHeading;
      return this;
    }

    public Builder length(Integer length) {
      this.length = length;
      return this;
    }

    public Builder width(Integer width) {
      this.width = width;
      return this;
    }

    public Builder surface(String surface) {
      this.surface = surface;
      return this;
    }

    public Builder pavementClassificationNumber(String pavementClassificationNumber) {
      this.pavementClassificationNumber = pavementClassificationNumber;
      return this;
    }

    public Builder highEndGeodeticLatitude(String highEndGeodeticLatitude) {
      this.highEndGeodeticLatitude = highEndGeodeticLatitude;
      return this;
    }

    public Builder highEndDegreesLatitude(Double highEndDegreesLatitude) {
      this.highEndDegreesLatitude = highEndDegreesLatitude;
      return this;
    }

    public Builder highEndGeodeticLongitude(String highEndGeodeticLongitude) {
      this.highEndGeodeticLongitude = highEndGeodeticLongitude;
      return this;
    }

    public Builder highEndDegreesLongitude(Double highEndDegreesLongitude) {
      this.highEndDegreesLongitude = highEndDegreesLongitude;
      return this;
    }

    public Builder highEndElevation(String highEndElevation) {
      this.highEndElevation = highEndElevation;
      return this;
    }

    public Builder highEndSlope(String highEndSlope) {
      this.highEndSlope = highEndSlope;
      return this;
    }

    public Builder highEndTDZE(String highEndTDZE) {
      this.highEndTDZE = highEndTDZE;
      return this;
    }

    public Builder highEndDisplacedThreshold(Integer highEndDisplacedThreshold) {
      this.highEndDisplacedThreshold = highEndDisplacedThreshold;
      return this;
    }

    public Builder highEndDisplacedThresholdElevation(String highEndDisplacedThresholdElevation) {
      this.highEndDisplacedThresholdElevation = highEndDisplacedThresholdElevation;
      return this;
    }

    public Builder highEndLightingSystem(List<Integer> highEndLightingSystem) {
      this.highEndLightingSystem = highEndLightingSystem;
      return this;
    }

    public Builder lowEndGeodeticLatitude(String lowEndGeodeticLatitude) {
      this.lowEndGeodeticLatitude = lowEndGeodeticLatitude;
      return this;
    }

    public Builder lowEndDegreesLatitude(Double lowEndDegreesLatitude) {
      this.lowEndDegreesLatitude = lowEndDegreesLatitude;
      return this;
    }

    public Builder lowEndGeodeticLongitude(String lowEndGeodeticLongitude) {
      this.lowEndGeodeticLongitude = lowEndGeodeticLongitude;
      return this;
    }

    public Builder lowEndDegreesLongitude(Double lowEndDegreesLongitude) {
      this.lowEndDegreesLongitude = lowEndDegreesLongitude;
      return this;
    }

    public Builder lowEndElevation(String lowEndElevation) {
      this.lowEndElevation = lowEndElevation;
      return this;
    }

    public Builder lowEndSlope(String lowEndSlope) {
      this.lowEndSlope = lowEndSlope;
      return this;
    }

    public Builder lowEndTDZE(String lowEndTDZE) {
      this.lowEndTDZE = lowEndTDZE;
      return this;
    }

    public Builder lowEndDisplacedThreshold(Integer lowEndDisplacedThreshold) {
      this.lowEndDisplacedThreshold = lowEndDisplacedThreshold;
      return this;
    }

    public Builder lowEndDisplacedThresholdElevation(String lowEndDisplacedThresholdElevation) {
      this.lowEndDisplacedThresholdElevation = lowEndDisplacedThresholdElevation;
      return this;
    }

    public Builder lowEndLightingSystem(List<Integer> lowEndLightingSystem) {
      this.lowEndLightingSystem = lowEndLightingSystem;
      return this;
    }

    public Builder trueHeadingHighEnd(Double trueHeadingHighEnd) {
      this.trueHeadingHighEnd = trueHeadingHighEnd;
      return this;
    }

    public Builder trueHeadingLowEnd(Double trueHeadingLowEnd) {
      this.trueHeadingLowEnd = trueHeadingLowEnd;
      return this;
    }

    public Builder usableRunway(String usableRunway) {
      this.usableRunway = usableRunway;
      return this;
    }

    public Builder highEndLandingDistance(Integer highEndLandingDistance) {
      this.highEndLandingDistance = highEndLandingDistance;
      return this;
    }

    public Builder highEndRunwayDistance(Integer highEndRunwayDistance) {
      this.highEndRunwayDistance = highEndRunwayDistance;
      return this;
    }

    public Builder highEndTakeOffDistance(Integer highEndTakeOffDistance) {
      this.highEndTakeOffDistance = highEndTakeOffDistance;
      return this;
    }

    public Builder highEndAccelerateStopDistance(Integer highEndAccelerateStopDistance) {
      this.highEndAccelerateStopDistance = highEndAccelerateStopDistance;
      return this;
    }

    public Builder lowEndLandingDistance(Integer lowEndLandingDistance) {
      this.lowEndLandingDistance = lowEndLandingDistance;
      return this;
    }

    public Builder lowEndRunwayDistance(Integer lowEndRunwayDistance) {
      this.lowEndRunwayDistance = lowEndRunwayDistance;
      return this;
    }

    public Builder lowEndTakeOffDistance(Integer lowEndTakeOffDistance) {
      this.lowEndTakeOffDistance = lowEndTakeOffDistance;
      return this;
    }

    public Builder lowEndAccelerateStopDistance(Integer lowEndAccelerateStopDistance) {
      this.lowEndAccelerateStopDistance = lowEndAccelerateStopDistance;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder coordinatePrecision(Integer coordinatePrecision) {
      this.coordinatePrecision = coordinatePrecision;
      return this;
    }

    public DafifRunway build() {
      return new DafifRunway(this);
    }
  }

}
