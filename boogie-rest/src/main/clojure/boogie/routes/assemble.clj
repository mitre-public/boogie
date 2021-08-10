(ns boogie.routes.assemble
  "Namespace for assembling the records loaded in cycles.clj such that they match the Boogie interfaces for Fixes/Airports/Airways/Procedures."
  (:require [boogie.arinc.cycles :refer [get-cycle-data current-cycle is-cached? target-cycle-file]])
  (:require [boogie.arinc.latest :refer [get-fix-database get-terminal-database]])
  (:import (org.mitre.tdp.boogie.arinc.assemble ProcedureAssembler AirwayAssembler AirportAssembler FixAssembler)
           (org.mitre.tdp.boogie.arinc.model ArincProcedureLeg ArincAirwayLeg)
           (java.util.stream Collectors)))

(defn assemble-procedures
  "Returns list of assembled procedure records."
  []
  (let [procedure-assembler (new ProcedureAssembler (get-terminal-database) (get-fix-database))]
    (.collect (.apply procedure-assembler (get (get-cycle-data (current-cycle)) (.getTypeName ArincProcedureLeg))) (Collectors/toList))))

(defn assemble-airways
  "Returns a list of assembled airway records."
  []
  (let [airway-assembler (new AirwayAssembler (get-fix-database))]
    (.collect (.apply airway-assembler (get (get-cycle-data (current-cycle)) (.getTypeName ArincAirwayLeg))) (Collectors/toList))))

(defn assemble-fixes []
  (let [fix-assembler (new FixAssembler)]
    (->> ())))

(defn assemble-airports []
  (let [airport-assembler (new AirportAssembler (get-terminal-database))]
    ))

(def procedures (atom (assemble-procedures)))

(def airways (atom (assemble-airways)))

(def fixes (atom (assemble-fixes)))

(def airports (atom (assemble-airports)))

(defn get-procedures
  "Returns the current collection of active Procedures.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of procedures."
  []
  (if (is-cached? (current-cycle)) @procedures (swap! procedures (fn [prc] (assemble-procedures)))))

(defn get-airways
  "Returns the current set of active Airways.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of Airways."
  [])

(defn get-fixes
  "Returns the current set of active Fixes.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of Fixes."
  [])

(defn get-airports
  "Returns the current set of active Airports.
  If the current cycle is no longer the cached cycle this action will trigger a re-assembly of the current cycle of airports."
  [])

(defn procedures
  "Returns the collection of assembled procedures matching the provided CSV list of procedure names."
  [identifiers-csv])

(defn airways
  "Returns the collection of assembled airways matching the provided CSV list of airway names."
  [identifiers-csv])

(defn fixes
  "Returns the collection of assembled fixes matching the provided CSV list of fix names."
  [identifiers-csv])

(defn airport
  "Returns to collection of assembled airports matching the provided CSV list of airport names."
  [identifiers-csv])
