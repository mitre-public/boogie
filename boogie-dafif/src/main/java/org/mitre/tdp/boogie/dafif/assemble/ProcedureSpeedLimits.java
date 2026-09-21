package org.mitre.tdp.boogie.dafif.assemble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Applies DAFIF field 295's SID-backward and STAR/approach-forward speed rules. */
public final class ProcedureSpeedLimits {
  private static final Logger LOG = LoggerFactory.getLogger(ProcedureSpeedLimits.class);

  private ProcedureSpeedLimits() {
  }

  /** A published limit, including the conditions which the core numeric range cannot represent. */
  public record Limit(int number, double knots, @Nullable String aircraft, @Nullable String belowAltitude) {
    public boolean isUnqualified() {
      return belowAltitude == null && (aircraft == null || "A".equals(aircraft));
    }
  }

  public static List<Limit> published(DafifTerminalSegment segment) {
    List<Limit> limits = new ArrayList<>(2);
    segment.speedLimit1().ifPresent(speed -> limits.add(new Limit(1, speed,
        segment.speedLimitAircraftType1().orElse(null), segment.speedLimitAltitude1().orElse(null))));
    segment.speedLimit2().ifPresent(speed -> limits.add(new Limit(2, speed,
        segment.speedLimitAircraftType2().orElse(null), segment.speedLimitAltitude2().orElse(null))));
    return List.copyOf(limits);
  }

  /**
   * Resolves limits without modifying the published records. Transitions connect only at matching fixes and in flight order.
   * Holding-pattern limits apply only to the holding leg. At a shared transition, conflicting incoming branch limits cannot be
   * represented by one set of fix constraints; only a limit common to every incoming branch is inherited.
   */
  public static Map<DafifTerminalSegment, List<Limit>> resolve(Collection<DafifTerminalSegment> segments) {
    Map<Key, List<DafifTerminalSegment>> groups = new LinkedHashMap<>();
    for (DafifTerminalSegment segment : segments) {
      Key key = new Key(segment.airportIdentification(), segment.terminalProcedureType(), segment.terminalIdentifier(),
          segment.terminalApproachType(), segment.transitionIdentifier().orElse(null));
      groups.computeIfAbsent(key, ignored -> new ArrayList<>()).add(segment);
    }
    List<Route> routes = groups.entrySet().stream().map(entry -> {
      entry.getValue().sort(Comparator.comparingInt(DafifTerminalSegment::terminalSequenceNumber));
      return new Route(entry.getKey(), entry.getValue());
    }).sorted(Comparator.comparingInt(Route::order)).toList();

    Map<Route, List<Limit>> exitLimits = new HashMap<>();
    Map<DafifTerminalSegment, List<Limit>> result = new HashMap<>();
    for (Route route : routes) {
      List<List<Limit>> incoming = routes.stream().filter(previous -> previous.connectsTo(route))
          // A common route supersedes the entry/exit transition; do not count its ancestor as another branch.
          .filter(previous -> routes.stream().noneMatch(between -> previous.connectsTo(between) && between.connectsTo(route)))
          .map(previous -> exitLimits.getOrDefault(previous, List.of())).toList();
      List<Limit> active = sharedLimits(incoming, route);
      for (int step = 0; step < route.segments().size(); step++) {
        int index = route.key().procedureType() == 2 ? route.segments().size() - 1 - step : step;
        DafifTerminalSegment segment = route.segments().get(index);
        List<Limit> own = published(segment);
        if (List.of("HA", "HF", "HM").contains(segment.trackDescriptionCode())) {
          result.put(segment, own.isEmpty() ? active : own);
        } else {
          if (!own.isEmpty()) {
            active = own;
          }
          result.put(segment, active);
        }
      }
      exitLimits.put(route, active);
    }
    return Map.copyOf(result);
  }

  private static List<Limit> sharedLimits(List<List<Limit>> incoming, Route route) {
    if (incoming.isEmpty()) {
      return List.of();
    }
    List<Limit> shared = incoming.get(0).stream()
        .filter(limit -> incoming.stream().allMatch(limits -> limits.stream().anyMatch(other -> equivalent(limit, other))))
        .toList();
    if (incoming.stream().anyMatch(limits -> limits.size() != shared.size())) {
      LOG.debug("Branch-specific speed limits cannot be inherited by shared DAFIF transition {}", route.key());
    }
    return shared;
  }

  private static boolean equivalent(Limit left, Limit right) {
    return left.knots() == right.knots() && Objects.requireNonNullElse(left.aircraft(), "A").equals(Objects.requireNonNullElse(right.aircraft(), "A"))
        && Objects.equals(left.belowAltitude(), right.belowAltitude());
  }

  private record Key(String airport, int procedureType, String procedure, String type, @Nullable String transition) {
    boolean sameProcedure(Key other) {
      return airport.equals(other.airport) && procedureType == other.procedureType && procedure.equals(other.procedure);
    }
  }

  private record Route(Key key, List<DafifTerminalSegment> segments) {
    int phase() {
      if (key.procedureType() == 3) {
        return "A".equals(key.type()) ? 0 : 1;
      }
      return (Integer.parseInt(key.type()) - 1) % 3;
    }

    int order() {
      return key.procedureType() == 2 ? -phase() : phase();
    }

    boolean connectsTo(Route next) {
      if (!key.sameProcedure(next.key()) || order() >= next.order()) {
        return false;
      }
      DafifTerminalSegment exit = segments.get(key.procedureType() == 2 ? 0 : segments.size() - 1);
      DafifTerminalSegment entry = next.segments().get(key.procedureType() == 2 ? next.segments().size() - 1 : 0);
      return exit.termSegWaypointIdentifier().isPresent()
          && exit.termSegWaypointIdentifier().equals(entry.termSegWaypointIdentifier())
          && exit.waypointCountryCode().equals(entry.waypointCountryCode());
    }
  }
}
