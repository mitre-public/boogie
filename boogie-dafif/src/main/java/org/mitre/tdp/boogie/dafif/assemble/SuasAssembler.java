package org.mitre.tdp.boogie.dafif.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assembles special use airspaces, retaining each sector as a separate airspace.
 * When using the standard strategy, assembly logs and omits point, annular, open and disconnected airspaces that
 * {@link Airspace} cannot represent. Source coordinate gaps up to 0.1 NM are bridged with explicit
 * great-circle sequences, retaining both endpoints. Sequence numbers are regenerated in path order.
 * Custom strategies receive every parent and its source segments without these restrictions.
 */
public interface SuasAssembler<A> {

  static SuasAssembler<Airspace> standard() {
    return usingStrategy(SuasAssemblyStrategy.standard());
  }

  static <A, S> SuasAssembler<A> usingStrategy(SuasAssemblyStrategy<A, S> strategy) {
    return new Standard<>(strategy, strategy instanceof SuasAssemblyStrategy.Standard);
  }

  Stream<A> assemble(Collection<DafifSuasParent> parents, Collection<DafifSuasSegment> segments);

  final class Standard<A, S> implements SuasAssembler<A> {

    private static final Logger LOG = LoggerFactory.getLogger(Standard.class);

    private final SuasAssemblyStrategy<A, S> strategy;
    private final boolean omitUnsupported;

    private Standard(SuasAssemblyStrategy<A, S> strategy, boolean omitUnsupported) {
      this.strategy = requireNonNull(strategy);
      this.omitUnsupported = omitUnsupported;
    }

    @Override
    public Stream<A> assemble(Collection<DafifSuasParent> parents, Collection<DafifSuasSegment> segments) {
      Map<Key, List<DafifSuasSegment>> grouped = segments.stream()
          .collect(Collectors.groupingBy(segment -> new Key(segment.suasIdentification(), segment.sector().orElse(""))));
      return parents.stream().flatMap(parent -> {
        Key key = new Key(parent.suasIdentification(), parent.sector().orElse(""));
        List<DafifSuasSegment> ordered = grouped.getOrDefault(key, List.of()).stream()
            .sorted(Comparator.comparingInt(DafifSuasSegment::segmentNumber))
            .toList();
        try {
          return Stream.of(strategy.convertSuas(parent, strategy.convertSuasSequences(ordered)));
        } catch (AirspaceGeometry.UnsupportedGeometry exception) {
          if (!omitUnsupported) {
            throw exception;
          }
          LOG.warn("Omitting DAFIF special use airspace {}/{}: {}", parent.suasIdentification(),
              parent.sector().orElse(""), exception.getMessage());
          return Stream.empty();
        }
      });
    }

    private record Key(String identifier, String sector) {
    }
  }
}
