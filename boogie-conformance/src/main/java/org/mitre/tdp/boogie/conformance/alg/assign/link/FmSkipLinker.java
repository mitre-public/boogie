package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.Collection;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * A {@link LegsLinker} that adds skip edges over FM legs whose associated fix is shared with the next leg
 * <p>
 * Without this linker the Viterbi graph has no direct edge from the pre-FM leg to the post-FM leg, forcing
 * every path through the FM even when the FM emission score is zero.
 */
final class FmSkipLinker implements LegsLinker {

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> apply(Collection<FlyableLeg> left, Collection<FlyableLeg> right) {
    return left.stream()
        .filter(l -> l.next().isPresent())
        .filter(l -> l.next().get().pathTerminator().equals(PathTerminator.FM))
        .map(l -> skipLinkFor(l, right))
        .flatMap(Collection::stream)
        .toList();
  }

  private static Collection<Pair<FlyableLeg, FlyableLeg>> skipLinkFor(FlyableLeg flyableLeg, Collection<FlyableLeg> right) {
    Fix fmFix = flyableLeg.next().orElseThrow(IllegalStateException::new)
        .associatedFix().orElseThrow(IllegalStateException::new);
    return right.stream()
        .filter(l -> l.current().associatedFix().map(Fix::fixIdentifier).map(s -> s.equals(fmFix.fixIdentifier())).orElse(false))
        .filter(l -> l.current().associatedFix().map(f -> f.distanceInNmTo(fmFix)).filter(d -> d < .5).isPresent())
        .map(l -> Pair.of(flyableLeg, l))
        .toList();
  }






}
