package org.mitre.tdp.boogie.arinc;

import org.mitre.tdp.boogie.arinc.v18.spec.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.spec.AirwaySpec;
import org.mitre.tdp.boogie.arinc.v18.spec.GlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.spec.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.spec.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.spec.TransitionSpec;
import org.mitre.tdp.boogie.arinc.v18.spec.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.spec.WaypointSpec;

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
      new GlideSlopeSpec(),
      new AirwaySpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new TransitionSpec());

  private final ArincParser parser;

  ArincVersion(RecordSpec... specs) {
    this.parser = new ArincParser(specs);
  }

  /**
   * Returns the internally constructed parser instance.
   */
  public ArincParser parser() {
    return parser;
  }
}
