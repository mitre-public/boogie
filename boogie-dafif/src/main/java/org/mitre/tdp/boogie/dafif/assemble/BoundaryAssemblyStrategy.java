package org.mitre.tdp.boogie.dafif.assemble;

import java.util.List;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;

/**
 * Strategy for converting DAFIF boundary metadata and complete, ordered segment collections.
 * The standard strategy uses the ICAO code as the area namespace and the boundary identification as
 * the identifier. Types 08 and 12 become FIR and UIR; remaining boundary types use CONTROLLED.
 * Generalized (G) boundaries use great-circle edges between their supplied approximation points.
 */
public interface BoundaryAssemblyStrategy<A, S> {

  static BoundaryAssemblyStrategy<Airspace, AirspaceSequence> standard() {
    return new Standard();
  }

  A convertBoundary(DafifBoundaryParent parent, List<S> sequences);

  /**
   * Receives all source endpoints, including shapes unsupported by the standard Airspace model.
   */
  List<S> convertBoundarySequences(List<DafifBoundarySegment> segments);

  final class Standard implements BoundaryAssemblyStrategy<Airspace, AirspaceSequence> {

    private Standard() {
    }

    @Override
    public Airspace convertBoundary(DafifBoundaryParent parent, List<AirspaceSequence> sequences) {
      AirspaceType type = switch (parent.boundaryType()) {
        case FIR -> AirspaceType.FIR;
        case UIR -> AirspaceType.UIR;
        default -> AirspaceType.CONTROLLED;
      };
      return Airspace.builder()
          .area(parent.icaoCode())
          .identifier(parent.boundaryIdentification())
          .airspaceType(type)
          .altitudeLimit(AirspaceAltitudeRange.INSTANCE.apply(parent.lowerAltitude(), parent.upperAltitude()))
          .sequences(sequences)
          .build();
    }

    @Override
    public List<AirspaceSequence> convertBoundarySequences(List<DafifBoundarySegment> segments) {
      return AirspaceGeometry.convert(segments.stream().map(AirspaceGeometry::from).toList());
    }
  }
}
