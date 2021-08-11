(ns boogie.arinc.cycles
  (:require [clojure.data.avl :as avl]
            [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:import (org.mitre.tdp.boogie.arinc PatternBasedFileLocator ArincFileStore)
           (java.time Instant)
           (org.mitre.tdp.boogie.arinc.utils AiracCycle)
           (java.io File)))

;; The instance of the file-locator which will be used to locate ARINC424 files on a visible filesystem for the application to
;; load and serve data from
;; This will use a provided file path template if provided as an ENV variable at start-up, otherwise will default to the MITRE
;; LIDO archive
(defonce ^PatternBasedFileLocator file-locator (atom (if (System/getenv "FILE_LOCATOR_PATH")
                                                       (new PatternBasedFileLocator (System/getenv "FILE_LOCATOR_PATH"))
                                                       (ArincFileStore/MITRE_CIFP))))

(defn current-cycle "Returns the current cycle as of time of the call to the method." [] (.cycle (new AiracCycle (Instant/now))))

(defn cycle-file
  "Converts the given cycle into the path it would be resolved to based on the FILE_LOCATOR_PATH."
  [cycle] (.apply @file-locator cycle))

(defn find-available-files
  "Returns a mapping from {cycle, {:path filePath}} for all files that exist within the date range based on the configured FILE_LOCATOR_PATH.
  <br>
  The underlying map implementation is from the clojure AVL library - and gives access to features similar to a java NavigableMap."
  []
  (let [earliest (Instant/parse "2015-01-01T00:00:00Z")]
    (taoensso.timbre/info "Indexing available files.")
    (let [res (->> (AiracCycle/cyclesBetween earliest (Instant/now))
                   (filter #(.exists (cycle-file %)))
                   (map #(assoc {} % {:path (.getAbsolutePath (cycle-file %))}))
                   (reduce merge))]
      (into (avl/sorted-map) (if (nil? res) {} (into {} res))))))

(defn nearest-available
  "Returns the nearest cycle/file to the requested cycle as [^String cycle ^File file] or [nil nil] if there are none available.
  <br>
  If the file/cycle combination don't exist in the list of available file/cycle combinations but the input file exists it will be
  added to the list of available files."
  [cycle]
  (let [available-files (find-available-files)
        lower (avl/nearest available-files <= cycle)
        upper (avl/nearest available-files >= cycle)
        entry (if (nil? lower) upper lower)]
    (timbre/debug (str "Finding nearest to cycle " cycle " in " (keys available-files)))
    (if (nil? entry) [nil nil] [(key entry) (new File (:path (val entry)))])))
