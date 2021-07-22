(ns boogie.arinc.server
  (:require [boogie.arinc.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]])
  ;; required to get clojurephant to do the right things
  (:gen-class :name boogie.arinc.server.RestApi))

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

(defn stop-server! []
  (when @server
    (.stop @server)
    (reset! server nil)))

(def -main (do (taoensso.timbre/info "Starting Boogie REST Server") start-server!))