package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Compares repeated and fused distance scans when two airway fixtures have no links within the requested range.
 * Graph and linker construction, plus verification that both implementations choose the same link, run outside timing.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgsAppend = {"-Xms512m", "-Xmx512m"})
@Threads(1)
public class RangeOrClosestBenchmark {

  private Linker chained;
  private Linker fused;

  @Setup(Level.Trial)
  public void setup() {
    TokenGrapher grapher = TokenGrapher.standard();
    Airway ug521 = Airways.UG521();
    Airway um219 = Airways.UM219();
    LinkableToken left = LinkableToken.anyAirway(ug521, grapher.graphRepresentationOf(ResolvedToken.standardAirway(ug521)));
    LinkableToken right = LinkableToken.anyAirway(um219, grapher.graphRepresentationOf(ResolvedToken.standardAirway(um219)));
    Distance range = Distance.ofNauticalMiles(.25);

    Linker withinRange = Linker.pointsWithinRange(range, left, right);
    if (!withinRange.links().isEmpty()) {
      throw new IllegalStateException("Airway fixtures must have no leg pairs within 0.25 NM to exercise the nearest-link fallback.");
    }

    chained = withinRange.orElseTry(Linker.closestPointBetween(left, right));
    fused = Linker.pointsWithinRangeOrClosest(range, left, right);

    List<LinkedLegs> expected = List.copyOf(chained.links());
    List<LinkedLegs> actual = List.copyOf(fused.links());
    if (expected.size() != 1 || !expected.equals(actual)) {
      throw new IllegalStateException("Both linkers must return the same single nearest link: " + expected + " / " + actual);
    }
  }

  @Benchmark
  public Collection<LinkedLegs> chainedScans() {
    return chained.links();
  }

  @Benchmark
  public Collection<LinkedLegs> fusedScan() {
    return fused.links();
  }
}
