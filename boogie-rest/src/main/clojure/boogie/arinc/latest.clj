(ns boogie.arinc.latest
  "Namespace for performing more targeted queries for infrastructure data."
  (:require [boogie.arinc.cycles :refer [get-cycle-data current-cycle is-cached? target-cycle-file]])
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:import (org.mitre.tdp.boogie.arinc.database ArincDatabaseFactory TerminalAreaDatabase FixDatabase)
           (org.mitre.tdp.boogie.arinc.model ArincNdbNavaid ArincVhfNavaid ArincWaypoint ArincAirport ArincRunway ArincLocalizerGlideSlope ArincProcedureLeg)
           (java.util Collections)))

;; taken from the clojure docs for merging nested maps
(defn deep-merge [a & maps]
  (if (map? a)
    (apply merge-with deep-merge a maps)
    (apply merge-with deep-merge maps)))

(defn new-fix-database [cycle]
  (let [[target-cycle target-file] (target-cycle-file cycle)
        cycle-data (get-cycle-data cycle)]
    (timbre/info (str "Generating fix database for target cycle " target-cycle))
    (if (not (contains? cycle-data "file"))
      (do (timbre/warn (str "Instantiating empty FixDatabase: " target-cycle))
          (ArincDatabaseFactory/newFixDatabase
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)))
      (ArincDatabaseFactory/newFixDatabase
        (get cycle-data (.getTypeName ArincNdbNavaid))
        (get cycle-data (.getTypeName ArincVhfNavaid))
        (get cycle-data (.getTypeName ArincWaypoint))
        (get cycle-data (.getTypeName ArincAirport))))))

(defn new-terminal-database [cycle]
  (let [[target-cycle target-file] (target-cycle-file cycle)
        cycle-data (get-cycle-data cycle)]
    (timbre/info (str "Generating terminal database for target cycle " target-cycle))
    (if (not (contains? cycle-data "file"))
      (do (timbre/warn (str "Instantiating empty TerminalAreaDatabase: " target-cycle))
          (ArincDatabaseFactory/newTerminalAreaDatabase
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)
            (Collections/emptyList)))
      (ArincDatabaseFactory/newTerminalAreaDatabase
        (get cycle-data (.getTypeName ArincAirport))
        (get cycle-data (.getTypeName ArincRunway))
        (get cycle-data (.getTypeName ArincLocalizerGlideSlope))
        (get cycle-data (.getTypeName ArincNdbNavaid))
        (get cycle-data (.getTypeName ArincVhfNavaid))
        (get cycle-data (.getTypeName ArincWaypoint))
        (get cycle-data (.getTypeName ArincProcedureLeg))))))

(defonce ^FixDatabase fix-database (atom (new-fix-database (current-cycle))))

(defn get-fix-database
  "Returns the instance of the FixDatabase appropriate for the current cycle given the set of available files configured at launch time.
  If the current fix-database isn't up-to-date with the latest available cycle then this loads the current cycle and re-indexes it."
  []
  (if (is-cached? (current-cycle)) @fix-database (swap! fix-database (fn [atm] (new-fix-database (current-cycle))))))

(defonce ^TerminalAreaDatabase terminal-database (atom (new-terminal-database (current-cycle))))

(defn get-terminal-database
  "Returns the instance of the TerminalAreaDatabase appropriate for the current cycle given the set of available files configured at launch time.
  If the current terminal-database isn't up-to-date with the latest available cycle then this loads the current cycle and re-indexes it."
  []
  (if (is-cached? (current-cycle)) @terminal-database (swap! terminal-database (fn [atm] (new-terminal-database (current-cycle))))))

(defn airports
  "Returns the mapping of {AirportIdentifier, ArincAirport} matching the input set of comma-delimited airport identifiers."
  [airport-csv]
  (timbre/info (str "Querying airport(s): " airport-csv))
  (let [airports (clojure.string/split airport-csv #",")]
    (->> (.airports (get-fix-database) (into-array String airports))
         (map #(assoc {} (.airportIdentifier %) %))
         (reduce merge))))

(defn runways
  "Returns the mapping of {AirportIdentifier, {RunwayIdentifier, ArincRunway}} matching the input set of comma-delimited airport
  identifiers."
  [airport-csv]
  (timbre/info (str "Querying runways at airport(s): " airport-csv))
  (->> (clojure.string/split airport-csv #",")
       (map #(assoc {} % (->> (.runwaysAt (get-terminal-database) %)
                              (map (fn [r] (assoc {} (.runwayIdentifier r) r)))
                              (reduce merge))))
       (reduce merge)))

(defn localizers
  "Returns the mapping of {AirportIdentifier, {LocalizerIdentifier, ArincLocalizerGlideSlope}} matching the input set of comma
  delimited airport identifiers."
  [airport-csv]
  (timbre/info (str "Querying localizers at airport(s): " airport-csv))
  (->> (clojure.string/split airport-csv #",")
       (map #(assoc {} % (->> (.localizerGlideSlopesAt (get-terminal-database) %)
                              (map (fn [l] (assoc {} (.localizerIdentifier l) l)))
                              (reduce merge))))
       (reduce merge)))

(defn waypoints
  "Returns all terminal and enroute waypoints matching the provided identifiers as a mapping of {Identifier, {IcaoRegion, ArincWaypoint}}"
  [identifiers-csv]
  (timbre/info (str "Querying waypoints with identifier(s): " identifiers-csv))
  (let [waypoints (clojure.string/split identifiers-csv #",")]
    (->> (.waypoints (get-fix-database) (into-array String waypoints))
         (map #(assoc {} (.waypointIdentifier %) (assoc {} (.waypointIcaoRegion %) %)))
         (reduce {} deep-merge))))

(defn vhf-navaids
  "Returns all vhf navaids matching the provided identifiers as a mapping of {Identifier, {IcaoRegion, ArincWaypoint}}"
  [identifiers-csv]
  (timbre/info (str "Querying VHF navaids with identifier(s): " identifiers-csv))
  (let [vhf-navaids (clojure.string/split identifiers-csv #",")]
    (->> (.vhfNavaids (get-fix-database) (into-array String vhf-navaids))
         (map #(assoc {} (.vhfIdentifier %) (assoc {} (.vhfIcaoRegion %) %)))
         (reduce {} deep-merge))))

(defn ndb-navaids
  "Returns all ndb navaids matching the provided identifiers as a mapping of {Identifier, {IcaoRegion, ArincWaypoint}}"
  [identifiers-csv]
  (timbre/info (str "Querying NDB navaids with identifier(s): " identifiers-csv))
  (let [ndb-navaids (clojure.string/split identifiers-csv #",")]
    (->> (.ndbNavaids (get-fix-database) (into-array String ndb-navaids))
         (map #(assoc {} (.ndbIdentifier %) (assoc {} (.ndbIcaoRegion %) %)))
         (reduce {} deep-merge))))

(defn procedure-legs
  "Returns all procedure legs at the given airport as {ProcedureName, List[ProcedureLegs]}"
  [procedures-csv airport]
  (timbre/info (str "Querying procedures with identifier(s): " procedures-csv))
  (->> (clojure.string/split procedures-csv #",")
       (map #(assoc {} % (.legsForProcedure (get-terminal-database) airport %)))
       (reduce merge)))

(defn terminal-areas
  "Returns all ARINC data objects related to the given terminal area(s) as {Airport/TerminalArea, {TypeClassName, List[Records]}}.
   Note all values are lists except the airport record which is a singleton."
  [terminal-areas-csv]
  (->> (clojure.string/split terminal-areas-csv #",")
       (map #(assoc {} % {(.getTypeName ArincAirport)             (.airport (get-terminal-database) %)
                          (.getTypeName ArincRunway)              (.runwaysAt (get-terminal-database) %)
                          (.getTypeName ArincLocalizerGlideSlope) (.localizerGlideSlopesAt (get-terminal-database) %)
                          (.getTypeName ArincNdbNavaid)           (.ndbNavaidsAt (get-terminal-database) %)
                          (.getTypeName ArincWaypoint)            (.waypointsAt (get-terminal-database) %)
                          (.getTypeName ArincProcedureLeg)        (.allProcedureLegsAt (get-terminal-database) %)}))))
