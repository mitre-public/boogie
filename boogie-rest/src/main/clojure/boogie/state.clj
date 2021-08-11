(ns boogie.state
  "Namespace for maintaining all of the application-level state information."
  (:require [boogie.arinc.cycles :refer [current-cycle]]
            [boogie.arinc.load :refer [load-cycle-from-file]]
            [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:import (org.mitre.tdp.boogie.arinc.database FixDatabase TerminalAreaDatabase ArincDatabaseFactory)
           (org.mitre.tdp.boogie.alg RouteExpander RouteExpanderFactory)
           (clojure.lang Atom)
           (org.mitre.tdp.boogie.arinc.assemble ProcedureAssembler AirwayAssembler AirportAssembler FixAssembler)
           (org.mitre.tdp.boogie.arinc.model ArincProcedureLeg ArincAirwayLeg ArincWaypoint ArincVhfNavaid ArincNdbNavaid ArincAirport ArincModel ArincRunway ArincLocalizerGlideSlope)
           (java.util.stream Collectors)))

(defonce uninitialized "UNINITIALIZED")

(defn initialized?
  "Returns the atom if the value within is initialized - otherwise throws an exception."
  [^Atom atom]
  (if (= uninitialized @atom) (throw (new IllegalStateException "Can't access uninitialized state - please initialize app state before performing operations.")) atom))

(defonce ^String target-cycle (atom (if (System/getenv "TARGET_CYCLE") (System/getenv "TARGET_CYCLE") (current-cycle))))

;{"file"                                  "filePath"
; (.getTypeName ArincAirport)             "Collection[ArincAirport]"
; (.getTypeName ArincRunway)              "Collection[ArincRunway]"
; (.getTypeName ArincLocalizerGlideSlope) "Collection[ArincLocalizerGlideSlope]"
; (.getTypeName ArincAirwayLeg)           "Collection[ArincAirwayLeg]"
; (.getTypeName ArincProcedureLeg)        "Collection[ArincProcedureLeg]"
; (.getTypeName ArincNdbNavaid)           "Collection[ArincNdbNavaid]"
; (.getTypeName ArincVhfNavaid)           "Collection[ArincVhfNavaid]"
; (.getTypeName ArincWaypoint)            "Collection[ArincWaypoint]"}
(defonce arinc-cycle (atom uninitialized))

(defn initialize-arinc-cycle [] (swap! arinc-cycle (fn [c] (load-cycle-from-file (current-cycle)))))

(defn get-arinc-cycle [] ((comp deref initialized?) arinc-cycle))

(defn new-fix-database [cycle-data]
  (timbre/info (str "Generating new fix database for target cycle."))
  (ArincDatabaseFactory/newFixDatabase
    (get cycle-data (.getTypeName ArincNdbNavaid))
    (get cycle-data (.getTypeName ArincVhfNavaid))
    (get cycle-data (.getTypeName ArincWaypoint))
    (get cycle-data (.getTypeName ArincAirport))))

(defn new-terminal-database [cycle-data]
  (timbre/info (str "Generating terminal database for target cycle."))
  (ArincDatabaseFactory/newTerminalAreaDatabase
    (get cycle-data (.getTypeName ArincAirport))
    (get cycle-data (.getTypeName ArincRunway))
    (get cycle-data (.getTypeName ArincLocalizerGlideSlope))
    (get cycle-data (.getTypeName ArincNdbNavaid))
    (get cycle-data (.getTypeName ArincVhfNavaid))
    (get cycle-data (.getTypeName ArincWaypoint))
    (get cycle-data (.getTypeName ArincProcedureLeg))))

; instanceOf FixDatabase
(defonce ^FixDatabase fix-database (atom uninitialized))

(defn initialize-fix-database [] (swap! fix-database (fn [a] (new-fix-database (get-arinc-cycle)))))

(defn get-fix-database
  "Returns the instance of the FixDatabase appropriate for the current cycle given the set of available files configured at launch time."
  [] ((comp deref initialized?) fix-database))

; instanceOf TerminalAreaDatabase
(defonce ^TerminalAreaDatabase terminal-database (atom uninitialized))

(defn initialize-terminal-database [] (swap! terminal-database (fn [a] (new-terminal-database (get-arinc-cycle)))))

(defn get-terminal-database
  "Returns the instance of the TerminalAreaDatabase appropriate for the current cycle given the set of available files configured at launch time."
  [] ((comp deref initialized?) terminal-database))

(defn assemble-procedures
  "Returns {Identifier, {Airport, List[Procedure]}}"
  []
  (let [procedure-assembler (new ProcedureAssembler (get-terminal-database) (get-fix-database))]
    (->> (.collect (.apply procedure-assembler (get (get-arinc-cycle) (.getTypeName ArincProcedureLeg))) (Collectors/toList))
         (group-by #(.procedureIdentifier %))
         (map (fn [entry] (assoc {} (key entry) (->> (val entry) (group-by #(.airportIdentifier %))))))
         (reduce merge))))

(defn assemble-airways
  "Returns {Identifier, List[Airway]}"
  []
  (let [airway-assembler (new AirwayAssembler (get-fix-database))]
    (->> (.collect (.apply airway-assembler (get (get-arinc-cycle) (.getTypeName ArincAirwayLeg))) (Collectors/toList))
         (group-by #(.airwayIdentifier %)))))

(defn assemble-fixes
  "Returns {Identifier, List[Fix]}, based on NDB/VHF navaids and waypoints."
  []
  (let [cycle-data (get-arinc-cycle)]
    (->> [(get cycle-data (.getTypeName ArincWaypoint))
          (get cycle-data (.getTypeName ArincVhfNavaid))
          (get cycle-data (.getTypeName ArincNdbNavaid))]
         (mapcat #(->> % (map (fn [^ArincModel model] (.apply (FixAssembler/INSTANCE) model)))))
         (group-by #(.fixIdentifier %)))))

(defn assemble-airports
  "Returns a list of all assembled airport records."
  []
  (let [airport-assembler (new AirportAssembler (get-terminal-database))]
    (->> (get (get-arinc-cycle) (.getTypeName ArincAirport))
         (map #(.apply airport-assembler %))
         (group-by #(.airportIdentifier %)))))

; {Identifier, {Airport, List[Procedure]}}
(defonce assembled-procedures (atom uninitialized))

(defn initialize-assembled-procedures [] (swap! assembled-procedures (fn [a] (assemble-procedures))))

(defn get-assembled-procedures
  "Returns the current collection of active Procedures as {Identifier, {Airport, List[Procedure]}}."
  [] ((comp deref initialized?) assembled-procedures))

; {Identifier, List[Airway]}
(defonce assembled-airways (atom uninitialized))

(defn initialize-assembled-airways [] (swap! assembled-airways (fn [a] (assemble-airways))))

(defn get-assembled-airways
  "Returns the current set of active Airways as {Identifier, List[Airway]}."
  [] ((comp deref initialized?) assembled-airways))

; {Identifier, List[Fix]}
(defonce assembled-fixes (atom uninitialized))

(defn initialize-assembled-fixes [] (swap! assembled-fixes (fn [a] (assemble-fixes))))

(defn get-assembled-fixes
  "Returns the current set of active Fixes as {Identifier, List[Fix]}."
  [] ((comp deref initialized?) assembled-fixes))

; {Identifier, List[Airport]}
(defonce assembled-airports (atom uninitialized))

(defn initialize-assembled-airports [] (swap! assembled-airports (fn [a] (assemble-airports))))

(defn get-assembled-airports
  "Returns the current set of active Airports as {Identifier, List[Airport]}."
  [] ((comp deref initialized?) assembled-airports))

(defn new-route-expander
  "Instantiates a new RouteExpander based on the provided collection(s) of converted Fixes/Airports/Airways/Procedures."
  []
  (let [airports (->> (get-assembled-airports) (mapcat val))
        fixes (->> (get-assembled-fixes) (mapcat val))
        airways (->> (get-assembled-airways) (mapcat val))
        procedures (->> (get-assembled-procedures) (mapcat #(->> (val %) (mapcat val))))]
    (timbre/info (str "Initializing new RouteExpander.\n"
                      " Airports: " (count airports) "\n"
                      " Fixes: " (count fixes) "\n"
                      " Airways: " (count airways) "\n"
                      " Procedures: " (count procedures) "\n"))
    (RouteExpanderFactory/newGraphicalRouteExpander fixes airways airports procedures)))

; instanceof RouteExpander
(defonce ^RouteExpander route-expander (atom uninitialized))

(defn initialize-route-expander [] (swap! route-expander (fn [a] (new-route-expander))))

(defn get-route-expander [] ((comp deref initialized?) route-expander))

(defn initialize-application-state
  "Sequentially initializes all of the application state in the order:
  <br>
  1. Load/Parse cycle of infrastructure information
  2. Index cycle in fix/terminal databases
  3. Assemble all Boogie model types from ARINC cycle
  4. Initialize a new RouteExpander instance for use
  <br>
  Once that sequence is done the application is ready to be used an all state is initialized."
  []
  (taoensso.timbre/info "Loading infrastructure from disk.")
  (initialize-arinc-cycle)
  (taoensso.timbre/info "Indexing cycle in internal databases.")
  (initialize-fix-database) (initialize-terminal-database)
  (taoensso.timbre/info "Assembling boogie model types.")
  (initialize-assembled-procedures) (initialize-assembled-airways) (initialize-assembled-fixes) (initialize-assembled-airports)
  (taoensso.timbre/info "Configuring RouteExpander.")
  (initialize-route-expander)
  (taoensso.timbre/info "Application state successfully initialized."))