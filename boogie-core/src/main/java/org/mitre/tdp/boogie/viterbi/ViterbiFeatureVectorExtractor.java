package org.mitre.tdp.boogie.viterbi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.mitre.caasd.commons.Pair;

import com.google.common.collect.ImmutableList;

/**
 * Aggregation class for extracting collections of features from objects into {@link ViterbiFeatureVector}s.
 *
 * Generally speaking this classes usage should look like:
 *
 * 1) {@link #newBuilder()} - to get an instance of a builder for a new extractor
 *
 * 2) That builder can then be used to add set of features which will be extracted from input state/stage combinations and
 * converted into a {@link ViterbiFeatureVector}
 *
 * 3) The builder will pre-check prior to feature generation that there are no duplicate-ly generated feature labels (if the
 * provided information is correct)
 *
 * 4) At runtime check during feature aggregation for explicitly generated duplicates (which will capture dups regardless of
 * the input to the methods at build time)
 *
 * From there this class operates as a {@link BiFunction} itself taking input stage/state combinations and generating viterbi
 * feature vectors for use elsewhere.
 */
public final class ViterbiFeatureVectorExtractor<STAGE, STATE> implements BiFunction<STAGE, STATE, ViterbiFeatureVector> {

  /**
   * The immutable list of all individual feature extractors we want to apply to the input data to generate the final output
   * {@link ViterbiFeatureVector}.
   */
  private final ImmutableList<BiFunction<STAGE, STATE, List<Pair<String, Double>>>> featureExtractors;

  public ViterbiFeatureVectorExtractor(Builder<STAGE, STATE> builder) {
    this.featureExtractors = ImmutableList.copyOf(new LinkedHashSet<>(builder.extractors.values()));
  }

  @Override
  public ViterbiFeatureVector apply(STAGE stage, STATE state) {
    return featureExtractors.stream().flatMap(extractor -> extractor.apply(stage, state).stream()).collect(new FeatureCollector());
  }

  /**
   * Generates and returns a new {@link Builder} record.
   */
  public static <Stage, State> Builder<Stage, State> newBuilder() {
    return new Builder<>();
  }

  /**
   * Typed builder for {@link ViterbiFeatureVectorExtractor}s. Allows a common (and more safe interface) for adding feature
   * extractors to an overall vector extractor.
   */
  public static final class Builder<Stage, State> {

    private final Map<String, BiFunction<Stage, State, List<Pair<String, Double>>>> extractors = new HashMap<>();

    /**
     * Attempts to add a one-to-many extractor for cases where some upstream computation in feature computation should be shared
     * (typically for efficiency reasons) between features. A good example is a cross/along track distance computation which may
     * be run a non-trivial number of times internally within the {@link ViterbiTagger}.
     *
     * This class tries to check feature uniqueness early by attempting to insert the extractor with each of the provided feature
     * names (checking for conflicts) in the insertion - but this implicitly relies on users providing the correct set of output
     * feature names.
     *
     * It's important to note that regardless duplicate features between extractors will cause failures downstream if the output
     * of the individually configured extractors conflict when collected into the final feature vector. This is checked in the
     * {@link FeatureCollector}.
     */
    public final Builder<Stage, State> addMultiFeatureExtractor(BiFunction<Stage, State, List<Pair<String, Double>>> extractor, String... featureNames) {
      checkArgument(featureNames.length > 0, "Must supply at least 1 associated feature name.");

      Arrays.stream(featureNames).forEach(featureName -> {
        checkArgument(!this.extractors.containsKey(requireNonNull(featureName)), "Cannot insert duplicate extractor for feature.");
        extractors.put(featureName, extractor);
      });
      return this;
    }

    /**
     * Adds a new {@link ViterbiFeatureExtractor} with the given feature name and extraction function.
     */
    public final Builder<Stage, State> addFeatureExtractor(String featureName, BiFunction<Stage, State, Double> extractor) {
      checkArgument(!this.extractors.containsKey(requireNonNull(featureName)), "Cannot insert duplicate extractor for feature.");
      return addFeatureExtractor(new ViterbiFeatureExtractor<>(featureName, requireNonNull(extractor)));
    }

    /**
     * Adds the provided {@link ViterbiFeatureExtractor} to the vector extraction configuration - checking for feature name
     * collisions and etc.
     */
    public final Builder<Stage, State> addFeatureExtractor(ViterbiFeatureExtractor<Stage, State> viterbiFeatureExtractor) {
      this.extractors.put(viterbiFeatureExtractor.featureName(), (stage, state) -> singletonList(viterbiFeatureExtractor.apply(stage, state)));
      return this;
    }

    /**
     * Adds the provided {@link ViterbiFeatureExtractor}(s) to the set of extractors to the used in vector construction.
     */
    @SafeVarargs
    public final Builder<Stage, State> addFeatureExtractors(ViterbiFeatureExtractor<Stage, State>... viterbiFeatureExtractors) {
      return addFeatureExtractors(Arrays.asList(viterbiFeatureExtractors));
    }

    /**
     * Adds the provided collection of {@link ViterbiFeatureExtractor}s to the set of extractors to the vector configuration.
     */
    public final Builder<Stage, State> addFeatureExtractors(Collection<ViterbiFeatureExtractor<Stage, State>> viterbiFeatureVectorExtractors) {
      viterbiFeatureVectorExtractors.forEach(this::addFeatureExtractor);
      return this;
    }

    public final ViterbiFeatureVectorExtractor<Stage, State> build() {
      return new ViterbiFeatureVectorExtractor<>(this);
    }
  }

  /**
   * {@link Collector} implementation allowing {@link ViterbiFeatureVector}s to be the target of stream reduce operations.
   */
  static final class FeatureCollector implements Collector<Pair<String, Double>, ViterbiFeatureVector.Builder, ViterbiFeatureVector> {

    @Override
    public Supplier<ViterbiFeatureVector.Builder> supplier() {
      return ViterbiFeatureVector.Builder::new;
    }

    @Override
    public BiConsumer<ViterbiFeatureVector.Builder, Pair<String, Double>> accumulator() {
      return (builder, pair) -> {
        checkArgument(!builder.containsFeature(pair.first()));
        builder.addFeature(pair.first(), pair.second());
      };
    }

    @Override
    public BinaryOperator<ViterbiFeatureVector.Builder> combiner() {
      return ViterbiFeatureVector.Builder::addFeatures;
    }

    @Override
    public Function<ViterbiFeatureVector.Builder, ViterbiFeatureVector> finisher() {
      return ViterbiFeatureVector.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return newHashSet(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
  }
}
