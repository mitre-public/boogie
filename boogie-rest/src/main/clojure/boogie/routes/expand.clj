(ns boogie.routes.expand
  "Namespace for performing route expansion."
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:require [boogie.arinc.cycles :refer [current-cycle is-cached?]])
  (:require [boogie.routes.assemble :refer [get-procedures get-airways get-fixes get-airports]])
  (:import (org.mitre.tdp.boogie.alg RouteExpanderFactory RouteExpander)
           (org.mitre.tdp.boogie RequiredNavigationEquipage)))

(defn new-route-expander
  "Instantiates a new RouteExpander based on the provided collection(s) of converted Fixes/Airports/Airways/Procedures."
  []
  (let [airports (->> (get-airports) (mapcat val))
        fixes (->> (get-fixes) (mapcat val))
        airways (->> (get-airways) (mapcat val))
        procedures (->> (get-procedures) (mapcat #(->> (val %) (mapcat val))))]
    (timbre/info (str "Initializing new RouteExpander.\n"
                      " Airports: " (count airports) "\n"
                      " Fixes: " (count fixes) "\n"
                      " Airways: " (count airways) "\n"
                      " Procedures: " (count procedures) "\n"))
    (RouteExpanderFactory/newGraphicalRouteExpander fixes airways airports procedures)))

(defonce ^RouteExpander route-expander (atom (new-route-expander)))

(defn initialize-route-expander [] (swap! route-expander (fn [ex] (new-route-expander))))

(defn get-route-expander [] (if (is-cached? (current-cycle)) @route-expander (initialize-route-expander)))

(defn expand-route
  "Returns the Optional<ExpandedRoute> based on the configured infrastructure information as well as supplemental information such as
  requested arrival/departure runways and equipage preferences."
  ([route]
   (.apply @route-expander route))
  ([route departure-runway arrival-runway]
   (.apply @route-expander route departure-runway arrival-runway))
  ([route departure-runway arrival-runway equipages]
   (let [parsed-equipages (->> (clojure.string/split equipages #",")
                               (map #(RequiredNavigationEquipage/valueOf %))
                               (into-array RequiredNavigationEquipage))]
     (.apply @route-expander route departure-runway arrival-runway parsed-equipages))))