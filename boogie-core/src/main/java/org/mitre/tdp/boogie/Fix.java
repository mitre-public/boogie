package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;

public interface Fix extends HasPosition, Infrastructure {

  /**
   * The localized {@link MagneticVariation} at the fix.
   */
  MagneticVariation magneticVariation();

  /**
   * The elevation in feet of the fix (if a navaid).
   */
  Optional<Float> elevation();
}