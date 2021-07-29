package org.mitre.tdp.boogie.validate;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniqueRecordElector} operates on a collection of input records to identify instances of duplicate record definitions
 * in the provided input data.
 * <br>
 * This class is <i>typically</i> used as a defensive pre-check in downstream metrics to exclude multiple definitions of the same
 * record which may have been provided either across sources or across different navigation cycles of information.
 */
final class UniqueRecordElector<A> implements Function<Collection<A>, Collection<A>>, Predicate<Collection<A>> {

  private static final Logger LOG = LoggerFactory.getLogger(UniqueRecordElector.class);

  /**
   * Function for generating an expected UUID from the input record type.
   */
  private final Function<A, String> uuidKeyer;
  /**
   * Function to use to choose a resultant airport when multiple are deemed identical in the input set.
   */
  private final Function<List<A>, A> recordElector;

  public UniqueRecordElector(Function<A, String> uuidKeyer) {
    this(uuidKeyer, UniqueRecordElector::exceptionThrowingElector);
  }

  public UniqueRecordElector(
      Function<A, String> uuidKeyer,
      Function<List<A>, A> recordElector
  ) {
    this.uuidKeyer = requireNonNull(uuidKeyer);
    this.recordElector = requireNonNull(recordElector);
  }

  @Override
  public Collection<A> apply(Collection<A> records) {
    Map<String, List<A>> grouped = records.stream().collect(Collectors.groupingBy(uuidKeyer));
    return grouped.values().stream().map(recordElector).collect(Collectors.toList());
  }

  @Override
  public boolean test(Collection<A> records) {
    return apply(records).size() == records.size();
  }

  /**
   * Internal elector used in the default configuration to log the offending records when duplicates are encountered and then
   * throw an exception.
   */
  private static <A> A exceptionThrowingElector(List<A> records) {
    if (records.size() == 1) {
      return records.get(0);
    } else {
      String recordString = records.stream().map(A::toString).collect(Collectors.joining("\n"));
      LOG.error("Encountered non-unique records: {}", recordString);
      throw new IllegalStateException("Throwing exception on non-unique records - see logs for details.");
    }
  }
}
