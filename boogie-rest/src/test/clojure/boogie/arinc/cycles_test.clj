(ns boogie.arinc.cycles-test
  (:require [clojure.test :refer :all]
            [boogie.arinc.cycles :refer [cycle-file find-available-files file-locator nearest-available get-available-cycles load-cycle]]
            [boogie.environment-test :refer [setup-and-teardown test-file-subpath]])
  (:import (java.io File)
           (org.mitre.tdp.boogie.arinc.model ArincAirport ArincRunway ArincLocalizerGlideSlope ArincAirwayLeg ArincProcedureLeg ArincNdbNavaid ArincVhfNavaid ArincWaypoint)))

(use-fixtures :once setup-and-teardown)

(deftest test-cycle-file
  (is (clojure.string/includes? (.getAbsolutePath (cycle-file "2001")) @test-file-subpath)
      "Cycle-file should expand the path (which is static) to just our static ARINC file."))

(deftest test-find-available-files
  (is (clojure.string/includes? (:path (get (find-available-files) "2001")) @test-file-subpath)
      "Pre-scan covers 2020, we should find our static file available for the 2020-01-01 cycle.")
  (is (not (get (find-available-files) "1201"))
      "Pre-scan is 2015-Now, available files shouldn't contain an earlier cycle."))

(deftest test-nearest-available
  (is (= ["2001" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "2001" (new File "idontexist.dat"))] [cycle (.getName file)]))
      "The nearest available cycle to one we know is available is that cycle itself.")
  (is (= ["1701" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "1701" (new File "idontexist.dat"))] [cycle (.getName file)]))
      "The nearest available cycle to one we know is available is that cycle itself.")
  (is (= ["1413" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "1201" (new File "idontexist.dat"))] [cycle (.getName file)]))
      "The nearest available cycle to one we know isn't available is the closest in time to the requested cycle time."))

(deftest test-load-cycle
  (is (= {"file"                                  "kjfk-and-friends.txt"
          "cycle"                                 "2001"
          (.getTypeName ArincAirport)             358
          (.getTypeName ArincRunway)              167
          (.getTypeName ArincLocalizerGlideSlope) 61
          (.getTypeName ArincAirwayLeg)           333
          (.getTypeName ArincProcedureLeg)        6462
          (.getTypeName ArincNdbNavaid)           24
          (.getTypeName ArincVhfNavaid)           137
          (.getTypeName ArincWaypoint)            1132}
         (let [data (load-cycle (cycle-file "2001") "2001")]
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