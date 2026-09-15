package org.mitre.tdp.boogie.dafif.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assembles DAFIF boundary parents and their ordered segments into airspaces.
 * When using the standard strategy, assembly logs and omits point, annular, open and disconnected airspaces that
 * {@link Airspace} cannot represent. Source coordinate gaps up to 0.1 NM are bridged with explicit
 * great-circle sequences, retaining both endpoints. Sequence numbers are regenerated in path order.
 * Custom strategies receive every parent and its source segments without these restrictions.
 */
public interface BoundaryAssembler<A> {

  static BoundaryAssembler<Airspace> standard() {
    return usingStrategy(BoundaryAssemblyStrategy.standard());
  }

  static <A, S> BoundaryAssembler<A> usingStrategy(BoundaryAssemblyStrategy<A, S> strategy) {
    return new Standard<>(strategy, strategy instanceof BoundaryAssemblyStrategy.Standard);
  }

  Stream<A> assemble(Collection<DafifBoundaryParent> parents, Collection<DafifBoundarySegment> segments);

  final class Standard<A, S> implements BoundaryAssembler<A> {

    private static final Logger LOG = LoggerFactory.getLogger(Standard.class);

    private final BoundaryAssemblyStrategy<A, S> strategy;
    private final boolean omitUnsupported;

    private Standard(BoundaryAssemblyStrategy<A, S> strategy, boolean omitUnsupported) {
      this.strategy = requireNonNull(strategy);
      this.omitUnsupported = omitUnsupported;
    }

    @Override
    public Stream<A> assemble(Collection<DafifBoundaryParent> parents, Collection<DafifBoundarySegment> segments) {
      Map<String, List<DafifBoundarySegment>> grouped = segments.stream()
          .collect(Collectors.groupingBy(DafifBoundarySegment::boundaryIdentification));
      return parents.stream().flatMap(parent -> {
        List<DafifBoundarySegment> ordered = grouped.getOrDefault(parent.boundaryIdentification(), List.of()).stream()
            .sorted(Comparator.comparingInt(DafifBoundarySegment::segmentNumber))
            .toList();
        try {
          return Stream.of(strategy.convertBoundary(parent, strategy.convertBoundarySequences(ordered)));
        } catch (AirspaceGeometry.UnsupportedGeometry exception) {
          if (!omitUnsupported) {
            throw exception;
          }
          LOG.warn("Omitting DAFIF boundary {}: {}", parent.boundaryIdentification(), exception.getMessage());
          return Stream.empty();
        }
      });
    }
  }
}
