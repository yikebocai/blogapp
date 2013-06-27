(ns myapp.models.feed 
	(:require 
		[clj-rss.core :as rss]
		[myapp.models.blog :as blog]))

(defn create-feeds2 []
(println
	(let [summarys (blog/list-blog-summary)]
		(dotimes [n (count summarys)]
			(let [sum (nth summarys n)]
				(conj {} 
					{:title (:title sum)} 
			  		{:link (:name sum)}
			  		{:description (:summary sum)}))))))

(defn create-feeds []
(rss/channel-xml {:title "Foo" :link "http://foo/bar" :description "some channel"}
                 :title "Foo"
                 :title "post" :author "author@foo.bar"
                 :description "bar"))