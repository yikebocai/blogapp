(ns myapp.models.login
   (:require  
    [myapp.models.config :as config] 
    [myapp.util :as util]
    ))

(defn valid [email password]
	(let [myemail (config/get-value "email")
		mypwd (config/get-value "password")
		result (and (= myemail email) (= mypwd (util/md5 password)))]
		(do (println "valid username and password")
		(if result true false))))


