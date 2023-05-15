package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SidElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * May need to think about this, but essentially want to allow them to pick a SID/STAR client-side and inject it if we don't find
 * one when processing the route string.
 */
final class DefaultSidInferrer implements SectionInferrer {

  private final Procedure sid;

  DefaultSidInferrer(Procedure sid) {
    this.sid = requireNonNull(sid);
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportElement> airport = left.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    // check whether the left is an airport and whether the SID is for that airport
    boolean missingSid = airport.isPresent()
        && right.elements().stream().noneMatch(e -> e instanceof SidElement)
        && airport.get().identifier().equalsIgnoreCase(sid.airportIdentifier());

    SectionSplit split = SectionSplit.builder()
        .setValue(sid.procedureIdentifier())
        .setIndex((left.sectionSplit().index() + right.sectionSplit().index()) / 2.)
        .build();

    return missingSid
        ? List.of(new ResolvedSection(split, List.of(new SidElement(sid))))
        : List.of();
  }
}
