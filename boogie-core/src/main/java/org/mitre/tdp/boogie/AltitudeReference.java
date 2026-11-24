package org.mitre.tdp.boogie;

/**
 * The surface or datum an altitude limit is measured from.
 *
 * <p>The reference decides which of a trajectory's vertical values the limit is compared with: its altitude above mean
 * sea level, its height above the terrain, or its pressure altitude.
 */
public enum AltitudeReference {
  /**
   * Feet above mean sea level.
   * <p>
   * Compared with a trajectory's altitude above mean sea level: a barometric altitude corrected to the local
   * altimeter setting, or a geometric altitude.
   */
  MSL,
  /**
   * Feet above ground level.
   * <p>
   * Compared with a trajectory's altitude above mean sea level minus the terrain elevation beneath it.
   */
  AGL,
  /**
   * Feet of pressure altitude: height in the standard atmosphere, with the altimeter set to 29.92 inHg (1013.25 hPa).
   * <p>
   * Flight levels are pressure altitudes in hundreds of feet. Compared with a trajectory's pressure altitude, which
   * is what transponders report.
   */
  PRESSURE
}
