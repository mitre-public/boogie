(ns boogie.arinc.routes
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
  (:require [boogie.arinc.cycles :refer [cycle-file get-cycle-data get-available-cycles]])
  ;; java dependencies
  (:import (org.mitre.tdp.boogie.arinc ArincRecord)
           (org.mitre.tdp.boogie.arinc ArincVersion PatternBasedFileLocator ArincFileStore)
           (org.apache.commons.lang3.exception ExceptionUtils)
           (java.time Instant)
           (org.mitre.tdp.boogie.arinc.utils AiracCycle)))

(s/def ::cycle string?)

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
      ["/boogie-arinc"
       ["/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "Boogie REST API for Navigation Data Access"
                                :description "Rest endpoint(s) for navigation data queries."}}
               :handler (swagger/create-swagger-handler)}}]
       ;["/startup-probe"] - may want to add one of these to pre-cache at least the latest cycles of navigation data
       ["/file"
        {:get {:summary    "Returns the path to the file that would be loaded given the provided cycle."
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
                           (-> (get-available-cycles)
                               (cheshire.core/generate-string)
                               (response)
                               (content-type "text/json")))}}]
       ["/latest-cycle"
        {:get {:summary  "Returns all of the supported and assembled ARINC data types for the <i>current</i> cycle as a JSON mapping."
               :response {200 {:body string?}}
               :handler  (fn [_] (get-cycle-data (.cycle (new AiracCycle (Instant/now)))))}}]
       ["/full-cycle"
        {:get {:summary    "Return all of the supported and assembled ARINC data types for a given cycle as a JSON mapping."
               :parameters {:query (s/keys :req-un [::cycle])}
               :responses  {200 {:body any?}}
               :handler    (fn [{{{:keys [cycle]} :query} :parameters}] (get-cycle-data cycle))}}]
       ["/parse-record"
        {:get {:summary  "Returns the partially decomposed and parsed verion of the provided ARINC record string."
               :response {200 {:body string?}}
               :handler  (fn [{{{:keys [record]} :query} :parameters}])}}]]
      {:exception pretty/exception
       ;; I don't know what any of this does and I don't want to find out - ask @aeckstein if anyone has questions or do some googling
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
        {:path   "/boogie-arinc/"
         :url    "/boogie-arinc/swagger.json"
         :config {:validatorUrl     nil
                  :operationsSorter "alpha"}})
      (ring/create-default-handler))))