package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * Wrapper for likelihood values, stored internally as log-likelihoods to prevent Double underflow.
 */
public final class Likelihood implements Comparable<Likelihood> {
  private final double l;

  private Likelihood(double l) {
    this.l = l;
  }

  public double logLikelihood() {
    return l;
  }

  public Likelihood times(Likelihood that) {
    return that == null ? null : new Likelihood(this.l + that.l);
  }

  public Likelihood times(Double that) {
    return times(valueOf(that));
  }

  public boolean isLessThan(Likelihood that) {
    return this.l < that.l;
  }

  public boolean isGreaterThan(Likelihood that) {
    return this.l > that.l;
  }

  public static Likelihood valueOf(double d) {
    Preconditions.checkArgument(0.0 <= d && d <= 1.0, "Likelihood values must be between 0.0 and 1.0");
    return new Likelihood(Math.log(d));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Likelihood that = (Likelihood) o;
    return Double.compare(that.l, l) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(l);
  }

  @Override
  public int compareTo(Likelihood that) {
    return Double.compare(this.l, that.l);
  }

}
