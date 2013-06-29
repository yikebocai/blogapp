(ns myapp.views.layout
  (:use noir.request)
  (:require 
  	[clabango.parser :as parser]
  	[taoensso.timbre :as timbre] 
  	[noir.session :as session]
  	))

(def template-path "myapp/views/templates/")

(defn render [template & [params]]
	(let [islogin (not (nil? (session/get :username)))
		cxt (assoc (:context *request*) :islogin islogin )]
	(do 
		(timbre/debug "request:" *request*)
  		(parser/render-file 
  			(str template-path template)
                      (if (nil? params)
                      	cxt
                      	(conj params  cxt))))))

