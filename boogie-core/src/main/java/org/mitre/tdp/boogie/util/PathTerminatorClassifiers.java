package org.mitre.tdp.boogie.util;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

/**
 * Collection of standard classifiers related to {@link Leg}s but working against their {@link Leg#pathTerminator()}s.
 */
public final class PathTerminatorClassifiers {

  private PathTerminatorClassifiers() {
    throw new IllegalStateException("Unable to instantiate static utility class.");
  }

  public static boolean isArc(PathTerminator pathTerminator) {
    requireNonNull(pathTerminator);
    return PathTerminator.RF.equals(pathTerminator) || PathTerminator.AF.equals(pathTerminator);
  }
}
