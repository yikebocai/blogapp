(ns myapp.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [myapp.models.schema :as schema]))

(defdb db schema/db-spec)

(declare tag)
(defentity blog
  (pk :id)
  (has-many tag))

(defentity config)
(defentity tag
  (pk :id)
  (table :tag)
  (belongs-to blog))

(defn post-blog
  [postdate name title summary]
  (insert blog 
    (values {
      :timestamp (new java.util.Date)
      :postdate postdate 
      :name name 
      :title title
      :summary summary
        })))

(defn find-blog [id]
  (select blog  
    (where {:id [= id]})))

(defn find-blog-by-name [name]
  (select blog  
    (where {:name [= name]})))

(defn update-blog [id postdate name title summary]
  (update blog 
  	(set-fields
       {:timestamp (new java.util.Date)
        :postdate postdate 
        :name name
        :title title
        :summary summary})
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

;;delete old blogs before timestamp
(defn delete-old-blogs [timestamp]
  (delete blog
    (where {:timestamp [< timestamp]})))

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

(defn group-by-tag []
  (select tag 
    (fields [:name])
    (aggregate (count :*) :count)
    (group :name)))

(defn find-blog-by-tag [tagname]
  (select blog  
    (where 
      {:id [in 
      (subselect tag
      (fields :blogid) 
      (where {:name tagname}))]})))

(defn count-blog-by-tag [tagname]
  (select tag 
    (aggregate (count :*) :cnt)
    (where {:name tagname})))

(defn find-tag [tagname blogid]
  (select tag 
    (where 
      {:name tagname
        :blogid blogid})))

(defn find-tag-by-blogid [blogid]
  (select tag 
    (fields :name)
    (where 
      {:blogid blogid})))

(defn update-tag [tagname blogid]
  (update tag 
    (set-fields
    {:timestamp (new java.util.Date)})
    (where 
      {:name tagname
      :blogid blogid})))

(defn insert-tag [tagname blogid]
  (insert tag 
    (values {
      :timestamp (new java.util.Date)
      :name tagname 
      :blogid blogid
      })))

(defn update-tags [tags blogid]
  (if-not (and (nil? tags) (empty? tags))
    (let [cnt (count tags)]
      (dotimes [x cnt]
        (let [tagname (nth tags x)
          rs (find-tag tagname blogid)]
          (if (= 1 (count rs))
            (update-tag tagname blogid)
            (insert-tag tagname blogid)))))))

(defn delete-old-tags [timestamp]
  (delete tag
    (where {:timestamp [< timestamp]})))
