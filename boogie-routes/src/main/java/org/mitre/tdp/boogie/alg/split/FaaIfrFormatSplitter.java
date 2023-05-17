package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.LeftMerger;

import com.google.common.base.Strings;

final class FaaIfrFormatSplitter implements RouteTokenizer {

  private static final Pattern ETAEET = Pattern.compile("/[0-9]{4}$");

  FaaIfrFormatSplitter() {
  }

  static String etaEet(String val) {
    Matcher matcher = ETAEET.matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }

  @Override
  public List<RouteToken> tokenize(String route) {

    String[] splits = route.split("\\.");

    List<RouteToken.FaaIfr> tokens = IntStream.range(0, splits.length)
        .mapToObj(i -> {
          String s = splits[i];

          String etaEet = etaEet(s);
          String ns = null == etaEet ? s : s.replace("/" + etaEet, "");

          String clean = ns.replaceAll("[^A-Za-z0-9/]", "");

          // if the cleaned section is empty - then that means the section indicates a direct - tag it in the wildcards
          String wildcards = ns.replaceAll("[A-Za-z0-9/]", "").concat(clean.isEmpty() ? " " : "");

          // special case for tailored, we want this to be a wildcard but we need this to work with Lat/Lon vals and
          // preserve the forward slash
          if ("/".equals(clean)) {
            clean = "";
            wildcards += "/";
          }

          return RouteToken.faaIfrBuilder(clean, i)
              .etaEet(etaEet)
              .wildcards(wildcards)
              .build();
        })
        .collect(TOKEN_MERGER.asCollector());

    return (List<RouteToken>) (List) tokens;
  }

  private static final LeftMerger<RouteToken.FaaIfr> TOKEN_MERGER = new LeftMerger<>(
      (l1, l2) -> Strings.isNullOrEmpty(l1.infrastructureName()),
      (l1, l2) -> l2.toBuilder().wildcards(l2.wildcards().orElse("").concat(l1.wildcards().orElse(""))).build()
  );
}
