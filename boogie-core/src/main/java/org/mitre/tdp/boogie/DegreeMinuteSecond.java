package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.IntFunction;

import org.mitre.tdp.boogie.util.CoordinateParser;

/**
 * Represents a position in a coordinate system as a degree-hour-minute value.
 *
 * <p>This class is <i>loosely</i> modeled after {@link Instant} and {@link DateTimeFormatter}.
 *
 * <p>TODO - think more about this when we have time (how to handle {@link CoordinateParser} and different coordinate standards).
 */
public final class DegreeMinuteSecond {

  private final int degrees;

  private final int minutes;

  private final int seconds;

  private DegreeMinuteSecond(int degrees, int minutes, int seconds) {
    this.degrees = degrees;
    this.minutes = minutes;
    this.seconds = seconds;
    checkArgument(abs(minutes) <= 60, "At most 60 minutes to a degree.");
    checkArgument(abs(seconds) <= 60, "At most 60 seconds to a minute.");
  }

//  /**
//   * Coerce a decimal coordinate value to a DMS value, this may be a lossy translation depending on the precision of the provided
//   * decimal coordinate.
//   */
//  public static DegreeMinuteSecond from(double decimalValue) {
//    return
//  }

  public int degrees() {
    return degrees;
  }

  public int minutes() {
    return minutes;
  }

  public int seconds() {
    return seconds;
  }

  public double decimalValue() {
    int sign = degrees < 0 ? -1 : 1;
    return sign * (abs(degrees) + (minutes / 60.) + (seconds / 60.));
  }

  public String format(Format format) {
    return format.format(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DegreeMinuteSecond that = (DegreeMinuteSecond) o;
    return degrees == that.degrees && minutes == that.minutes && seconds == that.seconds;
  }

  @Override
  public int hashCode() {
    return Objects.hash(degrees, minutes, seconds);
  }

  @Override
  public String toString() {
    return "DegreeMinuteSecond{" +
        "degrees=" + degrees +
        ", minutes=" + minutes +
        ", seconds=" + seconds +
        '}';
  }

  /**
   * Represents a canonical string format for the degree-minutes-seconds formatted coordinate.
   */
  public interface Format {

    static Format degrees() {
      return new Piecemeal().degreesFormat(degrees -> String.format("%02d", degrees)).toFormat();
    }

    static Format degreesMinutes() {
      return new Piecemeal()
          .degreesFormat(degrees -> String.format("%02d", degrees))
          .minutesFormat(minutes -> String.format("%02d", minutes))
          .toFormat();
    }

    static Format full() {
      return new Piecemeal()
          .degreesFormat(degrees -> String.format("%02d", degrees))
          .minutesFormat(minutes -> String.format("%02d", minutes))
          .secondsFormat(seconds -> String.format("%02d", seconds))
          .toFormat();
    }

    /**
     * Converts the input DMS value to a string format.
     */
    String format(DegreeMinuteSecond dms);

    final class Piecemeal {

      private IntFunction<String> degreesFormat;

      private IntFunction<String> minutesFormat = i -> "";

      private IntFunction<String> secondsFormat = i -> "";

      private Piecemeal() {
      }

      public Piecemeal degreesFormat(IntFunction<String> degreesFormat) {
        this.degreesFormat = requireNonNull(degreesFormat);
        return this;
      }

      public Piecemeal minutesFormat(IntFunction<String> minutesFormat) {
        this.minutesFormat = requireNonNull(minutesFormat);
        return this;
      }

      public Piecemeal secondsFormat(IntFunction<String> secondsFormat) {
        this.secondsFormat = requireNonNull(secondsFormat);
        return this;
      }

      public Format toFormat() {
        return dms -> new StringBuilder()
            .append(degreesFormat.apply(dms.degrees()))
            .append(minutesFormat.apply(dms.minutes()))
            .append(secondsFormat.apply(dms.seconds()))
            .toString();
      }
    }
  }
}
