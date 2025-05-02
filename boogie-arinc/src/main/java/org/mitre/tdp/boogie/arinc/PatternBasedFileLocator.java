package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;

import org.mitre.tdp.boogie.AiracCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A straightforward pattern-based file locator knows how to map string AIRAC cycles to file locations on a visible filesystem.
 * <br>
 * This class respects two distinct template patterns for mapping input AIRAC cycles to paths:
 * <br>
 * 1. {yy}{cc} - essentially direct substitution of the cycle string
 * 2. {yyyy}{mm}{dd} - substitution based on the start of the AIRAC cycle
 */
public final class PatternBasedFileLocator implements Function<String, File> {

  private static final Logger LOG = LoggerFactory.getLogger(PatternBasedFileLocator.class);

  private final String filePathPattern;

  public PatternBasedFileLocator(String filePathPattern) {
    this.filePathPattern = requireNonNull(filePathPattern);
  }

  @Override
  public File apply(String cycle) {
    Path path = filePathPattern.contains("{cc}") ? cycleReplacedPath(cycle) : dateReplacedPath(cycle);

    LOG.info("Resolved path {} for cycle {} and pattern {}.", path, cycle, filePathPattern);
    return path.toFile();
  }

  private Path cycleReplacedPath(String cycle) {
    String replacedPath = filePathPattern
        .replaceAll("\\{yy}", cycle.substring(0, 2))
        .replaceAll("\\{cc}", cycle.substring(2, 4));
    return Paths.get(replacedPath);
  }

  private Path dateReplacedPath(String cycle) {
    Instant startDate = AiracCycle.startDate(cycle);
    LocalDateTime localDate = startDate.atOffset(ZoneOffset.UTC).toLocalDateTime();

    String replacedPath = filePathPattern
        .replaceAll("\\{yyyy}", Integer.toString(localDate.getYear()))
        .replaceAll("\\{mm}", String.format("%02d", localDate.getMonthValue()))
        .replaceAll("\\{dd}", String.format("%02d", localDate.getDayOfMonth()));

    return Paths.get(replacedPath);
  }
}
