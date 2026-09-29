package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Joins leg spans into continuous visits for one trajectory on one thread. Submit every processed
 * leg, including legs with no spans, in time order. Within each airspace, spans must be in travel
 * order and must not overlap, as emitted by {@link AirspaceQuery}.
 * The mapped positions and altitudes must match those supplied to that query.
 *
 * <p>Spans join across consecutive legs only at fractions one and zero and only when the mapped
 * shared observation is equal. Disconnected legs close existing visits. Call {@link #finish()} at
 * trajectory boundaries and before skipping an untrusted time gap; gap thresholds are caller policy.
 *
 * <p>Completed visits are sent synchronously to the consumer. Only active visits and their original
 * events are retained. Memory therefore grows with the duration and number of overlapping active
 * visits. Callback or processing failures propagate and clear the accumulator's retained state.
 *
 * @param <K> caller's airspace key type
 * @param <E> caller's original event type
 */
public final class AirspaceVisitAccumulator<K, E> {
  private final Function<? super E, AirspacePoint> pointMapping;
  private final Consumer<AirspaceVisit<K, E>> output;
  private final Map<K, Pending<E>> active = new LinkedHashMap<>();
  private AirspacePoint previousEnd;
  private long legNumber;
  private boolean processing;

  /**
   * Stores the event adapter and completed-visit consumer.
   *
   * @param pointMapping maps original events to time, position, and nullable altitude in the bands' reference
   * @param output receives completed visits synchronously
   */
  public AirspaceVisitAccumulator(Function<? super E, AirspacePoint> pointMapping, Consumer<AirspaceVisit<K, E>> output) {
    this.pointMapping = requireNonNull(pointMapping, "pointMapping");
    this.output = requireNonNull(output, "output");
  }

  /**
   * Incorporates one leg's complete query results. The span collection can be cleared or reused after
   * this call. Shared observations are included once in a visit's original-event list.
   *
   * @param start original starting event
   * @param end original ending event, strictly later than start
   * @param spans every span for this leg, including an empty collection when none were emitted
   * @throws IllegalArgumentException if legs are out of time order, time does not advance, or spans overlap or are out of order
   * @throws IllegalStateException if a callback recursively invokes this accumulator
   */
  public void accept(E start, E end, Collection<AirspaceSpan<K>> spans) {
    begin();
    try {
      requireNonNull(spans, "spans");
      AirspacePoint a = requireNonNull(pointMapping.apply(requireNonNull(start, "start")), "starting point");
      AirspacePoint b = requireNonNull(pointMapping.apply(requireNonNull(end, "end")), "ending point");
      checkArgument(b.time().isAfter(a.time()), "Leg end must be later than its start");
      checkArgument(previousEnd == null || !a.time().isBefore(previousEnd.time()), "Legs must be supplied in time order");
      if (previousEnd != null && !previousEnd.equals(a)) {
        flush();
      }
      legNumber++;
      for (AirspaceSpan<K> span : spans) {
        incorporate(start, end, a, b, requireNonNull(span, "span"));
      }
      completeLeg();
      previousEnd = b;
    } catch (RuntimeException | Error failure) {
      reset();
      throw failure;
    } finally {
      processing = false;
    }
  }

  /**
   * Emits any remaining visits at their last covered observation, with {@code exitCrossing == false},
   * and resets state for the next trajectory. Safe to call again when no visits remain.
   *
   * @throws IllegalStateException if a callback recursively invokes this accumulator
   */
  public void finish() {
    begin();
    try {
      flush();
    } finally {
      reset();
      processing = false;
    }
  }

  private void incorporate(E start, E end, AirspacePoint a, AirspacePoint b, AirspaceSpan<K> span) {
    K key = span.airspaceKey();
    Pending<E> visit = active.get(key);
    boolean sameLeg = visit != null && visit.legNumber == legNumber;
    if (sameLeg) {
      checkArgument(span.enterFraction() >= visit.exitFraction, "Spans overlap or are out of order for airspace %s", key);
    }
    boolean joins = visit != null && span.enterFraction() == (sameLeg ? visit.exitFraction : 0.0);
    if (visit != null && !joins) {
      active.remove(key);
      emit(key, visit, visit.exitFraction < 1.0);
      visit = null;
    }
    if (visit == null) {
      visit = new Pending<>(AirspacePointInterpolator.INSTANCE.apply(a, b, span.enterFraction()), span.enterFraction() > 0.0);
      if (span.enterFraction() == 0.0) {
        visit.events.add(start);
      }
      active.put(key, visit);
    }
    visit.exit = AirspacePointInterpolator.INSTANCE.apply(a, b, span.exitFraction());
    visit.exitFraction = span.exitFraction();
    visit.legNumber = legNumber;
    visit.altitudeChecked &= span.altitudeChecked();
    if (span.exitFraction() == 1.0) {
      visit.events.add(end);
    }
  }

  private void completeLeg() {
    Iterator<Map.Entry<K, Pending<E>>> iterator = active.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<K, Pending<E>> entry = iterator.next();
      Pending<E> visit = entry.getValue();
      if (visit.legNumber != legNumber || visit.exitFraction < 1.0) {
        iterator.remove();
        emit(entry.getKey(), visit, visit.legNumber == legNumber && visit.exitFraction < 1.0);
      }
    }
  }

  private void flush() {
    Iterator<Map.Entry<K, Pending<E>>> iterator = active.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<K, Pending<E>> entry = iterator.next();
      iterator.remove();
      emit(entry.getKey(), entry.getValue(), false);
    }
  }

  private void emit(K key, Pending<E> visit, boolean exitCrossing) {
    output.accept(new AirspaceVisit<>(key, visit.entry, visit.exit, List.copyOf(visit.events),
        visit.entryCrossing, exitCrossing, visit.altitudeChecked));
  }

  private void begin() {
    if (processing) {
      throw new IllegalStateException("AirspaceVisitAccumulator cannot be called recursively");
    }
    processing = true;
  }

  private void reset() {
    active.clear();
    previousEnd = null;
    legNumber = 0;
  }

  private static final class Pending<E> {
    private final AirspacePoint entry;
    private final boolean entryCrossing;
    private final List<E> events = new ArrayList<>();
    private AirspacePoint exit;
    private double exitFraction;
    private long legNumber;
    private boolean altitudeChecked = true;

    private Pending(AirspacePoint entry, boolean entryCrossing) {
      this.entry = entry;
      this.entryCrossing = entryCrossing;
    }
  }
}
