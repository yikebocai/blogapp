(ns myapp.models.sync
	(:use [clojure.java.shell :only [sh]])
	(:require 
		[myapp.models.blog :as blog]
		[myapp.models.db :as db]
		[taoensso.timbre :as timbre]
		[noir.io :as io] ))

(defn sync-github[path url] 
	;if path is exist
	(let [mypath (java.io.File. path)
		  gitdir (str "--git-dir=" path "/.git")
		  worktree (str "--work-tree=" path) ]
		(do 
			(if (and (.exists mypath) (.isDirectory mypath))
				(let [gitresp1 (sh "git" gitdir "fetch") 
					  gitresp2 (sh "git" gitdir worktree "merge" "origin/master")]
					(do 
						(timbre/debug "gitresp1:" gitresp1)
						(timbre/debug "gitresp2:" gitresp2)
						(let [error1 (:err gitresp1)
							  error2 (:err gitresp2)]
						  (if-not (and (empty? error1) (empty? error2))
						   (timbre/error "git pull error:" error1 error2)))))
				(let [gitresp (sh "git" "clone" url path)]
					(do 
						(timbre/debug "gitresp:" gitresp)
						(let [error (:err gitresp)]
						  (if-not (empty? error)
						   (timbre/error "git clone error:" error))))))
			 
			(let [src (str path "/src/myimg")
				  dst (str (io/resource-path) "/")
				  cpresp (sh "cp" "-rf"  src dst)
				  error (:err cpresp)]
				(do 
					(timbre/debug "myimg path:" dst)
					(if (empty? error) true false)))))
)

;;sync database
(defn sync-db[path]
	(let [timestamp (java.util.Date.)
		dbresp
		(let [bloglist (blog/load-blog-list (str path "/src/"))
		      len (count bloglist)]
		    (do
		    (dotimes [x len]
			   (do
			   	   (let [blog (nth bloglist x)
			    		postdate (:postdate blog)
			    		name (:name blog)
			    		title (:title blog)
			    		summary (:summary blog)
			    		tags (:tags blog)
			    		rs (db/find-blog-by-name name)]
			    		(if (= (count rs) 1) 
			    			(let [id (:id (first rs))
			    				resp (db/update-blog id postdate name title summary)]
			    				(do 
			    					(timbre/debug "update-blog:" name)
			    					(timbre/debug "tags:" tags)
			    					(db/update-tags tags id)))
			    			(let [resp (db/post-blog postdate name title summary)
			    				  id (first (vals resp))]
			    				(do 
			    					(timbre/debug "post-blog:" id)
			    					(db/update-tags tags id)))

			    			))))
		    (db/delete-old-blogs timestamp))
		    (db/delete-old-tags timestamp))]
			    (if (empty? dbresp) true false)))

(defn sync-blog[path url]
	(let [gitresp (sync-github path url)]
		(if (true? gitresp)
		(let [dbresp (sync-db path)]
			(if (true? dbresp)
			(str "OK")
			(str "ERROR:sync db failed!")))
		(str "ERROR:sync GitHub failed!"))))