(ns myapp.models.search
  (:require [clucy.core :as clucy]
            [taoensso.timbre :as timbre]))

(def index (clucy/memory-index))

(defn build-index [name title content]
  (do
    (timbre/info (str "build index: " title))
    (clucy/delete index {:name name})
    (clucy/delete index {:title title})
    (clucy/delete index {:content content})
    (clucy/add index {:name name
                      :title title
                      :content content})))

(defn search [keyword]
  (do
    (timbre/debug (str "search for :" keyword))
    (clucy/search index keyword 20)))