package org.mitre.tdp.boogie.viterbi;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Named container class providing access to values associated with named features for use in Viterbi.
 *
 * Typically these are extracted via {@link ViterbiFeatureVectorExtractor} and then transformed into likelihood values via
 * {@link ViterbiFeatureVectorScorer} and then used within the {@link ViterbiTagger}.
 */
public final class ViterbiFeatureVector {

  /**
   * The internal immutable mapping from string feature name to feature value.
   *
   * Note that the use of an {@link ImmutableMap} ensures a deterministic iteration order.
   */
  private final ImmutableMap<String, Double> features;

  private ViterbiFeatureVector(Builder builder) {
    this.features = ImmutableMap.copyOf(requireNonNull(builder.features));
  }

  /**
   * Returns true if the vector contains a feature with the provided name.
   */
  public boolean containsFeature(String featureName) {
    return features.containsKey(requireNonNull(featureName));
  }

  /**
   * Returns the double value associated with the given feature name.
   */
  public double featureValue(String featureName) {
    return requireNonNull(features.get(featureName), "No value associated with feature.");
  }

  /**
   * Returns the {@link ViterbiFeatureVector} as a simple mutable {@link LinkedHashMap} retaining the feature insertion ordering.
   */
  public LinkedHashMap<String, Double> asMap() {
    return new LinkedHashMap<>(features);
  }

  /**
   * Returns a new {@link ViterbiFeatureVector} as generated from an input mapping of feature name to feature value.
   */
  public static ViterbiFeatureVector fromMap(Map<String, Double> featureMap) {
    return new Builder().addFeatures(featureMap).build();
  }

  /**
   * The builder is a simple decorator for a {@link LinkedHashMap} with methods allowing chainable addition of features to the
   * vector with some lightweight guard rails to prevent overwriting the value of an already inserted feature.
   *
   * Generally speaking downstream of this {@link ViterbiFeatureVectorExtractor} and {@link ViterbiFeatureExtractor} both have
   * reasonable guard rails themselves around configuring the set of derived features that will be populated - so if we wanted
   * to open this class up to a bit more flexibility (i.e. allowing updates to features - via a special separate signature we
   * could do so.
   */
  public static final class Builder {

    /**
     * Use a {@link LinkedHashMap} to maintain the insertion order of the features in iteration.
     */
    private final LinkedHashMap<String, Double> features = new LinkedHashMap<>();

    /**
     * Returns true if the current feature-set contains a feature with the provided name.
     */
    public boolean containsFeature(String feature) {
      return features.containsKey(requireNonNull(feature));
    }

    /**
     * Adds the provided feature by name to the {@link ViterbiFeatureVector} configuration. Won't allow addition of duplicates
     * features or updates to existing already set features.
     */
    public Builder addFeature(String key, Double value) {
      checkArgument(!features.containsKey(key), "Cannot overwrite already added feature.");
      this.features.put(requireNonNull(key), requireNonNull(value));
      return this;
    }

    /**
     * Adds the provided mapping of features to the {@link ViterbiFeatureVector} configuration.
     */
    public Builder addFeatures(Map<String, Double> features) {
      features.forEach(this::addFeature);
      return this;
    }

    /**
     * Combines the features of one builder with those from another returning one builder representing the union of both sets
     * of features.
     */
    public Builder addFeatures(Builder builder) {
      return addFeatures(builder.features);
    }

    public ViterbiFeatureVector build() {
      return new ViterbiFeatureVector(this);
    }
  }
}
