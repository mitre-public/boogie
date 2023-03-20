package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.routes.ImmutableTransition;
import org.mitre.tdp.boogie.contract.routes.Transition;

public final class ConvertTransition implements Function<org.mitre.tdp.boogie.Transition, Transition> {
  public static final ConvertTransition INSTANCE = new ConvertTransition();

  private static final ConvertLeg LEG = ConvertLeg.INSTANCE;

  private ConvertTransition() {

  }

  @Override
  public Transition apply(org.mitre.tdp.boogie.Transition transition) {
    return ImmutableTransition.builder()
        .transitionIdentifier(transition.transitionIdentifier())
        .procedureIdentifier(transition.procedureIdentifier())
        .airportIdentifier(transition.airportIdentifier())
        .airportRegion(transition.airportRegion())
        .procedureType(transition.procedureType())
        .transitionType(transition.transitionType())
        .legs(transition.legs().stream().map(LEG).collect(Collectors.toList()))
        .build();
  }
}
