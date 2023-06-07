package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

public interface Runway {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Runway delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Runway> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * The identifier of the runway.
   *
   * <p>Generally runway names are based on the magnetic heading of the runway (in tens of degrees). I.e. RW27R is aligned with
   * heading {@code 270deg}.
   */
  String runwayIdentifier();

  /**
   * The "origin" of the runway.
   *
   * <p>The origin (O) is initial point of the runway vector: {@code |O---->| 270deg}. So departures start at the origin and take
   * off along the runway away from it (going in the direction of the runway heading).
   *
   * <p>Arrivals arrive over the origin and touch down along the runway heading in the direction of the runways heading. I.e. the
   * runway heading is the heading the aircraft are on during landing and takeoff.
   *
   * <p>This when combined with the {@link #length()} should represent the "effective" extents of the runway available for usage
   * by aircraft taking off/touching down (and therefore may reflect any displaced thresholds).
   */
  LatLong origin();

  /**
   * The total "effective" length of the runway available for flights to use during takeoff and landing.
   *
   * <p>Boogie reserves the right to use this field to determine which runways are candidates for arrival/departure of certain
   * classes of aircraft.
   *
   * <p>This field is left optional to support varied types of things which show up as "runways" in various data sources (lakes
   * for seaplane bases in CIFP...). Clients are free to default this behind the interface but the framework can defer decisions
   * about what to do if we don't know the length till later.
   */
  Optional<Distance> length();

  /**
   * The <i>true</i> course of the runway (where it's pointing).
   *
   * <p>This field is left optional to support varied types of things which show up as "runways" in various data sources (lakes
   * for seaplane bases in CIFP...). Clients are free to default this behind the interface but the framework can defer decisions
   * about what to do if we don't know the length till later.
   */
  Optional<Course> course();

  final class Standard implements Runway {

    private final String runwayIdentifier;

    private final LatLong origin;

    private final Distance length;

    private final Course course;

    private Standard(Builder builder) {
      this.runwayIdentifier = requireNonNull(builder.runwayIdentifier);
      this.origin = requireNonNull(builder.origin);
      this.length = requireNonNull(builder.length);
      this.course = requireNonNull(builder.course);
    }

    @Override
    public String runwayIdentifier() {
      return runwayIdentifier;
    }

    @Override
    public LatLong origin() {
      return origin;
    }

    @Override
    public Optional<Distance> length() {
      return ofNullable(length);
    }

    @Override
    public Optional<Course> course() {
      return ofNullable(course);
    }

    public Builder toBuilder() {
      return builder()
          .runwayIdentifier(runwayIdentifier())
          .origin(origin())
          .length(length().orElse(null))
          .course(course().orElse(null));
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
      return Objects.equals(runwayIdentifier, standard.runwayIdentifier)
          && Objects.equals(origin, standard.origin)
          && Objects.equals(length, standard.length)
          && Objects.equals(course, standard.course);
    }

    @Override
    public int hashCode() {
      return Objects.hash(runwayIdentifier, origin, length, course);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "runwayIdentifier='" + runwayIdentifier + '\'' +
          ", origin=" + origin +
          ", length=" + length +
          ", course=" + course +
          '}';
    }

    public static final class Builder {
      private String runwayIdentifier;

      private LatLong origin;

      private Distance length;

      private Course course;

      private Builder() {
      }

      public Standard build() {
        return new Standard(this);
      }

      public Builder runwayIdentifier(String runwayIdentifier) {
        this.runwayIdentifier = runwayIdentifier;
        return this;
      }

      public Builder origin(LatLong origin) {
        this.origin = origin;
        return this;
      }

      public Builder length(Distance length) {
        this.length = length;
        return this;
      }

      public Builder course(Course course) {
        this.course = course;
        return this;
      }
    }
  }

  final class Record<T> implements Runway {

    private final T datum;

    private final Runway delegate;

    private Record(T datum, Runway delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String runwayIdentifier() {
      return delegate.runwayIdentifier();
    }

    @Override
    public LatLong origin() {
      return delegate.origin();
    }

    @Override
    public Optional<Distance> length() {
      return delegate.length();
    }

    @Override
    public Optional<Course> course() {
      return delegate.course();
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
}
