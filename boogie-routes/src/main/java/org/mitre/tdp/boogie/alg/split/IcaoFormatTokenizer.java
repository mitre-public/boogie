package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.LeftMerger;

import com.google.common.base.Strings;

final class IcaoFormatTokenizer implements RouteTokenizer {

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
  private static final Pattern speedLevel = Pattern.compile("/[KMN][0-9]{3,4}[FSAM][0-9]{3,4}$");

  //these show up as tokens vs items modifying the actual token
  private static final Pattern cruiseSpeedLevel = Pattern.compile("^-?[KMN][0-9]{3,4}[FSAM][0-9]{3,4}");

  private static final Pattern changeInCruiseWindow = Pattern.compile("/[KMN][0-9]{3,4}[FSAM][0-9]{3,4}[FSAM][0-9]{3,4}$");
  private static final Pattern changeInCruisePlus = Pattern.compile("/[KMN][0-9]{3,4}[FSAM][0-9]{3,4}PLUS$");


  IcaoFormatTokenizer() {
  }

  private static String findSpeedLevel(String val) {
    Matcher matcher = speedLevel.matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }

  private static String findChangeInCruiseSpeedLevel(String val) {
    Matcher window = changeInCruiseWindow.matcher(val);
    Matcher plus = changeInCruisePlus.matcher(val);
    return Optional.of(val)
        .filter(i -> window.find())
        .map(i -> window.group(0).replace("/", ""))
        .orElseGet(() -> Optional.of(val)
            .filter(i -> plus.find())
            .map(i -> plus.group(0).replace("/", ""))
            .orElse(null)
        );
  }

  private static String flightRuleChange(String val) {
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

          if(cruiseSpeedLevel.matcher(s).find()) {
            return null;
          }

          String etaEet = FaaIfrFormatTokenizer.etaEet(s);

          String sl = findSpeedLevel(s);
          sl = isNull(sl) ? findChangeInCruiseSpeedLevel(s) : sl; //check for windows

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

          //special case for change in cruise
          if (clean.startsWith("C/")) {
            clean = clean.replace("C/", "");
            wildcards += "C/";
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
