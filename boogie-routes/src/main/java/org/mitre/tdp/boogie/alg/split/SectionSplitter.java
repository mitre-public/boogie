package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.LookupService;

/**
 * Responsible for splitting an input route string into a sequence of elements matchable to infrastructure elements based on ID
 * e.g. {@code KATL.CHPPR1.DRSDN.J121.JMACK..HOBTT.GNDLF1.KORD} -> {@code [KATL, CHPPR1, J121, DRSDN, etc.]}.
 */
@FunctionalInterface
public interface SectionSplitter {

  /**
   * Section splitter for International ICAO format route strings.
   *
   * <p>e.g. {@code DCT GREKI IFR DCT BGO/N0485F410 M626 VKB M751 VPK B469 BIKTA PIBAP PAS3 MABA2A}
   */
  static SectionSplitter icaoFormat() {
    return new IcaoFormatSplitter();
  }

  /**
   * Section splitter for FAA IFR format route strings.
   *
   * <p>e.g. {@code KATL.BOOVE4.DRSDN..AMF.J121.RPA..WYNDE.WYNDE8.KORD/0211}
   */
  static SectionSplitter faaIfrFormat() {
    return new FaaIfrFormatSplitter();
  }

  /**
   * Split an input route of some (potentially undetermined format) and convert it to token identifiers which can be matched against
   * the contents of various {@link LookupService} implementations.
   *
   * @param route the stringified route-of-flight for an aircraft
   */
  List<SectionSplit> splits(String route);

  default <O> Function<String, O> andThen(Function<List<SectionSplit>, O> next) {
    requireNonNull(next);
    return s -> next.apply(splits(s));
  }
}
