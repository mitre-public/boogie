package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InputFilenameFilter implements Consumer<Path> {

  private static final Logger LOG = LoggerFactory.getLogger(InputFilenameFilter.class);

  private final Consumer<Path> delegate;
  private final Predicate<Path> allowed;

  private InputFilenameFilter(
      Consumer<Path> delegate,
      Predicate<Path> allowed
  ) {
    this.delegate = requireNonNull(delegate);
    this.allowed = requireNonNull(allowed);
  }

  static InputFilenameFilter allowMatchingPattern(Consumer<Path> delegate, @Nullable String patternString) {
    Predicate<String> nameMatcher = Optional.ofNullable(patternString)
        .map(Pattern::compile)
        .map(Pattern::asMatchPredicate)
        .orElse(x -> true);

    return new InputFilenameFilter(delegate, path -> nameMatcher.test(path.toFile().getName()));
  }

  @Override
  public void accept(Path path) {
    if (allowed.test(path)) {
      LOG.info("Accepting path matching input pattern: {}.", path);
      delegate.accept(path);
    } else {
      LOG.info("Ignoring file which didn't match input pattern: {}.", path);
    }
  }
}
