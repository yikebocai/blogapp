(ns myapp.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [myapp.models.schema :as schema]))

(defdb db schema/db-spec)

(defentity blog)
(defentity config)

(defn post-blog
  [name title postdate]
  (insert blog 
    (values {
      :timestamp (new java.util.Date)
    	:name name 
      :title title
      :postdate postdate 
        })))

(defn find-blog [id]
  (select blog  
    (where {:id [= id]})))

(defn find-blog-by-name [name]
  (select blog 
    (fields :id)
    (where {:name [= name]})))

(defn update-blog [id name title postdate]
  (update blog 
  	(set-fields
       {:timestamp (new java.util.Date)
        :name name
        :title title
        :postdate postdate })
        (where {:id [= id]})
        ))

(defn update-blog-stat [id pageview vote share]
  (update blog 
    (set-fields
       {:timestamp (new java.util.Date)
        :pageview pageview 
        :vote vote
        :share share })
        (where {:id [= id]})
        ))

(defn list-blog []
  (select blog 
  	(order :postdate :DESC)))

(defn insert-config [key value]
  (insert config 
    (values {
      :key key
      :value value
      :timestamp (new java.util.Date)
      })))

(defn find-config [key]
  (select config 
    (where {:key [= key]})))

(defn update-config [key value]
  (update config 
    (set-fields
      {:timestamp (new java.util.Date) 
        :value value})
    (where {:key [= key]})))
