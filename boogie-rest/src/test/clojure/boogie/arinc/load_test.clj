(ns boogie.arinc.load-test
  (:require [clojure.test :refer :all]
            [boogie.environment-test :refer [setup-and-teardown]]
            [boogie.arinc.load :refer [load-cycle-from-file]])
  (:import (org.mitre.tdp.boogie.arinc.model ArincAirport ArincRunway ArincLocalizerGlideSlope ArincAirwayLeg ArincProcedureLeg ArincNdbNavaid ArincVhfNavaid ArincWaypoint)
           (java.io File)))

(use-fixtures :once setup-and-teardown)

(deftest test-load-cycle
  (is (= {"file"                                  "kjfk-and-friends.txt"
          (.getTypeName ArincAirport)             358
          (.getTypeName ArincRunway)              167
          (.getTypeName ArincLocalizerGlideSlope) 61
          (.getTypeName ArincAirwayLeg)           333
          (.getTypeName ArincProcedureLeg)        6462
          (.getTypeName ArincNdbNavaid)           24
          (.getTypeName ArincVhfNavaid)           137
          (.getTypeName ArincWaypoint)            1132}
         (let [data (load-cycle-from-file "2001")]
           ;; rewrite the cache as counts instead of lists of records by re-associating the map values
           (-> data
               (assoc "file" (.getName (new File (get data "file"))))
               (assoc (.getTypeName ArincAirport) (count (get data (.getTypeName ArincAirport))))
               (assoc (.getTypeName ArincRunway) (count (get data (.getTypeName ArincRunway))))
               (assoc (.getTypeName ArincLocalizerGlideSlope) (count (get data (.getTypeName ArincLocalizerGlideSlope))))
               (assoc (.getTypeName ArincAirwayLeg) (count (get data (.getTypeName ArincAirwayLeg))))
               (assoc (.getTypeName ArincProcedureLeg) (count (get data (.getTypeName ArincProcedureLeg))))
               (assoc (.getTypeName ArincNdbNavaid) (count (get data (.getTypeName ArincNdbNavaid))))
               (assoc (.getTypeName ArincVhfNavaid) (count (get data (.getTypeName ArincVhfNavaid))))
               (assoc (.getTypeName ArincWaypoint) (count (get data (.getTypeName ArincWaypoint))))))))
  "Expect the type-based counts to be aligned between internal cycle parser and those extracted from the boogie-arinc tests.")
