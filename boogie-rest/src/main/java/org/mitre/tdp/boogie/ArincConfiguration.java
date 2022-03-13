package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import org.mitre.caasd.commons.fileutil.FileUtils;
import org.mitre.kraken.tentacular.core.InputFilenameFilter;
import org.mitre.kraken.tentacular.core.LatestFileOnlyEnforcer;
import org.mitre.kraken.tentacular.core.SimpleDirectoryWatcher;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
    FileUtils.makeDirIfMissing(arincDirectory().toFile());

    Consumer<Path> pathConsumer = environment.getProperty("cleanup.watchDirectory", Boolean.class, false)
        ? LatestFileOnlyEnforcer.managing(arincDirectory(), boogieCache())
        : boogieCache();

    Consumer<Path> target = InputFilenameFilter.allowMatchingPattern(
        pathConsumer,
        environment.getProperty("arinc.filePattern")
    );

    return SimpleDirectoryWatcher.forwardOnCreate(arincDirectory(), target, Duration.ofDays(1));
  }

  @Bean
  public BoogieState boogieCache() {

    ArincVersion version = ofNullable(environment.getProperty("arinc.version"))
        .flatMap(this::tryParseVersion)
        .orElse(ArincVersion.V19);

    LOG.info("Instantiating Boogie record cache with input version {}.", version);
    return BoogieState.forVersion(version);
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

  private Path arincDirectory() {
    return Paths.get(environment.getRequiredProperty("arinc.watchDirectory"));
  }
}
