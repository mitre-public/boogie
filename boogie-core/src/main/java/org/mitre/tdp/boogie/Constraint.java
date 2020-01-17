package org.mitre.tdp.boogie;

public interface Constraint {

  /**
   * The target value of the constraint.
   */
  Double value();

  /**
   * Returns the type of altitude constraint.
   */
  ConstraintBehavior behavior();
}
