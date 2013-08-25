(ns myapp.models.search
  (:require [clucy.core :as clucy]
            [taoensso.timbre :as timbre]
            [myapp.util :as util]))

(def index (clucy/memory-index))

(defn build-index [name title content]
  (let [content (util/strip-html-tags content)]
    (do
      (timbre/info (str "build index: " title))
      (clucy/delete index {:name name})
      (clucy/delete index {:title title})
      (clucy/delete index {:content content})
      (clucy/add index {:name name
                        :title title
                        :content content}))))

(defn search [keyword]
  (do
    (timbre/debug (str "search for : " keyword))
    (let [rs (clucy/search index keyword 100
               :highlight {:field :content :pre "<b><font color=\"red\">" :post "</font></b>"}
               :default-operator :and )]
      (loop [frs []
             i 0]
        (if (< i (count rs))
          (recur
            (conj frs
              (let [one (nth rs i)]
                (conj one {:fragments (let [code (-> (meta one) :_fragments)]
                                          ;filter the html tag in source code
                                          (util/html->highlight code))})))
            (inc i))
          (filter #(not (nil? %)) frs))))))