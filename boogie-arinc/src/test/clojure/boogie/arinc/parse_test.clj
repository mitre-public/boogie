(ns boogie.arinc.parse-test
  (:require [clojure.test :refer :all]
            [boogie.arinc.parse :refer [arinc->pojo]])
  (:import (org.mitre.tdp.boogie.arinc.model ArincAirport)))

(deftest test-parse-supported-record
  (is (= (.getTypeName ArincAirport) ((comp first keys) (arinc->pojo "SUSAP KJFKK6AJFK     0     145YHN40382374W073464329W013000013         1800018000C    MNAR    JOHN F KENNEDY INTL           224081912")))))

(deftest test-parse-unsupported-record
  (is (= "unsupported" ((comp first keys) (arinc->pojo "SUSAD KJFKK6 IHIQ  K6011090 ITWN                   IHIQN40374382W073464058W0130000240     NARJOHN F KENNEDY INTL           241981808")))))