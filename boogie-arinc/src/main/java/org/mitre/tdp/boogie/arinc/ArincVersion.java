package org.mitre.tdp.boogie.arinc;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegSpec;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemSpec;
import org.mitre.tdp.boogie.arinc.v18.Header01Spec;
import org.mitre.tdp.boogie.arinc.v18.HeliportSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v19.field.RouteTypeQualifier;
import org.mitre.tdp.boogie.arinc.v21.HelipadSpec;

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
  V18(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v18.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V18 ARINC 424 data for only nav records
   */
  V18_NAV(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v18.HoldingPatternSpec(),
      new HeliportSpec()
  ),

  /**
   * This will only parse the airspace content
   */
  V18_AIRSPACE(new Header01Spec(),
      new AirportSpec(),
      new RunwaySpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V19 ARINC 424 data.
   *
   * <p>From the perspective of the Boogie front-end parsers there is no difference in any of the parsing logic for the V19 record
   * contents other than for procedures where a few more categorical {@link RouteTypeQualifier}s were added.
   */
  V19(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v19.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HeliportSpec()
  ),

  /**
   * This is a version 19 for only navigation records
   */
  V19_NAV(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v19.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V20 ARINC 424 data.
   */
  V20(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v20.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v20.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V20 ARINC 424 data.
   */
  V20_NAV(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v20.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v20.ProcedureLegSpec(),
      new GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V21 ARINC 424 data.
   */
  V21(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v20.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v21.ProcedureLegSpec(),
      new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HelipadSpec(),
      new org.mitre.tdp.boogie.arinc.v21.HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V21 ARINC 424 data for only navigation relevant fields.
   */
  V21_NAV(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v20.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v19.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v21.ProcedureLegSpec(),
      new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new HelipadSpec(),
      new org.mitre.tdp.boogie.arinc.v21.HeliportSpec()
  ),

  /**
   * Returns a static implementation of a parser for V22 Arinc 424 data.
   */
  V22(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v22.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v22.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v22.ProcedureLegSpec(),
      new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new ControlledAirspaceLegSpec(),
      new FirUirLegSpec(),
      new HelipadSpec(),
      new org.mitre.tdp.boogie.arinc.v21.HeliportSpec()
  ),

  /**
   * This is a parser that does not parse the airspace's into memory.
   */
  V22_NAV(new Header01Spec(),
      new AirportSpec(),
      new AirportPrimaryExtensionSpec(),
      new org.mitre.tdp.boogie.arinc.v22.RunwaySpec(),
      new LocalizerGlideSlopeSpec(),
      new org.mitre.tdp.boogie.arinc.v22.AirwayLegSpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new org.mitre.tdp.boogie.arinc.v22.ProcedureLegSpec(),
      new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemSpec(),
      new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec(),
      new HelipadSpec(),
      new org.mitre.tdp.boogie.arinc.v21.HeliportSpec()
  );

  private static final Map<String, ArincVersion> LOOKUP = Map.ofEntries(
      Map.entry(V18.name(), V18),
      Map.entry(V18_NAV.name(), V18_NAV),
      Map.entry(V18_AIRSPACE.name(), V18_AIRSPACE),
      Map.entry(V19.name(), V19),
      Map.entry(V19_NAV.name(), V19_NAV),
      Map.entry(V20.name(), V20),
      Map.entry(V20_NAV.name(), V20_NAV),
      Map.entry(V21.name(), V21),
      Map.entry(V21_NAV.name(), V21_NAV),
      Map.entry(V22.name(), V22),
      Map.entry(V22_NAV.name(), V22_NAV)
  );

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
