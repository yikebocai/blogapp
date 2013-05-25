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
	(do
		(let [bloglist (loadblogs/load-blog-list (str path "/src/"))
		      len (count bloglist)]
		    (dotimes [x len]
			   (do
			   	   (let [blog (nth bloglist x)
			    		name (:name blog)
			    		title (:title blog)
			    		postdate (:postdate blog)
			    		ids (db/find-blog-by-name name)]
			    		(if (= (count ids) 1) 
			    			(db/update-blog (:id (first ids)) name title postdate)
			    			(db/post-blog name title postdate))))))
			    (str "OK")))

(defn sync-blog[path url]
	(let [gitresp (sync-github path url)]
		(if (.equals "OK" gitresp)
		(let [dbresp (sync-db path)]
			(if (.equals "OK" dbresp)
			(str "OK")
			(str "ERROR:sync db failed!<br> " dbresp)))
		(str "ERROR:sync GitHub failed!<br> " gitresp))))