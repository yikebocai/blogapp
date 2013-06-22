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
    (fields [:tag_name :name])
    (aggregate (count :*) :count)
    (group :tag_name)))

(defn find-blog-by-tag [tagname]
  (select blog 
    (fields :id :name :title :postdate)
    (where 
      {:id [in 
      (subselect tag
      (fields :blog_id) 
      (where {:tag_name tagname}))]})))

(defn count-blog-by-tag [tagname]
  (select tag 
    (aggregate (count :*) :cnt)
    (where {:tag_name tagname})))

(defn find-tag [tagname blogid]
  (select tag 
    (where 
      {:tag_name tagname
        :blog_id blogid})))

(defn find-tag-by-blogid [blogid]
  (select tag 
    (fields :tag_name)
    (where 
      {:blog_id blogid})))

(defn update-tag [tagname blogid]
  (update tag 
    (set-fields
    {:timestamp (new java.util.Date)})
    (where 
      {:tag_name tagname
      :blog_id blogid})))

(defn insert-tag [tagname blogid]
  (insert tag 
    (values {
      :timestamp (new java.util.Date)
      :tag_name tagname 
      :blog_id blogid
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
