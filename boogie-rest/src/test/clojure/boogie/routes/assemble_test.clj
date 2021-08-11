(ns boogie.routes.assemble-test
  (:require [clojure.test :refer :all]
            [boogie.environment-test :refer [setup-and-teardown test-file-subpath]]
            [boogie.routes.assemble :refer [procedures-by-identifier airways-by-identifier fixes-by-identifier airports-by-identifier]]
            [taoensso.timbre :as timbre]))

(use-fixtures :once setup-and-teardown)

(deftest test-fixes-by-identifier
  (let [fixes (fixes-by-identifier "SKORR,CAXUN")]
    (is (contains? fixes "SKORR"))
    (is (contains? fixes "CAXUN"))))

(deftest test-airports-by-identifier
  (let [airports (airports-by-identifier "KJFK,PAPG")]
    (is (contains? airports "KJFK"))
    (is (contains? airports "PAPG"))))

(deftest test-procedures-by-identifier
  (let [procedures (procedures-by-identifier "DEEZZ5,ROBBR2")
        procedures-at-jfk (procedures-by-identifier "DEEZZ5,ROBBR2" "KJFK")]

    (is (= #{"KJFK" "KFRG"} (->> procedures (mapcat (comp keys val)) (into #{}))) "Query without airport restriction should return multiple airports.")

    (is (= #{"KJFK"} (->> procedures-at-jfk (mapcat (comp keys val)) (into #{}))) "Query with KJFK restriction should only return KJFK.")

    (is (= #{"DEEZZ5" "ROBBR2"} (->> procedures (keys) (into #{}))) "Query should return both procedures as both exist in the database.")))
