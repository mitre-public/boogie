package org.mitre.boogie.xml.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A container to add and build a collection of data models to.
 */
public interface ArincRecords {

  static ArincRecords standard() {
    return new Standard();
  }

  void addWaypoint(ArincWaypoint waypoint);
  void addAirport(ArincAirport airport);
  void addNdbNavaid(ArincNdbNavaid ndb);
  void addVhfNavaid(ArincVhfNavaid vhf);
  void addAirway(ArincAirway arincAirway);
  void addHoldingPattern(ArincHoldingPattern holdingPattern);

  Set<ArincWaypoint> waypoints();
  ArincRecords waypoints(Set<ArincWaypoint> waypoints);
  Set<ArincAirport> airports();
  ArincRecords airports(Set<ArincAirport> airports);
  Set<ArincNdbNavaid> ndbNavaids();
  ArincRecords ndbNavaids(Set<ArincNdbNavaid> ndbNavaids);
  Set<ArincVhfNavaid> vhfNavaids();
  ArincRecords vhfNavaids(Set<ArincVhfNavaid> vhfNavaids);
  Set<ArincAirway> arincAirways();
  ArincRecords arincAirways(Set<ArincAirway> arincAirways);
  Set<ArincHoldingPattern> holdingPatterns();
  ArincRecords holdingPatterns(Set<ArincHoldingPattern> holdingPatterns);


  final class Standard implements ArincRecords {
    Set<ArincWaypoint> waypoints = new HashSet<>();
    Set<ArincAirport> airports = new HashSet<>();
    Set<ArincNdbNavaid> ndbNavaids = new HashSet<>();
    Set<ArincVhfNavaid> vhfNavaids = new HashSet<>();
    Set<ArincAirway> arincAirways = new HashSet<>();
    Set<ArincHoldingPattern> holdingPatterns = new HashSet<>();

    private Standard() {}

    public void addWaypoint(ArincWaypoint waypoint){
      waypoints.add(waypoint);
    }
    public void addAirport(ArincAirport airport){
      airports.add(airport);
    }
    public void addNdbNavaid(ArincNdbNavaid ndb){
      ndbNavaids.add(ndb);
    }
    public void addVhfNavaid(ArincVhfNavaid vhf){
      vhfNavaids.add(vhf);
    }
    public void addAirway(ArincAirway arincAirway){
      arincAirways.add(arincAirway);
    }
    public void addHoldingPattern(ArincHoldingPattern holdingPattern){
      holdingPatterns.add(holdingPattern);
    }

    public Set<ArincWaypoint> waypoints() {
      return waypoints;
    }

    public ArincRecords waypoints(Set<ArincWaypoint> waypoints) {
      this.waypoints = waypoints;
      return this;
    }

    public Set<ArincAirport> airports() {
      return airports;
    }

    public ArincRecords airports(Set<ArincAirport> airports) {
      this.airports = airports;
      return this;
    }

    public Set<ArincNdbNavaid> ndbNavaids() {
      return ndbNavaids;
    }

    public ArincRecords ndbNavaids(Set<ArincNdbNavaid> ndbNavaids) {
      this.ndbNavaids = ndbNavaids;
      return this;
    }

    public Set<ArincVhfNavaid> vhfNavaids() {
      return vhfNavaids;
    }

    public ArincRecords vhfNavaids(Set<ArincVhfNavaid> vhfNavaids) {
      this.vhfNavaids = vhfNavaids;
      return this;
    }

    public Set<ArincAirway> arincAirways() {
      return arincAirways;
    }

    public ArincRecords arincAirways(Set<ArincAirway> arincAirways) {
      this.arincAirways = arincAirways;
      return this;
    }

    public Set<ArincHoldingPattern> holdingPatterns() {
      return holdingPatterns;
    }

    public ArincRecords holdingPatterns(Set<ArincHoldingPattern> holdingPatterns) {
      this.holdingPatterns = holdingPatterns;
      return this;
    }
  }
}
