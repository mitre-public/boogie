package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.WaypointConverter;
import org.mitre.tdp.boogie.arinc.v18.WaypointValidator;

/**
 * Factory class for common instantiations of {@link ConvertingArincRecordConsumer}s.
 * <br>
 * Note that the consumer classes do allow hybridization of generic->specific record converter implementations (with some smart
 * functional composition) - however we provide as standard parts of the API only single-version factory implementations.
 */
public final class ConvertingArincRecordConsumerFactory {

  private ConvertingArincRecordConsumerFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class");
  }

  /**
   * Factory instantiation of a {@link ConvertingArincRecordConsumer} using all ARINC Version 18 adapter and conversion classes
   * for the {@link ArincRecord} -> boogie.arinc.model data classes.
   */
  public static ConvertingArincRecordConsumer withV18Conversions() {
    return new ConvertingArincRecordConsumer.Builder()
        .airportDelegator(new AirportValidator())
        .airportConverter(new AirportConverter())
        .airwayLegDelegator(new AirwayLegValidator())
        .airwayLegConverter(new AirwayLegConverter())
        .localizerGlideSlopeDelegator(new LocalizerGlideSlopeValidator())
        .localizerGlideSlopeConverter(new LocalizerGlideSlopeConverter())
        .ndbNavaidDelegator(new NdbNavaidValidator())
        .ndbNavaidConverter(new NdbNavaidConverter())
        .procedureLegDelegator(new ProcedureLegValidator())
        .procedureLegConverter(new ProcedureLegConverter())
        .runwayDelegator(new RunwayValidator())
        .runwayConverter(new RunwayConverter())
        .vhfNavaidDelegator(new VhfNavaidValidator())
        .vhfNavaidConverter(new VhfNavaidConverter())
        .waypointDelegator(new WaypointValidator())
        .waypointConverter(new WaypointConverter())
        .build();
  }
}
