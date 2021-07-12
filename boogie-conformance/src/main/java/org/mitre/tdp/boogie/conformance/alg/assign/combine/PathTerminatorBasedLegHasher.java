package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.mitre.tdp.boogie.PathTerm.AF;
import static org.mitre.tdp.boogie.PathTerm.CA;
import static org.mitre.tdp.boogie.PathTerm.CF;
import static org.mitre.tdp.boogie.PathTerm.CI;
import static org.mitre.tdp.boogie.PathTerm.DF;
import static org.mitre.tdp.boogie.PathTerm.IF;
import static org.mitre.tdp.boogie.PathTerm.RF;
import static org.mitre.tdp.boogie.PathTerm.TF;
import static org.mitre.tdp.boogie.PathTerm.VA;
import static org.mitre.tdp.boogie.PathTerm.VI;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

import com.google.common.base.Preconditions;

/**
 * This is an example of a hasher which can be used in situ with a {@link HashCombinationStrategy} to generate hashes for legs
 * based on their expected unique contents (generally determined by their {@link PathTerm}/{@link LegType}).
 *
 * If you scan a cycle of data across CIFP/LIDO or some other source combination the most frequent leg types are:
 *
 * ["VR" 105]
 * ["HA" 260]
 * ["VD" 281]
 * ["CR" 619]
 * ["CD" 1495]
 * ["FC" 3880]
 * ["FD" 4005]
 * ["FM" 4140]
 * ["RF" 4874]
 * ["PI" 4915]
 * ["VI" 4941]
 * ["FA" 8500]
 * ["VM" 9557]
 * ["VA" 12532]
 * ["HF" 14508]
 * ["CI" 15133]
 * ["AF" 20196]
 * ["HM" 36691]
 * ["CA" 37787]
 * ["DF" 56860]
 * ["CF" 73486]
 * ["IF" 198425]
 * ["TF" 457939]
 *
 * Generally speaking this class prioritizes hash-based mappings for the most common leg types and defaults the others to
 * using the native leg hashCode.
 */
public final class PathTerminatorBasedLegHasher implements Function<FlyableLeg, Integer> {

  public final HashMap<PathTerm, Function<FlyableLeg, Integer>> hashers;

  private PathTerminatorBasedLegHasher(Map<PathTerm, Function<FlyableLeg, Integer>> overrides) {
    this.hashers = new HashMap<>();
    this.initialize();
    this.hashers.putAll(overrides);
  }

  /**
   * Returns a new instance of a {@link PathTerminatorBasedLegHasher} with the default hashing functions per leg type.
   */
  public static PathTerminatorBasedLegHasher newInstance() {
    return withOverriddenHashers(new HashMap<>());
  }

  /**
   * Returns a new instance of a {@link PathTerminatorBasedLegHasher} with the supplied overrides.
   */
  public static PathTerminatorBasedLegHasher withOverriddenHashers(Pair<PathTerm, Function<FlyableLeg, Integer>>... overrides) {
    return withOverriddenHashers(Stream.of(overrides).collect(Collectors.toMap(Pair::first, Pair::second)));
  }

  /**
   * Returns a new instance of a {@link PathTerminatorBasedLegHasher} with the supplied mapping of overrides.
   */
  public static PathTerminatorBasedLegHasher withOverriddenHashers(Map<PathTerm, Function<FlyableLeg, Integer>> overrides) {
    return new PathTerminatorBasedLegHasher(overrides);
  }

  @Override
  public Integer apply(FlyableLeg flyableLeg) {
    return hashers.getOrDefault(flyableLeg.current().type(), Object::hashCode).apply(flyableLeg);
  }

  private void initialize() {
    hashers.put(IF, this::ifHasher);
    hashers.put(TF, this::tfHasher);
    hashers.put(CF, this::cfHasher);
    hashers.put(DF, this::dfHasher);
    // FA, FC, FD, FM
    hashers.put(CA, this::caHasher);
    // CD
    hashers.put(CI, this::ciHasher);
    hashers.put(RF, this::rfHasher);
    hashers.put(AF, this::afHasher);
    hashers.put(VA, this::vaHasher);
    // VD
    hashers.put(VI, this::viHasher);
    // VM, VR, PR, HA, HF, HM
  }

  private static class Hasher {
    private final HashCodeBuilder builder = new HashCodeBuilder();

    /**
     * The resolution to use when comparing lat/lon values.  Used to combine fixes from different sources with slightly different coordinates.
     * <p>
     * 1e-3 translates to ~111 meters at equator, so only fixes at most that far away will have the same hash.
     */
    private static final double HASH_RESOLUTION = 1e-3;

    public Hasher append(Object that) {
      builder.append(that);
      return this;
    }

    public Hasher withPathTerminator(Leg leg) {
      builder.append(leg.pathTerminator().identifier());
      builder.append(Math.round(leg.pathTerminator().latitude() / HASH_RESOLUTION));
      builder.append(Math.round(leg.pathTerminator().longitude() / HASH_RESOLUTION));
      return this;
    }

    public Hasher withRecommendedNavaid(Leg leg) {
      builder.append(leg.recommendedNavaid().map(Fix::identifier).orElse(null));
      builder.append(leg.recommendedNavaid().map(Fix::latLong).orElse(null));
      return this;
    }

    public Hasher withArcCenter(Leg leg) {
      builder.append(leg.centerFix().map(Fix::identifier).orElseThrow(IllegalStateException::new));
      builder.append(leg.centerFix().map(Fix::latLong).orElseThrow(IllegalStateException::new));
      return this;
    }

    public int buildHash() {
      return builder.build();
    }
  }

  private Hasher newHasher(FlyableLeg flyableLeg) {
    return new Hasher().append(flyableLeg.current().type());
  }

  private int ifHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(IF));
    return newHasher(flyableLeg).withPathTerminator(flyableLeg.current()).buildHash();
  }

  private int tfHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(TF));
    Hasher hasher = newHasher(flyableLeg).withPathTerminator(flyableLeg.current());
    return flyableLeg.previous().filter(l -> l.pathTerminator() != null).isPresent()
        // allow null previous fixes because the scorer lets them pass as well I think
        // this is likely an issue with CIFP airways on the boundary of countries
        ? hasher.withPathTerminator(flyableLeg.previous().orElseThrow(IllegalStateException::new)).buildHash()
        : hasher.buildHash();
  }

  private int cfHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(CF));
    return newHasher(flyableLeg)
        .withPathTerminator(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .append(flyableLeg.current().routeDistance().orElse(null))
        .buildHash();
  }

  private int dfHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(DF));
    return newHasher(flyableLeg).withPathTerminator(flyableLeg.current()).buildHash();
  }

  private int caHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(CA));
    return newHasher(flyableLeg).append(flyableLeg.current().outboundMagneticCourse().orElse(null)).buildHash();
  }

  private int ciHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(CI));
    return newHasher(flyableLeg).append(flyableLeg.current().outboundMagneticCourse().orElse(null)).buildHash();
  }

  private int rfHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(RF));
    return newHasher(flyableLeg)
        .withPathTerminator(flyableLeg.current())
        .withArcCenter(flyableLeg.current())
        .withPathTerminator(flyableLeg.previous().orElseThrow(IllegalStateException::new))
        .append(flyableLeg.current().turnDirection().orElseThrow(IllegalStateException::new))
        .buildHash();
  }

  private int afHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(AF));
    return newHasher(flyableLeg)
        .withPathTerminator(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().turnDirection())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .buildHash();
  }

  private int vaHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(VA));
    return newHasher(flyableLeg)
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .append(flyableLeg.current().altitudeConstraint().flatMap(AltitudeLimit::altitudeLimit).orElse(null))
        .buildHash();
  }

  private int viHasher(FlyableLeg flyableLeg) {
    Preconditions.checkArgument(flyableLeg.current().type().equals(VI));
    return newHasher(flyableLeg).append(flyableLeg.current().outboundMagneticCourse().orElse(null)).buildHash();
  }
}
