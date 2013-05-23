(ns myapp.models.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]
            [taoensso.timbre :as timbre]))

(def db-store "site")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".h2.db"))))


(defn create-blog-table []
  (sql/with-connection
    db-spec
    (sql/create-table
      :blog
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp] 
      [:name "varchar(100)"]
      [:title "varchar(100)"]
      [:postdate "INTEGER"]
      [:pageview "INTEGER"]
      [:vote "INTEGER"] 
      [:share "INTEGER"]
      )
    (sql/do-commands
      "CREATE INDEX postdate_index ON blog (postdate)"
      "CREATE INDEX vote_index ON blog (vote)")
    ))

(defn create-tables
  "creates the database tables used by the application"
  []
  (cond 
    (create-blog-table)
    (timbre/info "create blog table")))
