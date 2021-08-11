(ns boogie.arinc.cycles-test
  (:require [clojure.test :refer :all]
            [boogie.arinc.cycles :refer [cycle-file find-available-files file-locator nearest-available]]
            [boogie.environment-test :refer [setup-and-teardown test-file-subpath]])
  (:import (java.io File)))

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
  (is (= ["2001" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "2001")] [cycle (.getName file)]))
      "The nearest available cycle to one we know is available is that cycle itself.")
  (is (= ["1701" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "1701")] [cycle (.getName file)]))
      "The nearest available cycle to one we know is available is that cycle itself.")
  (is (= ["1413" "kjfk-and-friends.txt"] (let [[^String cycle ^File file] (nearest-available "1201")] [cycle (.getName file)]))
      "The nearest available cycle to one we know isn't available is the closest in time to the requested cycle time."))