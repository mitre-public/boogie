(ns boogie.arinc.cycles
  (:require [clojure.data.avl :as avl]
            [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:require [clojure.core [cache :as cache]])
  (:import (org.mitre.tdp.boogie.arinc ArincRecord ArincVersion PatternBasedFileLocator ArincFileStore ArincFileParser)
           (java.time Instant)
           (org.mitre.tdp.boogie.arinc.utils AiracCycle)
           (org.mitre.tdp.boogie.arinc.model ArincRecordConverterFactory ArincAirport ArincRunway ArincLocalizerGlideSlope ArincAirwayLeg ArincProcedureLeg ArincNdbNavaid ArincVhfNavaid ArincWaypoint)
           (java.io File)))



;; the maximum number of cycles that can be cached at once
(defonce cycle-cache-size (atom (if (System/getenv "CYCLE_CACHE_SIZE") (Integer/parseInt (System/getenv "CYCLE_CACHE_SIZE")) 3)))

;; The instance of the file-locator which will be used to locate ARINC424 files on a visible filesystem for the application to
;; load and serve data from
;; This will use a provided file path template if provided as an ENV variable at start-up, otherwise will default to the MITRE
;; LIDO archive
(defonce ^PatternBasedFileLocator file-locator (atom (if (System/getenv "FILE_LOCATOR_PATH")
                                                       (new PatternBasedFileLocator (System/getenv "FILE_LOCATOR_PATH"))
                                                       (ArincFileStore/MITRE_CIFP))))

(defonce ^ArincFileParser arinc-file-parser (atom (new ArincFileParser (.parser (ArincVersion/V19)))))

(defn current-cycle "Returns the current cycle as of time of the call to the method." [] (.cycle (new AiracCycle (Instant/now))))

(defn cycle-file
  "Converts the given cycle into the path it would be resolved to based on the FILE_LOCATOR_PATH."
  [cycle] (.apply @file-locator cycle))

(defn find-available-files
  "Returns a mapping from {cycle, filePath} for all files that exist within the date range based on the configured FILE_LOCATOR_PATH.
  The underlying map implementation is from the clojure AVL library - and gives access to features similar to a java NavigableMap."
  []
  (let [earliest (Instant/parse "2015-01-01T00:00:00Z")]
    (taoensso.timbre/info "Indexing available files.")
    (let [res (->> (AiracCycle/cyclesBetween earliest (Instant/now))
                   (filter #(.exists (cycle-file %)))
                   (map #(assoc {} % {:path (.getAbsolutePath (cycle-file %))}))
                   (reduce merge))]
      (into (avl/sorted-map) (if (nil? res) {} (into {} res))))))

;; the pre-indexed set of all available files
(defonce available-files (atom (find-available-files)))

(defn nearest-available
  "Returns the nearest cycle/file to the requested cycle. If the file/cycle combination don't exist in the list of available file/cycle
  combinations but the input file exists it will be added to the list of available files."
  [cycle file]
  (if (.exists file)
    (do (swap! available-files (fn [m] (assoc m cycle {:path (.getAbsolutePath file)})))
        [cycle file])
    (let [lower (avl/nearest @available-files < cycle)
          upper (avl/nearest @available-files > cycle)
          entry (if (nil? lower) upper lower)]
      (if (nil? entry) [nil nil] [(key entry) (new File (:path (val entry)))]))))

(defn load-cycle
  "Attempts to load (from disk) the file of ARINC-424 data appropriate for the provided cycle. If the requested cycle can't be found in
  the set of available files then it returns the nearest cycle to that which was requested as well as the appropriate file path."
  [^File target-file ^String target-cycle]
  (let [converter (ArincRecordConverterFactory/consumerForVersion (ArincVersion/V19))
        ;; parse the ArincRecord generics from the file
        records (.apply @arinc-file-parser target-file)]
    (timbre/info (str "Parsed " (count records) " records from file."))
    ; apply the converter to collect the java pojos
    (doall (map #(.accept converter %) records))
    (timbre/info (str "Finished conversion of cycle " target-cycle))
    {"file"                                  (.getAbsolutePath target-file)
     "cycle"                                 target-cycle
     (.getTypeName ArincAirport)             (.arincAirports converter)
     (.getTypeName ArincRunway)              (.arincRunways converter)
     (.getTypeName ArincLocalizerGlideSlope) (.arincLocalizerGlideSlopes converter)
     (.getTypeName ArincAirwayLeg)           (.arincAirwayLegs converter)
     (.getTypeName ArincProcedureLeg)        (.arincProcedureLegs converter)
     (.getTypeName ArincNdbNavaid)           (.arincNdbNavaids converter)
     (.getTypeName ArincVhfNavaid)           (.arincVhfNavaids converter)
     (.getTypeName ArincWaypoint)            (.arincWaypoints converter)}))

;; LRU cache implementation with a max size as configured by environment variable
;; this cache pre-loads the current cycle of navigation data on instantiation
(def cycle-cache (atom (cache/lru-cache-factory {} :threshold @cycle-cache-size)))

(defn get-available-cycles
  "Subsequent call to return all available files and update the listing of what's available explicitly. This also adds to the available file listing an
  indicator for whether the given file is currently in the cache."
  []
  (let [files-with-cache-status (->> (find-available-files)
                                     (map #(assoc {} (key %) (assoc (val %) :in-cache? (cache/has? @cycle-cache (key %)))))
                                     (reduce merge)
                                     (into (avl/sorted-map)))]
    (swap! available-files (fn [m] files-with-cache-status))))

(defn get-cycle-data
  "Provides wrapped access to the underlying cycle-cache of the application."
  [^String requested-cycle]
  (let [expected-file (cycle-file requested-cycle)
        [^String target-cycle ^File target-file] (nearest-available requested-cycle expected-file)]
    (timbre/info (str "Hitting cache for requested cycle: " requested-cycle))
    (if (nil? target-cycle)
      (do (timbre/info (str "Unable find available data for requested cycle " requested-cycle)) {requested-cycle {}})
      ;; if there is a valid target cycle which we can map the request to look it up
      (do (timbre/info (str "Targeting cycle " target-cycle " based on requested cycle " requested-cycle ", load initializing."))
          (if (cache/has? @cycle-cache target-cycle)
            (timbre/info (str "Target cycle found in cache - skipping parse."))
            (timbre/info (str "Targeted file for parse is: " (.getAbsolutePath target-file))))
          ;; this updates the cache to contain the supplied cycle of data if it's not already present - and then updates the cache atom
          ;; to the update cache containing the additional entry
          (swap! cycle-cache cache/through-cache target-cycle (partial load-cycle target-file))
          ;; this then looks-up that cycle in the cache (which is now sure to contain it)... clojure caches are weird
          (cache/lookup @cycle-cache target-cycle)))))
