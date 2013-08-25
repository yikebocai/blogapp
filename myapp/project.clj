(defproject myapp "0.1.5-SNAPSHOT"
  :description "a static and simple blog application built by clojure,articles from github"
  :url "http://github.com/yikebocai/blogapp"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.6.8"]
                 [compojure "1.1.5"]
                 [ring-server "0.2.8"]
                 [selmer "0.3.9"]
                 [com.taoensso/timbre "2.5.0"]
                 [com.taoensso/tower "1.5.1"]
                 [com.postspectacular/rotor "0.1.0"]
                 [markdown-clj "0.9.28"]
                 [com.h2database/h2 "1.3.170"]
                 [korma "0.3.0-RC5"]
                 [clojure-soup "0.0.1"]
                 [clj-rss "0.1.3"]
                 [clojurewerkz/quartzite "1.0.1"]
                 [clucy "0.4.0"]
                 ]
  :plugins [[lein-ring "0.8.6"]]
  :ring {:handler myapp.handler/war-handler
         :init    myapp.handler/init
         :destroy myapp.handler/destroy}
  :profiles
  {:production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.1.8"]]}}
  :min-lein-version "2.0.0")
