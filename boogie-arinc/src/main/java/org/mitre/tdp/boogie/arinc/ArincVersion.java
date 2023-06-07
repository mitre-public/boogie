package org.mitre.tdp.boogie.arinc;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v19.field.RouteTypeQualifier;

import com.google.common.collect.ImmutableMap;

/**
 * Pre-configured set of parsers for various well-known ARINC spec types and record formats.
 *
 * <p>This class explicitly <i>does not</i> return pre-configured parsers for all the potential record types that a user might
 * care about within a version nor does it provide pre-configured parsers for all version of 424 data.
 *
 * <p>However it is relatively simple to add new specs/parsers as need if we want to increase the pre-configured support.
 */
public enum ArincVersion {

  /**
   * Returns a static implementation of a parser for V18 ARINC 424 data.
   */
  V18(new AirportSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v18.HoldingPatternSpec()
  ),
  /**
   * Returns a static implementation of a parser for V19 ARINC 424 data.
   *
   * <p>From the perspective of the Boogie front-end parsers there is no difference in any of the parsing logic for the V19 record
   * contents other than for procedures where a few more categorical {@link RouteTypeQualifier}s were added.
   */
  V19(new AirportSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v19.HoldingPatternSpec()
  );

  private static final ImmutableMap<String, ArincVersion> LOOKUP = ImmutableMap.<String, ArincVersion>builder()
      .put(V18.name(), V18)
      .put(V19.name(), V19)
      .build();

  private final List<RecordSpec> specs;

  ArincVersion(RecordSpec... specs) {
    this.specs = List.of(specs);
  }

  /**
   * Implements a "safe" {@link ArincVersion} parse step which returns {@link Optional#empty()} if the string doesn't match one
   * of the known versions.
   */
  public static Optional<ArincVersion> parse(String version) {
    return ofNullable(version).map(String::toUpperCase).map(LOOKUP::get);
  }

  /**
   * Return the collection of specs owned by Boogie which make up the supported record types for the given 424 version.
   */
  public List<RecordSpec> specs() {
    return specs;
  }

  @Deprecated
  public ArincRecordParser parser() {
    return ArincRecordParser.standard(specs);
  }
}
