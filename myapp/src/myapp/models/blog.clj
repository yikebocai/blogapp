;;load markdown blog files 
(ns myapp.models.blog
   (:use clojure.java.io)
   (:require 
    [myapp.util :as util]
    [myapp.models.db :as db] 
    [myapp.models.config :as config]
    ))

;;define blog object
(defrecord blog [postdate title content])

(defn read-title [filename]
	(with-open [rdr (reader filename)]
		(first (line-seq rdr))))

;;second line
;;Tag:java,jvm
(defn read-tags [filename]
  (with-open [rdr (reader filename)]
    (let [tagline (nth (line-seq rdr) 2)]
      (if (.startsWith (.toLowerCase tagline) "tag")
        (.split (.substring tagline 4) ",")))))

(defn read-content [filename]
	(with-open [rdr (reader filename)]
		(reduce str (doall (line-seq rdr)))))

;;list all blog source files
(defn list-blog-sources [path]
  (let [myfile (file path)]
  	(if-not (.isDirectory myfile ) 
  	"You should pass a directory path"
  	;sort by posting date
  	(sort-by 
  		#(java.lang.Integer/parseInt (first(clojure.string/split % #"-")))
  		> 
  		(filter #(.endsWith % ".md") (.list myfile)))))
)

;;parse date and title ,20130426-darktime_note.md
;;the first line of the content is title
(defn load-blog-list [path]
	(let [blognames (list-blog-sources path)
		  cnt (count blognames)]
      (loop [bloglist []
      	     i 0]
      	     (if (< i cnt) 
      	     	(recur 
      	     		(conj bloglist 
                  (let [blogname   (nth blognames i)
                        blogpath (str path blogname)]
      							(conj {}
      			  				{:postdate (first(clojure.string/split blogname #"-"))}
                      {:name blogname}
      			  				{:title (read-title blogpath)}
                      {:tags (read-tags blogpath)}
      			  				{:content (util/md->html blogpath)}
      			  				)))
      	     		(inc i) )
      	     bloglist))))

;;load blog content by blog name
(defn load-blog-content [name]
  (let [filename  (:name (first (db/find-blog-by-name name)))
        postdate0 (first(clojure.string/split filename #"-"))
        path (config/get-src-path)
        tags (read-tags (str path filename))]
    (conj {} 
      {:postdate (str (.substring postdate0 0 4) "/" (.substring postdate0 4 6) "/" (.substring postdate0 6))}
      {:title (read-title (str path filename))}
      {:tags tags}
      {:hastag (if (> (count tags) 0) true)}
      {:content 
        ;remove the title
        (let [html (util/md->html  (str path filename))
          offset (+ (.indexOf html "</p>") 4)
          substr (.substring html offset)
          offset2 (+ (.indexOf substr "</p>" 4))
          substr2 (.substring substr offset2)]
          (if (.startsWith (.toLowerCase substr) "<p>tag")
            (str substr2)
            (str substr)))})
    )
  )




