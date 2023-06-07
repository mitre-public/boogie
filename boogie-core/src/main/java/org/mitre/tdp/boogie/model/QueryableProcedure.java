package org.mitre.tdp.boogie.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TraversalOrderSorter;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Decorates a {@link Procedure} record with a more "query-friendly" interface - supporting a variety of transition/leg/fix level
 * lookup queries.
 */
public final class QueryableProcedure implements Procedure {

  private final Procedure procedure;

  private final Map<String, TransitionPage> transitionsByName;
  private final LinkedHashMultimap<TransitionType, TransitionPage> transitionsByType;

  public QueryableProcedure(Procedure procedure) {
    this.procedure = requireNonNull(procedure);

    List<Transition> sortedTransitions = TraversalOrderSorter.forProcedureType(procedure.procedureType()).sort(procedure.transitions()).stream().flatMap(Collection::stream).collect(Collectors.toList());
    List<TransitionPage> transitionPages = sortedTransitions.stream().map(TransitionPage::new).collect(Collectors.toList());

    this.transitionsByName = transitionPages.stream()
        .collect(Collectors.toMap(t -> t.transition().transitionIdentifier().filter(s -> !s.isEmpty()).orElse("ALL"), Function.identity()));

    this.transitionsByType = LinkedHashMultimap.create();
    transitionPages.forEach(page -> transitionsByType.put(page.transition().transitionType(), page));
  }

  private String transitionName(String transitionName) {
    return null == transitionName || transitionName.isEmpty() ? "ALL" : transitionName;
  }

  public Optional<Transition> transitionWithName(@Nullable String transitionName) {
    return Optional.ofNullable(transitionsByName.get(transitionName(transitionName))).map(TransitionPage::transition);
  }

  public Set<Transition> transitionsOfType(TransitionType transitionType) {
    return transitionsByType.get(transitionType).stream().map(TransitionPage::transition).collect(Collectors.toSet());
  }

  public boolean containsFixWithIdentifier(String fixIdentifier) {
    return transitionsByName.values().stream().anyMatch(page -> page.containsFix(fixIdentifier));
  }

  public Optional<Leg> firstLegOfTransition(String transitionName) {
    return transitionWithName(transitionName).map(transition -> transition.legs().get(0));
  }

  public Optional<Leg> lastLegOfTransition(String transitionName) {
    return transitionWithName(transitionName).map(transition -> transition.legs().get(transition.legs().size() - 1));
  }

  public Set<Leg> initialProcedureLegs() {
    return transitionsByType.asMap().entrySet().iterator().next()
        .getValue().stream().map(TransitionPage::firstLeg).collect(Collectors.toSet());
  }

  public Set<Leg> finalProcedureLegs() {
    return transitionsByType.asMap().entrySet().stream().reduce((e1, e2) -> e2).orElseThrow(IllegalStateException::new)
        .getValue().stream().map(TransitionPage::lastLeg).collect(Collectors.toSet());
  }

  /**
   * Returns a fix with the provided identifier if there exist on in the procedure. If there are multiple fixes present this
   * returns the first.
   */
  public Optional<Fix> fixWithIdentifier(String fixIdentifier) {
    return fixesWithIdentifier(fixIdentifier).stream().findFirst();
  }

  public Collection<Fix> fixesWithIdentifier(String fixIdentifier) {
    return transitionsByName.values().stream().flatMap(page -> page.fixesWithName(fixIdentifier).stream()).collect(Collectors.toSet());
  }

  public Collection<Leg> associatedLegsOf(String fixIdentifier) {
    return fixesWithIdentifier(fixIdentifier).stream().flatMap(fix -> associatedLegsOf(fix).stream()).collect(Collectors.toSet());
  }

  public Collection<Leg> associatedLegsOf(Fix fix) {
    return transitionsByName.values().stream().flatMap(page -> page.legsWithFix(fix).stream()).collect(Collectors.toSet());
  }

  /**
   * "There can only be one"
   * <br>
   * https://www.youtube.com/watch?v=_J3VeogFUOs
   */
  private static <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }

  @Override
  public String procedureIdentifier() {
    return procedure.procedureIdentifier();
  }

  @Override
  public String airportIdentifier() {
    return procedure.airportIdentifier();
  }

  @Override
  public ProcedureType procedureType() {
    return procedure.procedureType();
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return procedure.requiredNavigationEquipage();
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return procedure.transitions();
  }

  @Override
  public void accept(Visitor visitor) {
    procedure.accept(visitor);
  }

  private static final class TransitionPage {

    private final Transition transition;

    private final Multimap<Fix, Leg> legLookup;
    private final Multimap<String, Fix> fixLookup;

    private TransitionPage(Transition transition) {
      this.transition = requireNonNull(transition);
      checkArgument(!transition.legs().isEmpty());

      this.legLookup = LinkedHashMultimap.create();
      this.fixLookup = LinkedHashMultimap.create();

      this.initializeLookups();
    }

    public Transition transition() {
      return transition;
    }

    public Leg firstLeg() {
      return transition.legs().get(0);
    }

    public Leg lastLeg() {
      return transition.legs().get(transition.legs().size() - 1);
    }

    public boolean containsFix(String fixIdentifier) {
      return fixLookup.containsKey(fixIdentifier);
    }

    public Optional<Fix> fixWithName(String fixIdentifier) {
      return highlander(fixLookup.get(fixIdentifier));
    }

    public Collection<Fix> fixesWithName(String fixIdentifier) {
      return fixLookup.get(fixIdentifier);
    }

    public Optional<Leg> legWithFix(Fix fix) {
      return highlander(legLookup.get(fix));
    }

    public Collection<Leg> legsWithFix(Fix fix) {
      return legLookup.get(fix);
    }

    private void initializeLookups() {
      transition.legs().stream().filter(leg -> leg.associatedFix().isPresent()).forEach(leg -> {
        Fix associatedFix = leg.associatedFix().orElseThrow(IllegalStateException::new);

        legLookup.put(associatedFix, leg);
        fixLookup.put(associatedFix.fixIdentifier(), associatedFix);
      });
    }
  }
}
