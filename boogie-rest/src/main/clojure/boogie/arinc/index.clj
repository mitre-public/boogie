(ns boogie.arinc.index
  "Namespace for performing more targeted queries for infrastructure data."
  (:require [boogie.state :refer [get-fix-database get-terminal-database]]
            [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:import (org.mitre.tdp.boogie.arinc.assemble StandardizedTransitionName)
           (org.mitre.tdp.boogie.arinc.model ArincNdbNavaid ArincWaypoint ArincAirport ArincRunway ArincLocalizerGlideSlope ArincProcedureLeg)))

;; taken from the clojure docs for merging nested maps
(defn deep-merge [a & maps]
  (if (map? a)
    (apply merge-with deep-merge a maps)
    (apply merge-with deep-merge maps)))

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
  (let [waypoints (clojure.string/split identifiers-csv #",")]
    (timbre/info (str "Querying waypoints with identifier(s): " waypoints))
    (->> (.waypoints (get-fix-database) (into-array String waypoints))
         (map #(assoc {} (.waypointIdentifier %) (assoc {} (.waypointIcaoRegion %) %)))
         (reduce deep-merge {})
         )))

(defn vhf-navaids
  "Returns all vhf navaids matching the provided identifiers as a mapping of {Identifier, {IcaoRegion, ArincVhfNavaid}}"
  [identifiers-csv]
  (timbre/info (str "Querying VHF navaids with identifier(s): " identifiers-csv))
  (let [vhf-navaids (clojure.string/split identifiers-csv #",")]
    (->> (.vhfNavaids (get-fix-database) (into-array String vhf-navaids))
         (map #(assoc {} (.vhfIdentifier %) (assoc {} (.vhfIcaoRegion %) %)))
         (reduce deep-merge {}))))

(defn ndb-navaids
  "Returns all ndb navaids matching the provided identifiers as a mapping of {Identifier, {IcaoRegion, ArincNdbNavaid}}"
  [identifiers-csv]
  (timbre/info (str "Querying NDB navaids with identifier(s): " identifiers-csv))
  (let [ndb-navaids (clojure.string/split identifiers-csv #",")]
    (->> (.ndbNavaids (get-fix-database) (into-array String ndb-navaids))
         (map #(assoc {} (.ndbIdentifier %) (assoc {} (.ndbIcaoRegion %) %)))
         (reduce deep-merge {}))))

(defn procedure-legs
  "Returns all procedure legs at the given airport as {ProcedureName, {TransitionName, List[ProcedureLegs}} and the legs per transition
  are sorted by their sequence number."
  [procedures-csv airport]
  (timbre/info (str "Querying procedures with identifier(s): " procedures-csv))
  (->> (clojure.string/split procedures-csv #",")
       (map #(assoc {} % (->> (.legsForProcedure (get-terminal-database) airport %)
                              (sort-by (fn [leg] (.sequenceNumber leg)))
                              (group-by (fn [leg] (-> (.transitionIdentifier leg)
                                                      (.map StandardizedTransitionName/INSTANCE)
                                                      (.orElse "ALL")))))))
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
                          (.getTypeName ArincProcedureLeg)        (.allProcedureLegsAt (get-terminal-database) %)}))
       (reduce merge)))
