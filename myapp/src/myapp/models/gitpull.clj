(ns myapp.models.gitpull
	(:use [clojure.java.shell :only [sh]])
	(:require 
		[myapp.models.loadblogs :as loadblogs]
		[myapp.models.db :as db]))

(defn sync-github[path url]
	(let [response (sh  "./sync.sh" path  url)
		output (:out response)]
			(last (clojure.string/split-lines output)))
)

;;sync database
(defn sync-db[path]
	(let [bloglist (loadblogs/load-blog-list (str path "/src/"))
		len (count bloglist)]
		(dotimes [x len]
			   (cond
			   	   (let [blog (nth bloglist x)
			    		name (:name blog)
			    		postdate (:postdate blog)
			    		ids (db/find-blog name)]
			    		(if (= (count ids) 1) 
			    			(db/update-blog (first ids) name postdate)
			    			(db/post-blog name postdate)))
			    (str "OK")))))

(defn sync-blog[path url]
	(if (.equals "OK" (sync-github path url))
		(if (.equals "OK" (sync-db path))
			(str "OK")
			(str "ERROR:sync db failed!"))
		(str "ERROR:sync GitHub failed!")))