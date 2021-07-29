package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.model.ProcedureFactory;

class TestRouteResolver {

  private static final String route0 = "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054";

  @Test
  void testFullResolveRoute0() {
    RouteResolver resolver = resolverForRoute0();

    ResolvedRoute route = resolver.apply(splits(route0));
    assertEquals(7, route.sections().size());

    List<ResolvedSection> sections = route.sections();
    assertTrue(allMatch(sections, s -> s.elements().size() == 1));

    assertTrue(matchesOrder(sections, Arrays.asList(
        ElementType.AIRPORT,
        ElementType.SID,
        ElementType.FIX,
        ElementType.AIRWAY,
        ElementType.FIX,
        ElementType.STAR,
        ElementType.AIRPORT)));
  }

  private RouteResolver resolverForRoute0() {
    Leg ifSherl = IF("SHERL", 0.0, 0.0);
    Leg ifBrigs = IF("BRIGS", 0.0, 0.0);

    Transition cstl6 = transition("CSTL6", TransitionType.COMMON, ProcedureType.SID, singletonList(ifSherl));
    Transition jiims2 = transition("JIIMS2", TransitionType.COMMON, ProcedureType.STAR, singletonList(ifBrigs));

    ProcedureResolver procedureResolver = new ProcedureResolver(newLookupService(
        Procedure::procedureIdentifier,
        ProcedureFactory.newProcedures(newArrayList(cstl6, jiims2)))
    );

    Airport kbdl = airport("KBDL", 0.0, 0.0);
    Airport kphl = airport("KPHL", 0.0, 0.0);

    AirportResolver airportResolver = new AirportResolver(newLookupService(
        Airport::airportIdentifier,
        newArrayList(kbdl, kphl))
    );

    Airway j121 = airway("J121", emptyList());
    Airway j122 = airway("J122", emptyList());

    AirwayResolver airwayResolver = new AirwayResolver(newLookupService(
        Airway::airwayIdentifier,
        Arrays.asList(j121, j122))
    );

    Fix sherl = fix("SHERL", 0.0, 0.0);
    Fix brigs = fix("BRIGS", 0.0, 0.0);

    FixResolver fixResolver = new FixResolver(newLookupService(
        Fix::fixIdentifier,
        Arrays.asList(sherl, brigs))
    );

    return new RouteResolver(
        procedureResolver,
        airportResolver,
        airwayResolver,
        fixResolver,
        new LatLonResolver()
    );
  }

  private boolean matchesOrder(List<ResolvedSection> sections, List<ElementType> types) {
    return IntStream.range(0, sections.size())
        .filter(i -> {
          ResolvedSection section = sections.get(i);
          ElementType type = types.get(i);

          ResolvedElement<?> element = section.elements().get(0);
          return element.type().equals(type);
        }).count() == sections.size();
  }

  private List<SectionSplit> splits(String name) {
    return new IfrFormatSectionSplitter().splits(name);
  }
}
