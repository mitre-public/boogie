package org.mitre.tdp.boogie.alg.facade;

import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.COSTR3;
import org.mitre.tdp.boogie.CUN;
import org.mitre.tdp.boogie.CZM;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.KMCO_I17L;
import org.mitre.tdp.boogie.KMCO_I17R;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Steady-state expansion of routes from {@code FluentRouteExpanderTest}, using its committed navigation fixtures.
 * Fixture construction and result validation run once per trial, outside the timed operation.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgsAppend = {"-Xms512m", "-Xmx512m"})
@Threads(1)
public class FluentRouteExpanderBenchmark {

  public enum RouteCase {
    MULTI_AIRWAY,
    STAR_APPROACH
  }

  @Param({"MULTI_AIRWAY", "STAR_APPROACH"})
  public RouteCase routeCase;

  private FluentRouteExpander expander;
  private String route;
  private RouteDetails details;

  @Setup(Level.Trial)
  public void setup() {
    List<String> expectedFixes = switch (routeCase) {
      case MULTI_AIRWAY -> setupMultiAirway();
      case STAR_APPROACH -> setupStarApproach();
    };

    ExpandedRoute expanded = expander.expand(route, details).orElseThrow();
    List<String> actualFixes = expanded.legs().stream()
        .map(leg -> leg.associatedFix().map(Fix::fixIdentifier).orElseThrow())
        .toList();
    requireEqual("expanded fixes", expectedFixes, actualFixes);

    if (routeCase == RouteCase.STAR_APPROACH) {
      RouteSummary summary = expanded.routeSummary().orElseThrow();
      requireEqual("arrival airport", "KMCO", summary.arrivalAirport());
      requireEqual("arrival runway", "RW17R", summary.arrivalRunway().orElse(null));
      requireEqual("STAR", "COSTR3", summary.star().orElse(null));
      requireEqual("STAR entry fix", "RSW", summary.starEntryFix().orElse(null));
      requireEqual("arrival fix", "COSTR", summary.arrivalFix().orElse(null));
      requireEqual("STAR equipage", RequiredNavigationEquipage.RNAV, summary.requiredStarEquipage().orElse(null));
      requireEqual("approach", "I17R", summary.approach().orElse(null));
      requireEqual("approach entry fix", "RATOY", summary.approachEntryFix().orElse(null));
      requireEqual("approach equipage", RequiredNavigationEquipage.CONV, summary.requiredApproachEquipage().orElse(null));
    }
  }

  @Benchmark
  public ExpandedRoute expandRoute() {
    return expander.expand(route, details).orElseThrow();
  }

  private List<String> setupMultiAirway() {
    // FluentRouteExpanderTest.testStarIncorrectlyUsedAsAirwayEntryAndExitFix
    route = "SAP.UG521.CZM.UB881.CUN.UM219.MYDIA";
    details = RouteDetails.builder().build();
    expander = FluentRouteExpander.inMemoryBuilder(
        List.of(),
        List.of(CZM.INSTANCE, CUN.INSTANCE),
        List.of(Airways.UG521(), Airways.UB881(), Airways.UM219()),
        List.of(
            fix("SAP", 15.4636, -87.91696111111112),
            fix("CZM", 20.507472222222223, -86.912),
            fix("CUN", 21.025108333333332, -86.85871666666667),
            fix("MYDIA", 24.04066388888889, -86.15824166666667)
        )
    ).build();
    return List.of(
        "SAP", "KORTI", "KIRAP", "AMIDA", "ITPIG", "CZM", "LIDEK", "CUN", "OMSUK", "ROTGI", "XOPGI", "RAKAR", "ALPUK", "MYDIA"
    );
  }

  private List<String> setupStarApproach() {
    // FluentRouteExpanderTest.testFPApchA
    route = "RSW.COSTR3.KMCO";
    details = RouteDetails.builder()
        .arrivalRunway("RW17R")
        .equipagePreference(RequiredNavigationEquipage.CONV)
        .build();
    expander = FluentRouteExpander.inMemoryBuilder(
        List.of(Airports.KMCO()),
        List.of(COSTR3.INSTANCE, KMCO_I17R.I17R, KMCO_I17L.I17L),
        List.of(),
        List.of(fix("RSW", 26.529875, -81.77576666666667))
    ).build();
    return List.of(
        "RSW", "DOWNN", "MOANS", "COSTR", "BIGGR", "TINKR", "KRAKN", "TWONA", "KNUKL", "RATOY", "SACRO", "TACOT",
        "DALTY", "ELLAN", "GLOSI", "MINCO", "RW17R", "KMCO"
    );
  }

  private static void requireEqual(String field, Object expected, Object actual) {
    if (!Objects.equals(expected, actual)) {
      throw new IllegalStateException("Unexpected " + field + ": expected " + expected + ", got " + actual);
    }
  }
}
