package org.mitre.tdp.boogie.alg.facade;

import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.List;

import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PLMMR2;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
class FluentRouteExpanderBenchmark {

  @Benchmark
  void expandRoute(Blackhole bh) {

    Fix start = fix("START", 33.3336, -84.7909);
    Fix end = fix("ENDDD", 33.5496, -84.1357);

    String route = "KATL..START..ZALLE.J000.GGOLF..ENDDD..KMCO"; //these are also on the sid

    FluentRouteExpander expander = FluentRouteExpander.inMemoryBuilder(
            List.of(Airports.KATL(), Airports.KMCO()),
            List.of(PLMMR2.INSTANCE),
            List.of(Airways.J000()),
            List.of(start, end)
        )
        .build();

    bh.consume(expander.apply(route).orElseThrow());
  }
}
