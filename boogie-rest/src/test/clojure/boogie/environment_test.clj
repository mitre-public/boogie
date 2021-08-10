(ns boogie.environment-test
  (:require [clojure.test :refer :all]
            [boogie.arinc.cycles :refer [file-locator get-available-cycles]])
  (:import (org.mitre.tdp.boogie.arinc PatternBasedFileLocator)
           (java.nio.file Paths)))

;; this is kind-of a static block used across all of the unit tests
(defonce test-file-subpath (atom "boogie-arinc/src/test/resources/kjfk-and-friends.txt"))

(with-test
  (defn test-file-path
    "Return the absolute path to the testing data file in the boogie-arinc module so it can be used here for testing.
    <br>
    Note this is annoying to get working both within intellij and from CLI as the working directory during the test execution is
    different between the two - so we add some hacky path resolution to get us between the two.
    <br>
    Intellij: .../boogie/boogie-rest/src/test VS CLI: .../boogie/boogie-rest
    <br>
    There is probably some weird Intellij setting to fix this but for now this will work with no additional configuration on the
    developer/environment side."
    []
    (let [base (Paths/get (clojure.string/replace (System/getProperty "user.dir") #"rest" "arinc") (into-array String []))]
      (-> (if (.endsWith base "src/test") base (.resolve base "src/test"))
          (.resolve "resources/kjfk-and-friends.txt")
          (.toString))))
  (is (clojure.string/includes? (test-file-path) @test-file-subpath)))

(defn setup-and-teardown
  "Performs the setup and teardown of the namespace prior to running the tests.
  This function should be configured in all of the Boogie test namespaces prior to running tests: (use-fixtures :once setup-and-teardown)"
  [f]
  (let [default-locator @file-locator]
    (swap! file-locator (fn [loc] (new PatternBasedFileLocator (test-file-path))))
    ;; make sure we update the available files states now that we've update the locator path
    (get-available-cycles)
    (f)
    (swap! file-locator (fn [loc] default-locator))))
