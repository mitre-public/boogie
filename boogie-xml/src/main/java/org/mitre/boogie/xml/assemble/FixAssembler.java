package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Fix;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincFixRecord;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;

/**
 * Assembler interface for converting a single fix-like XML record into a client-defined fix data model.
 *
 * <p>This interface can be used with {@link FixAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Fix}
 * implementations that can be used with other Boogie algorithms.
 *
 * <p>Callers are responsible for iterating over records and collecting results. This mirrors the ARINC
 * {@code FixAssembler} pattern where the assembler converts one record at a time.
 */
public interface FixAssembler<F> {

  static FixAssembler<Fix> standard() {
    return withStrategy(FixAssemblyStrategy.standard());
  }

  static <F> FixAssembler<F> withStrategy(FixAssemblyStrategy<F> strategy) {
    return new Standard<>(strategy);
  }

  F assemble(ArincFixRecord record);

  final class Standard<F> implements FixAssembler<F> {

    private final FixAssemblyStrategy<F> strategy;

    private Standard(FixAssemblyStrategy<F> strategy) {
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public F assemble(ArincFixRecord record) {
      if (record instanceof ArincWaypoint wp) {
        return strategy.convertWaypoint(wp);
      } else if (record instanceof ArincNdbNavaid ndb) {
        return strategy.convertNdbNavaid(ndb);
      } else if (record instanceof ArincVhfNavaid vhf) {
        return strategy.convertVhfNavaid(vhf);
      } else if (record instanceof ArincPortInfo airport) {
        return strategy.convertAirport(airport);
      } else if (record instanceof ArincRunway runway) {
        return strategy.convertRunway(runway);
      } else if (record instanceof ArincAirportGate gate) {
        return strategy.convertAirportGate(gate);
      } else if (record instanceof ArincHelipad helipad) {
        return strategy.convertHelipad(helipad);
      } else if (record instanceof ArincLocalizerGlideSlope loc) {
        return strategy.convertLocalizerGlideSlope(loc);
      } else if (record instanceof ArincLocalizerGlideslopeMarker marker) {
        return strategy.convertMarker(marker);
      } else if (record instanceof ArincGnssLandingSystem gls) {
        return strategy.convertGnssLandingSystem(gls);
      } else {
        return strategy.convertPoint(record.pointInfo(), record.recordInfo());
      }
    }
  }
}
