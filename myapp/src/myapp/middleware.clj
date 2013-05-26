(ns myapp.middleware 
	(:use noir.request)
	(:require 
		[myapp.models.config :as config] 
		) )


(defn set-context []
	( assoc {} 
	   :blogname (config/get-value "blogname")
       :nickname (config/get-value "nickname") 
))

(defn base-handler [handler]
	(fn [req] 
		(let [wrap-req (assoc req :context (set-context))
				  resp (handler wrap-req)]
			;(println resp)
			resp)))


