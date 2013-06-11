(ns myapp.models.login
   (:require  
    [myapp.models.config :as config] 
    [myapp.util :as util]
    [taoensso.timbre :as timbre] 
    [noir.session :as session]
    ))

;;if can't find email from db,use default account to valid
(defn valid [email password]
	"valid username and password from db,
	password should be encrypted ahead"
	(let [myemail (config/get-value "email")
		mypwd (config/get-value "password")
		result (if (nil? myemail) 
			(and (= email "admin") (= password "yikebocai")) 
			(and (= myemail email) (= mypwd (util/md5 password))))]
		(do 
			(timbre/info "valid username and password")
			(cond result (session/put! :username email))
			(if result true false))))

(defn signin [username password]
	(if (nil? (session/get :username))
		(valid username password)
		(do (println "valid ok from session") true)))

(defn signout [] 
	(session/put! :username nil) )