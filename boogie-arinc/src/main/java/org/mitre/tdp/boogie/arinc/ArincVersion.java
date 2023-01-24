package org.mitre.tdp.boogie.arinc;

import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.field.RouteTypeQualifier;

/**
 * Pre-configured set of parsers for various well-known ARINC spec types and record formats.
 * <br>
 * This class explicitly <i>does not</i> return pre-configured parsers for all of the potential record types that a user might
 * care about within a version nor does it provide pre-configured parsers for all version of 424 data. However it is relatively
 * simple to add new specs/parsers as need if we want to increase the pre-configured support.
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
   * Returns a static implementation of a parser for V19 ARINC 424 data. From the perspective of the Boogie front-end parsers
   * there is no difference in any of the parsing logic for the V19 record contents other than for procedures where a few more
   * categorical {@link RouteTypeQualifier}s were added.
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

  private final ArincRecordParser parser;

  ArincVersion(RecordSpec... specs) {
    this.parser = new ArincRecordParser(specs);
  }

  /**
   * Returns the internally constructed parser instance.
   */
  public ArincRecordParser parser() {
    return parser;
  }
}
