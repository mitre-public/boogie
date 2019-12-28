package org.mitre.tdp.boogie.conformance;

import java.util.List;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

/**
 * Packaged class for the score output of the conformance algorithm.
 */
public class Scores {
  /**
   * The score that was the minimum at the time according to the dynamic programmer.
   * I.e. this is the sum of the stage scores plus the transition ones.
   */
  private final double[] cumulative;
  /**
   * The cross track distance between the leg and the object at every point in time
   * where the track was considered to be conforming to the leg.
   */
  private final double[] scores;
  /**
   * Times where the leg was considered to be conformed to.
   */
  private final long[] times;

  private Scores(Builder bldr) {
    this.cumulative = bldr.cumul;
    this.scores = bldr.scores;
    this.times = bldr.times;
  }

  public double[] cumulative() {
    return cumulative;
  }

  public double cumulative(int i) {
    return cumulative[i];
  }

  public double[] scores() {
    return scores;
  }

  public double cross(int i) {
    return scores[i];
  }

  public long[] times() {
    return times;
  }

  public long time(int i) {
    return times[i];
  }

  public static class Builder {
    private double[] cumul;
    private double[] scores;
    private long[] times;

    public Builder setCumulative(double[] c) {
      this.cumul = c;
      return this;
    }

    public Builder setCumulative(List<Double> c) {
      return setCumulative(Doubles.toArray(c));
    }

    public Builder setScores(double[] c) {
      this.scores = c;
      return this;
    }

    public Builder setScores(List<Double> c) {
      return setScores(Doubles.toArray(c));
    }

    public Builder setTimes(long[] t) {
      this.times = t;
      return this;
    }

    public Builder setTimes(List<Long> t) {
      return setTimes(Longs.toArray(t));
    }

    public Scores build() {
      return new Scores(this);
    }
  }
}