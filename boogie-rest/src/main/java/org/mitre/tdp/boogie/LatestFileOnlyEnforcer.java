package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.fileutil.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple class which ensures the {@link #directory}s contents only reflect the most recently processed file.
 * <p>
 * It is assumed that this file is representative of the current chunk of data we wish to maintain and should be sufficient for
 * service recovery in the case of a restart.
 */
class LatestFileOnlyEnforcer implements Consumer<Path> {

  private static final Logger LOG = LoggerFactory.getLogger(LatestFileOnlyEnforcer.class);

  private final Path directory;
  private final Consumer<Path> delegate;

  private LatestFileOnlyEnforcer(
      Path directory,
      Consumer<Path> delegate
  ) {
    this.directory = directory;
    this.delegate = requireNonNull(delegate);
  }

  static LatestFileOnlyEnforcer managing(Path directory, Consumer<Path> delegate) {
    return new LatestFileOnlyEnforcer(directory, delegate);
  }

  @Override
  public void accept(Path path) {
    requireNonNull(path);

    delegate.accept(path);
    deleteContentsOtherThan(path);
  }

  private void deleteContentsOtherThan(Path path) {
    if (path.toFile().isFile()) {
      for (Path deletionTarget : deletionTargets(path)) {
        LOG.info("Deleting target file {}.", deletionTarget);
        checkDelete(deletionTarget);
      }
    }
  }

  private void checkDelete(Path deletionTarget) {
    try {
      FileUtils.forceDelete(deletionTarget.toFile());
    } catch (IOException e) {
      LOG.error("Error attempting to delete file {}", deletionTarget);
    }
  }

  private Set<Path> deletionTargets(Path path) {
    try {
      return Files.list(directory).filter(f -> !path.equals(f)).collect(Collectors.toSet());
    } catch (IOException e) {
      LOG.error("Error attempting to list the contents of the directory {} != {}", directory, path);
      return Collections.emptySet();
    }
  }
}
