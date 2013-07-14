(ns myapp.util
  (:use [noir.request])
  (:require [markdown.core :as md]
            [noir.session :as session]
            [jsoup.soup :as soup]))

(defn format-time
  "formats the time using SimpleDateFormat, the default format is
   \"dd MMM, yyyy\" and a custom one can be passed in as the second argument"
  ([time] (format-time time "dd MMM, yyyy"))
  ([time fmt]
    (.format (new java.text.SimpleDateFormat fmt) time)))

(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (slurp filename)
    (md/md-to-html-string)))

(defn encrypt [algorithm orig]
  (let [inst (java.security.MessageDigest/getInstance algorithm)
        size (* 2 (.getDigestLength inst))
        raw (.digest inst (.getBytes orig))
        sig (.toString (java.math.BigInteger. 1 raw) 16)
        padding (apply str (repeat (- size (count sig)) "0"))
        ]
    (str padding sig)))

(defn md5 [orig]
  (encrypt "MD5" orig))

(defn islogin []
  (let [islgn (not (nil? (session/get :username )))]
    (do
      (println "islogin:" islgn)
      (true? islgn))))

(defn get-header [header]
  (nth (nth (filter #(.contains % header) (:headers *request*)) 0) 1))

(defn strip-html-tags
  "Function strips HTML tags from string."
  [s]
  (.text (soup/parse s)))


