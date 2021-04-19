package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.v18.spec.record.AirportSpec;
import org.mitre.tdp.boogie.v18.spec.record.AirwaySpec;
import org.mitre.tdp.boogie.v18.spec.record.GlideSlopeSpec;
import org.mitre.tdp.boogie.v18.spec.record.NdbNavaidSpec;
import org.mitre.tdp.boogie.v18.spec.record.RunwaySpec;
import org.mitre.tdp.boogie.v18.spec.record.TransitionSpec;
import org.mitre.tdp.boogie.v18.spec.record.VhfNavaidSpec;
import org.mitre.tdp.boogie.v18.spec.record.WaypointSpec;

/**
 * Enum for a well known/formatted specification for ARINC files.
 */
public enum ArincVersion implements ArincSpec, ArincParser {
  /**
   * Implementation of an {@link ArincSpec} for use parsing data of the ARINC V18 specification.
   */
  V18(new AirportSpec(),
      new RunwaySpec(),
      new GlideSlopeSpec(),
      new AirwaySpec(),
      new NdbNavaidSpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new TransitionSpec());

  private final List<RecordSpec> specs;

  ArincVersion(RecordSpec... specs) {
    this(Arrays.asList(specs));
  }

  ArincVersion(List<RecordSpec> specs) {
    this.specs = specs;
  }

  @Override
  public List<RecordSpec> recordSpecs() {
    return specs;
  }

  @Override
  public ArincSpec arincSpec() {
    return this;
  }
}
