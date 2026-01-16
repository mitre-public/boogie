package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLegAssembler;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.util.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

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

    NavigableMap<EnvelopeSection, Collection<Route>> keyed = new TreeMap<>();

    keyed.put(EnvelopeSection.SID, departure);
    LOG.info("Departure candidate route count {}", keyed.get(EnvelopeSection.SID).size());

    keyed.put(EnvelopeSection.ENROUTE, enroute);
    LOG.info("Enroute candidate route count {}", keyed.get(EnvelopeSection.ENROUTE).size());

    keyed.put(EnvelopeSection.STAR, arrival);
    LOG.info("Arrival candidate route count {}", keyed.get(EnvelopeSection.STAR).size());

    keyed.put(EnvelopeSection.APPROACH, approach);
    LOG.info("Approach candidate route count {}", keyed.get(EnvelopeSection.APPROACH).size());

    keyed = Maps.filterEntries(keyed, entry -> !entry.getValue().isEmpty());
    List<Collection<Route>> routes = new ArrayList<>(keyed.values());

    Set<Pair<FlyableLeg, FlyableLeg>> links = new HashSet<>();

    LegsLinker linker = new NearestFxLinker()
        .andThenApply(new UniqueNearestNeighborLinker());

    // this covers the traversal order of flight
    if (keyed.size() > 1) {
      Iterators.pairwise(routes, (prev, next) -> {
        Collection<Pair<FlyableLeg, FlyableLeg>> additional = linker.apply(
            FlyableLegAssembler.assembleWithLinks(prev).first(),
            FlyableLegAssembler.assembleWithLinks(next).first()
        );
        links.addAll(additional);
      });

      // This links direct route legs to the destination airport with approaches, which
      // could otherwise be unlinked if STARs are present
      if (!enroute.isEmpty() && !approach.isEmpty()) {
        Collection<FlyableLeg> legs = FlyableLegAssembler.assembleWithLinks(enroute).first();
        Collection<FlyableLeg> c =FlyableLegAssembler.assembleWithLinks(enroute).first().stream().reduce((f,s) -> s).stream().toList();

        links.addAll(linker.apply(
            FlyableLegAssembler.assembleWithLinks(enroute).first().stream().reduce((f,s) -> s).stream().toList(), //todo i guess we need a extraction function
            FlyableLegAssembler.assembleWithLinks(approach).first()
        ));
      }
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