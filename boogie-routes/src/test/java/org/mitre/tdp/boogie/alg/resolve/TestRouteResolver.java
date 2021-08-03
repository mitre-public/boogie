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
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
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
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.model.ProcedureFactory;

class TestRouteResolver {

  private static final String route0 = "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054";

  @Test
  void testFullResolveRoute0() {
    SectionResolver resolver = resolverForRoute0();

    List<ResolvedSection> sections = resolver.applyTo(IfrFormatSectionSplitter.INSTANCE.apply(route0));
    assertEquals(7, sections.size());

    assertTrue(allMatch(sections, s -> s.elements().size() == 1));

    assertTrue(matchesOrder(sections, Arrays.asList(
        AirportElement.class,
        SidElement.class,
        FixElement.class,
        AirwayElement.class,
        FixElement.class,
        StarElement.class,
        AirportElement.class)));
  }

  private SectionResolver resolverForRoute0() {
    Leg ifSherl = IF("SHERL", 0.0, 0.0);
    Leg ifBrigs = IF("BRIGS", 0.0, 0.0);

    Transition cstl6 = transition("CSTL6", TransitionType.COMMON, ProcedureType.SID, singletonList(ifSherl));
    Transition jiims2 = transition("JIIMS2", TransitionType.COMMON, ProcedureType.STAR, singletonList(ifBrigs));

    SidStarResolver sidStarResolver = new SidStarResolver(newLookupService(
        ProcedureFactory.newProcedures(newArrayList(cstl6, jiims2)),
        Procedure::procedureIdentifier
    ));

    Airport kbdl = airport("KBDL", 0.0, 0.0);
    Airport kphl = airport("KPHL", 0.0, 0.0);

    AirportResolver airportResolver = new AirportResolver(newLookupService(
        newArrayList(kbdl, kphl),
        Airport::airportIdentifier
    ));

    Airway j121 = airway("J121", emptyList());
    Airway j122 = airway("J122", emptyList());

    AirwayResolver airwayResolver = new AirwayResolver(newLookupService(
        Arrays.asList(j121, j122),
        Airway::airwayIdentifier
    ));

    Fix sherl = fix("SHERL", 0.0, 0.0);
    Fix brigs = fix("BRIGS", 0.0, 0.0);

    FixResolver fixResolver = new FixResolver(newLookupService(
        Arrays.asList(sherl, brigs),
        Fix::fixIdentifier
    ));

    return SectionResolver.composeAll(
        sidStarResolver,
        airportResolver,
        airwayResolver,
        fixResolver,
        new LatLonResolver()
    );
  }

  private boolean matchesOrder(List<ResolvedSection> sections, List<Class<?>> elementClasses) {
    return IntStream.range(0, sections.size())
        .filter(i -> {
          ResolvedSection section = sections.get(i);
          ResolvedElement element = section.elements().iterator().next();
          return elementClasses.get(i).isAssignableFrom(element.getClass());
        }).count() == sections.size();
  }
}
