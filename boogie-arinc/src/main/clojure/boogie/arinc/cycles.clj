(ns boogie.arinc.cycles
  (:require [clojure.data.avl :as avl])
  (:import (org.mitre.tdp.boogie.arinc ArincRecord ArincVersion PatternBasedFileLocator ArincFileStore ArincFileParser)
           (com.google.common.cache LoadingCache)
           (com.google.common.cache CacheBuilder CacheLoader)
           (java.time Instant)
           (org.mitre.tdp.boogie.arinc.utils AiracCycle)
           (org.mitre.tdp.boogie.arinc.model ConvertingArincRecordConsumerFactory))
  (:gen-class))

;; the maximum number of cycles that can be cached at once
(defonce cycle-cache-size (atom (if (System/getenv "CYCLE_CACHE_SIZE") (System/getenv "CYCLE_CACHE_SIZE") 5)))

;; The instance of the file-locator which will be used to locate ARINC424 files on a visible filesystem for the application to
;; load and serve data from
;; This will use a provided file path template if provided as an ENV variable at start-up, otherwise will default to the MITRE
;; LIDO archive
(defonce ^PatternBasedFileLocator file-locator (atom (if (System/getenv "FILE_LOCATOR_PATH")
                                                       (new PatternBasedFileLocator (System/getenv "FILE_LOCATOR_PATH"))
                                                       (ArincFileStore/MITRE_LIDO))))

(defonce arinc-file-parser (new ArincFileParser (.parser (ArincVersion/V19))))

(defn cycle-file
  "Converts the given cycle into the path it would be resolved to based on the FILE_LOCATOR_PATH."
  [cycle] (.apply @file-locator cycle))

(defn find-available-files
  "Returns a mapping from {cycle, filePath} for all files that exist within the date range based on the configured FILE_LOCATOR_PATH."
  []
  (let [earliest (Instant/parse "2015-01-01T00:00:00Z")]
    (->> (AiracCycle/cyclesBetween earliest (Instant/now)) (filter #(.exists (cycle-file %))) (map #(assoc {} % (cycle-file %))) (reduce merge))))

;; the pre-indexed set of all available files
(defonce available-files (atom (into (sorted-map) (find-available-files))))

(defn get-available-cycles [] @available-files)

(defn nearest-available
  "Returns the nearest cycle/file to the requested cycle. If the file/cycle combination don't exist in the list of available file/cycle
  combinations but the input file exists it will be added to the list of available files."
  [cycle file]
  (if (.exists file)
    (do (swap! available-files (fn [m] (assoc m cycle file)))
        [cycle file])
    (let [entry (avl/nearest @available-files < cycle)]
      [(key entry) (val entry)])))

(defn load-cycle
  "Attempts to load (from disk) the file of ARINC-424 data appropriate for the provided cycle. If the requested cycle can't be found in
  the set of available files then it returns the nearest cycle to that which was requested as well as the appropriate file path."
  [^String cycle]
  (let [expected-file (cycle-file cycle)
        [target-cycle target-file] (nearest-available cycle expected-file)
        converter (ConvertingArincRecordConsumerFactory/forArincVersion (ArincVersion/V19))]
    (do (map #(.accept converter %) (.apply arinc-file-parser target-file))
        {target-cycle {:airports             (.arincAirports converter)
                       :runways              (.arincRunways converter)
                       :localizerGlideSlopes (.arincLocalizerGlideSlopes converter)
                       :airwayLegs           (.arincAirwayLegs converter)
                       :procedureLegs        (.arincProcedureLegs converter)
                       :ndbNavaids           (.arincNdbNavaids converter)
                       :vhfNavaids           (.arincVhfNavaids converter)
                       :waypoints            (.arincWaypoints converter)}})))

;; Have to proxy (implement/concrete subclass) the CacheLoader abstract class as part of the cache building
(def ^CacheLoader cache-loader (proxy [CacheLoader] [] (load [^String key] (load-cycle key))))

(defonce cycle-cache (atom (-> (CacheBuilder/newBuilder) (.maximumSize @cycle-cache-size) (.build cache-loader))))

(defn get-cycle-data
  "Provides wrapped access to the underlying cache of the application."
  [cycle] (.load @cycle-cache cycle))
