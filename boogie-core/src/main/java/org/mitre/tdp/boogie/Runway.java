package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.caasd.commons.LatLong;

public interface Runway {

  /**
   * The identifier of the runway. Typically the name is based on the magnetic heading of the runway from arrival->departure
   * (alternatively origin->reciprocal/base->reciprocal) end.
   */
  String runwayIdentifier();

  String airportIdentifier();

  String airportRegion();

  /**
   * The true course of the runway in degrees.
   */
  Optional<Double> trueCourse();

  /**
   * The width of the runway in feet - if provided in official sources.
   * <br>
   * Notable exceptions where a runway is defined with no width include:
   * <br>
   * 1. MITREs historical Jeppesen data - where width was not written out.
   * 2. Runways which aren't standard concrete/gravel/grass landing strips - a la 'runway's at seaplane bases. Certain NAV data
   * sources can contain these (e.g. CIFP).
   */
  Optional<Double> width();

  /**
   * The length of the runway in feet.
   * <br>
   * Implementations should <i>not</i> care about whether this is the official published value or one computed based on the feet
   * between the {@link #arrivalRunwayEnd()} and {@link #departureRunwayEnd()} (so long as those account for displaced thresholds).
   */
  Optional<Double> length();

  /**
   * The position of the end of the runway aircraft arrive over.
   */
  LatLong arrivalRunwayEnd();

  /**
   * The position of the end of the runway aircraft would depart over. This value is optional as some runways don't have well
   * defined reciprocal ends - though most standard ones do.
   */
  Optional<LatLong> departureRunwayEnd();
}
