(ns boogie.routes.expand
  "Namespace for performing route expansion."
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:require [boogie.state :refer [get-assembled-procedures get-assembled-airways get-assembled-fixes get-assembled-airports get-route-expander]])
  (:import (org.mitre.tdp.boogie RequiredNavigationEquipage)))

(defn expand-route
  "Returns the Optional<ExpandedRoute> based on the configured infrastructure information as well as supplemental information such as
  requested arrival/departure runways and equipage preferences."
  ([route]
   (timbre/info (str "Attempting to expand route " route))
   (.apply (get-route-expander) route))
  ([route departure-runway arrival-runway]
   (timbre/info (str "Attempting to expand route " route " with arrival/departure runways " arrival-runway "/" departure-runway))
   (.apply (get-route-expander) route departure-runway arrival-runway))
  ([route departure-runway arrival-runway equipages]
   (timbre/info (str "Attempting to expand route " route " with arrival/departure runways " arrival-runway "/" departure-runway))
   (let [parsed-equipages (->> (clojure.string/split equipages #",")
                               (map #(RequiredNavigationEquipage/valueOf %))
                               (into-array RequiredNavigationEquipage))]
     (.apply (get-route-expander) route departure-runway arrival-runway parsed-equipages))))