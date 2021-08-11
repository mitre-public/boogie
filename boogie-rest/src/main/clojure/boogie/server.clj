(ns boogie.server
  (:require [boogie.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [boogie.state :refer [initialize-application-state]]
            [taoensso.timbre :as timbre])
  ;; required to get clojurephant to manifest this as a the main class
  (:gen-class))

(defonce server (atom nil))

(defn start-server! [& [port]]
  (let [handler app-routes
        config {:port  (cond
                         (integer? port) port
                         (string? port) (Integer/parseInt port)
                         (nil? port) 8087
                         :else 8087)
                :join? false}]
    (reset! server (run-jetty handler config))))

(defn stop-server! [] (when @server (.stop @server) (reset! server nil)))

(def -main (do (timbre/info "Starting Boogie REST Server") (initialize-application-state) start-server!))