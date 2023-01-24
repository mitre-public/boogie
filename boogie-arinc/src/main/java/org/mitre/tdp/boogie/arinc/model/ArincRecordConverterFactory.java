package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.HoldingPatternConverter;

/**
 * Factory class for common instantiations of {@link ConvertingArincRecordConsumer}s.
 * <br>
 * Note that the consumer classes do allow hybridization of generic->specific record converter implementations (with some smart
 * functional composition) - however we provide as standard parts of the API only single-version factory implementations.
 */
public final class ArincRecordConverterFactory {

  private ArincRecordConverterFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class");
  }

  private static ConvertingArincRecordConsumer.Builder standardConsumer() {
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
        .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .holdingPatternDelegator(new HoldingPatternValidator());
  }

  private static ConvertingArincRecordMapper.Builder standardMapper() {
    return new ConvertingArincRecordMapper.Builder()
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
        .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .holdingPatternDelegator(new GnssLandingSystemValidator());
  }

  /**
   * Factory instantiation of a {@link ConvertingArincRecordConsumer} using all ARINC Version 18 adapter and conversion classes
   * for the {@link ArincRecord} -> boogie.arinc.model data classes.
   * <br>
   * The V18 converters work perfectly fine with the V19 record specifications in {@link ArincVersion#V19} (as they share the same
   * fieldsets and overall structure - albeit with slightly different contents && holding patterns). However, because
   * the newer holding pattern has new fields, a new consumer bust me bade for 18 vs 19. However, the same validator works
   * for both 19/18 holds as only optional fields were added.
   */
  public static ConvertingArincRecordConsumer consumerForVersion(ArincVersion version) {
    switch (version) {
      case V18:
        return standardConsumer()
            .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter())
            .build();
      case V19:
        return standardConsumer()
            .holdingPatternConverter(new HoldingPatternConverter())
            .build();
      default:
        throw new UnsupportedOperationException("No factory method support for provided pre-canned version spec: ".concat(version.name()));
    }
  }

  /**
   * Factory instantiation of a {@link ConvertingArincRecordMapper} using all ARINC Version 18 adapter and conversion classes for
   * the {@link ArincRecord} -> boogie.arinc.model data classes.
   * <br>
   * The V18 converters work perfectly fine with the V19 record specifications in {@link ArincVersion#V19} (as they share the same
   * fieldsets and overall structure - albeit with slightly different contents&& holding patterns). However, because
   * the newer holding pattern has new fields, a new consumer bust me bade for 18 vs 19. However, the same validator works
   * for both 19/18 holds as only optional fields were added.
   */
  public static ConvertingArincRecordMapper mapperForVersion(ArincVersion version) {
    switch (version) {
      case V18:
        return standardMapper()
            .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter())
            .build();
      case V19:
        return standardMapper()
            .holdingPatternConverter(new HoldingPatternConverter())
            .build();
      default:
        throw new UnsupportedOperationException("No factory method support for provided pre-canned version spec: ".concat(version.name()));
    }
  }
}
