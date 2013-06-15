(ns myapp.models.tag 
	(:require  
		[myapp.models.db :as db]
		[taoensso.timbre :as timbre] ))

(defn list-blog-by-tag [tagname]
	(let [rs (db/find-blog-by-tag tagname)]
		()))