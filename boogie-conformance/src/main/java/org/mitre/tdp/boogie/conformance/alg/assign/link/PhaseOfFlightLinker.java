package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLegAssembler;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.mitre.tdp.boogie.util.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PhaseOfFlightLinker implements LinkingStrategy, Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(PhaseOfFlightLinker.class);

  private final LinkingStrategy linkingStrategy;

  private PhaseOfFlightLinker(LinkingStrategy linkingStrategy) {
    this.linkingStrategy = linkingStrategy;
  }

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> collection) {
    return linkingStrategy.links(collection);
  }

  public static PhaseOfFlightLinker newStrategyFor(
      Collection<Route> departure,
      Collection<Route> enroute,
      Collection<Route> arrival,
      Collection<Route> approach
  ) {
    return newStrategyFor(departure, enroute, arrival, approach,
        r -> r.source() instanceof Procedure p ? Optional.of(p.airportIdentifier()) : Optional.empty());
  }

  public static PhaseOfFlightLinker newStrategyFor(
      Collection<Route> departure,
      Collection<Route> enroute,
      Collection<Route> arrival,
      Collection<Route> approach,
      Function<Route, Optional<String>> approachAirportIdentifier
  ) {

    NavigableMap<EnvelopeSection, Collection<Route>> keyed = new TreeMap<>();

    keyed.put(EnvelopeSection.SID, departure);
    LOG.info("Departure candidate route count {}", keyed.get(EnvelopeSection.SID).size());

    keyed.put(EnvelopeSection.ENROUTE, enroute);
    LOG.info("Enroute candidate route count {}", keyed.get(EnvelopeSection.ENROUTE).size());

    keyed.put(EnvelopeSection.STAR, arrival);
    LOG.info("Arrival candidate route count {}", keyed.get(EnvelopeSection.STAR).size());

    keyed.put(EnvelopeSection.APPROACH, approach);
    LOG.info("Approach candidate route count {}", keyed.get(EnvelopeSection.APPROACH).size());

    keyed.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    List<Collection<Route>> routes = new ArrayList<>(keyed.values());

    Set<Pair<FlyableLeg, FlyableLeg>> links = new HashSet<>();

    LegsLinker linker = new NearestFxLinker()
        .andThenApply(new FmSkipLinker())
        .andThenApply(new UniqueNearestNeighborLinker());

    // this covers the traversal order of flight
    if (keyed.size() > 1) {
      Iterators.pairwise(routes, (prev, next) -> {
        Collection<Pair<FlyableLeg, FlyableLeg>> additional = linker.apply(
            prev.stream().flatMap(r -> FlyableLegAssembler.assemble(r).stream()).toList(),
            next.stream().flatMap(r -> FlyableLegAssembler.assemble(r).stream()).toList()
        );
        links.addAll(additional);
      });

      // Links the last leg of each enroute section directly to IAF/IF approach legs, but only
      // when the enroute leg's fix identifier matches the approach procedure's airport identifier.
      // This covers the direct-to-airport case and when a STAR is present and bypassed.
      Map<String, List<FlyableLeg>> iafsByAirport = approach.stream()
          .filter(r -> approachAirportIdentifier.apply(r).isPresent())
          .collect(Collectors.groupingBy(
              r -> approachAirportIdentifier.apply(r).orElseThrow(),
              Collectors.flatMapping(
                  r -> FlyableLegAssembler.assemble(r).stream().filter(fl -> fl.current().isIntermediateOrInitialApproachFix()),
                  Collectors.toList()
              )
          ));
      enroute.stream()
          .map(FlyableLegAssembler::assemble)
          .filter(assembled -> !assembled.isEmpty())
          .map(assembled -> assembled.get(assembled.size() - 1))
          .forEach(lastLeg -> lastLeg.current().associatedFix()
              .map(Fix::fixIdentifier)
              .map(iafsByAirport::get)
              .ifPresent(iafLegs -> links.addAll(Combinatorics.cartesianProduct(List.of(lastLeg), iafLegs)))
          );
    } else {
      LOG.warn("Insufficient routes from envelope portions to apply strategy, {}", keyed.size());
    }

    LOG.info("Generating new supplied link strategy with {} total links.", links.size());
    SuppliedLinkStrategy strategy = new SuppliedLinkStrategy(links);

    return new PhaseOfFlightLinker(strategy);
  }

  enum EnvelopeSection {
    SID,
    ENROUTE,
    STAR,
    APPROACH
  }
}