package org.mitre.tdp.boogie.contract.routes;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.util.Declinations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableFix.class)
@JsonDeserialize(as = ImmutableFix.class)
public interface Fix {
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

  LatLong latLong();

  default double latitude() {
    return latLong().latitude();
  }

  default double longitude() {
    return latLong().longitude();
  }

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

  default org.mitre.caasd.commons.LatLong toCommons() {
    return org.mitre.caasd.commons.LatLong.of(latitude(), longitude());
  }
}
