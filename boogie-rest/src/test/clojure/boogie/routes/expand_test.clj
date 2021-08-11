(ns boogie.routes.expand-test
  (:require [clojure.test :refer :all]
            [boogie.environment-test :refer [setup-and-teardown]]
            [boogie.routes.expand :refer [expand-route]])
  (:import (org.mitre.tdp.boogie.alg RouteSummary)))

(use-fixtures :once setup-and-teardown)

(with-test
  (defn view-equivalent [left right]
    (is (= left (->> (keys left) (map #(assoc {} % (get right %))) (reduce merge)))))
  (view-equivalent {:a 1} {:a 1 :b 2}))

(deftest test-expand-routes
  (let [summary-converter (fn [^RouteSummary route-summary] {:route               (.route route-summary)
                                                             :arrival-airport     (.arrivalAirport route-summary)
                                                             :arrival-runway      (-> (.arrivalRunway route-summary) (.orElse nil))
                                                             :arrival-fix         (-> (.arrivalFix route-summary) (.orElse nil))
                                                             :departure-airport   (.departureAirport route-summary)
                                                             :departure-runway    (-> (.departureRunway route-summary) (.orElse nil))
                                                             :departure-fix       (-> (.departureFix route-summary) (.orElse nil))
                                                             :star                (-> (.star route-summary) (.orElse nil))
                                                             :star-entry-fix      (-> (.starEntryFix route-summary) (.orElse nil))
                                                             :required-star-equip (-> (.requiredStarEquipage route-summary) (.orElse nil))
                                                             :sid                 (-> (.sid route-summary) (.orElse nil))
                                                             :sid-exit-fix        (-> (.sidExitFix route-summary) (.orElse nil))
                                                             :required-sid-equip  (-> (.requiredSidEquipage route-summary) (.orElse nil))
                                                             :approach            (-> (.approach route-summary) (.orElse nil))
                                                             :approach-entry-fix  (-> (.approachEntryFix route-summary) (.orElse nil))})]
    (view-equivalent {:route           "ASPEN.ROBER2.KJFK"
                      :arrival-airport "KJFK"
                      :arrival-fix     "PARCH"
                      :star            "ROBER2"
                      :star-entry-fix  "ASPEN"}
                     (-> (expand-route "ASPEN.ROBER2.KJFK") (.orElse nil) (.routeSummary) (.orElse nil) (summary-converter)))
    (view-equivalent {:route             "KJFK.DEEZZ5.TOWIN"
                      :departure-airport "KJFK"
                      :departure-runway  "04R"
                      :departure-fix     "DEEZZ"
                      :sid               "DEEZZ5"
                      :sid-exit-fix      "TOWIN"}
                     (-> (expand-route "KJFK.DEEZZ5.TOWIN" "04R" nil) (.orElse nil) (.routeSummary) (.orElse nil) (summary-converter)))
    (view-equivalent {:route              "ASPEN.ROBER2.KJFK"
                      :arrival-airport    "KJFK"
                      :arrival-fix        "PARCH"
                      :star               "ROBER2"
                      :star-entry-fix     "ASPEN"
                      :approach           "H22LZ"
                      :approach-entry-fix "DPK"}
                     (-> (expand-route "ASPEN.ROBER2.KJFK" nil "RW22L" "RNP,RNAV,CONV") (.orElse nil) (.routeSummary) (.orElse nil) (summary-converter)))))