(ns myapp.models.config
   (:use clojure.java.io)
   (:require  
    [myapp.models.db :as db] 
    [myapp.util :as util]
    [taoensso.timbre :as timbre]))

(defn get-value [key]
	(:value (first (db/find-config key))))

(defn set-one-config [key value]
	(if (nil? (get-value key)) 
		(do (db/insert-config key value) (timbre/debug "insert " key ":" value))
		(do (db/update-config key value) (timbre/debug "update " key ":" value))))

(defn set-config [path url period blogname email password nickname]
	(let [encryptpwd (util/md5 password)]
	(do
		(timbre/debug "set config")
		(set-one-config "path" path)
		(set-one-config "url" url)
		(set-one-config "period" period)
		(set-one-config "blogname" blogname)
		(set-one-config "email" email)
		(if-not (= password (get-value "password")) (set-one-config "password" encryptpwd))
		(set-one-config "nickname" nickname)   
		(str "OK")
		)))



(defn get-src-path []
	(let [path (get-value "path")]
		(if (.endsWith path "/") (str path "src/") (str path "/src/"))))
