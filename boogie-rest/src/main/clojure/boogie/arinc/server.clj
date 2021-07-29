(ns boogie.arinc.server
  (:require [boogie.arinc.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [boogie.arinc.cycles :refer [current-cycle get-cycle-data]]
            [taoensso.timbre :as timbre])
  ;; required to get clojurephant to do the right things
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

(defn stop-server! []
  (when @server
    (.stop @server)
    (reset! server nil)))

(def -main (do (timbre/info "Starting Boogie REST Server")
               ;; start the server and then attempt to pre-index the navigational data in boogie.arinc.cycles with the current cycle of nav data
               (timbre/info (str "Attempting to pre-index the LRU cache with the latest cycle of navigational data (cycle " (current-cycle) ")."))
               (get-cycle-data (current-cycle))
               start-server!))