package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.collect.Range;

/**
 * A leg (as would or could be flown by an aircraft) from the perspective of Boogie. This class represents a superset of features
 * relevant to all common leg types (enumerated in the {@link PathTerminator} class).
 *
 * <p>This class provides a collection of factory/builder methods for instantiating legs of different {@link PathTerminator} types
 * with only setters for fields that make sense on them as per the ARINC424 leg specification.
 */
public interface Leg {

  /**
   * Returns a builder for a standard leg implementation containing a settable superset of the fields for any given type of leg,
   * mirroring the field-level spec for legs outlined in ARINC-424.
   *
   * <p>The {@link Leg} interface provides factory/builder methods for the various specific types of legs with only the relevant
   * field options settable - however the final built object is of the {@link Standard} type.
   *
   * <p>It is recommended that clients use the alternate {@link PathTerminator}-specific builders/factory methods instead this
   * method where possible for clarity.
   *
   * @param pathTerminator all legs regardless of type must have a {@link PathTerminator} designator
   */
  static Standard.Builder builder(PathTerminator pathTerminator, int sequenceNumber) {
    return new Standard.Builder(pathTerminator).sequenceNumber(sequenceNumber);
  }

  /**
   * Returns a new leg {@link Record} representing a decorated client object and a Boogie {@link Procedure} implementation the
   * record object can delegate its method calls to.
   *
   * <p>This is provided to make it clear to clients that they can easily decorate and use their own objects as legs with much
   * of Boogie's internal algorithms and then unwrap them later post-processing to get their objects back.
   *
   * @param datum    the client record type the leg was derived from
   * @param delegate the leg the delegate calls to (typically a {@link Standard})
   */
  static <T> Record<T> record(T datum, Leg delegate) {
    return new Record<>(datum, delegate);
  }

  /**
   * Intended use is similar to {@link #record(Object, Leg)}, but clarifies the contract that the delegated-to {@link Leg} impl
   * should be derived from the input client data type.
   *
   * @param datum the client record type the procedure was derived from
   * @param maker function to construct the delegate procedure implementation from the client object
   */
  static <T> Record<T> make(T datum, Function<T, Leg> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * Returns a new constrained instance of a builder for {@link Leg.Standard} specific to {@link PathTerminator#AF} legs.
   *
   * <p>This builder contains a subset of the setters available on the {@link Standard.Builder} and will throw exceptions when
   * building if the required fields aren't present.
   */
  static AfBuilder afBuilder(Fix associatedFix, Fix recommendedNavaid, int sequenceNumber, double outboundMagneticCourse) {
    return new AfBuilder()
        .associatedFix(associatedFix)
        .recommendedNavaid(recommendedNavaid)
        .sequenceNumber(sequenceNumber)
        .outboundMagneticCourse(outboundMagneticCourse);
  }

  /**
   * Returns a new constrained instance of a builder for {@link Leg.Standard} specific to {@link PathTerminator#IF} legs.
   *
   * <p>This builder contains a subset of the setters available on the {@link Standard.Builder} and will throw exceptions when
   * building if the required fields aren't present.
   */
  static IfBuilder ifBuilder(Fix associatedFix, int sequenceNumber) {
    return new IfBuilder().associatedFix(associatedFix).sequenceNumber(sequenceNumber);
  }

  /**
   * Returns a new constrained instance of a builder for {@link Leg.Standard} specific to {@link PathTerminator#DF} legs.
   *
   * <p>This builder contains a subset of the setters available on the {@link Standard.Builder} and will throw exceptions when
   * building if the required fields aren't present.
   */
  static DfBuilder dfBuilder(Fix associatedFix, int sequenceNumber) {
    return new DfBuilder().associatedFix(associatedFix).sequenceNumber(sequenceNumber);
  }

  /**
   * Returns a new constrained instance of a builder for {@link Leg.Standard} specific to {@link PathTerminator#TF} legs.
   *
   * <p>This builder contains a subset of the setters available on the {@link Standard.Builder} and will throw exceptions when
   * building if the required fields aren't present.
   */
  static TfBuilder tfBuilder(Fix associatedFix, int sequenceNumber) {
    return new TfBuilder().associatedFix(associatedFix).sequenceNumber(sequenceNumber);
  }

  static CfBuilder cfBuilder(Fix associatedFix, int sequenceNumber, double outboundMagneticCourse) {
    return new CfBuilder().associatedFix(associatedFix).sequenceNumber(sequenceNumber).outboundMagneticCourse(outboundMagneticCourse);
  }

  /**
   * The associated fix for the leg if one was specified in the leg definition.
   * <p>For example:
   * <ol>
   *   <li>TF (track-to-fix), this will be a navaid or a waypoint as a location is required to specify the end of the 2D path
   *   segment</li>
   *   <li>CA (course-to-altitude), this will be empty as the leg doesn't end in a concrete location - instead it ends when the
   *   flight reaches some altitude while flying the specified heading</li>
   * </ol>
   */
  Optional<? extends Fix> associatedFix();

  /**
   * If the leg requires a navaid to be flown correctly this field will contain the pertinent information about that navaid.
   * <br>
   * e.g. for a FA (fix to altitude) this will be the fix the outbound course will be with reference to for climbing to the
   * target altitude.
   */
  Optional<? extends Fix> recommendedNavaid();

  /**
   * For leg types which define arcs as a constant radius turn between fixes this will contain the information about the center
   * of that turn.
   */
  Optional<? extends Fix> centerFix();

  /**
   * The {@link PathTerminator} of the leg, indicating how the leg is to be flown.
   */
  PathTerminator pathTerminator();

  /**
   * The sequence number of the leg. This represents the position of the leg in the procedure/airway which references it.
   * <br>
   * Sorting legs by sequence number within an airway or procedure should produce the full path as intended by the procedure
   * designer.
   */
  int sequenceNumber();

  /**
   * The magnetic course to fly on the current leg - when considering triples of legs this is referred to as the outbound
   * magnetic course.
   * <br>
   * For non-airway legs the inbound magnetic course is taken to be the outbound magnetic course of the previous leg.
   */
  Optional<Double> outboundMagneticCourse();

  /**
   * For legs with a recommended navaid this is the distance in nm to that navaid.
   */
  Optional<Double> rho();

  /**
   * For legs with a recommended navaid this is the magnetic bearing to that navaid.
   */
  Optional<Double> theta();

  /**
   * The Required Navigational Performance (RNP) needed to fly the leg.
   * <br>
   * This represents the maximum distance (in NM) the aircraft should be allowed off the leg if
   */
  Optional<Double> rnp();

  /**
   * The along route distance of flight if required by the leg.
   * <br>
   * This should be the expected flown distance in NM (Nautical Miles) along the leg (if specified).
   */
  Optional<Double> routeDistance();

  /**
   * If the {@link #pathTerminator()} indicates a hold this is the duration of that hold.
   */
  Optional<Duration> holdTime();

  /**
   * The vertical angle identifies the vertical navigation path for the procedure.
   * <br>
   * This should be the angle in degrees of climb or descent in the range (-10., 10.).
   */
  Optional<Double> verticalAngle();

  /**
   * The speed constraint at the {@link Leg#associatedFix()}.
   * <br>
   * The interpretation of this field should be as follows:
   * <br>
   * If {@link Range#hasLowerBound()} equals {@link Range#hasUpperBound()}...
   * <br>
   * 1. Both bounds exist and are equal, implies <i>AT</i>
   * 2. Both bounds exist and are not equal, implies <i>BETWEEN</i>
   * 2. Neither bound exists, implies <i>UNCONSTRAINED</i>
   * <br>
   * If !{@link Range#hasLowerBound()} and {@link Range#hasUpperBound()}, implies <i>AT OR BELOW</i> the upper bound.
   * <br>
   * If {@link Range#hasLowerBound()} and !{@link Range#hasUpperBound()}, implies <i>AT OR ABOVE</i> the lower bound.
   */
  Range<Double> speedConstraint();

  /**
   * The altitude constraint at the {@link Leg#associatedFix()}.
   * <br>
   * Interpretation of constraint should be as in {@link #speedConstraint()}.
   */
  Range<Double> altitudeConstraint();

  /**
   * If the leg contains a turn this indicates the direction of the turn or both if either is viable.
   */
  Optional<TurnDirection> turnDirection();

  /**
   * Indicates whether the terminal fix of the leg is a 'FlyOver' fix..
   * <br>
   * Typically this can be inferred from the waypoint description information in the concrete leg definition (e.g. from ARINC).
   */
  boolean isFlyOverFix();

  /**
   * Indicates whether the terminal fix of the leg is a published holding fix. These are typically encountered at the end of
   * missed approach procedures.
   * <br>
   * Typically this can be inferred from the waypoint description information in the concrete leg definition (e.g. from ARINC).
   */
  boolean isPublishedHoldingFix();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Leg} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Leg {

    private final Fix associatedFix;

    private final Fix recommendedNavaid;

    private final Fix centerFix;

    private final PathTerminator pathTerminator;

    private final int sequenceNumber;

    private final Double outboundMagneticCourse;

    private final Double rho;

    private final Double theta;

    private final Double rnp;

    private final Double routeDistance;

    private final Duration holdTime;

    private final Double verticalAngle;

    private final Range<Double> speedConstraint;

    private final Range<Double> altitudeConstraint;

    private final TurnDirection turnDirection;

    private final boolean isFlyOverFix;

    private final boolean isPublishedHoldingFix;

    private Standard(Builder builder) {
      this.associatedFix = builder.associatedFix;
      this.recommendedNavaid = builder.recommendedNavaid;
      this.centerFix = builder.centerFix;
      this.pathTerminator = requireNonNull(builder.pathTerminator);
      this.sequenceNumber = builder.sequenceNumber;
      this.outboundMagneticCourse = builder.outboundMagneticCourse;
      this.rho = builder.rho;
      this.theta = builder.theta;
      this.rnp = builder.rnp;
      this.routeDistance = builder.routeDistance;
      this.holdTime = builder.holdTime;
      this.verticalAngle = builder.verticalAngle;
      this.speedConstraint = builder.speedConstraint;
      this.altitudeConstraint = builder.altitudeConstraint;
      this.turnDirection = builder.turnDirection;
      this.isFlyOverFix = builder.isFlyOverFix;
      this.isPublishedHoldingFix = builder.isPublishedHoldingFix;
    }

    @Override
    public Optional<Fix> associatedFix() {
      return Optional.ofNullable(associatedFix);
    }

    @Override
    public Optional<Fix> recommendedNavaid() {
      return Optional.ofNullable(recommendedNavaid);
    }

    @Override
    public Optional<Fix> centerFix() {
      return Optional.ofNullable(centerFix);
    }

    @Override
    public PathTerminator pathTerminator() {
      return pathTerminator;
    }

    @Override
    public int sequenceNumber() {
      return sequenceNumber;
    }

    @Override
    public Optional<Double> outboundMagneticCourse() {
      return Optional.ofNullable(outboundMagneticCourse);
    }

    @Override
    public Optional<Double> rho() {
      return Optional.ofNullable(rho);
    }

    @Override
    public Optional<Double> theta() {
      return Optional.ofNullable(theta);
    }

    @Override
    public Optional<Double> rnp() {
      return Optional.ofNullable(rnp);
    }

    @Override
    public Optional<Double> routeDistance() {
      return Optional.ofNullable(routeDistance);
    }

    @Override
    public Optional<Duration> holdTime() {
      return Optional.ofNullable(holdTime);
    }

    @Override
    public Optional<Double> verticalAngle() {
      return Optional.ofNullable(verticalAngle);
    }

    @Override
    public Range<java.lang.Double> speedConstraint() {
      return speedConstraint;
    }

    @Override
    public Range<java.lang.Double> altitudeConstraint() {
      return altitudeConstraint;
    }

    @Override
    public Optional<TurnDirection> turnDirection() {
      return Optional.ofNullable(turnDirection);
    }

    @Override
    public boolean isFlyOverFix() {
      return isFlyOverFix;
    }

    @Override
    public boolean isPublishedHoldingFix() {
      return isPublishedHoldingFix;
    }

    public Builder toBuilder() {
      return new Builder(pathTerminator())
          .associatedFix(associatedFix().orElse(null))
          .recommendedNavaid(recommendedNavaid().orElse(null))
          .centerFix(centerFix().orElse(null))
          .sequenceNumber(sequenceNumber())
          .outboundMagneticCourse(outboundMagneticCourse().orElse(null))
          .rho(rho().orElse(null))
          .theta(theta().orElse(null))
          .rnp(rnp().orElse(null))
          .routeDistance(routeDistance().orElse(null))
          .holdTime(holdTime().orElse(null))
          .verticalAngle(verticalAngle().orElse(null))
          .speedConstraint(speedConstraint())
          .altitudeConstraint(altitudeConstraint())
          .turnDirection(turnDirection().orElse(null))
          .isFlyOverFix(isFlyOverFix())
          .isPublishedHoldingFix(isPublishedHoldingFix());
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Standard standard = (Standard) o;
      return sequenceNumber == standard.sequenceNumber
          && isFlyOverFix == standard.isFlyOverFix
          && isPublishedHoldingFix == standard.isPublishedHoldingFix
          && Objects.equals(associatedFix, standard.associatedFix)
          && Objects.equals(recommendedNavaid, standard.recommendedNavaid)
          && Objects.equals(centerFix, standard.centerFix)
          && pathTerminator == standard.pathTerminator
          && Objects.equals(outboundMagneticCourse, standard.outboundMagneticCourse)
          && Objects.equals(rho, standard.rho) && Objects.equals(theta, standard.theta)
          && Objects.equals(rnp, standard.rnp) && Objects.equals(routeDistance, standard.routeDistance)
          && Objects.equals(holdTime, standard.holdTime)
          && Objects.equals(verticalAngle, standard.verticalAngle)
          && Objects.equals(speedConstraint, standard.speedConstraint)
          && Objects.equals(altitudeConstraint, standard.altitudeConstraint)
          && Objects.equals(turnDirection, standard.turnDirection);
    }

    @Override
    public int hashCode() {
      return Objects.hash(associatedFix, recommendedNavaid, centerFix, pathTerminator, sequenceNumber, outboundMagneticCourse, rho, theta, rnp, routeDistance, holdTime, verticalAngle, speedConstraint, altitudeConstraint, turnDirection, isFlyOverFix, isPublishedHoldingFix);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "associatedFix=" + associatedFix +
          ", recommendedNavaid=" + recommendedNavaid +
          ", centerFix=" + centerFix +
          ", pathTerminator=" + pathTerminator +
          ", sequenceNumber=" + sequenceNumber +
          ", outboundMagneticCourse=" + outboundMagneticCourse +
          ", rho=" + rho +
          ", theta=" + theta +
          ", rnp=" + rnp +
          ", routeDistance=" + routeDistance +
          ", holdTime=" + holdTime +
          ", verticalAngle=" + verticalAngle +
          ", speedConstraint=" + speedConstraint +
          ", altitudeConstraint=" + altitudeConstraint +
          ", turnDirection=" + turnDirection +
          ", isFlyOverFix=" + isFlyOverFix +
          ", isPublishedHoldingFix=" + isPublishedHoldingFix +
          '}';
    }

    public static final class Builder {
      private Fix associatedFix;
      private Fix recommendedNavaid;
      private Fix centerFix;
      private final PathTerminator pathTerminator;
      private Integer sequenceNumber;
      private Double outboundMagneticCourse;
      private Double rho;
      private Double theta;
      private Double rnp;
      private Double routeDistance;
      private Duration holdTime;
      private Double verticalAngle;
      private Range<java.lang.Double> speedConstraint;
      private Range<java.lang.Double> altitudeConstraint;
      private TurnDirection turnDirection;
      private boolean isFlyOverFix;
      private boolean isPublishedHoldingFix;

      private Builder(PathTerminator pathTerminator) {
        this.pathTerminator = requireNonNull(pathTerminator);
      }

      public Builder associatedFix(Fix associatedFix) {
        this.associatedFix = associatedFix;
        return this;
      }

      public Builder recommendedNavaid(Fix recommendedNavaid) {
        this.recommendedNavaid = recommendedNavaid;
        return this;
      }

      public Builder centerFix(Fix centerFix) {
        this.centerFix = centerFix;
        return this;
      }

      public Builder sequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
      }

      public Builder outboundMagneticCourse(Double outboundMagneticCourse) {
        this.outboundMagneticCourse = outboundMagneticCourse;
        return this;
      }

      public Builder rho(Double rho) {
        this.rho = rho;
        return this;
      }

      public Builder theta(Double theta) {
        this.theta = theta;
        return this;
      }

      public Builder rnp(Double rnp) {
        this.rnp = rnp;
        return this;
      }

      public Builder routeDistance(Double routeDistance) {
        this.routeDistance = routeDistance;
        return this;
      }

      public Builder holdTime(Duration holdTime) {
        this.holdTime = holdTime;
        return this;
      }

      public Builder verticalAngle(Double verticalAngle) {
        this.verticalAngle = verticalAngle;
        return this;
      }

      public Builder speedConstraint(Range<java.lang.Double> speedConstraint) {
        this.speedConstraint = speedConstraint;
        return this;
      }

      public Builder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
        this.altitudeConstraint = altitudeConstraint;
        return this;
      }

      public Builder turnDirection(TurnDirection turnDirection) {
        this.turnDirection = turnDirection;
        return this;
      }

      public Builder isFlyOverFix(boolean isFlyOverFix) {
        this.isFlyOverFix = isFlyOverFix;
        return this;
      }

      public Builder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
        this.isPublishedHoldingFix = isPublishedHoldingFix;
        return this;
      }

      public Standard build() {
        requireNonNull(pathTerminator, "Required: PathTerminator");
        requireNonNull(sequenceNumber, "Required: SequenceNumber");
        checkArgument(sequenceNumber >= 0, "SequenceNumber should be >= 0.");
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Leg {

    private final T datum;

    private final Leg delegate;

    private Record(T datum, Leg delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public Optional<? extends Fix> associatedFix() {
      return delegate.associatedFix();
    }

    @Override
    public Optional<? extends Fix> recommendedNavaid() {
      return delegate.recommendedNavaid();
    }

    @Override
    public Optional<? extends Fix> centerFix() {
      return delegate.centerFix();
    }

    @Override
    public PathTerminator pathTerminator() {
      return delegate.pathTerminator();
    }

    @Override
    public int sequenceNumber() {
      return delegate.sequenceNumber();
    }

    @Override
    public Optional<Double> outboundMagneticCourse() {
      return delegate.outboundMagneticCourse();
    }

    @Override
    public Optional<Double> rho() {
      return delegate.rho();
    }

    @Override
    public Optional<Double> theta() {
      return delegate.theta();
    }

    @Override
    public Optional<Double> rnp() {
      return delegate.rnp();
    }

    @Override
    public Optional<Double> routeDistance() {
      return delegate.routeDistance();
    }

    @Override
    public Optional<Duration> holdTime() {
      return delegate.holdTime();
    }

    @Override
    public Optional<Double> verticalAngle() {
      return delegate.verticalAngle();
    }

    @Override
    public Range<Double> speedConstraint() {
      return delegate.speedConstraint();
    }

    @Override
    public Range<Double> altitudeConstraint() {
      return delegate.altitudeConstraint();
    }

    @Override
    public Optional<TurnDirection> turnDirection() {
      return delegate.turnDirection();
    }

    @Override
    public boolean isFlyOverFix() {
      return delegate.isFlyOverFix();
    }

    @Override
    public boolean isPublishedHoldingFix() {
      return delegate.isPublishedHoldingFix();
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Record<?> record = (Record<?>) o;
      return Objects.equals(datum, record.datum)
          && Objects.equals(delegate, record.delegate);
    }

    @Override
    public int hashCode() {
      return Objects.hash(datum, delegate);
    }

    @Override
    public String toString() {
      return "Record{" +
          "datum=" + datum +
          ", delegate=" + delegate +
          '}';
    }
  }

  /**
   * Specific subclass of {@link RuntimeException} for handling missing required fields in {@link PathTerminator}-specific views
   * of {@link Leg}s.
   */
  final class MissingRequiredFieldException extends RuntimeException {

    private MissingRequiredFieldException(String fieldName) {
      super("Missing required field: " + fieldName);
    }
  }

  final class AfBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.AF);

    private AfBuilder() {
    }

    public AfBuilder associatedFix(Fix associatedFix) {
      this.delegate.associatedFix(requireNonNull(associatedFix));
      return this;
    }

    public AfBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(requireNonNull(recommendedNavaid));
      return this;
    }

    public AfBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public AfBuilder outboundMagneticCourse(double outboundMagneticCourse) {
      this.delegate.outboundMagneticCourse(outboundMagneticCourse);
      return this;
    }

    public AfBuilder rho(double rho) {
      this.delegate.rho(rho);
      return this;
    }

    public AfBuilder theta(double theta) {
      this.delegate.theta(theta);
      return this;
    }

    public AfBuilder altitudeConstraint(Range<Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public AfBuilder speedConstraint(Range<Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public AfBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.associatedFix, "Required: Associated Fix");
      requireNonNull(delegate.recommendedNavaid, "Required: Recommended Navaid");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      requireNonNull(delegate.outboundMagneticCourse, "Required: Outbound Magnetic Course");
      requireNonNull(delegate.theta, "Required: Theta");
      requireNonNull(delegate.rho, "Required: Rho");
      requireNonNull(delegate.turnDirection, "Required: TurnDirection");
      return delegate.build();
    }
  }

  final class CaBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.CA);

    private CaBuilder() {
    }

    public CaBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public CaBuilder outboundMagneticCourse(double outboundMagneticCourse) {
      this.delegate.outboundMagneticCourse(outboundMagneticCourse);
      return this;
    }

    public CaBuilder altitudeConstraint(Range<Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public CaBuilder speedConstraint(Range<Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public CaBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      requireNonNull(delegate.outboundMagneticCourse, "Required: Outbound Magnetic Course");
      requireNonNull(delegate.altitudeConstraint, "Required: Altitude Constraint");
      return delegate.build();
    }
  }

  final class CdBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.CD);

    private CdBuilder() {
    }

    public CdBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(requireNonNull(recommendedNavaid));
      return this;
    }

    public CdBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public CdBuilder outboundMagneticCourse(double outboundMagneticCourse) {
      this.delegate.outboundMagneticCourse(outboundMagneticCourse);
      return this;
    }

    public CdBuilder routeDistance(double routeDistance) {
      this.delegate.routeDistance(routeDistance);
      return this;
    }

    public CdBuilder altitudeConstraint(Range<Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public CdBuilder speedConstraint(Range<Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public CdBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.recommendedNavaid, "Required: Recommended Navaid");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      requireNonNull(delegate.outboundMagneticCourse, "Required: Outbound Magnetic Course");
      requireNonNull(delegate.routeDistance, "Required: Route Distance");
      return delegate.build();
    }
  }


  final class CfBuilder {
    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.CF);

    private CfBuilder() {

    }

    public CfBuilder associatedFix(Fix associatedFix) {
      this.delegate.associatedFix(requireNonNull(associatedFix));
      return this;
    }

    public CfBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(recommendedNavaid);
      return this;
    }

    public CfBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public CfBuilder outboundMagneticCourse(Double outboundMagneticCourse) {
      this.delegate.outboundMagneticCourse(outboundMagneticCourse);
      return this;
    }

    public CfBuilder rho(Double rho) {
      this.delegate.rho(rho);
      return this;
    }

    public CfBuilder theta(Double theta) {
      this.delegate.theta(theta);
      return this;
    }

    public CfBuilder rnp(Double rnp) {
      this.delegate.rnp(rnp);
      return this;
    }

    public CfBuilder routeDistance(Double routeDistance) {
      this.delegate.routeDistance(routeDistance);
      return this;
    }

    public CfBuilder holdTime(Duration holdTime) {
      this.delegate.holdTime(holdTime);
      return this;
    }

    public CfBuilder verticalAngle(Double verticalAngle) {
      this.delegate.verticalAngle(verticalAngle);
      return this;
    }

    public CfBuilder speedConstraint(Range<java.lang.Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public CfBuilder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public CfBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public CfBuilder isFlyOverFix(boolean isFlyOverFix) {
      this.delegate.isFlyOverFix(isFlyOverFix);
      return this;
    }

    public CfBuilder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
      this.delegate.isPublishedHoldingFix(isPublishedHoldingFix);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.associatedFix, "Required: Associated Fix");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      requireNonNull(delegate.outboundMagneticCourse, "Required: Outbound Magnetic Course");
      return delegate.build();
    }
  }

  final class IfBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.IF);

    private IfBuilder() {
    }

    public IfBuilder associatedFix(Fix associatedFix) {
      this.delegate.associatedFix(requireNonNull(associatedFix));
      return this;
    }

    public IfBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(recommendedNavaid);
      return this;
    }

    public IfBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public IfBuilder rho(Double rho) {
      this.delegate.rho(rho);
      return this;
    }

    public IfBuilder theta(Double theta) {
      this.delegate.theta(theta);
      return this;
    }

    public IfBuilder rnp(Double rnp) {
      this.delegate.rnp(rnp);
      return this;
    }

    public IfBuilder speedConstraint(Range<java.lang.Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public IfBuilder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public IfBuilder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
      this.delegate.isPublishedHoldingFix(isPublishedHoldingFix);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.associatedFix, "Required: Associated Fix");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      return delegate.build();
    }
  }

  final class DfBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.DF);

    private DfBuilder() {
    }

    public DfBuilder associatedFix(Fix associatedFix) {
      this.delegate.associatedFix(requireNonNull(associatedFix));
      return this;
    }

    public DfBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(recommendedNavaid);
      return this;
    }

    public DfBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public DfBuilder rho(Double rho) {
      this.delegate.rho(rho);
      return this;
    }

    public DfBuilder theta(Double theta) {
      this.delegate.theta(theta);
      return this;
    }

    public DfBuilder rnp(Double rnp) {
      this.delegate.rnp(rnp);
      return this;
    }

    public DfBuilder speedConstraint(Range<java.lang.Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public DfBuilder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public DfBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public DfBuilder isFlyOverFix(boolean isFlyOverFix) {
      this.delegate.isFlyOverFix(isFlyOverFix);
      return this;
    }

    public DfBuilder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
      this.delegate.isPublishedHoldingFix(isPublishedHoldingFix);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.associatedFix, "Required: Associated Fix");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      return delegate.build();
    }
  }

  final class TfBuilder {

    private final Standard.Builder delegate = new Standard.Builder(PathTerminator.TF);

    private TfBuilder() {
    }

    public TfBuilder associatedFix(Fix associatedFix) {
      this.delegate.associatedFix(requireNonNull(associatedFix));
      return this;
    }

    public TfBuilder recommendedNavaid(Fix recommendedNavaid) {
      this.delegate.recommendedNavaid(recommendedNavaid);
      return this;
    }

    public TfBuilder sequenceNumber(int sequenceNumber) {
      this.delegate.sequenceNumber(sequenceNumber);
      return this;
    }

    public TfBuilder outboundMagneticCourse(Double outboundMagneticCourse) {
      this.delegate.outboundMagneticCourse(outboundMagneticCourse);
      return this;
    }

    public TfBuilder rho(Double rho) {
      this.delegate.rho(rho);
      return this;
    }

    public TfBuilder theta(Double theta) {
      this.delegate.theta(theta);
      return this;
    }

    public TfBuilder rnp(Double rnp) {
      this.delegate.rnp(rnp);
      return this;
    }

    public TfBuilder routeDistance(Double routeDistance) {
      this.delegate.routeDistance(routeDistance);
      return this;
    }

    public TfBuilder holdTime(Duration holdTime) {
      this.delegate.holdTime(holdTime);
      return this;
    }

    public TfBuilder verticalAngle(Double verticalAngle) {
      this.delegate.verticalAngle(verticalAngle);
      return this;
    }

    public TfBuilder speedConstraint(Range<java.lang.Double> speedConstraint) {
      this.delegate.speedConstraint(speedConstraint);
      return this;
    }

    public TfBuilder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
      this.delegate.altitudeConstraint(altitudeConstraint);
      return this;
    }

    public TfBuilder turnDirection(TurnDirection turnDirection) {
      this.delegate.turnDirection(turnDirection);
      return this;
    }

    public TfBuilder isFlyOverFix(boolean isFlyOverFix) {
      this.delegate.isFlyOverFix(isFlyOverFix);
      return this;
    }

    public TfBuilder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
      this.delegate.isPublishedHoldingFix(isPublishedHoldingFix);
      return this;
    }

    public Standard build() {
      requireNonNull(delegate.associatedFix, "Required: Associated Fix");
      requireNonNull(delegate.sequenceNumber, "Required: Sequence Number");
      return delegate.build();
    }
  }
}
