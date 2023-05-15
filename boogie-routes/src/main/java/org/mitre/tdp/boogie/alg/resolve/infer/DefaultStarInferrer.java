package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.StarElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

final class DefaultStarInferrer implements SectionInferrer {

  private final Procedure star;

  DefaultStarInferrer(Procedure star) {
    this.star = requireNonNull(star);
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportElement> airport = right.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    boolean missingStar = airport.isPresent()
        && left.elements().stream().noneMatch(e -> e instanceof StarElement)
        && airport.get().identifier().equalsIgnoreCase(star.airportIdentifier());

    SectionSplit split = SectionSplit.builder()
        .setValue(star.procedureIdentifier())
        .setIndex((left.sectionSplit().index() + right.sectionSplit().index()) / 2.)
        .build();

    return missingStar
        ? List.of(new ResolvedSection(split, List.of(new StarElement(star))))
        : List.of();
  }
}
