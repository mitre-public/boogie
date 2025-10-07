package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.function.Function;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * This is a hasher that can be used in situ with a {@link HashCombinationStrategy} to generate hashes for legs
 * based on their expected unique contents (generally determined by their {@link PathTerminator}).
 */
public final class PathTerminatorBasedLegHasher implements Function<FlyableLeg, Integer> {

  private PathTerminatorBasedLegHasher() {
  }

  /**
   * Returns a new instance of a {@link PathTerminatorBasedLegHasher} with the hashing functions per leg type.
   */
  public static PathTerminatorBasedLegHasher newInstance() {
    return new PathTerminatorBasedLegHasher();
  }

  @Override
  public Integer apply(FlyableLeg flyableLeg) {
    return switch (flyableLeg.current().pathTerminator()) {
      case IF, TF, DF -> xfHash(flyableLeg);
      case CF -> cfHash(flyableLeg);
      case FA, FC, FD, FM -> fxHash(flyableLeg);
      case CA, VA -> xaHash(flyableLeg);
      case CR, VR -> xrHash(flyableLeg);
      case CD, VD -> xdHash(flyableLeg);
      case CI, VI, VM -> xiHash(flyableLeg);
      case RF -> rfHash(flyableLeg);
      case AF -> afHash(flyableLeg);
      case PI -> piHash(flyableLeg);
      case HA, HF, HM -> hxHash(flyableLeg);
    };
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

    public Hasher withAssociatedFix(Leg leg) {
      Fix associatedFix = leg.associatedFix().orElseThrow(IllegalStateException::new);
      builder.append(associatedFix.fixIdentifier());
      builder.append(FastMath.round(associatedFix.latitude() / HASH_RESOLUTION));
      builder.append(FastMath.round(associatedFix.longitude() / HASH_RESOLUTION));
      return this;
    }

    public Hasher withRecommendedNavaid(Leg leg) {
      leg.recommendedNavaid().ifPresent(n -> {
        builder.append(n.fixIdentifier());
        builder.append(FastMath.round(n.latitude() / HASH_RESOLUTION));
        builder.append(FastMath.round(n.longitude() / HASH_RESOLUTION));
      });
      return this;
    }

    public Hasher withArcCenter(Leg leg) {
      leg.centerFix().ifPresent(c -> {
        builder.append(c.fixIdentifier());
        builder.append(c.latitude()); //can be quite close so lets not round it
        builder.append(c.longitude());
      });
      return this;
    }

    public int buildHash() {
      return builder.build();
    }
  }

  private Hasher newHasher(FlyableLeg flyableLeg) {
    return new Hasher().append(flyableLeg.current().pathTerminator());
  }

  private int xfHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .buildHash();
  }

  private int cfHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .append(flyableLeg.current().routeDistance().orElse(null))
        .buildHash();
  }

  private int xaHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .append(flyableLeg.current().altitudeConstraint().lowerEndpoint())
        .buildHash();
  }

  private int rfHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .withArcCenter(flyableLeg.current())
        .withAssociatedFix(flyableLeg.previous().orElseThrow(IllegalStateException::new))
        .append(flyableLeg.current().turnDirection().orElseThrow(IllegalStateException::new))
        .buildHash();
  }

  private int afHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().turnDirection())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .buildHash();
  }

  private int xiHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg).append(flyableLeg.current().outboundMagneticCourse().orElse(null)).buildHash();
  }

  private int fxHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .buildHash();
  }

  private int xrHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .buildHash();
  }

  private int xdHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().routeDistance().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .buildHash();
  }

  private int piHash(FlyableLeg flyableLeg) {
    return newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .withRecommendedNavaid(flyableLeg.current())
        .append(flyableLeg.current().theta().orElse(null))
        .append(flyableLeg.current().rho().orElse(null))
        .append(flyableLeg.current().outboundMagneticCourse().orElse(null))
        .append(flyableLeg.current().turnDirection().orElse(null))
        .append(flyableLeg.current().routeDistance().orElse(null))
        .buildHash();
  }

  private int hxHash(FlyableLeg flyableLeg) {
    Hasher hasher = newHasher(flyableLeg)
        .withAssociatedFix(flyableLeg.current())
        .append(flyableLeg.current().turnDirection().orElse(null));
    flyableLeg.current().holdTime().ifPresent(hasher::append);
    flyableLeg.current().routeDistance().ifPresent(hasher::append);
    return hasher.buildHash();
  }
}
