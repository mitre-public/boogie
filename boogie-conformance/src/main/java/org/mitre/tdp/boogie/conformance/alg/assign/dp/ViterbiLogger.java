package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A custom logger to help diagnose decisions made by the ViterbiTagger in choosing
 * the maximum-likelihood state sequence matching a set of observations.
 * <p>
 * This is helpful because the
 * <p>
 * This is mutable
 */
public class ViterbiLogger<Stage extends Comparable<? super Stage>, State extends HmmState<Stage>> {
//  private static Optional<ViterbiLogger<?, ?>> listener = Optional.empty();
//
//  private Instant startInstant;
//  private Predicate<Stage> stageFilter = x -> true;
//  private BiPredicate<State, Integer> stateFilter = (x, i) -> true;
//  private Consumer<ViterbiTagger<Stage, State>.ViterbiTransition> logger = x -> System.out.println(x);
//  private Comparator<ViterbiTagger<Stage, State>.ViterbiTransition> cmp = Comparator.comparing((ViterbiTagger<Stage, State>.ViterbiTransition x) -> x.score()).reversed();
//
//  public static <Stage extends Comparable<? super Stage>, State extends HmmState<Stage>> ViterbiLogger<Stage, State> logTrellis() {
//    listener = Optional.of(new ViterbiLogger<>());
//    // noinspection unchecked
//    return (ViterbiLogger<Stage, State>) listener.get();
//  }
//
//  public ViterbiLogger<Stage, State> filteringStages(Predicate<Stage> pred) {
//    this.stageFilter = this.stageFilter.and(pred);
//    return this;
//  }
//
//  public ViterbiLogger<Stage, State> onlyTopStates(int numStates) {
//    return onlyTopStates(numStates, cmp);
//  }
//
//  public <U extends Comparable<? super U>> ViterbiLogger<Stage, State> onlyTopStates(int numStates, Function<ViterbiTagger<Stage, State>.ViterbiTransition, U> cmp) {
//    this.stateFilter = this.stateFilter.and((x, i) -> i < numStates);
//    this.cmp = Comparator.comparing(cmp).reversed();
//    return this;
//  }
//
//  public ViterbiLogger<Stage, State> onlyTopStates(int numStates, Comparator<ViterbiTagger<Stage, State>.ViterbiTransition> cmp) {
//    this.stateFilter = this.stateFilter.and((x, i) -> i < numStates);
//    this.cmp = cmp;
//    return this;
//  }
//
//  public ViterbiLogger<Stage, State> log(Consumer<ViterbiTagger<Stage, State>.ViterbiTransition> logger) {
//    this.logger = logger;
//    return this;
//  }
//
//  public static <Stage extends Comparable<? super Stage>, State extends HmmState<Stage>> void logIt(Collection<ViterbiTagger<Stage, State>.ViterbiTransition> stageScores) {
//    listener.ifPresent(x -> ((ViterbiLogger<Stage, State>) x).logit(stageScores));
//  }
//
//  private void logit(Collection<ViterbiTagger<Stage, State>.ViterbiTransition> stageScores) {
//    List<ViterbiTagger<Stage, State>.ViterbiTransition> z = stageScores.stream().sorted(cmp).collect(Collectors.toList());
//    IntStream.range(0, z.size())
//        .filter(i -> stateFilter.test(z.get(i).toState(), i))
//        .mapToObj(i -> z.get(i))
//        .filter(x -> stageFilter.test(x.toStage()))
//        .forEach(x -> logger.accept(x));
//  }
}
