(ns boogie.arinc.routes
  ;; clojure dependencies
  (:require [clojure.datafy :refer [datafy]]
            [clojure.java.io])
  (:require [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [spec-tools.core :as st]
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
  ;; java dependencies
  (:import (org.mitre.tdp.boogie.arinc ArincRecord)
           (org.mitre.tdp.boogie.arinc ArincVersion PatternBasedFileLocator ArincFileStore))
  ;; required for clojurephant to work
  (:gen-class))

(s/def ::cycle string?)

;; The instance of the file-locator which will be used to locate ARINC424 files on a visible filesystem for the application to
;; load and serve data from
;; This will use a provided file path template if provided as an ENV variable at start-up, otherwise will default to the MITRE
;; LIDO archive
(defonce file-locator (atom (if (System/getenv "FILE_LOCATOR_PATH")
                              (new PatternBasedFileLocator (System/getenv "FILE_LOCATOR_PATH"))
                              (ArincFileStore/MITRE_LIDO))))

(def app-routes
  (ring/ring-handler
    (ring/router
      ["/boogie-arinc"
       ["/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "Boogie REST API for Navigation Data Access"
                                :description "Rest endpoints for navigation data queries and route expansion."}}
               :handler (swagger/create-swagger-handler)}}]
       ;["/startup-probe"] - may want to add one of these to pre-cache at least the latest cycles of navigation data
       ["/file"
        {:get {:summary    "Returns the path to the file that would be loaded given the provided cycle."
               :parameters {:query (s/keys :req-un [::cycle])}
               :responses  {200 {:body any?}}
               :handler    (fn [{{{:keys [cycle]} :query} :parameters}]
                             (.apply file-locator cycle))}}]
       ["/full-cycle"
        {:get {:summary    "Return all of the supported and assembled ARINC data types for a given cycle as a JSON mapping."
               :parameters {:query (s/keys :req-un [::cycle])}
               :responses  {200 {:body any?}}
               :handler    (fn [{{{:keys [cycle]} :query} :parameters}])}}]])))