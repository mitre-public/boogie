package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.fn.TriFunction;

/**
 * Functional class for converting a collection of {@link ArincAirwayLeg}s into grouped and sequenced {@link Airway} records for
 * use in conjunction with downstream packages such as boogie-routes.
 * <br>
 * This class is similar in to {@link ProcedureAssembler} in that it groups legs of the airway together by the airways identifier
 * and then splits out legs into different top-level airway records based on the
 */
public final class AirwayAssembler<A,F,L> implements Function<Collection<ArincAirwayLeg>, Stream<A>> {

  /**
   * Functional class for transforming a {@link ArincAirwayLeg} and a pair of matched fixes as AssociatedFix and RecommendedNavaid
   * for the leg with the
   */
  private final ArincAirwayLegConverter<L,F> inflator;

  private final BiPredicate<ArincAirwayLeg, ArincAirwayLeg> shouldSplitAirway;

  private final BiFunction<ArincAirwayLeg, List<L>, A> airwayConverter;

  public AirwayAssembler(
      FixDatabase fixDatabase,
      Function<ArincModel, F> fixConverter,
      TriFunction<ArincAirwayLeg, F, F, L> legConverter,
      BiFunction<ArincAirwayLeg, List<L>, A> airwayConverter) {
    this.airwayConverter = airwayConverter;
    this.inflator = new ArincAirwayLegConverter<>(fixDatabase, fixConverter, legConverter);
    this.shouldSplitAirway = (previous, next) -> isSequenceNumberJump(previous, next)
        || isSequenceNumberReset(previous, next)
        || !previous.routeIdentifier().equals(next.routeIdentifier());
  }

  @Override
  public Stream<A> apply(Collection<ArincAirwayLeg> arincAirwayLegs) {
    return arincAirwayLegs.stream()
        .sorted(Comparator.comparing(ArincAirwayLeg::fileRecordNumber))
        .collect(Partitioners.newListCollector((list, next) -> shouldSplitAirway.negate().test(list.get(list.size() - 1), next)))
        .stream()
        .map(this::toAirway);
  }

  boolean isSequenceNumberReset(ArincAirwayLeg previous, ArincAirwayLeg next) {
    return next.sequenceNumber() <= previous.sequenceNumber();
  }

  /**
   * Splits subsequent singleton airway records when their initial sequence number jumps by at least 1.
   * <br>
   * i.e. most sequence number are 0010, 0020, 1020, 3050 - we want to split on 0010 -> 1020.
   * <br>
   * Context in {@link SequenceNumber}.
   */
  boolean isSequenceNumberJump(ArincAirwayLeg previous, ArincAirwayLeg next) {
    return !formattedSequenceNumber(previous).startsWith(formattedSequenceNumber(next).substring(0, 1));
  }

  String formattedSequenceNumber(ArincAirwayLeg arincAirwayLeg) {
    return String.format("%04d", arincAirwayLeg.sequenceNumber());
  }

  private A toAirway(List<ArincAirwayLeg> arincAirwayLegs) {
    ArincAirwayLeg representative = arincAirwayLegs.get(0);

    List<L> legs = arincAirwayLegs.stream().map(inflator).collect(Collectors.toList());
    return airwayConverter.apply(representative, legs);
  }

  /**
   * Converter logic for transforming an {@link ArincAirwayLeg} to a {@link Leg} implementation - handling the complex parts of
   * looking up the associated fix and recommended navaid for the leg.
   */
  public static final class ArincAirwayLegConverter<L,F> implements Function<ArincAirwayLeg, L> {

    private final LegFixDereferencer<F> legFixDereferencer;

    private final TriFunction<ArincAirwayLeg, F, F, L> legConverter;

    public ArincAirwayLegConverter(
        FixDatabase fixDatabase,
        Function<ArincModel, F> fixConverter,
        TriFunction<ArincAirwayLeg, F, F, L> legConverter) {
      this.legFixDereferencer = new LegFixDereferencer<>(fixConverter, ArincDatabaseFactory.emptyTerminalAreaDatabase(), fixDatabase);
      this.legConverter = requireNonNull(legConverter);
    }

    @Override
    public L apply(ArincAirwayLeg arincAirwayLeg) {
      return legConverter.apply(arincAirwayLeg, associatedFix(arincAirwayLeg).orElse(null), recommendedNavaid(arincAirwayLeg).orElse(null));
    }

    Optional<F> associatedFix(ArincAirwayLeg arincAirwayLeg) {
      return legFixDereferencer.dereference(
          arincAirwayLeg.fixIdentifier(),
          null,
          arincAirwayLeg.fixIcaoRegion(),
          arincAirwayLeg.fixSectionCode(),
          arincAirwayLeg.fixSubSectionCode().orElse(null)
      );
    }

    Optional<F> recommendedNavaid(ArincAirwayLeg arincAirwayLeg) {
      if (arincAirwayLeg.recommendedNavaidIdentifier().isPresent() && arincAirwayLeg.recommendedNavaidIcaoRegion().isPresent()) {
        return legFixDereferencer.dereferenceNavaid(
            arincAirwayLeg.recommendedNavaidIdentifier().orElseThrow(IllegalStateException::new),
            arincAirwayLeg.recommendedNavaidIcaoRegion().orElseThrow(IllegalStateException::new)
        );
      }
      return Optional.empty();
    }
  }
}
