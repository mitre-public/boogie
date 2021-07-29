package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.tdp.boogie.util.Declinations;

/**
 * A fix represents a generic named location in 2-3D space (elevation is optional).
 * <br>
 * Generally speaking "fixes" can be waypoints, NDB/VHF navaids, airports, or even runway ends. This interface is primarily meant
 * to be used in conjunction with the {@link Leg} information to represent all of the required information needed to know how to
 * fly a given leg.
 * <br>
 * All fixes should include their defined ICAO region - the assumption being that fix implementations should be unique per ICAO
 * region (e.g. a waypoint, NDB navaid, VHF navaid should all be uniquely identifiable by identifier, subclass, and region).
 */
public interface Fix extends HasPosition {

  /**
   * The (typically) 3-5 character name of the fix object.
   */
  String fixIdentifier();

  /**
   * The region which the fix exists in.
   * <br>
   * The general expectation here is that a given fix identifier will be <i>unique</i> within it's supplied region.
   * <br>
   * As an example: for fixes parsed from ARINC 424 data this is the ICAO region (K1, K7, PA, MM, EG, UT, etc.).
   */
  String fixRegion();

  /**
   * The localized {@link MagneticVariation} at the fix as published in official sources.
   */
  Optional<Double> publishedVariation();

  /**
   * Boogie provides the {@link Declinations} utility class to compute a modeled version of the declination at a fix based on the
   * WMM published by NOAA. Generally this information is required to convert the mostly magnetic courses in legs/etc. into true
   * courses for use in modeling.
   * <br>
   * Ideally this could be defaulted on the interface but the value to use is dependent on the publication date of the fix (as
   * there is a time component to the drift in the declination).
   */
  double modeledVariation();

  /**
   * Wraps the overridden interface contract methods as the more usable & functional {@link MagneticVariation} object.
   */
  default MagneticVariation magneticVariation() {
    return new MagneticVariation(publishedVariation().orElse(null), modeledVariation());
  }

  /**
   * The elevation in feet of the fix (typically only supplied for physical facilities - i.e. NDB/VHF navaids).
   */
  Optional<Double> elevation();
}