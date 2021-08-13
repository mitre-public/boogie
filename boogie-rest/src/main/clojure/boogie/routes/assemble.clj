(ns boogie.routes.assemble
  "Namespace for assembling the records loaded in cycles.clj such that they match the Boogie interfaces for Fixes/Airports/Airways/Procedures."
  (:require [boogie.state :refer [get-arinc-cycle get-fix-database get-terminal-database get-assembled-procedures get-assembled-airways get-assembled-fixes get-assembled-airports]]
            [taoensso.timbre :as timbre]))

(defn procedures-by-identifier
  "Returns the collection of assembled procedures matching the provided CSV list of procedure names."
  ([identifiers-csv]
   (procedures-by-identifier identifiers-csv nil))
  ([identifiers-csv airport-identifiers]
   (let [prcs (clojure.string/split identifiers-csv #",")
         apts (if (= nil airport-identifiers) [] (do (timbre/debug airport-identifiers) (clojure.string/split airport-identifiers #",")))]
     (->> prcs (map #(assoc {} % (let [pmap (get (get-assembled-procedures) %)] (if (empty? apts) pmap (select-keys pmap apts))))) (reduce merge)))))

(defn airways-by-identifier
  "Returns the collection of assembled airways matching the provided CSV list of airway names."
  [identifiers-csv]
  (let [awys (clojure.string/split identifiers-csv #",")]
    (->> awys (map #(assoc {} % (get (get-assembled-airways) %))) (reduce merge))))

(defn fixes-by-identifier
  "Returns the collection of assembled fixes matching the provided CSV list of fix names."
  [identifiers-csv]
  (let [fxs (clojure.string/split identifiers-csv #",")]
    (->> fxs (map #(assoc {} % (get (get-assembled-fixes) %))) (reduce merge))))

(defn airports-by-identifier
  "Returns to collection of assembled airports matching the provided CSV list of airport names."
  [identifiers-csv]
  (let [apts (clojure.string/split identifiers-csv #",")]
    (->> apts (map #(assoc {} % (get (get-assembled-airports) %))) (reduce merge))))