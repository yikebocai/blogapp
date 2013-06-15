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
  "create blog table,save blog related info,like title,postdate etc"
  (sql/with-connection
    db-spec
    (sql/create-table
      :blog
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp] 
      [:name "varchar(100)"]    ;md file name
      [:title "varchar(100)"]   ;blog title
      [:postdate "INTEGER"]     ;blog post date
      [:pageview "INTEGER"]
      [:vote "INTEGER"] 
      [:share "INTEGER"]
      )
    (sql/do-commands
      "CREATE INDEX postdate_index ON blog (postdate)"
      "CREATE INDEX name_index ON blog (name)"
      "CREATE INDEX vote_index ON blog (vote)")
    ))


(defn create-config-table []
  "create config table,save system info,for example:path,url,email etc,KV structure"
  (sql/with-connection
    db-spec
    (sql/create-table
      :config 
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp]
      [:key "varchar(100)"]
      [:value "varchar"]
      )
    (sql/do-commands
      "CREATE INDEX key_index ON config (key)")
    ))

(defn create-tables
  "creates the database tables used by the application"
  []
  (do 
    (if (create-blog-table) (timbre/info "create blog table"))
    (if (create-config-table) (timbre/info "create config table"))
    ))
