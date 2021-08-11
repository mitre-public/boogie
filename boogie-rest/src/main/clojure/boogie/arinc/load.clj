(ns boogie.arinc.load
  "The namespace for loading cycles of infrastructure data from disc."
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]]
            [boogie.arinc.cycles :refer [nearest-available]])
  (:import (org.mitre.tdp.boogie.arinc ArincVersion ArincFileParser)
           (org.mitre.tdp.boogie.arinc.model ArincRecordConverterFactory ArincAirport ArincRunway ArincLocalizerGlideSlope ArincAirwayLeg ArincProcedureLeg ArincNdbNavaid ArincVhfNavaid ArincWaypoint)
           (java.io File)))

(defonce ^ArincFileParser arinc-file-parser (atom (new ArincFileParser (.parser (ArincVersion/V19)))))

(defn load-from-file
  "Attempts to load (from disk) the file of ARINC-424 data appropriate for the provided cycle. If the requested cycle can't be found in
  the set of available files then it returns the nearest cycle to that which was requested as well as the appropriate file path."
  [^File target-file]
  (let [converter (ArincRecordConverterFactory/consumerForVersion (ArincVersion/V19))
        ;; parse the ArincRecord generics from the file
        records (.apply @arinc-file-parser target-file)]
    (timbre/info (str "Parsed " (count records) " records from file."))
    ; apply the converter to collect the java pojos
    (doall (map #(.accept converter %) records))
    (timbre/info (str "Finished conversion of file " target-file))
    {"file"                                  (.getAbsolutePath target-file)
     (.getTypeName ArincAirport)             (.arincAirports converter)
     (.getTypeName ArincRunway)              (.arincRunways converter)
     (.getTypeName ArincLocalizerGlideSlope) (.arincLocalizerGlideSlopes converter)
     (.getTypeName ArincAirwayLeg)           (.arincAirwayLegs converter)
     (.getTypeName ArincProcedureLeg)        (.arincProcedureLegs converter)
     (.getTypeName ArincNdbNavaid)           (.arincNdbNavaids converter)
     (.getTypeName ArincVhfNavaid)           (.arincVhfNavaids converter)
     (.getTypeName ArincWaypoint)            (.arincWaypoints converter)}))

(defn load-cycle-from-file
  "Attempts to load the requested cycle of infrastructure from disk.
  <br>
  If the target file doesn't exist or can't be looked up this class will return a cycle of empty data."
  [^String requested-cycle]
  (let [[^String target-cycle ^File target-file] (nearest-available requested-cycle)]
    (timbre/info (str "Attempting to load requested cycle: " requested-cycle))
    (timbre/info (str "Targeting cycle " target-cycle " and file " target-file ", load initializing."))
    (if (= nil target-cycle)
      {"file"                                  "none"
       (.getTypeName ArincAirport)             []
       (.getTypeName ArincRunway)              []
       (.getTypeName ArincLocalizerGlideSlope) []
       (.getTypeName ArincAirwayLeg)           []
       (.getTypeName ArincProcedureLeg)        []
       (.getTypeName ArincNdbNavaid)           []
       (.getTypeName ArincVhfNavaid)           []
       (.getTypeName ArincWaypoint)            []}
      (load-from-file target-file))))
