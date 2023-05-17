package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.LeftMerger;

import com.google.common.base.Strings;

final class IcaoFormatSplitter implements RouteTokenizer {

  /**
   * Takes the ifr vfr object and then adds it to the previous item where the rules changed at
   */
  private static final LeftMerger<RouteToken.Icao> ifrVfrMerger = new LeftMerger<>(
      (l1, l2) -> l2.flightRules().isPresent(),
      (l1, l2) -> l1.toBuilder().flightRules(l2.flightRules().orElse(null)).build()
  );

  /**
   * Cleans the ' ' delimited splits pushing any wildcards from referenced empty splits into the following split.
   */
  private static final LeftMerger<RouteToken.Icao> splitMerger = new LeftMerger<>(
      (l1, l2) -> Strings.isNullOrEmpty(l1.infrastructureName()),
      (l1, l2) -> l2.toBuilder().wildcards(l2.wildcards().orElse("").concat(l1.wildcards().orElse(""))).build()
  );
  static Pattern speedLevel = Pattern.compile("/[A-Z0-9]{8,10}$");

  IcaoFormatSplitter() {
  }

  static String findSpeedLevel(String val) {
    Matcher matcher = speedLevel.matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }

  static String flightRuleChange(String val) {
    return "VFR".equals(val) || "IFR".equals(val) ? val : null;
  }

  @Override
  public List<RouteToken> tokenize(String route) {
    String[] splits = route.split(" ");

    List<RouteToken.Icao> tokens = IntStream.range(0, splits.length)
        .mapToObj(i -> {
          String s = splits[i];

          if ("DCT".equals(s)) {
            return null;
          }

          String etaEet = FaaIfrFormatSplitter.etaEet(s);

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

          return RouteToken.icaoBuilder(clean, i)
              .etaEet(etaEet)
              .wildcards(wildcards)
              .speedLevel(sl)
              .flightRules(frc)
              .build();
        })
        .filter(Objects::nonNull)
        .collect(ifrVfrMerger.asCollector())
        .stream()
        .collect(splitMerger.asCollector());

    return (List<RouteToken>) (List) tokens;
  }
}
