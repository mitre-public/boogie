(ns boogie.arinc.index-test
  (:require [clojure.test :refer :all]
            [boogie.environment-test :refer [setup-and-teardown]]
            [boogie.arinc.index :refer [deep-merge airports runways localizers waypoints ndb-navaids vhf-navaids procedure-legs terminal-areas]]
            [taoensso.timbre :as timbre])
  (:import (org.mitre.tdp.boogie.arinc.model ArincAirport ArincRunway ArincLocalizerGlideSlope ArincWaypoint ArincNdbNavaid ArincVhfNavaid ArincProcedureLeg)))

(use-fixtures :once setup-and-teardown)

(deftest test-deep-merge
  (is (= {:a {:b 1}}
         (deep-merge {} {:a {:b 1}})))
  (is (= {:a {:b 1 :c 1}}
         (deep-merge {:a {:b 1}} {:a {:c 1}})))
  (is (= {:a {:b 1 :c 1}}
         (deep-merge {} {:a {:b 1}} {:a {:c 1}})))
  (is (= {:a {:b 1 :c 1}}
         (reduce deep-merge {} [{:a {:b 1}} {:a {:c 1}}])))
  (is (= {:a {:b 1}
          :c {:d 1}}
         (reduce deep-merge {} [{:a {:b 1}} {:c {:d 1}}])))
  (is (= {:a {:b 1}
          :c {:b 2}}
         (reduce deep-merge {} [{:a {:b 1}} {:c {:b 2}}]))))

(deftest test-airports
  (let [converter (fn [^ArincAirport airport] {:identifier (.airportIdentifier airport)
                                               :name       (.orElse (.airportFullName airport) nil)
                                               :region     (.airportIcaoRegion airport)})]
    (is (= {"KJFK" {:identifier "KJFK"
                    :name       "JOHN F KENNEDY INTL"
                    :region     "K6"}}
           (->> (airports "KJFK") (map #(assoc {} (key %) (converter (val %)))) (reduce merge))))
    (is (= {"KJFK" {:identifier "KJFK"
                    :name       "JOHN F KENNEDY INTL"
                    :region     "K6"}
            "PAPG" {:identifier "PAPG"
                    :name       "PETERSBURG JAMES A JOHNSON"
                    :region     "PA"}}
           (->> (airports "KJFK,PAPG") (map #(assoc {} (key %) (converter (val %)))) (reduce merge)))))
  )

(deftest test-runways
  (let [converter (fn [^ArincRunway runway] {:identifier       (.runwayIdentifier runway)
                                             :airport          (.airportIdentifier runway)
                                             :length           (.orElse (.runwayLength runway) nil)
                                             :magnetic-bearing (.orElse (.runwayMagneticBearing runway) nil)})
        rwys (runways "KJFK")]
    (is (= {:identifier       "RW04R"
            :airport          "KJFK"
            :length           8400
            :magnetic-bearing 44.}
           (-> rwys (get "KJFK") (get "RW04R") (converter))))
    (is (= {:identifier       "RW04L"
            :airport          "KJFK"
            :length           12079
            :magnetic-bearing 44.}
           (-> rwys (get "KJFK") (get "RW04L") (converter))))
    (is (= {:identifier       "RW13R"
            :airport          "KJFK"
            :length           14511
            :magnetic-bearing 134.}
           (-> rwys (get "KJFK") (get "RW13R") (converter))))
    (is (= {:identifier       "RW13L"
            :airport          "KJFK"
            :length           10000
            :magnetic-bearing 134.}
           (-> rwys (get "KJFK") (get "RW13L") (converter))))
    (is (= {:identifier       "RW22L"
            :airport          "KJFK"
            :length           8400
            :magnetic-bearing 224.}
           (-> rwys (get "KJFK") (get "RW22L") (converter))))
    (is (= {:identifier       "RW31L"
            :airport          "KJFK"
            :length           14511
            :magnetic-bearing 314.}
           (-> rwys (get "KJFK") (get "RW31L") (converter))))))

(deftest test-localizers
  (let [converter (fn [^ArincLocalizerGlideSlope localizer] {:identifier (.localizerIdentifier localizer)
                                                             :category   (.orElse (.ilsMlsGlsCategory localizer) nil)
                                                             :frequency  (.orElse (.localizerFrequency localizer) nil)})
        lcls (localizers "KJFK")]
    (is {:identifier "IIWY" :category "3" :frequency 110.9} (-> lcls (get "KJFK") (get "IIWY") (converter)))
    (is {:identifier "IJOC" :category "1" :frequency 109.5} (-> lcls (get "KJFK") (get "IJOC") (converter)))))

(deftest test-waypoints
  (let [converter (fn [^ArincWaypoint waypoint] {:identifier (.waypointIdentifier waypoint)
                                                 :latitude   (.latitude waypoint)
                                                 :longitude  (.longitude waypoint)})
        wpts (waypoints "SKORR,CAXUN")]
    (is (= {:identifier "SKORR" :latitude 40.60998888888889 :longitude -73.89711944444446} (-> wpts (get "SKORR") (get "K6") (converter))))
    (is (= {:identifier "CAXUN" :latitude 40.69476944444444 :longitude -73.87199444444444} (-> wpts (get "CAXUN") (get "K6") (converter))))))

(deftest test-navaids
  (let [ndb-converter (fn [^ArincNdbNavaid navaid] {:identifier   (.ndbIdentifier navaid)
                                                    :airport      (.orElse (.airportIdentifier navaid) nil)
                                                    :navaid-class (.orElse (.navaidClass navaid) nil)})
        vhf-converter (fn [^ArincVhfNavaid navaid] {:identifier      (.vhfIdentifier navaid)
                                                    :airport         (.orElse (.airportIdentifier navaid) nil)
                                                    :figure-of-merit (.orElse (.figureOfMerit navaid) nil)})
        ndbs (ndb-navaids "LGA,SIE")
        vhfs (vhf-navaids "LGA,SIE")]

    (is (= {:identifier "LGA" :airport nil :figure-of-merit 1} (-> vhfs (get "LGA") (get "K6") (vhf-converter))))
    (is (= {:identifier "SIE" :airport nil :figure-of-merit 2} (-> vhfs (get "SIE") (get "K6") (vhf-converter))))))

(deftest test-procedure-legs
  (let [leg-converter (fn [^ArincProcedureLeg procedure-leg
                           ] {:associated-fix  (-> (.fixIdentifier procedure-leg) (.orElse nil))
                              :path-terminator (-> (.pathTerm procedure-leg) (.name))
                              :sequence-number (-> (.sequenceNumber procedure-leg))})
        procedure-legs (procedure-legs "DEEZZ5,I22L" "KJFK")]
    (is (= #{"DEEZZ5" "I22L"} (into #{} (keys procedure-legs))) "Both procedures exist at KJFK and so both should be in the return.")
    ; :eyes:
    (is (= #{"ALL" "CANDR" "TOWIN" "RW04B" "RW13R" "RW13L" "RW22B" "RW31R" "RW31L"} (into #{} (-> procedure-legs (get "DEEZZ5") (keys)))) "DEEZZ5 should contain the expected transitions.")
    ; :eyes:
    (is (= #{"ALL" "DPK" "CIMBL" "NRTON"} (into #{} (-> procedure-legs (get "I22L") (keys)))) "I22L should contain the expected transitions.")
    ; :eyes:
    (is (= [{:associated-fix nil :path-terminator "VA" :sequence-number 10}
            {:associated-fix nil :path-terminator "VM" :sequence-number 20}
            {:associated-fix "DEEZZ" :path-terminator "DF" :sequence-number 30}]
           (->> (-> procedure-legs (get "DEEZZ5") (get "RW04B")) (map leg-converter)))
        "The RW04B transition of DEEZZ5 should contain these legs and they should be in sequence number order.")))

(deftest test-terminal-areas
  (is (map? (terminal-areas "KJFK,PAPG")))
  (is (= #{"KJFK" "PAPG"} (into #{} (keys (terminal-areas "KJFK,PAPG")))) "Records exist for both airport and so both should be present in the terminal area query return."))