package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

public class LegHashers {

  private LegHashers() {
  }

  /**
   * A hasher which simply leverages the existing {@link Leg#hashCode()} of the records.
   */
  public static LegHasher byHashCode() {
    return leg -> Optional.of(leg.hashCode());
  }

  /**
   * A hasher generating a hash based on the {@link Fix#identifier()} of the leg path terminator.
   */
  public static LegHasher byIdentifier() {
    return leg -> Optional.ofNullable(leg.pathTerminator()).map(Fix::identifier).map(String::hashCode);
  }

  /**
   * A hasher generating a hash based on the lat/lon location of the path terminator of the leg.
   */
  public static LegHasher byLocation() {
    return leg -> Optional.ofNullable(leg.pathTerminator()).map(Fix::latLong).map(LatLong::hashCode);
  }

  /**
   * A hasher generating a hash based on the {@link PathTerm} of the leg.
   */
  public static LegHasher byType() {
    return leg -> Optional.of(leg.type().hashCode());
  }

  /**
   * A hasher generating a hash based on whether the leg is {@link PathTerm#isConcrete()}.
   */
  public static LegHasher isConcrete() {
    return leg -> Optional.of(leg.type()).map(type -> Boolean.valueOf(type.isConcrete()).hashCode());
  }

  /**
   * A hasher generating a hash based on the {@link Leg#centerFix()} when {@link PathTerm#isArc()}.
   */
  public static LegHasher byArcCenter() {
    return leg -> Optional.of(leg).flatMap(l -> l.type().isArc() ? l.centerFix().map(Fix::latLong).map(LatLong::hashCode) : Optional.of(1));
  }
}
