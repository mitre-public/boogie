package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.mitre.tdp.boogie.model.BoogieProcedure;
import org.mitre.tdp.boogie.model.BoogieTransition;

/**
 * Functional class for converting a collection of {@link ArincProcedureLeg} into a sequence of {@link Transition} records as
 * would be expected in downstream data classes in packages like boogie-routes.
 */
public final class ProcedureAssembler implements Function<Collection<ArincProcedureLeg>, Stream<Procedure>> {

  private static final ArincTransitionTypeClassifier transitionTypeClassifier = new ArincTransitionTypeClassifier();

  private final TerminalAreaDatabase terminalAreaDatabase;
  private final FixDatabase fixDatabase;

  public ProcedureAssembler(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.fixDatabase = requireNonNull(fixDatabase);
  }

  @Override
  public Stream<Procedure> apply(Collection<ArincProcedureLeg> arincProcedureLegs) {
    return arincProcedureLegs.stream()
        .collect(Collectors.groupingBy(this::transitionGroupKey))
        .values().stream()
        .map(this::toTransition)
        .collect(Collectors.groupingBy(this::procedureGroupKey))
        .values().stream()
        .map(this::toProcedure);
  }

  /**
   * Converts the list of related {@link Transition}s into a {@link Procedure} which downstream algorithms can consume and operate
   * on more easily.
   */
  private Procedure toProcedure(List<Transition> transitions) {
    Transition representative = transitions.get(0);
    return new BoogieProcedure.Builder()
        .procedureIdentifier(representative.procedureIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportRegion())
        .procedureType(representative.procedureType())
        .requiredNavigationEquipage(RequiredNavigationEquipage.UNKNOWN)
        .transitions(transitions)
        .build();
  }

  /**
   * Converts the list of related {@link ArincProcedureLeg}s into a {@link Transition} representation of the form Boogie's other
   * algorithms can consume and operate on.
   */
  private Transition toTransition(List<ArincProcedureLeg> arincProcedureLegs) {
    ArincProcedureLeg representative = arincProcedureLegs.get(0);

    List<Leg> legs = arincProcedureLegs.stream()
        .map(this::toLeg)
        .sorted(Comparator.comparing(Leg::sequenceNumber))
        .collect(Collectors.toList());

    return new BoogieTransition.Builder()
        .procedureIdentifier(representative.sidStarIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportIcaoRegion())
        .transitionIdentifier(representative.transitionIdentifier().orElse("ALL"))
        .procedureType(toProcedureType(representative.subSectionCode()))
        .transitionType(transitionTypeClassifier.apply(arincProcedureLegs))
        .legs(legs)
        .build();
  }

  /**
   * The subSection within the database determines whether the procedure is a SID/STAR/APPROACH record.
   */
  private ProcedureType toProcedureType(String subSectionCode) {
    if ("F".equals(subSectionCode)) {
      return ProcedureType.APPROACH;
    } else if ("E".equals(subSectionCode)) {
      return ProcedureType.STAR;
    } else if ("D".equals(subSectionCode)) {
      return ProcedureType.SID;
    } else {
      throw new IllegalArgumentException("Unable to classify subSectionCode as a ProcedureType: ".concat(subSectionCode));
    }
  }

  private Leg toLeg(ArincProcedureLeg arincProcedureLeg) {
    return new BoogieLeg.Builder().build();
  }

  private String procedureGroupKey(Transition transition) {
    return transition.procedureIdentifier()
        .concat(transition.airportIdentifier())
        .concat(transition.airportRegion())
        .concat(transition.procedureType().name());
  }

  private String transitionGroupKey(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.airportIdentifier()
        .concat(arincProcedureLeg.airportIcaoRegion())
        .concat(arincProcedureLeg.sidStarIdentifier())
        .concat(arincProcedureLeg.transitionIdentifier().orElse("ALL"))
        .concat(arincProcedureLeg.subSectionCode());
  }
}
