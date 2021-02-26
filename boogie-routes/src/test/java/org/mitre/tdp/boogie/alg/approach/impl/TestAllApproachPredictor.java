package org.mitre.tdp.boogie.alg.approach.impl;

import static com.google.common.collect.Streams.concat;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.test.Airports.KDEN;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;
import org.mitre.tdp.boogie.test.CONNR5;
import org.mitre.tdp.boogie.test.HOBTT2;
import org.mitre.tdp.boogie.test.I16R;

class TestAllApproachPredictor {

  @Test
  void testReturnsAllApproaches() {
    Airport kden = KDEN();

    List<Transition> transitions = concat(
        CONNR5.build().transitions().stream(),
        HOBTT2.build().transitions().stream(),
        I16R.build().transitions().stream())
        .collect(Collectors.toList());

    AllApproachPredictor predictor = new AllApproachPredictor(ProcedureGraphService.withTransitions(transitions));

    ResolvedSection s1 = newSection("JIMMY1", 6, singletonList(resolvedElement(ElementType.STAR)));
    ResolvedSection s2 = newSection("KDEN", 6, singletonList(new AirportElement(kden)));

    ResolvedSection section = predictor.predictCandidateApproaches(s1, s2).get();

    assertEquals(1, section.elements().size());
  }

  private ResolvedElement<?> resolvedElement(ElementType type) {
    ResolvedElement<?> element = mock(ResolvedElement.class);
    when(element.type()).thenReturn(type);
    return element;
  }

  private SectionSplit newSplit(String name, int index) {
    return new SectionSplit.Builder().setValue(name).setIndex(index).build();
  }

  private ResolvedSection newSection(String name, int index, List<ResolvedElement<?>> elements) {
    return new ResolvedSection(newSplit(name, index)).setElements(elements);
  }
}
