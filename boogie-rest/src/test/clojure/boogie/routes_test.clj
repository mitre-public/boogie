(ns boogie.routes-test
  (:require [clojure.test :refer :all]
            [boogie.routes :refer :all]
            [clojure.spec.alpha :as s]))

(deftest test-route-specs
  (testing "Airport Specification"
    (is (s/valid? :boogie.routes/airport "KATL"))
    (is (s/valid? :boogie.routes/airport "ORD"))
    (is (s/valid? :boogie.routes/airport "02A"))
    (is (not (s/valid? :boogie.routes/airport "KATL2")) "Airport name is too long to meet the spec.")
    (is (not (s/valid? :boogie.routes/airport "KA")) "Airport name is too short to meet the spec.")
    (is (not (s/valid? :boogie.routes/airport "K#%")) "Airport name contains invalid characters."))
  (testing "Runway Specification"
    (is (s/valid? :boogie.routes/runway "RW02R"))
    (is (s/valid? :boogie.routes/arrival-runway "RW02R"))
    (is (s/valid? :boogie.routes/departure-runway "RW02R"))
    (is (s/valid? :boogie.routes/runway "RW18C"))
    (is (s/valid? :boogie.routes/runway "RW25L"))
    (is (s/valid? :boogie.routes/runway "02R"))
    (is (s/valid? :boogie.routes/runway "35W"))
    (is (not (s/valid? :boogie.routes/runway "47R")) "Runway number too high.")
    (is (not (s/valid? :boogie.routes/runway "1R")) "Runway number formatted improperly.")
    (is (not (s/valid? :boogie.routes/runway "ARRIVAL")) "Not a valid runway ID (no numeric component)."))
  (testing "Procedure Specification"
    (is (s/valid? :boogie.routes/procedure "HOBTT2"))
    (is (s/valid? :boogie.routes/procedure "H21-Z"))
    (is (s/valid? :boogie.routes/procedure "PAD4"))
    (is (not (s/valid? :boogie.routes/procedure "PROCEDURE")) "Procedure name too long.")
    (is (not (s/valid? :boogie.routes/procedure "PR")) "Procedure name too short."))
  (testing "CSV Specification"
    (is (s/valid? :boogie.routes/csv "ABC,DEF"))
    (is (s/valid? :boogie.routes/csv "ABC,DEF,GHI,JKL"))
    (is (not (s/valid? :boogie.routes/csv "ABC,DEF,"))))
  (testing "Airports Specification"
    (is (s/valid? :boogie.routes/airports "KATL"))
    (is (s/valid? :boogie.routes/airports "KATL,KORD,PHNL,MCO"))
    (is (not (s/valid? :boogie.routes/airports "KATL,")))
    (is (not (s/valid? :boogie.routes/airports ""))))
  (testing "Procedures Specification"
    (is (s/valid? :boogie.routes/procedures "HOBTT2"))
    (is (s/valid? :boogie.routes/procedures "HOBTT2,ROBER2,CUTTR1"))
    (is (not (s/valid? :boogie.routes/procedures "HOBTT2,")))
    (is (not (s/valid? :boogie.routes/procedures ""))))
  (testing "Route Specification")
  (testing "Equipage Specification"
    (is (s/valid? :boogie.routes/equipage "RNP"))
    (is (s/valid? :boogie.routes/equipage "RNAV"))
    (is (s/valid? :boogie.routes/equipage "CONV"))
    (is (s/valid? :boogie.routes/equipage "RNP,RNAV"))
    (is (s/valid? :boogie.routes/equipage "CONV,RNAV,RNP"))
    (is (not (s/valid? :boogie.routes/equipage "CONP")) "Invalid equipage value should not be matched.")
    (is (not (s/valid? :boogie.routes/equipage "RNA")) "Invalid equipage value should not be matched.")
    (is (not (s/valid? :boogie.routes/equipage "CONV,CONV,CONV,CONV")) "Too many supplied values should not be matched.")
    (is (not (s/valid? :boogie.routes/equipage "RNP,CONV,")) "Trailing comma should not be matched.")
    (is (not (s/valid? :boogie.routes/equipage "")) "Empty string should not be matched.")))
