;;load markdown blog files 
(ns myapp.models.loadblogs
   (:use clojure.java.io)
   (:require [myapp.util :as util] ))

;;define blog object
(defrecord blog [postdate title content])

(defn read-title [filename]
	(with-open [rdr (reader filename)]
		(first (line-seq rdr))))

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
  		(.list myfile))))
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
							(conj {}
			  				{:postdate (first(clojure.string/split (nth blognames i) #"-"))}
			  				{:title (read-title (str path "/" (nth blognames i)))}
			  				{:content (util/md->html (str "/md/" (nth blognames i)))}
			  				))
      	     		(inc i) )
      	     bloglist))))

;;load blog content by blog id
(defn load-blog-content [id]
  (let [filename  "20130428-reading_dream.md"]
    (conj {} 
      {:title (read-title (str "/Users/zxb/work/blog/src/" filename))}
      {:content 
        ;remove the title
        (let [html (util/md->html  (str "/md/" filename))]
          (.substring html (.indexOf html "</p>"))  )})
    )
  )




