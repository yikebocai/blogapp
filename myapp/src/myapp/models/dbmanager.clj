(ns myapp.models.dbmanager
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [myapp.models.schema :as schema]
            [myapp.models.db :as db]))

;(defdb db schema/db-spec)

;;because db was inited only once,
;;if you add a new table after first initiation
;;you can init it from dbmanager page
(defn init-table [tablename]
  (case tablename
    "blog" (schema/create-blog-table)
    "config" (schema/create-config-table)
    "tag" (schema/create-tag-table)
    (str "No the " tablename)))

;;show table content
(defn show-table [tablename]
  (select tablename))