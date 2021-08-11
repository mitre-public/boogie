(ns boogie.server
  (:require [boogie.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [boogie.arinc.cycles :refer [current-cycle get-cycle-data]]
            [boogie.arinc.latest :refer [re-initialize-fix-database re-initialize-terminal-database]]
            [boogie.routes.assemble :refer [re-initialize-procedures re-initialize-airways re-initialize-fixes re-initialize-airports]]
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

(defn initialize-backend-resources
  "Initialize the initial caches and parses of the backend resources across the routes/arinc modules."
  []
  ;; start the server and then attempt to pre-index the navigational data in boogie.arinc.cycles with the current cycle of nav data
  (timbre/info (str "Attempting to pre-index the LRU cache with the latest cycle of navigational data (cycle " (current-cycle) ")."))
  (get-cycle-data (current-cycle))

  ;; initialize the databases on top of the parsed ARINC pojos
  (timbre/info (str "Initializing the fix and terminal area databases."))
  (re-initialize-fix-database) (re-initialize-terminal-database)

  ;; initialize the assembled fixes/airports/airways/procedures
  (taoensso.timbre/info "Initializing assembled records.")
  (re-initialize-fixes) (re-initialize-airports) (re-initialize-airways) (re-initialize-procedures))

(def -main (do (timbre/info "Starting Boogie REST Server") (initialize-backend-resources) start-server!))