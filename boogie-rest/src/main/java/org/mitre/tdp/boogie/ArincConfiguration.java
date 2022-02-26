package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.avro.reflect.Nullable;
import org.mitre.caasd.commons.fileutil.FileUtils;
import org.mitre.kraken.tentacular.core.ScanDirectoryForFiles;
import org.mitre.kraken.tentacular.core.SimpleDirectoryWatcher;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@PropertySource("file:${arinc.config.path}/arinc.properties")
class ArincConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(ArincConfiguration.class);

  private final Environment environment;

  @Autowired
  ArincConfiguration(Environment environment) {
    this.environment = requireNonNull(environment);
  }

  @Scheduled(fixedDelay = 10_000L)
  public void scanForFiles() {
    LOG.debug("Scanning for new ARINC-424 files.");
    arincWatcher().run();
  }

  @Bean
  public SimpleDirectoryWatcher arincWatcher() {
    Path arincDirectory = Paths.get(environment.getRequiredProperty("arinc.watch.directory"));

    FileUtils.makeDirIfMissing(arincDirectory.toFile());

    Consumer<Path> target = InputFilenameFilter.allowMatchingPattern(
        boogieCache(),
        environment.getProperty("arinc.filename.pattern")
    );

    return SimpleDirectoryWatcher.forwardOnCreate(arincDirectory, target, Duration.ofDays(1));
  }

  @Bean
  public BoogieState boogieCache() {

    ArincVersion version = ofNullable(environment.getProperty("arinc.version"))
        .flatMap(this::tryParseVersion)
        .orElse(ArincVersion.V19);

    LOG.info("Instantiating Boogie record cache with input version {}.", version);
    return BoogieState.forVersion(version);
  }

  @EventListener
  public void onApplicationStart(ApplicationStartedEvent event) {
    boolean preScan = environment.getProperty("cache.preScan", Boolean.class, false);
    Path arincDirectory = Paths.get(environment.getRequiredProperty("arinc.watch.directory"));

    if (preScan) {
      ScanDirectoryForFiles.andHandoffTo(boogieCache()).accept(arincDirectory);
    }
  }

  Optional<ArincVersion> tryParseVersion(String versionString) {
    try {
      ArincVersion version = ArincVersion.valueOf(versionString);
      return Optional.of(version);
    } catch (IllegalArgumentException e) {
      LOG.error("Error matching specified version string '{}' to known 424 version - system will default to V19.", versionString);
      return Optional.empty();
    }
  }

  static final class InputFilenameFilter implements Consumer<Path> {

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
        delegate.accept(path);
      } else {
        LOG.info("Ignoring file which didn't match input pattern: {}.", path);
      }
    }
  }
}
