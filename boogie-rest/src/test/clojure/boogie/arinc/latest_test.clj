(ns boogie.arinc.latest-test
  (:require [clojure.test :refer :all]
            [boogie.environment-test :refer [setup-and-teardown test-file-subpath]]
            [boogie.arinc.latest :refer [deep-merge]]))

(use-fixtures :once setup-and-teardown)

(deftest test-deep-merge
  (is {:a {:b 1 :c 1}} (deep-merge {:a {:b 1}} {:a {:c 1}})))

(deftest test-get-fix-database
  (testing "Fix Queries"
    (is (= 1 1)))
  (testing "Navaid Queries"
    (is (= 1 1))))

(deftest test-get-terminal-database)