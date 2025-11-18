package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MockObjects;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;

class StandardRouteResolverTest {

  private static final String route0 = "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054";

  @Test
  void testFullResolveRoute0() {
    RouteTokenResolver resolver = resolverForRoute0();

    List<ResolvedTokens> sections = resolver.applyTo(RouteTokenizer.faaIfrFormat().tokenize(route0), (t) -> true);
    assertEquals(7, sections.size());

    assertTrue(allMatch(sections, s -> s.resolvedTokens().size() == 1));

    List<Class<?>> classOrder = List.of(
        ResolvedToken.StandardAirport.class,
        ResolvedToken.SidEnrouteCommon.class,
        ResolvedToken.DirectToFix.class,
        ResolvedToken.StandardAirway.class,
        ResolvedToken.DirectToFix.class,
        ResolvedToken.StarEnrouteCommon.class,
        ResolvedToken.StandardAirport.class
    );

    assertTrue(matchesOrder(sections, classOrder));
  }

  private RouteTokenResolver resolverForRoute0() {
    Leg ifSherl = IF("SHERL", 0.0, 0.0);
    Leg ifBrigs = IF("BRIGS", 0.0, 0.0);

    Transition cstl6 = transition("CSTL6", TransitionType.COMMON, ProcedureType.SID, singletonList(ifSherl));
    Transition jiims2 = transition("JIIMS2", TransitionType.COMMON, ProcedureType.STAR, singletonList(ifBrigs));

    SidResolver sidResolver = new SidResolver(
        inMemory(
            MockObjects.newProcedures(newArrayList(cstl6, jiims2)),
            p -> Stream.of(p.procedureIdentifier())
        )
    );

    StarResolver starResolver = new StarResolver(
        inMemory(
            MockObjects.newProcedures(newArrayList(cstl6, jiims2)),
            p -> Stream.of(p.procedureIdentifier())
        )
    );

    Airport kbdl = airport("KBDL", 0.0, 0.0);
    Airport kphl = airport("KPHL", 0.0, 0.0);

    AirportResolver airportResolver = new AirportResolver(inMemory(
        newArrayList(kbdl, kphl),
        a -> Stream.of(a.airportIdentifier())
    ));

    Airway j121 = airway("J121", emptyList());
    Airway j122 = airway("J122", emptyList());

    AirwayResolver airwayResolver = new AirwayResolver(inMemory(
        Arrays.asList(j121, j122),
        a -> Stream.of(a.airwayIdentifier())
    ));

    Fix sherl = fix("SHERL", 0.0, 0.0);
    Fix brigs = fix("BRIGS", 0.0, 0.0);

    FixResolver fixResolver = new FixResolver(inMemory(
        Arrays.asList(sherl, brigs),
        f -> Stream.of(f.fixIdentifier())
    ));

    return RouteTokenResolver.composite()
        .addResolver(sidResolver)
        .addResolver(starResolver)
        .addResolver(airportResolver)
        .addResolver(airwayResolver)
        .addResolver(fixResolver)
        .addResolver(new LatLongResolver())
        .build();
  }

  private boolean matchesOrder(List<ResolvedTokens> sections, List<Class<?>> elementClasses) {
    return IntStream.range(0, sections.size())
        .filter(i -> {
          ResolvedTokens section = sections.get(i);
          ResolvedToken element = section.resolvedTokens().iterator().next();
          return elementClasses.get(i).isAssignableFrom(element.getClass());
        }).count() == sections.size();
  }
}
