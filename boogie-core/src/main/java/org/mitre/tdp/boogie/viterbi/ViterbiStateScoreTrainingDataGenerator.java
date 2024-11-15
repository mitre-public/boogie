package org.mitre.tdp.boogie.viterbi;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * This classes name is a long-form way of saying if you have a {@link FeatureBasedViterbiScoringStrategy} and a simple function
 * allowing for an assessment of {@link #isValidAssociation}s this class can generate a set of labeled feature vectors based on
 * the scoring strategy and the labeler.
 */
public final class ViterbiStateScoreTrainingDataGenerator<STAGE, STATE> {

  private static final Logger LOG = LoggerFactory.getLogger(ViterbiStateScoreTrainingDataGenerator.class);

  private static final String IS_ASSIGNED = "IsAssigned";

  /**
   * The {@link FeatureBasedViterbiScoringStrategy} to use when generating feature vectors for all stage/state combinations.
   */
  private final FeatureBasedViterbiScoringStrategy<STAGE, STATE> scoringStrategy;
  /**
   * Function for labeling the provided stage/state combinations as "valid" associations - this is essentially the labeling
   * function for the stage/state pairs and will be used to add a isCorrectAssociation feature to the output vectors.
   *
   * Note this function can return <i>null</i> if there is no known valid state association for the provided stage. This is
   * handled internally by the trainer and results in not generating and returning a vector for that stage.
   */
  private final BiFunction<STAGE, STATE, Optional<Boolean>> isValidAssociation;
  /**
   * Optional print function of errors encountered related to specific stages, defaults to Object.toString().
   */
  private final Function<STAGE, String> stagePrinter;
  /**
   * Optional print function of errors encountered related to specific states, defaults to Object.toString().
   */
  private final Function<STATE, String> statePrinter;

  public ViterbiStateScoreTrainingDataGenerator(
      FeatureBasedViterbiScoringStrategy<STAGE, STATE> scoringStrategy,
      BiPredicate<STAGE, STATE> isValidAssociation
  ) {
    this(scoringStrategy, (stage, state) -> Optional.of(isValidAssociation.test(stage, state)), Objects::toString, Objects::toString);
  }

  public ViterbiStateScoreTrainingDataGenerator(
      FeatureBasedViterbiScoringStrategy<STAGE, STATE> scoringStrategy,
      BiFunction<STAGE, STATE, Optional<Boolean>> isValidAssociation
  ) {
    this(scoringStrategy, isValidAssociation, Object::toString, Object::toString);
  }

  public ViterbiStateScoreTrainingDataGenerator(
      FeatureBasedViterbiScoringStrategy<STAGE, STATE> scoringStrategy,
      BiFunction<STAGE, STATE, Optional<Boolean>> isValidAssociation,
      Function<STAGE, String> stagePrinter,
      Function<STATE, String> statePrinter
  ) {
    this.scoringStrategy = requireNonNull(scoringStrategy);
    this.isValidAssociation = requireNonNull(isValidAssociation);
    this.stagePrinter = requireNonNull(stagePrinter);
    this.statePrinter = requireNonNull(statePrinter);
  }

  /**
   * Generates a list of all possible {@link ViterbiFeatureVector}s based on the combinations of all possible states and stages
   * provided. This class also appends a state to the {@link ViterbiFeatureVector}s generated via the {@link #scoringStrategy}
   * indicating whether the stage/state combo is a valid
   */
  public List<ViterbiFeatureVector> generateAllFeatureVectorCombinations(Collection<STAGE> stages, Collection<STATE> states) {
    requireNonNull(stages);
    requireNonNull(states);
    checkArgument(!stages.isEmpty(), "Cannot generate all feature vector combinations with no provided stages.");
    checkArgument(!states.isEmpty(), "Cannot generate all feature vector combinations with no provided states.");
    checkNoStagesAssignedToMultipleStates(stages, states);

    Collection<Pair<STAGE, STATE>> allCombos = Combinatorics.cartesianProduct(stages, states);
    LOG.info("Generated {} total combinations of stage/state combinations for evaluation.", allCombos.size());

    return allCombos.stream()
        .filter(pair -> isValidAssociation.apply(pair.first(), pair.second()).isPresent())
        .map(pair -> labeledVectorFor(pair.first(), pair.second()))
        .collect(Collectors.toList());
  }

  /**
   * Generates a new labeled {@link ViterbiFeatureVector} for the given stage/state combination.
   */
  private ViterbiFeatureVector labeledVectorFor(STAGE stage, STATE state) {
    ViterbiFeatureVector baseVector = scoringStrategy.featureExtractor(stage, state).apply(stage, state);
    checkArgument(!baseVector.containsFeature(IS_ASSIGNED), "IsAssigned is a reserved feature in the data generation process.");

    Boolean label = isValidAssociation.apply(stage, state).orElseThrow(IllegalStateException::new);

    Map<String, Double> updated = baseVector.asMap();
    updated.put(IS_ASSIGNED, label ? 1. : 0.);

    return ViterbiFeatureVector.fromMap(updated);
  }

  /**
   * Checks that none of the provided stages are labeled as "assigned" to multiple states via the {@link #isValidAssociation}.
   *
   * This function does <i>not</i> check that all stages have at least 1 state associated with them - as that is not something
   * required by the contract of the training data generator.
   */
  private void checkNoStagesAssignedToMultipleStates(Collection<STAGE> stages, Collection<STATE> states) {
    String multiplyAssignedStages = stages.stream()
        .map(stage -> isMultiplyAssignedStage(stage, states))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.joining("\n"));

    checkArgument(multiplyAssignedStages.isEmpty(), "Found stages mapped to multiple states:\n".concat(multiplyAssignedStages));
  }

  /**
   * Returns a optional string of "stage:state1,state2,..." if the state was flagged as assignable to multiple states via the
   * configured {@link #isValidAssociation} function.
   */
  private Optional<String> isMultiplyAssignedStage(STAGE stage, Collection<STATE> states) {
    List<String> matchedStates = states.stream()
        .filter(state -> isValidAssociation.apply(stage, state).orElse(false))
        .map(statePrinter).collect(Collectors.toList());

    String matchStr = stagePrinter.apply(stage).concat(":").concat(Joiner.on(",").join(matchedStates));
    return matchedStates.size() > 1 ? Optional.of(matchStr) : Optional.empty();
  }
}
