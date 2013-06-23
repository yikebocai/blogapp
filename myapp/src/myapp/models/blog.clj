;;load markdown blog files 
(ns myapp.models.blog
   (:use clojure.java.io)
   (:require 
    [myapp.util :as util]
    [myapp.models.db :as db] 
    [myapp.models.config :as config]
    ))

(defn format-postdate [postdate]
  (str 
    (.substring postdate 0 4) "/" 
    (.substring postdate 4 6) "/" 
    (.substring postdate 6)))

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

;remove the title and tags
(defn read-content [filename]
  (let [html (util/md->html  filename) 
    offset (+ (.indexOf html "</p>") 4)
    substr (.substring html offset) 
    offset2 (+ (.indexOf substr "</p>" 4))
    substr2 (if (> offset2 4) (.substring substr offset2))]
    (if (.startsWith (.toLowerCase substr) "<p>tag")
      (str substr2)
      (str substr))))

;;read first 80 words after remove html tags
(defn read-summary [filename]
  (let [content (read-content filename) 
        txt (util/strip-html-tags content) 
        len (count txt)]
        (if (> len 80) (.substring txt 0 80) (str txt))))



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
                      {:summary (read-summary blogpath)} 
      			  				)))
      	     		(inc i) )
      	     bloglist))))

;;load blog content by blog name
(defn load-blog-content [name]
  (let [blog (first (db/find-blog-by-name name))
        filename  (:name blog)
        postdate0 (first(clojure.string/split filename #"-"))
        path (config/get-src-path)
        blogpath (str path filename)
        tags (read-tags blogpath) 
        pageview (if (nil? (:pageview blog)) 0 (:pageview blog))]
    (do 
      (db/update-blog-stat (:id blog)  (+ 1 pageview) 0 0)
      (conj {} 
      {:postdate (format-postdate postdate0)}
      {:title (read-title (str path filename))}
      {:tags tags}
      {:hastag (if (> (count tags) 0) true)}
      {:content (read-content blogpath)})
      )
    )
  )

;;home page show or tag search show
(defn list-blog-summary [& tag]
  (let [dblist (if (or (nil? tag) (empty? tag)) (db/list-blog)  (db/find-blog-by-tag tag))
       cnt (count dblist)]
     (loop [bloglist []
             i 0]
             (if (< i cnt) 
              (recur 
                (conj bloglist 
                  (let [blog  (nth dblist i)
                        t1 (println blog)
                        pageview (:pageview blog)]
                    (conj {}
                      {:postdate (format-postdate (.toString (:postdate blog)))}
                      {:name (:name blog)}
                      {:title (:title blog)}
                      {:tags (db/find-tag-by-blogid (:id blog))}
                      {:summary (do (println "summary:" (:summary blog)) (:summary blog))}
                      {:pageview (if (nil? pageview) 0 pageview)}
                      {:comment 0} ;todo
                      )))
                (inc i) )
             bloglist))))


