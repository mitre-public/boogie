package org.mitre.tdp.boogie;

import java.util.function.Predicate;

/**
 * Indicates a constraint at some given fix - this may be a speed or altitude constraint and may come in a variety
 * of forms (at_or_above, at_or_below, between, etc.) depending on the type of procedure its declared on.
 */
@FunctionalInterface
public interface Constraint extends Predicate<ConformablePoint> {
}
