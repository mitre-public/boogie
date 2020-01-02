package org.mitre.tdp.boogie.alg.resolve;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.ExpandRoutes;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.LinkedLegs;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.MockObjects.magneticVariation;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.utils.Collections.allMatch;
import static org.mockito.Mockito.when;

public class TestSectionResolver {

  private List<SectionSplit> splits(String name) {
    return SectionSplitter.splits(name);
  }

  @Test
  public void testSingleFixResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);

    ExpandRoutes expander = ExpandRoutes.with(singletonList(fix), emptyList(), emptyList(), emptyList());
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("JIMMY"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement element = section.elements().get(0);
    assertEquals("JIMMY", element.reference().identifier());
    assertEquals(ElementType.FIX, element.type());
  }

  @Test
  public void testSingleAirportResolution() {
    Airport airport = airport("JIMMY", 0.0, 0.0);

    ExpandRoutes expander = ExpandRoutes.with(emptyList(), emptyList(), singletonList(airport), emptyList());
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("JIMMY"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement element = section.elements().get(0);
    assertEquals("JIMMY", element.reference().identifier());
    assertEquals(ElementType.AIRPORT, element.type());
  }

  @Test
  public void testSingleAirwayResolution() {
    Airway airway = airway("JIMMY", emptyList());

    ExpandRoutes expander = ExpandRoutes.with(emptyList(), singletonList(airway), emptyList(), emptyList());
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("JIMMY"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement element = section.elements().get(0);
    assertEquals("JIMMY", element.reference().identifier());
    assertEquals(ElementType.AIRWAY, element.type());
  }

  @Test
  public void testSingleProcedureResolution() {
    Leg l = IF("FOO", 0.0, 0.0);
    Transition transition = transition("JIMMY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    ExpandRoutes expander = ExpandRoutes.with(emptyList(), emptyList(), emptyList(), singletonList(transition));
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("JIMMY"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement element = section.elements().get(0);
    assertEquals("JIMMY", element.reference().identifier());
    assertEquals(ElementType.PROCEDURE, element.type());
  }

  @Test
  public void testSingleLatLonResolution() {
    ExpandRoutes expander = ExpandRoutes.with(emptyList(), emptyList(), emptyList(), emptyList());
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("5300N/14000W"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement element = section.elements().get(0);
    assertEquals("5300N/14000W", element.reference().identifier());
    assertEquals(ElementType.LATLON, element.type());
  }

  @Test
  public void testSingleTailoredResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);

    MagneticVariation variation = magneticVariation(10.0f, 10.0f);
    when(fix.magneticVariation()).thenReturn(variation);

    ExpandRoutes expander = ExpandRoutes.with(singletonList(fix), emptyList(), emptyList(), emptyList());
    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits("JIMMY111018"));
    assertEquals(1, route.sections().size());

    ResolvedSection section = route.sections().get(0);
    assertEquals(1, section.elements().size());

    ResolvedElement<?> element = section.elements().get(0);
    assertEquals("JIMMY", element.reference().identifier());
    assertEquals(ElementType.TAILORED, element.type());

    LinkedLegs legs = element.legs().get(0);
    assertNotEquals(LatLong.of(0.0, 0.0), legs.source().leg().pathTerminator().latLong());
  }

  private String route0 = "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054";

  private ExpandRoutes expanderForRoute0() {
    Leg ifSherl = IF("SHERL", 0.0, 0.0);
    Leg ifBrigs = IF("BRIGS", 0.0, 0.0);

    Transition cstl6 = transition("CSTL6", TransitionType.COMMON, ProcedureType.SID, singletonList(ifSherl));
    Transition jiims2 = transition("JIIMS2", TransitionType.COMMON, ProcedureType.STAR, singletonList(ifBrigs));
    Airport kbdl = airport("KBDL", 0.0, 0.0);
    Airport kphl = airport("KPHL", 0.0, 0.0);
    Airway j121 = airway("J121", emptyList());
    Airway j122 = airway("J122", emptyList());
    Fix sherl = fix("SHERL", 0.0, 0.0);
    Fix brigs = fix("BRIGS", 0.0, 0.0);
    return ExpandRoutes.with(
        Arrays.asList(sherl, brigs),
        Arrays.asList(j121, j122),
        Arrays.asList(kbdl, kphl),
        Arrays.asList(cstl6, jiims2));
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

  @Test
  public void testFullResolveRoute0() {
    ExpandRoutes expander = expanderForRoute0();

    SectionResolver resolver = SectionResolver.with(expander);

    ResolvedRoute route = resolver.resolve(splits(route0));
    assertEquals(7, route.sections().size());

    List<ResolvedSection> sections = route.sections();
    assertTrue(allMatch(sections, s -> s.elements().size() == 1));

    assertTrue(matchesOrder(sections, Arrays.asList(
        ElementType.AIRPORT,
        ElementType.PROCEDURE,
        ElementType.FIX,
        ElementType.AIRWAY,
        ElementType.FIX,
        ElementType.PROCEDURE,
        ElementType.AIRPORT)));
  }
}
