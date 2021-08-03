package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.LeftMerger;

import com.google.common.base.Strings;

/**
 * Section splitter for FAA IFR format route strings.
 * <br>
 * e.g. KATL.BOOVE4.DRSDN..AMF.J121.RPA..WYNDE.WYNDE8.KORD/0211
 */
public final class IfrFormatSectionSplitter implements Function<String, List<SectionSplit>> {

  public static final IfrFormatSectionSplitter INSTANCE = new IfrFormatSectionSplitter();

  private IfrFormatSectionSplitter() {
  }

  /**
   * Also performs cleaning to strip attached wildcard characters ("*", "+", etc.) and sequences "/0219" from the ID.
   */
  @Override
  public List<SectionSplit> apply(String route) {
    String[] splits = route.split("\\.");

    return IntStream.range(0, splits.length)
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

          return new SectionSplit.Builder()
              .setValue(clean)
              .setEtaEet(etaEet)
              .setIndex(i)
              .setWildcards(wildcards)
              .build();
        })
        .collect(splitMerger.asCollector());
  }

  /**
   * Cleans the '.' delimited splits pushing any wildcards from referenced empty splits into the following split.
   */
  private static final LeftMerger<SectionSplit> splitMerger = new LeftMerger<>(
      (l1, l2) -> Strings.isNullOrEmpty(l1.value()),
      (l1, l2) -> l2.setWildcards(l2.wildcards().concat(l1.wildcards()))
  );

  static Pattern etaEet() {
    return Pattern.compile("/[0-9]{4}$");
  }

  static String etaEet(String val) {
    Matcher matcher = etaEet().matcher(val);
    return matcher.find() ? matcher.group(0).replace("/", "") : null;
  }
}
