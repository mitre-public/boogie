package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;
import static org.mitre.tdp.boogie.util.ConditionalLogging.logIf;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * Functional class for returning the collection of reciprocal runway pairs from an input collection of {@link ArincRunway}
 * records (assumed to all be for the same airport).
 * <br>
 * Note that some runways in the returned pairs may have a null .second() indicating the pairer was unable to identify a valid
 * reciprocal runway for the runway in .first().
 * <br>
 * Also note this class returns pairs in only one direction when the runways have been deemed to be reciprocals. I.e. if A/B are
 * reciprocals and C/D are not this class returns:
 * <br>
 * List{A, B, C, D} --> Pair{A, B}, Pair{C, null}, Pair{D, null}
 */
public final class ReciprocalRunwayPairer implements Function<Collection<ArincRunway>, Collection<Pair<ArincRunway, ArincRunway>>> {

  private static final Logger LOG = LoggerFactory.getLogger(ReciprocalRunwayPairer.class);

  public static final ReciprocalRunwayPairer INSTANCE = new ReciprocalRunwayPairer();

  private final BiPredicate<ArincRunway, ArincRunway> isReciprocalPair;

  private ReciprocalRunwayPairer() {
    this(ReciprocalRunwayPairer::matchesReciprocalIdentifier);
  }

  public ReciprocalRunwayPairer(BiPredicate<ArincRunway, ArincRunway> isReciprocalPair) {
    this.isReciprocalPair = requireNonNull(isReciprocalPair);
  }

  @Override
  public Collection<Pair<ArincRunway, ArincRunway>> apply(Collection<ArincRunway> arincRunways) {
    checkArgument(allMatch(arincRunways, ArincRunway::airportIdentifier) || arincRunways.isEmpty(), "All runways should be from the same airport.");

    LinkedHashSet<Pair<ArincRunway, ArincRunway>> reciprocalPairs = new LinkedHashSet<>();

    Combinatorics.pairwiseCombos(arincRunways).forEachRemaining(pair -> {
      if (isReciprocalPair.test(pair.first(), pair.second())) {
        reciprocalPairs.add(pair);
      }
    });

    checkNoSharedReciprocals(reciprocalPairs);

    Set<ArincRunway> allUnpaired = allUnpairedRunways(reciprocalPairs, arincRunways);

    String runwayIds = allUnpaired.stream().map(ArincRunway::runwayIdentifier).collect(Collectors.joining(","));
    logIf(!allUnpaired.isEmpty(), LOG::debug, "Unable to find reciprocals for runways {}.", runwayIds);

    return concat(reciprocalPairs.stream(), allUnpaired.stream().map(runway -> Pair.of(runway, (ArincRunway) null))).collect(toList());
  }

  Set<ArincRunway> allUnpairedRunways(Set<Pair<ArincRunway, ArincRunway>> reciprocalPairs, Collection<ArincRunway> allRunways) {
    Set<ArincRunway> reciprocalRunways = reciprocalPairs.stream().flatMap(pair -> Stream.of(pair.first(), pair.second())).collect(toSet());
    return Sets.difference(new LinkedHashSet<>(allRunways), reciprocalRunways);
  }

  /**
   * Checks there are no instances where (RwyA, RwyB) and (RwyB, RwyC) are paired as reciprocals - any runway can have at most one
   * reciprocal counterpart.
   */
  void checkNoSharedReciprocals(Set<Pair<ArincRunway, ArincRunway>> reciprocalPairs) {
    Set<ArincRunway> allRunways = reciprocalPairs.stream().flatMap(pair -> Stream.of(pair.first(), pair.second())).collect(toSet());
    checkArgument(allRunways.size() == 2 * reciprocalPairs.size(), "Non-Unique reciprocal runway pairs.");
  }

  /**
   * Returns true when the converted reciprocal runway identifier from the {@link ReciprocalRunwayIdentifier} is present and
   * matches the identifier of the paired runway.
   */
  static boolean matchesReciprocalIdentifier(ArincRunway first, ArincRunway second) {
    Optional<String> firstReciprocal = ReciprocalRunwayIdentifier.INSTANCE.apply(first.runwayIdentifier());
    Optional<String> secondReciprocal = ReciprocalRunwayIdentifier.INSTANCE.apply(second.runwayIdentifier());

    return firstReciprocal.filter(recip -> recip.equals(second.runwayIdentifier())).isPresent()
        && secondReciprocal.filter(recip -> recip.equals(first.runwayIdentifier())).isPresent();
  }
}
