package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.LeftMerger;

import com.google.common.base.Strings;

/**
 * Section splitter for International IFR format route strings
 * <p>
 * e.g.,"DCT GREKI IFR DCT BGO/N0485F410 M626 VKB M751 VPK B469 BIKTA PIBAP PAS3 MABA2A"
 */
public final class InternationalIfrFormatSectionSplitter implements Function<String, List<SectionSplit>> {

  public static final InternationalIfrFormatSectionSplitter INSTANCE = new InternationalIfrFormatSectionSplitter();

  /**
   * Takes the ifr vfr object and then adds it to the previous item where the rules changed at
   */
  private static final LeftMerger<SectionSplit> ifrVfrMerger = new LeftMerger<>(
      (l1, l2) -> nonNull(l2.flightRules()),
      (l1, l2) -> l1.toBuilder().setFlightRules(l2.flightRules()).build()
  );
  /**
   * Cleans the ' ' delimited splits pushing any wildcards from referenced empty splits into the following split.
   */
  private static final LeftMerger<SectionSplit> splitMerger = new LeftMerger<>(
      (l1, l2) -> Strings.isNullOrEmpty(l1.value()),
      (l1, l2) -> l2.setWildcards(l2.wildcards().concat(l1.wildcards()))
  );
  static Pattern speedLevel = Pattern.compile("/[A-Z0-9]{8,9}$");

  private InternationalIfrFormatSectionSplitter() {
  }

  static String findSpeedLevel(String val) {
    Matcher matcher = speedLevel.matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }

  static String flightRuleChange(String val) {
    return "VFR".equals(val) || "IFR".equals(val) ? val : null;
  }

  @Override
  public List<SectionSplit> apply(String route) {
    String[] splits = route.split(" ");
    return IntStream.range(0, splits.length)
        .mapToObj(i -> {
          String s = splits[i];

          if ("DCT".equals(s)) {
            return null;
          }

          String etaEet = IfrFormatSectionSplitter.etaEet(s);

          String sl = findSpeedLevel(s);

          String frc = flightRuleChange(s);

          String ns = isNull(etaEet) ? s : s.replace("/" + etaEet, "");
          ns = isNull(sl) ? ns : ns.replace("/" + sl, "");
          ns = isNull(frc) ? ns : "";

          String clean = ns.replaceAll("[^A-Za-z0-9/]", "");

          // if the cleaned section is empty - then that means the section indicates a direct - tag it in the wildcards
          String wildcards = ns.replaceAll("[A-Za-z0-9/]", "").concat(clean.isEmpty() ? " " : "");

          // special case for tailored, we want this to be a wildcard but we need this to work with Lat/Lon vals and
          // preserve the forward slash
          if ("/".equals(clean)) {
            clean = "";
            wildcards += "/";
          }

          return SectionSplit.builder()
              .setValue(clean)
              .setEtaEet(etaEet)
              .setSpeedLevel(sl)
              .setFlightRules(frc)
              .setIndex(i)
              .setWildcards(wildcards)
              .build();
        })
        .filter(Objects::nonNull)
        .collect(ifrVfrMerger.asCollector())
        .stream()
        .collect(splitMerger.asCollector());
  }
}
