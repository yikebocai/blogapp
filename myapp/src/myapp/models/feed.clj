(ns myapp.models.feed 
	(:require 
		[clj-rss.core :as rss]
		[myapp.util :as util]
		[myapp.models.blog :as blog]
		[myapp.models.config :as config]))

;;convert string 'yyyy/mm/dd' to a Date object
(defn- toDate [strDate]
	(let [arr (.split strDate "/")
		year (java.lang.Integer. (first arr))
		month (java.lang.Integer. (nth arr 1))
		day (java.lang.Integer. (nth arr 2))]
	(java.util.Date. year month day)))

(defn create-feeds []
(apply rss/channel-xml 
	(let [summarys (blog/list-blog-summary)
		  cnt (count summarys)
		  homepage (str "http://" (util/get-header "host"))
		  t1 (println "cnt:" cnt)]
		(loop [feeds [{:title (config/get-value "blogname")
						:link homepage
						:description (str (config/get-value "nickname") "'s blog")
						:copyright (config/get-value "email")}]
			   i 0]
				(if (< i cnt)
					(recur 
						(conj feeds 
							(let [sum (nth summarys i)]
							(conj {} 
								{:title (:title sum)} 
						  		{:link (str homepage "/blog?p=" (:name sum))}
						  		{:pubDate (toDate (:postdate sum))}
						  		{:description (:summary sum)})))
						(inc i))
						feeds)))))