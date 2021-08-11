(ns boogie.routes
  ;; clojure dependencies
  (:require [clojure.datafy :refer [datafy]]
            [clojure.java.io])
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [spec-tools.core :as st]
            [cheshire.core :refer :all]
            [reitit.core]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as rrc]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [muuntaja.core :as m]
            [ring.util.response :refer [response resource-response content-type redirect]]
            [ring.util.request :refer [body-string]])
  (:require [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.default-charset :refer [wrap-default-charset]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.gzip :as gzip])
  (:require [boogie.routes.assemble :refer [procedures-by-identifier airways-by-identifier fixes-by-identifier airports-by-identifier]])
  (:require [boogie.routes.expand :refer [expand-route]])
  (:require [boogie.arinc.cycles :refer [cycle-file get-cycle-data get-available-cycles current-cycle]])
  (:require [boogie.arinc.latest :refer [airports runways localizers waypoints ndb-navaids vhf-navaids procedure-legs terminal-areas]])
  (:require [boogie.arinc.parse :refer [arinc->pojo]])
  ;; java dependencies
  (:import (org.apache.commons.lang3.exception ExceptionUtils)
           (java.time Instant)
           (org.mitre.tdp.boogie.arinc.utils AiracCycle)
           (com.google.gson Gson)
           (org.mitre.tdp.boogie RequiredNavigationEquipage)))

(s/def ::cycle string?)
(s/def ::records string?)
(s/def ::identifiers string?)
(s/def ::airport string?)
(s/def ::airports string?)
(s/def ::procedures string?)
(s/def ::route string?)

(s/def ::arrival-airport string?)
(s/def ::arrival-runway string?)
(s/def ::departure-runway string?)
(s/def ::departure-airport string?)
(s/def ::equipage #{(.name (RequiredNavigationEquipage/RNP)) (.name (RequiredNavigationEquipage/RNAV)) (.name (RequiredNavigationEquipage/CONV))})

(defonce gson (atom (new Gson)))

(def exception-middleware
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {::exception/default (fn [^Exception e _]
                             {:status 500
                              :body   {:type    "exception"
                                       :class   (.getName (.getClass e))
                                       :message (.getMessage e)
                                       :stack   (ExceptionUtils/getStackTrace e)}})
       ::exception/wrap    (fn [handler e request]
                             (println "ERROR" (pr-str (:uri request)))
                             (.printStackTrace e)
                             (handler e request))})))

(def app-routes
  (ring/ring-handler
    (ring/router
      ["/boogie"
       ["/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "Boogie REST API for Navigation Data Access"
                                :description "Rest endpoint(s) for navigation data queries. Endpoints currently all return full cycles of navigational data when queried."}}
               :handler (swagger/create-swagger-handler)}}]
       ["/routes"
        ["/expand"
         {:get {:summary    "Returns the expanded route for the given flight plan route string (with some additional context)."
                :parameters {:query (s/keys :req-un [::route] :opt-un [::departure-runway ::arrival-runway ::equipage])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [route departure-runway arrival-runway equipage]} :query} :parameters}]
                              (-> (.toJson @gson (if (= nil equipage)
                                                   (expand-route route departure-runway arrival-runway)
                                                   (expand-route route departure-runway arrival-runway equipage)))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/airports"
         {:get {:summary    "Returns the composite version of Airport(s) as seen by the Boogie software."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson (airports-by-identifier identifiers))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/fixes"
         {:get {:summary    "Returns the composite version of Fix(es) (waypoint, vhf/ndb navaid) as seen by the Boogie software."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson (fixes-by-identifier identifiers))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/procedures"
         {:get {:summary    "Returns the composite version of Procedure(s) as seen by the Boogie software."
                :parameters {:query (s/keys :req-un [::identifiers] :opt-un [::airports])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [identifiers airports]} :query} :parameters}]
                              (-> (.toJson @gson (procedures-by-identifier identifiers airports))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/airways"
         {:get {:summary    "Returns the composite version of Airway(s) as seen by the Boogie software."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson (airways-by-identifier identifiers))
                                  (response)
                                  (content-type "text/json")))}}]
        ]
       ["/arinc"
        ["/file"
         {:get {:summary    "Returns the path to the resolved file that would targeted for load given the provided cycle. If this file doesn't exist the nearest
        file as outlined in the set of 'available-cycles' will be returned instead."
                :parameters {:query (s/keys :req-un [::cycle])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [cycle]} :query} :parameters}]
                              (timbre/info (str "Locating file for cycle: " cycle))
                              (let [file (cycle-file cycle)]
                                (-> {:path    (.getAbsolutePath file)
                                     :exists? (.exists file)}
                                    (cheshire.core/generate-string)
                                    (response)
                                    (content-type "text/json"))))}}]
        ["/available-cycles"
         {:get {:summary  "Returns the structured map of all available cycles and whether or not they are currently cached."
                :response {200 {:body string?}}
                :handler  (fn [_]
                            (-> (.toJson @gson (get-available-cycles))
                                (response)
                                (content-type "text/json")))}}]
        ["/latest-cycle"
         {:get {:summary  "Returns all of the supported and assembled ARINC data types for the current (most recent) cycle as a JSON mapping.
        Note if you run this from swagger you'll crash it your browser - so I would hit the endpoint directly: <host>:8087/boogie-arinc/latest-cycle?"
                :response {200 {:body string?}}
                :handler  (fn [_] (-> (.toJson @gson (get-cycle-data (current-cycle)))
                                      (response)
                                      (content-type "text/json")))}}]
        ["/full-cycle"
         {:get {:summary    "Return all of the supported and assembled ARINC data types for the provided cycle as a JSON mapping.
        Note if you run this from swagger you'll crash it your browser - so I would hit the endpoint directly: <host>:8087/boogie-arinc/full-cycle?cycle=2001"
                :parameters {:query (s/keys :req-un [::cycle])}
                :responses  {200 {:body any?}}
                :handler    (fn [{{{:keys [cycle]} :query} :parameters}]
                              (-> (.toJson @gson (get-cycle-data cycle))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/parse-records"
         {:get {:summary    "Returns the parsed and converted version of the input 424 record string(s) (CSV-delimited) as JSON.
        Not all records may be returned as some may be unsupported in the deployed version of the parsing logic. If this is the case the"
                :parameters {:query (s/keys :req-un [::records])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [records]} :query} :parameters}]
                              (-> (.toJson @gson (arinc->pojo records))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/airports"
         {:get {:summary    "Returns the ARINC airport record(s) matching the provided query parameters."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson (airports identifiers))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/runways"
         {:get {:summary    "Returns the ARINC runway record(s) at the provided airports."
                :parameters {:query (s/keys :req-un [::airports])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [airports]} :query} :parameters}]
                              (-> (.toJson @gson (runways airports))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/localizers"
         {:get {:summary    "Returns the ARINC localizer/glideslope record(s) at the provided airports."
                :parameters {:query (s/keys :req-un [::airports])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [airports]} :query} :parameters}]
                              (-> (.toJson @gson (localizers airports))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/waypoints"
         {:get {:summary    "Returns the ARINC waypoint record(s) matching the provided identifiers."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson (waypoints identifiers))
                                  (response)
                                  (content-type "text/json")))}}]
        ["/navaids"
         {:get {:summary    "Returns the ARINC vhf/ndb navaid record(s) matching the provided identifiers."
                :parameters {:query (s/keys :req-un [::identifiers])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
                              (-> (.toJson @gson {"ndb" (ndb-navaids identifiers)
                                                  "vhf" (vhf-navaids identifiers)})
                                  (response)
                                  (content-type "text/json")))}}]
        ["/procedures"
         {:get {:summary    "Returns the ARINC procedure (leg) record(s) matching the given procedure identifier at the provided airport."
                :parameters {:query (s/keys :req-un [::airport ::procedures])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [airport procedures]} :query} :parameters}]
                              (-> (.toJson @gson (procedure-legs procedures airport))
                                  (response)
                                  (content-type "text/json")))}}]
        ;["/airways"
        ; {:get {:summary    "Returns the ARINC airway (leg) record(s) matching the provided query parameters."
        ;        :parameters {:query (s/keys :req-un [::identifiers])}
        ;        :response   {200 {:body string?}}
        ;        :handler    (fn [{{{:keys [identifiers]} :query} :parameters}]
        ;                      )}}]
        ["/terminal-areas"
         {:get {:summary    "Returns all of the parsed ARINC records related to a given terminal area. (Airport, Runway, Localizers, Waypoints, Navaids, Procedure Legs)"
                :parameters {:query (s/keys :req-un [::airports])}
                :response   {200 {:body string?}}
                :handler    (fn [{{{:keys [airports]} :query} :parameters}]
                              (-> (.toJson @gson (terminal-areas airports))
                                  (response)
                                  (content-type "text/json")))}}]
        ]
       ]
      {:exception pretty/exception
       ;; I don't know what any of this does and I don't want to find out - ask @aeckstein if anyone has questions - or do some googling
       :data      {:coercion   reitit.coercion.spec/coercion
                   :muuntaja   m/instance
                   :middleware [exception-middleware
                                #(wrap-cors %
                                            :access-control-allow-origin [#"http://localhost:([0-9]){4,5}"]
                                            :access-control-allow-methods [:get :post])
                                swagger/swagger-feature
                                parameters/parameters-middleware
                                muuntaja/format-middleware
                                rrc/coerce-exceptions-middleware
                                rrc/coerce-request-middleware
                                rrc/coerce-response-middleware
                                wrap-cookies]}})
    (ring/routes
      (swagger-ui/create-swagger-ui-handler
        {:path   "/boogie/"
         :url    "/boogie/swagger.json"
         :config {:validatorUrl     nil
                  :operationsSorter "alpha"}})
      (ring/create-default-handler))))