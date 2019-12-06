package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Responsible for splitting the input route string into a sequence of elements
 * matchable to infrastructure elements based on ID.
 *
 * Also performs cleaning to strip attached wildcard characters ("*", "+", etc.)
 * and sequences "/0219" from the ID.
 *
 * Returns a list of {@link SectionSplit}s for use downstream.
 */
public class SectionSplitter {

  static Pattern etaEet() {
    return Pattern.compile("/[0-9]{4}$");
  }

  static String etaEet(String val) {
    Matcher matcher = etaEet().matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }

  public static List<SectionSplit> splits(String route) {
    String[] splits = route.split("\\.");
    return IntStream.range(0, splits.length)
        .mapToObj(i -> {
          String s = splits[i];

          String etaEet = etaEet(s);
          String ns = null == etaEet ? s : s.replace("/" + etaEet, "");

          String clean = ns.replaceAll("[^A-Za-z0-9/]", "");
          String wildcards = ns.replaceAll("[A-Za-z0-9/]", "");

          // special case for tailored, we want this to be a wildcard
          // but we need this to work with Lat/Lon vals and preserve
          // the forward slash
          if (clean.equals("/")){
            clean = "";
            wildcards += "/";
          }

          return new SectionSplit(clean, etaEet, i, wildcards);
        })
        .collect(Collectors.toList());
  }
}
