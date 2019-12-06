package org.mitre.tdp.boogie;

/** Common interface for the altitude constraints along a {@link Leg}.
 *
 * Typically these mean to be */
public interface AltitudeConstraint {

  Double altitude1();

  Double altitude2();

  enum Descriptor{
    AT, AT_OR_BELOW, AT_OR_ABOVE
  }
}
