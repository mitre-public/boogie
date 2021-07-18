package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
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
   * <br>
   * The V18 converters work perfectly fine with the V19 record specifications in {@link ArincVersion#V19} (as they share the same
   * fieldsets and overall structure - albeit with slightly different contents). So this can be used downstream of either versions
   * {@link ArincFileParser} implementation and therefore passes through the same branch of the {@link ArincVersion} switch.
   */
  public static ConvertingArincRecordConsumer forArincVersion(ArincVersion version) {
    switch (version) {
      case V18:
      case V19:
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
      default:
        throw new UnsupportedOperationException("No factory method support for provided pre-canned version spec: ".concat(version.name()));
    }
  }
}
