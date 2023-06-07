package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.fn.TriFunction;

/**
 * Assembler class for converting collections of {@link ArincAirwayLeg} records into a client-defined output class of type
 * {@code A} representing an Airway.
 *
 * <p>This class can be used with the {@link FixAssemblyStrategy#standard()} + {@link AirwayAssemblyStrategy#standard()} to
 * generate lightweight Boogie-defined {@link Airway} implementations that can be used with other Boogie algorithms.
 */
public final class AirwayAssembler<A, F, L> implements Function<Collection<ArincAirwayLeg>, Stream<A>> {

  private final ArincAirwayLegConverter<A, L, F> inflator;

  private final BiPredicate<ArincAirwayLeg, ArincAirwayLeg> shouldSplitAirway;

  private final AirwayAssemblyStrategy<A, F, L> strategy;

  private AirwayAssembler(
      FixDatabase fixDatabase,
      FixAssemblyStrategy<F> fixStrategy,
      AirwayAssemblyStrategy<A, F, L> airwayStrategy) {
    this.inflator = new ArincAirwayLegConverter<>(fixDatabase, fixStrategy, airwayStrategy);
    this.strategy = requireNonNull(airwayStrategy);
    this.shouldSplitAirway = (previous, next) -> isSequenceNumberJump(previous, next)
        || isSequenceNumberReset(previous, next)
        || !previous.routeIdentifier().equals(next.routeIdentifier());
  }

  /**
   * Create a new airway assembler with the given fix database and strategy for assembly as context. The assembler uses the database
   * to lookup relevant 424 records whose contents clients may wish to include in the generated model type.
   *
   * <p>Template types here notionally represent client-defined "Airway", "Fix", and "Leg" classes.
   *
   * @param database       containing indexed fixes
   * @param fixStrategy    strategy class for converting any resolved fix records referenced by airway legs into client-defined fix
   *                       implementations
   * @param airwayStrategy strategy class for converting 424 airway legs into client-defined leg types referencing the fixes created
   *                       by the fix strategy
   */
  public static <A, F, L> AirwayAssembler<A, F, L> create(FixDatabase database, FixAssemblyStrategy<F> fixStrategy, AirwayAssemblyStrategy<A, F, L> airwayStrategy) {
    return new AirwayAssembler<>(database, fixStrategy, airwayStrategy);
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
    return strategy.convertAirway(representative, legs);
  }

  private static final class ArincAirwayLegConverter<A, L, F> implements Function<ArincAirwayLeg, L> {

    private final LegFixDereferencer<F> legFixDereferencer;

    private final TriFunction<ArincAirwayLeg, F, F, L> legConverter;

    private ArincAirwayLegConverter(
        FixDatabase fixDatabase,
        FixAssemblyStrategy<F> fixStrategy,
        AirwayAssemblyStrategy<A, F, L> airwayStrategy
    ) {
      this.legFixDereferencer = new LegFixDereferencer<>(
          FixAssembler.create(fixStrategy),
          ArincDatabaseFactory.emptyTerminalAreaDatabase(),
          fixDatabase
      );
      this.legConverter = requireNonNull(airwayStrategy)::convertLeg;
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
