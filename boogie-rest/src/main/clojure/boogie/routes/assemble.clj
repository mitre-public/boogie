(ns boogie.routes.assemble
  "Namespace for assembling the records loaded in cycles.clj such that they match the Boogie interfaces for Fixes/Airports/Airways/Procedures."
  (:require [boogie.arinc.cycles :refer [get-cycle-data current-cycle is-cached? target-cycle-file]])
  (:require [boogie.arinc.latest :refer [get-fix-database get-terminal-database deep-merge]]
            [taoensso.timbre :as timbre])
  (:import (org.mitre.tdp.boogie.arinc.assemble ProcedureAssembler AirwayAssembler AirportAssembler FixAssembler)
           (org.mitre.tdp.boogie.arinc.model ArincProcedureLeg ArincAirwayLeg ArincWaypoint ArincVhfNavaid ArincNdbNavaid ArincAirport ArincModel)
           (java.util.stream Collectors)))

(defn assemble-procedures
  "Returns {Identifier, {Airport, List[Procedure]}}"
  []
  (let [procedure-assembler (new ProcedureAssembler (get-terminal-database) (get-fix-database))]
    (->> (.collect (.apply procedure-assembler (get (get-cycle-data (current-cycle)) (.getTypeName ArincProcedureLeg))) (Collectors/toList))
         (group-by #(.procedureIdentifier %))
         (map (fn [entry] (assoc {} (key entry) (->> (val entry) (group-by #(.airportIdentifier %))))))
         (reduce merge))))

(defn assemble-airways
  "Returns {Identifier, List[Airway]}"
  []
  (let [airway-assembler (new AirwayAssembler (get-fix-database))]
    (->> (.collect (.apply airway-assembler (get (get-cycle-data (current-cycle)) (.getTypeName ArincAirwayLeg))) (Collectors/toList))
         (group-by #(.airwayIdentifier %)))))

(defn assemble-fixes
  "Returns {Identifier, List[Fix]}, based on NDB/VHF navaids and waypoints."
  []
  (let [cycle-data (get-cycle-data (current-cycle))]
    (->> [(get cycle-data (.getTypeName ArincWaypoint))
          (get cycle-data (.getTypeName ArincVhfNavaid))
          (get cycle-data (.getTypeName ArincNdbNavaid))]
         (mapcat #(->> % (map (fn [^ArincModel model] (.apply (FixAssembler/INSTANCE) model)))))
         (group-by #(.fixIdentifier %)))))

(defn assemble-airports
  "Returns a list of all assembled airport records."
  []
  (let [airport-assembler (new AirportAssembler (get-terminal-database))]
    (->> (get (get-cycle-data (current-cycle)) (.getTypeName ArincAirport))
         (map #(.apply airport-assembler %))
         (group-by #(.airportIdentifier %)))))

;; assembled procedures cache
(defonce procedures (atom (assemble-procedures)))

(defn re-initialize-procedures [] (swap! procedures (fn [prcs] (assemble-procedures))))

(defn get-procedures
  "Returns the current collection of active Procedures as {Identifier, {Airport, List[Procedure]}}.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of procedures."
  []
  (if (is-cached? (current-cycle)) @procedures (re-initialize-procedures)))

;; assembled airways cache
(defonce airways (atom (assemble-airways)))

(defn re-initialize-airways [] (swap! airways (fn [awys] (assemble-airways))))

(defn get-airways
  "Returns the current set of active Airways as {Identifier, List[Airway]}.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of Airways."
  []
  (if (is-cached? (current-cycle)) @airways (re-initialize-airways)))

;; assembled fixes cache
(defonce fixes (atom (assemble-fixes)))

(defn re-initialize-fixes [] (swap! fixes (fn [fxs] (assemble-fixes))))

(defn get-fixes
  "Returns the current set of active Fixes as {Identifier, List[Fix]}.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of Fixes."
  []
  (if (is-cached? (current-cycle)) @fixes (re-initialize-fixes)))

;; assembled airports cache
(defonce airports (atom (assemble-airports)))

(defn re-initialize-airports [] (swap! airports (fn [apts] (assemble-airports))))

(defn get-airports
  "Returns the current set of active Airports as {Identifier, List[Airport]}.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of airports."
  []
  (if (is-cached? (current-cycle)) @airports (re-initialize-airports)))

(defn procedures-by-identifier
  "Returns the collection of assembled procedures matching the provided CSV list of procedure names."
  ([identifiers-csv]
   (procedures-by-identifier identifiers-csv nil))
  ([identifiers-csv airport-identifiers]
   (let [prcs (clojure.string/split identifiers-csv #",")
         apts (if (= nil airport-identifiers) [] (do (timbre/debug airport-identifiers) (clojure.string/split airport-identifiers #",")))]
     (->> prcs (map #(assoc {} % (let [pmap (get (get-procedures) %)] (if (empty? apts) pmap (select-keys pmap apts))))) (reduce merge)))))

(defn airways-by-identifier
  "Returns the collection of assembled airways matching the provided CSV list of airway names."
  [identifiers-csv]
  (let [awys (clojure.string/split identifiers-csv #",")]
    (->> awys (map #(assoc {} % (get (get-airways) %))) (reduce merge))))

(defn fixes-by-identifier
  "Returns the collection of assembled fixes matching the provided CSV list of fix names."
  [identifiers-csv]
  (let [fxs (clojure.string/split identifiers-csv #",")]
    (->> fxs (map #(assoc {} % (get (get-fixes) %))) (reduce merge))))

(defn airports-by-identifier
  "Returns to collection of assembled airports matching the provided CSV list of airport names."
  [identifiers-csv]
  (let [apts (clojure.string/split identifiers-csv #",")]
    (->> apts (map #(assoc {} % (get (get-airports) %))) (reduce merge))))
