package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.LegTransitionScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * High-level interface for a leg which should be considered "flyable". This means based on the definition of the leg we
 * have a notional understanding of what it would look like were an aircraft to be flying the leg.
 *
 * Think of flyable as a more relaxed version of conformable - conformance to legs tends to be directly tied to deviations
 * between the 2D path of the leg and the flown path of the aircraft - flyable relaxes this allowing us to assign scores
 * to legs of less explicit types (e.g. VA, CA, VI, VI) and gives us more flexibility when it comes to deciding when aircraft
 * were flying these less concrete leg types.
 *
 * This leg is centered around tuples of {@link Leg} objects as in leg definitions for the majority of leg types the features
 * which collectively define how the leg is to be flown are often contained in the flown leg - as well as the previous leg -
 * and also less commonly in the following leg (i.e. VI/CI legs, which are fly-till-you-intercept-the-next-leg-legs).
 */
public class FlyableLeg {

  private final Leg previous;
  private final Leg current;
  private final Leg next;

  /**
   * The configurable {@link OnLegScorer} to use when scoring this flyable leg.
   */
  private OnLegScorer onLegScorer;
  /**
   * The leg-to-leg transition scorer for transitions from this flyable leg.
   */
  private LegTransitionScorer legTransitionScorer;

  /**
   * The object which should be considered the source for the legs within the flyable leg.
   */
  private Object sourceObject;

  /**
   * Generates a new simple tuple of {@link FlyableLeg}.
   */
  public FlyableLeg(Leg p, @Nonnull Leg c, Leg n) {
    this.previous = p;
    this.current = c;
    this.next = n;
    this.onLegScorer = null;
    this.legTransitionScorer = null;
  }

  public Optional<Leg> previous() {
    return Optional.ofNullable(previous);
  }

  public <T> Optional<T> previous(Function<Leg, Optional<T>> ext) {
    return previous().flatMap(ext);
  }

  public Leg current() {
    return current;
  }

  public Optional<Leg> next() {
    return Optional.ofNullable(next);
  }

  public <T> Optional<T> next(Function<Leg, Optional<T>> ext) {
    return next().flatMap(ext);
  }

  public OnLegScorer onLegScorer() {
    return onLegScorer;
  }

  public FlyableLeg setOnLegScorer(OnLegScorer onLegScorer) {
    this.onLegScorer = onLegScorer;
    return this;
  }

  public LegTransitionScorer legTransitionScorer() {
    return legTransitionScorer;
  }

  public FlyableLeg setLegTransitionScorer(LegTransitionScorer legTransitionScorer) {
    this.legTransitionScorer = legTransitionScorer;
    return this;
  }

  public Optional<Object> getSourceObject() {
    return Optional.ofNullable(sourceObject);
  }

  public FlyableLeg setSourceObject(Object o) {
    this.sourceObject = o;
    return this;
  }

  public static FlyableLeg copyOf(FlyableLeg flyableLeg) {
    return new FlyableLeg(
        flyableLeg.previous().orElse(null),
        flyableLeg.current(),
        flyableLeg.next().orElse(null))
        .setOnLegScorer(flyableLeg.onLegScorer())
        .setLegTransitionScorer(flyableLeg.legTransitionScorer())
        .setSourceObject(flyableLeg.getSourceObject());
  }

  @Override
  public String toString() {
    return "FlyableLeg{" +
        "previous=" + previous +
        ", current=" + current +
        ", next=" + next +
        ", sourceObject=" + sourceObject +
        '}';
  }
}
