package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

/**
 * Represents a page of structured ARINC 424 data related to an {@link ArincHeliport}.
 */
final class HeliportPage {

  private final ArincHeliport heliport;
  private final Map<String, RunwayPage> runwayPages;
  private final Map<String, HelipadPage> helipadPages;
  private final SupportingPage supportingPage;

  HeliportPage(
      ArincHeliport heliport,
      Map<String, RunwayPage> runwayPages,
      Map<String, HelipadPage> helipadPages,
      SupportingPage supportingPage
  ) {
    this.heliport = requireNonNull(heliport);
    this.runwayPages = runwayPages;
    this.helipadPages = helipadPages;
    this.supportingPage = supportingPage;
  }

  public ArincHeliport heliport() {
    return heliport;
  }

  public Collection<ArincRunway> runways() {
    return runwayPages.isEmpty()
        ? Collections.emptySet()
        : runwayPages.values().stream().map(RunwayPage::runway).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincRunway> runway(String identifier) {
    return Optional.ofNullable(runwayPages.get(identifier)).map(RunwayPage::runway);
  }

  public Collection<ArincHelipad> helipads() {
    return helipadPages.values().stream().map(HelipadPage::helipad).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincHelipad> helipad(String identifier) {
    return Optional.ofNullable(helipadPages.get(identifier)).map(HelipadPage::helipad);
  }

  public Optional<ArincLocalizerGlideSlope> localizerGlideSlope(String identifier) {
    return Optional.ofNullable(runwayLocalizerGlideSlopes().get(identifier))
        .or(() -> Optional.ofNullable(helipadLocalizerGlideSlopes().get(identifier)));
  }

  public Map<String, ArincLocalizerGlideSlope> runwayLocalizerGlideSlopes() {
    return runwayPages.values().stream()
        .flatMap(page -> Stream.of(page.primaryLocalizerGlideSlope(), page.secondaryLocalizerGlideSlope()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(ArincLocalizerGlideSlope::localizerIdentifier, Function.identity(), (f,s) -> f));
  }

  public Map<String, ArincLocalizerGlideSlope> helipadLocalizerGlideSlopes() {
    return helipadPages.values().stream()
        .flatMap(page -> Stream.of(page.localizerGlideSlope(), page.secondaryLocalizerGlideSlope()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(ArincLocalizerGlideSlope::localizerIdentifier, Function.identity(), (f,s) -> f));
  }

  public Optional<ArincGnssLandingSystem> gnssLandingSystem(String identifier) {
    return Optional.ofNullable(runwayGnssLandingSystems().get(identifier))
        .or(() -> Optional.ofNullable(helipadGnssLandingSystems().get(identifier)));
  }

  public Map<String, ArincGnssLandingSystem> runwayGnssLandingSystems() {
    return runwayPages.values().stream()
        .map(RunwayPage::gnssLandingSystem)
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(ArincGnssLandingSystem::glsRefPathIdentifier, Function.identity(), (f,s) -> f));
  }

  public Map<String, ArincGnssLandingSystem> helipadGnssLandingSystems() {
    return helipadPages.values().stream()
        .map(HelipadPage::gnssLandingSystem)
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(ArincGnssLandingSystem::glsRefPathIdentifier, Function.identity(), (f,s) -> f));
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlopeForHelipad(String id) {
    return Optional.ofNullable(helipadPages.get(id)).flatMap(HelipadPage::localizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlopeForHelipad(String id) {
    return Optional.ofNullable(helipadPages.get(id)).flatMap(HelipadPage::secondaryLocalizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlopeForRunway(String runwayId) {
    return Optional.ofNullable(runwayPages.get(runwayId)).flatMap(RunwayPage::primaryLocalizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlopeForRunway(String runwayId) {
    return Optional.ofNullable(runwayPages.get(runwayId)).flatMap(RunwayPage::secondaryLocalizerGlideSlope);
  }

  public Collection<String> procedureNames() {
    return supportingPage.procedureNames();
  }

  public Collection<ArincProcedureLeg> procedureLegs() {
    return supportingPage.procedureLegs();
  }

  public Collection<ArincProcedureLeg> procedureLegs(String procedure) {
    return supportingPage.procedureLegs(procedure);
  }

  public Optional<ArincWaypoint> waypoint(String waypoint) {
    return supportingPage.waypoint(waypoint);
  }

  public Collection<ArincWaypoint> waypoints() {
    return supportingPage.waypoints();
  }

  public Optional<ArincNdbNavaid> ndbNavaid(String identifier) {
    return supportingPage.ndbNavaid(identifier);
  }

  public Collection<ArincNdbNavaid> ndbNavaids() {
    return supportingPage.ndbNavaids();
  }

  public Optional<ArincVhfNavaid> vhfNavaid(String identifier) {
    return supportingPage.vhfNavaid(identifier);
  }

  public Collection<ArincVhfNavaid> vhfNavaids() {
    return supportingPage.vhfNavaids();
  }
}
